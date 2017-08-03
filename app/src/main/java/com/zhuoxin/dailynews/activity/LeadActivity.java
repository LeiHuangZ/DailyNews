package com.zhuoxin.dailynews.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuoxin.dailynews.R;
import com.zhuoxin.dailynews.adapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeadActivity extends AppCompatActivity {
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    List<ImageView> mList = new ArrayList<>();
    @BindView(R.id.layout_dot)
    LinearLayout mLayoutDot;
    @BindView(R.id.tv_skip)
    TextView mTvSkip;
    private int mCurrentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mSharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String tag = "isFirst";
        if (mSharedPreferences.getBoolean(tag, true) == false) {
            goToLogoActivity();
        } else {
            setContentView(R.layout.activity_lead);
            ButterKnife.bind(this);


            //初始化ViewPager图片数据
            initViewPagerData();
            //给ViewPager设置适配器
            mViewpager.setAdapter(new MyViewPagerAdapter(mList));

            //初始化圆点
            initDotView();
            //更新圆点的状态
            updateDotview();

            //设置滑动监听，实现滑动时圆点改变
            mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                //页面滑动时
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                //页面选中时
                public void onPageSelected(int position) {
                    //更新圆点的状态
                    updateDotview();
                }

                //页面状态改变时
                public void onPageScrollStateChanged(int state) {
                }
            });


            //设置触摸事件监听，实现最后一页向左滑动跳转到主界面
            mViewpager.setOnTouchListener(new View.OnTouchListener() {
                float startX = 0;
                float endX = 0;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startX = event.getX();
                            break;
                        case MotionEvent.ACTION_UP:
                            endX = event.getX();
                            WindowManager mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                            Point mPoint = new Point();
                            mWindowManager.getDefaultDisplay().getSize(mPoint);
                            int mWidth = mPoint.x;

                            //判断是否最后一页且是否是向左滑动超过屏幕指定宽度
                            if (mCurrentItem == mList.size() - 1 && (startX - endX) > 0 && (startX - endX) >= (mWidth / 4)) {
                                SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor mEdit = sp.edit();
                                String tag = "isFirst";
                                boolean value = false;
                                mEdit.putBoolean(tag, value);
                                mEdit.commit();
                                goToLogoActivity();
                            }
                            break;
                    }

                    return false;
                }
            });
        }
    }

    private void updateDotview() {
        //获取当前页的角标
        mCurrentItem = mViewpager.getCurrentItem();
        for (int i = 0; i < mLayoutDot.getChildCount(); i++) {
            //获取子控件
            View mChildAt = mLayoutDot.getChildAt(i);
            //把当前页的点设置为黑色
            mChildAt.setBackgroundResource(mCurrentItem == i ? R.drawable.dot_selected : R.drawable.dot_unselected);
        }
    }

    private void initDotView() {
        for (int i = 0; i < mList.size(); i++) {
            //创建点的控件
            View mDotView = new View(LeadActivity.this);
            //设置布局参数，设置宽高
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(14, 14);
            if (i > 0) {
                mLayoutParams.leftMargin = 10;
            }
            mDotView.setLayoutParams(mLayoutParams);
            //设置布局
            mLayoutDot.addView(mDotView);
        }
    }

    private void initViewPagerData() {
        ImageView mImageView;
        mImageView = (ImageView) getLayoutInflater().inflate(R.layout.lead_activity_item, null);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setImageResource(R.mipmap.mood_sad);
        mList.add(mImageView);
        mImageView = (ImageView) getLayoutInflater().inflate(R.layout.lead_activity_item, null);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setImageResource(R.mipmap.mood_calm);
        mList.add(mImageView);
        mImageView = (ImageView) getLayoutInflater().inflate(R.layout.lead_activity_item, null);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setImageResource(R.mipmap.mood_happy);
        mList.add(mImageView);

    }

    public void goToLogoActivity() {
        startActivity(new Intent(LeadActivity.this, LogoActivity.class));
        finish();
    }

    @OnClick(R.id.tv_skip)
    public void onViewClicked() {
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEdit = sp.edit();
        String tag = "isFirst";
        boolean value = false;
        mEdit.putBoolean(tag, value);
        mEdit.commit();
        goToLogoActivity();
    }
}
