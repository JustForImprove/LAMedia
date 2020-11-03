//
// Created by 10415 on 2020/10/3.
//

#ifndef XRCORDER_FFENCODE_H
#define XRCORDER_FFENCODE_H


#include "IEncode.h"

class FFEncode:IEncode{
public:

    virtual bool Open(XParameter para,bool isHard=false);

    virtual bool SendPacket(XData pkt);

    virtual XData RecvFrame();



};


#endif //XPLAY_FFENCODE_H
