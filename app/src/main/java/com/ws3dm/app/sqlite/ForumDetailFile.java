package com.ws3dm.app.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ws3dm.app.bean.ForumDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/27.
 */

public class ForumDetailFile {
    private DatebaseHelper datebaseHelper;

    public ForumDetailFile(Context context) {
        datebaseHelper = new DatebaseHelper(context);
    }

    // 增加
    public void add(ForumDetail forum) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tid", forum.getTid());
        contentValues.put("rate", forum.getRate());
        contentValues.put("digest", forum.getDigest());
        contentValues.put("author", forum.getAuthor());
        contentValues.put("authimg", forum.getAuthimg());
        contentValues.put("subject", forum.getSubject());
        contentValues.put("authorid", forum.getAuthorid());
        contentValues.put("comments", forum.getComments());
        contentValues.put("dateline", forum.getDateline());
        contentValues.put("highlight", forum.getHighlight());
        contentValues.put("displayorder", forum.getDisplayorder());
        if (forum.getLitpic() == null) {
            contentValues.put("type", "0");
            contentValues.put("cover", "");
        } else if (forum.getLitpic().size() == 1) {
            contentValues.put("type", "1");
            contentValues.put("cover", forum.getLitpic().get(0));
        } else {
            contentValues.put("type", "2");
            String cover = "";
            for (int i = 0; i < 3; i++) {
                if (i == 2)
                    cover += forum.getLitpic().get(i);
                else
                    cover += forum.getLitpic().get(i) + "~";
            }
            contentValues.put("cover", cover);
        }
        db.insert("forum_detail", null, contentValues);
        db.close();
    }

    // 删除
    public void delete(String tid) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        db.delete("forum_detail", "tid" + "=?", new String[]{tid});
        db.close();
    }

    // 查询
    public List<ForumDetail> query() {
        List<ForumDetail> list = new ArrayList<>();
        SQLiteDatabase db = datebaseHelper.getReadableDatabase();
        Cursor cursor = db.query("forum_detail", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            ForumDetail forum = new ForumDetail();
            forum.setTid(cursor.getString(cursor.getColumnIndex("tid")));
            forum.setRate(cursor.getString(cursor.getColumnIndex("rate")));
            forum.setArttype(cursor.getString(cursor.getColumnIndex("type")));
            forum.setImgUrl(cursor.getString(cursor.getColumnIndex("cover")));
            forum.setDigest(cursor.getString(cursor.getColumnIndex("digest")));
            forum.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            forum.setAuthimg(cursor.getString(cursor.getColumnIndex("authimg")));
            forum.setSubject(cursor.getString(cursor.getColumnIndex("subject")));
            forum.setAuthorid(cursor.getString(cursor.getColumnIndex("authorid")));
            forum.setComments(cursor.getString(cursor.getColumnIndex("comments")));
            forum.setDateline(cursor.getString(cursor.getColumnIndex("dateline")));
            forum.setHighlight(cursor.getString(cursor.getColumnIndex("highlight")));
            forum.setDisplayorder(cursor.getString(cursor.getColumnIndex("displayorder")));
            list.add(forum);
        }
        db.close();
        return list;
    }
}
