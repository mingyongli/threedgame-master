package com.ws3dm.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ws3dm.app.R;
import com.ws3dm.app.activity.MGCategoryListActivity;
import com.ws3dm.app.activity.MGDetailActivity;
import com.ws3dm.app.bean.MGListBean;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.mvp.model.RespBean.MGTypeRespBean;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.glide.GlideUtil;

/**
 * Describution :游戏库界面item  adapter
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/21 11:47
 **/
public class MGMultiAdapter extends MultiItemRecyclerAdapter<MGListBean> {
	private int mSlideLast = 0;
	private boolean mSlideRunning = true;
	private Handler mSlideMessager = null;
	private Context mcontext=null;

	public MGMultiAdapter(Context ctx, MultiItemTypeSupport<MGListBean> support) {
		super(ctx, support);
		this.mcontext=ctx;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		int                layoutId;
		RecyclerViewHolder holder;
		switch (viewType) {
			case 0:// 无图模式
				layoutId = mMultiItemTypeSupport.getLayoutId(0);
				holder   = RecyclerViewHolder.get(mContext, parent, layoutId);
				break;
			case 1:// 顶部图片
				layoutId = mMultiItemTypeSupport.getLayoutId(1);
				holder   = RecyclerViewHolder.get(mContext, parent, layoutId);
				break;
			default://顶部图片
				layoutId = mMultiItemTypeSupport.getLayoutId(1);
				holder   = RecyclerViewHolder.get(mContext, parent, layoutId);
				break;
		}
		return holder;
	}

	@Override
	public void bindData(RecyclerViewHolder holder, int position, final MGListBean item) {
		switch (item.getImgnum()) {
			case 0:
				holder.setText(R.id.txt_cate_name,item.getType());
				if(StringUtil.isEmpty(item.getBigpic())){
					holder.getView(R.id.img_title).setVisibility(View.GONE);
				}else{
					holder.setImageByUrl(R.id.img_title,item.getBigpic());
					holder.getView(R.id.img_title).setVisibility(View.VISIBLE);
				}
				holder.getView(R.id.img_title).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						MGTypeRespBean.DataBean.ListBean typeItem=new MGTypeRespBean.DataBean.ListBean();
						typeItem.setType(item.getType());
						typeItem.setTypeid(item.getTypeid());
						typeItem.setLabels(item.getLabels());
						Intent intent = new Intent(mContext, MGCategoryListActivity.class);
						intent.putExtra("currentPosi",0);
						Bundle bundle = new Bundle();
						bundle.putSerializable("categoryData", typeItem);
						intent.putExtras(bundle);
						mContext.startActivity(intent);
					}
				});
				
				RecyclerView recyclerViewHorizon = holder.getView(R.id.recycler_hot_cates);
				LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
				layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
				recyclerViewHorizon.setLayoutManager(layoutManager);
				final BaseRecyclerAdapter<SoftGameBean> mSubHorizonAdapter=new BaseRecyclerAdapter<SoftGameBean>(mContext, R.layout.item_game_horizon) {
					@Override
					public void bindData(RecyclerViewHolder holder, int position, final SoftGameBean item) {
//						holder.setImageByUrl(R.id.imgCover,item.getLitpic());
						GlideUtil.loadRoundImage(mContext,item.getLitpic(), (ImageView) holder.getView(R.id.imgCover),5);
						holder.setText(R.id.tv_title,item.getTitle());
						holder.setText(R.id.tv_data,"v"+item.getSoft_ver()+" | "+item.getSoft_size());
						holder.setText(R.id.tv_info,item.getDesc());
						holder.getView(R.id.bt_download).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								Intent intent = new Intent(mContext, MGDetailActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("mSoftGameBean", item);
								intent.putExtras(bundle);
								mContext.startActivity(intent);
//								DownloadUtil.down(mContext,item.getDownurl(),item.getTitle()+"|"+item.getLitpic());
							}
						});
					}
				};
				recyclerViewHorizon.setAdapter(mSubHorizonAdapter);
				mSubHorizonAdapter.setOnClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(View itemView, int position) {
						Intent intent = new Intent(mContext, MGDetailActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("mSoftGameBean", mSubHorizonAdapter.getDataByPosition(position));
						intent.putExtras(bundle);
						mContext.startActivity(intent);
					}
				});
				mSubHorizonAdapter.clearAndAddList(item.getSoftlist());
			break;
			case 1:
				holder.setText(R.id.txt_cate_name,item.getType());
				RecyclerView recyclerView = holder.getView(R.id.recycler_hot_cates);
				GridLayoutManager manager = new GridLayoutManager(mContext, 4);
				recyclerView.setLayoutManager(manager);
				final BaseRecyclerAdapter<SoftGameBean> mSubVerticalAdapter=new BaseRecyclerAdapter<SoftGameBean>(mContext, R.layout.item_game_vertical) {
					@Override
					public void bindData(RecyclerViewHolder holder, int position, final SoftGameBean gameItem) {
//						holder.setImageByUrl(R.id.imgCover,item.getLitpic());
						GlideUtil.loadRoundImage(mContext,gameItem.getLitpic(), (ImageView) holder.getView(R.id.imgCover),5);
						holder.setText(R.id.txtName,gameItem.getTitle());
						holder.setText(R.id.tv_size,gameItem.getSoft_size());
						if(item.getShow()==1){
							holder.getView(R.id.bt_download).setVisibility(View.GONE);
						}else{
							holder.getView(R.id.bt_download).setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
//								DownloadUtil.down(mContext,item.getDownurl(),item.getTitle()+"|"+item.getLitpic());
									Intent intent = new Intent(mContext, MGDetailActivity.class);
									Bundle bundle = new Bundle();
									bundle.putSerializable("mSoftGameBean", gameItem);
									intent.putExtras(bundle);
									mContext.startActivity(intent);
								}
							});
						}
					}
				};
				recyclerView.setAdapter(mSubVerticalAdapter);
				mSubVerticalAdapter.setOnClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(View itemView, int position) {
						Intent intent = new Intent(mContext, MGDetailActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("mSoftGameBean", mSubVerticalAdapter.getDataByPosition(position));
						intent.putExtras(bundle);
						mContext.startActivity(intent);
					}
				});
				mSubVerticalAdapter.clearAndAddList(item.getSoftlist());
			break;
