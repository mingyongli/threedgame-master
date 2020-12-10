package com.ws3dm.app.activity;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.TabAdapter;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.databinding.AcGameVideoBinding;
import com.ws3dm.app.fragment.FragmentGameImage;
import com.ws3dm.app.fragment.FragmentVideo;
import com.ws3dm.app.util.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Describution : 游戏图片和视频
 * 
 * Author : DKjuan
 * 
 * Date : 2019/6/28 17:18
 **/
public class GameVideoActivity extends BaseActivity{

	private AcGameVideoBinding mBinding;
	private GameBean game;
	private int position,tag;//"tag"设置哪个选中
	private static TabAdapter mTabAdapter;
	private List<String> tabs = new ArrayList<>();//tablayout的标签数组
	private List<Fragment> fragments = new ArrayList<>();
	private String title[] = {"视频", "图片"};

	@Override
	protected void init() {
		mBinding=bindView(R.layout.ac_game_video);
		mBinding.setHandler(this);

		initView();
	}

	private void initView(){
//		tag=getIntent().getIntExtra("tag",0);
		if (getIntent().getSerializableExtra("game") != null)
			game = (GameBean) getIntent().getSerializableExtra("game");
//		处理图片列表中第一张小图问题
		List<String> imgs=game.getImgs();
		if (game.getShowtype() == 10) {//第一张是视频图片
			imgs.remove(0);
		}
		mBinding.txtTitle.setText(game.getTitle());
		
		tabs= Arrays.asList(title);

		Fragment gameVideoFragment=new FragmentVideo();//游戏视频
		Bundle bundleVideo = new Bundle();
		bundleVideo.putInt("aid",game.getAid());
		bundleVideo.putString("type","GameVideoActivity");
		bundleVideo.putString("dataType","3");//dataType 1,新闻  2.攻略  3.视频
		gameVideoFragment.setArguments(bundleVideo);
		fragments.add(gameVideoFragment);

		Fragment gameNewsFragment=new FragmentGameImage();//游戏图片
		Bundle bundleNew = new Bundle();
		bundleNew.putStringArrayList("images",(ArrayList)imgs);
		gameNewsFragment.setArguments(bundleNew);
		fragments.add(gameNewsFragment);
		//设置TabLayout的模式
		mBinding.mTabLayout.setTabMode(TabLayout.MODE_FIXED);

//		//设置分割线
//		LinearLayout linearLayout = (LinearLayout) mBinding.mTabLayout.getChildAt(0);
//		linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//		linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
//				R.drawable.divider)); //设置分割线的样式
//		linearLayout.setDividerPadding(dip2px(10)); //设置分割线间隔
		mTabAdapter=new TabAdapter(getSupportFragmentManager(),fragments,tabs);
		mBinding.mViewPager.setAdapter(mTabAdapter);
		mBinding.mViewPager.setOffscreenPageLimit(tabs.size());
		mBinding.mViewPager.setCurrentItem(tag);
		mBinding.mTabLayout.setupWithViewPager(mBinding.mViewPager);

		mBinding.mTabLayout.post(new Runnable() {
			@Override
			public void run() {
				AppUtil.setIndicator(mContext,mBinding.mTabLayout,25);
			}
		});
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
		}
	}

//	// 手指向右滑动时的最小速度
//	private static final int XSPEED_MIN = 1500;
//
//	// 记录手指按下时的横坐标。
//	private float xDown;
//
//	// 记录手指移动时的横坐标。
//	private float xMove;
//
//	// 手指向右滑动时的最小距离
//	private int XDISTANCE_MIN = 320;
//
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent event) {
//		createVelocityTracker(event);
//		switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				xDown = event.getRawX();
//				break;
//			case MotionEvent.ACTION_MOVE:
//				xMove = event.getRawX();
//				// 活动的距离
//				int distanceX = (int) (xMove - xDown);
//				// 获取顺时速度
//				int xSpeed = getScrollVelocity();
//				// 当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，返回到上一个activity
//				if (distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
//					//判断是否间隔800毫秒
//					long secondTime = System.currentTimeMillis();
//					if (position == 0&&secondTime - firstTime > 800) {                                         //如果两次按键时间间隔大于2秒，则不退出  
//						finish();
//					} else {                                                    //两次按键小于2秒时，退出应用  
//						firstTime = secondTime;//更新firstTime
//						mBinding.mViewPager.setCurrentItem(0);
//					}
//				}
//				break;
//			case MotionEvent.ACTION_UP:
////				recycleVelocityTracker();
//				break;
//			default:
//				break;
//		}
//		return super.dispatchTouchEvent(event);
//	}
//
//	// 用于计算手指滑动的速度。
//	private VelocityTracker mVelocityTracker;
//
//	/**
//	 * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
//	 *
//	 * @param event
//	 *
//	 */
//	private void createVelocityTracker(MotionEvent event) {
//		if (mVelocityTracker == null) {
//			mVelocityTracker = VelocityTracker.obtain();
//		}
//		mVelocityTracker.addMovement(event);
//	}
//
//	/**
//	 * 回收VelocityTracker对象。
//	 */
//	private void recycleVelocityTracker() {
//		mVelocityTracker.recycle();
//		mVelocityTracker = null;
//	}
//
//	/**
//	 * 获取手指在content界面滑动的速度。
//	 *
//	 * @return 滑动速度，以每秒钟移动了多少像素值为单位。
//	 */
//	private int getScrollVelocity() {
//		mVelocityTracker.computeCurrentVelocity(1000);
//		int velocity = (int) mVelocityTracker.getXVelocity();
//		return Math.abs(velocity);
//	}
//	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//			if (position == 0) {
//				finish();
//			} else{
//				mBinding.mViewPager.setCurrentItem(0);
//			}
//			return false;
//		}else {
//			return super.onKeyDown(keyCode, event);
//		}
//	}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		UMShareAPI.get(mContext).onActivityResult(requestCode, resultCode, data);
//	}
}