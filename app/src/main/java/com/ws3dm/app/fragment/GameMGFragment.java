package com.ws3dm.app.fragment;

import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.graphics.drawable.ClipDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.demo.MyAdapter;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.GameGiftActivity;
import com.ws3dm.app.activity.GameHomeActivity;
import com.ws3dm.app.activity.GiftDetailActivity;
import com.ws3dm.app.activity.GuessLikeActivity;
import com.ws3dm.app.activity.HotMGActivity;
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.activity.OriginalActivity;
import com.ws3dm.app.activity.PingceListActivity;
import com.ws3dm.app.activity.SelectMGListActivity;
import com.ws3dm.app.adapter.CommonFragmentPagerAdapter;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.bean.AuthorTeamBean;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.GameGiftBean;
import com.ws3dm.app.bean.GameTestBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.OriginalBean;
import com.ws3dm.app.databinding.FgBaseRecyclerviewBinding;
import com.ws3dm.app.mvp.model.RespBean.GameMGhomeRespBean;
import com.ws3dm.app.network.AdExposure;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.glide.GlideUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :游戏库（底部标签）新版
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/16 13:20
 **/

public class GameMGFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "GameMGFragment";
    private FgBaseRecyclerviewBinding mBinding;
    private CommonFragmentPagerAdapter fragmentPagerAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private View header;//存储顶部viewpager
    private CommonRecyclerAdapter<AuthorTeamBean> mAdapter;
    private boolean hasHead = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_base_recyclerview, container, false);
        header = LayoutInflater.from(mContext).inflate(R.layout.header_fg_game_mg, container, false);

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
//		mGamePresenter.getDJhome(obj.toString());//异步请求
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofitV4(0);
        GameService.Api service = retrofit.create(GameService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<GameMGhomeRespBean> call = service.syHome(body);
        call.enqueue(new Callback<GameMGhomeRespBean>() {
            @Override
            public void onResponse(Call<GameMGhomeRespBean> call, Response<GameMGhomeRespBean> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                initView(response.body());
            }

            @Override
            public void onFailure(Call<GameMGhomeRespBean> call, Throwable throwable) {
                mBinding.recyclerview.loadMoreError();
            }
        });
    }

    public void initView(GameMGhomeRespBean mData) {
        if (!hasHead) {
            mBinding.recyclerview.addHeaderView(header);
            hasHead = true;
        }
        initHotMG(mData.getData().getSlides());//热门手游

        header.findViewById(R.id.img0).setOnClickListener(this);
        header.findViewById(R.id.img1).setOnClickListener(this);
        header.findViewById(R.id.img2).setOnClickListener(this);
        header.findViewById(R.id.img3).setOnClickListener(this);
        header.findViewById(R.id.ll_tabs).setVisibility(View.VISIBLE);

        initEvaluate(mData.getData().getEvaluatlist());//手游评测
        initAnli(mData.getData().getAnli());//日常安利
        initGift(mData.getData().getLibbao());//礼包

        mBinding.recyclerview.refreshComplete();
    }

    //    初始化热门网游
    private void initHotMG(List<GameTestBean> mList) {
        if (mList == null || mList.size() == 0) {
            return;
        }
        ViewPager vp_hotmg = header.findViewById(R.id.vp_hotmg);
        vp_hotmg.setPageMargin((int) getResources().getDimensionPixelOffset(R.dimen.element_margin_9));
        vp_hotmg.setAdapter(new hotmgAdapter(mContext, mList));
        vp_hotmg.setOffscreenPageLimit(mList.size());
        //viewpager设置页面改变的监听
        vp_hotmg.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        header.findViewById(R.id.ll_hotmg).setVisibility(View.VISIBLE);
        // 发消息让轮播图动起来
//		if (mSlideMessager == null) {
//			// 发消息让轮播图动起来
//			mSlideMessager = new Handler() {
//				public void handleMessage(Message msg) {
//					mBinding.vpAdvice.gotoForward();//向后一页// 在发一个handler延时
//					mSlideMessager.sendEmptyMessageDelayed(0, 5000);
//				}
//			};
//			mSlideMessager.sendEmptyMessageDelayed(0, 5000);
//		}

        header.findViewById(R.id.tv_more_hotmg).setOnClickListener(this);//热门网游的更多
    }

    private class hotmgAdapter extends PagerAdapter {
        List<GameTestBean> list;
        Context context;

        public hotmgAdapter(Context context, List<GameTestBean> list) {
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
            final GameBean mGame = new GameBean();
            GameTestBean originalBean = list.get(position);
            mGame.setAid(originalBean.getAid());
            mGame.setArcurl(originalBean.getArcurl());
            mGame.setShowtype(originalBean.getShowtype());

            View view = View.inflate(mContext, R.layout.vp_program, null);

            ImageView img_top = view.findViewById(R.id.img_top);
            GlideUtil.loadImage(mContext, originalBean.getLitpic(), img_top);
            TextView title_column = view.findViewById(R.id.title_column);
            title_column.setText(originalBean.getTitle());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (originalBean.getHttp() == 1) {
                        AdExposure.getInstance().putExposure(
                                originalBean.getId()
                                , String.valueOf(UUID.randomUUID())
                                , AppUtil.getVersionCode(mContext)
                                , "Android"
                                , SharedUtil.getSharedPreferencesData("device")
                                , String.valueOf(System.currentTimeMillis()));
                        Uri parse = Uri.parse(originalBean.getArcurl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, parse);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, GameHomeActivity.class);
                        Bundle bundle = new Bundle();
//						bundle.putSerializable("game",mGame);
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
                        bundle.putString("str_game", JSON.toJSONString(mGame));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
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

    private void initEvaluate(final List<OriginalBean> eList) {//2.3DM评测  评测一共有3条数据
        ((TextView) header.findViewById(R.id.tv_title1)).setText(eList.get(0).getTitle());
        ((TextView) header.findViewById(R.id.tv_time1)).setText(TimeUtil.getFoolishTime2(eList.get(0).getPubdate_at() + "000"));
        ((TextView) header.findViewById(R.id.txtComment1)).setText(eList.get(0).getTotal_ct() + "");
        ((TextView) header.findViewById(R.id.pingce_name1)).setText(eList.get(0).getUser().nickname);
        ((TextView) header.findViewById(R.id.pingce_score1)).setText(eList.get(0).getScore() + "");
        GlideUtil.loadImage(mContext, eList.get(0).getLitpic(), (ImageView) header.findViewById(R.id.pingce_img1));
        GlideUtil.loadCircleImage(mContext, eList.get(0).getUser().avatarstr, (ImageView) header.findViewById(R.id.pingce_head1));
        ((TextView) header.findViewById(R.id.tv_title2)).setText(eList.get(1).getTitle());
        ((TextView) header.findViewById(R.id.tv_time2)).setText(TimeUtil.getFoolishTime2(eList.get(1).getPubdate_at() + "000"));
        ((TextView) header.findViewById(R.id.txtComment2)).setText(eList.get(1).getTotal_ct() + "");
        ((TextView) header.findViewById(R.id.pingce_name2)).setText(eList.get(1).getUser().nickname);
        ((TextView) header.findViewById(R.id.pingce_score2)).setText(eList.get(1).getScore() + "");
        GlideUtil.loadCircleImage(mContext, eList.get(1).getUser().avatarstr, (ImageView) header.findViewById(R.id.pingce_head2));
        GlideUtil.loadImage(mContext, eList.get(1).getLitpic(), (ImageView) header.findViewById(R.id.pingce_img2));
        ((TextView) header.findViewById(R.id.tv_title3)).setText(eList.get(2).getTitle());
        ((TextView) header.findViewById(R.id.tv_time3)).setText(TimeUtil.getFoolishTime2(eList.get(2).getPubdate_at() + "000"));
        ((TextView) header.findViewById(R.id.txtComment3)).setText(eList.get(2).getTotal_ct() + "");
        ((TextView) header.findViewById(R.id.pingce_name3)).setText(eList.get(2).getUser().nickname);
        ((TextView) header.findViewById(R.id.pingce_score3)).setText(eList.get(2).getScore() + "");
        GlideUtil.loadCircleImage(mContext, eList.get(2).getUser().avatarstr, (ImageView) header.findViewById(R.id.pingce_head3));
        GlideUtil.loadImage(mContext, eList.get(2).getLitpic(), (ImageView) header.findViewById(R.id.pingce_img3));

        header.findViewById(R.id.rl_pingce1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//				addHistory(eList.get(0));
                Intent intent = new Intent(mContext, OriginalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("originalBean", eList.get(0));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        header.findViewById(R.id.rl_pingce2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//				addHistory(eList.get(1));
                Intent intent = new Intent(mContext, OriginalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("originalBean", eList.get(1));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        header.findViewById(R.id.rl_pingce3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//				addHistory(eList.get(2));
                Intent intent = new Intent(mContext, OriginalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("originalBean", eList.get(2));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        header.findViewById(R.id.tv_more_pince).setOnClickListener(this);
    }

    //    初始化安利
    private void initAnli(final List<GameTestBean> list) {
        GlideUtil.loadImage(mContext, list.get(0).getLitpic(), (ImageView) header.findViewById(R.id.img_pince));
        ((TextView) header.findViewById(R.id.title_pince)).setText(list.get(0).getTitle());

        header.findViewById(R.id.rl_anli).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameTestBean clickBean = list.get(0);
                if (clickBean.getShowtype() == 6 || clickBean.getShowtype() == 7) {
                    OriginalBean originalBean = new OriginalBean();
                    originalBean.setTitle(clickBean.getTitle());
                    originalBean.setArcurl(clickBean.getArcurl());
                    originalBean.setWebviewurl(clickBean.getWebviewurl());
                    originalBean.setType("" + clickBean.getShowtype());//1新闻6评测7原创
                    Intent intent = new Intent(mContext, OriginalActivity.class);
                    Bundle bundleNews = new Bundle();
                    bundleNews.putSerializable("originalBean", originalBean);
                    intent.putExtras(bundleNews);
                    mContext.startActivity(intent);
                } else {
                    NewsBean news = new NewsBean();
                    news.setTitle(clickBean.getTitle());
                    news.setArcurl(clickBean.getArcurl());
                    news.setWebviewurl(clickBean.getWebviewurl());
                    news.setType("" + clickBean.getShowtype());//1新闻6评测7原创
                    Intent intent = new Intent(mContext, NewsActivity.class);
                    intent.putExtra("isGongLue", 2);
                    Bundle bundleNews = new Bundle();
                    bundleNews.putSerializable("newsBean", news);
                    intent.putExtras(bundleNews);
                    mContext.startActivity(intent);
                }
            }
        });

        header.findViewById(R.id.tv_more_anli).setOnClickListener(this);
    }

    //    初始化礼包
    private void initGift(final List<GameGiftBean> list) {
        if (list == null || list.size() == 0)
            return;
        header.findViewById(R.id.ll_gift).setVisibility(View.VISIBLE);

        GlideUtil.loadImage(mContext, list.get(0).getLitpic(), (ImageView) header.findViewById(R.id.gift_img1));
        ((TextView) header.findViewById(R.id.gift_name1)).setText(list.get(0).getTitle());
        ((TextView) header.findViewById(R.id.gift_time1)).setText(list.get(0).getRange_start() + " 至 " + list.get(0).getRange_end());
        ((TextView) header.findViewById(R.id.gift_name1)).setText(list.get(0).getTitle());

        ((TextView) header.findViewById(R.id.tv_remain1)).setText("剩余：" + list.get(0).getSurplusper() + "%");
        ImageView imgScore1 = header.findViewById(R.id.img_score1);
        // 获得ClipDrawable对象
        ClipDrawable clipDrawable1 = (ClipDrawable) imgScore1.getBackground();
        // 当级别为10000时，不裁剪图片，图片全部可见
        // 当全部显示后，设置不可见
        clipDrawable1.setLevel(list.get(0).getSurplusper() * 100);
        header.findViewById(R.id.rl_gift1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GiftDetailActivity.class);
                intent.putExtra("pageType", 1);//pageType //0 网游礼包  1 手游礼包
                Bundle bundle = new Bundle();
                bundle.putSerializable("mGameGiftBean", list.get(0));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        GlideUtil.loadImage(mContext, list.get(1).getLitpic(), (ImageView) header.findViewById(R.id.gift_img2));
        ((TextView) header.findViewById(R.id.gift_name2)).setText(list.get(1).getTitle());
        ((TextView) header.findViewById(R.id.gift_time2)).setText(list.get(1).getRange_start() + " 至 " + list.get(1).getRange_end());
        ((TextView) header.findViewById(R.id.gift_name2)).setText(list.get(1).getTitle());
        ((TextView) header.findViewById(R.id.tv_remain2)).setText("剩余：" + list.get(1).getSurplusper() + "%");
        ImageView imgScore2 = header.findViewById(R.id.img_score2);
        // 获得ClipDrawable对象
        ClipDrawable clipDrawable2 = (ClipDrawable) imgScore2.getBackground();
        // 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；
        // 当级别为10000时，不裁剪图片，图片全部可见
        // 当全部显示后，设置不可见
        clipDrawable2.setLevel(list.get(1).getSurplusper() * 100);
        header.findViewById(R.id.rl_gift2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GiftDetailActivity.class);
                intent.putExtra("pageType", 1);//pageType //0 网游礼包  1 手游礼包
                Bundle bundle = new Bundle();
                bundle.putSerializable("mGameGiftBean", list.get(1));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        GlideUtil.loadImage(mContext, list.get(2).getLitpic(), (ImageView) header.findViewById(R.id.gift_img3));
        ((TextView) header.findViewById(R.id.gift_name3)).setText(list.get(2).getTitle());
        ((TextView) header.findViewById(R.id.gift_time3)).setText(list.get(2).getRange_start() + " 至 " + list.get(2).getRange_end());
        ((TextView) header.findViewById(R.id.gift_name3)).setText(list.get(2).getTitle());
        ((TextView) header.findViewById(R.id.tv_remain3)).setText("剩余：" + list.get(2).getSurplusper() + "%");
        ImageView imgScore3 = header.findViewById(R.id.img_score3);
        // 获得ClipDrawable对象
        ClipDrawable clipDrawable3 = (ClipDrawable) imgScore3.getBackground();
        // 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；
        // 当级别为10000时，不裁剪图片，图片全部可见
        // 当全部显示后，设置不可见
        clipDrawable3.setLevel(list.get(2).getSurplusper() * 100);
        header.findViewById(R.id.rl_gift3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GiftDetailActivity.class);
                intent.putExtra("pageType", 1);//pageType //0 网游礼包  1 手游礼包
                Bundle bundle = new Bundle();
                bundle.putSerializable("mGameGiftBean", list.get(2));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        header.findViewById(R.id.tv_more_gift).setOnClickListener(this);//礼包的更多
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        MobclickAgent.onEvent(mContext, "06");
        switch (view.getId()) {
            case R.id.tv_more_hotmg://热门手游
                intent = new Intent(mContext, HotMGActivity.class);
                startActivity(intent);
                break;
            case R.id.img0://游戏专题
                intent = new Intent(mContext, SelectMGListActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_more_pince://手有评测
            case R.id.img1:
                intent = new Intent(mContext, PingceListActivity.class);
                intent.putExtra("pageType", 1);
                startActivity(intent);
                break;
            case R.id.tv_more_anli://日常安利
            case R.id.img2:
                intent = new Intent(mContext, GuessLikeActivity.class);
                intent.putExtra("title", "日常安利");
                intent.putExtra("pageType", 1);
                startActivity(intent);
                break;
            case R.id.tv_more_gift://游戏礼包
            case R.id.img3:
                intent = new Intent(mContext, GameGiftActivity.class);
                intent.putExtra("pageType", 1);
                startActivity(intent);
                break;
            default:
                Log.e(TAG, "TODO: ClickListener" + view.getId());
                break;
        }
    }

    protected void onFragmentFirstVisible() {
        mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
    }
}
