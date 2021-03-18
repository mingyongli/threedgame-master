package com.ws3dm.app.network.service;


import com.ws3dm.app.mvvm.bean.AdexposureBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AdExposureApi {
    @POST("lunbodjl")
    Call<AdexposureBean> LunBoTuAD(@Body RequestBody map);
}
