
#include <jni.h>
#include <string>
#include <android/native_window_jni.h>

#include "XLog.h"
#include "IPlayerPorxy.h"
extern "C"
JNIEXPORT
jint JNI_OnLoad(JavaVM *vm,void *res)
{
    IPlayerPorxy::Get()->Init(vm);
    return JNI_VERSION_1_4;
}

extern "C"
JNIEXPORT jstring
JNICALL
Java_xplay_xplay_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_xplay_XPlay_Open(JNIEnv *env, jobject thiz, jstring url_) {
    IPlayerPorxy::Get()->Open("/storage/4925-382B/1234.mp4"); //https://video19.ifeng.com/video09/2020/09/11/p37196910-102-009-083325.mp4
    IPlayerPorxy::Get()->Start();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_xplay_XPlay_InitView(JNIEnv *env, jobject thiz, jobject surface) {
    // TODO: implement InitView()
    ANativeWindow *win = ANativeWindow_fromSurface(env,surface);
    IPlayerPorxy::Get()->InitView(win);
}