package com.ws3dm.app.mvvm.viewmodel;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.mvvm.bean.MyIndexBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ws3dm.app.mvvm.viewmodel.BaseViewModel.State.EMPTY;
import static com.ws3dm.app.mvvm.viewmodel.BaseViewModel.State.ERR;
import static com.ws3dm.app.mvvm.viewmodel.BaseViewModel.State.LOADING;
import static com.ws3dm.app.mvvm.viewmodel.BaseViewModel.State.SUCCESS;

public class MyForumViewModel extends BaseViewModel implements LifecycleObserver {

    public MutableLiveData<State> dataState = new MutableLiveData<>();
    //recycleView的显示状态(有无数据)
    public MutableLiveData<State> lateState = new MutableLiveData<>();
    public MutableLiveData<State> followState = new MutableLiveData<>();
    //两个RecycleView的数据
    /**
     * 最近浏览的版块数据
     */
    public MutableLiveData<List<MyIndexBean.DataBean.LatelyPlateBean>> lateLiveData = new MutableLiveData<>();
    /**
     * 关注的版块数据
     */
    public MutableLiveData<List<MyIndexBean.DataBean.FollowPlateBean>> followLiveData = new MutableLiveData<>();

    /**
     * 获取数据
     */
    public void getMyIndexData() {
        dataState.postValue(LOADING);
        String uid = MyApplication.getUserData().uid;
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        String s = JSON.toJSONString(map);
        OkHttpUtils.postString().url(NewUrl.MYINDEX).content(s).build().execute(new Callback<MyIndexBean>() {
            @Override
            public MyIndexBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, MyIndexBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {
                dataState.postValue(ERR);
            }

            @Override
            public void onResponse(MyIndexBean response) {
                if (response.getCode() == 1) {
                    dataState.postValue(SUCCESS);
                    if (response.getData().getLatelyPlate() == null) {
                        lateState.postValue(EMPTY);
                    } else {
                        lateState.postValue(SUCCESS);
                        lateLiveData.postValue(response.getData().getLatelyPlate());
                    }
                    if (response.getData().getFollowPlate() == null) {
                        followState.postValue(EMPTY);
                    } else {
                        followState.postValue(SUCCESS);
                        followLiveData.postValue(response.getData().getFollowPlate());
                    }

                } else {
                    dataState.postValue(ERR);
                }
            }

        });
    }
}
