package com.ws3dm.app.adapter;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.MGListBean;

public class MGMultiItemTypeSupport implements MultiItemTypeSupport<MGListBean> {
    public static final int VIEW_TYPE_NOPIC = 1;
    public static final int VIEW_TYPE_ONE_PIC = 0;

    @Override
    public int getLayoutId(int itemType) {
        switch (itemType) {
            case VIEW_TYPE_NOPIC:// 无图模式
                return R.layout.adapter_hot_cate;//和 游戏-热门 共用布局
            case VIEW_TYPE_ONE_PIC:// 有顶部图
                return R.layout.adapter_mg_one_pic;
            default:// 有顶部图
                return R.layout.adapter_mg_one_pic;
        }
    }

    @Override
    public int getItemViewType(int position, MGListBean model) {
        return model.getImgnum();
    }
}
