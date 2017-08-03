package com.zhuoxin.dailynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhuoxin.dailynews.R;
import com.zhuoxin.dailynews.bean.News;
import com.zhuoxin.dailynews.biz.SaveNewsDB;
import com.zhuoxin.dailynews.biz.UrlUtils;
import com.zhuoxin.dailynews.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/18.
 */

public class NewsActivity extends AppCompatActivity {
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.title)
    TextView mTitle;
    private News mNewsinfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        //获取传递的数据
        mNewsinfo = (News) getIntent().getExtras().getSerializable("newsinfo");

        //初始化标题
        initTitle();

        //初始化WebView
        initWebView();

    }

    private void initWebView() {
        mWebView.setWebViewClient(new WebViewClient());//不调用系统默认浏览器
        mWebView.loadUrl(mNewsinfo.link);//加载网页
        WebSettings mSettings = mWebView.getSettings();
        mSettings.setJavaScriptEnabled(true);//调用JavaScript
        mSettings.setLoadWithOverviewMode(true);//适应屏幕显示
        mSettings.setUseWideViewPort(true);//按任意比例缩放
        mSettings.setBuiltInZoomControls(true);
        mSettings.setDisplayZoomControls(true);
        mWebView.setWebChromeClient(mWebChromeClient);
    }

    WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressBar.setProgress(newProgress);//更行进度条
            if (mProgressBar.getProgress() == 100) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    };

    private void initTitle() {
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mTitle.setText(mNewsinfo.title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NewsActivity.this.finish();
                break;
            case R.id.collect:
                boolean insert = SaveNewsDB.insertNews(NewsActivity.this, mNewsinfo, "news");
                if (insert) {
                    ToastUtil.Toast(NewsActivity.this, "收藏成功");
                } else {
                    ToastUtil.Toast(NewsActivity.this, "新闻已收藏");
                }
                break;
            case R.id.comment:
                //NewsInfo传入评论界面
                Intent mIntent = new Intent(NewsActivity.this, CommentActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("info", mNewsinfo);
                mIntent.putExtras(mBundle);
                startActivityForResult(mIntent, 1);
                break;
            case R.id.share:
                showShare();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(mNewsinfo.title);
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(mNewsinfo.link);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mNewsinfo.summary);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        oks.setImageUrl(mNewsinfo.icon);
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //引入菜单布局
        getMenuInflater().inflate(R.menu.menu_news, menu);
        //获取评论数
        getCommentNum(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void getCommentNum(final Menu menu) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request mRequest = new Request.Builder()
                .url(UrlUtils.MAIN_URL + "cmt_num?ver=1& nid=" + mNewsinfo.nid)
                .build();
        Call mCall = mOkHttpClient.newCall(mRequest);

        mCall.enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {

            }

            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject mJSONObject = new JSONObject(result);
                    if (mJSONObject.getInt("status") == 0) {
                        final int mData = mJSONObject.getInt("data");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                menu.getItem(1).setTitle("评论（" + mData + ")");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
