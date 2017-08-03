package com.zhuoxin.dailynews.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zhuoxin.dailynews.R;
import com.zhuoxin.dailynews.base.RCAdapter;
import com.zhuoxin.dailynews.bean.UserInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */

public class RCUserAdapter extends RCAdapter<UserInfo.DataBean.LoginlogBean> {
    public RCUserAdapter(Context context, List<UserInfo.DataBean.LoginlogBean> list, int layoutID) {
        super(context, list, layoutID);
    }

    @Override
    public void getItemView(MyViewHolder holder, int position) {
        UserInfo.DataBean.LoginlogBean mLoginlogBean = mList.get(position);
        holder.setText(R.id.tv_time,mLoginlogBean.getTime());
        holder.setText(R.id.tv_address,mLoginlogBean.getAddress());
        holder.setText(R.id.tv_device,mLoginlogBean.getDevice()+"");
    }
}
