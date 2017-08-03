package com.zhuoxin.dailynews.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhuoxin.dailynews.R;
import com.zhuoxin.dailynews.biz.UrlUtils;
import com.zhuoxin.dailynews.utils.LogUtils;
import com.zhuoxin.dailynews.utils.OkHttpUtils;
import com.zhuoxin.dailynews.utils.SPUtils;
import com.zhuoxin.dailynews.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/20.
 */

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.SurfaceView)
    SurfaceView mSurfaceView;
    @BindView(R.id.account)
    EditText mAccount;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.register)
    TextView mRegister;
    private MediaPlayer mMediaPlayer;
    private String mUsername;
    private String mPassword1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mMediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor mFd = getAssets().openFd("welcome.mp4");
            mMediaPlayer.setDataSource(mFd.getFileDescriptor(),mFd.getStartOffset(), mFd.getLength());
            mMediaPlayer.setLooping(true);
            final SurfaceHolder mHolder = mSurfaceView.getHolder();
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
            mHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    mMediaPlayer.setDisplay(mHolder);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1){
            Bundle mBundle = data.getExtras();
            mUsername = mBundle.getString("username");
            mPassword1 = mBundle.getString("password");
            mAccount.setText(mUsername);
            this.mPassword.setText(mPassword1);
        }

    }

    @OnClick({R.id.login, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (mAccount.getText().length()==0){
                    mAccount.setError("用户名不能为空");
                    return;
                }
                if (mPassword.getText().length()==0){
                    mPassword.setError("密码不能为空");
                    return;
                }
                String url = UrlUtils.MAIN_URL+"user_login?ver=1&uid="+mAccount.getText()+"&pwd="+mPassword.getText()+"&device=0";
                OkHttpUtils.getOkHttp(url, LoginActivity.this, new OkHttpUtils.CallBack() {
                    @Override
                    public void onSucceed(Call call, String strJson) {
                        try {
                            JSONObject mJSONObject = new JSONObject(strJson);
                            switch (mJSONObject.getInt("status")){
                                case 0:
                                    //更改登录状态
                                    SPUtils.putLoginState(LoginActivity.this,true);
                                    //将token存入SP中
                                    mJSONObject = mJSONObject.getJSONObject("data");
                                    ToastUtil.Toast(LoginActivity.this,mJSONObject.getString("explain"));
                                    SPUtils.putToken(LoginActivity.this,mJSONObject.getString("token"));
                                    getUserInfo();
                                    setResult(1);
                                    break;
                                case -1:
                                    ToastUtil.Toast(LoginActivity.this,"用户名或密码错误");
                                    break;
                                case -2:
                                    ToastUtil.Toast(LoginActivity.this,"拒绝服务，请升级客户端");
                                    break;
                                case -3:
                                    ToastUtil.Toast(LoginActivity.this,"服务器内部异常，请稍候重试");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                });
                break;
            case R.id.register:
                startActivityForResult(new Intent(LoginActivity.this,RegisterActivity.class),1);
                break;
        }
    }

    private void getUserInfo(){
        String mToken = SPUtils.getToken(LoginActivity.this);
        String url = UrlUtils.MAIN_URL + "user_home?ver=1&imei=1&token =" + mToken;
        OkHttpUtils.getOkHttp(url, LoginActivity.this, new OkHttpUtils.CallBack() {
            @Override
            public void onSucceed(Call call, String strJson) {
                try {
                    JSONObject mJSONObject = new JSONObject(strJson);
                    SPUtils.putUserInfo(LoginActivity.this,strJson);
                    switch (mJSONObject.getInt("status")) {
                        case 0:
                            mJSONObject = mJSONObject.getJSONObject("data");
                            //获取头像网址
                            String mPortrait1 = mJSONObject.getString("portrait");
                            //根据网址获取头像
                            getPortrait(mPortrait1);
                            //获取用户名
                            String uid = mJSONObject.getString("uid");
                            //储存用户名和图片地址
                            SPUtils.putUserName(LoginActivity.this,uid);
                            SPUtils.putPortrait(LoginActivity.this,mPortrait1);
                            //更改登录状态
                            SPUtils.putLoginState(LoginActivity.this,true);
                            break;
                        case -1:

                            break;
                        case -2:
                            ToastUtil.Toast(LoginActivity.this, "拒绝服务，请升级客户端");
                            break;
                        case -3:
                            ToastUtil.Toast(LoginActivity.this, "服务器内部异常，请稍候重试");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });

    }

    private void getPortrait(String portrait1) {
        new OkHttpClient().newCall(new Request.Builder().url(portrait1).build()).enqueue(new Callback() {

            private FileOutputStream mFileOutputStream;
            private InputStream mInputStream;

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    //获取图片字节流
                    mInputStream = response.body().byteStream();
                    //获取图片存储目录
                    String mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Portrait";
                    //判断目录是否存在，不存在就创建
                    File mFileDir = new File(mPath);
                    if (!mFileDir.exists()) {//如果不存在
                        mFileDir.mkdirs();//创建
                    }
                    //图片存储的具体路径名
                    final File mFile = new File(mFileDir, "portrait.jpg");
                    //存储图片的路径
                    SPUtils.putFilePath(LoginActivity.this,mFile.getAbsolutePath());
                    //开始读写操作
                    mFileOutputStream = new FileOutputStream(mFile);
                    byte[] mBytes = new byte[1024];
                    int len = 0;
                    while ((len = mInputStream.read(mBytes)) != -1) {
                        mFileOutputStream.write(mBytes, 0, len);
                    }
                    mFileOutputStream.flush();
                } catch (Exception e) {

                } finally {
                    if (mInputStream != null) {
                        mInputStream.close();
                    }
                    if (mFileOutputStream != null) {
                        mFileOutputStream.close();
                    }
                    finish();
                }

            }
        });
    }
}
