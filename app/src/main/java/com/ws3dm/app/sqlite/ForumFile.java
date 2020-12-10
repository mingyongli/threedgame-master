package com.ws3dm.app.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ws3dm.app.bean.Forum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */

public class ForumFile {
    private DatebaseHelper datebaseHelper;

    public ForumFile(Context context) {
        datebaseHelper = new DatebaseHelper(context);
    }

    // 增加
    public void add(Forum forum) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fid", forum.getFid());
        contentValues.put("rank", forum.getRank());
        contentValues.put("cover", forum.getIcon());
        contentValues.put("title", forum.getName());
        contentValues.put("post", forum.getTodayposts());
        db.insert("forum", null, contentValues);
        db.close();
    }

    // 删除
    public void delete(String fid) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        db.delete("forum", "fid" + "=?", new String[]{fid});
        db.close();
    }

    // 查询
    public List<Forum> query() {
        List<Forum> list = new ArrayList<>();
        SQLiteDatabase db = datebaseHelper.getReadableDatabase();
        Cursor cursor = db.query("forum", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Forum forum = new Forum();
            forum.setFid(cursor.getString(cursor.getColumnIndex("fid")));
            forum.setRank(cursor.getString(cursor.getColumnIndex("rank")));
            forum.setName(cursor.getString(cursor.getColumnIndex("title")));
            forum.setIcon(cursor.getString(cursor.getColumnIndex("cover")));
            forum.setTodayposts(cursor.getString(cursor.getColumnIndex("post")));
            list.add(forum);
        }
        db.close();
        return list;
    }
}
