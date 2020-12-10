package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

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
import com.ws3dm.app.databinding.UserMessageLayoutBinding;
import com.ws3dm.app.util.StringUtil;
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

public class MessageActivity extends BaseActivity{
    private UserMessageLayoutBinding mBinding;
    private CommonRecyclerAdapter<MessageRoot.Message> mAdapter;
    @Override
    protected void init() {
        mBinding = bindView(R.layout.user_message_layout);
        mBinding.setHandler(this);
        getUserMessage();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }
    public static String getDateStringByTimeSTamp(Long timeStamp,String pattern){
        String result = null;
        Date date = new Date(timeStamp*1000);
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        result = sd.format(date);
        return result;
    }
    private void initDynamic(MessageRoot.Data ud){
        if (MyApplication.getUserData().loginStatue) {//已经登录
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mBinding.recyclerview.setLayoutManager(layoutManager);
            mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
            mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

            mAdapter = new CommonRecyclerAdapter<MessageRoot.Message>(mContext, R.layout.adapter_user_message_item) {
                @Override
                public void bindData(RecyclerViewHolder holder, final int position, final MessageRoot.Message item) {
                    GlideUtil.loadCircleImage(mContext, item.getUser().getAvatarstr(), holder.getView(R.id.user_img));
                    holder.setText(R.id.title, item.getUser().getNickname());
                    holder.setText(R.id.content, item.getTitle());
                    holder.setText(R.id.replay, item.getContent());
                    holder.setText(R.id.good_number, item.getTotal_ct() + "");
                    String date = getDateStringByTimeSTamp(item.getPubdate_at(), "yyyy-MM-dd HH:mm");
                    holder.setText(R.id.time, date);
                    holder.setText(R.id.good_number, item.getGoodcount() + "");

                    holder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NewsBean bean = new NewsBean();
                            bean.setAid((int) item.getAid());
                            bean.setArcurl(item.getArcurl());
                            bean.setWebviewurl(item.getWebviewurl());
                            Intent intent = new Intent(mContext, NewsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("newsBean", bean);
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
                    getUserMessage();
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
                    getUserMessage();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.recyclerview.loadMoreComplete();
                        }
                    },1000);
                }
            });
            if(page == 1){
                mAdapter.clearAndAddList(ud.getList());
            }else{
                mAdapter.appendList(ud.getList());
            }
        }
    }

    int page = 1;
    int pageSize = 20;
    private void getUserMessage(){
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + pageSize + page + time + NewUrl.KEY);

        Map<String, Object> values = new HashMap<>();
        values.put("uid",userData.uid);
        values.put("sign",sign);
        values.put("time",time);
        values.put("pagesize",pageSize);
        values.put("page",page);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.MY_MESSAGE)
                .content(json)
                .build()
                .execute(new Callback<MessageRoot>() {

                    @Override
                    public MessageRoot parseNetworkResponse(Response response) throws IOException {
                        String string = response.body().string();
                        return new Gson().fromJson(string, MessageRoot.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message",e.getMessage());
                    }

                    @Override
                    public void onResponse(MessageRoot response) {
                        if(response.getCode() == 1){
                            initDynamic(response.getData());
                        }else if(response.getCode() == 2){

                            //ToastUtil.showToast(MessageActivity.this,response.getMsg());
                        }
                    }

                });

    }



    public class MessageRoot{
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

        public class Data {
            private List<Message> list;

            public List<Message> getList() {
                return list;
            }

            public void setList(List<Message> list) {
                this.list = list;
            }
        }


        public class User {
            private String uid;
            private String nickname;
            private String avatarstr;
            public void setUid(String uid) {
                this.uid = uid;
            }
            public String getUid() {
                return uid;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
            public String getNickname() {
                return nickname;
            }

            public void setAvatarstr(String avatarstr) {
                this.avatarstr = avatarstr;
            }
            public String getAvatarstr() {
                return avatarstr;
            }

        }


        public class Message {
            private long id;
            private int goodcount;
            private String content;
            private long aid;
            private String title;
            private String arcurl;
            private int total_ct;
            private int showtype;
            private User user;
            private int order_num;
            private long pubdate_at;
            private String webviewurl;
            public void setId(long id) {
                this.id = id;
            }
            public long getId() {
                return id;
            }

            public void setGoodcount(int goodcount) {
                this.goodcount = goodcount;
            }
            public int getGoodcount() {
                return goodcount;
            }

            public void setContent(String content) {
                this.content = content;
            }
            public String getContent() {
                return content;
            }

            public void setAid(long aid) {
                this.aid = aid;
            }
            public long getAid() {
                return aid;
            }

            public void setTitle(String title) {
                this.title = title;
            }
            public String getTitle() {
                return title;
            }

            public void setArcurl(String arcurl) {
                this.arcurl = arcurl;
            }
            public String getArcurl() {
                return arcurl;
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

            public void setUser(User user) {
                this.user = user;
            }
            public User getUser() {
                return user;
            }

            public void setOrder_num(int order_num) {
                this.order_num = order_num;
            }
            public int getOrder_num() {
                return order_num;
            }

            public void setPubdate_at(long pubdate_at) {
                this.pubdate_at = pubdate_at;
            }
            public long getPubdate_at() {
                return pubdate_at;
            }

            public void setWebviewurl(String webviewurl) {
                this.webviewurl = webviewurl;
            }
            public String getWebviewurl() {
                return webviewurl;
            }

        }
    }
}
