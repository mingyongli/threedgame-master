package com.ws3dm.app.fragment;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcodecraeer.demo.MyAdapter;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;
import com.ws3dm.app.Constant;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.ArticleListActivity;
import com.ws3dm.app.activity.AuthorDetailActivity;
import com.ws3dm.app.activity.AuthorTeamActivity;
import com.ws3dm.app.activity.ColumActivity;
import com.ws3dm.app.activity.LastListActivity;
import com.ws3dm.app.activity.OriginalActivity;
import com.ws3dm.app.activity.PingceListActivity;
import com.ws3dm.app.activity.ProgramActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.AuthorTeamBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.OriginalBean;
import com.ws3dm.app.databinding.FgOriginalBinding;
import com.ws3dm.app.mvp.model.RespBean.OrignewHomeRespBean;
import com.ws3dm.app.mvp.presenter.OriginalPresenter;
import com.ws3dm.app.sqlite.NewsFile;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.util.glide.GlideUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Describution : 原创（底部标签）新版
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2019/8/6 9:27
 **/

public class OriginalFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "OriginalFragment";
    private FgOriginalBinding mBinding;
    private OriginalPresenter mOriginalPresenter;
    private Handler mHandler;
    private View header;//存储顶部viewpager
    private CommonRecyclerAdapter<AuthorTeamBean> mAdapter;
    private boolean hasHead = false;
    private NewsFile newsCollectFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_original, container, false);
        mBinding.setHandler(this);//添加监听事件
        header = LayoutInflater.from(mContext).inflate(R.layout.header_fg_original, container, false);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (!mIsRunning) {
                    return;
                }
                switch (msg.what) {
                    case Constant.Notify.LOAD_START:
                        ToastUtil.showToast(mContext, "载入中");
                        break;
                    case Constant.Notify.LOAD_FAILURE:
                        ToastUtil.showToast(mContext, "请求失败");
                        mBinding.recyclerview.loadMoreError();
                        break;
                    case Constant.Notify.ORIGIN_HOME://处理返回结果
                        initView((OrignewHomeRespBean) msg.obj);
                        break;
                }
            }
        };

        mOriginalPresenter = OriginalPresenter.getInstance();
        mOriginalPresenter.setHandler(mHandler);

        newsCollectFile = new NewsFile(mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerview.setLayoutManager(layoutManager);

        mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);       //原来就注释掉的
        mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mBinding.recyclerview.setLoadingMoreEnabled(false);
        mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        obtainData();
                    }
                }, 50);            //refresh data here
            }

            @Override
            public void onLoadMore() {
            }
        });
        mBinding.recyclerview.setAdapter(new MyAdapter(new ArrayList<String>()));//没啥用，只是让listview可以显示

        return mBinding.getRoot();
    }

    public void obtainData() {
        //获取数据
        long time = System.currentTimeMillis();
        String sign = StringUtil.MD5(time + "");
        JSONObject obj = new JSONObject();
        try {
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mOriginalPresenter.getOrignewhome(obj.toString());//异步请求
    }

    public void initView(OrignewHomeRespBean mData) {
//        mBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////				startActivity(new Intent(getActivity(), NewSearchActivity.class).putExtra("search", "GameFragment"));
//                startActivity(new Intent(getActivity(), SearchActivity.class).putExtra("search", "NewsFragment"));
////				Intent aggreement=new Intent(mContext,SingleWebActivity.class);
////				aggreement.putExtra("title","搜索");
////				aggreement.putExtra("url","https://wap.sogou.com/");
////				startActivity(aggreement);
//            }
//        });

        if (!hasHead) {
            mBinding.recyclerview.addHeaderView(header);
            hasHead = true;
        }
        initLast(mData.getData().getNeworig());
        initProgram(mData.getData().getProgramlist());
        initElse(mData.getData());
        initTeam(mData.getData().getMyauthors());
        initAuthor(mData.getData().getMediaauthor());

        mBinding.recyclerview.refreshComplete();
    }

    //    初始化最新原创
    private void initLast(List<OriginalBean> mList) {
        mList.add(new OriginalBean());//加一个空的放箭头
        header.findViewById(R.id.tv_more_last).setOnClickListener(this);
        ViewPager vp_last = header.findViewById(R.id.vp_last);
        vp_last.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.element_margin_9));
        vp_last.setAdapter(new lastAdapter(mContext, mList));
        vp_last.setOffscreenPageLimit(mList.size());
        //viewpager设置页面改变的监听
        vp_last.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //    初始化我们的节目
    private void initProgram(List<OriginalBean> mList) {
        header.findViewById(R.id.tv_more_program).setOnClickListener(this);
        ViewPager vp_last = header.findViewById(R.id.vp_program);
        vp_last.setPageMargin((int) getResources().getDimensionPixelOffset(R.dimen.element_margin_9));
        vp_last.setAdapter(new programAdapter(mContext, mList));
        vp_last.setOffscreenPageLimit(mList.size());
        //viewpager设置页面改变的监听
        vp_last.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        header.findViewById(R.id.tv_more_program).setOnClickListener(this);
    }

    // 1.历史上的今天，2.3DM评测，3.专栏，4.本周热度
    private void initElse(final OrignewHomeRespBean.DataBean bean) {
        //1.先初始化历史上的今天
        if (bean.getHistoday() == null) {
            header.findViewById(R.id.ll_history).setVisibility(View.GONE);
        } else {
            long long_time = bean.getHistoday().getPubdate_at();
            TextView day = header.findViewById(R.id.tv_day);
            TextView year = header.findViewById(R.id.tv_year);
            TextView week = header.findViewById(R.id.tv_week);
            day.setText(StringUtil.Keep2DecimalPlaces(TimeUtil.getDay(long_time)));
            year.setText(TimeUtil.getTimeCNYM(long_time));
            week.setText(TimeUtil.getCNWeekDay(long_time));
            ((TextView) header.findViewById(R.id.tv_history)).setText(bean.getHistoday().getTitle());

            header.findViewById(R.id.ll_history).setOnClickListener(new View.OnClickListener() {//历史上的今天
                @Override
                public void onClick(View v) {
                    addHistory(bean.getHistoday());
                    Intent intent = new Intent(mContext, OriginalActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("originalBean", bean.getHistoday());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
        //2.3DM评测
//		评测一共有3条数据
        ((TextView) header.findViewById(R.id.tv_title1)).setText(bean.getEvaluatlist().get(0).getTitle());
        ((TextView) header.findViewById(R.id.tv_time1)).setText(TimeUtil.getFoolishTime2(bean.getEvaluatlist().get(0).getPubdate_at() + "000"));
        ((TextView) header.findViewById(R.id.txtComment1)).setText(bean.getEvaluatlist().get(0).getTotal_ct() + "");
        ((TextView) header.findViewById(R.id.pingce_name1)).setText(bean.getEvaluatlist().get(0).getUser().nickname);
        ((TextView) header.findViewById(R.id.tv_score1)).setText(bean.getEvaluatlist().get(0).getScore() + "");
        GlideUtil.loadImage(mContext, bean.getEvaluatlist().get(0).getLitpic(), (ImageView) header.findViewById(R.id.img1));
        GlideUtil.loadCircleImage(mContext, bean.getEvaluatlist().get(0).getUser().avatarstr, (ImageView) header.findViewById(R.id.pingce_head1));
        ((TextView) header.findViewById(R.id.tv_title2)).setText(bean.getEvaluatlist().get(1).getTitle());
        ((TextView) header.findViewById(R.id.tv_time2)).setText(TimeUtil.getFoolishTime2(bean.getEvaluatlist().get(1).getPubdate_at() + "000"));
        ((TextView) header.findViewById(R.id.txtComment2)).setText(bean.getEvaluatlist().get(1).getTotal_ct() + "");
        ((TextView) header.findViewById(R.id.pingce_name2)).setText(bean.getEvaluatlist().get(1).getUser().nickname);
        ((TextView) header.findViewById(R.id.tv_score2)).setText(bean.getEvaluatlist().get(1).getScore() + "");
        GlideUtil.loadCircleImage(mContext, bean.getEvaluatlist().get(1).getUser().avatarstr, (ImageView) header.findViewById(R.id.pingce_head2));
        GlideUtil.loadImage(mContext, bean.getEvaluatlist().get(1).getLitpic(), (ImageView) header.findViewById(R.id.img2));
        ((TextView) header.findViewById(R.id.tv_title3)).setText(bean.getEvaluatlist().get(2).getTitle());
        ((TextView) header.findViewById(R.id.tv_time3)).setText(TimeUtil.getFoolishTime2(bean.getEvaluatlist().get(2).getPubdate_at() + "000"));
        ((TextView) header.findViewById(R.id.txtComment3)).setText(bean.getEvaluatlist().get(2).getTotal_ct() + "");
        ((TextView) header.findViewById(R.id.pingce_name3)).setText(bean.getEvaluatlist().get(2).getUser().nickname);
        ((TextView) header.findViewById(R.id.tv_score3)).setText(bean.getEvaluatlist().get(2).getScore() + "");
        GlideUtil.loadCircleImage(mContext, bean.getEvaluatlist().get(2).getUser().avatarstr, (ImageView) header.findViewById(R.id.pingce_head3));
        GlideUtil.loadImage(mContext, bean.getEvaluatlist().get(2).getLitpic(), (ImageView) header.findViewById(R.id.img3));

        header.findViewById(R.id.rl_pingce1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHistory(bean.getEvaluatlist().get(0));
                Intent intent = new Intent(mContext, OriginalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("originalBean", bean.getEvaluatlist().get(0));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        header.findViewById(R.id.rl_pingce2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHistory(bean.getEvaluatlist().get(1));
                Intent intent = new Intent(mContext, OriginalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("originalBean", bean.getEvaluatlist().get(1));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        header.findViewById(R.id.rl_pingce3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHistory(bean.getEvaluatlist().get(2));
                Intent intent = new Intent(mContext, OriginalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("originalBean", bean.getEvaluatlist().get(2));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        header.findViewById(R.id.tv_more_pince).setOnClickListener(this);
//		3.专栏
        GlideUtil.loadImage(mContext, bean.getCollist().getLitpic(), (ImageView) header.findViewById(R.id.img_column));
        header.findViewById(R.id.img_column).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHistory(bean.getCollist());
                Intent intent = new Intent(mContext, OriginalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("originalBean", bean.getCollist());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ((TextView) header.findViewById(R.id.tv_title_column)).setText(bean.getCollist().getTitle());
//		if(bean.getCollist().getLabel().size()>0)
//			((TextView)header.findViewById(R.id.tag_colum)).setText(bean.getCollist().getLabel().get(0).getName());
        header.findViewById(R.id.tv_more_column).setOnClickListener(this);

//		4.本周热度
        GlideUtil.loadCircleImage(mContext, bean.getWeektop().get(0).getUser().avatarstr, (ImageView) header.findViewById(R.id.top_head1));
        ((TextView) header.findViewById(R.id.top_title1)).setText(bean.getWeektop().get(0).getTitle());
        ((TextView) header.findViewById(R.id.top_name1)).setText(bean.getWeektop().get(0).getUser().nickname);
        ((TextView) header.findViewById(R.id.top_Comment1)).setText(bean.getWeektop().get(0).getTotal_ct() + "");
        GlideUtil.loadImage(mContext, bean.getWeektop().get(0).getLitpic(), (ImageView) header.findViewById(R.id.img_top1));

        GlideUtil.loadCircleImage(mContext, bean.getWeektop().get(1).getUser().avatarstr, (ImageView) header.findViewById(R.id.top_head2));
        ((TextView) header.findViewById(R.id.top_title2)).setText(bean.getWeektop().get(1).getTitle());
        ((TextView) header.findViewById(R.id.top_name2)).setText(bean.getWeektop().get(1).getUser().nickname);
        ((TextView) header.findViewById(R.id.top_Comment2)).setText(bean.getWeektop().get(1).getTotal_ct() + "");
        GlideUtil.loadImage(mContext, bean.getWeektop().get(1).getLitpic(), (ImageView) header.findViewById(R.id.img_top2));

        GlideUtil.loadCircleImage(mContext, bean.getWeektop().get(2).getUser().avatarstr, (ImageView) header.findViewById(R.id.top_head3));
        ((TextView) header.findViewById(R.id.top_title3)).setText(bean.getWeektop().get(2).getTitle());
        ((TextView) header.findViewById(R.id.top_name3)).setText(bean.getWeektop().get(2).getUser().nickname);
        ((TextView) header.findViewById(R.id.top_Comment3)).setText(bean.getWeektop().get(2).getTotal_ct() + "");
        GlideUtil.loadImage(mContext, bean.getWeektop().get(2).getLitpic(), (ImageView) header.findViewById(R.id.img_top3));

        GlideUtil.loadCircleImage(mContext, bean.getWeektop().get(3).getUser().avatarstr, (ImageView) header.findViewById(R.id.top_head4));
        ((TextView) header.findViewById(R.id.top_title4)).setText(bean.getWeektop().get(3).getTitle());
        ((TextView) header.findViewById(R.id.top_name4)).setText(bean.getWeektop().get(3).getUser().nickname);
        ((TextView) header.findViewById(R.id.top_Comment4)).setText(bean.getWeektop().get(3).getTotal_ct() + "");
        GlideUtil.loadImage(mContext, bean.getWeektop().get(3).getLitpic(), (ImageView) header.findViewById(R.id.img_top4));

        header.findViewById(R.id.rl_top1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHistory(bean.getWeektop().get(0));
                Intent intent = new Intent(mContext, OriginalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("originalBean", bean.getWeektop().get(0));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        header.findViewById(R.id.rl_top2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHistory(bean.getWeektop().get(1));
                Intent intent = new Intent(mContext, OriginalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("originalBean", bean.getWeektop().get(1));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        header.findViewById(R.id.rl_top3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHistory(bean.getWeektop().get(2));
                Intent intent = new Intent(mContext, OriginalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("originalBean", bean.getWeektop().get(2));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        header.findViewById(R.id.rl_top4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHistory(bean.getWeektop().get(3));
                Intent intent = new Intent(mContext, OriginalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("originalBean", bean.getWeektop().get(3));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    //    我们的团队
    private void initTeam(final List<AuthorTeamBean> mList) {
        AuthorTeamBean arrow = new AuthorTeamBean();
        arrow.setNickname("查看更多");
        mList.add(arrow);
        RecyclerView rv_team = header.findViewById(R.id.rv_team);
        rv_team.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        mAdapter = new CommonRecyclerAdapter<AuthorTeamBean>(mContext, R.layout.item_author) {
            @Override
            public void bindData(RecyclerViewHolder holder, int position, AuthorTeamBean bean) {
                if (position == mList.size() - 1) {
                    Glide.with(mContext).load(R.drawable.next).dontAnimate().into((ImageView) holder.getView(R.id.img_head));
                    holder.getView(R.id.img_head).setVisibility(View.INVISIBLE);
                    holder.getView(R.id.img_more).setVisibility(View.VISIBLE);
                } else {
                    GlideUtil.loadCircleImage(mContext, bean.getLitpic(), (ImageView) holder.getView(R.id.img_head));
                    holder.getView(R.id.img_head).setVisibility(View.VISIBLE);
                    holder.getView(R.id.img_more).setVisibility(View.INVISIBLE);
                }
                holder.setText(R.id.tv_name, bean.getNickname());
                holder.setText(R.id.tv_info, bean.getDesc());
            }
        };
        rv_team.setAdapter(mAdapter);
        mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (position == mList.size() - 1) {
                    Intent intent = new Intent(mContext, AuthorTeamActivity.class);
                    intent.putExtra("type", 2);//2原创作者团队3入住媒体
                    startActivity(intent);
                } else {
                    //Intent intent=new Intent(mContext,ArticleListActivity.class);
                    //intent.putExtra("authorid",mAdapter.getDataByPosition(position).getId());
                    Intent intent = new Intent(mContext, AuthorDetailActivity.class);
                    intent.putExtra("uid", mAdapter.getDataByPosition(position).getAuthor_id() + "");
                    startActivity(intent);
                }

            }
        });
        mAdapter.clearAndAddList(mList);

        header.findViewById(R.id.tv_more_team).setOnClickListener(this);//team的更多
    }

    //    初始化入驻作者
    private void initAuthor(List<AuthorTeamBean> mList) {
        mList.add(new AuthorTeamBean());//加一个空的放箭头
        header.findViewById(R.id.tv_more_author).setOnClickListener(this);
        ViewPager vp_last = header.findViewById(R.id.vp_author);
        vp_last.setPageMargin((int) getResources().getDimensionPixelOffset(R.dimen.element_margin_9));
        vp_last.setAdapter(new authorAdapter(mContext, mList));
        vp_last.setOffscreenPageLimit(mList.size());
        //viewpager设置页面改变的监听
        vp_last.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        header.findViewById(R.id.tv_more_author).setOnClickListener(this);//作者的更多
    }

    private class lastAdapter extends PagerAdapter {
        List<OriginalBean> list;
        Context context;

        public lastAdapter(Context context, List<OriginalBean> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            if (position < list.size() - 1) {
                final OriginalBean originalBean = list.get(position);
                View view = View.inflate(mContext, R.layout.vp_last, null);

                ImageView img_top = view.findViewById(R.id.img_top);
                GlideUtil.loadImage(mContext, originalBean.getLitpic(), img_top);
                ImageView img_head = view.findViewById(R.id.img_head);
                GlideUtil.loadCircleImage(mContext, originalBean.getUser().avatarstr, img_head);
                TextView tv = view.findViewById(R.id.tv_name);
                tv.setText(originalBean.getUser().nickname);
                TextView tv_label = view.findViewById(R.id.tv_label);
                if (originalBean.getLabel().size() == 0) {
                    tv_label.setVisibility(View.GONE);
                } else {
                    tv_label.setText(originalBean.getLabel().get(0).getName());
                    tv_label.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ArticleListActivity.class);
                            intent.putExtra("tag", 1);//tag:0作者团队,显示顶部个人信息 1专栏，只有背景图
                            intent.putExtra("labelid", Integer.parseInt(originalBean.getLabel().get(0).getId()));
                            intent.putExtra("title", originalBean.getLabel().get(0).getName());
                            startActivity(intent);
                        }
                    });
                }
                TextView tv_title = view.findViewById(R.id.tv_title);
                tv_title.setText(originalBean.getTitle());
                TextView tv_time = view.findViewById(R.id.tv_time);
                tv_time.setText(TimeUtil.getFoolishTime2(originalBean.getPubdate_at() + "000"));
                TextView txtComment = view.findViewById(R.id.txtComment);
                txtComment.setText(originalBean.getTotal_ct() + "");

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MobclickAgent.onEvent(mContext,"07");
                        addHistory(originalBean);
                        Intent intent = new Intent(mContext, OriginalActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("originalBean", originalBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                container.addView(view);
                return view;
            } else {
                View more = View.inflate(mContext, R.layout.view_more, null);
                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        header.findViewById(R.id.tv_more_last).performClick();
                    }
                });
                container.addView(more);
                return more;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public float getPageWidth(int position) {
            if (position == list.size() - 1)
                return 0.25f;
            else
                return 1f;
        }
    }

    private class programAdapter extends PagerAdapter {
        List<OriginalBean> list;
        Context context;

        public programAdapter(Context context, List<OriginalBean> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final OriginalBean originalBean = list.get(position);
            View view = View.inflate(mContext, R.layout.vp_program, null);

            ImageView img_top = view.findViewById(R.id.img_top);
            GlideUtil.loadImage(mContext, originalBean.getLitpic(), img_top);
            TextView title_column = view.findViewById(R.id.title_column);
            title_column.setText(originalBean.getTitle());

//			TextView txt_label = view.findViewById(R.id.txt_label);
//			if(originalBean.getLabel().size()==0)
//				txt_label.setVisibility(View.GONE);
//			else
//				txt_label.setText(originalBean.getLabel().get(0).getName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(mContext,"08");
                    addHistory(originalBean);
                    Intent intent = new Intent(mContext, OriginalActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("originalBean", originalBean);
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
//					bundle.putString("str_game", JSON.toJSONString(mGame));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class authorAdapter extends PagerAdapter {
        List<AuthorTeamBean> list;
        Context context;

        public authorAdapter(Context context, List<AuthorTeamBean> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            if (position < list.size() - 1) {
                final AuthorTeamBean authorTeamBean = list.get(position);
                View view = View.inflate(mContext, R.layout.vp_author, null);

                ImageView img_head = view.findViewById(R.id.img_head);
                GlideUtil.loadCircleImage(mContext, authorTeamBean.getLitpic(), img_head);
                TextView tv_name = view.findViewById(R.id.tv_name);
                tv_name.setText(authorTeamBean.getNickname());
                TextView tv_info = view.findViewById(R.id.tv_info);
                tv_info.setText(authorTeamBean.getDesc());
                ImageView img_news = view.findViewById(R.id.img_news);
                GlideUtil.loadRoundImage(mContext, authorTeamBean.getNewslist().getLitpic(), img_news, 5);
                TextView txtTitle = view.findViewById(R.id.txtTitle);
                txtTitle.setText(authorTeamBean.getNewslist().getTitle());
                TextView tv_time = view.findViewById(R.id.tv_time);
                tv_time.setText(TimeUtil.getFoolishTime2(authorTeamBean.getNewslist().getPubdate_at() + "000"));
                TextView txtComment = view.findViewById(R.id.txtComment);
                txtComment.setText(authorTeamBean.getNewslist().getTotal_ct() + "");
                view.findViewById(R.id.rl_news).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addHistory(authorTeamBean.getNewslist());
                        Intent intent = new Intent(mContext, OriginalActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("originalBean", authorTeamBean.getNewslist());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ArticleListActivity.class);
                        intent.putExtra("authorid", authorTeamBean.getId());
                        startActivity(intent);
                    }
                });
                container.addView(view);
                return view;
            } else {
                View more = View.inflate(mContext, R.layout.view_more, null);
                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        header.findViewById(R.id.tv_more_author).performClick();
                    }
                });
                container.addView(more);
                return more;
            }

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public float getPageWidth(int position) {
            if (position == list.size() - 1)
                return 0.25f;
            else
                return 1f;
        }
    }

    public void addHistory(OriginalBean originalBean) {
        NewsBean newsBean = new NewsBean();
        newsBean.setArcurl(originalBean.getArcurl());
        newsBean.setWebviewurl(originalBean.getWebviewurl());
        newsBean.setTitle(originalBean.getTitle());
        newsBean.setLitpic(originalBean.getLitpic());
        newsBean.setType("原创");
        newsBean.setSeeDate(TimeUtil.dateDayNow());
        newsCollectFile.addHistory(newsBean);
    }

    protected void onFragmentFirstVisible() {
        mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mOriginalPresenter != null) {
            mOriginalPresenter.setHandler(mHandler);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_more_last://更多最新原创
                MobclickAgent.onEvent(mContext,"07");
                intent = new Intent(mContext, LastListActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_more_program://更多节目
                MobclickAgent.onEvent(mContext,"08");
                intent = new Intent(mContext, ProgramActivity.class);
                intent.putExtra("tag", 1);//0不显示顶部  1显示顶部
                startActivity(intent);
                break;
            case R.id.tv_more_pince://更多评测
                MobclickAgent.onEvent(mContext,"09");
                intent = new Intent(mContext, PingceListActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_more_column://更多专栏
                MobclickAgent.onEvent(mContext,"10");
                intent = new Intent(mContext, ColumActivity.class);
                intent.putExtra("tag", 1);//0不显示顶部  1显示顶部
                startActivity(intent);
                break;
            case R.id.tv_more_team://更多团队
                intent = new Intent(mContext, AuthorTeamActivity.class);
                intent.putExtra("type", 2);//2原创作者团队3入住媒体
                startActivity(intent);
                break;
            case R.id.tv_more_author://更多作者
                intent = new Intent(mContext, AuthorTeamActivity.class);
                intent.putExtra("type", 3);//2原创作者3入住媒体
                startActivity(intent);
                break;
            default:
                Log.e(TAG, "TODO: ClickListener" + view.getId());
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.original_toolbar_item, menu);
    }
}
