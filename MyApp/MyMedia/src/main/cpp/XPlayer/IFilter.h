//
// Created by 10415 on 2020/9/30.
//

#ifndef XPLAY_IFILTER_H
#define XPLAY_IFILTER_H


#include "IObserver.h"

struct AVFilterContext;
struct AVFilterGraph;
struct AVFrame;
struct AVCodecContext;

enum FilterType{
    A
};

class IFilter {

public:
    virtual bool SetFilter(AVCodecContext *avCodecContext, FilterType filterType) = 0;
    virtual bool DropFilter() = 0;
    virtual AVFrame* Filter(AVFrame* inputData){};

protected:

};
#endif //XPLAY_IFILTER_H
