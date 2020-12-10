package com.ws3dm.app.network.service;


import com.ws3dm.app.Constant;
import com.ws3dm.app.mvp.model.RespBean.ForumBannerRespBean;
import com.ws3dm.app.mvp.model.RespBean.ForumGidRespBean;
import com.ws3dm.app.mvp.model.RespBean.ForumRankRespBean;
import com.ws3dm.app.mvp.model.RespBean.ForumTidPostRespBean;
import com.ws3dm.app.mvp.model.RespBean.ForumTidTypeRespBean;
import com.ws3dm.app.mvp.model.RespBean.ForumTopRespBean;
import com.ws3dm.app.network.Caller;
import com.ws3dm.app.network.RetrofitFactory;

import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Describution :论坛service
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/16 14:45
 **/
public class ForumService {

    private Api mApi;

    public ForumService() {
        mApi = RetrofitFactory.getForumService();
    }

    //论坛分类标签
    public void loadBanner(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<ForumBannerRespBean> c = new Caller<>(Constant.Event.FORUM_BANNER_SUCCESS,Constant.Event.FORUM_BANNER_FAILURE);
        c.execute(mApi.getBanner(body));
    }
    
    //论坛排行
    public void getForumRank(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<ForumRankRespBean> c = new Caller<>(Constant.Event.FORUM_RANK_SUCCESS,Constant.Event.FORUM_RANK_FAILURE);
        c.execute(mApi.getForumRank(body));
    }
    
    //论坛板块分区列表
    public void getForumGid(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<ForumGidRespBean> c = new Caller<>(Constant.Event.FORUM_GID_SUCCESS,Constant.Event.FORUM_GID_FAILURE);
        c.execute(mApi.getForumGid(body));
    }
    
    //论坛 子板块分区列表
    public void getForumList(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<ForumRankRespBean> c = new Caller<>(Constant.Event.FORUM_LIST_SUCCESS,Constant.Event.FORUM_LIST_FAILURE);
        c.execute(mApi.getForumSonList(body));
    }
    
    //论坛 主题列表
    public void getForumThreadList(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<ForumTopRespBean> c = new Caller<>(Constant.Event.FORUM_LISTHREAD_SUCCESS,Constant.Event.FORUM_LISTHREAD_FAILURE);
        c.execute(mApi.getForumThreadList(body));
    }
    
    //获取板块类型标签列表
    public void getForumTidType(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<ForumTidTypeRespBean> c = new Caller<>(Constant.Event.FORUM_TIDTYPE_SUCCESS,Constant.Event.FORUM_TIDTYPE_FAILURE);
        c.execute(mApi.getForumTidType(body));
    }
    
    //当前作者修改自己的主题帖子
    public void getForumFirstPost(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<ForumTidTypeRespBean> c = new Caller<>(Constant.Event.FORUM_GETFIRSTPOST_SUCCESS,Constant.Event.FORUM_GETFIRSTPOST_FAILURE);
        c.execute(mApi.getForumTidType(body));
    }
    
    //获取回帖列表
    public void getForumTidPost(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<ForumTidPostRespBean> c = new Caller<>(Constant.Event.FORUM_TIDPOST_SUCCESS,Constant.Event.FORUM_TIDPOST_FAILURE);
        c.execute(mApi.getTidPost(body));
    }
    
    
    public interface Api {

        @POST("index.php")
        Call<ForumBannerRespBean> getBanner(@Body RequestBody body);

        @POST("forumrank") //论坛排行
        Call<ForumRankRespBean> getForumRank(@Body RequestBody body);

        @POST("forumgid") //论坛板块分区列表
        Call<ForumGidRespBean> getForumGid(@Body RequestBody body);

        @POST("forumlist") //论坛 子版块分区 列表
        Call<ForumRankRespBean> getForumSonList(@Body RequestBody body);

        @POST("setfidfavorite")//添加删除板块收藏
        Call<ResponseBody> setFidFavorite(@Body RequestBody body);
        
        @POST("forumthread") //论坛 子版块主题 列表
        Call<ForumTopRespBean> getForumThreadList(@Body RequestBody body);

        @POST("getforumtidtype") //获取板块类型标签列表
        Call<ForumTidTypeRespBean> getForumTidType(@Body RequestBody body);

        @POST("getfirstpost") //当前作者修改自己的主题帖子
        Call<ForumTidTypeRespBean> getForumFirstPost(@Body RequestBody body);

        @POST("gettidpost") //获取 回帖列表
        Call<ForumTidPostRespBean> getTidPost(@Body RequestBody body);

        @POST("bbsnewpost") //添加回帖 发布回帖
        Call<ResponseBody> addTidPost(@Body RequestBody body);

        @POST("gettidfavorite") //获取当前主题收藏状态
        Call<JSONObject> getTidFavorite(@Body RequestBody body);

        @POST("settidfavorite") //设置当前主题收藏--添加删除收藏
        Call<JSONObject> setTidFavorite(@Body RequestBody body);

        @POST("topthread")  //论坛全局置顶主题列表
        Call<ForumTopRespBean> getForumTopthread(@Body RequestBody body);

        @POST("topnoticebbs")  //论坛 站内公告列表
        Call<ForumTopRespBean> getForumTopnotece(@Body RequestBody body);

        @POST("topdigestbbs")  //论坛 精华列表
        Call<ForumTopRespBean> getForumTopdigest(@Body RequestBody body);

        @POST("newthread")//提交新主题
        Call<ResponseBody> submitNewThread(@Body RequestBody body);

        @POST("uploadbbs")//bbs提交图片
        Call<JSONObject> upLoadBBS(@Body RequestBody body);

        @POST("threadfavorite")  //收藏的帖子列表
        Call<ForumTopRespBean> getThreadFavorite(@Body RequestBody body);

        @POST("settidfavorite")//添加删除收藏
        Call<ResponseBody> setThreadFavorite(@Body RequestBody body);
    }
}