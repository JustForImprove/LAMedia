package com.la.mymedia.live;

/**
 * Using FFmpeg to push FLV stream
 * Created by frank on 2018/2/2.
 */

public class NativePusher {
    static {
        System.loadLibrary("XMedia");
    }

    /**
     * JNI interface: select file and push to rtmp server
     *
     * @param filePath liveUrl
     * @param liveUrl  the url of rtmp server
     * @return the result of pushing stream
     */
    public native int pushStream(String filePath, String liveUrl);

}
