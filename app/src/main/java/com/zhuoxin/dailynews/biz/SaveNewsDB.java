package com.zhuoxin.dailynews.biz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhuoxin.dailynews.bean.News;
import com.zhuoxin.dailynews.bean.NewsType;
import com.zhuoxin.dailynews.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */

public class SaveNewsDB {

    public static boolean insertType(Context context, NewsType type){
        SaveNewsDBHelper mHelper = new SaveNewsDBHelper(context);
        SQLiteDatabase mDatabase = mHelper.getReadableDatabase();
        Cursor mCursor = mDatabase.rawQuery("select * from "+"news_type"+" where subid = " + type.subid, null);
        while (mCursor.moveToNext()){
            mCursor.close();
            return false;
        }
        mCursor.close();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("subgroup",type.getSubgroup());
        mContentValues.put("subid",type.getSubid());
        long mNews = mDatabase.insert("news_type", null, mContentValues);
        mDatabase.close();
        return true;
    }

    public static List<NewsType> queryType(Context context){
        List<NewsType> mNewsList = new ArrayList<>();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("data/data/com.zhuoxin.dailynews/databases/savenews.db", null);
        Cursor mCursor = db.rawQuery("select * from "+"news_type", null);
        while (mCursor.moveToNext()){
            String mSubgroup = mCursor.getString(mCursor.getColumnIndex("subgroup"));
            int mSubid = mCursor.getInt(mCursor.getColumnIndex("subid"));
            mNewsList.add(new NewsType(mSubgroup,mSubid));
        }
        mCursor.close();
        db.close();
        return mNewsList;

    }

    public static boolean insertNews(Context context, News news,String table){
        SaveNewsDBHelper mHelper = new SaveNewsDBHelper(context);
        SQLiteDatabase mDatabase = mHelper.getReadableDatabase();
        Cursor mCursor = mDatabase.rawQuery("select * from "+table+" where nid = " + news.nid, null);
        while (mCursor.moveToNext()){
            mCursor.close();
            return false;
        }
        mCursor.close();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("stamp",news.stamp);
        mContentValues.put("title",news.title);
        mContentValues.put("summary",news.summary);
        mContentValues.put("icon",news.icon);
        mContentValues.put("link",news.link);
        mContentValues.put("nid",news.nid);
        long mNews = mDatabase.insert(table, null, mContentValues);
        mDatabase.close();
        return true;
    }

    public static boolean insertNews(Context context, News news,String table,int subid){
        SaveNewsDBHelper mHelper = new SaveNewsDBHelper(context);
        SQLiteDatabase mDatabase = mHelper.getReadableDatabase();
        Cursor mCursor = mDatabase.rawQuery("select * from "+table+" where nid = " + news.nid, null);
        while (mCursor.moveToNext()){
            mCursor.close();
            return false;
        }
        mCursor.close();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("stamp",news.stamp);
        mContentValues.put("title",news.title);
        mContentValues.put("summary",news.summary);
        mContentValues.put("icon",news.icon);
        mContentValues.put("link",news.link);
        mContentValues.put("nid",news.nid);
        mContentValues.put("subid",subid);
        long mInsert = mDatabase.insert(table, null, mContentValues);
        mDatabase.close();
        return true;
    }

    public static List<News> queryNews(Context context,String table){
        List<News> mNewsList = new ArrayList<>();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("data/data/com.zhuoxin.dailynews/databases/savenews.db", null);
        Cursor mCursor = db.rawQuery("select * from "+table, null);
        while (mCursor.moveToNext()){
            String mIcon = mCursor.getString(mCursor.getColumnIndex("icon"));
            String stamp = mCursor.getString(mCursor.getColumnIndex("stamp"));
            String title = mCursor.getString(mCursor.getColumnIndex("title"));
            String summary = mCursor.getString(mCursor.getColumnIndex("summary"));
            String link = mCursor.getString(mCursor.getColumnIndex("link"));
            int nid = mCursor.getInt(mCursor.getColumnIndex("nid"));
            News mNews = new News(stamp, mIcon, title, summary, link, nid);
            mNewsList.add(mNews);
        }
        mCursor.close();
        db.close();
        return mNewsList;

    }


    public static List<News> queryNews(Context context,String table,int subid){
        List<News> mNewsList = new ArrayList<>();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("data/data/com.zhuoxin.dailynews/databases/savenews.db", null);
        Cursor mCursor = db.rawQuery("select * from "+table, null);
        while (mCursor.moveToNext()){
            String mIcon = mCursor.getString(mCursor.getColumnIndex("icon"));
            String stamp = mCursor.getString(mCursor.getColumnIndex("stamp"));
            String title = mCursor.getString(mCursor.getColumnIndex("title"));
            String summary = mCursor.getString(mCursor.getColumnIndex("summary"));
            String link = mCursor.getString(mCursor.getColumnIndex("link"));
            int nid = mCursor.getInt(mCursor.getColumnIndex("nid"));
            int mSubid = mCursor.getInt(mCursor.getColumnIndex("subid"));
            if (mSubid == subid){
                News mNews = new News(stamp, mIcon, title, summary, link, nid);
                mNewsList.add(mNews);
            }
        }
        mCursor.close();
        db.close();
        return mNewsList;

    }

    public static boolean deleteOne(Context context,News news,String table){
        SaveNewsDBHelper mSaveNewsDBHelper = new SaveNewsDBHelper(context);
        SQLiteDatabase db = mSaveNewsDBHelper.getReadableDatabase();
        int mDelete = db.delete(table, "nid=?", new String[]{news.nid + ""});
        if (mDelete != -1){
            return true;
        }
        db.close();
        return false;
    }

    public static boolean deleteAll(Context context,String table){
        SaveNewsDBHelper mSaveNewsDBHelper = new SaveNewsDBHelper(context);
        SQLiteDatabase db = mSaveNewsDBHelper.getReadableDatabase();
        int mDelete = db.delete(table, null, null);
        if (mDelete != -1){
            return true;
        }
        db.close();
        return false;
    }

    public static boolean deleteAll(Context context,String table,int subid){
        SaveNewsDBHelper mSaveNewsDBHelper = new SaveNewsDBHelper(context);
        SQLiteDatabase db = mSaveNewsDBHelper.getReadableDatabase();
        int mDelete = db.delete(table, "subid=?", new String[]{subid+""});
        if (mDelete != -1){
            return true;
        }
        db.close();
        return false;
    }
}
