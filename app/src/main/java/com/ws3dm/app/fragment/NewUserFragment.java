package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.CollectActivity;
import com.ws3dm.app.activity.ContinuousSignActivity;
import com.ws3dm.app.activity.DailyTaskActivity;
import com.ws3dm.app.activity.GameCardActivity;
import com.ws3dm.app.activity.HistoryActivity;
import com.ws3dm.app.activity.LevelActivity;
import com.ws3dm.app.activity.LoginActivity;
import com.ws3dm.app.activity.PSN_ID_Activity;
import com.ws3dm.app.activity.PsnCertification;
import com.ws3dm.app.activity.STEAM_ID_Activity;
import com.ws3dm.app.activity.UserAttentionActivity;
import com.ws3dm.app.activity.UserFansActivity;
import com.ws3dm.app.adapter.UserCommentsAdapter;
import com.ws3dm.app.bean.NewUserInfo;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.bean.mysteampsn.Psn;
import com.ws3dm.app.bean.mysteampsn.Steam;
import com.ws3dm.app.bean.mysteampsn.SteamPsn;
import com.ws3dm.app.databinding.FragmentTheNewUserBinding;
import com.ws3dm.app.sqlite.NewsFile;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewUserFragment extends BaseFragment {

    private FragmentTheNewUserBinding mBinding;
    private NewsFile newsFile;
    private Steam steam;
    private Psn psn;
    List<String> title = new ArrayList<>();
    private UserCommentsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_the_new_user, container, false);
        newsFile = new NewsFile(mContext);
        setLogin();
        title.add("评论");
        title.add("消息");
        initTable();
        return mBinding.getRoot();
    }

    private void setLogin() {
        if (MyApplication.getUserData().loginStatue) {//已经登录
            mBinding.userInfoLayout.setVisibility(View.VISIBLE);
            mBinding.userActionLayout.setVisibility(View.VISIBLE);
            mBinding.messageLayout.setVisibility(View.VISIBLE);
            initUserView();
            getUserInfo();
            initListener();
            getUserPsnSteamInfo();
        } else {
            setNoLogin();
            mBinding.userLv.setText("--");
        }
    }


    private void initListener() {
        mBinding.imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
        mBinding.myAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserAttentionActivity.class));
            }
        });
        mBinding.myFans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserFansActivity.class));
            }
        });
        mBinding.llCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), CollectActivity.class));
                }
            }
        });
        mBinding.record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), HistoryActivity.class));
                }
            }
        });
        mBinding.lvDj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LevelActivity.class));
            }
        });
        mBinding.lxqd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ContinuousSignActivity.class));
            }
        });
        mBinding.dailyBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DailyTaskActivity.class));
            }
        });
    }


    // 没有登录显示界面
    private void setNoLogin() {
        mBinding.userInfoLayout.setVisibility(View.GONE);
        mBinding.userActionLayout.setVisibility(View.GONE);
        mBinding.messageLayout.setVisibility(View.GONE);
        mBinding.steamPsnCard.setVisibility(View.GONE);
        mBinding.bindSteamPsnCard.setVisibility(View.GONE);
        GlideUtil.loadCircleImage(mContext, R.drawable.no_login, mBinding.imgHead);
        mBinding.userName.setText("点击头像登录");

    }

    private void initUserView() {
        UserDataBean mUserDataBean = MyApplication.getUserData();
        GlideUtil.loadCircleImage(mContext, mUserDataBean.avatarstr, mBinding.imgHead);
        if (StringUtil.isEmpty(mUserDataBean.nickname)) {
            mBinding.userName.setText(mUserDataBean.username);
        } else {
            mBinding.userName.setText(mUserDataBean.nickname);
        }

    }

    private void getUserInfo() {
        //获取用户数据
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
                .url(NewUrl.MY_HOME)
                .content(json)
                .build()
                .execute(new Callback<NewUserInfo>() {

                    @Override
                    public NewUserInfo parseNetworkResponse(Response response) throws IOException {
                        String string = response.body().string();
                        return new Gson().fromJson(string, NewUserInfo.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("message", e.getMessage());
                    }

                    @Override
                    public void onResponse(NewUserInfo response) {
                        if (response.getCode() == 1) {
                            UpdateUserView(response);
                        } else {
                            ToastUtil.showToast(mContext, response.getMsg() + response.getCode());
                        }
                    }
                });
    }

    NewUserInfo.Info info;

    private void UpdateUserView(NewUserInfo response) {
        info = response.getData().getInfo();
        List<NewsBean> newsBeans = newsFile.queryHistory();
        mBinding.recordCount.setText(newsBeans.size() + "");
        mBinding.tiezi.setText(info.getFavorite_total() + "");
        mBinding.guanzhu.setText(info.getFollow_total() + "");
        mBinding.fensi.setText(info.getFansi_total() + "");
        mBinding.userLv.setText("Lv." + this.info.getUser_level());
    }

    private void getUserPsnSteamInfo() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);
        Map<String, Object> values = new HashMap<>();
        values.put("uid", userData.uid);
        values.put("sign", sign);
        values.put("time", time);
        String json = new JSONObject(values).toString();
        Log.d("获取信息", json);
        OkHttpUtils
                .postString()
                .url(NewUrl.MY_STEAMPSN)
                .content(json)
                .build()
                .execute(new Callback<SteamPsn>() {

                    @Override
                    public SteamPsn parseNetworkResponse(Response response) throws IOException {
                        String data = response.body().string();
                        return new Gson().fromJson(data, SteamPsn.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                    }

                    @Override
                    public void onResponse(SteamPsn response) {
                        if(response.getCode() ==1){
                            updateSteamView(response);
                        }else {
                            ToastUtil.showToast(mContext, response.getMsg() + response.getCode());
                        }
                    }

                });

    }

    private void updateSteamView(SteamPsn response) {
        steam = response.getData().getSteam();
        psn = response.getData().getPsn();
        //都没绑定
        if (psn.getIsbang() == 0 && steam.getIsbang() == 0) {
            mBinding.steamPsnCard.setVisibility(View.GONE);
            mBinding.bindSteamPsnCard.setVisibility(View.VISIBLE);
            mBinding.bindSteamPsnCard.findViewById(R.id.bind_steam_psn_card).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, GameCardActivity.class));
                }
            });
            return;
        }
        //其中一个绑定了
        if (psn.getIsbang() == 1 || steam.getIsbang() == 1) {
            mBinding.steamPsnCard.setVisibility(View.VISIBLE);
            mBinding.bindSteamPsnCard.setVisibility(View.GONE);
            if (psn.getIsbang() == 1) {
                mBinding.psnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBinding.steamCard.setVisibility(View.GONE);
                        mBinding.psnCard.setVisibility(View.VISIBLE);
                    }
                });
                mBinding.psnCard.setVisibility(View.VISIBLE);
                mBinding.steamCard.setVisibility(View.GONE);
                setPsnView(psn);
            } else {
                mBinding.psnCard.setVisibility(View.GONE);
                mBinding.psnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast(getContext(), "还没有绑定Psn账号");
                    }
                });
            }

            if (steam.getIsbang() == 1) {
                mBinding.steamButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBinding.steamCard.setVisibility(View.VISIBLE);
                        mBinding.psnCard.setVisibility(View.GONE);
                    }
                });
                mBinding.steamCard.setVisibility(View.VISIBLE);
                mBinding.psnCard.setVisibility(View.GONE);
                setSteamView();
            } else {
                mBinding.steamCard.setVisibility(View.GONE);
                mBinding.steamButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast(getContext(), "还没有绑定steam账号");
                    }
                });
            }

            mBinding.jumpCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, GameCardActivity.class));
                }
            });
        }
    }

    private void setSteamView() {
        GlideUtil.loadImage(mContext, steam.getSt_avatarstr(), mBinding.steamCard.findViewById(R.id.img_steam_head));
        GlideUtil.loadImage(mContext, R.drawable.steam_value, mBinding.steamCard.findViewById(R.id.price_bg));
        GlideUtil.loadImage(mContext, R.drawable.steam_game, mBinding.steamCard.findViewById(R.id.count_bg));
        GlideUtil.loadImage(mContext, R.drawable.steam_time, mBinding.steamCard.findViewById(R.id.hour_bg));
        TextView gamevalue = mBinding.steamCard.findViewById(R.id.my_steam_game_price);
        TextView gamecount = mBinding.steamCard.findViewById(R.id.steam_game_count);
        TextView user_name = mBinding.steamCard.findViewById(R.id.steam_user_name);
        TextView time = mBinding.steamCard.findViewById(R.id.steam_time);
        time.setText(steam.getGamehour());
        user_name.setText(steam.getSt_nickname());
        gamevalue.setText(steam.getGameprice() + "");
        gamecount.setText(steam.getGamecount() + "");
        mBinding.steamCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, STEAM_ID_Activity.class));
            }
        });

    }

    private void setPsnView(Psn psn) {
        if (psn.getIsauth() == 0) {
            mBinding.psnCard.findViewById(R.id.psn_to_certi).setVisibility(View.VISIBLE);
            GlideUtil.loadImage(mContext, R.drawable.psn_uncertification, mBinding.psnCard.findViewById(R.id.psn_certification));
            mBinding.psnCard.findViewById(R.id.psn_to_certi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PsnCertification.class);
                    intent.putExtra("psnid", psn.getPsn_nickname());
                    startActivity(intent);
                }
            });
        } else if (psn.getIsauth() == 1) {
            mBinding.psnCard.findViewById(R.id.psn_to_certi).setVisibility(View.GONE);
            GlideUtil.loadImage(mContext, R.drawable.psn_certification, mBinding.psnCard.findViewById(R.id.psn_certification));
        }

        GlideUtil.loadImage(mContext, psn.getPsn_avatarstr(), mBinding.psnCard.findViewById(R.id.img_psn_head));
        TextView userName = mBinding.psnCard.findViewById(R.id.psn_user_name);
        TextView masonry = mBinding.psnCard.findViewById(R.id.masonry);
        TextView gold = mBinding.psnCard.findViewById(R.id.gold);
        TextView silver = mBinding.psnCard.findViewById(R.id.silver);
        TextView copper = mBinding.psnCard.findViewById(R.id.copper);
        TextView price = mBinding.psnCard.findViewById(R.id.game_price);
        TextView count = mBinding.psnCard.findViewById(R.id.game_count);

        price.setText(psn.getGameprice() + "");
        count.setText(psn.getGamecount() + "");
        userName.setText(psn.getPsn_nickname());
        masonry.setText(psn.getMasonry() + "");
        gold.setText(psn.getGold() + "");
        silver.setText(psn.getSilver() + "");
        copper.setText(psn.getCopper() + "");

        mBinding.psnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PSN_ID_Activity.class));
            }
        });
    }

    private void initTable() {
        mBinding.tablayout.setTabMode(TabLayout.MODE_FIXED);
        mBinding.tablayout.setupWithViewPager(mBinding.viewpager);
        adapter = new UserCommentsAdapter(getChildFragmentManager());
        mBinding.viewpager.setAdapter(adapter);
        adapter.setData(title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.user_toobar_item, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        setLogin();
        //initTable();
    }
}
