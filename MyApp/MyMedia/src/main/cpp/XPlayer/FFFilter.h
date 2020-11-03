//
// Created by 10415 on 2020/10/1.
//

#ifndef XPLAY_FFFILTER_H
#define XPLAY_FFFILTER_H

#include "IFilter.h"



class FFFilter: public IFilter{
public:
    virtual AVFrame* Filter(AVFrame* input);
    virtual bool SetFilter(AVCodecContext *avCodecContext, FilterType filterType);
    virtual bool DropFilter();

protected:
    AVFilterContext *buffersrc_ctx;
    AVFilterContext *buffersink_ctx;
    AVFilterGraph *filterGraph;
    AVFrame *inputFrame;
    AVFrame *outputFrame;
};

#endif //XPLAY_FFFILTER_H
