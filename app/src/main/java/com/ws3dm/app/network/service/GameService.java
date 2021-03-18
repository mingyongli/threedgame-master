package com.ws3dm.app.network.service;


import com.ws3dm.app.Constant;
import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.mvp.model.RespBean.GameCategoryRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameConfigRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDJLabelRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDJRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDJhomeRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDJitemRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDetailRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameGiftRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameHotRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameListRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameMGhomeRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameOLHotRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameOLhomeRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameOLitemRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameOLtestRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameSYhotRespBean;
import com.ws3dm.app.mvp.model.RespBean.MGGiftDetailRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsDigitalRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrignewListRespBean;
import com.ws3dm.app.network.Caller;
import com.ws3dm.app.network.RetrofitFactory;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Describution :游戏库service
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/16 14:45
 **/
public class GameService {

    private Api mApi;

    public GameService() {
        mApi = RetrofitFactory.getGameService();
    }

    //获取单机首页页面数据(热门界面)
    public void getHotGame(String str_body) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), str_body);
        Caller<GameHotRespBean> c = new Caller<>(Constant.Event.GAME_HOT_SUCCESS, Constant.Event.GAME_HOT_SUCCESS);
        c.execute(mApi.getHotGame(body));
    }

    //游戏详情信息
    public void loadGameDetail(String str_body) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), str_body);
        Caller<GameListRespBean> c = new Caller<>(Constant.Event.GAME_DETAIL);
        c.execute(mApi.getGameChannelDetail(body));
    }

    //游戏分类
    public void getGameCategory(String str_body) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), str_body);
        Caller<GameCategoryRespBean> c = new Caller<>(Constant.Event.GAME_CATEGORY_SUCCESS, Constant.Event.GAME_CATEGORY_FAILURE);
        c.execute(mApi.getGameCategory(body));
    }

    //游戏分类详情列表
    public void getGameChice(String str_body) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), str_body);
        Caller<GameListRespBean> c = new Caller<>(Constant.Event.GAME_CHOICE_SUCCESS, Constant.Event.GAME_CHOICE_FAILURE);
        c.execute(mApi.getGameChice(body));
    }

    //游戏详情
    public void getGameDetail(String str_body) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), str_body);
        Caller<GameDetailRespBean> c = new Caller<>(Constant.Event.GAME_DETAIL_SUCCESS, Constant.Event.GAME_DETAIL_FAILURE);
        c.execute(mApi.getGameDetail(body));
    }

    //新版单机首页
    public void getDJhome(String str_body) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), str_body);
        Caller<GameDJhomeRespBean> c = new Caller<>(Constant.Event.GAME_DJHOME_SUCCESS, Constant.Event.GAME_DJHOME_FAILURE);
        c.execute(mApi.djHome(body));
    }

    //用于获取单机分类、语言、类型筛选项
    public void getDJitem(String str_body) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), str_body);
        Caller<GameDJitemRespBean> c = new Caller<>(Constant.Event.GAME_DJITEM_SUCCESS, Constant.Event.GAME_DJITEM_FAILURE);
        c.execute(mApi.djitem(body));
    }

    //获取网游类型、标签、运营商筛选项
    public void getOlitem(String str_body) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), str_body);
        Caller<GameOLitemRespBean> c = new Caller<>(Constant.Event.GAME_OLITEM_SUCCESS, Constant.Event.GAME_OLITEM_FAILURE);
        c.execute(mApi.olitem(body));
    }

    //获取手游类型、标签、运营商筛选项
    public void getSYitem(String str_body) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), str_body);
        Caller<GameOLitemRespBean> c = new Caller<>(Constant.Event.GAME_SYITEM_SUCCESS, Constant.Event.GAME_SYITEM_FAILURE);
        c.execute(mApi.syitem(body));
    }

    public interface Api {

        @POST("appgamechannel.php")
        Call<GameListRespBean> getGameChannelDetail(@Body RequestBody body);

        @POST("hotgame")
        Call<GameHotRespBean> getHotGame(@Body RequestBody body);

        @POST("gametypes")
//游戏分类
        Call<GameCategoryRespBean> getGameCategory(@Body RequestBody body);

        @POST("gamesale")
//游戏发售列表
        Call<GameListRespBean> getGameSale(@Body RequestBody body);

        @POST("gameunsale")
//游戏未发售列表
        Call<GameListRespBean> getGameUnSale(@Body RequestBody body);

        @POST("gamechine")
//游戏汉化
        Call<GameListRespBean> getGameChinese(@Body RequestBody body);

        @POST("gamerank")
//游戏汉化
        Call<GameListRespBean> getGameRank(@Body RequestBody body);

        @POST("gameconfig")
//游戏配置
        Call<GameConfigRespBean> getGameConfig(@Body RequestBody body);

        @POST("gameach")
//申请汉化
        Call<ResponseBody> applyChina(@Body RequestBody body);

        @POST("newdjhome")
//新版单机首页
        Call<GameDJhomeRespBean> djHome(@Body RequestBody body);

        @POST("newolhome")
//新版网游首页
        Call<GameOLhomeRespBean> olHome(@Body RequestBody body);

        @POST("olhot")
//热门网游列表数据
        Call<GameOLHotRespBean> olHot(@Body RequestBody body);

        @POST("syscreen")
//手游赛选列表数据
        Call<GameOLHotRespBean> syscreen(@Body RequestBody body);

        @POST("olscreen")
//热门网游列表数据
        Call<GameOLHotRespBean> olscreen(@Body RequestBody body);

        @POST("syeval")
//手游评测列表页面数据
        Call<OrignewListRespBean> syeval(@Body RequestBody body);

        @POST("syanli")
//手游日常安利列表
        Call<NewsDigitalRespBean> syanli(@Body RequestBody body);

        @POST("newsyhome")
//新版手游首页
        Call<GameMGhomeRespBean> syHome(@Body RequestBody body);

        @POST("ollibaodetail")
            //获取网游礼包详情
        Call<MGGiftDetailRespBean> ollibaodetail(@Body RequestBody body);

        @POST("giftollibao")
            //获取网游礼包code
        Call<ResponseBody> giftollibao(@Body RequestBody body);

        @POST("sylibaodetail3")
            //获取手游礼包详情
        Call<MGGiftDetailRespBean> sylibaodetail3(@Body RequestBody body);

        @POST("giftsylibao")
            //获取手游礼包code
        Call<ResponseBody> giftsylibao(@Body RequestBody body);

        @POST("djitem")
//用于获取单机分类、语言、类型筛选项
        Call<GameDJitemRespBean> djitem(@Body RequestBody body);

        @POST("olitem")
//获取网游类型、标签、运营商筛选项
        Call<GameOLitemRespBean> olitem(@Body RequestBody body);

        @POST("syitem")
//获取手游类型筛选项
        Call<GameOLitemRespBean> syitem(@Body RequestBody body);

        @POST("djscreen")
//用于获取单机游戏库页面筛选数据
        Call<GameDJRespBean> djscreen(@Body RequestBody body);

        @POST("oltest")
//用于获取网游开测列表数据
        Call<GameOLtestRespBean> oltest(@Body RequestBody body);

        @POST("olesportzt")
//获取网游电竞专题列表数据
        Call<GameOLtestRespBean> olesportzt(@Body RequestBody body);

        @POST("syhot")
//热门手游列表数据
        Call<GameSYhotRespBean> syhot(@Body RequestBody body);

        @POST("olztlibao")
//网游专题礼包列表数据
        Call<GameGiftRespBean> olztlibao(@Body RequestBody body);

        @POST("syztlibao")
//手游专题礼包列表数据
        Call<GameGiftRespBean> syztlibao(@Body RequestBody body);

        @POST("djhh")
//获取单机汉化页面筛选数据
        Call<GameDJRespBean> djhh(@Body RequestBody body);

        @POST("djsale")
//获取单机发售页面筛选数据
        Call<GameDJRespBean> djsale(@Body RequestBody body);

        @POST("djlabel")
//获取单机标签配置数据
        Call<GameDJLabelRespBean> djlabel(@Body RequestBody body);

        @POST("djrank")
//用于获取单机排行
        Call<GameDJRespBean> djrank(@Body RequestBody body);

        @POST("olrank")
//获取网游排行
        Call<GameOLHotRespBean> olrank(@Body RequestBody body);

        @POST("ollibao")
//获取网游礼包
        Call<GameGiftRespBean> ollibao(@Body RequestBody body);

        @POST("sylibao3")
//获取手游礼包
        Call<GameGiftRespBean> sylibao3(@Body RequestBody body);

        @POST("djlabelso")
//用于获取单机标签页面筛选数据
        Call<GameListRespBean> getGameChice(@Body RequestBody body);

        @POST("djdetail")
//游戏详情
        Call<GameDetailRespBean> getGameDetail(@Body RequestBody body);

        @POST("syzqdetail")
//手游详情
        Call<GameDetailRespBean> getSYGameDetail(@Body RequestBody body);

        @POST("oldetail")
//网游详情
        Call<GameDetailRespBean> getOLGameDetail(@Body RequestBody body);
    }
}
