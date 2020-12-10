package com.ws3dm.app.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.databinding.AcGonglueListBinding;
import com.ws3dm.app.mvp.model.RespBean.NewsGLspecialRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsSpecilReslRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.glide.GlideRoundTransform;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 攻略所有列表，按字母分类
 * 
 * Author : DKjuan
 * 
 * Date : 2019/9/28 10:53
 **/
public class GonglueListActivity extends BaseActivity{

	private AcGonglueListBinding mBinding;
	private CommonRecyclerAdapter<NewsGLspecialRespBean.DataBean.ListGong> mAdapter;
	private BaseRecyclerAdapter<NewsBean> mSubRecyclerAdapter,resultAdapter;
	private RecyclerView mLabelRecyclerView;
	private BaseRecyclerAdapter<String> mLabelAdapter;
	private PopupWindow mPopupWindow;
	public List<NewsGLspecialRespBean.DataBean.ListGong> listData =new ArrayList<>();
	public List<NewsBean> itemResult =new ArrayList<>();
	private List<String> labels=new ArrayList<>();
	private String txt_cate="筛选";////▲  ▼
	private int totalSize,mPage,showtype;//17单机18手游19网游
	private Boolean onSelect;

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_gonglue_list);
		mBinding.setHandler(this);

		initView();
	}

	//界面初始化
	private void initView() {//17单机18手游19网游
		mPage = 1;
		for(char i='A';i<='Z';i++)
			labels.add(""+i);
		labels.add("#");
		
		mBinding.tvTitle.setText(getIntent().getStringExtra("title"));
		showtype=getIntent().getIntExtra("showtype",0);

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mAdapter = new CommonRecyclerAdapter<NewsGLspecialRespBean.DataBean.ListGong>(mContext, R.layout.adapter_category) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final NewsGLspecialRespBean.DataBean.ListGong item) {
				holder.setText(R.id.tv_title, item.getName());

				RecyclerView recyclerView = holder.getView(R.id.recycler_hot_cates);
				if(showtype==17){//17单机18手游19网游
					GridLayoutManager manager = new GridLayoutManager(mContext,3);
					recyclerView.setLayoutManager(manager);

					mSubRecyclerAdapter= new BaseRecyclerAdapter<NewsBean>(mContext, R.layout.item_gong_pc) {
						@Override
						public void bindData(RecyclerViewHolder holder, int position, NewsBean child) {
							holder.setImageByUrl(R.id.img_top,child.getLitpic());
							holder.setText(R.id.tv_title, child.getTitle());
						}
					};
				}else if(showtype==18){//17单机18手游19网游
					GridLayoutManager manager = new GridLayoutManager(mContext,3);
					recyclerView.setLayoutManager(manager);

					mSubRecyclerAdapter= new BaseRecyclerAdapter<NewsBean>(mContext, R.layout.item_gong_mg) {
						@Override
						public void bindData(RecyclerViewHolder holder, int position, NewsBean child) {
//							Glide.with(mContext).load(child.getLitpic()).dontAnimate().error(R.drawable.load_error)
//									// 设置高斯模糊,模糊程度(最大25)  缩放比例
//									.bitmapTransform(new BlurTransformation(mContext, 14, 3)).into((ImageView) holder.getView(R.id.mg_bg));
//							GlideUtil.loadRoundImage(mContext,child.getLitpic(), (ImageView) holder.getView(R.id.img_top),5);
							Glide.with(mContext).load(child.getLitpic())
									.placeholder(R.drawable.load_default)
									.error(R.drawable.load_error)
									.diskCacheStrategy(DiskCacheStrategy.SOURCE)
									.transform(new GlideRoundTransform(mContext, 5))
									.into((ImageView) holder.getView(R.id.img_top));
//							holder.setImageByUrl(R.id.img_top,child.getLitpic());
							holder.setText(R.id.tv_title, child.getTitle());
						}
					};
				}else{//17单机18手游19网游
					GridLayoutManager manager = new GridLayoutManager(mContext,2);
					recyclerView.setLayoutManager(manager);

					mSubRecyclerAdapter= new BaseRecyclerAdapter<NewsBean>(mContext, R.layout.item_gong_net) {
						@Override
						public void bindData(RecyclerViewHolder holder, int position, NewsBean child) {
							holder.setImageByUrl(R.id.img_top,child.getLitpic());
							holder.setText(R.id.tv_title, child.getTitle());
						}
					};
				}
				mSubRecyclerAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(View itemView, int posi) {
						jumpGonglue(item.getList().get(posi).getAid(),item.getList().get(posi).getShowtype(),item.getList().get(posi).getTitle());
					}
				});
				recyclerView.setAdapter(mSubRecyclerAdapter);
				mSubRecyclerAdapter.clearAndAddList(item.getList());
			}
		};

		mBinding.recyclerview.setAdapter(mAdapter);
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						mPage=1;
						obtainData();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
			}
		});
		mBinding.recyclerview.setRefreshing(true);

		//初始化popview
		mLabelAdapter = new BaseRecyclerAdapter<String>(mContext, R.layout.item_pop_label) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, String item) {
				onSelect=item.equals(txt_cate)?true:false;
				TextView textView=holder.getView(R.id.tv_name);
				if(onSelect){
					textView.setTextColor(getResources().getColor(R.color.white));
					textView.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_fillred));
				}else{
					textView.setTextColor(getResources().getColor(R.color.black_55));
					textView.setBackground(getResources().getDrawable(R.drawable.bg_roundrect_gray));
				}
				textView.setText(item);
			}
		};
		
		mLabelAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				txt_cate=mLabelAdapter.getDataByPosition(position);//▲  ▼
				mBinding.tvCate.setText(txt_cate+" ▼");
				mPage=1;
				clickLabel(txt_cate);
				mPopupWindow.dismiss();
			}
		});

		View contentViewCate = LayoutInflater.from(mContext).inflate(R.layout.pop_list_game, null);
		mLabelRecyclerView = (RecyclerView) contentViewCate.findViewById(R.id.recycler_view_cate);
		mLabelRecyclerView.setAdapter(mLabelAdapter);
		contentViewCate.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPopupWindow.dismiss();
			}
		});

		GridLayoutManager manager = new GridLayoutManager(mContext, 4);
		mLabelRecyclerView.setLayoutManager(manager);

		mPopupWindow = new PopupWindow(contentViewCate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setAnimationStyle(R.style.AnimBottom);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setFocusable(false); // pop 显示时，让外部 view 响应点击事件
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	private void showPopView() {
		//展示popview
		mBinding.tvCate.setText(txt_cate+" ▲");//▲  ▼
		mLabelAdapter.clearAndAddList(labels);

		if (Build.VERSION.SDK_INT >= 24) {//适配安卓7.0popview 显示位置不对
			Rect visibleFrame = new Rect();
			mBinding.recyclerview.getGlobalVisibleRect(visibleFrame);
			mPopupWindow.setHeight(visibleFrame.height());
			mPopupWindow.showAtLocation(mBinding.llTopbar, Gravity.TOP, visibleFrame.left,visibleFrame.top);
		}else{
			int[] location = new int[2];
			mBinding.llTopbar.getLocationInWindow(location);
			mPopupWindow.showAsDropDown(mBinding.llTopbar, Gravity.TOP, location[0], location[1] + mBinding.llTopbar.getHeight());
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:// 返回
				onBackPressed();
				break;
			case R.id.tv_cate:
				if(mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
					mBinding.tvCate.setText(txt_cate+" ▼");
				}else 
					showPopView();
				break;
			case R.id.ll_topbar:
			case R.id.rlHead://头部       
				mPopupWindow.dismiss();
				mBinding.tvCate.setText(txt_cate+" ▼");
				break;
		}
	}

	public void clickLabel(String txt){
		if(txt.equals(txt_cate)){
			mPage=1;
			if(showtype==17){//17单机18手游19网游
				GridLayoutManager manager = new GridLayoutManager(mContext,3);
				mBinding.recyclerview.setLayoutManager(manager);

				resultAdapter= new BaseRecyclerAdapter<NewsBean>(mContext, R.layout.item_gong_pc) {
					@Override
					public void bindData(RecyclerViewHolder holder, int position, NewsBean child) {
						holder.setImageByUrl(R.id.img_top,child.getLitpic());
						holder.setText(R.id.tv_title, child.getTitle());
					}
				};
			}else if(showtype==18){//17单机18手游19网游
				GridLayoutManager manager = new GridLayoutManager(mContext,3);
				mBinding.recyclerview.setLayoutManager(manager);

				resultAdapter= new BaseRecyclerAdapter<NewsBean>(mContext, R.layout.item_gong_mg) {
					@Override
					public void bindData(RecyclerViewHolder holder, int position, NewsBean child) {
//						Glide.with(mContext).load(child.getLitpic()).dontAnimate().error(R.drawable.load_error)
//								// 设置高斯模糊,模糊程度(最大25)  缩放比例
//								.bitmapTransform(new BlurTransformation(mContext, 14, 3)).into((ImageView) holder.getView(R.id.mg_bg));
//						holder.setImageByUrl(R.id.img_top,child.getLitpic());
						Glide.with(mContext).load(child.getLitpic())
								.placeholder(R.drawable.load_default)
								.error(R.drawable.load_error)
								.diskCacheStrategy(DiskCacheStrategy.SOURCE)
								.transform(new GlideRoundTransform(mContext, 5))
								.into((ImageView) holder.getView(R.id.img_top));
						holder.setText(R.id.tv_title, child.getTitle());
					}
				};
			}else{//17单机18手游19网游
				GridLayoutManager manager = new GridLayoutManager(mContext,2);
				mBinding.recyclerview.setLayoutManager(manager);

				resultAdapter= new BaseRecyclerAdapter<NewsBean>(mContext, R.layout.item_gong_net) {
					@Override
					public void bindData(RecyclerViewHolder holder, int position, NewsBean child) {
						holder.setImageByUrl(R.id.img_top,child.getLitpic());
						holder.setText(R.id.tv_title, child.getTitle());
					}
				};
			}
			resultAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(View itemView, int posi) {
					jumpGonglue(resultAdapter.getDataByPosition(posi).getAid(),resultAdapter.getDataByPosition(posi).getShowtype(),resultAdapter.getDataByPosition(posi).getTitle());
				}
			});
			mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
				@Override
				public void onRefresh() {
					new Handler().postDelayed(new Runnable(){
						public void run() {
							mPage=1;
							obtainItems();
						}
					}, 50);            //refresh data here
				}

				@Override
				public void onLoadMore() {
					new Handler().postDelayed(new Runnable(){
						public void run() {
							obtainItems();
						}
					}, 50);
				}
			});
			mBinding.recyclerview.setAdapter(resultAdapter);
		}
		
		obtainItems();
	}

	public void obtainData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+showtype+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("showtype", showtype);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//        mForumPresenter.getForumRank(obj.toString());
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		NewsService.Api service = retrofit.create(NewsService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<NewsGLspecialRespBean> call = service.getGLSpecial(body);
		call.enqueue(new Callback<NewsGLspecialRespBean>() {
			@Override
			public void onResponse(Call<NewsGLspecialRespBean> call, Response<NewsGLspecialRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<NewsGLspecialRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	private void initData(NewsGLspecialRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if(bean!=null&&bean.getData()!=null) {
			listData = bean.getData().getList();
		}
		if(mPage>1) {
			mAdapter.appendList(listData);
		}else {
			mAdapter.clearAndAddList(listData);
			mBinding.recyclerview.setNoMore(true);
		}
	}

	public void obtainItems(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= showtype+txt_cate+20+mPage+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("showtype", showtype);
			obj.put("name", txt_cate);
			obj.put("pagesize", 20);
			obj.put("page", mPage);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//        mForumPresenter.getForumRank(obj.toString());
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		NewsService.Api service = retrofit.create(NewsService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<NewsSpecilReslRespBean> call = service.getGLSpecialSEL(body);
		call.enqueue(new Callback<NewsSpecilReslRespBean>() {
			@Override
			public void onResponse(Call<NewsSpecilReslRespBean> call, Response<NewsSpecilReslRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initItems(response.body());
			}

			@Override
			public void onFailure(Call<NewsSpecilReslRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	public void jumpGonglue(int aid,int showtype,String title){
		GameBean game=new GameBean();
		game.setAid(aid);
		game.setShowtype(showtype);
		game.setTitle(title);

		Intent intent = new Intent(mContext, GameNewsActivity.class);
		intent.putExtra("tag", 1);//设置哪个标签选中
		Bundle bundle1 = new Bundle();
		bundle1.putSerializable("game", game);
		intent.putExtras(bundle1);
		startActivity(intent);
	}

	private void initItems(NewsSpecilReslRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if(bean!=null&&bean.getData()!=null) {
			itemResult = bean.getData().getList();
		}else{
			mBinding.recyclerview.setNoMore(true);
			return;
		}
		if(mPage>1) {
			resultAdapter.appendList(itemResult);
		}else {
			totalSize=bean.getData().getTotal();
			resultAdapter.clearAndAddList(itemResult);
		}
		if (totalSize==resultAdapter.getItemCount()) {
			mBinding.recyclerview.setNoMore(true);
		} else {
			mPage++;
		}
	}
}