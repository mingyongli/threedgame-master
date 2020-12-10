/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ws3dm.app.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbToastUtil.java 
 * 描述：Toast工具类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-07-02 下午11:52:13
 */

public class ToastUtil {
    
    /** 上下文. */
    private static Context mContext = null;
    
    /** 显示Toast. */
    public static final int SHOW_TOAST = 0;
    
    //用于立即显示的字符串显示，覆盖上一个字符串
    private static String oldMsg;
	protected static Toast toast = null;
	private static long oneTime = 0;
	private static long twoTime = 0;
    
    /**
	 * 主要Handler类，在线程中可用
	 * what：0.提示文本信息
	 */
	private static Handler baseHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SHOW_TOAST:
					showToast(mContext,msg.getData().getString("TEXT"));
					break;
				default:
					break;
			}
		}
	};
    
    /**
     * 描述：Toast提示文本.
     * @param text  文本
     */
	public static void showToast(Context context,String text) {
		//简单显示字符串
//		mContext = context;
//		if(!StringUtil.isEmpty(text)){
//			Toast.makeText(context,text, Toast.LENGTH_SHORT).show();
//		}
		
		if (toast == null) {
			/*
			 * 这种写法如果传入Activity的实例进来，将有可能会导致Activvity泄露 因为静态工具类的生存周期
			 */
			// toast =Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
			/* 这样的话，不管传递什么content进来，都只会引用全局唯一的Content，不会产生内存泄露 */
			try {
				toast = Toast.makeText(context.getApplicationContext(), text,Toast.LENGTH_SHORT);
				toast.show();
				oneTime = System.currentTimeMillis();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			twoTime = System.currentTimeMillis();
			if (text.equals(oldMsg)) {
				if (twoTime - oneTime > Toast.LENGTH_SHORT) {
					toast.show();
				}
			} else {
				oldMsg = text;
				toast.setText(text);
				toast.show();
			}
		}
		oneTime = twoTime;
		
	}
	
	/**
     * 描述：Toast提示文本.
     * @param resId  文本的资源ID
     */
	public static void showToast(Context context,int resId) {
		mContext = context;
		Toast.makeText(context,""+context.getResources().getText(resId), Toast.LENGTH_SHORT).show();
	}
    
    /**
	 * 描述：在线程中提示文本信息.
	 * @param resId 要提示的字符串资源ID，消息what值为0,
	 */
	public static void showToastInThread(Context context,int resId) {
		mContext = context;
		Message msg = baseHandler.obtainMessage(SHOW_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString("TEXT", context.getResources().getString(resId));
		msg.setData(bundle);
		baseHandler.sendMessage(msg);
	}
	
	/**
	 * 描述：在线程中提示文本信息.
	 * @param toast 消息what值为0
	 */
	public static void showToastInThread(Context context,String text) {
		mContext = context;
		Message msg = baseHandler.obtainMessage(SHOW_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString("TEXT", text);
		msg.setData(bundle);
		baseHandler.sendMessage(msg);
	}
    

}
