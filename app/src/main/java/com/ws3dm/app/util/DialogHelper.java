package com.ws3dm.app.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.ws3dm.app.util.callback.CallBack;
import com.ws3dm.app.R;

/**
 * dialog帮助类，帮助创建dialog提示框
 * 
 * @author zhangh
 * @date 2015-08-27
 * 
 */
public class DialogHelper {
	/**
	 * 对话框是否显示
	 */
	private boolean isOnShow=false;
	/**
	 * 布尔值表示点击的是'确定'还是'取消'按钮
	 */
	private boolean isConfirm;
	/**
	 * 如果中间是可编辑的就可以获取中间内容
	 */
	private String contents;
	/**
	 * 上下文环境
	 */
	private Context context;
	/**
	 * 回调接口
	 */
	private static CallBack callBack;
	/**
	 * 确定按钮
	 */
	private TextView confirmButton;
	/**
	 * 取消按钮
	 */
	private TextView cancelButton;

	private boolean onBack = false;

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public DialogHelper(Context context, CallBack callBack) {
		this.context = context;
		this.isConfirm = false;
		this.callBack = callBack;
	}

	/**
	 * 构造函数 监听返回键
	 * 
	 * @param context
	 */
	public DialogHelper(Context context, CallBack callBack, boolean onBack) {
		this.context = context;
		this.isConfirm = false;
		this.onBack = onBack;
		this.callBack = callBack;
	}

	public boolean isOnShow() {
		return isOnShow;
	}
	
	public boolean isConfirm() {
		return isConfirm;
	}

	public String getContent() {
		return contents;
	}

	/**
	 * 弹出提示框
	 * 
	 * @param title
	 *            要显示的抬头标题
	 * @param content
	 *            提示内容
	 * @param isSingle
	 *            单按钮还是双按钮：false是双，true是单
	 */

	@SuppressLint("InflateParams")
	public void showDialog(String title, String content, boolean isSingle) {
		int width = ScreenUtil.getScreenWidth(context);
		LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.dg_hint_layout, null);
		final Dialog dialog = new AlertDialog.Builder(context).create();
		dialog.setCanceledOnTouchOutside(true);
		TextView hintTitle = (TextView) layout.findViewById(R.id.tv_hint_title);
		final EditText hintName = (EditText) layout
				.findViewById(R.id.tv_hint_text);
		confirmButton = (TextView) layout.findViewById(R.id.tv_confirm);

		TextView seperateLine = (TextView) layout
				.findViewById(R.id.tv_separate_line);
		cancelButton = (TextView) layout.findViewById(R.id.tv_cancel);

		isOnShow=true;
		// 如果是单按键，则取消消失按钮
		if (isSingle) {
			cancelButton.setVisibility(View.GONE);
			confirmButton.setBackgroundResource(R.drawable.dialog_btn_click);
			seperateLine.setVisibility(View.GONE);
		}
		
		dialog.setOnDismissListener(new Dialog.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				isOnShow=false;
				callBack.callBackFunction();
			}
		});
		// 如果不是可输入，则设置不可编辑及去掉自带背景
		// if(!isInput){
		hintName.setBackgroundResource(R.color.transparent);// 无背景
		hintName.setEnabled(false);// 不可编辑
		// }else{
		//
		// }
		hintTitle.setText(title);
		hintName.setText(content);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.show();
		// 设置参数
		// LinearLayout.LayoutParams prams = new LinearLayout.LayoutParams(
		// (int) (width * 0.6), LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams prams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.getWindow().setContentView(layout, prams);
		// dialog.getWindow().setContentView(layout);

		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& event.getRepeatCount() == 0 && onBack) {
					System.exit(0);
				}
				return false;
			}
		});
		/*
		 * 确认按钮监听
		 */
		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isConfirm = true;
				contents = hintName.getText().toString();
				dialog.cancel();
//				callBack.callBackFunction();
			}
		});
		/*
		 * 取消按钮监听
		 */
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isConfirm = false;
				dialog.cancel();
				callBack.callBackFunction();
			}
		});

	}

	// 给两个按钮添加显示内容
	public void setTextContent(String content, String contentTwo) {
		confirmButton.setText(content);
		cancelButton.setText(contentTwo);
	}

	public static PopupWindow createPopupWindow(final Activity context, View v,
			int animStyle) {
		PopupWindow pop = new PopupWindow(v,
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT,true);
		pop.setAnimationStyle(animStyle);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(false);
		pop.setTouchable(true);
		pop.setOutsideTouchable(true);
		pop.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
//				WindowManager.LayoutParams params = context.getWindow()
//						.getAttributes();
//				params.alpha = 1f;
//				context.getWindow().setAttributes(params);
			}
		});
		return pop;
	}
	
	public static PopupWindow createPopupWindow(final Activity context, View v,
			int animStyle , CallBack callBacks) {
		PopupWindow pop = new PopupWindow(v,
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		pop.setAnimationStyle(animStyle);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setTouchable(true);
		pop.setOutsideTouchable(true);
		callBack = callBacks;
		return pop;
	}
	
	
	public static PopupWindow createPopupWindow(final Activity context, View v,
			int animStyle , OnDismissListener listener) {
		PopupWindow pop = new PopupWindow(v,
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		pop.setAnimationStyle(animStyle);
        ColorDrawable dw = new ColorDrawable(0xb0000000);  //实例化一个ColorDrawable颜色为半透明  
		pop.setBackgroundDrawable(dw);//设置SelectPicPopupWindow弹出窗体的背景  
		pop.setFocusable(true);
		pop.setTouchable(true);
		pop.setOutsideTouchable(true);
		pop.setOnDismissListener(listener);
		return pop;
	}
	
	
	public static Dialog CreateDialog(Context context,View v){
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.setView(v);
		return dialog;
	}
	
	public static void showPop(Activity activity,View viewParent,PopupWindow popupWindow,int gravity,int x,int y){
		popupWindow.showAtLocation(viewParent, Gravity.BOTTOM, x, y);
		WindowManager.LayoutParams params = activity.getWindow().getAttributes();
		params.alpha = 0.45f;
		activity.getWindow().setAttributes(params);
	}
}
