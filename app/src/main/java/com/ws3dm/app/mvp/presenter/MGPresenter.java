package com.ws3dm.app.mvp.presenter;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.ws3dm.app.Constant;
import com.ws3dm.app.event.ErrorEvent;
import com.ws3dm.app.mvp.model.RespBean.MGChinaRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsCommentRespBean;
import com.ws3dm.app.network.service.MGService;
import com.ws3dm.app.util.EventBus;

/**
 * Describution :手游 Presentmenter
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/16 15:47
 **/
public class MGPresenter extends BasePresenter {

    private static final String TAG = "MGPresenter";

    private static MGPresenter mMGPresenter = null;

    private MGService mMGService;

    public static synchronized MGPresenter getInstance() {
        if (mMGPresenter == null) {
            mMGPresenter = new MGPresenter();
        }
        return mMGPresenter;
    }

    private MGPresenter() {
        mMGService = new MGService();
        registerEvent();
    }

    //获取手游游戏首页页面数据
    public void getGameMG(String body) {
        mMGService.getGameMG(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.MG_GAME_SUCCESS)})
    public void getGameMGResult(NewsCommentRespBean bean) {
        notify(Constant.Notify.RESULT_MG_GAME, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.MG_GAME_FAILURE)})
    public void getGameMGFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //获取手游软件首页页面数据
    public void getSoftMG(String body) {
        mMGService.getSoftMG(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.MG_SOFT_SUCCESS)})
    public void getSoftMGMGResult(NewsCommentRespBean bean) {
        notify(Constant.Notify.RESULT_MG_SOFT, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.MG_SOFT_FAILURE)})
    public void getSoftMGMGFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }


    //手游筛选列表
    public void getChoiceMG(String body) {
        mMGService.getChoiceMG(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.MG_CHOICE_SUCCESS)})
    public void getChoiceMGResult(MGChinaRespBean bean) {
        notify(Constant.Notify.RESULT_MG_CHOICE, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.MG_CHOICE_FAILURE)})
    public void getChoiceMGFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }


    //加载新闻列表内容
    public void postDown(String num) {
        EventBus.getDefault().post(Constant.Event.ADD_DOWNLIST, num);
    }
}
