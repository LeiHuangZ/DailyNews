package com.zhuoxin.dailynews.utils;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Administrator on 2017/7/28.
 */

public class SPUtils {
    public static void putLoginState(Activity activity , boolean isLogin){
        activity.getSharedPreferences("login",Context.MODE_PRIVATE).edit().putBoolean("isLogin",isLogin).apply();
    }

    public static boolean getLoginState(Activity activity){
        boolean mIsLogin = activity.getSharedPreferences("login", Context.MODE_PRIVATE).getBoolean("isLogin", false);
        return mIsLogin;
    }

    public static void putToken(Activity activity , String token){
        activity.getSharedPreferences("userinfo",Context.MODE_PRIVATE).edit().putString("token",token).apply();
    }

    public static String getToken(Activity activity){
        return activity.getSharedPreferences("userinfo", Context.MODE_PRIVATE).getString("token", "");
    }

    public static void putUserInfo(Activity activity , String strJson){
        activity.getSharedPreferences("userinfo",Context.MODE_PRIVATE).edit().putString("userinfo",strJson).apply();
    }

    public static String getUserInfo(Activity activity){
        return activity.getSharedPreferences("userinfo",Context.MODE_PRIVATE).getString("userinfo","");
    }

    public static void putUserName(Activity activity , String name){
        activity.getSharedPreferences("userinfo",Context.MODE_PRIVATE).edit().putString("username",name).apply();
    }

    public static String getUserName(Activity activity){
        return activity.getSharedPreferences("userinfo",Context.MODE_PRIVATE).getString("username","");
    }

    public static void putPortrait(Activity activity , String portrait){
        activity.getSharedPreferences("userinfo",Context.MODE_PRIVATE).edit().putString("portrait",portrait).apply();
    }

    public static String getPortrait(Activity activity){
        return activity.getSharedPreferences("userinfo",Context.MODE_PRIVATE).getString("portrait","");
    }

    public static void putFilePath(Activity activity , String filepath){
        activity.getSharedPreferences("userinfo",Context.MODE_PRIVATE).edit().putString("filepath",filepath).apply();
    }

    public static String getFilePath(Activity activity){
        return activity.getSharedPreferences("userinfo",Context.MODE_PRIVATE).getString("filepath","");
    }

    public static void clearLoginSP(Activity activity ){
        activity.getSharedPreferences("login",Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static void clearUserinfoSP(Activity activity ){
        activity.getSharedPreferences("userinfo",Context.MODE_PRIVATE).edit().clear().apply();
    }
}
