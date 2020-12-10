package com.ws3dm.app.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ws3dm.app.bean.HomeTabsDBBean;

import java.util.ArrayList;
import java.util.List;

public class TitleFile {
    private DatebaseHelper datebaseHelper;

    public TitleFile(Context mContext) {
        datebaseHelper = new DatebaseHelper(mContext);
    }


    public void add(List<HomeTabsDBBean.HomeTabsData> data) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        for (HomeTabsDBBean.HomeTabsData datum : data) {
            ContentValues values = new ContentValues();
            values.put("aid", datum.getAid());
            values.put("cid", datum.getCid());
            values.put("litpic", datum.getLitpic());
            values.put("title", datum.getTitle());
            values.put("open", datum.isOpen());
            values.put("type", datum.getType());
            db.insert("title_info", null, values);
        }
        db.close();
    }

    public void addOne(HomeTabsDBBean.HomeTabsData data) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("aid", data.getAid());
        values.put("cid", data.getCid());
        values.put("litpic", data.getLitpic());
        values.put("title", data.getTitle());
        values.put("open", 1);
        values.put("type", data.getType());
        db.insert("title_info", null, values);
        db.close();
    }

    public List<HomeTabsDBBean.HomeTabsData> query() {
        SQLiteDatabase db = datebaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from title_info", null);
        List<HomeTabsDBBean.HomeTabsData> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HomeTabsDBBean.HomeTabsData homeTabsData = new HomeTabsDBBean.HomeTabsData();
            String title = cursor.getString(cursor.getColumnIndex("title"));
            int aid = cursor.getInt(cursor.getColumnIndex("aid"));
            int cid = cursor.getInt(cursor.getColumnIndex("cid"));
            String litpic = cursor.getString(cursor.getColumnIndex("litpic"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            int open = cursor.getInt(cursor.getColumnIndex("open"));
            homeTabsData.setAid(aid);
            homeTabsData.setTitle(title);
            homeTabsData.setCid(cid);
            homeTabsData.setLitpic(litpic);
            homeTabsData.setType(type);
            homeTabsData.setOpen(open);
            list.add(homeTabsData);
        }
        db.close();
        return list;
    }

    public void updateAll(List<HomeTabsDBBean.HomeTabsData> data) {
        deleteAll();
        add(data);
    }

    public void update(List<HomeTabsDBBean.HomeTabsData> data) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (HomeTabsDBBean.HomeTabsData datum : data) {
            values.put("litpic", datum.getLitpic());
            values.put("title", datum.getTitle());
            values.put("type", datum.getType());
            values.put("cid", datum.getCid());
            db.update("title_info", values, "aid=?", new String[]{String.valueOf(datum.getAid())});
        }
        db.close();
    }

    public void updateOne(HomeTabsDBBean.HomeTabsData data, int Aid) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cid", data.getCid());
        values.put("litpic", data.getLitpic());
        values.put("title", data.getTitle());
        values.put("type", data.getType());
        db.update("title_info", values, "aid=?", new String[]{String.valueOf(Aid)});
        db.close();
    }

    public void delete(int Aid) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        db.delete("title_info", "aid=?", new String[]{String.valueOf(Aid)});
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        db.delete("title_info", null, null);
        db.close();
    }
}
