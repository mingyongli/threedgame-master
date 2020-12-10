package com.ws3dm.app.network.service;


import com.ws3dm.app.Constant;
import com.ws3dm.app.mvp.model.RespBean.HotSearchRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsAboutRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsCommentRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsDigitalRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsGLpageRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsGLspecialRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsGLzqlabelpaReslRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsHotCommentRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsListRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsSpeDetReslRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsGonglueReslRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsSpecilReslRespBean;
import com.ws3dm.app.mvp.model.RespBean.OriginalListRespBean;
import com.ws3dm.app.network.Caller;
import com.ws3dm.app.network.RetrofitFactory;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Describution :新闻service
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/16 14:45
 **/
public class NewsService {

    private Api mApi;

    public NewsService() {
        mApi = RetrofitFactory.getNewsService();
    }

    //获取评论列表
    public void getCommentList(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<NewsCommentRespBean> c = new Caller<>(Constant.Event.NEWS_COMMENT_LIST_SUCCESS,Constant.Event.NEWS_COMMENT_LISTR_FAILURE);
        c.execute(mApi.getCommentList(body));
    }

    //获取热门评论列表
    public void getHotComment(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<NewsHotCommentRespBean> c = new Caller<>(Constant.Event.NEWS_HOTCOMMENT_LIST_SUCCESS,Constant.Event.NEWS_HOTCOMMENT_LIST_FAILURE);
        c.execute(mApi.getHotComment(body));
    }

    //获取内容页相关内容数据
    public void getNewsAbout(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<NewsAboutRespBean> c = new Caller<>(Constant.Event.NEWS_ABOUT_SUCCESS,Constant.Event.NEWS_ABOUT_FAILURE);
        c.execute(mApi.getNewsAbout(body));
    }
    
    //用于获取攻略封面页数据
    public void getGLpage2(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<NewsGLpageRespBean> c = new Caller<>(Constant.Event.NEWS_GLPAGE2);
        c.execute(mApi.getGLpage2(body));
    }

    public interface Api {
//        新版分界线 -------------------------------
        @POST("hotnewspage") //新闻热点页面数据
        Call<NewsListRespBean> getHotNewsPage(@Body RequestBody body);
        
        @POST("videopage") //新闻--视频 页面数据
        Call<NewsListRespBean> getNewsVideo(@Body RequestBody body);
        
        @POST("newspage") //新闻--新闻 页面数据
        Call<NewsListRespBean> getNewsPage(@Body RequestBody body);
        
        @POST("amusepage2") //新闻--娱乐 页面数据
        Call<NewsDigitalRespBean> getNewsENT(@Body RequestBody body);
        
        @POST("digital") //新闻--数码 页面数据
        Call<NewsDigitalRespBean> getNewsDigital(@Body RequestBody body);
        
        @POST("glpage") //新闻--攻略 页面数据
        Call<NewsListRespBean> getGLpage(@Body RequestBody body);
        
        @POST("glpage2") //用于获取攻略封面页数据
        Call<NewsGLpageRespBean> getGLpage2(@Body RequestBody body);
        
        @POST("glspecial") //用于获取攻略专区全部字母数据
        Call<NewsGLspecialRespBean> getGLSpecial(@Body RequestBody body);
        
        @POST("glspecialsel") //用于获取攻略专区全部字母数据
        Call<NewsSpecilReslRespBean> getGLSpecialSEL(@Body RequestBody body);
        
        @POST("glspecialdetail") //用于获取攻略专区检索页数据
        Call<NewsSpeDetReslRespBean> getGLSpecialDel(@Body RequestBody body);
        
        @POST("specialgllist") //用于获取专区全部攻略详情数据
        Call<NewsSpecilReslRespBean> getGLSpecialList(@Body RequestBody body);
        
        @POST("specialnewslist") //用于获取攻略专区新闻数据
        Call<NewsSpecilReslRespBean> getGLSpecialNewList(@Body RequestBody body);
        
        @POST("glspecialpic") //用于获取攻略专区图鉴详情数据
        Call<NewsGonglueReslRespBean> getGLSpecialPic(@Body RequestBody body);
        
        @POST("glzqpicpage") //用于获取攻略专区图鉴详情数据
        Call<NewsGLzqlabelpaReslRespBean> getGLzqPicPage(@Body RequestBody body);
        
        @POST("glspeciallabel") //获取攻略专区攻略标签详情数据
        Call<NewsGonglueReslRespBean> getSpecialLabel(@Body RequestBody body);
        
        @POST("specialglso") //获取攻略专区攻略标签详情数据
        Call<NewsSpecilReslRespBean> getSpecialGLso(@Body RequestBody body);
        
        @POST("glzqlabelpage") //获取攻略专区攻略标签详情数据
        Call<NewsGLzqlabelpaReslRespBean> getSpecLabPage(@Body RequestBody body);

        @POST("getnewcomment")//获取评论列表
        Call<NewsCommentRespBean> getCommentList(@Body RequestBody body);

        @POST("getnewhotcomment")//获取文章热门评论接口
        Call<NewsHotCommentRespBean> getHotComment(@Body RequestBody body);

        @POST("newsabout")//获取内容页相关内容数据
        Call<NewsAboutRespBean> getNewsAbout(@Body RequestBody body);

        @POST("originalcolpage")//获取专栏文章列表页面数据
        Call<OriginalListRespBean> getOriginalColum(@Body RequestBody body);

        @POST("addcomment")//添加评论
        Call<ResponseBody> addComment(@Body RequestBody body);

        @POST("replycomment")//回复评论
        Call<ResponseBody> replyComment(@Body RequestBody body);

        @POST("gamegl") //单机游戏攻略详情数据
        Call<NewsListRespBean> getGameGl(@Body RequestBody body);

        @POST("gamevideo") //单机游戏详情新闻数据
        Call<NewsListRespBean> getGameVideo(@Body RequestBody body);

        @POST("hotso") //获取热门搜索列表
        Call<HotSearchRespBean> getHotSearch(@Body RequestBody body);

        @POST("newhotso") //新版获取热门搜索列表
        Call<HotSearchRespBean> getNewhotso(@Body RequestBody body);

        @POST("pushnews") //获取推送的新闻列表
        Call<NewsListRespBean> getPushNews(@Body RequestBody body);

        @POST("pagereport") //文章的举报接口
        Call<ResponseBody> pagereport(@Body RequestBody body);

        @POST("report") //评论的举报接口
        Call<ResponseBody> report(@Body RequestBody body);

    }
}
