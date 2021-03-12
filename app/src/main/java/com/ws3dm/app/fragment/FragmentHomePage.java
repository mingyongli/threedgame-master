package com.ws3dm.app.fragment;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;
import com.umeng.analytics.MobclickAgent;
import com.ws3dm.app.NewUrl;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.activity.OriginalActivity;
import com.ws3dm.app.activity.SingleWebActivity;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.NewsMultiAdapter;
import com.ws3dm.app.adapter.NewsMultiItemTypeSupport;
import com.ws3dm.app.adapter.ViewPagerAdapter;
import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.bean.HomeNewsListBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.OriginalBean;
import com.ws3dm.app.bean.SlidesBean;
import com.ws3dm.app.databinding.FgNewsHotBinding;
import com.ws3dm.app.mvp.model.RespBean.NewsListRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.sqlite.NewsFile;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.glide.GlideRoundTransform;
import com.yu.imgpicker.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :新闻 -- 热点
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/18 17:22
 **/
public class FragmentHomePage extends BaseFragment {
    private FgNewsHotBinding mBinding;
    //	private CommonRecyclerAdapter<NewsBean> mAdapter;
    private NewsMultiAdapter mAdapter;
    public List<NewsBean> listData = new ArrayList<NewsBean>();
    private NewsFile newsCollectFile;
    private View header;//存储顶部viewpager

    //	顶部轮播图
    private int mSlideCount = 0;
    private List<SlidesBean> mSlidesList;
    private ViewPager mSlideViewPager;
    private Handler mSlideMessager = null;
    private boolean mSlideRunning = true;
    private List<ImageView> pointList;//存放View的List
    private int index;//當前廣告下标
    private TextView titleBanner;

