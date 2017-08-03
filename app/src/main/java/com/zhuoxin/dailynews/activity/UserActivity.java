package com.zhuoxin.dailynews.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuoxin.dailynews.R;
import com.zhuoxin.dailynews.adapter.RCUserAdapter;
import com.zhuoxin.dailynews.bean.UserInfo;
import com.zhuoxin.dailynews.biz.UrlUtils;
import com.zhuoxin.dailynews.utils.LogUtils;
import com.zhuoxin.dailynews.utils.OkHttpUtils;
import com.zhuoxin.dailynews.utils.SPUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/24.
 */

public class UserActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.user_portrait)
    CircleImageView mUserPortrait;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.user_count)
    TextView mUserCount;
    @BindView(R.id.user_follow)
    TextView mUserFollow;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.appBar)
    AppBarLayout mAppBar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.logout)
    Button mLogout;
    private RCUserAdapter mRCUserAdapter;
    private List<UserInfo.DataBean.LoginlogBean> mLoginlog;
    private UserInfo.DataBean mData;
    private AlertDialog mDialog;
    private String mFilePath;
    private String TAG = "UserActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        initData();

        initView();

        initRecyclerView();

        changePortrait();
    }

    private void changePortrait() {
        //为头像设置点击事件
        mUserPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建弹窗
                mDialog = new AlertDialog.Builder(UserActivity.this).create();
                mDialog.show();
                //引入弹窗布局
                View mView = LayoutInflater.from(UserActivity.this).inflate(R.layout.dialog_user, null);
                //为弹窗设置布局
                mDialog.setContentView(mView);
                //获取窗口管理器
                Window mWindow = mDialog.getWindow();
                //设置弹窗的间隙
                //mWindow.getDecorView().setPadding(0,0,0,0);
                //获取弹窗属性
                WindowManager.LayoutParams mLayoutParams = mWindow.getAttributes();
                //设置宽高属性
                mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                //为弹窗重新设置属性
                mWindow.setAttributes(mLayoutParams);
                //使弹窗位于界面底部
                mWindow.setGravity(Gravity.BOTTOM);
                //为弹窗设置背景
                mWindow.setBackgroundDrawableResource(R.drawable.dialog_bg);
                //找到弹窗布局中的控件
                TextView tv_camera = (TextView) mView.findViewById(R.id.tv_camera);
                TextView tv_photo = (TextView) mView.findViewById(R.id.tv_photo);
                TextView tv_cancel = (TextView) mView.findViewById(R.id.tv_cancel);
                //为控件设置点击事件
                tv_camera.setOnClickListener(UserActivity.this);
                tv_photo.setOnClickListener(UserActivity.this);
                tv_cancel.setOnClickListener(UserActivity.this);
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRCUserAdapter = new RCUserAdapter(this, mLoginlog, R.layout.layout_rcitem_user);
        mRecyclerView.setAdapter(mRCUserAdapter);
    }

    private void initView() {
        setSupportActionBar(mToolBar);
        ActionBar mSupportActionBar = getSupportActionBar();
        mSupportActionBar.setDisplayHomeAsUpEnabled(true);
        mSupportActionBar.setHomeButtonEnabled(true);
        mFilePath = SPUtils.getFilePath(UserActivity.this);
        mUserPortrait.setImageBitmap(BitmapFactory.decodeFile(mFilePath));
        mUserName.setText("" + mData.getUid() + "");
        mUserCount.setText("积分：" + mData.getIntegration() + "");
        mUserFollow.setText("跟帖：" + mData.getComnum() + "");
    }

    private void initData() {
        String mStrJson = SPUtils.getUserInfo(UserActivity.this);
        Gson mGson = new Gson();
        UserInfo mUserInfo = mGson.fromJson(mStrJson, UserInfo.class);
        mData = mUserInfo.getData();
        mLoginlog = mData.getLoginlog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.logout)
    public void onViewClicked() {
        SPUtils.clearLoginSP(UserActivity.this);
        SPUtils.clearUserinfoSP(UserActivity.this);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_camera:
                //开启拍照界面
                takePhoto();
                break;
            case R.id.tv_photo:
                //开启相册功能
                pickPhoto();
                break;
            case R.id.tv_cancel:

                break;
        }
        mDialog.dismiss();
    }

    private void pickPhoto() {
        Intent mIntent = new Intent(Intent.ACTION_PICK, null);
        mIntent.setType("image/*");
        mIntent.putExtra("crop", "true");//设置裁剪功能
        mIntent.putExtra("aspectX", 1); //宽高比例
        mIntent.putExtra("aspectY", 1);
        mIntent.putExtra("outputX", 80); //宽高值
        mIntent.putExtra("outputY", 80);
        mIntent.putExtra("return-data", true); //返回裁剪结果
        startActivityForResult(mIntent, 2);
    }

    private void takePhoto() {
        Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(mIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //获取从拍照界面传过的图片数据
            Bundle mBundle = data.getExtras();
            Bitmap mBitmap = (Bitmap) mBundle.get("data");
            //把图片提交给服务器
            File portrait = new File(SPUtils.getFilePath(UserActivity.this));
            String url = UrlUtils.MAIN_URL+"user_image?token="+SPUtils.getToken(UserActivity.this)+"&portrait="+portrait;
            LogUtils.LogE(TAG,url);
            OkHttpUtils.getOkHttp(url, UserActivity.this, new OkHttpUtils.CallBack() {
                @Override
                public void onSucceed(Call call, String strJson) {
                    LogUtils.LogE(TAG,strJson);
                }

                @Override
                public void onFailure(Call call, IOException e) {

                }
            });
            //将图片存入本地头像文件
            try {
                FileOutputStream fos = new FileOutputStream(mFilePath);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                //为头像控件重新设置图片
                mUserPortrait.setImageBitmap(BitmapFactory.decodeFile(mFilePath));
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
}
