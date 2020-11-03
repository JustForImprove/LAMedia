//
// Created by 10415 on 2020/10/3.
//

#ifndef XRCORDER_IENCODE_H
#define XRCORDER_IENCODE_H


#include "IObserver.h"
#include "XParameter.h"
#include <list>

class IEncode: public IObserver{
public:

    // 打开编码器，是否硬编码
    virtual bool Open(XParameter para,bool isHard=false) = 0;

    //future模型 发送数据到线程编码
    virtual bool SendPacket(XData pkt) = 0;

    //从线程中获取编码结果  再次调用会复用上次空间，线程不安全
    virtual XData RecvFrame() = 0;

    // 由主体notify的数据
    virtual void Update(XData data);

    // 判断是否为音频数据
    bool isAudio = false;

    //最大的队列缓冲
    int maxList = 100;

    //同步时间，再次打开文件要清理
    int synPts = 0;
    int pts = 0;

protected:
    virtual void Main();
    // 缓冲队列
    std::list<XData> packs;
    // 线程安全锁
    std::mutex packsMutex;
};


#endif //XPLAY_IENCODE_H
