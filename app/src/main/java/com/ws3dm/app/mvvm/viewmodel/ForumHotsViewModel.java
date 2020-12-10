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
import com.ws3dm.app.mvvm.bean.PlateContentBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForumHotsViewModel extends ViewModel implements LifecycleObserver {
    public MutableLiveData<BaseViewModel.State> state = new MutableLiveData<>();
    public MutableLiveData<List<PlateContentBean.DataBean.ListBean>> hotListData = new MutableLiveData<>();

    public void getHotsList(int page) {
        String uid = "";
        if (MyApplication.getUserData() == null) {
            uid = "0";
        } else {
            uid = MyApplication.getUserData().uid;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("page", page);
        map.put("pageSize", 10);
        String s = JSON.toJSONString(map);
        OkHttpUtils.postString().url(NewUrl.HOTTOPIC).content(s).build().connTimeOut(5000).execute(new Callback<PlateContentBean>() {
            @Override
            public PlateContentBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, PlateContentBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {
                state.postValue(BaseViewModel.State.ERR);
            }

            @Override
            public void onResponse(PlateContentBean response) {
                if (response.getCode() == 1) {
                    state.postValue(BaseViewModel.State.SUCCESS);
                    hotListData.postValue(response.getData().getList());
                } else {

                }
            }
        });
    }
}
