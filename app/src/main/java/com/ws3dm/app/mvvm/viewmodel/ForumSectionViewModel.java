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
import com.ws3dm.app.bean.Forum;
import com.ws3dm.app.mvvm.bean.ForumListBean;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForumSectionViewModel extends ViewModel implements LifecycleObserver {
    public MutableLiveData<BaseViewModel.State> state = new MutableLiveData<>();
    public MutableLiveData<List<ForumListBean.DataBean.ListBean>> forumSection = new MutableLiveData<>();
    public MutableLiveData<Map<Integer, ForumListBean.DataBean.ListBean>> forumBean = new MutableLiveData<>();
    //用来记录当前界面的plateType的id
    public MutableLiveData<Integer> plateTypeId = new MutableLiveData<>();

    public void getInitList() {
        String uid = MyApplication.getUserData().uid;
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("page", 1);
        map.put("pageSize", 10);
        String s = JSON.toJSONString(map);
        OkHttpUtils.postString().url(NewUrl.LIST).content(s).build().execute(new Callback<ForumListBean>() {
            @Override
            public ForumListBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, ForumListBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {
                state.postValue(BaseViewModel.State.ERR);
            }

            @Override
            public void onResponse(ForumListBean response) {
                if (response.getCode() == 1) {
                    state.postValue(BaseViewModel.State.SUCCESS);
                    forumSection.postValue(response.getData().getList());
                    plateTypeId.postValue(response.getData().getList().get(0).getPlateTypeId());
                    //根据id插入对应的数据
//                    Map<Integer, ForumListBean.DataBean.ListBean> beanMap = new HashMap<>();
//                    for (ForumListBean.DataBean.ListBean listBean : response.getData().getList()) {
//                        beanMap.put(listBean.getPlateTypeId(), listBean);
//                    }
//                    forumBean.postValue(beanMap);
                } else {

                }
            }
        });
    }

    public void getContentList(int platetypeid, int page) {
        String uid = MyApplication.getUserData().uid;
        Map<String, Object> map = new HashMap<>();
        map.put("plateTypeId", platetypeid);
        map.put("uid", uid);
        map.put("page", page);
        map.put("pageSize", 20);
        String s = JSON.toJSONString(map);
        OkHttpUtils.postString().url(NewUrl.LIST).content(s).build().execute(new Callback<ForumListBean>() {
            @Override
            public ForumListBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, ForumListBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {
                state.postValue(BaseViewModel.State.ERR);
            }

            @Override
            public void onResponse(ForumListBean response) {
                if (response.getCode() == 1) {
                    state.postValue(BaseViewModel.State.SUCCESS);
                    //forumSection.postValue(response.getData().getList());
                    //判断是否为初始化数据返回真实的id
                    if (platetypeid == 0) {
                        plateTypeId.postValue(response.getData().getList().get(0).getPlateTypeId());
                    }
                    //根据id插入对应的数据
                    Map<Integer, ForumListBean.DataBean.ListBean> beanMap = new HashMap<>();
                    for (ForumListBean.DataBean.ListBean listBean : response.getData().getList()) {
                        beanMap.put(listBean.getPlateTypeId(), listBean);
                    }
                    forumBean.postValue(beanMap);
                } else {
                }
            }
        });
    }
}