    private int mPage = 2;
    private boolean hasHead;
    private ViewPagerAdapter mViewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_news_hot, container, false);
        header = LayoutInflater.from(mContext).inflate(R.layout.header_viewpager, container, false);
        mBinding.recyclerview.addHeaderView(header);

        newsCollectFile = new NewsFile(getActivity());
        hasHead = false;

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerview.setLayoutManager(layoutManager);

        mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);       //原来就注释掉的
        mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

        NewsMultiItemTypeSupport modelMultiItemTypeSupport = new NewsMultiItemTypeSupport();
        mAdapter = new NewsMultiAdapter(mContext, modelMultiItemTypeSupport);
        mBinding.recyclerview.setAdapter(mAdapter);
        mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        obtainData(1);
                        getHotNewsPage();
                    }
                }, 50);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        LoadMoreNews(10, mPage++);
                    }
                }, 50);
            }
        });
        mAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                MobclickAgent.onEvent(mContext, "03");
                mAdapter.getDataByPosition(position).setHavesee(1);
                NewsBean news = mAdapter.getDataByPosition(position);
                if (news.getArcurl() == null) {
                    return;
                }
                news.setSeeDate(TimeUtil.dateDayNow());
                newsCollectFile.addHistory(news);

                if (news.getShowtype() == 7) {//1新闻6评测7原创
                    if (news.getArcurl() == null) {
                        return;
                    }
                    OriginalBean originalBean = new OriginalBean();
                    originalBean.setTitle(news.getTitle());
                    originalBean.setArcurl(news.getArcurl());
                    originalBean.setWebviewurl(news.getWebviewurl());
                    originalBean.setType("7");//1新闻6评测7原创

                    Intent intent = new Intent(mContext, OriginalActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("originalBean", originalBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    if (news.getArcurl() == null) {
                        return;
                    }
                    Intent intent = new Intent(mContext, NewsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("newsBean", news);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        obtainData(1);
        mBinding.recyclerview.setRefreshing(true);
        //getHotNewsPage();
        return mBinding.getRoot();
    }


    protected void onFragmentFirstVisible() {
        mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    public void obtainData(int page) {
        //获取数据
        long time = System.currentTimeMillis();
        String validate = "10" + page + time;
        String sign = StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("pagesize", 10);
            obj.put("page", page);
            obj.put("time", time);
            obj.put("sign", sign);
            obj.put("api_ver", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("requestSuccesss", "----------请求参数-------------" + obj.toString());
//        mForumPresenter.getForumRank(obj.toString());
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        NewsService.Api service = retrofit.create(NewsService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<NewsListRespBean> call = service.getHotNewsPage(body);
        call.enqueue(new Callback<NewsListRespBean>() {
            @Override
            public void onResponse(Call<NewsListRespBean> call, Response<NewsListRespBean> response) {
                NewsListRespBean bean = response.body();
                String s = new Gson().toJson(bean);
                Log.e("requestSuccesss", "------------返回值-----------" + s);


                if (bean != null && bean.getData() != null) {
                    mSlidesList = bean.getData().getSlides();
                    if (mSlidesList != null && mSlidesList.size() > 0 && !hasHead) {
                        initHeadView(bean);

                        hasHead = true;
                    } else if (mSlidesList != null && hasHead && mPage == 1) {
                        initHeadView(bean);
                    }
                }
                //initData(response.body());
                if (!TimeUtil.dateDayNow().equals(SharedUtil.getSharedPreferencesData("today"))) {
                    SharedUtil.setSharedPreferencesData("today", TimeUtil.dateDayNow());
                    SharedUtil.setSharedPreferencesData("hotnews", JSON.toJSONString(response.body().getData().getList()));
                }
            }


            @Override
            public void onFailure(Call<NewsListRespBean> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
                if (listData.size() == 0) {
                    String json = SharedUtil.getSharedPreferencesData("hotnews");
                    if (!StringUtil.isEmpty(json) && json.length() > 100) {
                        listData = JSON.parseArray(json, NewsBean.class);
                        mAdapter.clearAndAddList(listData);
                    }
                }
                mBinding.recyclerview.loadMoreError();
            }
        });
    }

    /**
     * 新的首页接口
     */
    private void getHotNewsPage() {
        long timeMillis = System.currentTimeMillis();
        String sign = StringUtil.newMD5(timeMillis + NewUrl.KEY);
        Map<String, Object> map = new HashMap<>();
        map.put("time", timeMillis);
        map.put("sign", sign);
        String s = JSON.toJSON(map).toString();
        OkHttpUtils.postString().content(s).url(NewUrl.HOTNEWSPAGE).build().connTimeOut(5000).execute(new com.zhy.http.okhttp.callback.Callback<HomeNewsListBean>() {
            @Override
            public HomeNewsListBean parseNetworkResponse(com.squareup.okhttp.Response response) throws IOException {
                long end = System.currentTimeMillis();
                String string = response.body().string();
                return new Gson().fromJson(string, HomeNewsListBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(HomeNewsListBean response) {
                if (response.getCode() == 1) {
                    //因为不准备重写home界面  所以请求回来的数据和老接口数据结构设置成一样的NewsBean
                    setTopNews(response.getData().getTopnews());
                    setGussLike(response.getData().getGuesslike());
                    setNewList(response.getData().getList());
                    mBinding.recyclerview.refreshComplete();

                }
            }
        });
    }

    //设置最新的6条新闻
    private void setTopNews(List<NewsBean> topnews) {
        mAdapter.clearAndAddList(topnews);
    }

    //设置猜你喜欢
    private void setGussLike(List<NewsBean> guesslike) {
        if (guesslike.size() == 0) {
            return;
        }
        List<AvatarBean> guessList = new ArrayList<>();
        for (NewsBean bean : guesslike) {
            AvatarBean bean1 = new AvatarBean();
            bean1.setAid(bean.getAid());
            bean1.setLitpic(bean.getLitpic());
            bean1.setTitle(bean.getTitle());
            bean1.setPubdate_at(bean.getPubdate_at());
            guessList.add(bean1);
        }
        for (NewsBean bean : guesslike) {
            bean.setTypelist(guessList);
        }
        List<NewsBean> CurrentGuessLike = new ArrayList<>();
        CurrentGuessLike.add(guesslike.get(0));
        mAdapter.appendList(CurrentGuessLike);
    }

    //设置最新的10条新闻
    private void setNewList(List<NewsBean> list) {
        mAdapter.appendList(list);
    }

    private void LoadMoreNews(int pageSize, int page) {
        long timeMillis = System.currentTimeMillis();
        String sign = StringUtil.newMD5(pageSize + String.valueOf(page) + timeMillis + NewUrl.KEY);
        Map<String, Object> map = new HashMap<>();
        map.put("pagesize", pageSize);
        map.put("page", page);
        map.put("time", timeMillis);
        map.put("sign", sign);
        String s = JSON.toJSON(map).toString();
        OkHttpUtils.postString().content(s).url(NewUrl.HOTNEWSLIST).build().execute(new com.zhy.http.okhttp.callback.Callback<HomeNewsListBean>() {
            @Override
            public HomeNewsListBean parseNetworkResponse(com.squareup.okhttp.Response response) throws IOException {
                String string = response.body().string();
                return new Gson().fromJson(string, HomeNewsListBean.class);
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(HomeNewsListBean response) {
                mBinding.recyclerview.loadMoreComplete();
                if (response.getCode() == 1) {
                    mBinding.recyclerview.loadMoreComplete();
                    mBinding.recyclerview.refreshComplete();
                    mAdapter.appendList(response.getData().getList());
                }
            }
        });
    }

    //头部的滑动广告栏
    private void initHeadView(NewsListRespBean bean) {
        mBinding.recyclerview.refreshComplete();
        final List<SlidesBean> bannerList = bean.getData().getSlides();
        pointList = new ArrayList<>();
        mSlideViewPager = (ViewPager) header.findViewById(R.id.top_slide);
        titleBanner = (TextView) header.findViewById(R.id.tv_info);
        titleBanner.setText(bannerList.get(0).getTitle());
        mSlideCount = bannerList.size();
        mViewPagerAdapter = new ViewPagerAdapter(bannerList, mContext);
        final LinearLayout ll_points = (LinearLayout) header.findViewById(R.id.point_group);
        if (ll_points.getChildCount() > 0)
            ll_points.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mSlideCount; i++) {
            ImageView imageView = new ImageView(mContext);
            if (i == 0) {
                imageView.setImageResource(R.drawable.red_point);
            } else {
                imageView.setImageResource(R.drawable.white_point);
            }
            params.leftMargin = 5;
            params.rightMargin = 5;
            pointList.add(imageView);
            ll_points.addView(imageView, params);
        }
        ll_points.setGravity(Gravity.CENTER_VERTICAL);
        //初始化图片
//		for(int i=0;i<mSlideCount;i++){
//			ImageView img=new ImageView(getActivity());
//			GlideUtil.loadImage(mContext,bannerList.get(i).getLitpic(),img);
//			img.setScaleType(ImageView.ScaleType.FIT_XY);
//			imageViewList.add(img);
//		}
        mSlideViewPager.setAdapter(mViewPagerAdapter);
        mSlideViewPager.setOffscreenPageLimit(mSlideCount);
        /**

         //        private static final float MIN_SCALE = 0.85f;    可以修改当前图片的缩放尺寸
         vp_head.setPageTransformer(true, new ViewPager.PageTransformer() {
        @Override public void transformPage(View view, float position) {
        if (position <= 1) {
        float scaleFactor = Math.max(MIN_SCALE,1 - Math.abs(position));
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
        }
        }
        });
         **/
        mSlideViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mSlideCount; i++) {
                    if (i == (position % mSlideCount)) {
                        pointList.get(i).setImageResource(R.drawable.red_point);
                    } else {
                        //设置非当前显示页圆点
                        pointList.get(i).setImageResource(R.drawable.white_point);
                    }
                }
                index = position;
                titleBanner.setText(bannerList.get(position % mSlideCount).getTitle());
            }
        });
        //如果图片多于1张开启广告轮播
        if (mSlideCount > 1) {
            //开启广告轮播
            index = 0;
            if (mSlideMessager == null) {
                // 发消息让轮播图动起来
                mSlideMessager = new Handler() {
                    public void handleMessage(Message msg) {
                        if (mSlideRunning) {
                            // 执行滑动到下一个页面
                            mSlideViewPager.setCurrentItem(index + 1);
                            // 在发一个handler延时
                            sendEmptyMessageDelayed(0, 5000);
                        }
                    }
                };
                mSlideMessager.sendEmptyMessageDelayed(0, 3500);
            }
        }


        //初始化投票
        final NewsBean voteDate = bean.getData().getVote();
        if (voteDate != null) {
//			GlideUtil.loadRoundImage(getActivity(),voteDate.getLitpic(),(ImageView)header.findViewById(R.id.img_vote),1);
            Glide.with(mContext).load(voteDate.getLitpic())
//				.placeholder(R.drawable.load_default)
//				.error(R.drawable.load_error)
                    .transform(new GlideRoundTransform(mContext, 4))//20：圆角半径
                    .into((ImageView) header.findViewById(R.id.img_vote));
            ((TextView) header.findViewById(R.id.tv_title)).setText(voteDate.getTitle());
            ((TextView) header.findViewById(R.id.bt_vote)).setText(voteDate.getBt_title());
            header.findViewById(R.id.ll_vote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(mContext, "02");
                    NewsBean news = voteDate;
                    if (news.getShowtype() == 1 || news.getShowtype() == 13) {
                        Intent intent = new Intent(mContext, NewsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("newsBean", news);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (news.getShowtype() == 14) {
                        Intent webIntent = new Intent(mContext, SingleWebActivity.class);
                        webIntent.putExtra("title", news.getDetail_title());
                        webIntent.putExtra("url", news.getWebviewurl());
                        startActivity(webIntent);
                    }
                }
            });

            header.findViewById(R.id.ll_vote).setVisibility(View.VISIBLE);
        }
        //卡片
    }

    private void initData(NewsListRespBean bean) {
        mBinding.recyclerview.refreshComplete();
        if (bean == null || bean.getData() == null || bean.getData().getList() == null)
            return;
        listData = bean.getData().getList();
        if (mPage > 1) {
            mAdapter.appendList(listData);
        } else {
            mAdapter.clearAndAddList(listData);
        }
        if (listData == null || listData.size() < 1) {
            mBinding.recyclerview.setNoMore(true);
        } else {
            mPage++;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSlideMessager = null;
    }

    public void reLoad() {
        mBinding.recyclerview.scrollToPosition(0);
//		mBinding.recyclerview.setRefreshing(true);
//		mPage=1;
//		obtainData(mPage);
    }

}