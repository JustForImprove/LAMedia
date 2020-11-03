
#ifndef MY_APPLICATION_JNI_DEFINE_H
#define MY_APPLICATION_JNI_DEFINE_H


// denfine语法怎么用的？ 学习Mark
// 扩展C要加extern "C"，不然找不到
#define XPLAYER_FUNC(RETURN_TYPE, FUNC_NAME, ...) \
    extern "C" JNIEXPORT RETURN_TYPE JNICALL Java_com_la_mymedia_player_XPlay_ ## FUNC_NAME \
    (JNIEnv *env, jobject thiz, ##__VA_ARGS__)\

#define Live_FUNC(RETURN_TYPE, FUNC_NAME, ...) \
    extern "C" \
    JNIEXPORT RETURN_TYPE JNICALL Java_com_la_mymedia_live_LivePusherNew_ ## FUNC_NAME \
    (JNIEnv *env, jobject instance, ##__VA_ARGS__)\

#endif //MY_APPLICATION_JNI_DEFINE_H
