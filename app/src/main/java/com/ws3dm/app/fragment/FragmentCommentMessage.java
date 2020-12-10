package com.ws3dm.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.MessageActivity;
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.UserDataBean;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.ws3dm.app.view.DMFreshHead;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentCommentMessage extends BaseFragment {

    private View inflate;
    private String title;
    private Bundle arguments;
    private int pageSize = 10;
    private int commentsPage = 1;
    private int messagepage = 1;
    private CommonRecyclerAdapter<UserDynamicRoot.UserDynamic> mAdapter;
    private CommonRecyclerAdapter<MessageActivity.MessageRoot.Message> messageAdapter;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView1;
    private SmartRefreshLayout refreshLayout;

    public FragmentCommentMessage newIntance(String s) {
        FragmentCommentMessage fragment = new FragmentCommentMessage();
        Bundle bundle = new Bundle();
        bundle.putString("title", s);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fg_universal_recyclerview, container, false);
        arguments = getArguments();
        title = arguments.getString("title");
        refreshLayout = inflate.findViewById(R.id.refresh_layout);
        refreshLayout.setRefreshHeader(new DMFreshHead(mContext));
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        if ("评论".equals(title)) {
            recyclerView1 = inflate.findViewById(R.id.recyclerview);
            recyclerView1.setLayoutManager(new LinearLayoutManager(mContext));
            mAdapter = new CommonRecyclerAdapter<UserDynamicRoot.UserDynamic>(mContext, R.layout.adapter_dynamic) {
                @Override
                public void bindData(RecyclerViewHolder holder, final int position, final UserDynamicRoot.UserDynamic item) {
                    View view = holder.getView(R.id.user_img);
                    View item_content = holder.getView(R.id.item_content);
                    View content = holder.getView(R.id.content);
                    holder.setText(R.id.content_type, StringUtil.getShowType(item.getShowtype()));
                    if (position == 0)
                        item_content.setBackgroundResource(R.drawable.shape_user_recomm);
                    content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UserDynamicRoot.UserDynamic db = mAdapter.getDataByPosition(position);
                            NewsBean nb = new NewsBean();
                            nb.setAid((int) db.getAid());
                            nb.setArcurl(db.getArcurl());
                            nb.setBt_title(db.getTitle());
                            nb.setId((int) db.getId());
                            nb.setWebviewurl(db.getWebviewurl());
                            Intent intent = new Intent(mContext, NewsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("newsBean", nb);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    GlideUtil.loadCircleImage(mContext, MyApplication.getUserData().avatarstr, (ImageView) view);
                    holder.setText(R.id.name, MyApplication.getUserData().nickname);
                    holder.setText(R.id.title, item.getContent());
                    holder.setText(R.id.content, item.getTitle());
                    String date = getDateStringByTimeSTamp(item.getPubdate_at(), "yyyy-MM-dd HH:mm");
                    holder.setText(R.id.time, date);
                    holder.setText(R.id.good_number, item.getGoodcount() + "");
                }
            };
            recyclerView1.setAdapter(mAdapter);
            if (!MyApplication.getUserData().loginStatue) {//已经登录
                return null;
            }
            initCommentView();
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    commentsPage = 1;
                    initCommentView();
                }
            });
            refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    commentsPage++;
                    initCommentView();
                }
            });
        } else if ("消息".equals(title)) {
            recyclerView2 = inflate.findViewById(R.id.recyclerview);
            recyclerView2.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView2.setNestedScrollingEnabled(true);
            messageAdapter = new CommonRecyclerAdapter<MessageActivity.MessageRoot.Message>(mContext, R.layout.adapter_user_message_item) {
                @Override
                public void bindData(RecyclerViewHolder holder, final int position, final MessageActivity.MessageRoot.Message item) {
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
            recyclerView2.setAdapter(messageAdapter);
            if (!MyApplication.getUserData().loginStatue) {//已经登录
                return null;
            }
            initMessageView();
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    messagepage = 1;
                    initMessageView();
                }
            });
            refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    messagepage++;
                    initMessageView();
                }
            });
        }
        return inflate;
    }

    private void initMessageView() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + pageSize + messagepage + time + NewUrl.KEY);

        Map<String, Object> values = new HashMap<>();
        values.put("uid", userData.uid);
        values.put("sign", sign);
        values.put("time", time);
        values.put("pagesize", pageSize);
        values.put("page", messagepage);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.MY_MESSAGE)
                .content(json)
                .build()
                .execute(new Callback<MessageActivity.MessageRoot>() {

                    @Override
                    public MessageActivity.MessageRoot parseNetworkResponse(Response response) throws IOException {
                        String string = response.body().string();
                        return new Gson().fromJson(string, MessageActivity.MessageRoot.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(MessageActivity.MessageRoot response) {
                        if (response.getCode() == 1) {

                            updateMessageView(response.getData());
                        }
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                });

    }

    private void updateMessageView(MessageActivity.MessageRoot.Data data) {

        if (messagepage == 1) {
            messageAdapter.clearAndAddList(data.getList());
        } else {
            messageAdapter.appendList(data.getList());
        }
    }

    private void initCommentView() {
        UserDataBean userData = MyApplication.getUserData();
        long time = System.currentTimeMillis();
        String sign = StringUtil.newMD5(userData.uid + pageSize + commentsPage + time + NewUrl.KEY);

        Map<String, Object> values = new HashMap<>();
        values.put("uid", userData.uid);
        values.put("sign", sign);
        values.put("time", time);
        values.put("pagesize", pageSize);
        values.put("page", commentsPage);
        String json = new JSONObject(values).toString();
        OkHttpUtils
                .postString()
                .url(NewUrl.MY_DUNAMIC)
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
                            updateCommentView(response.getData());
                        } else {
                        }
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }

                });
    }

    private void updateCommentView(UserDynamicRoot.UserDynamicList data) {
        if (commentsPage == 1) {
            mAdapter.clearAndAddList(data.getList());
        } else {
            mAdapter.appendList(data.getList());
        }

    }


    public static String getDateStringByTimeSTamp(Long timeStamp, String pattern) {
        String result = null;
        Date date = new Date(timeStamp * 1000);
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        result = sd.format(date);
        return result;
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
            private long pubdate_at;
            private String content;
            private long aid;
            private String title;
            private String arcurl;
            private int showtype;
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

            public long getPubdate_at() {
                return pubdate_at;
            }

            public void setPubdate_at(long pubdate_at) {
                this.pubdate_at = pubdate_at;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
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

            public int getShowtype() {
                return showtype;
            }

            public void setShowtype(int showtype) {
                this.showtype = showtype;
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
