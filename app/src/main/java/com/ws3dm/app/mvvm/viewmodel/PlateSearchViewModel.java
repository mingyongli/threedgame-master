package com.ws3dm.app.mvvm.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

public class PlateSearchViewModel extends ViewModel {
    private final int pageSize = 10;
    public MutableLiveData<BaseViewModel.State> state = new MutableLiveData<>();
    public MutableLiveData<List<ForumPlateBean.DataBean.ListBean>> plateBean = new MutableLiveData<>();

    public void SearchPlate(String PlateId, String PlateName, int Page) {
        String uid = "";
        Map<String, Object> map = new HashMap<>();
        if (MyApplication.getUserData().uid != null) {
            uid = MyApplication.getUserData().uid;
            map.put("uid", uid);
        }
        map.put("plateId", PlateId);
        map.put("name", PlateName);
        map.put("page", Page);
        map.put("pageSize", pageSize);
        String s = JSON.toJSONString(map);
        OkHttpUtils.postString().url(NewUrl.PLATESO).content(s).build().connTimeOut(5000).execute(new Callback<ForumPlateBean>() {
            @Override
            public ForumPlateBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, ForumPlateBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {
                state.postValue(BaseViewModel.State.ERR);
            }

            @Override
            public void onResponse(ForumPlateBean response) {
                if (response.getCode() == 1) {
                    if (response.getData().getList().size() == 0) {
                        state.postValue(BaseViewModel.State.EMPTY);
                    } else {
                        state.postValue(BaseViewModel.State.SUCCESS);
                        plateBean.postValue(response.getData().getList());
                    }
                } else {
                    state.postValue(BaseViewModel.State.ERR);
                }
            }

        });
    }
}
