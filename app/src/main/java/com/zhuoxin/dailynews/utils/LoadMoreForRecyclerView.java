package com.zhuoxin.dailynews.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/7/7.
 */

public class LoadMoreForRecyclerView {
    private int state;//滑动状态
    private int mLastVisibleItemPosition;
    private int offsetY;//y轴偏移量
    private float moveY;//Y轴移动距离
    private float oldY;//记录触摸之前的位置


    public LoadMoreForRecyclerView(RecyclerView recyclerView, LoadMoreListener loadMoreListener) {
        width(recyclerView, loadMoreListener);
    }

    public void width(final RecyclerView recyclerView, LoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
        //线性布局管理器
        final LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //更新状态
                state = newState;
                //获取最后一个item的位置
                mLastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                if (state == RecyclerView.SCROLL_STATE_IDLE&&mLastVisibleItemPosition==mLayoutManager.getItemCount()-1&&moveY<0){
                    mLoadMoreListener.loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //记录Y轴偏移量
                offsetY = dy;
            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        //计算出移动的距离
                        moveY = event.getY() - oldY;
                        oldY = event.getY();//记录当前触摸的位置
                        break;
                    case MotionEvent.ACTION_UP:
                        //如果是触摸滑动或者快速滑动并且显示的是适配器中最后一条数据
                        if ((state == RecyclerView.SCROLL_STATE_DRAGGING || state == RecyclerView.SCROLL_STATE_SETTLING) && mLastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
                            if (offsetY > 0) {//偏移量大于0
                                if (mLoadMoreListener != null) {
                                    //回调加载更多方法
                                    mLoadMoreListener.loadListener();
                                }
                            } else if (offsetY == 0) {
                                if (moveY < 0) {
                                    if (mLoadMoreListener != null) {
                                        mLoadMoreListener.loadListener();
                                    }
                                }
                            }
                        }
                        break;

                }
                return false;
            }
        });
    }

    private LoadMoreListener mLoadMoreListener;

    public interface LoadMoreListener {
        void loadListener();
        void loadMore();
    }
}
