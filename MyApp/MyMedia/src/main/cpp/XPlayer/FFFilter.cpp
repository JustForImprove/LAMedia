//
// Created by 10415 on 2020/10/1.
//

#include "FFFilter.h"
#include "XLog.h"
extern "C"{
    #include "libavfilter/buffersink.h"
    #include "libavfilter/avfilter.h"
    #include "libavcodec/avcodec.h"
    #include "libavfilter/buffersrc.h"
    #include "libavutil/opt.h"
};

AVFrame* FFFilter::Filter(AVFrame* inputData) {
    AVFrame *outputFrame = av_frame_alloc();
    // 装填数据
    XLOGE("dddddd");
    if(av_buffersrc_add_frame_flags(buffersrc_ctx, inputData, 0) < 0 ){
        XLOGE("图像处理出错");
        return inputData;
    }
    while(1){
        XLOGE("图像处理1");
        int ret = av_buffersink_get_frame(buffersink_ctx, outputFrame);
        if(ret == AVERROR(EAGAIN) || ret == AVERROR_EOF){
            XLOGE("图像处理3");
            break;
        }
        XLOGE("图像处理2");
        //av_frame_unref(outputFrame);
    }


    XLOGE("图像处理4");

    //av_frame_free(&outputFrame);
    XLOGE("%d", outputFrame->width);


    return outputFrame;
}


bool FFFilter::SetFilter(AVCodecContext *avCodecContext,FilterType) {

    filterGraph = avfilter_graph_alloc();
    // 添加一个起始filter作为视频帧数据的接收者
    const AVFilter * buffersrc = avfilter_get_by_name("buffer");
    char args[512];
    snprintf(args, sizeof(args), "video_size=%dx%d:pix_fmt=%d:time_base=%d/%d:pixel_aspect=%d/%d",
            avCodecContext->width,avCodecContext->height, avCodecContext->pix_fmt,
            avCodecContext->time_base.num, avCodecContext->time_base.den,
            avCodecContext->sample_aspect_ratio.num,
            avCodecContext->sample_aspect_ratio.den);
    // 创建输入图，
    int ret = avfilter_graph_create_filter(&buffersrc_ctx,  buffersrc, "in", args, NULL, filterGraph);
    if (ret<0){
        return false;
    }

    const AVFilter *buffersink = avfilter_get_by_name("buffersink");
    // 创建输出图
    ret = avfilter_graph_create_filter(&buffersink_ctx, buffersink, "out", NULL, NULL, filterGraph);
    if (ret < 0){
        XLOGE("图像filter创建出错");
    }
    // 设置输出YUV420P，输入随意
    enum AVPixelFormat pix_fmts[] = { AV_PIX_FMT_YUV420P, AV_PIX_FMT_NONE };
    ret = av_opt_set_int_list(buffersink_ctx, "pix_fmts", pix_fmts,
                              AV_PIX_FMT_NONE, AV_OPT_SEARCH_CHILDREN);

    if (ret<0){
        XLOGE("图像filter输出处理出错");
        return false;
    }

    // 设置输出像素格式

    char  filters_descr[512];
    snprintf(filters_descr, sizeof(filters_descr), "hflip");// 垂直翻转暂时有bug

    // outputs变量意指buffersrc_ctx滤镜的输出引脚(output pad)
    // src缓冲区(buffersrc_ctx滤镜)的输出必须连到filters_descr中第一个
    // 滤镜的输入；filters_descr中第一个滤镜的输入标号未指定，故默认为
    // "in"，此处将buffersrc_ctx的输出标号也设为"in"，就实现了同标号相连

    AVFilterInOut *outputs = avfilter_inout_alloc();
    outputs->name = av_strdup("in");
    outputs->filter_ctx = buffersrc_ctx;
    outputs->pad_idx = 0;
    outputs->next = NULL;

    AVFilterInOut *inputs = avfilter_inout_alloc();
    inputs->name = av_strdup("out");
    inputs->filter_ctx = buffersink_ctx;
    inputs->pad_idx = 0;
    inputs->next = NULL;

    ret = avfilter_graph_parse_ptr(filterGraph, filters_descr, &inputs, &outputs, NULL);

    if (ret < 0){
        return false;
    }
    ret = avfilter_graph_config(filterGraph, NULL);
    if (ret < 0){
        XLOGE("滤镜配置出错！%s", av_err2str(ret));
        return false;
    }
    avfilter_inout_free(&outputs);
    avfilter_inout_free(&inputs);
    XLOGE("滤镜初始化成功！");
    return true;
}

bool FFFilter::DropFilter() {
    return false;
}