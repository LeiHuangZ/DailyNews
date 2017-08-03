package com.zhuoxin.dailynews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */

public class MyViewPagerAdapter extends PagerAdapter {
    private List<ImageView> mList;

    public MyViewPagerAdapter(List<ImageView> list) {
        mList = list;
    }

    public int getCount() {
        return mList==null?0:mList.size();
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        ImageView mImageView = mList.get(position);
        container.addView(mImageView);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView mImageView = mList.get(position);
        container.removeView(mImageView);
    }
}
