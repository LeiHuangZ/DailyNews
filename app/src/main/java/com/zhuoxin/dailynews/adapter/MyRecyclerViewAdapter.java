package com.zhuoxin.dailynews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuoxin.dailynews.R;
import com.zhuoxin.dailynews.bean.News;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/14.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<News> mNewsList;
    private int VIEW_ITEM = 0;
    private int VIEW_FOOT = 1;

    public void setNewsList(List<News> newsList) {
        mNewsList = newsList;
    }

    public MyRecyclerViewAdapter(Context context, List<News> newsList) {
        mContext = context;
        mNewsList = newsList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mNewsList.size()) {
            return VIEW_FOOT;
        }
        return VIEW_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_rec_item_home, parent, false);
        View mLoad = LayoutInflater.from(mContext).inflate(R.layout.layout_loading, parent, false);
        if (viewType == VIEW_ITEM) {
            RecyclerView.ViewHolder mHolder = new MyViewHolder(mView);
            return mHolder;
        } else if (viewType == VIEW_FOOT) {
            RecyclerView.ViewHolder mHolder = new FooterViewHolder(mLoad);
            return mHolder;
        }
        return null;
    }


    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < mNewsList.size() && position != mNewsList.size()) {
            try {
                MyViewHolder mHolder = (MyViewHolder) holder;
                final News mNews = mNewsList.get(position);
                String mIcon = mNews.icon;
                Picasso.with(mContext).load(mIcon).into(mHolder.mIcon);
                String mTitle = mNews.title;
                mHolder.mTvTitle.setText(mTitle);
                mHolder.mTvSummary.setText(mNews.summary);
                mHolder.mTvStamp.setText(mNews.stamp);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    @Override
    public int getItemCount() {
        return mNewsList == null ? 0 : mNewsList.size() + 1;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mIcon;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_summary)
        TextView mTvSummary;
        @BindView(R.id.tv_stamp)
        TextView mTvStamp;
        View mView;

        MyViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
            mIcon = (ImageView) view.findViewById(R.id.icon);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
