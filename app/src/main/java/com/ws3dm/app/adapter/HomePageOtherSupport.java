package com.ws3dm.app.adapter;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.TopTabDetailBean;

public class HomePageOtherSupport implements MultiItemTypeSupport<TopTabDetailBean.DataBean.ListBean> {
    public static final int VIEW_TYPE_NOPIC = 0;
    public static final int VIEW_TYPE_ONE_RIGHT = 1;
    public static final int VIEW_TYPE_MORE_PIC = 2;
    public static final int VIEW_TYPE_LIST = 3;

    @Override
    public int getLayoutId(int itemType) {
        switch (itemType) {
            case VIEW_TYPE_NOPIC:// 无图模式
                return R.layout.adapter_news_no_pic;
            case VIEW_TYPE_ONE_RIGHT:// 单图右侧模式
                return R.layout.adapter_news_one_right_pic;
            case VIEW_TYPE_MORE_PIC:// 多图模式
                return R.layout.adapter_news_more_pic; // text
            case VIEW_TYPE_LIST:// 横向recyclerview，展示踩你喜欢
                return R.layout.adapter_news_horizonal; // text
            default:// 单图左侧模式
                return R.layout.adapter_news_one_right_pic;   // pic
        }
    }

    @Override
    public int getItemViewType(int position, TopTabDetailBean.DataBean.ListBean listBean) {
        return listBean.getBodyimg() == null ? VIEW_TYPE_LIST : listBean.getBodyimg().size() == 3 ? VIEW_TYPE_MORE_PIC : VIEW_TYPE_ONE_RIGHT;
    }
}
