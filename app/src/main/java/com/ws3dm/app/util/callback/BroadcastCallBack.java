package com.ws3dm.app.util.callback;

import android.content.Context;
import android.content.Intent;

/**
 * 廣播回调接口
 * @author zhangh
 * @date 2015-09-02
 */
public interface BroadcastCallBack {
	
	public void callBackFunction(Context context, Intent intent);//回调方法

}
