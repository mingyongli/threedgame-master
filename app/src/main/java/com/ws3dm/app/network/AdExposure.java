package com.ws3dm.app.network;

import com.alibaba.fastjson.JSON;
import com.ws3dm.app.mvvm.bean.AdexposureBean;
import com.ws3dm.app.network.service.AdExposureApi;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 所有的轮播图广告 的点击曝光量
 */
public class AdExposure {

    private static final AdExposure instance = new AdExposure();


    private AdExposure() {
    }

    public static AdExposure getInstance() {
        return AdExposure.instance;
    }

    /**
     * @param id         广告id
     * @param uuid       手机的uuid
     * @param unitType   手机型号
     * @param appVersion app版本号
     * @param phoneSys   手机系统 Ios/Android
     * @param time       时间
     */
    public void putExposure(int id, String uuid, int appVersion, String phoneSys, String unitType, String time) {
        AdExposureApi adExposure = RetrofitFactory.getNewRetrofitV4(0).create(AdExposureApi.class);
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("uuid", uuid);
        map.put("unittype", unitType);
        map.put("appversion", appVersion);
        map.put("phonesys", phoneSys);
        map.put("time", time);
        String s = JSON.toJSONString(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        adExposure.LunBoTuAD(body).enqueue(new Callback<AdexposureBean>() {
            @Override
            public void onResponse(Call<AdexposureBean> call, Response<AdexposureBean> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    int code = response.body().getCode();
                }
            }

            @Override
            public void onFailure(Call<AdexposureBean> call, Throwable t) {

            }
        });
    }
}
