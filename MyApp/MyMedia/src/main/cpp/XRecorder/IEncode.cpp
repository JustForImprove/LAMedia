//
// Created by 10415 on 2020/10/3.
//

#include "IEncode.h"


void IEncode::Update(XData pkt) {
    // 根据所在线程是音频编码线程还是视频编码线程决定要压入队列的数据
    if(pkt.isAudio != isAudio)
    {
        return;
    }
    while (!isExit)
    {
        packsMutex.lock();

        //阻塞
        if(packs.size() < maxList)
        {
            //生产者
            packs.push_back(pkt);
            packsMutex.unlock();
            break;
        }
        packsMutex.unlock();
        XSleep(1);
    }
}



void IEncode::Main() {
    while(!isExit)
    {
        if(IsPause())
        {
            XSleep(2);
            continue;
        }

        packsMutex.lock();

        //判断音视频同步
        if(!isAudio && synPts > 0)
        {
            if(synPts < pts)
            {
                packsMutex.unlock();
                XSleep(1);
                continue;
            }
        }

        if(packs.empty())
        {
            packsMutex.unlock();
            XSleep(1);
            continue;
        }
        //取出packet 消费者
        XData pack = packs.front();
        packs.pop_front();

        //发送数据到编码线程，一个数据包，可能编码多个帧
        if(this->SendPacket(pack))
        {
            while(!isExit)
            {
                //获取编码数据
                XData frame = RecvFrame();
                if(!frame.data) break;
                //XLOGE("RecvFrame %d",frame.size);
                // 获取pts
                pts = frame.pts;
                //发送数据给观察者
                this->Notify(frame);
            }

        }
        pack.Drop();
        packsMutex.unlock();
    }
}

