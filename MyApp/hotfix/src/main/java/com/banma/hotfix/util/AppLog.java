package com.banma.hotfix.util;

import android.util.Log;

public class AppLog {
    private static final String TAG = "RobustFixGlobalTag";
    private static final String TAG_STEP = "RobustFixStepTag";

    private static boolean mDebug = true;

    public static void setDebug(boolean debug) {
        AppLog.mDebug = debug;
    }

    public static void d(String message) {
        if (!mDebug) return;
        Log.d(TAG, message);
    }

    private static int lastStep = 0;
    public static void step(int idx, String message) {
        String prefix = idx == 0?"":"Step " + idx + ": ";
        if (idx != lastStep) message += "\r\n";
        lastStep = idx;
        Log.d(TAG_STEP, prefix + message);
    }
    public static void e(String message, Throwable tr) {
        Log.e(TAG, message, tr);
    }
    public static void i(String message) {
        Log.i(TAG, message);
    }
}
