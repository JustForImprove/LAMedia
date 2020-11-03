
#include "jni_define.h"
#include <jni.h>
#include <string>
#include <android/native_window_jni.h>

#include "XPlayer/XLog.h"
#include "XPlayer/IPlayerPorxy.h"
extern "C"{
    #include "jvm_init.h"
}
/* 硬解码兼容性太差
extern "C"
JNIEXPORT
jint JNI_OnLoad(JavaVM *vm,void *res)
{
    IPlayerPorxy::Get()->Init(vm);

    /*IPlayerPorxy::Get()->Open("/sdcard/v1080.mp4");
    IPlayerPorxy::Get()->Start();

    IPlayerPorxy::Get()->Open("/sdcard/1080.mp4");
    IPlayerPorxy::Get()->Start();
    return JNI_VERSION_1_4;
}
*/

XPLAYER_FUNC(void, Seek, jdouble pos) {
    // TODO: implement Seek()
    IPlayerPorxy::Get()->Seek(pos);
}

XPLAYER_FUNC(void, Open, jstring url_) {
    // TODO: implement Open()

    const char *url = env->GetStringUTFChars(url_, 0);

    IPlayerPorxy::Get()->Open(url);
    IPlayerPorxy::Get()->Start();

    //IPlayerPorxy::Get()->Seek(0.5);

    env->ReleaseStringUTFChars(url_, url);
}
XPLAYER_FUNC(void, InitView, jobject surface) {
    // TODO: implement InitView()
    // TODO
    IPlayerPorxy::Get()->Init(javaVM);
    ANativeWindow *win = ANativeWindow_fromSurface(env,surface);
    IPlayerPorxy::Get()->InitView(win);
}
XPLAYER_FUNC( void,PlayOrPause) {
    // TODO: implement PlayOrPause()
    IPlayerPorxy::Get()->SetPause(!IPlayerPorxy::Get()->IsPause());

}
XPLAYER_FUNC( jdouble ,PlayPos) {
    // TODO: implement PlayPos()
    return IPlayerPorxy::Get()->PlayPos();
}
