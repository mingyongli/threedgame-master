package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.IdRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.GameNewsActivity;
import com.ws3dm.app.activity.GongLabelActivity;
import com.ws3dm.app.activity.GongTuJianActivity;
import com.ws3dm.app.activity.GonglueActivity;
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GllistBean;
import com.ws3dm.app.bean.GonglueBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.PiclistBean;
import com.ws3dm.app.databinding.FgGongMixBinding;
import com.ws3dm.app.mvp.model.RespBean.NewsSpeDetReslRespBean;
import com.ws3dm.app.mvp.model.RespBean.NewsSpecilReslRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.NewsService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;

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
 * Describution : 攻略复杂混合
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2019/10/9 14:04
 **/
public class FragmentGonglueMix extends BaseFragment {
	private FgGongMixBinding mBinding;
	private BaseRecyclerAdapter<GllistBean> mAdapter;
	private BaseRecyclerAdapter<NewsBean> newsAdapter;
	public List<NewsBean> listData = new ArrayList<NewsBean>();
	private int totalCount,showtype, aid = 0,mPage=1;//aid游戏新闻列表用到  mixType-17单机18手游19网游
	private String title;
	//	private NewsFile newsCollectFile;
	private View header;
	private boolean hasHead;
	private static ImageView imgSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_gong_mix, container, false);
		header = LayoutInflater.from(mContext).inflate(R.layout.header_gl_mix, container, false);

		initView();
		return mBinding.getRoot();
	}

	public void initView() {
//		newsCollectFile = new NewsFile(getActivity());
		hasHead=false;
		aid = getArguments().getInt("aid");
		showtype = getArguments().getInt("showtype");
		title = getArguments().getString("title");
		mBinding.tvAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GonglueActivity.class);
				intent.putExtra("aid", aid);
				intent.putExtra("showtype", showtype);
				intent.putExtra("title", title);
				intent.putExtra("isAll", 1);
				startActivity(intent);
			}
		});
		initSearch();

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);
		mBinding.recyclerview.setLoadingMoreEnabled(false);

//		//添加分割线
//		if (mListV3DividerItemDecoration == null) {
//			mListV3DividerItemDecoration = new DividerItemDecorationAdapter(mContext);
//		} else {
//			mBinding.recyclerview.removeItemDecoration(mListV3DividerItemDecoration);
//		}
//		mBinding.recyclerview.addItemDecoration(mListV3DividerItemDecoration);

		mAdapter = new BaseRecyclerAdapter<GllistBean>(mContext, R.layout.adapter_hot_cate) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final GllistBean item) {
				holder.setText(R.id.txt_cate_name, item.getName());
				if(item.getIs_more()==1){
					holder.getView(R.id.more).setVisibility(View.VISIBLE);
					holder.getView(R.id.more).setOnClickListener(new View.OnClickListener() {//最期待->发售  热门跳转排行
						@Override
						public void onClick(View view) {
							Intent intent = new Intent(mContext, GongLabelActivity.class);
							intent.putExtra("showtype", showtype);
							intent.putExtra("zq_id", aid);
							intent.putExtra("aid", item.getId());
							intent.putExtra("position", item.getPosition());
							intent.putExtra("title", item.getName());
							startActivity(intent);
						}
					});
				}else{
					holder.getView(R.id.more).setVisibility(View.GONE);
				}
				

				RecyclerView recyclerView = holder.getView(R.id.recycler_hot_cates);
				GridLayoutManager manager = new GridLayoutManager(mContext, 2);
				recyclerView.setLayoutManager(manager);

				final BaseRecyclerAdapter<GonglueBean> mSubRecyclerAdapter = new BaseRecyclerAdapter<GonglueBean>(mContext, R.layout.item_pop_label) {
					@Override
					public void bindData(RecyclerViewHolder holder, int position, GonglueBean child) {
						holder.setText(R.id.tv_name, child.getTitle());
					}
				};
				mSubRecyclerAdapter.setOnClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(View itemView, int posi) {
						GonglueBean bean =mSubRecyclerAdapter.getDataByPosition(posi);
						NewsBean news = new NewsBean();
						news.setTitle(bean.getTitle());
						news.setArcurl(bean.getArcurl());
						news.setWebviewurl(bean.getWebviewurl());
						news.setType("" + bean.getShowtype());
						Intent intent = new Intent(mContext, NewsActivity.class);
						intent.putExtra("isGongLue",1);
						Bundle bundle = new Bundle();
						bundle.putSerializable("newsBean", news);
						intent.putExtras(bundle);
						mContext.startActivity(intent);
					}
				});
				recyclerView.setAdapter(mSubRecyclerAdapter);
				mSubRecyclerAdapter.clearAndAddList(item.getList());
			}
		};