//			case "2": {
//				holder.setText(R.id.txtTitle, item.getTitle());
//				holder.setText(R.id.txtTime, TimeUtil.getTimeCN(item.getPubdate_at()));
//				if(item.getLitpic()!=null){
//					switch (item.getLitpic().size()) {
//						case 3:
//							holder.setImageByUrl(R.id.imgArrayThree,item.getLitpic().get(2).get(0));
//						case 2:
//							holder.setImageByUrl(R.id.imgArrayTwo,item.getLitpic().get(1).get(0));
//						case 1:
//							holder.setImageByUrl(R.id.imgArrayOne,item.getLitpic().get(0).get(0));
//							break;
//						default:
//							break;
//					}
//				}
//			}
//			break;
//			case "3": {
//				holder.setText(R.id.txtTitle, item.getTitle());
//				holder.setText(R.id.txtTime, TimeUtil.getTimeCN(item.getPubdate_at()));
//				holder.setImageByUrl(R.id.imgArrayOne,item.getLitpic()==null?"":item.getLitpic().get(0).get(0));
//			}
//			break;
//			case VIEW_TYPE_PAGES: {
//				// 顶部轮播图
//
//				int n = item.getList().size();
//				if (n < 1) {
//					break;
//				}
//				final LinearLayout points = ((LinearLayout) holder.getView(R.id.point_group));
//				if (points.getChildCount() > 0) {
//					points.removeAllViews();
//				}
//				mSlideLast = 0;
//				mSlideImageList = new ArrayList<ImageView>();
//				for (int i = 0; i < n; i++) {
//					ImageView imageView = new ImageView(mContext);
//					GlideUtils.loadImgFromUrl(mContext, item.getList().get(i).getCover(), imageView);
//					imageView.setTag(item.getList().get(i));
//					mSlideImageList.add(imageView);
//
//					// 指示小点
//					ImageView                 point  = new ImageView(mContext);
//					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
//					params.rightMargin = 20;
//					params.bottomMargin = 10;
//					point.setLayoutParams(params);
//					point.setBackgroundResource(R.drawable.point_bg);
//					if (i == 0) {
//						// 设置第焦点在第一张上
//						point.setBackgroundResource(R.drawable.point_bg_focus);
//						point.setEnabled(true);
//					} else {
//						point.setEnabled(false);
//					}
//					points.addView(point);
//				}
//
//				mSlideViewPager = (ViewPager) holder.getView(R.id.top_slide);
//				mSlideViewPager.setAdapter(new ImagePagerAdapter(mSlideImageList));
//				mSlideViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % mSlideImageList.size()));
//				mSlideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//					@Override
//					public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//						// 页面正在滑动时间回调
//					}
//
//					@Override
//					public void onPageSelected(int position) {
//						// 页面切换后调用， position是新的页面位置
//						// 循环播放
//						position %= mSlideImageList.size();
//						// 把当前点设置为true,将上一个点设为false；并设置point_group图标
//						points.getChildAt(position).setEnabled(true);
//						points.getChildAt(position).setBackgroundResource(R.drawable.point_bg_focus);//设置聚焦时的图标样式
//						points.getChildAt(mSlideLast).setEnabled(false);
//						points.getChildAt(mSlideLast).setBackgroundResource(R.drawable.point_bg);//上一张恢复原有图标
//						mSlideLast = position;
//					}
//
//					@Override
//					public void onPageScrollStateChanged(int state) {
//						// 当pageView 状态发生改变的时候，回调
//					}
//				});
//
//				mSlideViewPager.setOnTouchListener(new View.OnTouchListener() {
//					private float x;
//					private boolean mIsMove = false;
//
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						switch (event.getAction()) {
//							case MotionEvent.ACTION_DOWN:
//								mIsMove = false;
//								x = event.getX();
//								break;
//							case MotionEvent.ACTION_MOVE:
//								float diff = event.getX() - x;
//								if (diff > 100) {
//									// 向向滑动
//									mSlideViewPager.setCurrentItem(mSlideViewPager.getCurrentItem() + 1);
//								} else {
//									// 向左滑动
//									mSlideViewPager.setCurrentItem(mSlideViewPager.getCurrentItem() - 1);
//								}
//								mIsMove = true;
//								break;
//							case MotionEvent.ACTION_UP:
//								if (mIsMove) {
//									break;
//								}
//								ImagePagerAdapter adapter = (ImagePagerAdapter) mSlideViewPager.getAdapter();
//								ImageView image = adapter.getCurrentImageView();
//								BookInfoBean info = (BookInfoBean) image.getTag();
//								ActivityUtils.startBookDetailActivity(mContext, info.getId());
//								break;
//							default:
//								break;
//						}
//						return false;
//					}
//				});
//
//				if (mSlideImageList.size() < 2) {
//					points.setVisibility(View.GONE);
//					// 如果只有一张图，就不要滚动播放了
//					break;
//				}
//
//				if (mSlideMessager == null) {
//					// 发消息让轮播图动起来
//					mSlideMessager = new Handler() {
//						public void handleMessage(android.os.Message msg) {
//							if (mSlideRunning) {
//								// 执行滑动到下一个页面
//								mSlideViewPager.setCurrentItem(mSlideViewPager.getCurrentItem() + 1);
//								// 在发一个handler延时
//								mSlideMessager.sendEmptyMessageDelayed(0, 5000);
//							}
//						}
//					};
//					mSlideMessager.sendEmptyMessageDelayed(0, 3500);
//				}
//			}
//			break;
		}
		// 更换主题
//		if (SharedUtil.getSharedPreferencesData("isNight").equals("0")) {
//			((TextView)holder.getView(R.id.txtTitle)).setTextColor(0xff555555);
//			((TextView)holder.getView(R.id.txtTime)).setTextColor(0xff9c9c9c);
//			((LinearLayout)holder.getView(R.id.llNews)).setBackgroundColor(0xffffffff);
//			((TextView)holder.getView(R.id.txtLine)).setBackgroundColor(0xffe7e7e7);
//		} else {
//			((TextView)holder.getView(R.id.txtTitle)).setTextColor(0xff9c9c9c);
//			((TextView)holder.getView(R.id.txtTime)).setTextColor(0xff555555);
//			((LinearLayout)holder.getView(R.id.llNews)).setBackgroundColor(0xff2a2a2a);
//			((TextView)holder.getView(R.id.txtLine)).setBackgroundColor(0xff666666);
//		}
	}

	private void setSlideRunning(boolean enabled) {
		mSlideRunning = enabled;
	}

}
