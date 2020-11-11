
#include <cstring>
#include <jni.h>
#include "VideoStream.h"
#include "rtmp/rtmp.h"
#include "PushInterface.h"

VideoStream::VideoStream() {
    pthread_mutex_init(&mutex, 0);
}

VideoStream::~VideoStream() {
    pthread_mutex_destroy(&mutex);
    if (videoCodec) {
        x264_encoder_close(videoCodec);
        videoCodec = 0;
    }
    if (pic_in) {
        x264_picture_clean(pic_in);
        DELETE(pic_in);
    }
}

void VideoStream::setVideoEncInfo(int width, int height, int fps, int bitrate) {
    pthread_mutex_lock(&mutex);
    mWidth = width;
    mHeight = height;
    mFps = fps;
    mBitrate = bitrate;
    ySize = width * height;
    uvSize = ySize / 4;
    if (videoCodec) {
        x264_encoder_close(videoCodec);
        videoCodec = 0;
    }
    if (pic_in) {
        x264_picture_clean(pic_in);
        DELETE(pic_in);
    }

    //setting x264 params
    x264_param_t param;
    x264_param_default_preset(&param, "ultrafast", "zerolatency");
    param.i_level_idc = 32;
    //input format
    param.i_csp = X264_CSP_I420;
    param.i_width = width;
    param.i_height = height;
    //no B frame
    param.i_bframe = 0;
    //i_rc_method:bitrate control, CQP(constant quality), CRF(constant bitrate), ABR(average bitrate)
    param.rc.i_rc_method = X264_RC_ABR;
    //bitrate(Kbps)
    param.rc.i_bitrate = bitrate / 1000;
    //max bitrate
    param.rc.i_vbv_max_bitrate = bitrate / 1000 * 1.2;
    //unit:kbps
    param.rc.i_vbv_buffer_size = bitrate / 1000;

    //frame rate
    param.i_fps_num = fps;
    param.i_fps_den = 1;
    param.i_timebase_den = param.i_fps_num;
    param.i_timebase_num = param.i_fps_den;
    //using fps
    param.b_vfr_input = 0;
    //key frame interval(GOP)
    param.i_keyint_max = fps * 2;
    //each key frame attaches sps/pps
    param.b_repeat_headers = 1;
    //thread number
    param.i_threads = 1;

    x264_param_apply_profile(&param, "baseline");
    //open encoder
    videoCodec = x264_encoder_open(&param);
    pic_in = new x264_picture_t;
    x264_picture_alloc(pic_in, X264_CSP_I420, width, height);
    pthread_mutex_unlock(&mutex);
}

void VideoStream::setVideoCallback(VideoCallback videoCallback) {
    this->videoCallback = videoCallback;
}

void VideoStream::encodeData(int8_t *data) {
    // 线程锁
    pthread_mutex_lock(&mutex);
    // 对h264框架的输入图片结构体赋值，拷贝0-ySize到Y层, ySize有设置编码参数时width和height计算出
    memcpy(pic_in->img.plane[0], data, ySize);
    // 根据YUV420P编码规则知道uvSize为ySize的1/4
    // 传入数据是NV21, 摄像头录制的数据一般是NV21
    for (int i = 0; i < uvSize; ++i) {
        // u层赋值
        *(pic_in->img.plane[1] + i) = *(data + ySize + i * 2 + 1);
        // v层赋值，
        *(pic_in->img.plane[2] + i) = *(data + ySize + i * 2);
    }
    // pp_nal是编码返回的NAL单元
    x264_nal_t *pp_nal;
    //编码返回的nal单元个数
    int pi_nal;
    // 定义编码输出
    x264_picture_t pic_out;
    // 调用编码函数，传入nal单元，pi_nal
    x264_encoder_encode(videoCodec, &pp_nal, &pi_nal, pic_in, &pic_out);
    // 序列参数集长度
    int sps_len = 0;
    // 图像参数集长度
    int pps_len = 0;
    // 序列参数集
    uint8_t sps[100];
    // 图像参数集
    uint8_t pps[100];
    // 遍历编码后的nal单元
    for (int i = 0; i < pi_nal; ++i) {
        //3-7位 范围1-32代表nal单元类型
        // 如果是是序列参数集
        if (pp_nal[i].i_type == NAL_SPS) {
            // payload通常用于从实际开销中区分出协议开销
            // sps长度为 pp_nal[i]开销数据大小 - 4 , sps与pps必定以4位位开始标志符
            // SPS 对于H264而言，就是编码后的第一帧，如果是读取的H264文件，就是第一个帧界定符和第二个帧界定符之间的数据的长度是4
            //PPS 就是编码后的第二帧，如果是读取的H264文件，就是第二帧界定符和第三帧界定符中间的数据长度不固定。
            sps_len = pp_nal[i].i_payload - 4;
            // 将p_payload数组地址加4从第四个索引开始拷贝，前四个用于存储开始码，每个32位， 到开始码之后,使用开始码之后的第一个字节的低5位判断是否为7(sps)或者8(pps),
            memcpy(sps, pp_nal[i].p_payload + 4, static_cast<size_t>(sps_len));
        } else if (pp_nal[i].i_type == NAL_PPS) { // 如果是pps NAL单元
            //
            // sps长度为 pp_nal[i]有效载荷大小 - 4
            pps_len = pp_nal[i].i_payload - 4;
            memcpy(pps, pp_nal[i].p_payload + 4, static_cast<size_t>(pps_len));
            // 发送spsNal,ppsNal
            sendSpsPps(sps, pps, sps_len, pps_len);
        } else { // 如果是数据帧单元
            // 发送数据帧，帧类型，帧数据，帧数据大小
            sendFrame(pp_nal[i].i_type, pp_nal[i].p_payload, pp_nal[i].i_payload);
        }
    }
    // 释放线程锁
    pthread_mutex_unlock(&mutex);
}

