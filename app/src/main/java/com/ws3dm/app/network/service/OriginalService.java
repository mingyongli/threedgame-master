package com.ws3dm.app.network.service;


import com.ws3dm.app.Constant;
import com.ws3dm.app.mvp.model.RespBean.GameCategoryRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameConfigRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDJLabelRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDJRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDJhomeRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDJitemRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameDetailRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameHotRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameListRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrigLabelRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrigauthListRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrigauthorRespBean;
import com.ws3dm.app.mvp.model.RespBean.OriginalListRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrignewColRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrignewHomeRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrignewListRespBean;
import com.ws3dm.app.mvp.model.RespBean.OrignewlevalRespBean;
import com.ws3dm.app.network.Caller;
import com.ws3dm.app.network.RetrofitFactory;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Describution :游戏库service
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/16 14:45
 **/
public class OriginalService {

    private Api mApi;

    public OriginalService() {
        mApi = RetrofitFactory.getOriginalService();
    }

    //用于原创首页数据
    public void getOrignewhome(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<OrignewHomeRespBean> c = new Caller<>(Constant.Event.ORIGIN_HOME_SUCCESS,Constant.Event.ORIGIN_HOME_FAILURE);
        c.execute(mApi.getOrignewhome(body));
    }

    //用于原创列表数据
    public void getOrignewList(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<OrignewListRespBean> c = new Caller<>(Constant.Event.ORIGIN_LIST_SUCCESS,Constant.Event.ORIGIN_LIST_FAILURE);
        c.execute(mApi.getOrignewList(body));
    }

    //用于获取原创节目标签配置数据
    public void getOrigLabel(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<OrignewListRespBean> c = new Caller<>(Constant.Event.ORIGIN_LABEL_SUCCESS,Constant.Event.ORIGIN_LABEL_FAILURE);
        c.execute(mApi.getOrignewList(body));
    }

    //用于原创节目列表数据
    public void getOrigProgram(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<OrignewListRespBean> c = new Caller<>(Constant.Event.ORIGIN_PROGRAM_SUCCESS,Constant.Event.ORIGIN_PROGRAM_FAILURE);
        c.execute(mApi.getOrigProgram(body));
    }

    //原创节目标签列表数据
    public void getOrigProgramLabel(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<OrignewListRespBean> c = new Caller<>(Constant.Event.ORIGIN_PROGRAMLABEL_SUCCESS,Constant.Event.ORIGIN_PROGRAMLABEL_FAILURE);
        c.execute(mApi.getOrigProgramLabel(body));
    }

    //原创专栏列表数据
    public void getOrignewcol(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<OrignewColRespBean> c = new Caller<>(Constant.Event.ORIGIN_NEWCOL_SUCCESS,Constant.Event.ORIGIN_NEWCOL_FAILURE);
        c.execute(mApi.getOrignewcol(body));
    }

    //专栏标签详情数据
    public void getOrigcollabel(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<OrignewListRespBean> c = new Caller<>(Constant.Event.ORIGIN_COLLABEL_SUCCESS,Constant.Event.ORIGIN_COLLABEL_FAILURE);
        c.execute(mApi.getOrigcollabel(body));
    }

    //122我的团队
    public void getOrigauthor(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<OrigauthorRespBean> c = new Caller<>(Constant.Event.ORIGIN_AUTHOR_SUCCESS,Constant.Event.ORIGIN_AUTHOR_FAILURE);
        c.execute(mApi.getOrigauthor(body));
    }

    //入住作者作品列表数据
    public void getOrigauthList(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<OrigauthListRespBean> c = new Caller<>(Constant.Event.ORIGIN_AUTHORLIST_SUCCESS,Constant.Event.ORIGIN_AUTHORLIST_FAILURE);
        c.execute(mApi.getOrigauthList(body));
    }
    
    public interface Api {
        @POST("orignewhome")//原创首页
        Call<OrignewHomeRespBean> getOrignewhome(@Body RequestBody body);

        @POST("orignewlist")//原创列表数据
        Call<OrignewListRespBean> getOrignewList(@Body RequestBody body);

        @POST("origlabel")//原创节目标签配置数据
        Call<OrigLabelRespBean> getOrigLabel(@Body RequestBody body);

        @POST("origprogram")//原创专栏列表数据
        Call<OrignewListRespBean> getOrigProgram(@Body RequestBody body);

        @POST("origproglabel")//原创节目标签列表数据
        Call<OrignewListRespBean> getOrigProgramLabel(@Body RequestBody body);

        @POST("orignewleval")//原创评测列表页面数据
        Call<OrignewlevalRespBean> getOrignewleval(@Body RequestBody body);

        @POST("orignewcol")//原创专栏列表数据
        Call<OrignewColRespBean> getOrignewcol(@Body RequestBody body);

        @POST("origcollabel")//专栏标签详情数据
        Call<OrignewListRespBean> getOrigcollabel(@Body RequestBody body);

        @POST("origauthor")//122我的团队
        Call<OrigauthorRespBean> getOrigauthor(@Body RequestBody body);

        @POST("origauthlist")//入住作者作品列表数据
        Call<OrigauthListRespBean> getOrigauthList(@Body RequestBody body);
    }
}
