package com.ws3dm.app.mvp.presenter;

import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.util.EventBus;

/**
 * Describution :友盟消息推送 Presentmenter
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/16 15:47
 **/
public class UMengPresenter extends BasePresenter {

    private static final String TAG = "UMengPresenter";

    private static UMengPresenter mUMengPresenter = null;

    public static synchronized UMengPresenter getInstance() {
        if (mUMengPresenter == null) {
            mUMengPresenter = new UMengPresenter();
        }
        return mUMengPresenter;
    }

    private UMengPresenter() {
        registerEvent();
    }

    //加载新闻列表内容
    public void postNews(NewsBean newsBean) {
        EventBus.getDefault().post(Constant.Event.UMENG_PUSH_NEWS, newsBean);
    }

}
