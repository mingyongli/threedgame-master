package com.ws3dm.app.mvvm.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.yu.imgpicker.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class ForumDetailWebViewModel extends BaseViewModel {

    private MutableLiveData<resultsBean> isCollect = new MutableLiveData<>();

    public MutableLiveData<resultsBean> getIsCollect() {
        return isCollect;
    }

    /**
     * 收藏和取消收藏
     *
     * @param tid
     * @param isAdd //1添加2删除
     */
    public void addDelFavorite(String tid, int isAdd) {
        String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
        long time = System.currentTimeMillis();
        String validate = temp + tid + isAdd + time;
        String sign = StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", MyApplication.getUserData().uid);
            obj.put("tid", tid);
            obj.put("act", isAdd);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = obj.toString();
        OkHttpUtils.postString().url(NewUrl.SET_TID_FAVORITE).content(s).build().execute(new Callback<resultsBean>() {
            @Override
            public resultsBean parseNetworkResponse(Response response) throws IOException {
                return new Gson().fromJson(response.body().string(), resultsBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(resultsBean response) {
                if (response.getCode() == 1) {
                    getThreadCollect(tid);
                }
            }

        });
    }

    /**
     * 获取帖子收藏状态
     *
     * @param tid
     */
    public void getThreadCollect(String tid) {
        long time = System.currentTimeMillis();
        String validate = "" + MyApplication.getUserData().uid + tid + time;
        String sign = StringUtil.MD5(validate);
        Map<String, Object> map = new HashMap<>();
        map.put("uid", MyApplication.getUserData().uid);
        map.put("tid", tid);
        map.put("time", time);
        map.put("sign", sign);
        String s = JSON.toJSONString(map);
        OkHttpUtils.postString().url(NewUrl.GET_TID_FAVORITE).content(s).build().execute(new Callback<resultsBean>() {
            @Override
            public resultsBean parseNetworkResponse(Response response) throws IOException {
                return new Gson().fromJson(response.body().string(), resultsBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(resultsBean response) {
                isCollect.postValue(response);
            }
        });
    }

    /**
     * 提交举报
     *
     * @param uid
     * @param tid
     * @param pid
     * @param fid
     */
    public void submitReport(Context context, String uid, String tid, String pid, String fid) {//提交举报内容
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("tid", tid);
        map.put("pid", pid);
        map.put("fid", fid);
        String s = JSON.toJSONString(map);
        OkHttpUtils.postString().url(NewUrl.BBS_REPORT).content(s).build().execute(new Callback<resultsBean>() {
            @Override
            public resultsBean parseNetworkResponse(Response response) throws IOException {

                return new Gson().fromJson(response.body().string(), resultsBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(resultsBean response) {
                if (response.getCode() == 1) {
                    ToastUtil.showToast(context, "举报成功");
                } else {
                    ToastUtil.showToast(context, "失败");
                }
            }

        });


    }

    public class resultsBean {

        /**
         * code : 1
         * msg : success
         * data : {"is_collect":1}
         */

        private int code;
        private String msg;
        /**
         * is_collect : 1
         */

        private DataDTO data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public DataDTO getData() {
            return data;
        }

        public void setData(DataDTO data) {
            this.data = data;
        }

        public class DataDTO {
            private int is_collect;

            public int getIsCollect() {
                return is_collect;
            }

            public void setIsCollect(int is_collect) {
                this.is_collect = is_collect;
            }
        }
    }
}
