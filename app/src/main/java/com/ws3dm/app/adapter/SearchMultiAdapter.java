package com.ws3dm.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws3dm.app.R;
import com.ws3dm.app.activity.MGDetailActivity;
import com.ws3dm.app.bean.SearchBean;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.glide.GlideUtil;

/**
 * Describution :搜索界面item  adapter
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/21 11:47
 **/
public class SearchMultiAdapter extends MultiItemRecyclerAdapter<SearchBean> {
	private int mSlideLast = 0;
	private boolean mSlideRunning = true;
	private Handler mSlideMessager = null;
	private Context mcontext=null;

	public SearchMultiAdapter(Context ctx, MultiItemTypeSupport<SearchBean> support) {
		super(ctx, support);
		this.mcontext=ctx;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		int                layoutId;
		RecyclerViewHolder holder;
		switch (viewType) {
			case 3://专题
				layoutId = mMultiItemTypeSupport.getLayoutId(3);
				holder   = RecyclerViewHolder.get(mContext, parent, layoutId);
				break;
			case 10://视频
				layoutId = mMultiItemTypeSupport.getLayoutId(10);
				holder   = RecyclerViewHolder.get(mContext, parent, layoutId);
				break;
			case 4://手游
				layoutId = mMultiItemTypeSupport.getLayoutId(4);
				holder   = RecyclerViewHolder.get(mContext, parent, layoutId);
				break;
			default://新闻
				layoutId = mMultiItemTypeSupport.getLayoutId(1);
				holder   = RecyclerViewHolder.get(mContext, parent, layoutId);
				break;
		}
		return holder;
	}

	@Override
	public void bindData(RecyclerViewHolder holder, int position, final SearchBean item) {
		switch (item.getShowtype()) {
			case 3://专题
				holder.setImageByUrl(R.id.imgCover,item.getLitpic());
				holder.setText(R.id.tv_score, item.getScore()+"");
				holder.setText(R.id.txtName, item.getTitle());
				holder.setText(R.id.tv_type, "类型：" +item.getType());
				holder.setText(R.id.txtTime, "发售："+ (item.getPubdate_at()==0?"未知":TimeUtil.getFormatTimeSimple(item.getPubdate_at())));
				holder.setText(R.id.tv_label, "标签：" +item.getLabelString());

				holder.getView(R.id.txt_label1).setVisibility(View.GONE);
				holder.getView(R.id.txt_label2).setVisibility(View.GONE);
				holder.getView(R.id.txt_label3).setVisibility(View.GONE);
				holder.getView(R.id.txt_label4).setVisibility(View.GONE);
				holder.getView(R.id.txt_label5).setVisibility(View.GONE);
				if(!StringUtil.isEmpty(item.getSystem())){
					String[] sy=item.getSystem().split("/");
					switch (sy.length>5?5:sy.length) {
						case 5:
							holder.getView(R.id.txt_label5).setVisibility(View.VISIBLE);
						case 4:
							holder.getView(R.id.txt_label4).setVisibility(View.VISIBLE);
							holder.setText(R.id.txt_label4,sy[3]);
						case 3:
							holder.getView(R.id.txt_label3).setVisibility(View.VISIBLE);
							holder.setText(R.id.txt_label3,sy[2]);
						case 2:
							holder.getView(R.id.txt_label2).setVisibility(View.VISIBLE);
							holder.setText(R.id.txt_label2,sy[1]);
						case 1:
							holder.getView(R.id.txt_label1).setVisibility(View.VISIBLE);
							holder.setText(R.id.txt_label1,sy[0]);
							break;
					}
				}
				break;
			case 10://视频
				ImageView imgCover = holder.getView(R.id.imgCover);
				GlideUtil.loadImage(mContext,item.getLitpic(),imgCover,R.drawable.video_loading, R.drawable.video_error);

//				GlideUtil.loadCircleImage(mContext,item.getUser().avatarstr,(ImageView)holder.getView(R.id.img_head));
//				holder.setText(R.id.tv_name, item.getUser().nickname);
				holder.setText(R.id.tv_time,TimeUtil.getFriendlyTime(""+item.getPubdate_at()+"000"));
				holder.setText(R.id.tv_num, item.getTotal_ct()+"");
				holder.setText(R.id.tv_title, item.getTitle());
				break;
			case 4://手游
//				GlideUtil.loadRoundImage(mContext,item.getLitpic(), (ImageView) holder.getView(R.id.imgCover),5);
//				holder.setText(R.id.tv_title,item.getTitle());
//				holder.setText(R.id.tv_data,"v"+item.getSoft_ver()+" | "+item.getSoft_size());
//				holder.setText(R.id.tv_info,item.getDesc());
//				holder.getView(R.id.bt_download).setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						SoftGameBean soft=new SoftGameBean();
//						soft.setLitpic(item.getLitpic());
//						soft.setDownurl(item.getDownurl());
//						soft.setTitle(item.getTitle());
//						soft.setArcurl(item.getArcurl());
//						soft.setDesc(item.getDesc());
//						soft.setAid(item.getAid());
//						Intent intent = new Intent(mContext, MGDetailActivity.class);
//						Bundle bundle = new Bundle();
//						bundle.putSerializable("mSoftGameBean",soft);
//						intent.putExtras(bundle);
//						mContext.startActivity(intent);
////						DownloadUtil.down(mContext,item.getDownurl(),item.getTitle()+"|"+item.getLitpic());
//					}
//				});
				break;
			default://新闻
				holder.getView(R.id.ll_user).setVisibility(View.GONE);
				holder.setText(R.id.txtTitle, item.getTitle());
				holder.setText(R.id.txtComment, item.getTotal_ct()+"");
				TextView tv_time=holder.getView(R.id.tv_time_game);
				tv_time.setText(TimeUtil.getFriendlyTime(""+item.getPubdate_at()+"000"));
				tv_time.setCompoundDrawables(null, null, null, null);
				tv_time.setVisibility(View.VISIBLE);
				holder.getView(R.id.img_time_game).setVisibility(View.VISIBLE);
//				holder.setText(R.id.tv_name, item.getUser().nickname);
////				holder.setImageByUrl(R.id.img_head,item.getUser().avatarstr);
//				GlideUtil.loadCircleImage(mContext,item.getUser().avatarstr,(ImageView)holder.getView(R.id.img_head));
				holder.setImageByUrl(R.id.imgArrayOne,item.getLitpic());
		}
	}
}
