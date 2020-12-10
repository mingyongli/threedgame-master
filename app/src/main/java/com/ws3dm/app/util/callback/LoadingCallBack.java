package com.ws3dm.app.util.callback;

public interface LoadingCallBack {
	void loading();//加载中
	void loadFile();//加载失败
	void loadSuccess();//加载完成
	void NoMore();//没有更多
	void changeStatus(int size);//更改加载状态
}
