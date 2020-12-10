package com.ws3dm.app.sqlite;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.ws3dm.app.bean.News;
import com.ws3dm.app.bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class NewsFile {
    private DatebaseHelper datebaseHelper;

    public NewsFile(Context context) {
        datebaseHelper = new DatebaseHelper(context);
    }

    // 增加
    public void add(News news) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("appid", news.getId());
        contentValues.put("time", news.getTime());
        contentValues.put("cyid", news.getCyId());
        contentValues.put("head", news.getLmfl());
        contentValues.put("url", news.getArcurl());
        contentValues.put("title", news.getTitle());
        contentValues.put("comment", news.getComment());
        contentValues.put("description", news.getDescription());
        if (news.getPhoto() == null) {
            contentValues.put("type", "0");
            contentValues.put("cover", "");
        } else if (news.getPhoto().size() == 1) {
            contentValues.put("type", "1");
            contentValues.put("cover", news.getPhoto().get(0));
        } else {
            contentValues.put("type", "2");
            String cover = "";
            for (int i = 0; i < 3; i++) {
                if (i == 2)
                    cover += news.getPhoto().get(i);
                else
                    cover += news.getPhoto().get(i) + "~";
            }
            contentValues.put("cover", cover);
        }
        db.insert("news", null, contentValues);
        db.close();
    }

    // 删除
    public void delete(String appid) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        db.delete("news", "appid" + "=?", new String[]{appid});
        db.close();
    }

    // 查询
    public List<News> query() {
        List<News> list = new ArrayList<>();
        SQLiteDatabase db = datebaseHelper.getReadableDatabase();
        Cursor cursor = db.query("news", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            News news = new News();
            news.setId(cursor.getString(cursor.getColumnIndex("appid")));
            news.setTime(cursor.getString(cursor.getColumnIndex("time")));
            news.setType(cursor.getString(cursor.getColumnIndex("type")));
            news.setLmfl(cursor.getString(cursor.getColumnIndex("head")));
            news.setCyId(cursor.getString(cursor.getColumnIndex("cyid")));
            news.setArcurl(cursor.getString(cursor.getColumnIndex("url")));
            news.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            news.setImgUrl(cursor.getString(cursor.getColumnIndex("cover")));
            news.setComment(cursor.getString(cursor.getColumnIndex("comment")));
            news.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            list.add(news);
        }
        db.close();
        return list;
    }

    // 增加历史记录 临时 新增htmlbean
    public void addHistory(NewsBean news) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("appid", news.getId());
        contentValues.put("time", news.getSeeDate());
        contentValues.put("type", news.getType());
        contentValues.put("title", news.getTitle());
        contentValues.put("arcurl", news.getArcurl());
        contentValues.put("weburl", news.getWebviewurl());
        db.insert("history", null, contentValues);
        db.close();
    }

    // 查询历史记录
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public List<NewsBean> queryHistory() {
        List<NewsBean> list = new ArrayList<>();
        SQLiteDatabase db = datebaseHelper.getReadableDatabase();
//        Cursor cursor = db.query("history", null, null, null, null, null, null);
        Cursor cursor = db.query(true, "history", new String[]{"_id","appid","time","type","title","arcurl","weburl"}, null, null, "title", null, null, null, null);//去重
        while (cursor.moveToNext()) {
            NewsBean news = new NewsBean();
            news.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("appid"))));
            news.setSeeDate(cursor.getString(cursor.getColumnIndex("time")));
            news.setType(cursor.getString(cursor.getColumnIndex("type")));
            news.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            news.setArcurl(cursor.getString(cursor.getColumnIndex("arcurl")));
            news.setWebviewurl(cursor.getString(cursor.getColumnIndex("weburl")));
            list.add(news);
        }
        db.close();
        return list;
    }

    public void clearHistory(){
        SQLiteDatabase db = datebaseHelper.getReadableDatabase();
        db.execSQL("delete from history");
        db.close();
    }

    // 增加推送消息
    public void addPush(NewsBean news) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("appid", news.getId());
        contentValues.put("time", news.getSeeDate());
        contentValues.put("type", news.getType());
        contentValues.put("title", news.getTitle());
        contentValues.put("arcurl", news.getArcurl());
        contentValues.put("weburl", news.getWebviewurl());
        // pubdate_at  litpic showtype total_ct   aid  click
        contentValues.put("pubdate_at", news.getPubdate_at());
        contentValues.put("litpic", news.getLitpic());
        contentValues.put("showtype", news.getShowtype());
        contentValues.put("total_ct", news.getTotal_ct());
        contentValues.put("aid", news.getAid());
        contentValues.put("click", news.getClick());
        
        db.insert("pushtable", null, contentValues);
        db.close();
    }

    // 查询历史记录
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public List<NewsBean> queryPush() {
        List<NewsBean> list = new ArrayList<>();
        SQLiteDatabase db = datebaseHelper.getReadableDatabase();
//        Cursor cursor = db.query("history", null, null, null, null, null, null);
        Cursor cursor = db.query(true, "pushtable", new String[]{"_id","appid","time","type","title","arcurl","weburl","pubdate_at","litpic","showtype","total_ct","aid","click"}, null, null, "title", null, null, null, null);//去重
        while (cursor.moveToNext()) {
            NewsBean news = new NewsBean();
            news.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("appid"))));
            news.setSeeDate(cursor.getString(cursor.getColumnIndex("time")));
            news.setType(cursor.getString(cursor.getColumnIndex("type")));
            news.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            news.setArcurl(cursor.getString(cursor.getColumnIndex("arcurl")));
            news.setWebviewurl(cursor.getString(cursor.getColumnIndex("weburl")));
            // pubdate_at  litpic showtype total_ct   aid  click
            news.setPubdate_at(Integer.parseInt(cursor.getString(cursor.getColumnIndex("pubdate_at"))));
            news.setLitpic(cursor.getString(cursor.getColumnIndex("litpic")));
            news.setShowtype(Integer.parseInt(cursor.getString(cursor.getColumnIndex("showtype"))));
            news.setTotal_ct(Integer.parseInt(cursor.getString(cursor.getColumnIndex("total_ct"))));
            news.setAid(Integer.parseInt(cursor.getString(cursor.getColumnIndex("aid"))));
            news.setClick(Integer.parseInt(cursor.getString(cursor.getColumnIndex("click"))));
            list.add(news);
        }
        db.close();
        return list;
    }

    public void clearPush(){
        SQLiteDatabase db = datebaseHelper.getReadableDatabase();
        db.execSQL("delete from pushtable");
        db.close();
    }
}
