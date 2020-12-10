package com.ws3dm.app.util.Transformer;

import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2019/6/21  10:32
 * 
 * 卡片飞出效果
 * 
 * 使用
 *  mViewPager.setPageTransformer(true,new RotationPageTransformer());
    mViewPager.setOffscreenPageLimit(3); //预加载个数
    mViewPager.setPageMargin(30); //用于设置两个Page之间的距离
 
 
 
    page.setTranslationX(mAdapter.dp2px(-60));//这里设置位移，实现中间的pager盖住两边pager的效果
 */
public class CardTransformer implements ViewPager.PageTransformer {
	private int mOffset = 60;

	@Override
	public void transformPage(View page, float position) {

		if (position <= 0) {
			page.setRotation(45 * position);
			page.setTranslationX((page.getWidth() / 2 * position));
		} else {
			//移动X轴坐标，使得卡片在同一坐标
			page.setTranslationX(-position * page.getWidth());
			//缩放卡片并调整位置
			float scale = (page.getWidth() - mOffset * position) / page.getWidth();
			page.setScaleX(scale);
			page.setScaleY(scale);
			//移动Y轴坐标
			page.setTranslationY(position * mOffset);
		}


	}
}