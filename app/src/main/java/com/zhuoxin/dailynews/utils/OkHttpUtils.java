package com.zhuoxin.dailynews.utils;

import android.app.Activity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/11.
 */

public class OkHttpUtils {
    public static void getOkHttp(String url, final Activity activity, final CallBack callBack) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request mRequest = new Request.Builder()
                .get()
                .url(url)
                .build();
        mOkHttpClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(call, e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                final String result = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSucceed(call, result);
                    }
                });
            }
        });
    }


    public interface CallBack {
        void onSucceed(Call call, String strJson);

        void onFailure(Call call, IOException e);
    }
}
