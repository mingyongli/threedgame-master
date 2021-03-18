package com.ws3dm.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.MGDetailActivity;
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.SlidesBean;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.network.AdExposure;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.SharedUtil;

import java.util.List;
import java.util.UUID;

/**
 * Describution :顶部广告栏适配器
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/9/26 15:47
 **/
public class ViewPagerAdapter extends PagerAdapter {
    private List<SlidesBean> mBannerList;
    //	private List<ImageView> viewList;
//	private List<ImageView> imageViewList=new ArrayList<ImageView>();
    private Context mContext;

    // 构造方法
    public ViewPagerAdapter(List<SlidesBean> bannerList, Context context) {
        this.mBannerList = bannerList;
        this.mContext = context;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {

    }

    @Override
    public void finishUpdate(View arg0) {

    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//		container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (position < 0) {
            position = mBannerList.size() + position;
        }
        final int p = position % mBannerList.size();
        View imgLayout = View.inflate(mContext, R.layout.item_img_vp, null);
        final ImageView view = (ImageView) imgLayout.findViewById(R.id.imageView);
//		final ImageView view=new ImageView(mContext);
//		view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setPadding(0, 0, 0, 0);
//		GlideUtil.loadRoundImage(mContext,mBannerList.get(p).getLitpic(),view,5);
        Glide.with(mContext)
                .load(mBannerList.get(p).getLitpic())
//				.placeholder(R.drawable.load_default)
//				.error(R.drawable.load_error)
//				.transform(new GlideRoundTransform(mContext, 5))//20：圆角半径
                .into(view);

        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        container.addView(view);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {//:1新闻2攻略3游戏4手游5杂谈6评测7原创8安利9礼包10视频11专栏
                MobclickAgent.onEvent(mContext, "01");
                Intent intent = null;
                SlidesBean slidesBean = mBannerList.get(p);
                if (slidesBean.getHttp() == 1) {
                    AdExposure.getInstance().putExposure(
                            slidesBean.getId()
                            , SharedUtil.getSharedPreferencesData("uuid")
                            , AppUtil.getVersionCode(mContext)
                            , "Android"
                            , SharedUtil.getSharedPreferencesData("device")
                            , String.valueOf(System.currentTimeMillis()));
                    Uri webPage = Uri.parse(slidesBean.getWebviewurl());
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webPage);
                    mContext.startActivity(webIntent);
                } else {
                    switch (slidesBean.getShowtype()) {
                        case "1":
                        case "2":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "10":
                            NewsBean news = new NewsBean();
                            news.setTitle(slidesBean.getTitle());
                            news.setArcurl(slidesBean.getArcurl());
                            news.setWebviewurl(slidesBean.getWebviewurl());
                            news.setLitpic(slidesBean.getLitpic());
                            news.setType(slidesBean.getShowtype());
                            intent = new Intent(mContext, NewsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("newsBean", news);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                            break;
                        case "4"://手游
                            SoftGameBean soft = new SoftGameBean();
                            soft.setTitle(slidesBean.getTitle());
                            soft.setArcurl(slidesBean.getArcurl());
                            soft.setType(slidesBean.getShowtype());
                            soft.setAid(slidesBean.getAid());
                            intent = new Intent(mContext, MGDetailActivity.class);
                            Bundle bundlesoft = new Bundle();
                            bundlesoft.putSerializable("mSoftGameBean", soft);
                            intent.putExtras(bundlesoft);
                            mContext.startActivity(intent);
                            break;
                    }
                }
            }
        });
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
