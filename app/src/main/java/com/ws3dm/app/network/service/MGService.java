package com.ws3dm.app.network.service;


import com.ws3dm.app.Constant;
import com.ws3dm.app.mvp.model.RespBean.MGAroundRespBean;
import com.ws3dm.app.mvp.model.RespBean.MGChinaRespBean;
import com.ws3dm.app.mvp.model.RespBean.MGDetailRespBean;
import com.ws3dm.app.mvp.model.RespBean.MGGiftDetailRespBean;
import com.ws3dm.app.mvp.model.RespBean.MGGiftRespBean;
import com.ws3dm.app.mvp.model.RespBean.MGListRespBean;
import com.ws3dm.app.mvp.model.RespBean.MGTypeRespBean;
import com.ws3dm.app.network.Caller;
import com.ws3dm.app.network.RetrofitFactory;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Describution :手游service
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/16 14:45
 **/
public class MGService {

    private Api mApi;

    public MGService() {
        mApi = RetrofitFactory.getMGService();
    }

    //获取手游游戏首页页面数据
    public void getGameMG(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<MGListRespBean> c = new Caller<>(Constant.Event.MG_GAME_SUCCESS,Constant.Event.MG_GAME_FAILURE);
        c.execute(mApi.getGameMG(body));
    }

    //获取手游软件首页页面数据
    public void getSoftMG(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<MGListRespBean> c = new Caller<>(Constant.Event.MG_SOFT_SUCCESS,Constant.Event.MG_SOFT_FAILURE);
        c.execute(mApi.getGameMG(body));
    }

    //手游筛选列表
    public void getChoiceMG(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<MGChinaRespBean> c = new Caller<>(Constant.Event.MG_CHOICE_SUCCESS,Constant.Event.MG_CHOICE_FAILURE);
        c.execute(mApi.getChoiceMG(body));
    }

    public interface Api {
        @POST("sygamehome") //手游游戏首页页面数据
        Call<MGListRespBean> getGameMG(@Body RequestBody body);

        @POST("sysofthome") //手游软件首页页面数据
        Call<MGListRespBean> getSoftMG(@Body RequestBody body);

        @POST("sytypes") //手游分类
        Call<MGTypeRespBean> getTypeMG(@Body RequestBody body);

        @POST("sysdmhh") //手游汉化
        Call<MGChinaRespBean> getChina(@Body RequestBody body);

        @POST("sylibao") //手游礼包
        Call<MGGiftRespBean> getLibaoMG(@Body RequestBody body);

        @POST("sychice") //手游筛选列表
        Call<MGChinaRespBean> getChoiceMG(@Body RequestBody body);

        @POST("sydetail") //手游单个游戏详情
        Call<MGDetailRespBean> getDetailMG(@Body RequestBody body);

        @POST("syperiphery") //获取游戏周边
        Call<MGAroundRespBean> getAroundMG(@Body RequestBody body);

        @POST("sygetcore") //获取游戏评分
        Call<ResponseBody> getScore(@Body RequestBody body);

        @POST("syscore") //提交游戏评分
        Call<ResponseBody> setScore(@Body RequestBody body);

        @POST("shouyouso") //搜索手游列表页面数据
        Call<MGChinaRespBean> getSearchMG(@Body RequestBody body);

        @POST("sykaso") //搜索手游礼包列表页面数据
        Call<MGGiftRespBean> getSearchGift(@Body RequestBody body);
        
    }
}
