

#ifndef XRCORDER_XLOG_H
#define XRCORDER_XLOG_H

class XLog {

};
#ifdef ANDROID
#include <android/log.h>
#define XLOGD(...) __android_log_print(ANDROID_LOG_DEBUG,"XRecorder",__VA_ARGS__)
#define XLOGI(...) __android_log_print(ANDROID_LOG_INFO,"XRecorder",__VA_ARGS__)
#define XLOGE(...) __android_log_print(ANDROID_LOG_ERROR,"XRecorder",__VA_ARGS__)
#else
#define XLOGD(...) printf("XRecord",__VA_ARGS__)
#define XLOGI(...) printf("XRecord",__VA_ARGS__)
#define XLOGE(...) printf("XRecord",__VA_ARGS__)

#endif



#endif //XPLAY_XLOG_H
