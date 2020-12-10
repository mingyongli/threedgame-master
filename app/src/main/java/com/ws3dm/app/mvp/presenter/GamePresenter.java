package com.ws3dm.app.mvp.presenter;

import com.ws3dm.app.event.ErrorEvent;
import com.ws3dm.app.mvp.model.RespBean.GameCategoryRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDJhomeRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDJitemRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDetailRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameHotRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameListRespBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.mvp.model.RespBean.GameOLitemRespBean;
import com.ws3dm.app.network.service.GameService;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

/**
 * Describution :游戏库Presentmenter
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/16 15:47
 **/
public class GamePresenter extends BasePresenter {

    private static final String TAG = "GamePresenter";

    private static GamePresenter mGamePresenter= null;

    private GameService mGameService;

    public static synchronized GamePresenter getInstance() {
        if (mGamePresenter == null) {
            mGamePresenter = new GamePresenter();
        }
        return mGamePresenter;
    }

    private GamePresenter() {
        mGameService = new GameService();
        registerEvent();
    }

    //获取单机首页页面数据
    public void getHotGame(String body) {
        mGameService.getHotGame(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_HOT_SUCCESS)})
    public void getHotGameResult(GameHotRespBean bean) {
        notify(Constant.Notify.RESULT_GAME_HOT, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_HOT_FAILURE)})
    public void getHotGameFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //游戏分类
    public void getGameCategory(String body) {
        mGameService.getGameCategory(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_CATEGORY_SUCCESS)})
    public void getGameCategoryResult(GameCategoryRespBean bean) {
        notify(Constant.Notify.RESULT_GAME_CATEGORY, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_CATEGORY_FAILURE)})
    public void getGameCategoryFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //游戏分类详情列表
    public void getGameChice(String body) {
        mGameService.getGameChice(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_CHOICE_SUCCESS)})
    public void getGameChiceResult(GameListRespBean bean) {
        notify(Constant.Notify.RESULT_GAME_CHOICE, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_CHOICE_FAILURE)})
    public void getGameChiceFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //获取单机详页游戏数据
    public void getGameDetail(String body) {
        mGameService.getGameDetail(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_DETAIL_SUCCESS)})
    public void getGameDetailResult(GameDetailRespBean bean) {
        notify(Constant.Notify.RESULT_GAME_DETAIL, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_DETAIL_FAILURE)})
    public void getGameDetailFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //获取新版单机首页面
    public void getDJhome(String body) {
        mGameService.getDJhome(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_DJHOME_SUCCESS)})
    public void getDJhomeResult(GameDJhomeRespBean bean) {
        notify(Constant.Notify.RESULT_GAME_DJHOME, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_DJHOME_FAILURE)})
    public void getDJhomeFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //用于获取单机分类、语言、类型筛选项
    public void getDJitem(String body) {
        mGameService.getDJitem(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_DJITEM_SUCCESS)})
    public void getDJitemResult(GameDJitemRespBean bean) {
        notify(Constant.Notify.RESULT_GAME_DJITEM, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_DJITEM_FAILURE)})
    public void getDJitemFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //用于获取网游筛选项
    public void getOLitem(String body) {
        mGameService.getOlitem(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_OLITEM_SUCCESS)})
    public void getOLitemResult(GameOLitemRespBean bean) {
        notify(Constant.Notify.RESULT_GAME_OLITEM, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_OLITEM_FAILURE)})
    public void getOLitemFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

    //用于获取网游筛选项
    public void getSYitem(String body) {
        mGameService.getSYitem(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_SYITEM_SUCCESS)})
    public void getSYitemResult(GameOLitemRespBean bean) {
        notify(Constant.Notify.RESULT_GAME_SYITEM, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GAME_SYITEM_FAILURE)})
    public void getSYitemFailure(ErrorEvent result) {
        notify(Constant.Notify.LOAD_FAILURE, result);
    }

}