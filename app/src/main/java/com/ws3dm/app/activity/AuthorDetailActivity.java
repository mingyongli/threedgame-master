package com.ws3dm.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.AuthorDetailLayoutBinding;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorDetailActivity extends BaseActivity{
    private AuthorDetailLayoutBinding mBinding;
    private CommonRecyclerAdapter<AuthorList.Auth> mAdapter;
    private String auth_id;
    @Override
    protected void init() {
        mBinding = bindView(R.layout.author_detail_layout);
        mBinding.setHandler(this);
        auth_id = getIntent().getStringExtra("uid");
        getAuthinfo();
        getUserDynamic();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }

    //获取用户中心所有数据
    private void getAuthinfo(){
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid +auth_id+ time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("uid",userData.uid);
        values.put("sign",sign);
        values.put("time",time);
        values.put("author_id",auth_id);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.AUTH_INFO)
                .content(json)
                .build()
                .execute(new Callback<AuthorDetail>() {

                    @Override
                    public AuthorDetail parseNetworkResponse(Response response) throws IOException {
                        String body = response.body().string();
                        return new Gson().fromJson(body, AuthorDetail.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message",e.getMessage());
                    }

                    @Override
                    public void onResponse(AuthorDetail response) {
                        if(response.getCode() == 1){
                            initView(response.getData().getInfo());
                        }
                    }
                });
    }


    int page = 1;
    int pageSize = 20;
    private void getUserDynamic(){
        long time = System.currentTimeMillis();
        String str = pageSize +""+ page + auth_id + time + NewUrl.KEY;
        String sign = StringUtil.newMD5(str);
        Map<String, Object> values = new HashMap<>();
        values.put("sign",sign);
        values.put("time",time);
        values.put("pagesize",pageSize);
        values.put("page",page);
        values.put("author_id", auth_id);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.AUTH_LIST)
                .content(json)
                .build()
                .execute(new Callback<AuthorList>() {

                    @Override
                    public AuthorList parseNetworkResponse(Response response) throws IOException {
                        String string = response.body().string();
                        return new Gson().fromJson(string, AuthorList.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message",e.getMessage());
                    }

                    @Override
                    public void onResponse(AuthorList response) {
                        if(response.getCode() == 1){
                            initAuthList(response.getData());
                        }else{
                            ToastUtil.showToast(AuthorDetailActivity.this,response.getMsg());
                        }
                    }

                });

    }

    public static String getDateStringByTimeSTamp(Long timeStamp,String pattern){
        String result = null;
        Date date = new Date(timeStamp*1000);
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        result = sd.format(date);
        return result;
    }
    private void initAuthList(AuthorList.Data ud){
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mBinding.recyclerview.setLayoutManager(layoutManager);
            mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
            mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

            mAdapter = new CommonRecyclerAdapter<AuthorList.Auth>(mContext, R.layout.adapter_author_detail_item) {
                @Override
                public void bindData(RecyclerViewHolder holder, final int position, final AuthorList.Auth item) {
                    View view = holder.getView(R.id.img);
                    GlideUtil.loadImage(mContext,item.getLitpic(),(ImageView) view);
                    holder.setText(R.id.content, item.getTitle());
                    holder.setText(R.id.time, getDateStringByTimeSTamp((long)item.getPubdate_at(),"yyyy-MM-dd"));
                    holder.setText(R.id.good_number, item.getTotal_ct()+"");

                    holder.getView(R.id.root_view).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AuthorList.Auth db = mAdapter.getDataByPosition(position);
                            NewsBean nb =new NewsBean();
                            nb.setAid((int)db.getAid());
                            nb.setArcurl(db.getArcurl());
                            nb.setBt_title(db.getTitle());
                            nb.setWebviewurl(db.getWebviewurl());


                            Intent intent = new Intent(mContext, NewsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("newsBean",nb);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            };
            mBinding.recyclerview.setAdapter(mAdapter);
            mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
                @Override
                public void onRefresh() {
                    page = 1;
                    getUserDynamic();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.recyclerview.refreshComplete();
                        }
                    },1000);
                }

                @Override
                public void onLoadMore() {
                    page++;
                    getUserDynamic();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.recyclerview.loadMoreComplete();
                        }
                    },1000);
                }
            });
            if(page == 1){
                if(mAdapter!=null)
                    mAdapter.clearAndAddList(ud.getList());
            }else{
                if(mAdapter!=null)
                    mAdapter.appendList(ud.getList());
            }

    }

    public class AuthorList {

        private int code;
        private Data data;
        private String msg;
        public void setCode(int code) {
            this.code = code;
        }
        public int getCode() {
            return code;
        }

        public void setData(Data data) {
            this.data = data;
        }
        public Data getData() {
            return data;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
        public String getMsg() {
            return msg;
        }

        public class Auth {

            private int aid;
            private String title;
            private String litpic;
            private String arcurl;
            private int total_ct;
            private int showtype;
            private int pubdate_at;
            private String webviewurl;

            public String getArcurl() {
                return arcurl;
            }

            public void setArcurl(String arcurl) {
                this.arcurl = arcurl;
            }

            public void setAid(int aid) {
                this.aid = aid;
            }
            public int getAid() {
                return aid;
            }

            public void setTitle(String title) {
                this.title = title;
            }
            public String getTitle() {
                return title;
            }

            public void setLitpic(String litpic) {
                this.litpic = litpic;
            }
            public String getLitpic() {
                return litpic;
            }

            public void setTotal_ct(int total_ct) {
                this.total_ct = total_ct;
            }
            public int getTotal_ct() {
                return total_ct;
            }

            public void setShowtype(int showtype) {
                this.showtype = showtype;
            }
            public int getShowtype() {
                return showtype;
            }

            public void setPubdate_at(int pubdate_at) {
                this.pubdate_at = pubdate_at;
            }
            public int getPubdate_at() {
                return pubdate_at;
            }

            public void setWebviewurl(String webviewurl) {
                this.webviewurl = webviewurl;
            }
            public String getWebviewurl() {
                return webviewurl;
            }

        }

        public class Data {

            private List<Auth> list;

            public List<Auth> getList() {
                return list;
            }

            public void setList(List<Auth> list) {
                this.list = list;
            }
        }

    }







    private void initView(final AuthorDetail.Info udi) {
        GlideUtil.loadCircleImage(mContext, udi.getAvatarstr(), (ImageView) mBinding.imgHead);
        mBinding.nickName.setText(udi.getNickname());
        mBinding.detail.setText(StringUtil.isEmpty(udi.getBrief())?"简介：暂无简介":udi.getBrief());
        if(udi.getIs_follow() == 0){
            mBinding.isFollow.setText("+ 关注");
            mBinding.isFollow.setTextColor(Color.parseColor("#444444"));
            mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
        }else if(udi.getIs_follow()  == 1){
            mBinding.isFollow.setText("√ 已关注");
            mBinding.isFollow.setTextColor(Color.parseColor("#444444"));
            mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
        }else{
            mBinding.isFollow.setText("= 互相关注");
            mBinding.isFollow.setTextColor(Color.parseColor("#ffffff"));
            mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));

        }

        mBinding.isFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(udi.getIs_follow() == 0){
                    udi.setIs_follow(1);
                    mBinding.isFollow.setText("√ 已关注");
                    mBinding.isFollow.setTextColor(Color.parseColor("#444444"));
                    mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
                    subfollow(1,udi.getAuthor_id()+"");
                }else if(udi.getIs_follow() == 1){
                    udi.setIs_follow(1);
                    mBinding.isFollow.setText("+ 关注");
                    mBinding.isFollow.setTextColor(Color.parseColor("#ffffff"));
                    mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
                    subfollow(2,udi.getAuthor_id()+"");
                }else{
                    udi.setIs_follow(0);
                    mBinding.isFollow.setText("+ 关注");
                    mBinding.isFollow.setTextColor(Color.parseColor("#ffffff"));
                    mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
                    subfollow(2,udi.getAuthor_id()+"");
                }
            }
        });
    }


    public class AuthorDetail {

        private int code;
        private Data data;
        private String msg;

        public class Data {
            private Info info;
            public void setInfo(Info info) {
                this.info = info;
            }
            public Info getInfo() {
                return info;
            }
        }

        public class Info {

            private long author_id;
            private String nickname;
            private String brief;
            private String avatarstr;
            private String bgpic;
            private int is_follow;
            public void setAuthor_id(long author_id) {
                this.author_id = author_id;
            }
            public long getAuthor_id() {
                return author_id;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
            public String getNickname() {
                return nickname;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }
            public String getBrief() {
                return brief;
            }

            public void setAvatarstr(String avatarstr) {
                this.avatarstr = avatarstr;
            }
            public String getAvatarstr() {
                return avatarstr;
            }

            public void setBgpic(String bgpic) {
                this.bgpic = bgpic;
            }
            public String getBgpic() {
                return bgpic;
            }

            public void setIs_follow(int is_follow) {
                this.is_follow = is_follow;
            }
            public int getIs_follow() {
                return is_follow;
            }

        }


        public void setCode(int code) {
            this.code = code;
        }
        public int getCode() {
            return code;
        }

        public void setData(Data data) {
            this.data = data;
        }
        public Data getData() {
            return data;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
        public String getMsg() {
            return msg;
        }

    }



    //关注or取消
    private void subfollow(int type,String follow_uid){
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + follow_uid +type + time + NewUrl.KEY);

        Map<String, Object> values = new HashMap<>();
        values.put("uid",userData.uid);
        values.put("sign",sign);
        values.put("time",time);
        values.put("type",type);
        values.put("author_id",follow_uid);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.SUBFOLLOW_AUTH)
                .content(json)
                .build()
                .execute(new Callback<String>() {

                    @Override
                    public String parseNetworkResponse(Response response) throws IOException {
                        return response.body().string();
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message",e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("FFFFFFFF",response);
                    }

                });

    }

}
