package com.zhuoxin.dailynews.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/7/14.
 */

public class LogUtils {
    private static boolean isShow = true;

    public static void LogE(String tag, String str) {
        if (isShow == true) {
            Log.e(tag, str);

        }
    }
}
