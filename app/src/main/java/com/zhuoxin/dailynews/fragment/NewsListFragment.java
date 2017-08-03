package com.zhuoxin.dailynews.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhuoxin.dailynews.R;
import com.zhuoxin.dailynews.activity.NewsActivity;
import com.zhuoxin.dailynews.adapter.MyRecyclerViewAdapter;
import com.zhuoxin.dailynews.bean.News;
import com.zhuoxin.dailynews.biz.SaveNewsDB;
import com.zhuoxin.dailynews.biz.SaveNewsDBHelper;
import com.zhuoxin.dailynews.biz.UrlUtils;
import com.zhuoxin.dailynews.listener.RecyclerItemClickListener;
import com.zhuoxin.dailynews.utils.LoadMoreForRecyclerView;
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
 * Created by Administrator on 2017/7/14.
 */

public class NewsListFragment extends Fragment {


    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    Unbinder unbinder;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    private int mSubid;
    private MyRecyclerViewAdapter mMyRecyclerViewAdapter;
    private List<News> mNewsList = new ArrayList<>();
    List<News> mNewsList1 = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mPB;
    private TextView mTextView;
    private View mChildAt;


    public static NewsListFragment getNewsListFragment(int subid) {
        NewsListFragment mNewsListFragment = new NewsListFragment();
        Bundle mBundle = new Bundle();
        mBundle.putInt("subid", subid);
        mNewsListFragment.setArguments(mBundle);
        return mNewsListFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.layout_fragmentlist, container, false);
        mSubid = getArguments().getInt("subid");
        unbinder = ButterKnife.bind(this, mView);
        mMyRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), mNewsList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mMyRecyclerViewAdapter);

        //获取本地数据库新闻
        SaveNewsDBHelper mHelper = new SaveNewsDBHelper(getActivity());
        SQLiteDatabase mDatabase = mHelper.getReadableDatabase();
        getLocalNews();
        //请求新闻数据
        getData(1);


        mRecyclerview.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerview, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < mNewsList.size()) {
                    Intent mIntent = new Intent(getActivity(), NewsActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("newsinfo", mNewsList.get(position));
                    mIntent.putExtras(mBundle);
                    getActivity().startActivityForResult(mIntent, 3);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        //设置SwipeRefreshLayout的下拉监听
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMyRecyclerViewAdapter.setNewsList(mNewsList1);
                mNewsList.clear();

                mLayoutManager = mRecyclerview.getLayoutManager();
                mChildAt = mLayoutManager.getChildAt(mLayoutManager.getChildCount() - 1);
                mTextView = (TextView) mChildAt.findViewById(R.id.load_more);
                if (mTextView != null) {
                    mPB = (ProgressBar) mChildAt.findViewById(R.id.rcv_load_more);
                    mTextView.setVisibility(View.INVISIBLE);
                    mPB.setVisibility(View.INVISIBLE);
                }

                getData(1);
            }
        });
        mSrl.setColorSchemeColors(Color.CYAN);


        return mView;
    }

    private void getLocalNews() {
        mNewsList1 = SaveNewsDB.queryNews(getActivity(), "local_news", mSubid);
        if (mNewsList1 != null && mNewsList1.size() > 0) {
            mMyRecyclerViewAdapter.setNewsList(mNewsList1);
            mMyRecyclerViewAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        new LoadMoreForRecyclerView(mRecyclerview, new LoadMoreForRecyclerView.LoadMoreListener() {

            @Override
            public void loadListener() {
                mLayoutManager = mRecyclerview.getLayoutManager();
                mChildAt = mLayoutManager.getChildAt(mLayoutManager.getChildCount() - 1);
                mTextView = (TextView) mChildAt.findViewById(R.id.load_more);
                mPB = (ProgressBar) mChildAt.findViewById(R.id.rcv_load_more);
                mTextView.setVisibility(View.INVISIBLE);
                mPB.setVisibility(View.VISIBLE);
                getData(2);
            }

            @Override
            public void loadMore() {
                mChildAt = mLayoutManager.getChildAt(mLayoutManager.getChildCount() - 1);
                mTextView = (TextView) mChildAt.findViewById(R.id.load_more);
                mPB = (ProgressBar) mChildAt.findViewById(R.id.rcv_load_more);
                mTextView.setVisibility(View.VISIBLE);
                mPB.setVisibility(View.INVISIBLE);
                mTextView.setText("更多...");
                mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTextView.setVisibility(View.INVISIBLE);
                        mPB.setVisibility(View.VISIBLE);
                        getData(2);
                    }
                });
            }
        });
    }

    private void getData(final int dir) {
        int nid;
        String stamp;
        OkHttpClient mOkHttpClient = new OkHttpClient();
        if (dir == 1) {
            nid = 0;
            stamp = "0";
        } else {
            if (mNewsList1.size() == 0) {
                return;
            }
            int mI = mRecyclerview.getAdapter().getItemCount() - 2;
            News mNews = mNewsList1.get(mI);
            nid = mNews.nid;
            stamp = mNews.stamp;
        }
        Request mRequest = new Request.Builder()
                .url(UrlUtils.MAIN_URL + "news_list?ver=1&subid=" + mSubid + "&dir=" + dir + "&nid=" + nid + "&stamp=+" + stamp + "&cnt=20")
                .build();
        Call mCall = mOkHttpClient.newCall(mRequest);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.Toast(getActivity(), "新闻列表拉取失败，请重试");
                        mSrl.setRefreshing(false);
                        if (mNewsList1 != null && mChildAt != null) {
                            mTextView = (TextView) mChildAt.findViewById(R.id.load_more);
                            mPB = (ProgressBar) mChildAt.findViewById(R.id.rcv_load_more);
                            if (mPB != null) {
                                mPB.setVisibility(View.INVISIBLE);
                            }
                            if (mTextView != null) {
                                mTextView.setVisibility(View.VISIBLE);
                                if (dir == 2) {
                                    mTextView.setText("服务器抽风啦，点击重试");
                                } else if (dir == 1) {
                                    mTextView.setText("服务器抽风啦，请重新刷新");
                                }
                            }

                        }

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject mJSONObject = new JSONObject(result);
                    if (mJSONObject.getInt("status") == 0) {
                        if (dir == 1) {
                            SaveNewsDB.deleteAll(getActivity(), "local_news", mSubid);
                        }
                        JSONArray mData = mJSONObject.getJSONArray("data");
                        for (int i = 0; i < mData.length(); i++) {
                            mJSONObject = mData.getJSONObject(i);
                            String mStamp = mJSONObject.getString("stamp");
                            String mIcon = mJSONObject.getString("icon");
                            String mTitle = mJSONObject.getString("title");
                            String mSummary = mJSONObject.getString("summary");
                            String mLink = mJSONObject.getString("link");
                            int mNid = mJSONObject.getInt("nid");
                            News mNews = new News(mStamp, mIcon, mTitle, mSummary, mLink, mNid);
                            SaveNewsDB.insertNews(getActivity(), mNews, "local_news", mSubid);
                            mNewsList.add(mNews);
                            mNewsList1.add(mNews);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mMyRecyclerViewAdapter.setNewsList(mNewsList);
                                mMyRecyclerViewAdapter.notifyDataSetChanged();
                                mNewsList1.clear();
                                for (int i = 0; i < mNewsList.size(); i++) {
                                    mNewsList1.add(mNewsList.get(i));
                                }
                                mSrl.setRefreshing(false);
                                if (dir == 2) {
                                    ToastUtil.Toast(getActivity(), "加载更多");
                                    mTextView = (TextView) mChildAt.findViewById(R.id.load_more);
                                    mPB = (ProgressBar) mChildAt.findViewById(R.id.rcv_load_more);
                                    mPB.setVisibility(View.INVISIBLE);
                                    mTextView.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                    } else if (mJSONObject.getInt("status") == -3) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSrl.setRefreshing(false);
                                ToastUtil.Toast(getActivity(), "别拉了，没有了");
                                if (mNewsList1 != null) {
                                    mTextView = (TextView) mChildAt.findViewById(R.id.load_more);
                                    mPB = (ProgressBar) mChildAt.findViewById(R.id.rcv_load_more);
                                    mPB.setVisibility(View.INVISIBLE);
                                    mTextView.setVisibility(View.VISIBLE);
                                    mTextView.setText("已经到底了");
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}


