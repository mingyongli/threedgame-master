package com.ws3dm.app.adapter;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.NewsBean;

public class NewsENTMultiItemTypeSupport implements MultiItemTypeSupport<NewsBean> {
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
                //不显示
                return R.layout.null_view; // text
            default:// 单图左侧模式
                return R.layout.adapter_news_one_right_pic;   // pic
        }
    }

    @Override
    public int getItemViewType(int position, NewsBean model) {
        //
//        if(SharedUtil.getSharedPreferencesData("numPic")==null||"".equals(SharedUtil.getSharedPreferencesData("numPic"))){
//            return 0;
//        }else if("2".equals(SharedUtil.getSharedPreferencesData("numPic"))){//图片数目为3返回多图，为1返回右侧
//            return model.getLitpic()==null?0:model.getLitpic().size()==3?2:3;
//        }else{
//            return Integer.parseInt(SharedUtil.getSharedPreferencesData("numPic"));
//        }
//        return model.getBodyimg()!=null&&model.getBodyimg().size()==1?VIEW_TYPE_MORE_PIC:VIEW_TYPE_ONE_RIGHT;
        return model.getBodyimg()==null?VIEW_TYPE_LIST:model.getBodyimg().size()==3?VIEW_TYPE_MORE_PIC:VIEW_TYPE_ONE_RIGHT;
    }
}