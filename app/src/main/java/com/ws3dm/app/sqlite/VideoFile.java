package com.ws3dm.app.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ws3dm.app.bean.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class VideoFile {
    private DatebaseHelper datebaseHelper;

    public VideoFile(Context context) {
        datebaseHelper = new DatebaseHelper(context);
    }

    // 增加
    public void add(Video video) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("appid", video.getId());
        contentValues.put("time", video.getSenddate());
        contentValues.put("play", video.getClick());
        contentValues.put("cyid", video.getChangyan_id());
        contentValues.put("cover", video.getVideopic());
        contentValues.put("title", video.getTitle());
        contentValues.put("comment", video.getFeedback());
        contentValues.put("description", video.getDescription());
        db.insert("video", null, contentValues);
        db.close();
    }

    // 删除
    public void delete(String appid) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        db.delete("video", "appid" + "=?", new String[]{appid});
        db.close();
    }

    // 查询
    public List<Video> query() {
        List<Video> list = new ArrayList<>();
        SQLiteDatabase db = datebaseHelper.getReadableDatabase();
        Cursor cursor = db.query("video", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Video video = new Video();
            video.setId(cursor.getString(cursor.getColumnIndex("appid")));
            video.setSenddate(cursor.getString(cursor.getColumnIndex("time")));
            video.setClick(cursor.getString(cursor.getColumnIndex("play")));
            video.setChangyan_id(cursor.getString(cursor.getColumnIndex("cyid")));
            video.setVideopic(cursor.getString(cursor.getColumnIndex("cover")));
            video.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            video.setFeedback(cursor.getString(cursor.getColumnIndex("comment")));
            video.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            list.add(video);
        }
        db.close();
        return list;
    }
}
