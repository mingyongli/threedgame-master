//package com.ws3dm.app.fragment;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.databinding.DataBindingUtil;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.RequiresApi;
//import android.support.v7.widget.LinearLayoutManager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
//import com.jcodecraeer.xrecyclerview.ProgressStyle;
//import com.jcodecraeer.xrecyclerview.XRecyclerView;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;
//import com.ws3dm.app.MyApplication;
//import com.ws3dm.app.NewUrl;
//import com.ws3dm.app.R;
//import com.ws3dm.app.activity.CollectActivity;
//import com.ws3dm.app.activity.ContinuousSignActivity;
//import com.ws3dm.app.activity.DailyTaskActivity;
//import com.ws3dm.app.activity.GameCardActivity;
//import com.ws3dm.app.activity.HistoryActivity;
//import com.ws3dm.app.activity.LevelActivity;
//import com.ws3dm.app.activity.LoginActivity;
//import com.ws3dm.app.activity.MessageActivity;
//import com.ws3dm.app.activity.NewsActivity;
//import com.ws3dm.app.activity.PSN_ID_Activity;
//import com.ws3dm.app.activity.PsnCertification;
//import com.ws3dm.app.activity.STEAM_ID_Activity;
//import com.ws3dm.app.activity.UserAttentionActivity;
//import com.ws3dm.app.activity.UserFansActivity;
//import com.ws3dm.app.adapter.CommonRecyclerAdapter;
//import com.ws3dm.app.adapter.RecyclerViewHolder;
//import com.ws3dm.app.bean.NewUserInfo;
//import com.ws3dm.app.bean.News;
//import com.ws3dm.app.bean.NewsBean;
//import com.ws3dm.app.bean.UserDataBean;
//import com.ws3dm.app.bean.mysteampsn.Psn;
//import com.ws3dm.app.bean.mysteampsn.Steam;
//import com.ws3dm.app.bean.mysteampsn.SteamPsn;
//import com.ws3dm.app.databinding.FragmentUserBinding;
//import com.ws3dm.app.sqlite.NewsFile;
//import com.ws3dm.app.util.StringUtil;
//import com.ws3dm.app.util.ToastUtil;
//import com.ws3dm.app.util.glide.GlideUtil;
//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.Callback;
//
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Describution :用户中心
// * Author : mingyong
// * Date : 2020/6/16
// **/
//@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//public class UserFragment extends BaseFragment {
//    private CommonRecyclerAdapter<UserDynamicRoot.UserDynamic> mAdapter;
//    private FragmentUserBinding mBinding;
//    private View header;
//    private CommonRecyclerAdapter<MessageActivity.MessageRoot.Message> messageAdapter;
//    private NewsFile newsFile;
//    //用来判断消息和评论哪个被点击了,然后判断相应的加载更多和刷新
//    private boolean checkedcomments = true;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
//        mBinding.setHandler(this);
//        setLogin();
//        newsFile = new NewsFile(mContext);
//        addHeader(container);
//        getSteampsn();
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mBinding.recyclerview.setLayoutManager(layoutManager);
//        mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
//        mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
////		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);       //原来就注释掉的
//        mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
//        return mBinding.getRoot();
//    }
//
//    public void onClick(View view) {
//        switch (view.getId()) {
////            case R.id.tv_push_list:
////                getActivity().startActivity(new Intent(getActivity(), RecommendActivity.class));
////                break;
//            case R.id.img_head://头像点击，登录
//                if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
////                break;
////            case R.id.but_setting://设置
////                startActivity(new Intent(getActivity(), SettingActivity.class));
////                break;
////            case R.id.update_user://修改资料
////                if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
////                    startActivity(new Intent(getActivity(), LoginActivity.class));
////                } else if (MyApplication.getUserData().mobile.length() == 0) {//第三方登陆，非绑定跳转绑定界面
////                    Intent intent = new Intent(mContext, ForgetPassActivity.class);
////                    intent.putExtra("type", 1);//1,绑定手机号，2修改密码，3忘记密码
////                    startActivity(intent);
////                } else {
////                    Intent intent = new Intent(getActivity(), SetupActivity.class);
////                    intent.putExtra("lv", info.getUser_level() + "");
////                    getActivity().startActivity(intent);
////                }
//                break;
//        }
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (this != null && !hidden) {
//            setLogin();
//        }
//    }
//
//    // 用户信息
//    @SuppressLint("SetTextI18n")
//    private void setUser() {
//        UserDataBean mUserDataBean = MyApplication.getUserData();
//        GlideUtil.loadCircleImage(mContext, mUserDataBean.avatarstr, mBinding.imgHead);
//        if (StringUtil.isEmpty(mUserDataBean.nickname)) {
//            mBinding.userName.setText(mUserDataBean.username);
//        } else {
//            mBinding.userName.setText(mUserDataBean.nickname);
//        }
//    }
//
//    // 没有登录显示界面
//    private void setNoLogin() {
//        GlideUtil.loadCircleImage(mContext, R.drawable.no_login, mBinding.imgHead);
//        mBinding.userName.setText("点击头像登录");
//    }
//
//    // 用户登录
//    private void setLogin() {
//        if (MyApplication.getUserData().loginStatue) {//已经登录
//            setUser();
//            getUserData();
//            getUserDynamic();
//            getSteampsn();
//            mBinding.recyclerview.setVisibility(View.VISIBLE);
//        } else {//未登录
//            setNoLogin();
//            mBinding.userLv.setText("--");
//            mBinding.recyclerview.setVisibility(View.GONE);
//        }
//    }
//
//
//    public static String getDateStringByTimeSTamp(Long timeStamp, String pattern) {
//        String result = null;
//        Date date = new Date(timeStamp * 1000);
//        SimpleDateFormat sd = new SimpleDateFormat(pattern);
//        result = sd.format(date);
//        return result;
//    }
//
//    //初始化我的动态（已登录）
//    private void initDynamic(UserDynamicRoot ud) {
//        if (MyApplication.getUserData().loginStatue) {//已经登录
//
//            mAdapter = new CommonRecyclerAdapter<UserDynamicRoot.UserDynamic>(mContext, R.layout.adapter_dynamic) {
//                @Override
//                public void bindData(RecyclerViewHolder holder, final int position, final UserDynamicRoot.UserDynamic item) {
//                    View view = holder.getView(R.id.user_img);
//                    View item_content = holder.getView(R.id.item_content);
//                    View content = holder.getView(R.id.content);
//                    if (position == 0)
//                        item_content.setBackgroundResource(R.drawable.shape_user_recomm);
//                    content.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            UserDynamicRoot.UserDynamic db = mAdapter.getDataByPosition(position);
//                            NewsBean nb = new NewsBean();
//                            nb.setAid((int) db.getAid());
//                            nb.setArcurl(db.getArcurl());
//                            nb.setBt_title(db.getTitle());
//                            nb.setId((int) db.getId());
//                            nb.setWebviewurl(db.getWebviewurl());
//
//
//                            Intent intent = new Intent(mContext, NewsActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("newsBean", nb);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                        }
//                    });
//                    GlideUtil.loadCircleImage(mContext, MyApplication.getUserData().avatarstr, (ImageView) view);
//                    holder.setText(R.id.name, MyApplication.getUserData().nickname);
//                    holder.setText(R.id.title, item.getContent());
//                    holder.setText(R.id.content, item.getTitle());
//                    String date = getDateStringByTimeSTamp(item.getPubdate_at(), "yyyy-MM-dd HH:mm");
//                    holder.setText(R.id.time, date);
//                    holder.setText(R.id.good_number, item.getGoodcount() + "");
//                }
//            };
//            mBinding.recyclerview.setAdapter(mAdapter);
//            mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
//                @Override
//                public void onRefresh() {
//                    if (checkedcomments) {
//                        commentsPage = 1;
//                        getUserDynamic();
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mBinding.recyclerview.refreshComplete();
//                            }
//                        }, 1000);
//                    } else {
//                        messagePage = 1;
//                        getUserMessage();
//                    }
//
//                }
//
//                @Override
//                public void onLoadMore() {
//                    if (checkedcomments) {
//                        commentsPage++;
//                        getUserDynamic();
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mBinding.recyclerview.loadMoreComplete();
//                            }
//                        }, 1000);
//                    } else {
//                        messagePage++;
//                        getUserMessage();
//                    }
//                }
//            });
//
//
//            if (page == 1) {
//                mAdapter.clearAndAddList(ud.getData().getList());
//            } else {
//                mAdapter.appendList(ud.getData().getList());
//            }
//        }
//    }
//
//    int page = 1;
//    int messagePage = 1;
//    int commentsPage = 1;
//    int pageSize = 20;
//
//    private void getUserDynamic() {
//        UserDataBean userData = MyApplication.getUserData();
//        long time = System.currentTimeMillis();
//        String sign = StringUtil.newMD5(userData.uid + pageSize + commentsPage + time + NewUrl.KEY);
//
//        Map<String, Object> values = new HashMap<>();
//        values.put("uid", userData.uid);
//        values.put("sign", sign);
//        values.put("time", time);
//        values.put("pagesize", pageSize);
//        values.put("page", page);
//        String json = new JSONObject(values).toString();
//        OkHttpUtils
//                .postString()
//                .url(NewUrl.MY_DUNAMIC)
//                .content(json)
//                .build()
//                .execute(new Callback<UserDynamicRoot>() {
//
//                    @Override
//                    public UserDynamicRoot parseNetworkResponse(Response response) throws IOException {
//                        String string = response.body().string();
//                        return new Gson().fromJson(string, UserDynamicRoot.class);
//                    }
//
//                    @Override
//                    public void onError(Request request, Exception e) {
//                        Log.e("message", e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(UserDynamicRoot response) {
//                        if (response.getCode() == 1) {
//                            initDynamic(response);
//                        } else if (response.getCode() == -2) {
//
//                            //ToastUtil.showToast(UserFragment.this.getActivity(),response.getMsg());
//                        }
//                    }
//
//                });
//
//    }
//
//    private void getUserMessage() {
//        UserDataBean userData = MyApplication.getUserData();
//        long time = System.currentTimeMillis();
//        String sign = StringUtil.newMD5(userData.uid + pageSize + messagePage + time + NewUrl.KEY);
//
//        Map<String, Object> values = new HashMap<>();
//        values.put("uid", userData.uid);
//        values.put("sign", sign);
//        values.put("time", time);
//        values.put("pagesize", pageSize);
//        values.put("page", page);
//        String json = new JSONObject(values).toString();
//        OkHttpUtils
//                .postString()
//                .url(NewUrl.MY_MESSAGE)
//                .content(json)
//                .build()
//                .execute(new Callback<MessageActivity.MessageRoot>() {
//
//                    @Override
//                    public MessageActivity.MessageRoot parseNetworkResponse(Response response) throws IOException {
//                        String string = response.body().string();
//                        return new Gson().fromJson(string, MessageActivity.MessageRoot.class);
//                    }
//
//                    @Override
//                    public void onError(Request request, Exception e) {
//                        Log.e("message", e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(MessageActivity.MessageRoot response) {
//                        if (response.getCode() == 1) {
//                            showUserMessage(response.getData());
//                        } else {
//                            if (response.getCode() == -2) {
//                                messagePage--;
//                                showUserMessage(response.getData());
//                            }
//                        }
//                        mBinding.recyclerview.refreshComplete();
//                        mBinding.recyclerview.loadMoreComplete();
//                    }
//
//                });
//
//    }
//
//    private void showUserMessage(MessageActivity.MessageRoot.Data data) {
//        if (MyApplication.getUserData().loginStatue) {//已经登录
//            messageAdapter = new CommonRecyclerAdapter<MessageActivity.MessageRoot.Message>(mContext, R.layout.adapter_user_message_item) {
//                @Override
//                public void bindData(RecyclerViewHolder holder, final int position, final MessageActivity.MessageRoot.Message item) {
//                    holder.setText(R.id.flow, item.getOrder_num() + "");
//                    holder.setText(R.id.title, item.getUser().getNickname());
//                    holder.setText(R.id.content, item.getTitle());
//                    holder.setText(R.id.replay, item.getContent());
//                    holder.setText(R.id.good_number, item.getTotal_ct() + "");
//                    String date = getDateStringByTimeSTamp(item.getPubdate_at(), "yyyy-MM-dd HH:mm");
//                    holder.setText(R.id.time, date);
//                    holder.setText(R.id.good_number, item.getGoodcount() + "");
//                }
//            };
//            mBinding.recyclerview.setAdapter(messageAdapter);
//            if (data.getList().size() == 0) {
//                messageAdapter.cleaList();
//            } else {
//                messageAdapter.appendList(data.getList());
//            }
//        }
//    }
//
//    View lvJf, daily_but, lxqd, bind_steam_psn_card;
//    TextView guanzhu, fensi, tiezi, comments, message, record_count;
//    ImageView psn_but, steam_but, jump_rank;
//    View psn_card, steam_card;
//
//    private void addHeader(ViewGroup container) {
//        header = LayoutInflater.from(mContext).inflate(R.layout.dynamic_headr, container, false);
//        mBinding.recyclerview.addHeaderView(header);
//        psn_card = header.findViewById(R.id.user_psn_card);
//        steam_card = header.findViewById(R.id.user_steam_card);
//        lvJf = header.findViewById(R.id.lv_dj);
//        record_count = header.findViewById(R.id.record_count);
//        daily_but = header.findViewById(R.id.daily_but);
//        lxqd = header.findViewById(R.id.lxqd);
//        psn_but = header.findViewById(R.id.user_psn_button);
//        steam_but = header.findViewById(R.id.user_steam_button);
//        guanzhu = header.findViewById(R.id.guanzhu);
//        fensi = header.findViewById(R.id.fensi);
//        tiezi = header.findViewById(R.id.tiezi);
//        bind_steam_psn_card = header.findViewById(R.id.bind_steam_psn_card);
//        jump_rank = header.findViewById(R.id.jump_rank);
//        comments = header.findViewById(R.id.comments);
//        message = header.findViewById(R.id.message);
//        //先默认隐藏掉psn的card
//        psn_card.setVisibility(View.GONE);
//
//
//        lvJf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent point = new Intent(mContext, LevelActivity.class);
//                startActivity(point);
//            }
//        });
//        daily_but.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(mContext, DailyTaskActivity.class));
//            }
//        });
//        lxqd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(mContext, ContinuousSignActivity.class));
//            }
//        });
//        header.findViewById(R.id.my_attention).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), UserAttentionActivity.class));
//            }
//        });
//
//        header.findViewById(R.id.my_fans).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), UserFansActivity.class));
//
//            }
//        });
//
//        header.findViewById(R.id.ll_collect).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                } else {
//                    getActivity().startActivity(new Intent(getActivity(), CollectActivity.class));
//                }
//            }
//        });
//
//        header.findViewById(R.id.record).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (MyApplication.getUserData() == null || !MyApplication.getUserData().loginStatue) {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                } else {
//                    startActivity(new Intent(getActivity(), HistoryActivity.class));
//                }
//            }
//        });
//
//
//        bind_steam_psn_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), GameCardActivity.class));
//            }
//        });
//
//        steam_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mContext, STEAM_ID_Activity.class));
//            }
//        });
//        psn_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mContext, PSN_ID_Activity.class));
//            }
//        });
//        jump_rank.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, GameCardActivity.class);
//                startActivity(intent);
//            }
//        });
//        message.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                checkedcomments = false;
//                messageText = true;
//                switchTextColor();
//                getUserMessage();
//            }
//        });
//        comments.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                checkedcomments = true;
//                page = 1;
//                messageText = false;
//                switchTextColor();
//                getUserDynamic();
//            }
//        });
//    }
//
//    private boolean messageText = false;
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @SuppressLint("ResourceAsColor")
//    private void switchTextColor() {
//        if (messageText == true) {
//            message.setTextColor(mContext.getColor(R.color.black));
//            comments.setTextColor(mContext.getColor(R.color.steam_message_text));
//
//        } else {
//            message.setTextColor(mContext.getColor(R.color.steam_message_text));
//            comments.setTextColor(mContext.getColor(R.color.black));
//
//        }
//    }
//
//    private void getSteampsn() {
//        UserDataBean userData = MyApplication.getUserData();
//        long time = System.currentTimeMillis();
//        String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);
//        Map<String, Object> values = new HashMap<>();
//        values.put("uid", userData.uid);
//        values.put("sign", sign);
//        values.put("time", time);
//        String json = new JSONObject(values).toString();
//        Log.d("获取信息", json);
//        OkHttpUtils
//                .postString()
//                .url(NewUrl.MY_STEAMPSN)
//                .content(json)
//                .build()
//                .execute(new Callback<SteamPsn>() {
//
//                    @Override
//                    public SteamPsn parseNetworkResponse(Response response) throws IOException {
//                        String data = response.body().string();
//                        Log.e("xxxx", data);
//                        return new Gson().fromJson(data, SteamPsn.class);
//                    }
//
//                    @Override
//                    public void onError(Request request, Exception e) {
//                        Log.e("message", e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(SteamPsn response) {
//                        updateSteamView(response);
//                    }
//
//                });
//    }
//
//    private void updateSteamView(SteamPsn sp) {
//        Psn psn = sp.getData().getPsn();
//        Steam steam = sp.getData().getSteam();
//        //都没绑定
//        if (psn.getIsbang() == 0 && steam.getIsbang() == 0) {
//            header.findViewById(R.id.steam_psn_card).setVisibility(View.GONE);
//            header.findViewById(R.id.bind_steam_psn_card).setVisibility(View.VISIBLE);
//        }
//        //都绑定了
//        if (psn.getIsbang() == 1 || steam.getIsbang() == 1) {
//            header.findViewById(R.id.steam_psn_card).setVisibility(View.VISIBLE);
//            header.findViewById(R.id.bind_steam_psn_card).setVisibility(View.GONE);
//        }
//        //未绑定
//        if (psn.getIsbang() == 0) {
//            //
//            header.findViewById(R.id.user_psn_button).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ToastUtil.showToast(getContext(), "还没有绑定psn账号");
//                }
//            });
//        } else if (psn.getIsbang() == 1) {
//            psn_but.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    steam_card.setVisibility(View.GONE);
//                    psn_card.setVisibility(View.VISIBLE);
//                }
//            });
//            if (psn.getIsauth() == 0) {
//                GlideUtil.loadImage(mContext, R.drawable.psn_uncertification, psn_card.findViewById(R.id.psn_certification));
//                psn_card.findViewById(R.id.psn_to_certi).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mContext, PsnCertification.class);
//                        intent.putExtra("psnid", psn.getPsn_nickname());
//                        startActivity(intent);
//                    }
//                });
//            } else if (psn.getIsauth() == 1) {
//                GlideUtil.loadImage(mContext, R.drawable.psn_certification, psn_card.findViewById(R.id.psn_certification));
//            }
//            GlideUtil.loadImage(mContext, psn.getPsn_avatarstr(), psn_card.findViewById(R.id.img_psn_head));
//            TextView userName = psn_card.findViewById(R.id.psn_user_name);
//            TextView masonry = psn_card.findViewById(R.id.masonry);
//            TextView gold = psn_card.findViewById(R.id.gold);
//            TextView silver = psn_card.findViewById(R.id.silver);
//            TextView copper = psn_card.findViewById(R.id.copper);
//            TextView price = psn_card.findViewById(R.id.game_price);
//            TextView count = psn_card.findViewById(R.id.game_count);
//
//            price.setText(psn.getGameprice() + "");
//            count.setText(psn.getGamecount() + "");
//            userName.setText(psn.getPsn_nickname());
//            masonry.setText(psn.getMasonry() + "");
//            gold.setText(psn.getGold() + "");
//            silver.setText(psn.getSilver() + "");
//            copper.setText(psn.getCopper() + "");
//
//        }
//
//        if (steam.getIsbang() == 0) {
//            header.findViewById(R.id.user_steam_button).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ToastUtil.showToast(getContext(), "还没有绑定steam账号");
//                }
//            });
//        } else {
//            steam_but.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    steam_card.setVisibility(View.VISIBLE);
//                    psn_card.setVisibility(View.GONE);
//                }
//            });
//            GlideUtil.loadImage(mContext, steam.getSt_avatarstr(), steam_card.findViewById(R.id.img_steam_head));
//            TextView gamevalue = steam_card.findViewById(R.id.my_steam_game_price);
//            TextView gamecount = steam_card.findViewById(R.id.steam_game_count);
//            TextView user_name = steam_card.findViewById(R.id.steam_user_name);
//            TextView time = steam_card.findViewById(R.id.steam_time);
//            time.setText(steam.getGamehour());
//            user_name.setText(steam.getSt_nickname());
//            gamevalue.setText(steam.getGameprice() + "");
//            gamecount.setText(steam.getGamecount() + "");
//
//        }
//
//    }
//
//    //获取用户中心数据
//    private void getUserData() {
//        UserDataBean userData = MyApplication.getUserData();
//        long time = System.currentTimeMillis();
//        String sign = StringUtil.newMD5(userData.uid + time + NewUrl.KEY);
//
//        Map<String, Object> values = new HashMap<>();
//        values.put("uid", userData.uid);
//        values.put("sign", sign);
//        values.put("time", time);
//        String json = new JSONObject(values).toString();
//
//        OkHttpUtils
//                .postString()
//                .url(NewUrl.MY_HOME)
//                .content(json)
//                .build()
//                .execute(new Callback<NewUserInfo>() {
//
//                    @Override
//                    public NewUserInfo parseNetworkResponse(Response response) throws IOException {
//                        String string = response.body().string();
//                        return new Gson().fromJson(string, NewUserInfo.class);
//                    }
//
//                    @Override
//                    public void onError(Request request, Exception e) {
//                        Log.e("message", e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(NewUserInfo response) {
//                        updateView(response);
//                    }
//
//                });
//
//    }
//
//    NewUserInfo.Info info;
//
//    private void updateView(NewUserInfo nui) {
//        List<NewsBean> newsBeans = newsFile.queryHistory();
//        record_count.setText(newsBeans.size() + "");
//        info = nui.getData().getInfo();
//        tiezi.setText(info.getFavorite_total() + "");
//        guanzhu.setText(info.getFollow_total() + "");
//        fensi.setText(info.getFansi_total() + "");
//        mBinding.userLv.setText("Lv." + info.getUser_level());
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        setLogin();
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//        inflater.inflate(R.menu.user_toobar_item, menu);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//    }
//
//    public class UserDynamicRoot {
//        private int code;
//        private UserDynamicList data;
//        private String msg;
//
//        public int getCode() {
//            return code;
//        }
//
//        public void setCode(int code) {
//            this.code = code;
//        }
//
//        public UserDynamicList getData() {
//            return data;
//        }
//
//        public void setData(UserDynamicList data) {
//            this.data = data;
//        }
//
//        public String getMsg() {
//            return msg;
//        }
//
//        public void setMsg(String msg) {
//            this.msg = msg;
//        }
//
//        class UserDynamicList {
//            private List<UserDynamic> list;
//
//            public List<UserDynamic> getList() {
//                return list;
//            }
//
//            public void setList(List<UserDynamic> list) {
//                this.list = list;
//            }
//        }
//
//        class UserDynamic {
//            private long id;
//            private int position;
//            private int goodcount;
//            private long pubdate_at;
//            private String content;
//            private long aid;
//            private String title;
//            private String arcurl;
//            private int showtype;
//            private String webviewurl;
//
//            public long getId() {
//                return id;
//            }
//
//            public void setId(long id) {
//                this.id = id;
//            }
//
//            public int getPosition() {
//                return position;
//            }
//
//            public void setPosition(int position) {
//                this.position = position;
//            }
//
//            public int getGoodcount() {
//                return goodcount;
//            }
//
//            public void setGoodcount(int goodcount) {
//                this.goodcount = goodcount;
//            }
//
//            public long getPubdate_at() {
//                return pubdate_at;
//            }
//
//            public void setPubdate_at(long pubdate_at) {
//                this.pubdate_at = pubdate_at;
//            }
//
//            public String getContent() {
//                return content;
//            }
//
//            public void setContent(String content) {
//                this.content = content;
//            }
//
//            public long getAid() {
//                return aid;
//            }
//
//            public void setAid(long aid) {
//                this.aid = aid;
//            }
//
//            public String getTitle() {
//                return title;
//            }
//
//            public void setTitle(String title) {
//                this.title = title;
//            }
//
//            public String getArcurl() {
//                return arcurl;
//            }
//
//            public void setArcurl(String arcurl) {
//                this.arcurl = arcurl;
//            }
//
//            public int getShowtype() {
//                return showtype;
//            }
//
//            public void setShowtype(int showtype) {
//                this.showtype = showtype;
//            }
//
//            public String getWebviewurl() {
//                return webviewurl;
//            }
//
//            public void setWebviewurl(String webviewurl) {
//                this.webviewurl = webviewurl;
//            }
//        }
//    }
//
//}
