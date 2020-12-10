package com.ws3dm.app.mvp.presenter;

import com.ws3dm.app.mvp.model.RespBean.CollectNewsRespBean;
import com.ws3dm.app.mvp.model.RespBean.GameListRespBean;
import com.ws3dm.app.mvp.model.RespBean.GetSettingRespBean;
import com.ws3dm.app.mvp.model.RespBean.MyCommentRespBean;
import com.ws3dm.app.mvp.model.RespBean.SearchGameRespBean;
import com.ws3dm.app.mvp.model.RespBean.VersionRespBean;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.Constant;
import com.ws3dm.app.event.ErrorEvent;
import com.ws3dm.app.mvp.model.RespBean.UserRespBean;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

/**
 * Describution :用户 Presentmenter
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/16 15:47
 **/
public class UserPresenter extends BasePresenter {

    private static final String TAG = "UserPresenter";

    private static UserPresenter mUserPresenter= null;

    private UserService mUserService;

    public static synchronized UserPresenter getInstance() {
        if (mUserPresenter == null) {
            mUserPresenter = new UserPresenter();
        }
        return mUserPresenter;
    }

    private UserPresenter() {
        mUserService = new UserService();
        registerEvent();
    }

    /**
     * @param body 用户登录 传入json 字符串
     */
    public void userLogin(String body) {
        mUserService.login(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_LOGIN_SUCCESS)})
    public void userLoginSuccess(UserRespBean bean) {
        notify(Constant.Notify.RESULT_USER_LOGIN, bean);
    }


    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_LOGIN_FAILURE)})
    public void userLoginFailure(ErrorEvent result) {
        notifyFailure(result.code, result);
    }

    /**
     * @param body 用户登录 传入json 字符串
     */
    public void userRegist(String body) {
        mUserService.regist(body);
    }
    
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_REGIST_SUCCESS)})
    public void userRegistSuccess(UserRespBean bean) {
        notify(Constant.Notify.RESULT_USER_REGISTER, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_REGIST_FAILURE)})
    public void userRegistFailure(ErrorEvent result) {
        notifyFailure(result.code, result);
    }

    /**
     * @param body 修改 密码 传入json 字符串
     */
    public void modifyPass(String body) {
        mUserService.modifyPass(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_MODIFYPASS_SUCCESS)})
    public void modifySuccess(UserRespBean bean) {
        notify(Constant.Notify.RESULT_MODIFY_PASS, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_MODIFYPASS_FAILURE)})
    public void modifyFailure(ErrorEvent result) {
        notifyFailure(result.code, result);
    }
    /**
     * 老用户绑定手机号
     */
    public void bindPhoneByUid(String body) {
        mUserService.bindPhoneByUid(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_BINDBYID_SUCCESS)})
    public void bindPhoneByUidSuccess(UserRespBean bean) {
        notify(Constant.Notify.RESULT_USER_BINDBYID, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_BINDBYID_FAILURE)})
    public void bindPhoneByUidFailure(ErrorEvent result) {
        notifyFailure(result.code, result);
    }
    
    /**
     * 第三方登陆绑定，非老用户
     */
    public void bindPhone(String body) {
        mUserService.bindPhone(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_BINDPHONE_SUCCESS)})
    public void bindPhoneSuccess(UserRespBean bean) {
        notify(Constant.Notify.RESULT_USER_BINDPHONE, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_BINDPHONE_FAILURE)})
    public void bindPhoneFailure(ErrorEvent result) {
        notifyFailure(result.code, result);
    }

    /**
     * @param body 重置（忘记） 密码 传入json 字符串
     */
    public void resetPass(String body) {
        mUserService.resetPass(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_RESET_SUCCESS)})
    public void resetSuccess(UserRespBean bean) {
        notify(Constant.Notify.RESULT_RESET_PASS, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_RESET_FAILURE)})
    public void resetFailure(ErrorEvent result) {
        notifyFailure(result.code, result);
    }

    /**
     * @param body 获取设置页信息 传入json 字符串
     */
    public void getSetting(String body) {
        mUserService.getSetting(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_GETSETTING_SUCCESS)})
    public void getSettingSuccess(GetSettingRespBean bean) {
        notify(Constant.Notify.RESULT_GETSETTING, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_GETSETTING_FAILURE)})
    public void getSettingFailure(ErrorEvent result) {
        notifyFailure(result.code, result);
    }

    /**
     * @param body 获取我的评论 传入json 字符串
     */
    public void getMyComment(String body) {
        mUserService.getMyComment(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_GETMYCOMMENT_SUCCESS)})
    public void getMyCommentSuccess(MyCommentRespBean bean) {
        notify(Constant.Notify.RESULT_GETMYCOMMENT, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_GETMYCOMMENT_FAILURE)})
    public void getMyCommentFailure(ErrorEvent result) {
        notifyFailure(result.code, result);
    }

    /**
     * @param body 我的新闻收藏 传入json 字符串
     */
    public void myNewsCollect(String body) {
        mUserService.getCollectNews(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.MY_NEWS_COLLECT_SUCCESS)})
    public void myNewsCollectSuccess(CollectNewsRespBean bean) {
        notify(Constant.Notify.RESULT_MY_NEWS_COLLECT, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.MY_NEWS_COLLECT_FAILURE)})
    public void myNewsCollectFailure(ErrorEvent result) {
        notifyFailure(result.code, result);
    }

    /**
     * @param body 我的礼包收藏 传入json 字符串
     */
    public void myGiftCollect(String body) {
        mUserService.getCollectGift(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.MY_GAME_COLLECT_SUCCESS)})
    public void myGiftCollectSuccess(CollectNewsRespBean bean) {
        notify(Constant.Notify.RESULT_MY_GAME_COLLECT, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.MY_GAME_COLLECT_FAILURE)})
    public void myGiftCollectFailure(ErrorEvent result) {
        notifyFailure(result.code, result);
    }

    /**
     * @param body 版本号
     */
    public void getVersion(String body) {
        mUserService.getVersion(body);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GET_VERSION_SUCCESS)})
    public void getVersionSuccess(VersionRespBean bean) {
        notify(Constant.Notify.APP_VERSION, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.GET_VERSION_FAILURE)})
    public void getVersionFailure(ErrorEvent result) {
        notifyFailure(result.code, result);
    }


//    /**
//     * @param body 修改设置页信息 传入json 字符串
//     */
//    public void modifySetting(String body) {
//        mUserService.modifySetting(body);
//    }
//    
//    /**
//     * @param body 添加反馈 传入json 字符串
//     */
//    public void addSuggest(String body) {
//        mUserService.modifySetting(body);
//    }
    
    

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_MODIFYSETTING_SUCCESS)})
    public void modifySettingSuccess(GetSettingRespBean bean) {
        notify(Constant.Notify.RESULT_MODITYSETTING, bean);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Constant.Event.USER_MODIFYSETTING_FAILURE)})
    public void modifySettingFailure(ErrorEvent result) {
        notifyFailure(result.code, result);
    }

}
