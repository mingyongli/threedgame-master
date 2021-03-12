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
import com.ws3dm.app.bean.UpLoadImageBean;
import com.ws3dm.app.mvp.model.RespBean.ForumTidTypeRespBean;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class PostContentViewModel extends ViewModel implements LifecycleObserver {
    private MutableLiveData<ForumTidTypeRespBean.DataBean> TidList = new MutableLiveData<>();
    private MutableLiveData<UpLoadImageBean> netImagePath = new MutableLiveData<>();
    private MutableLiveData<BaseViewModel.State> upImageState = new MutableLiveData<>();
    private MutableLiveData<BaseViewModel.State> postContentState = new MutableLiveData<>();

    public MutableLiveData<BaseViewModel.State> getPostContentState() {
        return postContentState;
    }

    public MutableLiveData<BaseViewModel.State> getUpImageState() {
        return upImageState;
    }

    public MutableLiveData<UpLoadImageBean> getNetImagePath() {
        return netImagePath;
    }

    public MutableLiveData<ForumTidTypeRespBean.DataBean> getTidList() {
        return TidList;
    }

    /**
     * 获取标签
     */
    public void getLabel(Context context, int fid) {

        Map<String, Object> map = new HashMap<>();
        long timeMillis = System.currentTimeMillis();
        String sign = StringUtil.MD5(fid + "" + timeMillis);
        map.put("fid", fid);
        map.put("time", timeMillis);
        map.put("sign", sign);
        String s = JSON.toJSONString(map);
        OkHttpUtils.postString().url(NewUrl.GET_FORUM_TID_TYPE).content(s).build().execute(new Callback<ForumTidTypeRespBean>() {
            @Override
            public ForumTidTypeRespBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();

                return new Gson().fromJson(string, ForumTidTypeRespBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(ForumTidTypeRespBean response) {
                if (response.getCode() == 1) {
                    upImageState.postValue(BaseViewModel.State.SUCCESS);
                    TidList.postValue(response.getData());
                } else {
                    upImageState.postValue(BaseViewModel.State.ERR);
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 上传图片
     *
     * @param context
     * @param plateId
     * @param file
     */
    public void upLoadImage(Context context, String plateId, File file) {
        upImageState.postValue(BaseViewModel.State.LOADING);
        long timeMillis = System.currentTimeMillis();
        String s = "" + MyApplication.getUserData().uid + plateId + 0 + 0 + timeMillis;
        String sign = StringUtil.MD5(s);

        OkHttpClient build = new OkHttpClient.Builder().build();
        RequestBody body = RequestBody.create(MediaType.get("image/png"), file);

        RequestBody builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("imgfile", file.getName(), body)
                .addFormDataPart("uid", MyApplication.getUserData().uid)
                .addFormDataPart("fid", plateId)
                .addFormDataPart("tid", "0")
                .addFormDataPart("pid", "0")
                .addFormDataPart("time", String.valueOf(timeMillis))
                .addFormDataPart("sign", sign).build();
        okhttp3.Request request = new okhttp3.Request.Builder().post(builder).url("https://my.3dmgame.com/app/uploadbbs").build();
        build.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                upImageState.postValue(BaseViewModel.State.ERR);
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    String string = Objects.requireNonNull(response.body()).string();
                    UpLoadImageBean imageData = new Gson().fromJson(string, UpLoadImageBean.class);
                    if (imageData.getCode() == 1) {
                        upImageState.postValue(BaseViewModel.State.SUCCESS);
                        netImagePath.postValue(imageData);
                    } else {
                        upImageState.postValue(BaseViewModel.State.ERR);
                        netImagePath.postValue(imageData);
                    }
                }
            }
        });
    }


    /**
     * 发布新主题
     *
     * @param context
     * @param typeId
     * @param plateId
     * @param title
     * @param content
     */
    public void PostContent(Context context, String typeId, String plateId, String title, String content) {
        postContentState.postValue(BaseViewModel.State.LOADING);
        Map<String, Object> map = new HashMap<>();
        map.put("uid", MyApplication.getUserData().uid);
        map.put("typeId", typeId);
        map.put("plateId", plateId);
        map.put("title", title);
        map.put("content", content);
        String json = JSON.toJSONString(map);
        OkHttpUtils.postString().url(NewUrl.NEW_THREAD).content(json).build().execute(new Callback<UpLoadImageBean>() {
            @Override
            public UpLoadImageBean parseNetworkResponse(Response response) throws IOException {
                return new Gson().fromJson(response.body().string(), UpLoadImageBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(UpLoadImageBean response) {
                if (response.getCode() == 1) {
                    postContentState.postValue(BaseViewModel.State.SUCCESS);
                    ToastUtil.showToast(context, response.getMsg());
                } else {
                    postContentState.postValue(BaseViewModel.State.ERR);
                    ToastUtil.showToast(context, response.getMsg());
                }
            }

        });

    }


}
