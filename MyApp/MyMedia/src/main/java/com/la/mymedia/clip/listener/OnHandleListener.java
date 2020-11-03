package com.la.mymedia.clip.listener;

/**
 * listener of FFmpeg processing
 * Created by frank on 2019/11/11.
 */
public interface OnHandleListener {
    void onBegin();
    void onProgress(int progress, int duration);
    void onEnd(int resultCode, String resultMsg);
}
