package com.ws3dm.app.mvp.presenter;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.ws3dm.app.Constant;
import com.ws3dm.app.event.ErrorEvent;
import com.ws3dm.app.mvp.model.RespBean.ForumBannerRespBean;
import com.ws3dm.app.mvp.model.RespBean.ForumGidRespBean;
import com.ws3dm.app.mvp.model.RespBean.ForumRankRespBean;
import com.ws3dm.app.mvp.model.RespBean.ForumTidPostRespBean;
import com.ws3dm.app.mvp.model.RespBean.ForumTidTypeRespBean;
import com.ws3dm.app.mvp.model.RespBean.ForumTopRespBean;
import com.ws3dm.app.network.service.ForumService;

/**
 * Describution :论坛Presentmenter
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/16 15:47
 **/
public class ForumPresenter extends BasePresenter {

    private static final String TAG = "ForumPresenter";

    private static ForumPresenter mForumPresenter = null;

    private ForumService mForumService;

    public static synchronized ForumPresenter getInstance() {
        if (mForumPresenter == null) {
            mForumPresenter = new ForumPresenter();
        }
        return mForumPresenter;
    }

    private ForumPresenter() {
        mForumService = new ForumService();
        registerEvent();
    }

    //加载频道标签列表
    public void getForumBanner(String body) {
        mForumService.loadBanner(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_BANNER_SUCCESS)})
    public void getForumBannerResult(ForumBannerRespBean bean) {
        notify(Constant.Notify.RESULT_FORUM_BANNER, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_BANNER_FAILURE)})
    public void getBannerFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    
    /**
     * Describution :分隔符
     * 
     * Author : DKjuan
     * 
     * Date : 2018/3/28 18:02
     **/
    //论坛排行榜
    public void getForumRank(String body) {
        mForumService.getForumRank(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_RANK_SUCCESS)})
    public void getForumRankSuccess(ForumRankRespBean bean) {
        notify(Constant.Notify.RESULT_FORUM_RANK, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_RANK_FAILURE)})
    public void getForumRankFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }
    
    
    public void getForumGid(String body) {//论坛板块分区列表
        mForumService.getForumGid(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_GID_SUCCESS)})
    public void getForumGidSuccess(ForumGidRespBean bean) {
        notify(Constant.Notify.RESULT_FORUM_GID, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_GID_FAILURE)})
    public void getForumGidFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }
    
    public void getForumList(String body) {//论坛 子板块分区列表
        mForumService.getForumList(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_LIST_SUCCESS)})
    public void getForumListSuccess(ForumRankRespBean bean) {
        notify(Constant.Notify.RESULT_FORUM_LIST, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_LIST_FAILURE)})
    public void getForumListFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }
    
    public void getForumThreadList(String body) {//论坛 子板块分区 主题列表
        mForumService.getForumThreadList(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_LISTHREAD_SUCCESS)})
    public void getForumThreadListSuccess(ForumTopRespBean bean) {
        notify(Constant.Notify.RESULT_FORUMTHREAD_LIST, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_LISTHREAD_FAILURE)})
    public void getForumThreadListFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }
    
    public void getForumFirstPost(String body) {//当前作者修改自己的主题帖子
        mForumService.getForumFirstPost(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_GETFIRSTPOST_SUCCESS)})
    public void getForumFirstPostSuccess(ForumTidTypeRespBean bean) {
        notify(Constant.Notify.RESULT_GETFIRSTPOST, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_GETFIRSTPOST_FAILURE)})
    public void getForumFirstPostFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE,result);
    }
    
    public void getForumTidType(String body) {//获取板块类型标签列表
        mForumService.getForumTidType(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_TIDTYPE_SUCCESS)})
    public void getForumTidTypeSuccess(ForumTidTypeRespBean bean) {
        notify(Constant.Notify.RESULT_FORUMTIDTYPE_LIST, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_TIDTYPE_FAILURE)})
    public void getForumTidTypeFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE,result);
    }
    
    public void getForumTidPost(String body) {//获取回帖列表
        mForumService.getForumTidPost(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_TIDPOST_SUCCESS)})
    public void getForumTidPostSuccess(ForumTidPostRespBean bean) {
        notify(Constant.Notify.RESULT_FORUMTIDPOST_LIST, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.FORUM_TIDPOST_FAILURE)})
    public void getForumTidPostFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }
   
    
}
