cmake_minimum_required(VERSION 3.4.1)

#设置两个目录，${ANDROID_ABI}会根据设备变化
set(DIR ${PROJECT_SOURCE_DIR}/../libs/${ANDROID_ABI})
set(DIR2 ${PROJECT_SOURCE_DIR}/../libs)
#指定h文件的目录，编译过程需要找.h文件
include_directories(${DIR2}/include)

#导入FFmpeg的库
add_library(libavcodec SHARED IMPORTED)
#so文件的位置
set_target_properties(libavcodec
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libavcodec.so)


add_library(libavfilter SHARED IMPORTED)
set_target_properties(libavfilter
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libavfilter.so)

add_library(libavformat SHARED IMPORTED)
set_target_properties(libavformat
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libavformat.so)

add_library(libavutil SHARED IMPORTED)
set_target_properties(libavutil
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libavutil.so)

add_library(libswresample SHARED IMPORTED)
set_target_properties(libswresample
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libswresample.so)

add_library(libswscale SHARED IMPORTED)
set_target_properties(libswscale
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libswscale.so)
# 将所有绝对路径下的cpp文件编译成一个库
add_library(
        native-lib
        SHARED
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/FFDemux.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/IDemux.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/XData.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/XLog.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/XThread.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/IObserver.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/IDecode.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/FFDecode.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/XParameter.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/IVideoView.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/GLVideoView.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/XTexture.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/XShader.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/XEGL.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/FFResample.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/IResample.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/IAudioPlay
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/SLAudioPlay
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/IPlayer
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/IPlayerBuilder.cpp
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/FFPlayerBuilder
        ${PROJECT_SOURCE_DIR}/../cpp/native-lib/IPlayerPorxy


        native-lib/native-lib.cpp
)

#set_target_properties(native-lib
 #       PROPERTIES IMPORTED_LOCATION
  #      ${PROJECT_SOURCE_DIR}/../cpp/)

find_library( # Sets the name of the path variable.
        log-lib
        log)


#连接库
target_link_libraries(
        native-lib
        ${log-lib}
        # Specifies the target library.
        # Links the target library to the log library
        # included in the NDK.

        libavfilter
        libavformat
        libswresample
        libswscale
        libavutil
        libavcodec
        android
        z
        OpenSLES
        GLESv2
        EGL
)