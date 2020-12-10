package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.webkit.WebSettings;

import com.alibaba.fastjson.JSON;
import com.umeng.socialize.UMShareAPI;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.databinding.AcGameHomeBinding;
import com.ws3dm.app.fragment.FragmentGameComment;
import com.ws3dm.app.fragment.FragmentGameWeb;
import com.ws3dm.app.util.AppUtil;
import com.ws3dm.app.util.SharedUtil;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.view.NoScrollWebView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : DKjuan: 游戏详情首页，包含简单的游戏设置
 * <p>
 * Date :  2017/9/1  15:57
 */
public class GameHomeActivity extends BaseActivity{

	private AcGameHomeBinding mBinding;
	private GameBean game;
	private long firstTime = 0;
	private int position;
	public static ViewPager viewPager;
	private List<Fragment> fragments = new ArrayList<>();

	@Override
	protected void init() {
		mBinding=bindView(R.layout.ac_game_home);
		mBinding.setHandler(this);

		initView();
	}

	private void initView(){
//		if (getIntent().getSerializableExtra("game") != null)
//			game = (GameBean) getIntent().getSerializableExtra("game");
		String str_game=getIntent().getStringExtra("str_game");
		if (!StringUtil.isEmpty(str_game))//str_game
			game = JSON.parseObject(str_game,GameBean.class);

		initFragment();
	}

	//  初始化Fragment
	private void initFragment() {
		fragments.clear();
		for (int i = 0; i < 2; i++) {
			Fragment fragment;
			Bundle bundle = new Bundle();
			if (i == 0) {
				fragment = new FragmentGameWeb();
			} else {
				fragment = new FragmentGameComment();
			}
			bundle.putString("type", "GameHomeActivity");
			bundle.putSerializable("gameBean", game);
			fragment.setArguments(bundle);
			fragments.add(fragment);
		}
//		mBinding.txtTitle.setText(news.getTitle());
		mBinding.mViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
		mBinding.mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
		mBinding.mViewPager.setOffscreenPageLimit(0);
		viewPager=mBinding.mViewPager;
	}

