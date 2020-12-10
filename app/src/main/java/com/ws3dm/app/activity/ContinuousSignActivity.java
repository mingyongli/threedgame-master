package com.ws3dm.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.databinding.ContinueSignLayoutBinding;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
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


public class ContinuousSignActivity extends BaseActivity {
    private ContinueSignLayoutBinding mBinding;

    @Override
    protected void init() {
        mBinding = bindView(R.layout.continue_sign_layout);
        mBinding.setHandler(this);
        getContinueSige();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    public static String getDateStringByTimeSTamp(Long timeStamp, String pattern) {
        String result = null;
        Date date = new Date(timeStamp * 1000);
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        result = sd.format(date);
        return result;

    }

    @SuppressLint("SetTextI18n")
    private void initView(ContinueSign.Data data) {
        ContinueSign.Data.Info info = data.getInfo();
        mBinding.day.setText(info.getToday_day());
        mBinding.week.setText(info.getToday_week());
        mBinding.signDay.setText(info.getSign_total() + "");
        mBinding.lianxuSign.setText("目前连续签到" + info.getContin_sign_total() + "天");
        mBinding.number.setText(info.getWeek_sign_total() + "/7");
        mBinding.bar.setMax(7);
        mBinding.bar.setProgress(info.getWeek_sign_total());
        mBinding.userLv.setText("Lv." + info.getUser_level());
        mBinding.jingyan.setText(info.getNext_level_need() + "经验");
        mBinding.monthYear.setText(info.getNongli_month() + " " + info.getNongli_year() + "年"
                + "【" + info.getNongli_shengxiao() + "年】");
        mBinding.time.setText(TimeUtil.getCurrentYear() + "-" + TimeUtil.getCurrentMonth() + "-" + info.getToday_day());


        final ContinueSign.Data.Todayhistory cdt = data.getTodayhistory();
        mBinding.hist.setText(cdt.getTitle());
        GlideUtil.loadImage(mContext, cdt.getLitpic(), mBinding.histImg);
        mBinding.hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContinueSign.Data.Todayhistory db = cdt;
                NewsBean nb = new NewsBean();
                nb.setAid((int) db.getAid());
                nb.setArcurl(db.getArcurl());
                nb.setBt_title(db.getTitle());
                nb.setWebviewurl(db.getWebviewurl());

                Intent intent = new Intent(mContext, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsBean", nb);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        final List<ContinueSign.Data.Todayhot> todayhot = data.getTodayhot();
        mBinding.hot1.setText("·    " + todayhot.get(0).getTitle());
        mBinding.hot2.setText("·    " + todayhot.get(1).getTitle());
        mBinding.hot3.setText("·    " + todayhot.get(2).getTitle());

        mBinding.hot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContinueSign.Data.Todayhot db = todayhot.get(0);
                NewsBean nb = new NewsBean();
                nb.setAid((int) db.getAid());
                nb.setArcurl(db.getArcurl());
                nb.setBt_title(db.getTitle());
                nb.setWebviewurl(db.getWebviewurl());

                Intent intent = new Intent(mContext, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsBean", nb);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mBinding.hot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContinueSign.Data.Todayhot db = todayhot.get(1);
                NewsBean nb = new NewsBean();
                nb.setAid((int) db.getAid());
                nb.setArcurl(db.getArcurl());
                nb.setBt_title(db.getTitle());
                nb.setWebviewurl(db.getWebviewurl());

                Intent intent = new Intent(mContext, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsBean", nb);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mBinding.hot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContinueSign.Data.Todayhot db = todayhot.get(2);
                NewsBean nb = new NewsBean();
                nb.setAid((int) db.getAid());
                nb.setArcurl(db.getArcurl());
                nb.setBt_title(db.getTitle());
                nb.setWebviewurl(db.getWebviewurl());

                Intent intent = new Intent(mContext, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsBean", nb);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        List<ContinueSign.Data.Salegame> salegame = data.getSalegame();
        mBinding.fsDate1.setText(getDateStringByTimeSTamp(salegame.get(0).getPubdate_at(), "MM-dd"));
        GlideUtil.loadImage(mContext, salegame.get(0).getLitpic(), mBinding.fsImg1);
        mBinding.fsName1.setText(salegame.get(0).getTitle());

        mBinding.fsDate2.setText(getDateStringByTimeSTamp(salegame.get(1).getPubdate_at(), "MM-dd"));
        GlideUtil.loadImage(mContext, salegame.get(1).getLitpic(), mBinding.fsImg2);
        mBinding.fsName2.setText(salegame.get(1).getTitle());


        mBinding.fsDate3.setText(getDateStringByTimeSTamp(salegame.get(2).getPubdate_at(), "MM-dd"));
        GlideUtil.loadImage(mContext, salegame.get(2).getLitpic(), mBinding.fsImg3);
        mBinding.fsName3.setText(salegame.get(2).getTitle());

    }

    private void getContinueSige() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("uid", userData.uid);
        values.put("sign", sign);
        values.put("time", time);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.MY_SIGN)
                .content(json)
                .build()
                .execute(new Callback<ContinueSign>() {

                    @Override
                    public ContinueSign parseNetworkResponse(Response response) throws IOException {
                        String body = response.body().string();
                        return new Gson().fromJson(body, ContinueSign.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message", e.getMessage());
                    }

                    @Override
                    public void onResponse(ContinueSign response) {
                        if (response.getCode() == 1) {
                            initView(response.getData());
                        }
                    }
                });
    }


    public class ContinueSign {
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
            private Info info;
            private Todayhistory todayhistory;
            private List<Todayhot> todayhot;
            private List<Salegame> salegame;

            public void setInfo(Info info) {
                this.info = info;
            }

            public Info getInfo() {
                return info;
            }

            public void setTodayhistory(Todayhistory todayhistory) {
                this.todayhistory = todayhistory;
            }

            public Todayhistory getTodayhistory() {
                return todayhistory;
            }

            public void setTodayhot(List<Todayhot> todayhot) {
                this.todayhot = todayhot;
            }

            public List<Todayhot> getTodayhot() {
                return todayhot;
            }

            public void setSalegame(List<Salegame> salegame) {
                this.salegame = salegame;
            }

            public List<Salegame> getSalegame() {
                return salegame;
            }


            public class Info {

                private int sign_total;
                private int contin_sign_total;
                private int week_sign_total;
                private int user_level;
                private int next_level_need;
                private String today_day;
                private String today_week;
                private String nongli_month;
                private String nongli_year;
                private String nongli_shengxiao;

                public void setSign_total(int sign_total) {
                    this.sign_total = sign_total;
                }

                public int getSign_total() {
                    return sign_total;
                }

                public void setContin_sign_total(int contin_sign_total) {
                    this.contin_sign_total = contin_sign_total;
                }

                public int getContin_sign_total() {
                    return contin_sign_total;
                }

                public void setWeek_sign_total(int week_sign_total) {
                    this.week_sign_total = week_sign_total;
                }

                public int getWeek_sign_total() {
                    return week_sign_total;
                }

                public void setUser_level(int user_level) {
                    this.user_level = user_level;
                }

                public int getUser_level() {
                    return user_level;
                }

                public void setNext_level_need(int next_level_need) {
                    this.next_level_need = next_level_need;
                }

                public int getNext_level_need() {
                    return next_level_need;
                }

                public void setToday_day(String today_day) {
                    this.today_day = today_day;
                }

                public String getToday_day() {
                    return today_day;
                }

                public void setToday_week(String today_week) {
                    this.today_week = today_week;
                }

                public String getToday_week() {
                    return today_week;
                }

                public void setNongli_month(String nongli_month) {
                    this.nongli_month = nongli_month;
                }

                public String getNongli_month() {
                    return nongli_month;
                }

                public void setNongli_year(String nongli_year) {
                    this.nongli_year = nongli_year;
                }

                public String getNongli_year() {
                    return nongli_year;
                }

                public void setNongli_shengxiao(String nongli_shengxiao) {
                    this.nongli_shengxiao = nongli_shengxiao;
                }

                public String getNongli_shengxiao() {
                    return nongli_shengxiao;
                }

            }


            public class Todayhistory {

                private long aid;
                private String arcurl;
                private String title;
                private String litpic;
                private String webviewurl;
                private int showtype;

                public void setAid(long aid) {
                    this.aid = aid;
                }

                public long getAid() {
                    return aid;
                }

                public void setArcurl(String arcurl) {
                    this.arcurl = arcurl;
                }

                public String getArcurl() {
                    return arcurl;
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

                public void setWebviewurl(String webviewurl) {
                    this.webviewurl = webviewurl;
                }

                public String getWebviewurl() {
                    return webviewurl;
                }

                public void setShowtype(int showtype) {
                    this.showtype = showtype;
                }

                public int getShowtype() {
                    return showtype;
                }

            }


            public class Todayhot {

                private long aid;
                private String arcurl;
                private int showtype;
                private String title;
                private String webviewurl;

                public void setAid(long aid) {
                    this.aid = aid;
                }

                public long getAid() {
                    return aid;
                }

                public void setArcurl(String arcurl) {
                    this.arcurl = arcurl;
                }

                public String getArcurl() {
                    return arcurl;
                }

                public void setShowtype(int showtype) {
                    this.showtype = showtype;
                }

                public int getShowtype() {
                    return showtype;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getTitle() {
                    return title;
                }

                public void setWebviewurl(String webviewurl) {
                    this.webviewurl = webviewurl;
                }

                public String getWebviewurl() {
                    return webviewurl;
                }

            }


            public class Salegame {

                private long aid;
                private String arcurl;
                private String title;
                private long pubdate_at;
                private String litpic;
                private int showtype;
                private String webviewurl;

                public void setAid(long aid) {
                    this.aid = aid;
                }

                public long getAid() {
                    return aid;
                }

                public void setArcurl(String arcurl) {
                    this.arcurl = arcurl;
                }

                public String getArcurl() {
                    return arcurl;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getTitle() {
                    return title;
                }

                public void setPubdate_at(long pubdate_at) {
                    this.pubdate_at = pubdate_at;
                }

                public long getPubdate_at() {
                    return pubdate_at;
                }

                public void setLitpic(String litpic) {
                    this.litpic = litpic;
                }

                public String getLitpic() {
                    return litpic;
                }

                public void setShowtype(int showtype) {
                    this.showtype = showtype;
                }

                public int getShowtype() {
                    return showtype;
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
}
