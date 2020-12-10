package com.ws3dm.app.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ws3dm.app.bean.GameBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class GameFile {
    private DatebaseHelper datebaseHelper;

    public GameFile(Context context) {
        datebaseHelper = new DatebaseHelper(context);
    }

    // 增加
    public void add(GameBean game) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put("appid", game.getId());
//        contentValues.put("tid", game.getType());
//        contentValues.put("title", game.getName());
//        contentValues.put("litpic", game.getCover());
//        contentValues.put("terrace", game.getPlatform());
//        contentValues.put("release_date", game.getTime());
//        contentValues.put("release_company", game.getPublisher());
//        contentValues.put("game_trans_name", game.getOfficiaName());
        db.insert("game", null, contentValues);
        db.close();
    }

    // 删除
    public void delete(String appid) {
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        db.delete("game", "appid" + "=?", new String[]{appid});
        db.close();
    }

    // 查询
    public List<GameBean> query() {
        List<GameBean> list = new ArrayList<>();
        SQLiteDatabase db = datebaseHelper.getReadableDatabase();
        Cursor cursor = db.query("game", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            GameBean game = new GameBean();
//            game.setId(cursor.getString(cursor.getColumnIndex("appid")));
//            game.setType(cursor.getString(cursor.getColumnIndex("tid")));
//            game.setName(cursor.getString(cursor.getColumnIndex("title")));
//            game.setCover(cursor.getString(cursor.getColumnIndex("litpic")));
//            game.setPlatform(cursor.getString(cursor.getColumnIndex("terrace")));
//            game.setTime(cursor.getString(cursor.getColumnIndex("release_date")));
//            game.setPublisher(cursor.getString(cursor.getColumnIndex("release_company")));
//            game.setOfficiaName(cursor.getString(cursor.getColumnIndex("game_trans_name")));
            list.add(game);
        }
        db.close();
        return list;
    }
}
