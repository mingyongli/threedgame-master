package com.ws3dm.app.view;

import android.view.View;
import android.view.ViewGroup;
/**
 * Describution : CoverFlowView搭配使用
 * 
 * Author : DKjuan
 * 
 * Date : 2019/6/25 15:02
 **/
public interface ICoverFlowAdapter {

    int getCount();

    Object getItem(int position);

    long getItemId(int position);

    View getView(int position, View convertView, ViewGroup parent);

    void getData(View view, int position);

}
