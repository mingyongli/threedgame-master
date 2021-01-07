package com.ws3dm.app.view.delrecyclerview;

import android.view.View;

/**
 * Created by prj on 2016/10/25.
 */
public interface OnItemDelClickListener {
    /**
     * item点击回调
     *
     * @param view
     * @param position
     */
    void onItemClick(View view, int position);

    /**
     * 删除按钮回调
     *
     * @param position
     */
    void onDeleteClick(int position);
}
