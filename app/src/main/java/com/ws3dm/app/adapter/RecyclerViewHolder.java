package com.ws3dm.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws3dm.app.util.glide.GlideUtil;


/**
 * 一个RecyclerView通用的组件缓存类
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mViews;
    private Context mContext;

    private RecyclerViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        this.mViews = new SparseArray<View>();
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param mContext
     * @param parent
     * @param layoutId
     *
     * @return
     */
    public static RecyclerViewHolder get(Context mContext, ViewGroup parent, int layoutId) {
//        return new RecyclerViewHolder(mContext, LayoutInflater.from(mContext).inflate(layoutId, null, false));//match_parent属性失效  
        return new RecyclerViewHolder(mContext, LayoutInflater.from(mContext).inflate(layoutId, parent, false));
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     *
     * @return
     */
    public RecyclerViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
//        AutoUtils.autoSize(view);
        view.setText(text);
        return this;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = this.itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     *
     * @return
     */
    public RecyclerViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     *
     * @return
     */
    public RecyclerViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }


    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param url
     *
     * @return
     */
    public RecyclerViewHolder setImageByUrl(int viewId, String url) {
//        GlideUtils.loadImgFromUrl(mContext, url, (ImageView) getView(viewId));
        GlideUtil.loadImage(mContext, url, (ImageView) getView(viewId));
        return this;
    }
    public RecyclerViewHolder setImageByUrl(int viewId, String url,int radis) {
//        GlideUtils.loadImgFromUrl(mContext, url, (ImageView) getView(viewId));
        GlideUtil.loadRoundImage(mContext, url, (ImageView) getView(viewId),radis);
        return this;
    }

    public RecyclerViewHolder setImageByUrlNoScal(int viewId, String url) {
        GlideUtil.loadImage(mContext, url, (ImageView) getView(viewId));
        return this;
    }

    /**
     * 给某一个控件添加单击事件
     *
     * @param viewId
     * @param onClickListener
     *
     * @return
     */
    public RecyclerViewHolder setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        getView(viewId).setOnClickListener(onClickListener);
        return this;
    }


    /**
     * 给某一个控件添加长按事件
     *
     * @param viewId
     * @param onLongClickListener
     *
     * @return
     */
    public RecyclerViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener onLongClickListener) {
        getView(viewId).setOnLongClickListener(onLongClickListener);
        return this;
    }

}

