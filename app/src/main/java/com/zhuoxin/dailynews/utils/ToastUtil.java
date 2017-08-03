package com.zhuoxin.dailynews.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/7/14.
 */

public class ToastUtil {
    public static void Toast(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