//		NewsMultiItemTypeSupport modelMultiItemTypeSupport = new NewsMultiItemTypeSupport();
//		mAdapter= new NewsMultiAdapter(mContext, modelMultiItemTypeSupport);
		mBinding.recyclerview.setAdapter(mAdapter);
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						obtainData();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						obtainAllData();
					}
				}, 50);
			}
		});

		mBinding.recyclerview.setRefreshing(true);
	}

	public void obtainData() {//获取游戏相关数据
		long time = System.currentTimeMillis();
		String validate = "" + aid + showtype + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("aid", aid);
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
		Call<NewsSpeDetReslRespBean> call = service.getGLSpecialDel(body);
		call.enqueue(new Callback<NewsSpeDetReslRespBean>() {
			@Override
			public void onResponse(Call<NewsSpeDetReslRespBean> call, Response<NewsSpeDetReslRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<NewsSpeDetReslRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	private void initData(NewsSpeDetReslRespBean bean) {
		if (bean.getData().getIs_fullgl()==1){
//			mBinding.recyclerview.setNoMore(true);
//			mBinding.tvNodate.setVisibility(View.VISIBLE);
			mBinding.llBottom.setVisibility(View.GONE);
			initListData(bean);
			return;
		}
//		处理图鉴
		if (bean.getData().getPiclist() != null && bean.getData().getPiclist().size() > 0) {
			final List<PiclistBean> mlist=bean.getData().getPiclist();
			XRecyclerView tujian_recyclerView=header.findViewById(R.id.tujian_recycleView);
			
			LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
			layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
			tujian_recyclerView.setLayoutManager(layoutManager);
			tujian_recyclerView.setRefreshing(false);
			tujian_recyclerView.setLoadingMoreEnabled(false);

			CommonRecyclerAdapter tujian_adapter = new CommonRecyclerAdapter<PiclistBean>(mContext, R.layout.adapter_hot_cate) {
				@Override
				public void bindData(RecyclerViewHolder holder, final int position, final PiclistBean item) {
					RecyclerView picRecyclerView = holder.getView(R.id.recycler_hot_cates);
					holder.setText(R.id.txt_cate_name,mlist.get(position).getName());
					if(mlist.get(position).getIs_more()==1){
						holder.getView(R.id.more).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(mContext, GongTuJianActivity.class);
								intent.putExtra("aid", item.getId());
								intent.putExtra("showtype", showtype);
								intent.putExtra("title", item.getName());
								startActivity(intent);
							}
						});
						holder.getView(R.id.more).setVisibility(View.VISIBLE);
					}else{
						holder.getView(R.id.more).setVisibility(View.VISIBLE);
					}
					
					GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
					layoutManager.setOrientation(GridLayoutManager.VERTICAL);
					picRecyclerView.setLayoutManager(layoutManager);
//		tujian_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.HORIZONTAL));
					final CommonRecyclerAdapter mPicAdapter = new CommonRecyclerAdapter<GonglueBean>(mContext, R.layout.item_mg) {
						@Override
						public void bindData(RecyclerViewHolder holder, int position, GonglueBean bean) {
							holder.setImageByUrl(R.id.img_top, bean.getLitpic());
							holder.setText(R.id.tv_title, bean.getTitle());
						}
					};
					mPicAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
						@Override
						public void onItemClick(View itemView, int posi) {
						GonglueBean bean =mlist.get(position).getList().get(posi);
						NewsBean news = new NewsBean();
						news.setTitle(bean.getTitle());
						news.setArcurl(bean.getArcurl());
						news.setWebviewurl(bean.getWebviewurl());
						news.setType("" + bean.getShowtype());
						Intent intent = new Intent(mContext, NewsActivity.class);
							intent.putExtra("isGongLue",1);
						Bundle bundle = new Bundle();
						bundle.putSerializable("newsBean", news);
						intent.putExtras(bundle);
						mContext.startActivity(intent);
						}
					});
					picRecyclerView.setAdapter(mPicAdapter);
					mPicAdapter.clearAndAddList(mlist.get(position).getList());
				}
			};
			tujian_recyclerView.setAdapter(tujian_adapter);
			tujian_adapter.clearAndAddList(bean.getData().getPiclist());
			
		}
		if(!hasHead){
			mBinding.recyclerview.addHeaderView(header);
			hasHead=true;
		}
		
