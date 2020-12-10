package com.ws3dm.app.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.RequiresApi;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Describution :游戏库界面item  adapter
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/21 11:47
 **/
public class NewsENTMultiAdapter extends MultiItemRecyclerAdapter<NewsBean> {
    private int mSlideLast = 0;
    private boolean mSlideRunning = true;
    private Handler mSlideMessager = null;
    private List<NewsBean> guessLikeList = new ArrayList<>();

    public NewsENTMultiAdapter(Context ctx, MultiItemTypeSupport<NewsBean> support) {
        super(ctx, support);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        RecyclerViewHolder holder;
        switch (viewType) {
            case NewsMultiItemTypeSupport.VIEW_TYPE_NOPIC:// 无图模式
                layoutId = mMultiItemTypeSupport.getLayoutId(0);
                holder = RecyclerViewHolder.get(mContext, parent, layoutId);
                break;
            case NewsMultiItemTypeSupport.VIEW_TYPE_ONE_RIGHT:// 右侧图片
                layoutId = mMultiItemTypeSupport.getLayoutId(1);
                holder = RecyclerViewHolder.get(mContext, parent, layoutId);
                break;
            case NewsMultiItemTypeSupport.VIEW_TYPE_MORE_PIC:// 多图模式
                layoutId = mMultiItemTypeSupport.getLayoutId(2);
                holder = RecyclerViewHolder.get(mContext, parent, layoutId);
                break;
            case NewsMultiItemTypeSupport.VIEW_TYPE_LIST:// 横向recyclerview，展示踩你喜欢
                layoutId = mMultiItemTypeSupport.getLayoutId(3);
                //holder = RecyclerViewHolder.get(mContext, parent, layoutId);
                holder = RecyclerViewHolder.get(mContext, parent, layoutId);
                break;
            default://右侧图片
                layoutId = mMultiItemTypeSupport.getLayoutId(1);
                holder = RecyclerViewHolder.get(mContext, parent, layoutId);
                break;
        }
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void bindData(RecyclerViewHolder holder, int position, final NewsBean item) {
        int type = item.getBodyimg() == null ? 3 : item.getBodyimg().size() == 3 ? 2 : 1;
        switch (type) {
            case 0: {
                holder.setText(R.id.txtTitle, item.getTitle());
                holder.setText(R.id.txtTime, TimeUtil.getTimeCN(item.getPubdate_at()));
            }
            break;
            case 1: {
                //holder.getView(R.id.ll_user).setVisibility(View.GONE);
                if (item.getUser() != null) {
                    GlideUtil.loadImage(mContext, item.getUser().avatarstr, holder.getView(R.id.img_head));
                    TextView textname = holder.getView(R.id.tv_name);
                    textname.setText(item.getUser().nickname);
                }
//				holder.setText(R.id.txtTitle, item.getTitle());
                holder.setText(R.id.tv_time, StringUtil.getShowType(item.getShowtype()));
                TextView tv_title = holder.getView(R.id.txtTitle);
                tv_title.setText(item.getTitle());
                tv_title.setTextColor(item.getHavesee() == 0 ? 0xff2a2a2a : 0xff999999);
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
                if (item.getUser() != null) {
                    GlideUtil.loadImage(mContext, item.getUser().avatarstr, holder.getView(R.id.img_head));
                    TextView textname = holder.getView(R.id.tv_name);
                    textname.setText(item.getUser().nickname);
                }
                holder.setText(R.id.txtComment, item.getTotal_ct() + "");
                holder.setText(R.id.tv_type, StringUtil.getShowType(item.getShowtype()));
                TextView tv_title = holder.getView(R.id.txtTitle);
                tv_title.setText(item.getTitle());
                tv_title.setTextColor(item.getHavesee() == 0 ? 0xff2a2a2a : 0xff999999);
                holder.setText(R.id.txtComment, item.getTotal_ct() + "");
                holder.setText(R.id.tv_time, TimeUtil.getFoolishTime2("" + item.getPubdate_at() + "000"));
//				holder.setImageByUrl(R.id.img_head,item.getUser().avatarstr);
//				GlideUtil.loadCircleImage(mContext,item.getUser().avatarstr,(ImageView)holder.getView(R.id.img_head));
                holder.setImageByUrl(R.id.imgArrayOne, item.getBodyimg().get(0));
                holder.setImageByUrl(R.id.imgArrayTwo, item.getBodyimg().get(1));
                holder.setImageByUrl(R.id.imgArrayThree, item.getBodyimg().get(2));
                if (item.getShowtype() == 10) {//视频
                    holder.getView(R.id.img_play).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.img_play).setVisibility(View.GONE);
                }
            }
            break;
            case 3: {

                // 横向recyclerview，展示踩你喜欢
//                RecyclerView rv_type = holder.getView(R.id.rv_type);
//                rv_type.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
//
//                final CommonRecyclerAdapter inAdapter = new CommonRecyclerAdapter<AvatarBean>(mContext, R.layout.item_typelist) {
//
//                    @Override
//                    public int getItemCount() {
//                        return mData.size();
//                    }
//
//                    @Override
//                    public void bindData(RecyclerViewHolder holder, int position, AvatarBean item) {
////                        Glide.with(mContext).load(item.getTypelist().get(position).getLitpic()), holder.getView(R.id.img_bg);
//                        GlideUtil.loadImage(mContext, item.getLitpic(), holder.getView(R.id.img_bg));
//                        holder.setText(R.id.title, item.getTitle());
//                        //if (position == item.getTypelist().size() - 1) {
//                        holder.getView(R.id.line_view).setVisibility(View.VISIBLE);
//                        holder.setText(R.id.time, TimeUtil.getFoolishTime(item.getPubdate_at()));
//                        // } else {
//                        //holder.getView(R.id.line_view).setVisibility(View.GONE);
//                        // }
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(mContext, GuessNewsListActivity.class);
//                                intent.putExtra("aid", item.getAid());
//                                mContext.startActivity(intent);
//                            }
//                        });
//                    }
//                };
//
//                rv_type.setAdapter(inAdapter);
//                inAdapter.clearAndAddList(item.getTypelist());
//                holder.getView(R.id.more_like).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent intent = new Intent(mContext, GuessLikeActivity.class);
//                        mContext.startActivity(intent);
//                    }
//                });
//            }
                break;
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
    }


    private void setSlideRunning(boolean enabled) {
        mSlideRunning = enabled;
    }


}