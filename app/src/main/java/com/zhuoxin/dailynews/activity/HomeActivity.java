package com.zhuoxin.dailynews.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.dailynews.R;
import com.zhuoxin.dailynews.fragment.NewsCollectionFragment;
import com.zhuoxin.dailynews.fragment.NewsFragment;
import com.zhuoxin.dailynews.utils.SPUtils;
import com.zhuoxin.dailynews.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/11.
 */

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.design_navigation_view)
    NavigationView mDesignNavigationView;
    @BindView(R.id.drawerlayout)
    DrawerLayout mDrawerlayout;
    private ActionBar mSupportActionBar;
    private NewsFragment mNewsFragment;
    private FragmentManager mSupportFragmentManager = getSupportFragmentManager();
    private Fragment currentFragment;
    private NewsCollectionFragment mNewsCollectionFragment;
    private String title = "新闻";
    private FrameLayout mFl;
    private ImageView mProtrait;
    private TextView mUid;
    private boolean mIsLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mFl = (FrameLayout) mDesignNavigationView.getHeaderView(0);
        mUid = (TextView) mFl.findViewById(R.id.uid);
        mProtrait = (ImageView) mFl.findViewById(R.id.portrait);


        //判断登录状态
        mIsLogin = SPUtils.getLoginState(HomeActivity.this);
        if (mIsLogin) {
            mProtrait.setImageBitmap(BitmapFactory.decodeFile(SPUtils.getFilePath(HomeActivity.this)));
            mUid.setText(SPUtils.getUserName(HomeActivity.this));
        }

        initView();

        //设置侧滑菜单的宽度为屏幕的一半
        initmDesignNavigationView();

    }


    private void initView() {
        mToolBar.setTitle("新闻");
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolBar);
        mSupportActionBar = getSupportActionBar();
        //显示菜单图标和标题
        //mSupportActionBar.setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerlayout, mToolBar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mSupportActionBar.setTitle("菜单");
            }

            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mSupportActionBar.setTitle(title);
            }
        };
        //设置Drawerlayout打开和关闭时图标样式
        mActionBarDrawerToggle.syncState();
        mDrawerlayout.addDrawerListener(mActionBarDrawerToggle);

        //使用fragment替换原来的布局
        mNewsFragment = new NewsFragment();
        showFragment(mNewsFragment);

        //为NavigationView设置条目点击
        mDesignNavigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.news:
                    if (mNewsFragment == null) {
                        mNewsFragment = new NewsFragment();
                    }
                    showFragment(mNewsFragment);
                    title = "新闻";
                    mToolBar.setTitle(title);
                    mDrawerlayout.closeDrawer(Gravity.START);
                    break;
                case R.id.collection:
                    if (mNewsCollectionFragment == null) {
                        mNewsCollectionFragment = new NewsCollectionFragment();
                    }
                    if (mNewsCollectionFragment.isAdded()) {
                        mNewsCollectionFragment.getData();
                    }
                    showFragment(mNewsCollectionFragment);
                    title = "收藏";
                    mToolBar.setTitle(title);
                    mDrawerlayout.closeDrawer(Gravity.START);
                    break;
            }
            return true;
        }
    };


    private void initmDesignNavigationView() {
        WindowManager mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Point mPoint = new Point();
        mWindowManager.getDefaultDisplay().getSize(mPoint);
        int mWidth = mPoint.x;
        int mHeight = mPoint.y;

        ViewGroup.LayoutParams mLayoutParams = mDesignNavigationView.getLayoutParams();
        mLayoutParams.width = mWidth / 3 * 2;
        mLayoutParams.height = mHeight;
        mDesignNavigationView.setLayoutParams(mLayoutParams);

        mFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsLogin = SPUtils.getLoginState(HomeActivity.this);
                if (!mIsLogin) {
                    startActivityForResult(new Intent(HomeActivity.this, LoginActivity.class), 1);
                } else {
                    startActivityForResult(new Intent(HomeActivity.this, UserActivity.class), 2);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {//从用户界面返回时
            mIsLogin = SPUtils.getLoginState(HomeActivity.this);
            if (!mIsLogin) {
                mProtrait.setImageDrawable(getResources().getDrawable(R.mipmap.user));
                mUid.setText("点击登录");
                return;
            }
        }
        mIsLogin = SPUtils.getLoginState(HomeActivity.this);
        if (mIsLogin){
            String mFilePath = SPUtils.getFilePath(HomeActivity.this);
            mProtrait.setImageBitmap(BitmapFactory.decodeFile(mFilePath));
            mUid.setText(SPUtils.getUserName(HomeActivity.this));
        }
    }

    private void showFragment(Fragment fragment) {
        final FragmentTransaction mFt = mSupportFragmentManager.beginTransaction();
        if (fragment == null) {
            return;
        }
        if (currentFragment == null) {
            currentFragment = fragment;
            mFt.add(R.id.ll_content, fragment);
            mFt.show(fragment);
            mFt.commit();
        }
        if (currentFragment.getClass().getSimpleName().equals(fragment.getClass().getSimpleName())) {
            return;
        } else {
            if (!fragment.isAdded()) {
                mFt.add(R.id.ll_content, fragment);
                mFt.show(fragment);
            } else {
                mFt.show(fragment);
            }
            mFt.hide(currentFragment);
            mFt.commit();
            currentFragment = fragment;

        }
    }

    /**
     * 重写返回键按压事件，实现双击退出应用
     */
    private long firstTime = 0;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            ToastUtil.Toast(HomeActivity.this, "再按一次返回键退出应用");
            firstTime = secondTime;
        } else {
            HomeActivity.this.finish();
        }
    }
}
