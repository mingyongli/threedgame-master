package com.ws3dm.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ws3dm.app.activity.MainActivity;
import com.ws3dm.app.R;
import com.ws3dm.app.util.glide.GlideUtil;

/**
 * 引导页WelcomeFragmentGuide
 * @author zhangh
 *
 */
public class WelcomeFragmentGuide extends Fragment {

	/**
	 * mImageView:TODO（图片资源ID）
	 * 
	 * @since 1.0.0
	 */
	private int resID;
	/**
	 * 是否为结束页
	 */
	private boolean isEnd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View mView = inflater.inflate(R.layout.fg_welcome, null);
		isEnd=getArguments().getBoolean("isEnd");
		resID=getArguments().getInt("resID");

		if (resID != 0) {
			GlideUtil.loadImage(getContext(),resID,mView.findViewById(R.id.splash_bg));
			//mView.setBackgroundResource(resID);
		}
		if (isEnd) {
			TextView tv = (TextView) mView.findViewById(R.id.tv_welcome);
			tv.setVisibility(View.VISIBLE);
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
                    //跳至首页
					Intent intent = new Intent(getActivity(),MainActivity.class);
					intent.putExtra("isOnce", true);
					startActivity(intent);
					getActivity().finish();
				}
			});
		}
		return mView;
	}

	/**
	 * 创建一个新的实例 WelcomeFragmentGuide.
	 */
	public static final WelcomeFragmentGuide newInstance(int resID, boolean isEnd){
		WelcomeFragmentGuide fragment = new WelcomeFragmentGuide();
		Bundle bundle = new Bundle();
		bundle.putInt("resID",resID);
		bundle.putBoolean("isEnd", isEnd);
		fragment.setArguments(bundle);
		return fragment ;
	}

	public WelcomeFragmentGuide() {
	}

}
