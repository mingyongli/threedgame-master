package com.ws3dm.app.mvvm.viewmodel;

import android.content.Context;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.mvvm.bean.PlateListHeadBean;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SectionPageViewModel extends ViewModel implements LifecycleObserver {
    private MutableLiveData<BaseViewModel.State> state = new MutableLiveData<>();
    private MutableLiveData<BaseViewModel.State> FollowState = new MutableLiveData<>();
    private MutableLiveData<PlateListHeadBean.DataBean> headData = new MutableLiveData<>();

    public MutableLiveData<BaseViewModel.State> getState() {
        return state;
    }

    public MutableLiveData<BaseViewModel.State> getFollowState() {
        return FollowState;
    }

    public MutableLiveData<PlateListHeadBean.DataBean> getHeadData() {
        return headData;
    }

    public void getPlateListHead(int plateId) {
        String uid = "";
        if (MyApplication.getUserData() == null) {
            uid = "0";
        } else {
            uid = MyApplication.getUserData().uid;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("plateId", plateId);
        String s = JSON.toJSONString(map);
        OkHttpUtils.postString().url(NewUrl.PLATELISTHEAD).content(s).build().connTimeOut(5000).execute(new Callback<PlateListHeadBean>() {
            @Override
            public PlateListHeadBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, PlateListHeadBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {
                state.postValue(BaseViewModel.State.ERR);
            }

            @Override
            public void onResponse(PlateListHeadBean response) {
                if (response.getCode() == 1) {
                    state.postValue(BaseViewModel.State.SUCCESS);
                    headData.postValue(response.getData());
                } else {
                    state.postValue(BaseViewModel.State.ERR);
                }
            }
        });
    }

    //关注和取消关注
    public void plateFollow(Context context, int plateId, int type) {
        String uid;
        if (MyApplication.getUserData() != null && MyApplication.getUserData().loginStatue ) {
            uid = MyApplication.getUserData().uid;
        } else {
            ToastUtil.showToast(context, "请登录app");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("plateId", plateId);
        map.put("type", type);
        String s = JSON.toJSONString(map);
        OkHttpUtils.postString().url(NewUrl.PLATEFOLLOW).content(s).build().connTimeOut(5000).execute(new Callback<FollowBean>() {
            @Override
            public FollowBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, FollowBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {
                FollowState.postValue(BaseViewModel.State.ERR);
            }

            @Override
            public void onResponse(FollowBean response) {
                if (response.getCode() == 1) {
                    FollowState.postValue(BaseViewModel.State.SUCCESS);
                }
            }

        });
    }

    class FollowBean {

        /**
         * code : 1
         * msg : success
         */

        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
