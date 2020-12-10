package com.ws3dm.app.mvp.presenter;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.ws3dm.app.Constant;
import com.ws3dm.app.event.ErrorEvent;
import com.ws3dm.app.mvp.model.RespBean.NewsAboutRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsCommentRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsGLpageRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsHotCommentRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsListRespBean;
import com.ws3dm.app.mvp.model.RespBean.VideoDetailRespBean;
import com.ws3dm.app.network.service.NewsService;

/**
 * Describution :新闻 Presentmenter
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/16 15:47
 **/
public class NewsPresenter extends BasePresenter {

    private static final String TAG = "NewsPresenter";

    private static NewsPresenter mNewsPresenter = null;

    private NewsService mNewsService;

    public static synchronized NewsPresenter getInstance() {
        if (mNewsPresenter == null) {
            mNewsPresenter = new NewsPresenter();
        }
        return mNewsPresenter;
    }

    private NewsPresenter() {
        mNewsService = new NewsService();
        registerEvent();
    }

    //获取热门评论列表
    public void getHotComment(String body) {
        mNewsService.getHotComment(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.NEWS_HOTCOMMENT_LIST_SUCCESS)})
    public void getHotCommentResult(NewsHotCommentRespBean bean) {
        notify(Constant.Notify.RESULT_NEWS_HOTCOMMENT_LIST, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.NEWS_HOTCOMMENT_LIST_FAILURE)})
    public void getHotCommentFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //用于获取攻略封面页数据
    public void getGLpage2(String body) {
        mNewsService.getGLpage2(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.NEWS_GLPAGE2)})
    public void getGLpage2Result(NewsGLpageRespBean bean) {
        notify(Constant.Notify.RESULT_NEWS_NEWS_GLPAGE2, bean);
    }
}
