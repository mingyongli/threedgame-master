package com.ws3dm.app.adapter;

/**
 * Created by lixiangdong on 2017/5/10.
 */

public interface MultiItemTypeSupport<T> {

    public abstract int getLayoutId(int itemType);

    public abstract int getItemViewType(int position, T t);
}