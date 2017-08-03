package com.zhuoxin.dailynews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.zhuoxin.dailynews.bean.NewsType;
import com.zhuoxin.dailynews.fragment.NewsListFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class MyFragmentListAdapter extends FragmentPagerAdapter {
    private List<NewsType> mTypeList;
    private List<NewsListFragment> mFragmentList;


    public void setTypeList(List<NewsType> typeList) {
        mTypeList = typeList;
    }

    public void setFragmentList(List<NewsListFragment> fragmentList) {
        mFragmentList = fragmentList;
    }

    public MyFragmentListAdapter(FragmentManager fm, List<NewsType> TypeList, List<NewsListFragment> FragmentList) {
        super(fm);
        mTypeList = TypeList;
        mFragmentList = FragmentList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTypeList.get(position).subgroup;
    }

    //返回条目的数据
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mTypeList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
