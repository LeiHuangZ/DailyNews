package com.zhuoxin.dailynews.adapter;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.zhuoxin.dailynews.R;
import com.zhuoxin.dailynews.base.RCAdapter;
import com.zhuoxin.dailynews.bean.Comments;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/7/27.
 */

public class RCComAdapter extends RCAdapter {
    public RCComAdapter(Context context, List list, int layoutID) {
        super(context, list, layoutID);
    }

    @Override
    public void getItemView(MyViewHolder holder, int position) {
        Comments.DataBean mDataBean = (Comments.DataBean) mList.get(position);
        CircleImageView ccimg = holder.getView(R.id.portrait);
        Glide.with(mContext).load(mDataBean.getPortrait()).into(ccimg);
        holder.setText(R.id.tv_uid,mDataBean.getUid());
        holder.setText(R.id.tv_stamp,mDataBean.getStamp());
        holder.setText(R.id.tv_comment,mDataBean.getContent());
    }
}
