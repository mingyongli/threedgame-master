package com.ws3dm.app.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Describution : 特别推荐activity
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2018/5/5 11:13
 **/
public class RecommendActivity extends BaseActivity implements View.OnClickListener {
    private CommonRecyclerAdapter<Notice.SSS.Not> mAdapter;
    private LinearLayout imgReturn;
    private TextView mTitle;
    private XRecyclerView recyclerview;
    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();
    private Toolbar toolbar;


    @Override
    protected void init() {
        setContentView(R.layout.ac_base_recyclerview);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTitle = (TextView) findViewById(R.id.base_title);
        recyclerview = (XRecyclerView) findViewById(R.id.recyclerview);
        myNotice();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static String getDateStringByTimeSTamp(Long timeStamp, String pattern) {
        String result = null;
        Date date = new Date(timeStamp * 1000);
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        result = sd.format(date);
        return result;
    }

    public void initView(Notice.SSS nn) {
        mTitle.setText("我的通知");

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        recyclerview.setPullRefreshEnabled(false);

        mAdapter = new CommonRecyclerAdapter<Notice.SSS.Not>(mContext, R.layout.adapter_news_one_right_pics) {
            @Override
            public void bindData(RecyclerViewHolder holder, final int position, final Notice.SSS.Not item) {
                String dateStringByTimeSTamp = getDateStringByTimeSTamp(item.getPubdate_at(), "yyyy-MM-dd");
                if (IsToday(dateStringByTimeSTamp)) {
                    holder.setText(R.id.time, "今天");
                } else if (IsYesterday(dateStringByTimeSTamp)) {
                    holder.setText(R.id.time, "昨天");
                } else {
                    holder.setText(R.id.time, dateStringByTimeSTamp);
                }

                holder.setText(R.id.content, item.getContent() + "");
            }
        };
        recyclerview.setAdapter(mAdapter);
        mAdapter.appendList(nn.getList());
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsYesterday(String day) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = getDateFormat().parse(day);
        } catch (ParseException e) {
        }
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(String day) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = getDateFormat().parse(day);
        } catch (ParseException e) {
        }
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgReturn:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    int page = 1;
    int pageSize = 20;

    private void myNotice() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + pageSize + page + time + NewUrl.KEY);

        Map<String, Object> values = new HashMap<>();
        values.put("uid", userData.uid);
        values.put("sign", sign);
        values.put("time", time);
        values.put("pagesize", pageSize);
        values.put("page", page);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.MY_NOTICE)
                .content(json)
                .build()
                .execute(new com.zhy.http.okhttp.callback.Callback<Notice>() {

                    @Override
                    public Notice parseNetworkResponse(com.squareup.okhttp.Response response) throws IOException {
                        String string = response.body().string();
                        return new Gson().fromJson(string, Notice.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message", e.getMessage());
                    }

                    @Override
                    public void onResponse(Notice response) {
                        if (response.getCode() == 1) {
                            initView(response.getData());
                        } else {
                            ToastUtil.showToast(RecommendActivity.this, response.getMsg());
                        }
                    }

                });

    }


    public class Notice {
        private int code;
        private SSS data;
        private String msg;

        public class SSS {

            private List<Not> list;

            public List<Not> getList() {
                return list;
            }

            public void setList(List<Not> list) {
                this.list = list;
            }


            public class Not {

                private String id;
                private String content;
                private long pubdate_at;

                public void setId(String id) {
                    this.id = id;
                }

                public String getId() {
                    return id;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getContent() {
                    return content;
                }

                public void setPubdate_at(long pubdate_at) {
                    this.pubdate_at = pubdate_at;
                }

                public long getPubdate_at() {
                    return pubdate_at;
                }

            }
        }


        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public SSS getData() {
            return data;
        }

        public void setData(SSS data) {
            this.data = data;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }


    }
}
