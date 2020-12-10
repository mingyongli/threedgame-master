package com.ws3dm.app.network.service;


import com.ws3dm.app.mvp.model.RespBean.AddressRespBean;
import com.ws3dm.app.mvp.model.RespBean.CollectGameRespBean;
import com.ws3dm.app.mvp.model.RespBean.CollectNewsRespBean;
import com.ws3dm.app.mvp.model.RespBean.GetSettingRespBean;
import com.ws3dm.app.mvp.model.RespBean.MGChinaRespBean;
import com.ws3dm.app.mvp.model.RespBean.MGGiftRespBean;
import com.ws3dm.app.mvp.model.RespBean.MyCommentRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewSearchRespBean;
import com.ws3dm.app.Constant;
import com.ws3dm.app.mvp.model.RespBean.SearchGameRespBean;
import com.ws3dm.app.mvp.model.RespBean.UserRespBean;
import com.ws3dm.app.mvp.model.RespBean.VersionRespBean;
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
public class UserService {

    private Api mApi;

    public UserService() {
        mApi = RetrofitFactory.getUserService();
    }

    //用户登陆
    public void login(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<UserRespBean> c = new Caller<>(Constant.Event.USER_LOGIN_SUCCESS,Constant.Event.USER_LOGIN_FAILURE);
        c.execute(mApi.getUserInfo(body));
    }
    //用户注册
    public void regist(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<UserRespBean> c = new Caller<>(Constant.Event.USER_REGIST_SUCCESS,Constant.Event.USER_REGIST_FAILURE);
        c.execute(mApi.doRegist(body));
    }
    
    //修改密码
    public void modifyPass(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<UserRespBean> c = new Caller<>(Constant.Event.USER_MODIFYPASS_SUCCESS,Constant.Event.USER_MODIFYPASS_FAILURE);
        c.execute(mApi.doModify(body));
    }

    //老用户绑定手机号
    public void bindPhoneByUid(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<UserRespBean> c = new Caller<>(Constant.Event.USER_BINDBYID_SUCCESS,Constant.Event.USER_BINDBYID_FAILURE);
        c.execute(mApi.bindPhoneByUid(body));
    }

    //第三方登陆绑定，非老用户
    public void bindPhone(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<UserRespBean> c = new Caller<>(Constant.Event.USER_BINDPHONE_SUCCESS,Constant.Event.USER_BINDPHONE_FAILURE);
        c.execute(mApi.bindPhone(body));
    }
    
    //重置密码
    public void resetPass(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<UserRespBean> c = new Caller<>(Constant.Event.USER_RESET_SUCCESS,Constant.Event.USER_RESET_FAILURE);
        c.execute(mApi.doReset(body));
    }
    
    //获取设置页信息
    public void getSetting(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<GetSettingRespBean> c = new Caller<>(Constant.Event.USER_GETSETTING_SUCCESS,Constant.Event.USER_GETSETTING_FAILURE);
        c.execute(mApi.getSettingInfo(body));
    }
    
    //获取我的评论
    public void getMyComment(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<MyCommentRespBean> c = new Caller<>(Constant.Event.USER_GETMYCOMMENT_SUCCESS,Constant.Event.USER_GETMYCOMMENT_FAILURE);
        c.execute(mApi.getMyComment(body));
    }
    
    //获取我的收藏新闻
    public void getCollectNews(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<CollectNewsRespBean> c = new Caller<>(Constant.Event.MY_NEWS_COLLECT_SUCCESS,Constant.Event.MY_NEWS_COLLECT_FAILURE);
        c.execute(mApi.getCollectNews(body));
    }
    
    //获取我的收藏礼包
    public void getCollectGift(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<CollectNewsRespBean> c = new Caller<>(Constant.Event.MY_GIFT_COLLECT_SUCCESS,Constant.Event.MY_GIFT_COLLECT_FAILURE);
        c.execute(mApi.getCollectNews(body));
    }

    //获取版本号
    public void getVersion(String str_body) {
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
        Caller<VersionRespBean> c = new Caller<>(Constant.Event.GET_VERSION_SUCCESS,Constant.Event.GET_VERSION_FAILURE);
        c.execute(mApi.getVersion(body));
    }
    
//    //修改设置页信息
//    public void modifySetting(String str_body) {
//        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
//        Caller<GetSettingRespBean> c = new Caller<>(Constant.Event.USER_MODIFYSETTING_SUCCESS,Constant.Event.USER_MODIFYSETTING_FAILURE);
//        c.execute(mApi.modifySettingInfo(body));
//    }
//    
//    //添加意见反馈
//    public void addSuggest(String str_body) {
//        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),str_body);
//        Caller<GetSettingRespBean> c = new Caller<>(Constant.Event.USER_ADDSUGGEST_SUCCESS,Constant.Event.USER_ADDSUGGEST_FAILURE);
//        c.execute(mApi.modifySettingInfo(body));
//    }
    
