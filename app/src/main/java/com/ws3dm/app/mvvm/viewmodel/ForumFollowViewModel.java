package com.ws3dm.app.mvvm.viewmodel;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.mvvm.bean.MyFollowBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForumFollowViewModel extends BaseViewModel implements LifecycleObserver {
    private static final int pageSize = 10;
    //请求的数据
    private MutableLiveData<List<MyFollowBean.DataBean.FollowPlateBean>> myFollowData = new MutableLiveData<>();
    //请求时的状态
    private MutableLiveData<State> dataState = new MutableLiveData<>();

    public MutableLiveData<List<MyFollowBean.DataBean.FollowPlateBean>> getMyFollowData() {
        return myFollowData;
    }

    public MutableLiveData<State> getDataState() {
        return dataState;
    }

    public void getFollowList(int page) {
        String uid = MyApplication.getUserData().uid;
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("page", page);
        map.put("pageSize", pageSize);
        String s = JSON.toJSONString(map);
        OkHttpUtils.postString().url(NewUrl.MYFOLLOW).content(s).build().execute(new Callback<MyFollowBean>() {
            @Override
            public MyFollowBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, MyFollowBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {
                dataState.postValue(State.ERR);
            }

            @Override
            public void onResponse(MyFollowBean response) {
                if (response.getCode() == 1) {
                    dataState.postValue(State.SUCCESS);
                    myFollowData.postValue(response.getData().getFollowPlate());
                } else {
                    dataState.postValue(State.ERR);
                }
            }
        });
    }
}