//		处理攻略
		if (bean.getData().getGllist() != null)
			mAdapter.clearAndAddList(bean.getData().getGllist());

		mBinding.recyclerview.refreshComplete();
	}

	public void obtainAllData(){//获取所有攻略列表
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+showtype+aid+10+mPage+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("showtype", showtype);
			obj.put("aid", aid);
			obj.put("pagesize", 10);
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
		Call<NewsSpecilReslRespBean> call = service.getGLSpecialList(body);
		call.enqueue(new Callback<NewsSpecilReslRespBean>() {
			@Override
			public void onResponse(Call<NewsSpecilReslRespBean> call, Response<NewsSpecilReslRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initAllData(response.body());
			}

			@Override
			public void onFailure(Call<NewsSpecilReslRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	private void initAllData(NewsSpecilReslRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if(bean!=null&&bean.getData()!=null) {
			listData = bean.getData().getList();
		}
		if(mPage>1) {
			newsAdapter.appendList(listData);
		}else {
			newsAdapter.clearAndAddList(listData);
		}
		if(newsAdapter.getItemCount()==totalCount){
			mBinding.recyclerview.setNoMore(true);
		}else{
			mPage++;
		}
	}
	
	public static void showBut(){
		imgSearch.setVisibility(View.VISIBLE);
	}
	
	private void initSearch(){
		imgSearch=mBinding.imgSearch;
		imgSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imgSearch.setVisibility(View.GONE);
				GameNewsActivity.showClick();
			}
		});
	}
	
	private void initListData(NewsSpeDetReslRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if (bean != null && bean.getData() != null) {
			listData = bean.getData().getList();
		}
		totalCount=bean.getData().getTotal();
		newsAdapter = new BaseRecyclerAdapter<NewsBean>(mContext, R.layout.adapter_news_one_right_pic) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final NewsBean item) {
//				LinearLayout ll_user=holder.getView(R.id.ll_user);
				holder.setText(R.id.tv_time_game,TimeUtil.getTimeEN(item.getPubdate_at()));
				holder.getView(R.id.tv_time_game).setVisibility(View.VISIBLE);
				holder.getView(R.id.ll_user).setVisibility(View.GONE);
				holder.setText(R.id.txtTitle, item.getTitle());
				holder.setText(R.id.txtComment, item.getTotal_ct()+"");
				holder.setText(R.id.tv_time, TimeUtil.getFriendlyTime(""+item.getPubdate_at()+"000"));
				holder.setImageByUrl(R.id.imgArrayOne,item.getLitpic());
				if(item.getShowtype()==10){//视频
					holder.getView(R.id.img_play).setVisibility(View.VISIBLE);
				}else{
					holder.getView(R.id.img_play).setVisibility(View.GONE);
				}
			}
		};
		newsAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				newsAdapter.getDataByPosition(position).setHavesee(1);
				NewsBean news = newsAdapter.getDataByPosition(position);
				news.setSeeDate(TimeUtil.dateDayNow());
//				newsCollectFile.addHistory(news);
				Intent intent = new Intent(mContext, NewsActivity.class);
				intent.putExtra("isGongLue",1);
				Bundle bundle = new Bundle();
				bundle.putSerializable("newsBean",news);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		mBinding.recyclerview.setAdapter(newsAdapter);
		mBinding.recyclerview.setLoadingMoreEnabled(true);
		newsAdapter.clearAndAddList(listData);
		
		if (newsAdapter.getItemCount()== totalCount) {
			mBinding.recyclerview.setNoMore(true);
		} else {
			mPage++;
		}
	}
}