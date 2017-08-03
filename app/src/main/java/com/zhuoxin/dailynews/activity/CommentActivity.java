package com.zhuoxin.dailynews.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhuoxin.dailynews.R;
import com.zhuoxin.dailynews.adapter.RCComAdapter;
import com.zhuoxin.dailynews.bean.Comments;
import com.zhuoxin.dailynews.bean.News;
import com.zhuoxin.dailynews.biz.UrlUtils;
import com.zhuoxin.dailynews.utils.LoadMoreForRecyclerView;
import com.zhuoxin.dailynews.utils.LogUtils;
import com.zhuoxin.dailynews.utils.OkHttpUtils;
import com.zhuoxin.dailynews.utils.SPUtils;
import com.zhuoxin.dailynews.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/26.
 */

public class CommentActivity extends AppCompatActivity {
    @BindView(R.id.iv)
    ImageView mIv;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tooBar_layout)
    CollapsingToolbarLayout mTooBarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.rcv_comment)
    RecyclerView mRcvComment;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    private News mNews;
    private String stamp;
    private int cid;
    private RCComAdapter mRCComAdapter;
    private List<Comments.DataBean> cacheList = new ArrayList<>();
    private List<Comments.DataBean> comList = new ArrayList<>();
    private AlertDialog mAlertDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        //获取上个界面穿过的数据
        mNews = (News) getIntent().getExtras().getSerializable("info");

        initToolBar();

        initRC();

        getData(1);
    }

    private void getData(final int dir) {
        if (dir == 1) {
            stamp = "0";
            cid = 0;
        } else if (dir == 2) {
            if (comList.size() == 0){
                return;
            }
            stamp = comList.get(mRcvComment.getAdapter().getItemCount() - 1).getStamp();
            cid = comList.get(mRcvComment.getAdapter().getItemCount() - 1).getCid();
        }
        String url = UrlUtils.MAIN_URL + "cmt_list?ver=1&nid=" + mNews.nid + "&type=1&stamp=" + stamp + "&cid=" + cid + "&dir=" + dir + "&cnt=20";
        OkHttpUtils.getOkHttp(url, CommentActivity.this, new OkHttpUtils.CallBack() {

            @Override
            public void onSucceed(Call call, String strJson) {
                LogUtils.LogE("strJson", strJson);
                Gson mGson = new Gson();
                Comments mComments = mGson.fromJson(strJson, Comments.class);
                List<Comments.DataBean> mData = mComments.getData();
                if (mData != null) {
                    if (dir == 2){
                        Snackbar.make(mRcvComment, R.string.LoadMore, Snackbar.LENGTH_SHORT).show();
                    }
                    for (int i = 0; i < mData.size(); i++) {
                        comList.add(mData.get(i));
                    }
                    mRCComAdapter.setList(comList);
                    mRCComAdapter.notifyDataSetChanged();
                    cacheList.clear();
                    for (int i = 0; i < comList.size(); i++) {
                        cacheList.add(comList.get(i));
                    }
                }else {
                    Snackbar.make(mRcvComment,R.string.NoMore,Snackbar.LENGTH_SHORT).show();
                }
                mSrl.setRefreshing(false);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.Toast(CommentActivity.this, CommentActivity.this.getResources().getString(R.string.onFailure));
            }
        });
    }


    private void initRC() {
        mRCComAdapter = new RCComAdapter(CommentActivity.this, comList, R.layout.layout_rcitem_comment);
        mRcvComment.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        mRcvComment.setAdapter(mRCComAdapter);
        mSrl.setColorSchemeColors(getResources().getColor(R.color.blue));
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRCComAdapter.setList(cacheList);
                comList.clear();
                getData(1);
            }
        });
        new LoadMoreForRecyclerView(mRcvComment, new LoadMoreForRecyclerView.LoadMoreListener() {
            @Override
            public void loadListener() {
                getData(2);
            }

            @Override
            public void loadMore() {

            }

        });
    }


    private void initToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("新闻评论");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        Glide.with(CommentActivity.this).load(mNews.icon).into(mIv);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            CommentActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        final String mToken = SPUtils.getToken(CommentActivity.this);
        boolean mIsLogin = SPUtils.getLoginState(CommentActivity.this);
        if (mIsLogin) {
            mAlertDialog = new AlertDialog.Builder(CommentActivity.this).create();
            mAlertDialog.show();
            View mView = LayoutInflater.from(CommentActivity.this).inflate(R.layout.dialog_comment, null);
            mAlertDialog.setContentView(mView);
            Window mWindow = mAlertDialog.getWindow();
            mWindow.setGravity(Gravity.BOTTOM);
            mWindow.setBackgroundDrawableResource(R.drawable.dialog_bg);
            WindowManager.LayoutParams mLayoutParams = mWindow.getAttributes();
            //设置宽高属性
            mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            //为弹窗重新设置属性
            mWindow.setAttributes(mLayoutParams);
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            ImageView send = (ImageView) mView.findViewById(R.id.send);
            final EditText mEditText = (EditText) mView.findViewById(R.id.et_comment);
            mEditText.setFocusable(true);
            mEditText.setFocusableInTouchMode(true);
            mEditText.requestFocus();
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Editable comment = mEditText.getText();
                    mAlertDialog.dismiss();
                    String url = UrlUtils.MAIN_URL + "cmt_commit?ver=1&nid=" + mNews.nid + "&token=" + mToken + "&imei=0&ctx=" + comment;
                    OkHttpUtils.getOkHttp(url, CommentActivity.this, new OkHttpUtils.CallBack() {
                        @Override
                        public void onSucceed(Call call, String strJson) {
                            try {
                                JSONObject mJSONObject = new JSONObject(strJson);
                                int mStatus = mJSONObject.getInt("status");
                                if (mStatus == -3) {//用户令牌失效
                                    //清空本地数据，更改本地存储登录状态
                                    SPUtils.clearUserinfoSP(CommentActivity.this);
                                    SPUtils.clearLoginSP(CommentActivity.this);
                                    //提示用户登录失效，重新登录
                                    Snackbar.make(mRcvComment,R.string.login,Snackbar.LENGTH_SHORT)
                                            .setAction(R.string.LoseLogin, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(new Intent(CommentActivity.this,LoginActivity.class));
                                                }
                                            })
                                            .show();
                                }
                                mJSONObject = mJSONObject.getJSONObject("data");
                                if (mStatus == 0 && mJSONObject.getInt("result") == 0) {//评论发布成功
                                    ToastUtil.Toast(CommentActivity.this, mJSONObject.getString("explain"));
                                    //封装Comments.DataBean，加入集合
                                    String uid = SPUtils.getUserName(CommentActivity.this);
                                    String content = comment.toString().trim();
                                    String stamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                    String portrait = SPUtils.getPortrait(CommentActivity.this);
                                    Comments.DataBean mDataBean = new Comments.DataBean(uid, content, stamp, portrait);
                                    comList.add(0, mDataBean);
                                    cacheList.add(0, mDataBean);
                                    //为适配器重新设置集合
                                    mRCComAdapter.setList(comList);
                                    //刷新适配器
                                    mRCComAdapter.notifyDataSetChanged();
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
            });

        } else {
            Snackbar.make(mRcvComment,R.string.login,Snackbar.LENGTH_SHORT)
                    .setAction(R.string.CheckLogin, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(CommentActivity.this,LoginActivity.class));
                        }
                    })
                    .show();
        }

    }
}
