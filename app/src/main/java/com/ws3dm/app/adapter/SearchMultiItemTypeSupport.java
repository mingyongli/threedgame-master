package com.ws3dm.app.adapter;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.SearchBean;

/**
 * Describution : 搜索复合适配器
 * 
 * Author : DKjuan
 * 
 * Date : 2018/8/11 14:04
 **/
public class SearchMultiItemTypeSupport implements MultiItemTypeSupport<SearchBean> {
    public static final int PC_SPECIAL = 3;
    public static final int PC_NEWS = 1;
    public static final int PC_GONGLUE = 2;
    public static final int PC_ORIGINAL = 7;
    
    public static final int MG_YINGYONG = 4;
    public static final int MG_NEWS = 1;
    public static final int MG_GONGLUE = 2;
    public static final int MG_ORIGINAL= 7;
    
    public static final int WY_NEWS = 1;
    public static final int WY_GONGLUE = 2;
    public static final int WY_DIANJING = 16;
    public static final int WY_VIDEO = 10;
    public static final int WY_PINGCE = 6;
    //1:单机专区 7:单机新闻 8:单机攻略 9:单机原创
    // 10:手游应用 11:手游新闻 12:手游攻略 13:手游原创
    // 14:网游新闻 15:网游攻略 16:网游电竞 17:网游视频 18:网游评测
    //showtype  1新闻2攻略3游戏4手游5杂谈6评测7原创8安利9礼包10视频11专栏12节目13投票14专题h5页面

    @Override
    public int getLayoutId(int itemType) {
        switch (itemType) {
            case WY_NEWS:// 新闻
                return R.layout.adapter_news_one_right_pic;
            case MG_YINGYONG://手游应用
                return R.layout.item_game_horizon;
            case PC_SPECIAL:// PC专题
                return R.layout.adapter_game;
            case WY_VIDEO:// 视频
                return R.layout.adapter_video;
            default://新闻
                return R.layout.adapter_news_one_right_pic;
        }
    }

    @Override
    public int getItemViewType(int position, SearchBean model) {
        return model.getShowtype();
    }
}