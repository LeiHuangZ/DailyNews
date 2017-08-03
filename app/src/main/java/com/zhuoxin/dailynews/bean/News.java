package com.zhuoxin.dailynews.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/14.
 */

public class News implements Serializable {
    public String stamp;
    public String icon;
    public String title;
    public String summary;
    public String link;
    public int nid;

    public News(String stamp, String icon, String title, String summary, String link, int nid) {
        this.stamp = stamp;
        this.icon = icon;
        this.title = title;
        this.summary = summary;
        this.link = link;
        this.nid = nid;
    }

    @Override
    public String toString() {
        return "News{" +
                "stamp='" + stamp + '\'' +
                ", icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", link='" + link + '\'' +
                ", nid='" + nid + '\'' +
                '}';
    }
}
