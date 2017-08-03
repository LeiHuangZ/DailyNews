package com.zhuoxin.dailynews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhuoxin.dailynews.R;
import com.zhuoxin.dailynews.activity.NewsActivity;
import com.zhuoxin.dailynews.adapter.MyRecyclerViewAdapter;
import com.zhuoxin.dailynews.bean.News;
import com.zhuoxin.dailynews.biz.SaveNewsDB;
import com.zhuoxin.dailynews.listener.RecyclerItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/7/19.
 */

public class NewsCollectionFragment extends Fragment {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.rootView)
    LinearLayout mRootView;
    Unbinder unbinder;
    private MyRecyclerViewAdapter mRecyclerViewAdapter;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<News> mNewsList = (List<News>) msg.obj;
            if (mRecyclerViewAdapter == null) {
                mRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), mNewsList);
                mRecyclerview.setAdapter(mRecyclerViewAdapter);
            } else {
                mRecyclerViewAdapter.setNewsList(mNewsList);
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
            mSrl.setRefreshing(false);
        }
    };
    private List<News> mNewsList;
    private PopupWindow mPopupWindow;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.layout_fragmentlist, container, false);
        unbinder = ButterKnife.bind(this, mView);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        mRecyclerview.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerview, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Intent mIntent = new Intent(getActivity(), NewsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("newsinfo", mNewsList.get(position));
                mIntent.putExtras(mBundle);
                getActivity().startActivity(mIntent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                showPopup(position);
            }
        }));
        getData();
        return mView;
    }

    private void showPopup(final int position) {
        mPopupWindow = new PopupWindow(getActivity());
        View mView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popup, null);
        mPopupWindow.setContentView(mView);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_bg));
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.pop_anim);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);

        TextView deleteOne = (TextView) mView.findViewById(R.id.deleteOne);
        TextView deleteAll = (TextView) mView.findViewById(R.id.deleteAll);
        deleteOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SaveNewsDB.deleteOne(getActivity(), mNewsList.get(position),"news")) {
                    mNewsList.remove(position);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                    mPopupWindow.dismiss();
                }
            }
        });
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SaveNewsDB.deleteAll(getActivity(),"news")) {
                    mNewsList.clear();
                    mRecyclerViewAdapter.notifyDataSetChanged();
                    mPopupWindow.dismiss();
                }
            }
        });

    }

    public void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mNewsList = SaveNewsDB.queryNews(getActivity(),"news");
                Message mMessage = mHandler.obtainMessage();
                mMessage.obj = mNewsList;
                mHandler.sendMessage(mMessage);
            }
        }).start();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
