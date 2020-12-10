package com.ws3dm.app.mvvm.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.mvvm.bean.PlateListSearchBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectionSearchViewModel extends ViewModel {
    private static final int pageSize = 10;
    public MutableLiveData<BaseViewModel.State> state = new MutableLiveData<>();
    public MutableLiveData<List<PlateListSearchBean.DataBean.ListBean>> searchBean = new MutableLiveData<>();

    public void searchPlate(String plateName, int page) {
        state.postValue(BaseViewModel.State.LOADING);
        String uid = "";
        if (MyApplication.getUserData() != null) {
            uid = MyApplication.getUserData().uid;
        } else {
            uid = "0";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("name", plateName);
        map.put("page", page);
        map.put("pageSize", pageSize);
        String s = JSON.toJSONString(map);
        OkHttpUtils.postString().url(NewUrl.LISTSO).content(s).build().connTimeOut(5000).execute(new Callback<PlateListSearchBean>() {
            @Override
            public PlateListSearchBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, PlateListSearchBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {
                state.postValue(BaseViewModel.State.ERR);
            }

            @Override
            public void onResponse(PlateListSearchBean response) {
                if (response.getCode() == 1) {
                    if (response.getData().getList().size() == 0) {
                        state.postValue(BaseViewModel.State.EMPTY);
                    } else {
                        state.postValue(BaseViewModel.State.SUCCESS);
                        searchBean.postValue(response.getData().getList());
                    }
                }
            }
        });
    }

}
