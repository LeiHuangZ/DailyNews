package com.zhuoxin.dailynews.biz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/7/19.
 */

public class SaveNewsDBHelper extends SQLiteOpenHelper {
    public SaveNewsDBHelper(Context context) {
        super(context, "savenews.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table news(_id integer primary key autoincrement, stamp text," + "icon text," + "title text," + "summary text," + "link text," + "nid integer)");
        db.execSQL("create table local_news(_id integer primary key autoincrement, stamp text," + "icon text," + "title text," + "summary text," + "link text," + "nid integer,"+"subid integer)");
        db.execSQL("create table news_type(id integer primary key autoincrement,subgroup text,subid integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
