package com.ws3dm.app.adapter;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.OriginalBean;

/**
 * Describution : 最新原创
 * 
 * Author : DKjuan
 * 
 * Date : 2019/8/8 17:36
 **/
public class LastMultiItemTypeSupport implements MultiItemTypeSupport<OriginalBean> {
    public static final int IMG_NORMAL = 0;
    public static final int IMG_BIG = 1;

    @Override
    public int getLayoutId(int itemType) {
        switch (itemType) {
            case IMG_BIG://大图模式
                return R.layout.adapter_big_original;
            default://正常图片
                return R.layout.adapter_news_original;
        }
    }

    @Override
    public int getItemViewType(int position, OriginalBean model) {
        return model.getIs_showbig()==1?IMG_BIG:IMG_NORMAL;
    }
}