package com.zhuoxin.dailynews.biz;

import com.zhuoxin.dailynews.bean.RegisterInfo;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2017/7/31.
 */

public interface Upload {
    @Multipart
    @POST("user_image?token=cfadcefe9d4e32166853838dee380f7c& portrait =")
    Call<ResponseBody> uploadFile(@Part() RequestBody file);

    @POST("user_register?ver=1")
    @FormUrlEncoded
    Call<RegisterInfo> register(@Field("uid") String uid, @Field("email") String email, @Field("pwd") String pwd);
}
