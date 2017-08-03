package com.zhuoxin.dailynews.fragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuoxin.dailynews.R;
import com.zhuoxin.dailynews.adapter.MyFragmentListAdapter;
import com.zhuoxin.dailynews.bean.NewsType;
import com.zhuoxin.dailynews.biz.SaveNewsDB;
import com.zhuoxin.dailynews.biz.SaveNewsDBHelper;
import com.zhuoxin.dailynews.biz.UrlUtils;
import com.zhuoxin.dailynews.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/12.
 */

public class NewsFragment extends Fragment {
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager_main)
    ViewPager mViewPagerMain;
    Unbinder unbinder;
    private MyFragmentListAdapter mMyFragmentListAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.layout_fragment_news, null);
        unbinder = ButterKnife.bind(this, mView);

        /**
         * 请求服务器数据
         */
        OkHttpClient mBuild = new OkHttpClient.Builder()
                .build();
        Request mRequest = new Request.Builder()
                .url(UrlUtils.MAIN_URL + "news_sort?ver=1&imei=1")
                .build();
        Call mCall = mBuild.newCall(mRequest);
        mCall.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.Toast(getActivity(), "服务器连接失败");

                        //获取本地数据库数据
                        getLocalData();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                //定义数组存放数据
                final List<NewsType> mTypeList = new ArrayList<>();
                //定义数组存放NewsListFragment
                final List<NewsListFragment> mFragmentList = new ArrayList<>();
                /**
                 * json解析数据
                 */
                try {

                    JSONObject mObject = new JSONObject(result);
                    if (mObject.getInt("status") == 0) {
                        JSONArray mData = mObject.getJSONArray("data");
                        //清空本地数据库
                        SaveNewsDB.deleteAll(getActivity(), "news_type");
                        for (int i = 0; i < mData.length(); i++) {
                            mObject = mData.getJSONObject(i);
                            JSONArray mSubgrp = mObject.getJSONArray("subgrp");
                            for (int j = 0; j < mSubgrp.length(); j++) {
                                mObject = mSubgrp.getJSONObject(j);
                                String mSubgroup = mObject.getString("subgroup");
                                int mSubid = mObject.getInt("subid");
                                NewsType mNewsType = new NewsType(mSubgroup, mSubid);
                                //将标题数据存入本地数据库
                                SaveNewsDB.insertType(getActivity(), mNewsType);
                                mTypeList.add(mNewsType);
                                NewsListFragment mNewsListFragment = NewsListFragment.getNewsListFragment(mNewsType.subid);
                                mFragmentList.add(mNewsListFragment);
                            }

                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //为ViewPager设置适配器
                                if (mMyFragmentListAdapter == null) {
                                    mMyFragmentListAdapter = new MyFragmentListAdapter(getActivity().getSupportFragmentManager(), mTypeList, mFragmentList);
                                    mViewPagerMain.setAdapter(mMyFragmentListAdapter);
                                } else {
                                    mMyFragmentListAdapter.setFragmentList(mFragmentList);
                                    mMyFragmentListAdapter.setTypeList(mTypeList);
                                    mMyFragmentListAdapter.notifyDataSetChanged();
                                }
                                mTabLayout.setupWithViewPager(mViewPagerMain, true);
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return mView;
    }

    private void getLocalData() {
        SaveNewsDBHelper mSaveNewsDBHelper = new SaveNewsDBHelper(getActivity());
        mSaveNewsDBHelper.getReadableDatabase();
        List<NewsType> mNewsTypes = SaveNewsDB.queryType(getActivity());
        List<NewsListFragment> mFragmentList = new ArrayList<>();
        if (mNewsTypes != null && mNewsTypes.size() > 0) {
            for (int i = 0; i < mNewsTypes.size(); i++) {
                NewsType mNewsType = mNewsTypes.get(i);
                NewsListFragment mNewsListFragment = NewsListFragment.getNewsListFragment(mNewsType.subid);
                mFragmentList.add(mNewsListFragment);
            }
            //为ViewPager设置适配器
            if (mMyFragmentListAdapter == null) {
                mMyFragmentListAdapter = new MyFragmentListAdapter(getActivity().getSupportFragmentManager(), mNewsTypes, mFragmentList);
                mViewPagerMain.setAdapter(mMyFragmentListAdapter);
            } else {
                mMyFragmentListAdapter.setTypeList(mNewsTypes);
                mMyFragmentListAdapter.setFragmentList(mFragmentList);
                mMyFragmentListAdapter.notifyDataSetChanged();
            }
            mTabLayout.setupWithViewPager(mViewPagerMain, true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