void VideoStream::encodeDataNew(int8_t *y_plane, int8_t *u_plane, int8_t *v_plane) {
    // 线程锁
    pthread_mutex_lock(&mutex);
    //
    memcpy(pic_in->img.plane[0], y_plane, (size_t) ySize);
    memcpy(pic_in->img.plane[1], u_plane, (size_t) ySize / 4);
    memcpy(pic_in->img.plane[2], v_plane, (size_t) ySize / 4);

    x264_nal_t *pp_nal;
    int pi_nal;
    x264_picture_t pic_out;
    x264_encoder_encode(videoCodec, &pp_nal, &pi_nal, pic_in, &pic_out);
    int sps_len = 0;
    int pps_len = 0;
    uint8_t sps[100];
    uint8_t pps[100];
    for (int i = 0; i < pi_nal; ++i) {
        if (pp_nal[i].i_type == NAL_SPS) {
            sps_len = pp_nal[i].i_payload - 4;
            memcpy(sps, pp_nal[i].p_payload + 4, static_cast<size_t>(sps_len));
        } else if (pp_nal[i].i_type == NAL_PPS) {
            pps_len = pp_nal[i].i_payload - 4;
            memcpy(pps, pp_nal[i].p_payload + 4, static_cast<size_t>(pps_len));
            sendSpsPps(sps, pps, sps_len, pps_len);
        } else {
            sendFrame(pp_nal[i].i_type, pp_nal[i].p_payload, pp_nal[i].i_payload);
        }
    }
    pthread_mutex_unlock(&mutex);
}
// 将spsNal和ppsNal用rtmp协议封进rtmp packet数据包，并发送给服务器
void VideoStream::sendSpsPps(uint8_t *sps, uint8_t *pps, int sps_len, int pps_len) {
    //按照h264标准配置SPS和PPS共13+3+SPS参数内容长+PPS参数内容长
    int bodySize = 13 + sps_len + 3 + pps_len;
    // 实例化RTMPPacket对象
    RTMPPacket *packet = new RTMPPacket;
    // 为packet分配内存,大小为bodysize
    RTMPPacket_Alloc(packet, bodySize);

    int i = 0;
    //start code，0x17 也就是高4位为1意味着关键帧，低4位为7意味着AVC格式
    // （也就是h.264,AVC 实际上是 H.264 协议的别名。但自从H.264协议中增加了SVC的部分之后，人们习惯将不包含SVC的H.264协议那一部分称为 AVC，而将SVC这一部分单独称为SVC.）
    //  H.264中还有一个SVC概念(Scalable Video Coding),可分层编码。
    packet->m_body[i++] = 0x17;
    //type  AVCPacketType设置为AVC sequence header ，也就是0，其占用buffer[1]=0x00
    // CompositionTime，如果为AVCPacketType=0的话，这三个字节也是0
    packet->m_body[i++] = 0x00;
    packet->m_body[i++] = 0x00;
    packet->m_body[i++] = 0x00;
    packet->m_body[i++] = 0x00;

    //version 接下来的buffer[5]=0x01为配置版本号固定为1
    packet->m_body[i++] = 0x01;
    // AVCProfileIndication（配置特征） 所以这里是Baseline(基线配置)。所以我们这里buffer[6]=0x42(十进制66).
    packet->m_body[i++] = sps[1];
    // 在H.264-AVC-ISO_IEC_14496-10中规定，对每一种profile的支持级别，如果全部支持就将相应的位bit设置为1，否则就是0，我们看c0=二进制(1100 0000) ,
    // 对应的下图的8个bit，也就是前两个constraint_set0_flag=1,constraint_set1_flag=1,
    // 而前一个constraint_set0_flag代表对Baseline profile的支持程度，后一个constraint_set1_flag代表对Main profile的支持程度,也就是它们都为1，就是都是全部支持的，所以这里需要将buffer[7]=0xc0.
    packet->m_body[i++] = sps[2];
    // buffer[8]=0x33   LevelIDC 默认值为21 我们选择51则为0x33 支持4096X2304
    packet->m_body[i++] = sps[3];
    // 前6位为111111，后两位代表前面头的长度，也就是将h.264转换成可以进行网络rtmp传输的flv的相应的格式的媒体流的时候，
    // 不用原来的nalu 起始位00 00 00 01 来代表一个nalu了，而是用一个固定的4个字节来代表一个nalu，
    // 而这4个字节，在NALULengthSizeMinusOne这个位置表示的时候不能写0x04，而是0x04-1=0x03，而0x03 的二进制是11,
    // 所以该处NALULengthSizeMinusOne的完整表示是二进制(1111 1111),十六进制是0xFF,这个是规则，所以用它来代表这里的nalu的起始位置，所以buffer[9]=(0xFF).
    packet->m_body[i++] = 0xFF;

    //sps 接下来的就是，numOfSequenceParameterSets，也就是SPS的个数，我们常常只获得一个SPS，所以个数为1，而高位前三位的是保留的111，所以就是1110 0001，也就是0xE1，所以buffer[10]=(0xE1).
    packet->m_body[i++] = 0xE1;
    //sps len 接下来的就是两个字节的是（SPS的长度）
    packet->m_body[i++] = (sps_len >> 8) & 0xff;
    packet->m_body[i++] = sps_len & 0xff;
    //
    memcpy(&packet->m_body[i], sps, sps_len);
    i += sps_len; // 将i移动sps数据结束，pps数据开始处

    //pps 然后是将4个字节的PPS数据添加到这个数据的末尾，所以现在buffer的长度就是39+4=43个字节，
    // numOfPictureParameterSets,也就是PPS的个数，这里就是buffer[36]=0x01
    packet->m_body[i++] = 0x01;
    packet->m_body[i++] = (pps_len >> 8) & 0xff; // 高8位数据
    packet->m_body[i++] = (pps_len) & 0xff; // 低8位数据
    memcpy(&packet->m_body[i], pps, pps_len); //

    //video
    packet->m_packetType = RTMP_PACKET_TYPE_VIDEO;
    packet->m_nBodySize = bodySize;
    packet->m_nChannel = 10;
    //sps and pps no timestamp
    packet->m_nTimeStamp = 0;
    packet->m_hasAbsTimestamp = 0;
    packet->m_headerType = RTMP_PACKET_SIZE_MEDIUM;

    videoCallback(packet);
}
// 发送帧数据
void VideoStream::sendFrame(int type, uint8_t *payload, int i_payload) {
    // 判断界定符类型 根据第三个byte来判断 00 00 00 01 或 00 00 01
    if (payload[2] == 0x00) {
        i_payload -= 4;
        payload += 4;
    } else {
        i_payload -= 3;
        payload += 3;
    }
    // 9个配置+负载大小
    int bodySize = 9 + i_payload;
    RTMPPacket *packet = new RTMPPacket;
    RTMPPacket_Alloc(packet, bodySize);
    // 高四位为2代表非关键帧，默认非关键帧
    packet->m_body[0] = 0x27;
    // 如果是关键帧，就设为关键帧
    if (type == NAL_SLICE_IDR) {
        packet->m_body[0] = 0x17;
        if (DEBUG) {
            LOGI("IDR key frame");
        }
    }
    //packet type  Head_Type占用RTMP包的第一个字节，这个字节里面记录了包的类型和包的ChannelID。Head_Type字节的前两个Bit决定了包头的长度. 01为8字节
    // Head_Type的后面6个Bit和StreamID决定了ChannelID。  StreamID和ChannelID对应关系：StreamID=(ChannelID-4)/5+1 参考red5
    packet->m_body[1] = 0x01;
    //timestamp 时间戳  发送时再设置
    packet->m_body[2] = 0x00;
    packet->m_body[3] = 0x00;
    packet->m_body[4] = 0x00;
    //packet len packt大小为一个完整的int32大小
    packet->m_body[5] = (i_payload >> 24) & 0xff;
    packet->m_body[6] = (i_payload >> 16) & 0xff;
    packet->m_body[7] = (i_payload >> 8) & 0xff;
    packet->m_body[8] = (i_payload) & 0xff;

    memcpy(&packet->m_body[9], payload, static_cast<size_t>(i_payload));

    packet->m_hasAbsTimestamp = 0;
    packet->m_nBodySize = bodySize;
    packet->m_packetType = RTMP_PACKET_TYPE_VIDEO;
    packet->m_nChannel = 0x10;
    packet->m_headerType = RTMP_PACKET_SIZE_LARGE;
    videoCallback(packet);
}
