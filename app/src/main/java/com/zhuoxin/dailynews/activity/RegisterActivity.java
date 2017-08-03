package com.zhuoxin.dailynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.zhuoxin.dailynews.R;
import com.zhuoxin.dailynews.bean.RegisterInfo;
import com.zhuoxin.dailynews.biz.Upload;
import com.zhuoxin.dailynews.biz.UrlUtils;
import com.zhuoxin.dailynews.utils.CommonUtil;
import com.zhuoxin.dailynews.utils.LogUtils;
import com.zhuoxin.dailynews.utils.OkHttpUtils;
import com.zhuoxin.dailynews.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/7/21.
 */

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.account)
    EditText mAccount;
    @BindView(R.id.e_mail)
    EditText mEMail;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.register)
    Button mRegister;
    private String TAG = "RegisterActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.register)
    public void onViewClicked() {
        register();
    }

    private void register() {
        final String account = mAccount.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        String email = mEMail.getText().toString().trim();
        if (account.length()==0){
            mAccount.setError("用户名不能为空");
            return;
        }
        if (password.length()==0){
            mPassword.setError("密码不能为空");
            return;
        }
        if (email.length()==0){
            mEMail.setError("邮箱不能为空");
            return;
        }

        if (!CommonUtil.verifyPassword(password)){
            mPassword.setError("密码必须为6-16位的数字或字母");
            return;
        }
        if (!CommonUtil.verifyEmail(email)){
            mEMail.setError("邮箱格式不正确");
            return;
        }

        String url = UrlUtils.MAIN_URL + "user_register?ver=1&uid="+account+"&email="+email+"&pwd="+password;
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(UrlUtils.MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Upload mUpload = mRetrofit.create(Upload.class);
        mUpload.register(account,email,password).enqueue(new Callback<RegisterInfo>() {
            @Override
            public void onResponse(retrofit2.Call<RegisterInfo> call, Response<RegisterInfo> response) {
                RegisterInfo mInfo = response.body();
                if (mInfo.getStatus() == 0){
                    ToastUtil.Toast(RegisterActivity.this,mInfo.getData().getExplain());
                    if (mInfo.getData().getResult() == 0){
                        Intent mIntent = new Intent();
                        Bundle mBundle = new Bundle();
                        mBundle.putString("username",account);
                        mBundle.putString("password",password);
                        mIntent.putExtras(mBundle);
                        setResult(1,mIntent);
                        RegisterActivity.this.finish();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<RegisterInfo> call, Throwable t) {

            }
        });
        /*OkHttpUtils.getOkHttp(url, RegisterActivity.this, new OkHttpUtils.CallBack() {
            @Override
            public void onSucceed(Call call, String strJson) {
                LogUtils.LogE("strjson",strJson);
                try {
                    JSONObject mJSONObject = new JSONObject(strJson);
                    if (mJSONObject.getInt("status")==0){
                        mJSONObject = mJSONObject.getJSONObject("data");
                        ToastUtil.Toast(RegisterActivity.this,mJSONObject.getString("explain"));
                        if (mJSONObject.getInt("result")==0){
                            Intent mIntent = new Intent();
                            Bundle mBundle = new Bundle();
                            mBundle.putString("username",account);
                            mBundle.putString("password",password);
                            mIntent.putExtras(mBundle);
                            setResult(1,mIntent);
                            RegisterActivity.this.finish();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.Toast(RegisterActivity.this,"服务器连接失败");
            }
        });*/
    }
}
