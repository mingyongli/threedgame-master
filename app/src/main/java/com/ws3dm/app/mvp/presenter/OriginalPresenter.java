package com.ws3dm.app.mvp.presenter;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.ws3dm.app.Constant;
import com.ws3dm.app.event.ErrorEvent;
import com.ws3dm.app.mvp.model.RespBean.GameCategoryRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDJhomeRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDJitemRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDetailRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameHotRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameListRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrigauthListRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrigauthorRespBean;
import com.ws3dm.app.mvp.model.RespBean.OriginalListRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrignewColRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrignewHomeRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrignewListRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrignewlevalRespBean;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.network.service.OriginalService;

/**
 * Describution : 原创Presentmenter
 * 
 * Author : DKjuan
 * 
 * Date : 2019/8/15 9:36
 **/
public class OriginalPresenter extends BasePresenter {

    private static final String TAG = "OriginalPresenter";

    private static OriginalPresenter mOriginalPresenter= null;

    private OriginalService mOriginalService;

    public static synchronized OriginalPresenter getInstance() {
        if (mOriginalPresenter == null) {
            mOriginalPresenter = new OriginalPresenter();
        }
        return mOriginalPresenter;
    }

    private OriginalPresenter() {
        mOriginalService = new OriginalService();
        registerEvent();
    }

    //获取原创首页页面数据
    public void getOrignewhome(String body) {
        mOriginalService.getOrignewhome(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_HOME_SUCCESS)})
    public void getORIGIN_HOMEResult(OrignewHomeRespBean bean) {
        notify(Constant.Notify.ORIGIN_HOME, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_HOME_FAILURE)})
    public void getORIGIN_HOMEFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //用于原创列表数据
    public void getOrignewList(String body) {
        mOriginalService.getOrignewList(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_LIST_SUCCESS)})
    public void getORIGIN_LISTResult(OrignewListRespBean bean) {
        notify(Constant.Notify.ORIGIN_LIST, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_LIST_FAILURE)})
    public void getORIGIN_LISTFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //用于获取原创节目标签配置数据
    public void getOrigLabel(String body) {
        mOriginalService.getOrignewList(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_LABEL_SUCCESS)})
    public void getOrigLabelResult(OrignewListRespBean bean) {
        notify(Constant.Notify.ORIGIN_LABEL, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_LABEL_FAILURE)})
    public void getOrigLabelFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //用于原创节目列表数据
    public void getOrigProgram(String body) {
        mOriginalService.getOrigProgram(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_PROGRAM_SUCCESS)})
    public void getOrigProgramResult(OrignewListRespBean bean) {
        notify(Constant.Notify.ORIGIN_PROGRAM, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_PROGRAM_FAILURE)})
    public void getOrigProgramFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //节目标签列表数据
    public void getOrigProgramLabel(String body) {
        mOriginalService.getOrigProgramLabel(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_PROGRAMLABEL_SUCCESS)})
    public void getOrigProgramLabelResult(OrignewListRespBean bean) {
        notify(Constant.Notify.ORIGIN_PROGRAM, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_PROGRAMLABEL_FAILURE)})
    public void getOrigProgramLabelFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //专栏列表数据
    public void getOrignewcol(String body) {
        mOriginalService.getOrignewcol(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_NEWCOL_SUCCESS)})
    public void getOrignewcolResult(OrignewColRespBean bean) {
        notify(Constant.Notify.ORIGIN_NEWCOL, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_NEWCOL_FAILURE)})
    public void getOrignewcolFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //专栏标签详情数据
    public void getOrigcollabel(String body) {
        mOriginalService.getOrigcollabel(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_COLLABEL_SUCCESS)})
    public void getOrigcollabelResult(OrignewListRespBean bean) {
        notify(Constant.Notify.ORIGIN_COLLABEL, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_COLLABEL_FAILURE)})
    public void getOrigcollabelFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //122我的团队作者
    public void getOrigauthor(String body) {
        mOriginalService.getOrigauthor(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_AUTHOR_SUCCESS)})
    public void getOrigauthorResult(OrigauthorRespBean bean) {
        notify(Constant.Notify.ORIGIN_AUTHOR, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_AUTHOR_FAILURE)})
    public void getOrigauthorFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //入住作者作品列表数据
    public void getOrigauthList(String body) {
        mOriginalService.getOrigauthList(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_AUTHORLIST_SUCCESS)})
    public void getOrigauthListResult(OrigauthListRespBean bean) {
        notify(Constant.Notify.ORIGIN_AUTHORLIST, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.ORIGIN_AUTHORLIST_FAILURE)})
    public void getOrigauthListFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }
}