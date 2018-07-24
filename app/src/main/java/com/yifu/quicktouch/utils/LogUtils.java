package com.yifu.quicktouch.utils;

import android.util.Log;

/**
 * Created by yifu on 16-10-17.
 */

public class LogUtils {
    private static final boolean DEBUG = true;

    public static final void d(String tag, String msg) {
        if (DEBUG)
            Log.d(tag, msg);
    }

    public static final void w(String tag, String msg) {
        if (DEBUG)
            Log.w(tag, msg);
    }

    public static final void w(String tag, String msg, Throwable tr) {
        if (DEBUG)
            Log.w(tag, msg, tr);
    }

    public static final void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static final void i(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUG)
            Log.v(tag, msg);
    }
}
