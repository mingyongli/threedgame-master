package com.ws3dm.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.TopTabDetailBean;
import com.ws3dm.app.sqlite.NewsFile;
import com.ws3dm.app.util.TimeUtil;

public class HomePageOtherAdapter extends MultiItemRecyclerAdapter<TopTabDetailBean.DataBean.ListBean> {
    public HomePageOtherAdapter(Context ctx, MultiItemTypeSupport<TopTabDetailBean.DataBean.ListBean> multiItemTypeSupport) {
        super(ctx, multiItemTypeSupport);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        RecyclerViewHolder holder;
        switch (viewType) {
            case HomePageOtherSupport.VIEW_TYPE_NOPIC:// 无图模式
                layoutId = mMultiItemTypeSupport.getLayoutId(0);
                holder = RecyclerViewHolder.get(mContext, parent, layoutId);
                break;
            case HomePageOtherSupport.VIEW_TYPE_ONE_RIGHT:// 右侧图片
                layoutId = mMultiItemTypeSupport.getLayoutId(1);
                holder = RecyclerViewHolder.get(mContext, parent, layoutId);
                break;
            case HomePageOtherSupport.VIEW_TYPE_MORE_PIC:// 多图模式
                layoutId = mMultiItemTypeSupport.getLayoutId(2);
                holder = RecyclerViewHolder.get(mContext, parent, layoutId);
                break;
            case HomePageOtherSupport.VIEW_TYPE_LIST:// 横向recyclerview，展示踩你喜欢
                layoutId = mMultiItemTypeSupport.getLayoutId(3);
                holder = RecyclerViewHolder.get(mContext, parent, layoutId);
                break;
            default://右侧图片
                layoutId = mMultiItemTypeSupport.getLayoutId(1);
                holder = RecyclerViewHolder.get(mContext, parent, layoutId);
                break;
        }
        return holder;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, TopTabDetailBean.DataBean.ListBean item) {
        int type = item.getBodyimg() == null ? 3 : item.getBodyimg().size() == 3 ? 2 : 1;
        NewsBean bean = new NewsBean();
        switch (type) {
            case 0: {
                holder.setText(R.id.txtTitle, item.getTitle());
                holder.setText(R.id.txtTime, TimeUtil.getTimeCN(item.getPubdate_at()));
            }
            break;
            case 1: {
                holder.getView(R.id.ll_user).setVisibility(View.GONE);
//				holder.setText(R.id.txtTitle, item.getTitle());
                TextView tv_title = holder.getView(R.id.txtTitle);
                tv_title.setText(item.getTitle());
                holder.setText(R.id.txtComment, item.getTotal_ct() + "");
                holder.setText(R.id.tv_time_game, TimeUtil.getFoolishTime2(item.getPubdate_at() + "000"));
                holder.getView(R.id.tv_time_game).setVisibility(View.VISIBLE);
//				holder.getView(R.id.img_time_game).setVisibility(View.VISIBLE);
//				holder.setText(R.id.tv_name, item.getUser().nickname);
//				GlideUtil.loadCircleImage(mContext,item.getUser().avatarstr,(ImageView)holder.getView(R.id.img_head));
                holder.setImageByUrl(R.id.imgArrayOne, item.getLitpic());
                if (item.getShowtype() == 10) {//视频
                    holder.getView(R.id.img_play).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.img_play).setVisibility(View.GONE);
                }
            }
            break;
            case 2: {
//				holder.setText(R.id.txtTitle, item.getTitle());
                holder.getView(R.id.ll_user).setVisibility(View.GONE);
                TextView tv_title = holder.getView(R.id.txtTitle);
                tv_title.setText(item.getTitle());
                holder.setText(R.id.txtComment, item.getTotal_ct() + "");
                holder.setText(R.id.tv_time, TimeUtil.getFoolishTime2("" + item.getPubdate_at() + "000"));
//				holder.setImageByUrl(R.id.img_head,item.getUser().avatarstr);
//				GlideUtil.loadCircleImage(mContext,item.getUser().avatarstr,(ImageView)holder.getView(R.id.img_head));
                holder.setImageByUrl(R.id.imgArrayOne, item.getBodyimg().get(0).toString());
                holder.setImageByUrl(R.id.imgArrayTwo, item.getBodyimg().get(1).toString());
                holder.setImageByUrl(R.id.imgArrayThree, item.getBodyimg().get(2).toString());
                if (item.getShowtype() == 10) {//视频
                    holder.getView(R.id.img_play).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.img_play).setVisibility(View.GONE);
                }
            }
            break;
        }
        bean.setArcurl(item.getArcurl());
        bean.setAid(item.getAid());
        bean.setDetail_title(item.getTitle());
        bean.setLitpic(item.getLitpic());
        bean.setWebviewurl(item.getWebviewurl());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext,"03");
                NewsFile newsCollectFile = new NewsFile(mContext);
                NewsBean newsBean = new NewsBean();
                newsBean.setWebviewurl(item.getWebviewurl());
                newsBean.setArcurl(item.getArcurl());
                newsBean.setSeeDate(TimeUtil.dateDayNow());
                newsBean.setType(item.getType());
                newsBean.setTitle(item.getTitle());
                newsCollectFile.addHistory(newsBean);
                Intent intent = new Intent(mContext, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsBean", bean);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }
}
