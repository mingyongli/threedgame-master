package com.ws3dm.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.UserAttentionActivityBinding;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAttentionActivity extends BaseActivity{
    private UserAttentionActivityBinding mBinding;
    private CommonRecyclerAdapter<UserFollwRoot.UserFollw> mAdapter;
    private int index = 0;
    private UserDetailActivity.UserDetail.Info uui;
    @Override
    protected void init() {
        mBinding = bindView(R.layout.user_attention_activity);
        mBinding.setHandler(this);
        uui = (UserDetailActivity.UserDetail.Info) getIntent().getSerializableExtra("data");
        if(uui == null){
            UserDataBean userData = MyApplication.getUserData();
            getMyfollw(userData.uid);
        }else{
            mBinding.tvTitle.setText(uui.getNickname()+" 的关注");
            getMyfollw(uui.getUid()+"");
        }
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.myfollow:
                if(index == 0) return;
                index = 0;
                page = 1;
                if(mAdapter!=null){
                    mAdapter.cleaList();
                }
                mBinding.myfollow.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                mBinding.myfollowauth.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                if(uui == null){
                    UserDataBean userData = MyApplication.getUserData();
                    getMyfollw(userData.uid);
                }else{
                    getMyfollw(uui.getUid()+"");
                }
                break;
            case R.id.myfollowauth:
                if(index == 1) return;
                index = 1;
                page = 1;
                if(mAdapter!=null){
                    mAdapter.cleaList();
                }
                mBinding.myfollow.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                mBinding.myfollowauth.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                if(uui == null){
                    UserDataBean userData = MyApplication.getUserData();
                    getMyfollw(userData.uid);
                }else{
                    getMyfollw(uui.getUid()+"");
                }
                break;
        }
    }


    private void initDynamic(UserFollwRoot ud){
        if (MyApplication.getUserData().loginStatue) {//已经登录
            mBinding.followCount.setText("共关注 "+ud.getData().getList().size());
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mBinding.recyclerview.setLayoutManager(layoutManager);
            mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
            mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

            mAdapter = new CommonRecyclerAdapter<UserFollwRoot.UserFollw>(mContext, R.layout.adapter_user_attention_item) {
                @Override
                public void bindData(RecyclerViewHolder holder, final int position, final UserFollwRoot.UserFollw item) {
                    View view = holder.getView(R.id.img);
                    final TextView status = holder.getView(R.id.status);
                    status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(item.getIs_follow() == 0){
                                status.setText("√ 已关注");
                                item.setIs_follow(1);
                                status.setTextColor(Color.parseColor("#444444"));
                                status.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_remain));
                                subfollow(1,index == 0 ?item.getUid():item.getAuthor_id());
                            }else if(item.getIs_follow() == 1){
                                status.setText("+ 关注");
                                item.setIs_follow(0);
                                status.setTextColor(Color.parseColor("#ffffff"));
                                status.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
                                subfollow(2,index == 0 ?item.getUid():item.getAuthor_id());
                            }else{
                                item.setIs_follow(0);
                                status.setText("+ 关注");
                                status.setTextColor(Color.parseColor("#ffffff"));
                                status.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
                                subfollow(2,index == 0 ?item.getUid():item.getAuthor_id());
                            }
                        }
                    });
                    GlideUtil.loadCircleImage(mContext,item.getAvatarstr(),(ImageView) view);
                    if(index == 0){
                        holder.setText(R.id.nick_name, item.getNickname());
                    }else{
                        holder.setText(R.id.nick_name, item.getAuthor_name());
                    }
                    if(item.getIs_follow() == 0){
                        status.setText("+ 关注");
                        status.setTextColor(Color.parseColor("#444444"));
                        status.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_remain));
                    }else if(item.getIs_follow() == 1){
                        status.setText("√ 已关注");
                        status.setTextColor(Color.parseColor("#444444"));
                        status.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_remain));
                    }else{
                        status.setText("⇌ 互相关注");
                        status.setTextColor(Color.parseColor("#ffffff"));
                        status.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
                    }
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent;
                            if(index == 0){
                                intent = new Intent(UserAttentionActivity.this,
                                        UserDetailActivity.class);
                                intent.putExtra("uid",item.getUid());
                            }else{
                                intent = new Intent(UserAttentionActivity.this,
                                        AuthorDetailActivity.class);
                                intent.putExtra("uid",item.getAuthor_id());
                            }

                            UserAttentionActivity.this.startActivity(intent);
                        }
                    });
                }
            };
            mBinding.recyclerview.setAdapter(mAdapter);
            mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
                @Override
                public void onRefresh() {
                    page = 1;
                    if(uui == null){
                        UserDataBean userData = MyApplication.getUserData();
                        getMyfollw(userData.uid);
                    }else{
                        getMyfollw(uui.getUid()+"");
                    }
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
                    if(uui == null){
                        UserDataBean userData = MyApplication.getUserData();
                        getMyfollw(userData.uid);
                    }else{
                        getMyfollw(uui.getUid()+"");
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.recyclerview.loadMoreComplete();
                        }
                    },1000);
                }
            });
            if(page == 1){
                mAdapter.clearAndAddList(ud.getData().getList());
            }else{
                mAdapter.appendList(ud.getData().getList());
            }
        }
    }
    //关注or取消
    private void subfollow(int type,String id){
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + id +type + time + NewUrl.KEY);

        Map<String, Object> values = new HashMap<>();
        values.put("uid",userData.uid);
        values.put("sign",sign);
        values.put("time",time);
        values.put("type",type);
        if(index==0){
            values.put("follow_uid",id);
        }else{
            values.put("author_id",id);
        }
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(index == 0 ? NewUrl.SUBFOLLOW : NewUrl.SUBFOLLOW_AUTH)
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
    int page = 1;
    int pageSize = 20;
    //关注列表
    private void getMyfollw(String uid){
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(uid + pageSize + page + time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("uid",uid);
        values.put("sign",sign);
        values.put("time",time);
        values.put("pagesize",pageSize);
        values.put("page",page);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(index == 0 ? NewUrl.MY_FOLLOW : NewUrl.MY_FOLLOW_AUTH)
                .content(json)
                .build()
                .execute(new Callback<UserFollwRoot>() {

                    @Override
                    public UserFollwRoot parseNetworkResponse(Response response) throws IOException {
                        String string = response.body().string();
                        return new Gson().fromJson(string, UserFollwRoot.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message",e.getMessage());
                    }

                    @Override
                    public void onResponse(UserFollwRoot response) {
                        if(response.getCode() == 1){
                            initDynamic(response);
                        }else{
                            ToastUtil.showToast(UserAttentionActivity.this,response.getMsg());
                        }
                    }

                });

    }

    public class UserFollwRoot{
        private int code;
        private UserFollwList data;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public UserFollwList getData() {
            return data;
        }

        public void setData(UserFollwList data) {
            this.data = data;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        class UserFollwList{
            private List<UserFollw> list;

            public List<UserFollw> getList() {
                return list;
            }

            public void setList(List<UserFollw> list) {
                this.list = list;
            }
        }
        class UserFollw{
            private String uid;
            private String nickname;
            private String avatarstr;
            private int is_follow;

            private String author_id;
            private String author_name;

            public String getAuthor_id() {
                return author_id;
            }

            public void setAuthor_id(String author_id) {
                this.author_id = author_id;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatarstr() {
                return avatarstr;
            }

            public void setAvatarstr(String avatarstr) {
                this.avatarstr = avatarstr;
            }

            public int getIs_follow() {
                return is_follow;
            }

            public void setIs_follow(int is_follow) {
                this.is_follow = is_follow;
            }
        }
    }
}
