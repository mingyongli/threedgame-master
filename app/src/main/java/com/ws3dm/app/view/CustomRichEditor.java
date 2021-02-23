package com.ws3dm.app.view;

import android.content.Context;
import android.util.AttributeSet;

import jp.wasabeef.richeditor.RichEditor;

public class CustomRichEditor extends RichEditor {
    public CustomRichEditor(Context context) {
        super(context);
    }

    public CustomRichEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //图片自适应
    public void insertAutoImage(String url, String alt) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertImageWH('" + url + "', '" + alt + "','" + "100%" + "', '" + "auto" + "');");
    }
}
