package com.ws3dm.app.fragment;

import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

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
import com.squareup.okhttp.OkHttpClient;
import com.umeng.analytics.MobclickAgent;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.GameCategoryActivity;
import com.ws3dm.app.activity.GameChineseActivity;
import com.ws3dm.app.activity.GameHomeActivity;
import com.ws3dm.app.activity.GameLabelActivity;
import com.ws3dm.app.activity.GameRandListActivity;
import com.ws3dm.app.activity.GameSellActivity;
import com.ws3dm.app.adapter.CommonFragmentPagerAdapter;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.databinding.FgBaseRecyclerviewBinding;
import com.ws3dm.app.mvp.model.RespBean.GameDJhomeRespBean;
import com.ws3dm.app.network.AdExposure;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.glide.GlideUtil;
import com.ws3dm.app.view.CoverFlowView;
import com.ws3dm.app.view.CustomViewPager;
import com.ws3dm.app.view.ICoverFlowAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
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

public class GamePCFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "GamePCFragment";
    private FgBaseRecyclerviewBinding mBinding;
    private CommonFragmentPagerAdapter fragmentPagerAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private View header;//存储顶部viewpager
    private boolean hasHead = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_base_recyclerview, container, false);
        header = LayoutInflater.from(mContext).inflate(R.layout.header_fg_game_pc, container, false);

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
                        initData();
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
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        GameService.Api service = retrofit.create(GameService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<GameDJhomeRespBean> call = service.djHome(body);
        call.enqueue(new Callback<GameDJhomeRespBean>() {
            @Override
            public void onResponse(Call<GameDJhomeRespBean> call, Response<GameDJhomeRespBean> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                initView(response.body());
            }

            @Override
            public void onFailure(Call<GameDJhomeRespBean> call, Throwable throwable) {
                mBinding.recyclerview.loadMoreError();
            }
        });
    }

    private void initData() {
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
        Call<GameDJhomeRespBean> call = service.djHome(body);
        call.enqueue(new Callback<GameDJhomeRespBean>() {
            @Override
            public void onResponse(Call<GameDJhomeRespBean> call, Response<GameDJhomeRespBean> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                initView(response.body());
            }

            @Override
            public void onFailure(Call<GameDJhomeRespBean> call, Throwable throwable) {
                mBinding.recyclerview.loadMoreError();
            }
        });

    }

    public void initView(GameDJhomeRespBean mData) {
        if (!hasHead) {
            mBinding.recyclerview.addHeaderView(header);
            hasHead = true;
        }
        initAdvise(mData.getData().getSlides());

        header.findViewById(R.id.img0).setOnClickListener(this);
        header.findViewById(R.id.img1).setOnClickListener(this);
        header.findViewById(R.id.img2).setOnClickListener(this);
        header.findViewById(R.id.img3).setOnClickListener(this);
        header.findViewById(R.id.img4).setOnClickListener(this);
        header.findViewById(R.id.tv_more_hot).setOnClickListener(this);
        header.findViewById(R.id.tv_more_sell).setOnClickListener(this);
        header.findViewById(R.id.tv_more_china).setOnClickListener(this);
        header.findViewById(R.id.ll_tabs).setVisibility(View.VISIBLE);

        initSell(mData.getData().getSalegame());
        initClass(mData.getData().getClassicgame());
        initChina(mData.getData().getChinesegame());

        mBinding.recyclerview.refreshComplete();
    }

    //    初始化编辑推荐
    private void initAdvise(List<GameBean> list) {
        header.findViewById(R.id.ll_advice).setVisibility(View.VISIBLE);
        if (list == null)
            return;

        final List<GameBean> mList = new ArrayList<>();
        mList.addAll(list);
        CoverFlowView vpAdvice = header.findViewById(R.id.vp_advice);
        vpAdvice.setVisibility(View.VISIBLE);
        vpAdvice.setAdapter(new AdviseAdapter(getActivity(), mList));

        // 给coverFlowView的TOPView 添加点击事件监听
        vpAdvice.setOnTopViewClickListener(new CoverFlowView.OnTopViewClickListener() {
            @Override
            public void onClick(int position, View itemView) {
                if (mList.get(position).getHttp() == 1) {
                    AdExposure.getInstance().putExposure(
                            mList.get(position).getId()
                            , String.valueOf(UUID.randomUUID())
                            , AppUtil.getVersionCode(mContext)
                            , "Android"
                            , SharedUtil.getSharedPreferencesData("device")
                            , String.valueOf(System.currentTimeMillis()));
                    Uri parse = Uri.parse(mList.get(position).getArcurl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, parse);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, GameHomeActivity.class);
                    Bundle bundle = new Bundle();
//				bundle.putSerializable("game",mList.get(position));
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
                    GameBean tempGame = mList.get(position);
                    bundle.putString("str_game", JSON.toJSONString(tempGame));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

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
    }

    //    初始化经典大作
    private void initClass(List<GameBean> list) {
        header.findViewById(R.id.line_class).setVisibility(View.VISIBLE);
        header.findViewById(R.id.ll_class).setVisibility(View.VISIBLE);
        list.add(new GameBean());//加一个空的放箭头
        final List<GameBean> mList = new ArrayList<>();
        mList.addAll(list);
        ViewPager vpClass = header.findViewById(R.id.vp_class);
        vpClass.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.element_margin_9));
        vpClass.setAdapter(new classAdapter(mContext, mList));
        vpClass.setOffscreenPageLimit(list.size());
        //viewpager设置页面改变的监听
        vpClass.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    //    初始化即将发售
    private void initSell(List<GameDJhomeRespBean.DataBean.SalegameBean> salegame) {
        if (fragments.size() > 0)
            fragments.clear();
        header.findViewById(R.id.line_sell).setVisibility(View.VISIBLE);
        header.findViewById(R.id.ll_sell).setVisibility(View.VISIBLE);
        final CustomViewPager vpSell = header.findViewById(R.id.vp_sell);
        for (int i = 0; i < salegame.size(); i++) {
            Fragment fragment = new MonthFragment(5, vpSell);//新闻列表
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            bundle.putString("pre_mon", salegame.get(i).getMonth() + "月");
            bundle.putString("next_mon", i < salegame.size() - 1 ? salegame.get(i + 1).getMonth() + "月" : "");
            bundle.putSerializable("monList", (Serializable) salegame.get(i).getList());
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }

        fragmentPagerAdapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), fragments);
        vpSell.setAdapter(fragmentPagerAdapter);
        vpSell.setOffscreenPageLimit(fragments.size());
        vpSell.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                vpSell.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                vpSell.resetHeight(0);
            }
        }, 1);
    }

    //    初始化汉化
    private void initChina(final List<GameBean> list) {
        header.findViewById(R.id.line_china).setVisibility(View.VISIBLE);
        header.findViewById(R.id.ll_china).setVisibility(View.VISIBLE);
        GlideUtil.loadImage(mContext, list.get(0).getLitpic(), (ImageView) header.findViewById(R.id.imgCover1));
        ((TextView) header.findViewById(R.id.name1)).setText(list.get(0).getTitle());
        ((TextView) header.findViewById(R.id.type1)).setText("类型：" + list.get(0).getType());
        String[] sy1 = list.get(0).getSystem().split("/");
        switch (sy1.length) {
            case 7:
            case 6:
            case 5:
                (header.findViewById(R.id.txt_label15)).setVisibility(View.VISIBLE);
            case 4:
                (header.findViewById(R.id.txt_label14)).setVisibility(View.VISIBLE);
                ((TextView) header.findViewById(R.id.txt_label14)).setText(sy1[3]);
            case 3:
                (header.findViewById(R.id.txt_label13)).setVisibility(View.VISIBLE);
                ((TextView) header.findViewById(R.id.txt_label13)).setText(sy1[2]);
            case 2:
                (header.findViewById(R.id.txt_label12)).setVisibility(View.VISIBLE);
                ((TextView) header.findViewById(R.id.txt_label12)).setText(sy1[1]);
            case 1:
                (header.findViewById(R.id.txt_label11)).setVisibility(View.VISIBLE);
                ((TextView) header.findViewById(R.id.txt_label11)).setText(sy1[0]);
                break;
        }
        ((TextView) header.findViewById(R.id.tag1)).setText("标签：" + list.get(0).getLabelString());
        ((TextView) header.findViewById(R.id.tv_score1)).setText(list.get(0).getScore() + "");

        header.findViewById(R.id.rl_game1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GameHomeActivity.class);
                Bundle bundle = new Bundle();
//				bundle.putSerializable("game", list.get(0));
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
                GameBean tempGame = list.get(0);
                bundle.putString("str_game", JSON.toJSONString(tempGame));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        GlideUtil.loadImage(mContext, list.get(1).getLitpic(), (ImageView) header.findViewById(R.id.imgCover2));
        ((TextView) header.findViewById(R.id.name2)).setText(list.get(1).getTitle());
        ((TextView) header.findViewById(R.id.type2)).setText("类型：" + list.get(1).getType());
        String[] sy2 = list.get(1).getSystem().split("/");
        switch (sy2.length) {
            case 7:
            case 6:
            case 5:
                header.findViewById(R.id.txt_label25).setVisibility(View.VISIBLE);
            case 4:
                header.findViewById(R.id.txt_label24).setVisibility(View.VISIBLE);
                ((TextView) header.findViewById(R.id.txt_label24)).setText(sy2[3]);
            case 3:
                header.findViewById(R.id.txt_label23).setVisibility(View.VISIBLE);
                ((TextView) header.findViewById(R.id.txt_label23)).setText(sy2[2]);
            case 2:
                header.findViewById(R.id.txt_label22).setVisibility(View.VISIBLE);
                ((TextView) header.findViewById(R.id.txt_label22)).setText(sy2[1]);
            case 1:
                header.findViewById(R.id.txt_label21).setVisibility(View.VISIBLE);
                ((TextView) header.findViewById(R.id.txt_label21)).setText(sy2[0]);
                break;
        }
        ((TextView) header.findViewById(R.id.tag2)).setText("标签：" + list.get(1).getLabelString());
        ((TextView) header.findViewById(R.id.tv_score2)).setText(list.get(1).getScore() + "");

        header.findViewById(R.id.rl_game2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GameHomeActivity.class);
                Bundle bundle = new Bundle();
//				bundle.putSerializable("game", list.get(1));
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
                GameBean tempGame = list.get(1);
                bundle.putString("str_game", JSON.toJSONString(tempGame));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        GlideUtil.loadImage(mContext, list.get(2).getLitpic(), (ImageView) header.findViewById(R.id.imgCover3));
        ((TextView) header.findViewById(R.id.name3)).setText(list.get(2).getTitle());
        ((TextView) header.findViewById(R.id.type3)).setText("类型：" + list.get(2).getType());
        String[] sy3 = list.get(2).getSystem().split("/");
        switch (sy3.length) {
            case 7:
            case 6:
            case 5:
                header.findViewById(R.id.txt_label35).setVisibility(View.VISIBLE);
            case 4:
                header.findViewById(R.id.txt_label34).setVisibility(View.VISIBLE);
                ((TextView) header.findViewById(R.id.txt_label34)).setText(sy3[3]);
            case 3:
                header.findViewById(R.id.txt_label33).setVisibility(View.VISIBLE);
                ((TextView) header.findViewById(R.id.txt_label33)).setText(sy3[2]);
            case 2:
                header.findViewById(R.id.txt_label32).setVisibility(View.VISIBLE);
                ((TextView) header.findViewById(R.id.txt_label32)).setText(sy3[1]);
            case 1:
                header.findViewById(R.id.txt_label31).setVisibility(View.VISIBLE);
                ((TextView) header.findViewById(R.id.txt_label31)).setText(sy3[0]);
                break;
        }
        ((TextView) header.findViewById(R.id.tag3)).setText("标签：" + list.get(2).getLabelString());
        ((TextView) header.findViewById(R.id.tv_score3)).setText(list.get(2).getScore() + "");

        header.findViewById(R.id.rl_game3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GameHomeActivity.class);
                Bundle bundle = new Bundle();
//				bundle.putSerializable("game", list.get(2));
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
                GameBean tempGame = list.get(2);
                bundle.putString("str_game", JSON.toJSONString(tempGame));
                intent.putExtras(bundle);
                startActivity(intent);
                bundle.clear();
            }
        });
    }

    private class AdviseAdapter implements ICoverFlowAdapter {
        List<GameBean> list;
        Context context;

        public AdviseAdapter(Context context, List<GameBean> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if (convertView == null) {
                holder = new Holder();
                convertView = View.inflate(context, R.layout.vp_game_advice, null);
                holder.img_game = (ImageView) convertView.findViewById(R.id.img_game);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tv_plat = (TextView) convertView.findViewById(R.id.tv_plat);
                holder.tv_score = (TextView) convertView.findViewById(R.id.tv_score);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            return convertView;
        }

        @Override
        public void getData(View view, int position) {
            Holder holder = (Holder) view.getTag();
            final GameBean mGameBean = list.get(position);
            GlideUtil.loadImage(mContext, mGameBean.getLitpic(), holder.img_game);

            holder.tv_name.setText(mGameBean.getTitle());
            holder.tv_time.setText("发售：" + TimeUtil.getTimeEN(mGameBean.getPubdate_at()));
            holder.tv_plat.setText("平台：" + mGameBean.getSystem());
            holder.tv_score.setText(mGameBean.getScore() + "");

            holder.tv_score.setText(mGameBean.getScore() + "");
        }

        public class Holder {
            ImageView img_game;
            TextView tv_name;
            TextView tv_time;
            TextView tv_plat;
            TextView tv_score;
        }
    }

    private class classAdapter extends PagerAdapter {
        List<GameBean> list;
        Context context;

        public classAdapter(Context context, List<GameBean> list) {
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
                final GameBean mGame = list.get(position);
                View view = View.inflate(mContext, R.layout.vp_game_hot, null);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GameHomeActivity.class);
                        Bundle bundle = new Bundle();
//						bundle.putSerializable("game",mGame);
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
                        bundle.putString("str_game", JSON.toJSONString(mGame));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                ImageView gameBg = (ImageView) view.findViewById(R.id.img_game);
                GlideUtil.loadImage(mContext, mGame.getLitpic(), gameBg);

                TextView tv = (TextView) view.findViewById(R.id.tv_name);
                tv.setText(mGame.getTitle());
                TextView type = (TextView) view.findViewById(R.id.tv_type);
                type.setText(mGame.getType());
                TextView plat = (TextView) view.findViewById(R.id.tv_plat);
                plat.setText(mGame.getSystem());
                container.addView(view);
                return view;
            } else {
                View more = View.inflate(mContext, R.layout.view_more, null);

                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GameRandListActivity.class);
                        startActivity(intent);
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


    @Override
    public void onClick(View view) {
        Intent intent;
        MobclickAgent.onEvent(mContext, "04");
        switch (view.getId()) {
            case R.id.img0://游戏库
                intent = new Intent(mContext, GameCategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_more_china://汉化更多
            case R.id.img1://汉化
                intent = new Intent(mContext, GameChineseActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_more_sell://即将发售更多
            case R.id.img2://发售
                intent = new Intent(mContext, GameSellActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_more_hot://经典更多
            case R.id.img3://排行
                intent = new Intent(mContext, GameRandListActivity.class);
                startActivity(intent);
                break;
            case R.id.img4://游戏标签
                intent = new Intent(mContext, GameLabelActivity.class);
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

    @Override
    public void onResume() {
        super.onResume();
//		mBinding.vpAdvice.gotoForward();//向后一页
//		obtainData();
    }
}
