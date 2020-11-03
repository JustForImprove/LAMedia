# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             XMedia

             # Sets the library as a shared library.
             SHARED
        src/main/cpp/jvm_init.c

        src/main/cpp/XPlayer.cpp
        src/main/cpp/XPlayer/FFDemux.cpp
        src/main/cpp/XPlayer/IDemux.cpp
        src/main/cpp/XPlayer/XData.cpp
        src/main/cpp/XPlayer/XLog.cpp
        src/main/cpp/XPlayer/XThread.cpp
        src/main/cpp/XPlayer/IObserver.cpp
        src/main/cpp/XPlayer/IDecode.cpp
        src/main/cpp/XPlayer/FFDecode.cpp
        src/main/cpp/XPlayer/XParameter.cpp
        src/main/cpp/XPlayer/IVideoView.cpp
        src/main/cpp/XPlayer/GLVideoView.cpp
        src/main/cpp/XPlayer/XTexture.cpp
        src/main/cpp/XPlayer/XShader.cpp
        src/main/cpp/XPlayer/XEGL.cpp
        src/main/cpp/XPlayer/FFResample.cpp
        src/main/cpp/XPlayer/IResample.cpp
        src/main/cpp/XPlayer/IAudioPlay.cpp
        src/main/cpp/XPlayer/SLAudioPlay.cpp
        src/main/cpp//XPlayer/IPlayer.cpp
        src/main/cpp/XPlayer/IPlayerBuilder.cpp
        src/main/cpp/XPlayer/FFPlayerBuilder.cpp
        src/main/cpp/XPlayer/IPlayerPorxy.cpp
        src/main/cpp/XPlayer/IFilter.cpp
        src/main/cpp/XPlayer/FFFilter.cpp
        # Live模块代码
        src/main/cpp/XLive.cpp
        src/main/cpp/XLive/AudioStream.cpp
        src/main/cpp/XLive/VideoStream.cpp

        # Recorder模块的代码
        src/main/cpp/XRecorder.cpp
        src/main/cpp/XRecorder/FFEncode.cpp
        src/main/cpp/XRecorder/IEncode.cpp
        src/main/cpp/XRecorder/FFMux.cpp
        src/main/cpp/XRecorder/IMux.cpp

        # Provides a relative path to your source file(s).
            # XClip代码
            src/main/cpp/ffmpeg/cmdutils.c
            src/main/cpp/ffmpeg/ffmpeg.c
            src/main/cpp/ffmpeg/ffmpeg_filter.c
            src/main/cpp/ffmpeg/ffmpeg_opt.c
            src/main/cpp/ffmpeg/ffprobe.c
            src/main/cpp/ffmpeg/ffmpeg_hw.c
        src/main/cpp/XClip/ffmpeg_cmd.c
        src/main/cpp/XClip/audio_player.c
        src/main/cpp/XClip/openSL_audio_player.c
        src/main/cpp/XClip/video_player.c
        src/main/cpp/XClip/ffmpeg_pusher.cpp
        src/main/cpp/XClip/AVpacket_queue.c
        src/main/cpp/XClip/media_player.c
        src/main/cpp/XClip/video_filter.c
        src/main/cpp/XClip/audio_lame.c
        src/main/cpp/XClip/fast_start.c
        src/main/cpp/XClip/ffprobe_cmd.c
        )

add_library( ffmpeg
             SHARED
             IMPORTED )
set_target_properties( ffmpeg
                       PROPERTIES IMPORTED_LOCATION
                       ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libffmpeg.so )

add_library( mp3lame
             SHARED
             IMPORTED )
set_target_properties( mp3lame
            PROPERTIES IMPORTED_LOCATION
            ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libmp3lame.so )


add_library(faac
        STATIC
        IMPORTED)
set_target_properties(faac
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libfaac.a)

add_library(rtmp
        STATIC
        IMPORTED)
set_target_properties(rtmp
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/librtmp.a)

add_library(x264
        STATIC
        IMPORTED)
set_target_properties(x264
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libx264.a)


set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

include_directories(src/main/cpp)
include_directories(src/main/cpp/include)
if(${CMAKE_ANDROID_ARCH_ABI} MATCHES "armeabi-v7a")
    include_directories(src/main/cpp/include/armeabi-v7a)
    message("This is armeabi-v7a")
elseif(${CMAKE_ANDROID_ARCH_ABI} MATCHES "arm64-v8a")
    include_directories(src/main/cpp/include/arm64-v8a)
    message("This is arm64-v8a")
endif()

find_library( # Sets the name of the path variable.
              log-lib
              log )

target_link_libraries( # Specifies the target library.
                       XMedia
                       mp3lame
                       ffmpeg
                       -landroid #native_window
                       -ljnigraphics #bitmap
                       -lOpenSLES #openSLES
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                        faac
                        rtmp
                        x264
                        android
                        z
                        OpenSLES
                        GLESv2
                        EGL
        )