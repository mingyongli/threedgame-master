package com.ws3dm.app.mvvm.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.mvvm.bean.ForumPlateBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForumPlateViewModel extends BaseViewModel {

    public MutableLiveData<State> state = new MutableLiveData<>();
    public MutableLiveData<List<ForumPlateBean.DataBean.ListBean>> plateBean = new MutableLiveData<>();

    public void getPlateContentList(String plateId, int type, int page) {
        String uid;
        if (MyApplication.getUserData() == null) {
            uid = "0";
        } else {
            uid = MyApplication.getUserData().uid;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("plateId", plateId);
        map.put("type", type);
        map.put("page", page);
        map.put("pageSize", 10);
        String s = JSON.toJSONString(map);
        OkHttpUtils.postString().url(NewUrl.PLATECONTENTLIST).content(s).build().connTimeOut(5000).execute(new Callback<ForumPlateBean>() {
            @Override
            public ForumPlateBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, ForumPlateBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {
                state.postValue(State.ERR);
            }

            @Override
            public void onResponse(ForumPlateBean response) {
                if (response.getCode() == 1) {
                    state.postValue(State.SUCCESS);
                    plateBean.postValue(response.getData().getList());
                } else {
                    state.postValue(State.ERR);
                }
            }

        });
    }
}
