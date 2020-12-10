package com.ws3dm.app.activity;

import android.graphics.Color;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
import com.ws3dm.app.databinding.DailyTaskLayoutBinding;
import com.ws3dm.app.util.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyTaskActivity extends BaseActivity{
    private DailyTaskLayoutBinding mBinding;
    private CommonRecyclerAdapter<MyTask.Info> mAdapter;
    @Override
    protected void init() {
        mBinding = bindView(R.layout.daily_task_layout);
        mBinding.setHandler(this);
        getMyTask();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void initView(MyTask.Data md){
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerview.setLayoutManager(layoutManager);
        mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
        mAdapter = new CommonRecyclerAdapter<MyTask.Info>(mContext, R.layout.adapter_daily_item) {
            @Override
            public void bindData(RecyclerViewHolder holder, final int position, final MyTask.Info item) {
                final TextView lingqu = holder.getView(R.id.lingqu);
                holder.setText(R.id.name,item.getTips());
                holder.setText(R.id.rewards,item.getRewards()+"经验");
                holder.setText(R.id.number,item.getMy_total()+"/"+item.getNeed_total());
                ProgressBar pb = holder.getView(R.id.bar);
                pb.setMax(item.getNeed_total());
                pb.setProgress(item.getMy_total());
                if(item.getIs_available() == 0){
                    lingqu.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray));
                    lingqu.setTextColor(Color.parseColor("#333333"));
                    lingqu.setText("领取");
                }else if(item.getIs_available() == 1){
                    lingqu.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_chengse));
                    lingqu.setTextColor(Color.parseColor("#ffffff"));
                    lingqu.setText("领取");
                }else{
                    lingqu.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_bg));
                    lingqu.setTextColor(Color.parseColor("#333333"));
                    lingqu.setText("已领取");
                }
                lingqu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(item.getMy_total()==item.getNeed_total()){
                            gettaskgrowth(item.getType());
                            lingqu.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_bg));
                            lingqu.setTextColor(Color.parseColor("#333333"));
                            lingqu.setText("已领取");
                        }
                    }
                });
            }
        };
        mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getMyTask();
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        mBinding.recyclerview.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {

            }
        });
        mBinding.recyclerview.setAdapter(mAdapter);
        mAdapter.clearAndAddList(md.getInfo());
    }


    //获取用户中心数据
    private void getMyTask(){
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);

        Map<String, Object> values = new HashMap<>();
        values.put("uid",userData.uid);
        values.put("sign",sign);
        values.put("time",time);
        String json = new JSONObject(values).toString();

        OkHttpUtils
                .postString()
                .url(NewUrl.MY_TASK)
                .content(json)
                .build()
                .execute(new Callback<MyTask>() {

                    @Override
                    public MyTask parseNetworkResponse(Response response) throws IOException {
                        String string = response.body().string();
                        return new Gson().fromJson(string, MyTask.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message",e.getMessage().toString());
                    }

                    @Override
                    public void onResponse(MyTask response) {
                        if(response.getCode()==1){
                            initView(response.getData());
                        }
                    }
                });
    }

    private void gettaskgrowth(int type){
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid +type + time + NewUrl.KEY);

        Map<String, Object> values = new HashMap<>();
        values.put("uid",userData.uid);
        values.put("sign",sign);
        values.put("time",time);
        values.put("type",type);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.MY_TASK_GROWTH)
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

    class MyTask {
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

        class Data {
            private List<Info> info;
            public void setInfo(List<Info> info) {
                this.info = info;
            }
            public List<Info> getInfo() {
                return info;
            }
        }

        class Info{
            private String tips;
            private int rewards;
            private int need_total;
            private int my_total;
            private int is_available;
            private int type;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getTips() {
                return tips;
            }

            public void setTips(String tips) {
                this.tips = tips;
            }

            public int getRewards() {
                return rewards;
            }

            public void setRewards(int rewards) {
                this.rewards = rewards;
            }

            public int getNeed_total() {
                return need_total;
            }

            public void setNeed_total(int need_total) {
                this.need_total = need_total;
            }

            public int getMy_total() {
                return my_total;
            }

            public void setMy_total(int my_total) {
                this.my_total = my_total;
            }

            public int getIs_available() {
                return is_available;
            }

            public void setIs_available(int is_available) {
                this.is_available = is_available;
            }
        }
    }
}
