package com.ws3dm.app.mvp.presenter;

import android.os.Handler;
import android.os.Message;

import com.ws3dm.app.util.EventBus;
import com.ws3dm.app.Constant;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by xia on 5/14/17.
 */

public class BasePresenter {

    protected Handler mHandler     = null;
    private Object mEventObject = null;

    private HashMap<String,Long> mRequestTime;

    public synchronized void registerEvent() {
        // 多次重复注册事件会崩溃
        if (mEventObject == this) {
            return;
        }
        if (mEventObject != null) {
            EventBus.getDefault().unregister(mEventObject);
        }
        EventBus.getDefault().register(this);
        mEventObject = this;
    }

    public synchronized void unregisterEvent() {
        if (mEventObject != null) {
            EventBus.getDefault().unregister(mEventObject);
            mEventObject = null;
        }
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    /**
     * 访问间隔限制
     * @param key     请求函数
     * @param period  请求的时间间隔，单位秒
     * @return
     */
    protected boolean requestLimit(String key, int period) {
        long ts = new Date().getTime();
        if (mRequestTime==null) {
            mRequestTime = new HashMap<>();
            mRequestTime.put(key, ts);
            return false;
        }
        long last = mRequestTime.get(key);
        if (last == 0 || ts >= last + period * 1000) {
            mRequestTime.put(key, ts);
            return false;
        }
        return true;
    }


    public Message newMessage(int what, Object obj) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        return msg;
    }

    public void notify(int what, Object obj) {
        if (mHandler != null) {
            mHandler.sendMessage(newMessage(what, obj));
        }
    }

    public void notifyLoading() {
        notify(Constant.Notify.LOAD_START, null);
    }

    public void notifySuccess(Object obj) {
        notify(Constant.Notify.LOAD_SUCCESS, obj);
    }

    public void notifyNoMore() {
        notify(Constant.Notify.LOAD_NO_MORE, null);
    }

    public void notifyNoData(int id) {
        notify(Constant.Notify.LOAD_NO_DATA, id);
    }

    public void notifyFailure(int code, Object obj) {
        if (mHandler != null) {
//            Message reason = newMessage(code, error);
            notify(Constant.Notify.LOAD_FAILURE, obj);
        }
    }
}
