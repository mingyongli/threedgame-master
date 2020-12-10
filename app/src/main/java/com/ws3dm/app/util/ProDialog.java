package com.ws3dm.app.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ws3dm.app.R;

/**
 * 自定义加载动画
 * @author zhangh
 * @date 2015-08-27
 *
 */
@SuppressLint("InflateParams")
public class ProDialog {
	static String tip="上传中...";
	/**
	 * 得到自定义的progressDialog
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("ResourceAsColor")
	public static Dialog createLoadingDialog(Context context,String tip) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dg_loading, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
//		// main.xml中的ImageView
//		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
//		// 加载动画
//		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//				context, R.anim.loading_anim);
//		// 使用ImageView显示动画
//		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(tip);// 设置加载信息
//		tipTextView.setTextColor(R.color.white);

		Dialog loadingDialog = new Dialog(context, R.style.progressDialog);// 创建自定义样式dialog
//		loadingDialog.setCanceledOnTouchOutside(true);      
		loadingDialog.setCancelable(true);// 可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;

	}
	
	public static Dialog createLoadingDialog(Context context,boolean onClick) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dg_loading, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
//		// main.xml中的ImageView
//		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
//		// 加载动画
//		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//				context, R.anim.loading_anim);
//		// 使用ImageView显示动画
//		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(tip);// 设置加载信息
//		tipTextView.setTextColor(R.color.white);

		Dialog loadingDialog = new Dialog(context, R.style.progressDialog);// 创建自定义样式dialog
		loadingDialog.setCanceledOnTouchOutside(false);      
		loadingDialog.setCancelable(onClick);// 可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;

	}
	
	public static void setString(String txt){
		tip=txt;
	}
}
