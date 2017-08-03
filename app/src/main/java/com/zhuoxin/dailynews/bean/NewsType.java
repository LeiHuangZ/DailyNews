package com.zhuoxin.dailynews.bean;

/**
 * Created by Administrator on 2017/7/14.
 */

public class NewsType {
    public String subgroup;
    public int subid;

    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
    }

    public int getSubid() {
        return subid;
    }

    public void setSubid(int subid) {
        this.subid = subid;
    }

    public NewsType(String subgroup, int subid) {
        this.subgroup = subgroup;
        this.subid = subid;
    }

    @Override
    public String toString() {
        return "NewsType{" +
                "subgroup='" + subgroup + '\'' +
                ", subid=" + subid +
                '}';
    }
}