    public interface Api {
/*      
        @GET("/comment/pagec")
        Call<CommentPageRespBean> getCommentPage(@Query("book_id") int book_id, @Query("offset") int offset, @Query("limit") int limit);

        @POST("/comment/add")
        Call<BaseRespBean> addComment(@Body RequestBody body);
 */
        
        @POST("mylogin")
        Call<UserRespBean> getUserInfo(@Body RequestBody body);

        @POST("mysendsms")
        Call<ResponseBody> getSMS(@Body RequestBody body);

        @POST("myregister")
        Call<UserRespBean> doRegist(@Body RequestBody body);

        @POST("myupdatepasswd")//修改密码
        Call<UserRespBean> doModify(@Body RequestBody body);

        @POST("bindmobilebyuid")//老用户绑定手机号
        Call<UserRespBean> bindPhoneByUid(@Body RequestBody body);

        @POST("bindmobile")//第三方登陆绑定
        Call<UserRespBean> bindPhone(@Body RequestBody body);

        @POST("myfindpasswd")//重置密码
        Call<UserRespBean> doReset(@Body RequestBody body);

        @POST("mobilefeed")
        Call<ResponseBody> mobileFeed(@Body RequestBody body);

        @POST("myinfo")
        Call<GetSettingRespBean> getSettingInfo(@Body RequestBody body);

        @POST("setmyinfo")
        Call<ResponseBody> modifySettingInfo(@Body RequestBody body);

        @POST("usercomment")
        Call<MyCommentRespBean> getMyComment(@Body RequestBody body);

        @POST("favoritenews")
        Call<CollectNewsRespBean> getCollectNews(@Body RequestBody body);

        @POST("favoritegame")
        Call<CollectGameRespBean> getCollectGame(@Body RequestBody body);

        @POST("favoriteolzt")//收藏的网游
        Call<CollectGameRespBean> getCollectNet(@Body RequestBody body);

        @POST("favoritesyzt")//收藏的手游
        Call<CollectGameRespBean> getCollectSY(@Body RequestBody body);

        @POST("getappversion")
        Call<VersionRespBean> getVersion(@Body RequestBody body);

        @POST("favoritesyou")
        Call<MGChinaRespBean> getCollectMG(@Body RequestBody body);

        @POST("favoriteka")
        Call<MGGiftRespBean> getCollectGift(@Body RequestBody body);

        @POST("saveappfeed")
        Call<ResponseBody> feedBack(@Body RequestBody body);

        @POST("getarcfavorite")//获取收藏状态
        Call<ResponseBody> getArcFavorite(@Body RequestBody body);
        
        @POST("setarcfavorite")//添加删除收藏
        Call<ResponseBody> setArcFavorite(@Body RequestBody body);
        
        @POST("addpraise")//文章评论点赞接口
        Call<ResponseBody> addPraise(@Body RequestBody body);
        
        @POST("bindopen")//绑定第三方账号
        Call<ResponseBody> bindopen(@Body RequestBody body);
        
        @POST("unbindopen")//解绑第三方账号
        Call<ResponseBody> unbindopen(@Body RequestBody body);
        
        @POST("getopenuser")//用于app第三方登录获取绑定信息接口
        Call<UserRespBean> getOpenUserInfo(@Body RequestBody body);

        @POST("allso")//搜索
        Call<NewSearchRespBean> allso(@Body RequestBody body);

        @POST("newallso")//搜索
        Call<NewSearchRespBean> newallso(@Body RequestBody body);

        @POST("sodjzt")//单机专题搜索
        Call<SearchGameRespBean> sodjzt(@Body RequestBody body);

        @POST("soolzt")//网游专题搜索
        Call<SearchGameRespBean> soolzt(@Body RequestBody body);

        @POST("sosyzt")//手游专题搜索
        Call<SearchGameRespBean> sosyzt(@Body RequestBody body);

        @POST("getappad")
        Call<ResponseBody> getAppad(@Body RequestBody body);

        @POST("opentitle")//开启头衔
        Call<ResponseBody> openTitle(@Body RequestBody body);

        @POST("cancel")//注销账户
        Call<ResponseBody> closeAccount(@Body RequestBody body);

        @POST("chgoldmobile")//验证旧手机号
        Call<ResponseBody> invalOldPhone(@Body RequestBody body);

        @POST("chgmobile")//验证新手机号
        Call<ResponseBody> invalNewPhone(@Body RequestBody body);

        @POST("address")//地址列表
        Call<AddressRespBean> getAddress(@Body RequestBody body);

        @POST("addaddr")//添加地址
        Call<ResponseBody> addAddress(@Body RequestBody body);

        @POST("editaddr")//修改地址
        Call<ResponseBody> modifyAddress(@Body RequestBody body);

        @POST("deladdr")//删除地址
        Call<ResponseBody> delAddress(@Body RequestBody body);

        @POST("defaultaddr")//删除地址
        Call<ResponseBody> setDefault(@Body RequestBody body);

        @POST("djvote")//文章评论点赞接口
        Call<ResponseBody> addVote(@Body RequestBody body);
    }
}
