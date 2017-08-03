package com.zhuoxin.dailynews.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */

public abstract class RCAdapter<T> extends RecyclerView.Adapter<RCAdapter.MyViewHolder> {
    public Context mContext;
    public List<T> mList;
    private LayoutInflater mLayoutInflater;
    private int mLayoutID;

    public RCAdapter(Context context, List<T> list,int layoutID) {
        mContext = context;
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
        mLayoutID = layoutID;
    }

    public void setList(List<T> list) {
        mList = list;
    }

    public List<T> getList() {
        return mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(mLayoutID, parent, false);
        MyViewHolder mViewHolder = new MyViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        getItemView(holder,position);
    }

    public abstract void getItemView(MyViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        SparseArray<View> mSparseArray = null;
        public MyViewHolder(View itemView) {
            super(itemView);
            mSparseArray = new SparseArray<>();
        }

        public <T extends View>T getView(int viewID){
            View mView = mSparseArray.get(viewID);
            if (mView==null){//如果为空
                mView = itemView.findViewById(viewID);
                mSparseArray.put(viewID,mView);
            }
            return (T) mView;
        }

        public void setText(int viewID,String str){
            TextView tv = getView(viewID);
            tv.setText(str);
        }
    }
}
