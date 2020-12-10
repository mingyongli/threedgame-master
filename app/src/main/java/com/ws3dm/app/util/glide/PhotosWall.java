package com.ws3dm.app.util.glide;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.GridView;

import com.ws3dm.app.activity.CheckPermissionsActivity;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.R;

import java.util.ArrayList;

public class PhotosWall extends CheckPermissionsActivity {
/**
 * Describution :展示手机上所有照片，使用时记得注册activity
 * 
 * Author : DKjuan
 * 
 * Date : 2017/7/11 10:18
 **/

    private ArrayList<String> img_paths;

    GridView gridView;

    private PhotoAdapter adapter;

    @Override
    protected void init() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_wall);
        AppUtil.verifyStoragePermissions(this);
        gridView= (GridView) findViewById(R.id.photos_grid);
        img_paths = new ArrayList<String>();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        //遍历相册
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            //将图片路径添加到集合
            img_paths.add(path);
        }
        cursor.close();

        initlist();
    }

    private void initlist() {
        adapter  = new PhotoAdapter(this,img_paths);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void hasGetAllPermissions() {
        ToastUtil.showToast(this,"开始了");
    }
}
