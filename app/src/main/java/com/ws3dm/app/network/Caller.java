package com.ws3dm.app.network;

import com.ws3dm.app.event.ErrorEvent;
import com.ws3dm.app.mvp.model.RespBean.BaseRespBean;
import com.ws3dm.app.util.EventBus;
import com.ws3dm.app.Constant;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Caller<T> {

    protected String mSuccessTag;
    protected String mFailureTag;

    protected MediaType mMediaTypeJson = null;

    public Caller(String successTag) {
        mSuccessTag = successTag;
        mFailureTag = Constant.Event.DEFAULT_FAILURE;
    }

    public Caller(String successTag, String failureTag) {
        mSuccessTag = successTag;
        mFailureTag = failureTag;
    }

    protected MediaType getMediaTypeJson() {
        if (mMediaTypeJson == null) {
            mMediaTypeJson = MediaType.parse("application/json; charset=utf-8");
        }
        return mMediaTypeJson;
    }

    public ErrorEvent catchError(Response response) {
        if (response.code() != 200) {
            return new ErrorEvent(response.code(), response.message());
        }

        BaseRespBean res = (BaseRespBean) response.body();
        if (res == null) {
            return new ErrorEvent(-1, "decode response body failed");
        }

        if (res.getCode() != 1) {//用户信息为1
            return new ErrorEvent(res.getCode(), res.getMsg());
        }
        return null;
    }

    public ErrorEvent newThrowableEvent(Call c, Throwable throwable) {
        return new ErrorEvent(-1, c.request().toString() + " : " + throwable.getMessage());
    }

    public void postEvent(String tag, Object event) {
        EventBus.getDefault().post(tag, event);
    }

    public RequestBody encode(Object args) {
        String payload = new Gson().toJson(args);
        return RequestBody.create(getMediaTypeJson(), payload);
    }

    public void execute(Call<T> call) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                ErrorEvent error = catchError(response);
                if (error == null) {
                    postEvent(mSuccessTag, response.body());
                } else {
                    postEvent(mFailureTag, error);
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable throwable) {
//                //返回失败时的详细处理（示例仅供参考）
//                if (throwable instanceof EOFException || throwable instanceof ConnectException || throwable instanceof SocketException || 
//                        throwable instanceof BindException || throwable instanceof SocketTimeoutException || throwable instanceof UnknownHostException) {
//                    Toast.makeText(mContext, "网络异常，请稍后重试！", Toast.LENGTH_SHORT).show();
//                } else if (throwable instanceof ApiException) {
//                    onError(mWhichRequest, throwable);
//                } else {
//                    Toast.makeText(mContext, "未知错误！", Toast.LENGTH_SHORT).show();
//                }
                postEvent(mFailureTag, newThrowableEvent(call, throwable));
            }
        });
    }

    public void callback(Call<T>call, Callback<T> callback) {
        call.enqueue(callback);
    }
}
