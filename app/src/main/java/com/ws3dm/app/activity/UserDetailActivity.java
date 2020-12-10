package com.ws3dm.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentTransaction;
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
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.UserDetailLayoutBinding;
import com.ws3dm.app.fragment.CollectFragment;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDetailActivity extends BaseActivity {
    private UserDetailLayoutBinding mBinding;
    private CommonRecyclerAdapter<UserDynamicRoot.UserDynamic> mAdapter;
    private String uid;
    UserDetail.Info mui;

    @Override
    protected void init() {
        mBinding = bindView(R.layout.user_detail_layout);
        mBinding.setHandler(this);
        uid = getIntent().getStringExtra("uid");
        if (!StringUtil.isEmpty(uid)) {
            getUserinfo();
            initFragemnt();
            getUserDynamic();
            mBinding.recyclerview.setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.follow_total:
                Intent intent = new Intent(this, UserAttentionActivity.class);
                intent.putExtra("data", mui);
                startActivity(intent);
                break;
            case R.id.fansi_total:
                Intent intents = new Intent(this, UserFansActivity.class);
                intents.putExtra("data", mui);
                startActivity(intents);
                break;
            case R.id.collect:
                mBinding.fragment.setVisibility(View.VISIBLE);
                mBinding.recyclerview.setVisibility(View.GONE);
                mBinding.replay.setTextColor(Color.parseColor("#585858"));
                mBinding.collect.setTextColor(Color.parseColor("#F0412A"));
                mBinding.replayView.setVisibility(View.GONE);
                mBinding.collectView.setVisibility(View.VISIBLE);
                break;
            case R.id.replay:
                mBinding.fragment.setVisibility(View.GONE);
                mBinding.recyclerview.setVisibility(View.VISIBLE);
                mBinding.replay.setTextColor(Color.parseColor("#F0412A"));
                mBinding.collect.setTextColor(Color.parseColor("#585858"));
                mBinding.replayView.setVisibility(View.VISIBLE);
                mBinding.collectView.setVisibility(View.GONE);
                break;
        }
    }

    private void initFragemnt() {
        CollectFragment collectFragment = new CollectFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        collectFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, collectFragment);
        fragmentTransaction.commit();
    }

    //获取用户中心所有数据
    private void getUserinfo() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + uid + time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("uid", userData.uid);
        values.put("sign", sign);
        values.put("time", time);
        values.put("user_uid", uid);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.USER_HOME_INFO)
                .content(json)
                .build()
                .execute(new Callback<UserDetail>() {

                    @Override
                    public UserDetail parseNetworkResponse(Response response) throws IOException {
                        String body = response.body().string();
                        return new Gson().fromJson(body, UserDetail.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message", e.getMessage());
                    }

                    @Override
                    public void onResponse(UserDetail response) {
                        if (response.getCode() == 1) {
                            initView(response.getData().getInfo());
                        }
                    }
                });
    }

    private void initView(final UserDetail.Info udi) {
        mui = udi;
        GlideUtil.loadCircleImage(mContext, udi.getAvatarstr(), (ImageView) mBinding.imgHead);
        mBinding.name.setText(udi.getNickname());
        mBinding.tvTitle.setText(udi.getNickname() + "的动态");
        mBinding.userLv.setText("Lv." + udi.getUser_level());
        mBinding.followTotal.setText("关注 " + udi.getFollow_total());
        mBinding.fansiTotal.setText("粉丝 " + udi.getFansi_total());


        if (udi.getIs_follow() == 0) {
            mBinding.isFollow.setText("+ 关注");
            mBinding.isFollow.setTextColor(Color.parseColor("#ffffff"));
            mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
        } else if (udi.getIs_follow() == 1) {
            mBinding.isFollow.setText("√ 已关注");
            mBinding.isFollow.setTextColor(Color.parseColor("#444444"));
            mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
        } else {
            mBinding.isFollow.setText("= 互相关注");
            mBinding.isFollow.setTextColor(Color.parseColor("#444444"));
            mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
        }
        mBinding.isFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (udi.getIs_follow() == 0) {
                    udi.setIs_follow(1);
                    mBinding.isFollow.setText("√ 已关注");
                    mBinding.isFollow.setTextColor(Color.parseColor("#444444"));
                    mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
                    subfollow(1, udi.getUid() + "");
                } else if (udi.getIs_follow() == 1) {
                    udi.setIs_follow(0);
                    mBinding.isFollow.setText("+ 关注");
                    mBinding.isFollow.setTextColor(Color.parseColor("#ffffff"));
                    mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
                    subfollow(2, udi.getUid() + "");
                } else {
                    udi.setIs_follow(0);
                    mBinding.isFollow.setText("+ 关注");
                    mBinding.isFollow.setTextColor(Color.parseColor("#ffffff"));
                    mBinding.isFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_rectangle));
                    subfollow(2, udi.getUid() + "");
                }
            }
        });
    }


    //关注or取消
    private void subfollow(int type, String follow_uid) {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + follow_uid + type + time + NewUrl.KEY);

        Map<String, Object> values = new HashMap<>();
        values.put("uid", userData.uid);
        values.put("sign", sign);
        values.put("time", time);
        values.put("type", type);
        values.put("follow_uid", follow_uid);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.SUBFOLLOW)
                .content(json)
                .build()
                .execute(new Callback<String>() {

                    @Override
                    public String parseNetworkResponse(Response response) throws IOException {
                        return response.body().string();
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("FFFFFFFF", response);
                    }

                });

    }

    public class UserDetail implements Serializable {
        private int code;
        private Data data;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public class Data implements Serializable {
            private Info info;

            public void setInfo(Info info) {
                this.info = info;
            }

            public Info getInfo() {
                return info;
            }
        }

        public class Info implements Serializable {
            private long uid;
            private String username;
            private String nickname;
            private String avatarstr;
            private int follow_total;
            private int fansi_total;
            private String posts_total;
            private int user_level;
            private int auth_level;
            private String auth_title;
            private int is_follow;

            public long getUid() {
                return uid;
            }

            public void setUid(long uid) {
                this.uid = uid;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
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

            public int getFollow_total() {
                return follow_total;
            }

            public void setFollow_total(int follow_total) {
                this.follow_total = follow_total;
            }

            public int getFansi_total() {
                return fansi_total;
            }

            public void setFansi_total(int fansi_total) {
                this.fansi_total = fansi_total;
            }

            public String getPosts_total() {
                return posts_total;
            }

            public void setPosts_total(String posts_total) {
                this.posts_total = posts_total;
            }

            public int getUser_level() {
                return user_level;
            }

            public void setUser_level(int user_level) {
                this.user_level = user_level;
            }

            public int getAuth_level() {
                return auth_level;
            }

            public void setAuth_level(int auth_level) {
                this.auth_level = auth_level;
            }

            public String getAuth_title() {
                return auth_title;
            }

            public void setAuth_title(String auth_title) {
                this.auth_title = auth_title;
            }

            public int getIs_follow() {
                return is_follow;
            }

            public void setIs_follow(int is_follow) {
                this.is_follow = is_follow;
            }
        }
    }

    private void initDynamic(UserDynamicRoot ud) {
        if (MyApplication.getUserData().loginStatue) {//已经登录
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mBinding.recyclerview.setLayoutManager(layoutManager);
            mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
            mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

            mAdapter = new CommonRecyclerAdapter<UserDynamicRoot.UserDynamic>(mContext, R.layout.adapter_user_detail_item) {
                @Override
                public void bindData(RecyclerViewHolder holder, final int position, final UserDynamicRoot.UserDynamic item) {
                    holder.setText(R.id.flow, item.getOrder_num() + "");
                    holder.setText(R.id.title, item.getTitle());
                    holder.setText(R.id.content, item.getContent());
                    holder.setText(R.id.good_number, item.getTotal_ct() + "");
                    @SuppressLint("SimpleDateFormat")
                    String date = new SimpleDateFormat("HH:mm").format(item.getPubdate_at());
                    holder.setText(R.id.time, date);
                    holder.setText(R.id.good_number, item.getGoodcount() + "");
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
                    }, 1000);
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
                    }, 1000);
                }
            });
            if (page == 1) {
                mAdapter.clearAndAddList(ud.getData().getList());
            } else {
                mAdapter.appendList(ud.getData().getList());
            }
        }
    }

    int page = 1;
    int pageSize = 20;

    private void getUserDynamic() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + uid + pageSize + page + time + NewUrl.KEY);

        Map<String, Object> values = new HashMap<>();
        values.put("uid", userData.uid);
        values.put("sign", sign);
        values.put("time", time);
        values.put("pagesize", pageSize);
        values.put("page", page);
        values.put("user_uid", uid);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.MY_USER_DUNAMIC)
                .content(json)
                .build()
                .execute(new Callback<UserDynamicRoot>() {

                    @Override
                    public UserDynamicRoot parseNetworkResponse(Response response) throws IOException {
                        String string = response.body().string();
                        return new Gson().fromJson(string, UserDynamicRoot.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message", e.getMessage());
                    }

                    @Override
                    public void onResponse(UserDynamicRoot response) {
                        if (response.getCode() == 1) {
                            initDynamic(response);
                        } else {
                            ToastUtil.showToast(UserDetailActivity.this, response.getMsg());
                        }
                    }

                });

    }


    public class UserDynamicRoot {
        private int code;
        private UserDynamicRoot.UserDynamicList data;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public UserDynamicRoot.UserDynamicList getData() {
            return data;
        }

        public void setData(UserDynamicRoot.UserDynamicList data) {
            this.data = data;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        class UserDynamicList {
            private List<UserDynamicRoot.UserDynamic> list;

            public List<UserDynamicRoot.UserDynamic> getList() {
                return list;
            }

            public void setList(List<UserDynamicRoot.UserDynamic> list) {
                this.list = list;
            }
        }

        class UserDynamic {
            private long id;
            private int position;
            private int goodcount;
            private int badcount;
            private long created_at;
            private String content;
            private String view_status;
            private long aid;
            private String title;
            private String arcurl;
            private int total_ct;
            private int showtype;
            private int isshowapp;
            private String time;
            private int has_reply;
            private int order_num;
            private long pubdate_at;
            private String webviewurl;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public int getGoodcount() {
                return goodcount;
            }

            public void setGoodcount(int goodcount) {
                this.goodcount = goodcount;
            }

            public int getBadcount() {
                return badcount;
            }

            public void setBadcount(int badcount) {
                this.badcount = badcount;
            }

            public long getCreated_at() {
                return created_at;
            }

            public void setCreated_at(long created_at) {
                this.created_at = created_at;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getView_status() {
                return view_status;
            }

            public void setView_status(String view_status) {
                this.view_status = view_status;
            }

            public long getAid() {
                return aid;
            }

            public void setAid(long aid) {
                this.aid = aid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getArcurl() {
                return arcurl;
            }

            public void setArcurl(String arcurl) {
                this.arcurl = arcurl;
            }

            public int getTotal_ct() {
                return total_ct;
            }

            public void setTotal_ct(int total_ct) {
                this.total_ct = total_ct;
            }

            public int getShowtype() {
                return showtype;
            }

            public void setShowtype(int showtype) {
                this.showtype = showtype;
            }

            public int getIsshowapp() {
                return isshowapp;
            }

            public void setIsshowapp(int isshowapp) {
                this.isshowapp = isshowapp;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getHas_reply() {
                return has_reply;
            }

            public void setHas_reply(int has_reply) {
                this.has_reply = has_reply;
            }

            public int getOrder_num() {
                return order_num;
            }

            public void setOrder_num(int order_num) {
                this.order_num = order_num;
            }

            public long getPubdate_at() {
                return pubdate_at;
            }

            public void setPubdate_at(long pubdate_at) {
                this.pubdate_at = pubdate_at;
            }

            public String getWebviewurl() {
                return webviewurl;
            }

            public void setWebviewurl(String webviewurl) {
                this.webviewurl = webviewurl;
            }
        }
    }
}
