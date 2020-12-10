package com.ws3dm.app.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/4/11.
 */

public class DatebaseHelper extends SQLiteOpenHelper {
    private static final int version = 2; // 版本号
    private static final String DB_NAME = "3dm.db"; // 数据库名

    public DatebaseHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String FORUM_TABLE = "create table forum(_id integer primary key autoincrement,fid text,cover text,title text,post text,rank text)"; // 论坛表
        String VIDEO_TABLE = "create table video(_id integer primary key autoincrement,time text,play text,appid text,title text,comment text,cover text,cyid text,content text,description text)"; // 视频表
        String GAME_TABLE = "create table game(_id integer primary key autoincrement,tid text,title text,appid text,litpic text,terrace text,release_date text,release_company text,game_trans_name text)"; //  游戏表
        String NEWS_TABLE = "create table news(_id integer primary key autoincrement,time text,type text,appid text,title text,comment text,cover text,url text,cyid text,content text,head text,description text)"; // 新闻表
        String HISTORY_TABLE = "create table history(_id integer primary key autoincrement,appid text,time text,type text,title text,arcurl text,weburl text)"; // 记录表
        String PUSH_TABLE = "create table pushtable(_id integer primary key autoincrement,appid text,time text,type text,title text,arcurl text,weburl text,pubdate_at text,litpic text,showtype text,total_ct text,aid text,click text)"; // 记录表
        String FORUM_DETAIL_TABLE = "create table forum_detail(_id integer primary key autoincrement,tid text,author text,authorid text,dateline text,highlight text,digest text,rate text,comments text,authimg text,subject text,cover text, type text,displayorder text)"; // 帖子表
        String HOME_TITLE = "create table title_info(_id integer primary key autoincrement,aid integer,litpic text,title text ,type integer ,cid integer ,open integer)";
        sqLiteDatabase.execSQL(NEWS_TABLE);
        sqLiteDatabase.execSQL(HISTORY_TABLE);
        sqLiteDatabase.execSQL(PUSH_TABLE);
        sqLiteDatabase.execSQL(GAME_TABLE);
        sqLiteDatabase.execSQL(VIDEO_TABLE);
        sqLiteDatabase.execSQL(FORUM_TABLE);
        sqLiteDatabase.execSQL(FORUM_DETAIL_TABLE);
        sqLiteDatabase.execSQL(HOME_TITLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            String HOME_TITLE = "create table title_info(_id integer primary key autoincrement,aid integer,litpic text,title text ,type integer ,cid integer ,open integer)";
            sqLiteDatabase.execSQL(HOME_TITLE);
        }
    }
}