	private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			mBinding.mViewPager.setCurrentItem(arg0);
			position = arg0;
			if (arg0 == 1) {
//				mBinding.imgRefresh.setVisibility(View.GONE);
//				mBinding.imgChangeTxt.setVisibility(View.GONE);
				mBinding.bgImgBack.setVisibility(View.INVISIBLE);
			} else {
//				mBinding.imgRefresh.setVisibility(View.VISIBLE);
//				mBinding.imgChangeTxt.setVisibility(View.VISIBLE);
				mBinding.bgImgBack.setVisibility(View.VISIBLE);
				AppUtil.closeKeyboard(mContext);
			}
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private class MyFragmentAdapter extends FragmentPagerAdapter {
		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}
		@Override
		public int getCount() {
			return fragments.size();
		}
	}

	public void clickHandler(View view) {
		switch (view.getId()) {
			// 返回
			case R.id.imgReturn:
				if (position == 0) {
					finish();
				} else{
					mBinding.mViewPager.setCurrentItem(0);
				}
				break;
			case R.id.imgRefresh:
//				ToastUtil.showToast(mContext,"页面已重新加载");
				((NoScrollWebView) fragments.get(0).getView().findViewById(R.id.mWebView)).reload();
				break;
			// 选择字体大小
			case R.id.imgChangeTxt:
				mBinding.txtBig.setTextColor(0xff555555);
				mBinding.txtSmall.setTextColor(0xff555555);
				mBinding.txtNormal.setTextColor(0xff555555);
				mBinding.imgBig.setImageResource(R.drawable.next);
				mBinding.imgSmall.setImageResource(R.drawable.next);
				mBinding.imgNormal.setImageResource(R.drawable.next);
				if (SharedUtil.getSharedPreferencesData("FontSize").equals("1")) {
					mBinding.txtSmall.setTextColor(0xffe8433c);
					mBinding.imgSmall.setImageResource(R.drawable.next_red);
				}
				if (SharedUtil.getSharedPreferencesData("FontSize").equals("0")) {
					mBinding.txtNormal.setTextColor(0xffe8433c);
					mBinding.imgNormal.setImageResource(R.drawable.next_red);
				}
				if (SharedUtil.getSharedPreferencesData("FontSize").equals("2")) {
					mBinding.txtBig.setTextColor(0xffe8433c);
					mBinding.imgBig.setImageResource(R.drawable.next_red);
				}
				mBinding.rlChangeTxt.setVisibility(View.VISIBLE);
				break;
			// 取消提示框
			case R.id.txtHide:
			case R.id.txtCancel:
				mBinding.rlChangeTxt.setVisibility(View.GONE);
				break;
			// 小字体
			case R.id.rlSmall:
			case R.id.txtSmall:
			case R.id.imgSmall:
				mBinding.rlChangeTxt.setVisibility(View.GONE);
				SharedUtil.setSharedPreferencesData("FontSize", "1");
				((NoScrollWebView) fragments.get(0).getView().findViewById(R.id.mWebView)).getSettings().setTextSize(WebSettings.TextSize.SMALLER);
				break;
			// 中字体
			case R.id.rlNormal:
			case R.id.txtNormal:
			case R.id.imgNormal:
				mBinding.rlChangeTxt.setVisibility(View.GONE);
				SharedUtil.setSharedPreferencesData("FontSize", "0");
				((NoScrollWebView) fragments.get(0).getView().findViewById(R.id.mWebView)).getSettings().setTextSize(WebSettings.TextSize.NORMAL);
				break;
			// 大字体
			case R.id.rlBig:
			case R.id.txtBig:
			case R.id.imgBig:
				mBinding.rlChangeTxt.setVisibility(View.GONE);
				SharedUtil.setSharedPreferencesData("FontSize", "2");
				((NoScrollWebView) fragments.get(0).getView().findViewById(R.id.mWebView)).getSettings().setTextSize(WebSettings.TextSize.LARGER);
				break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	// 手指向右滑动时的最小速度
	private static final int XSPEED_MIN = 6000;

	// 记录手指按下时的横坐标。
	private float xDown;

	// 记录手指移动时的横坐标。
	private float xMove;

	// 手指向右滑动时的最小距离
	private int XDISTANCE_MIN = 400;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				xDown = event.getRawX();
				break;
			case MotionEvent.ACTION_MOVE:
				xMove = event.getRawX();
				// 活动的距离
				int distanceX = (int) (xMove - xDown);
				// 获取顺时速度
				int xSpeed = getScrollVelocity();
				// 当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，返回到上一个activity
				if (distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
					//判断是否间隔800毫秒
					long secondTime = System.currentTimeMillis();
					if (position == 0&&secondTime - firstTime > 800) {                                         //如果两次按键时间间隔大于2秒，则不退出  
						finish();
					} else {                                                    //两次按键小于2秒时，退出应用  
						firstTime = secondTime;//更新firstTime
						mBinding.mViewPager.setCurrentItem(0);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
//				recycleVelocityTracker();
				break;
			default:
				break;
		}
		return super.dispatchTouchEvent(event);
	}

	// 用于计算手指滑动的速度。
	private VelocityTracker mVelocityTracker;

	/**
	 * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
	 *
	 * @param event
	 *
	 */
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * 回收VelocityTracker对象。
	 */
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	/**
	 * 获取手指在content界面滑动的速度。
	 *
	 * @return 滑动速度，以每秒钟移动了多少像素值为单位。
	 */
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (position == 0) {
				finish();
			} else{
				mBinding.mViewPager.setCurrentItem(0);
			}
			return false;
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(mContext).onActivityResult(requestCode, resultCode, data);
	}
}