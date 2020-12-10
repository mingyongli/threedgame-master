package com.ws3dm.app.network;


import com.ws3dm.app.network.service.ForumService;
import com.ws3dm.app.network.service.MGService;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.network.service.OriginalService;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.Constant;
import com.ws3dm.app.network.service.GameService;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private static Cache mCache;

    private static NewsService.Api mNewsService;
    private static UserService.Api mUserService;
    private static GameService.Api mGameService;
    private static MGService.Api mMGService;
    private static ForumService.Api mForumService;
    private static OriginalService.Api mOriginalService;

    public static void setCache(File directory, int maxSize) {
        mCache = new Cache(directory, maxSize);
    }

    public static class CacheInterceptor implements Interceptor {

        private int expire = 1; // expire second

        public CacheInterceptor setExpire(int second) {
            expire = second;
            return this;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request   = chain.request();
            Response response  = chain.proceed(request);
            Response response1 = response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control").header("Cache-Control", "max-age=" + expire).build();
            return response1;
        }
    }

    public static synchronized Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Accept", "application/json").addHeader("Content-Type", "application/json").addHeader("Encrypt-Off", "true").addHeader("App-Version", "1.0").removeHeader("User-Agent").addHeader("User-Agent",getUserAgent()).build();
                return chain.proceed(request);
            }
        };
    }

    /**
     * 返回正确的UserAgent
     * @return
     */
    private  static String getUserAgent(){
        String userAgent = "";
        StringBuffer sb = new StringBuffer();
        userAgent = System.getProperty("http.agent");//Dalvik/2.1.0 (Linux; U; Android 6.0.1; vivo X9L Build/MMB29M)

        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static synchronized Interceptor getDebugInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);//正式发布时去掉
        return logging;
    }

    public static synchronized OkHttpClient getHttpClient(int expire) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .retryOnConnectionFailure(true).addInterceptor(getHeaderInterceptor());
        
        if (expire > 0) {
            builder.addNetworkInterceptor(new CacheInterceptor().setExpire(expire)).cache(mCache);
        }

        return builder.build();
    }

    public static Retrofit getNewRetrofit(int expire) {//没有参数就传0
        return new Retrofit.Builder().baseUrl(Constant.NEWHOST).
                client(getHttpClient(expire)).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static Retrofit getNewRetrofitV4(int expire) {//没有参数就传0
        return new Retrofit.Builder().baseUrl(Constant.NEWHOSTV4).
                client(getHttpClient(expire)).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized NewsService.Api getNewsService() {
        if (mNewsService == null) {
            mNewsService = getNewRetrofit(0).create(NewsService.Api.class);
        }
        return mNewsService;
    }

    public static synchronized GameService.Api getGameService() {
        if (mGameService == null) {
            mGameService = getNewRetrofit(0).create(GameService.Api.class);
        }
        return mGameService;
    }
    
    public static synchronized ForumService.Api getForumService() {
        if (mForumService == null) {
            mForumService = getNewRetrofit(0).create(ForumService.Api.class);
        }
        return mForumService;
    }

    public static synchronized MGService.Api getMGService() {
        if (mMGService == null) {
            mMGService = getNewRetrofit(0).create(MGService.Api.class);
        }
        return mMGService;
    }

    public static synchronized UserService.Api getUserService() {
        if (mUserService == null) {
            mUserService = getNewRetrofit(0).create(UserService.Api.class);
        }
        return mUserService;
    }

    public static synchronized OriginalService.Api getOriginalService() {
        if (mOriginalService == null) {
            mOriginalService = getNewRetrofit(0).create(OriginalService.Api.class);
        }
        return mOriginalService;
    }
}


