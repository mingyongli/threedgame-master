package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.GameHomeActivity;
import com.ws3dm.app.activity.NewsActivity;
import com.ws3dm.app.activity.OriginalActivity;
import com.ws3dm.app.activity.SearchActivity;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.OriginalBean;
import com.ws3dm.app.bean.SearchBean;
import com.ws3dm.app.databinding.FgBaseRecyclerviewBinding;
import com.ws3dm.app.mvp.model.RespBean.NewSearchRespBean;
import com.ws3dm.app.mvvm.view.activity.SectionPageActivity;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.UserService;
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
 * Describution :搜索fragment
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2018/1/6 14:05
 **/
public class SearchFragment extends BaseFragment {
	private BaseRecyclerAdapter<SearchBean> mAdapter;
	public List<SearchBean> listData = new ArrayList<SearchBean>();
	private FgBaseRecyclerviewBinding mBinding;
	private int mPage = 1, type,totalCount;//1单机专区2新闻3攻略4论坛
	private String mWord;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_base_recyclerview, container, false);
//        mBinding.MyUtilbar.setTitle(getResources().getString(R.string.person));
//        activity.setSupportActionBar(mBinding.MyUtilbar);

		initView();
		return mBinding.getRoot();
	}

	public void initView() {
		type = getArguments().getInt("type");

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);

//		//添加分割线
//		if (mListV3DividerItemDecoration == null) {
//			mListV3DividerItemDecoration = new DividerItemDecorationAdapter(mContext);
//		} else {
//			mBinding.recyclerview.removeItemDecoration(mListV3DividerItemDecoration);
//		}
//		mBinding.recyclerview.addItemDecoration(mListV3DividerItemDecoration);

		switch (type) {//1单机专区2新闻3攻略4论坛
			case 1:
				mAdapter = new BaseRecyclerAdapter<SearchBean>(mContext, R.layout.adapter_game) {
					@Override
					public void bindData(RecyclerViewHolder holder, final int position, final SearchBean game) {
						holder.setImageByUrl(R.id.imgCover, game.getLitpic());
						holder.setText(R.id.tv_score, game.getScore() + "");
						holder.setText(R.id.txtName, game.getTitle());
						holder.setText(R.id.tv_type, "类型：" + game.getType());
						holder.setText(R.id.txtTime, "发售：" + (game.getPubdate_at() == 0 ? "未知" : TimeUtil.getFormatTimeSimple(game.getPubdate_at())));
						holder.setText(R.id.tv_label, "标签：" + game.getLabelString());

						holder.getView(R.id.txt_label1).setVisibility(View.GONE);
						holder.getView(R.id.txt_label2).setVisibility(View.GONE);
						holder.getView(R.id.txt_label3).setVisibility(View.GONE);
						holder.getView(R.id.txt_label4).setVisibility(View.GONE);
						holder.getView(R.id.txt_label5).setVisibility(View.GONE);
						String[] sy = game.getSystem().split("/");
						switch (sy.length > 5 ? 5 : sy.length) {
							case 5:
								holder.getView(R.id.txt_label5).setVisibility(View.VISIBLE);
							case 4:
								holder.getView(R.id.txt_label4).setVisibility(View.VISIBLE);
								holder.setText(R.id.txt_label4, sy[3]);
							case 3:
								holder.getView(R.id.txt_label3).setVisibility(View.VISIBLE);
								holder.setText(R.id.txt_label3, sy[2]);
							case 2:
								holder.getView(R.id.txt_label2).setVisibility(View.VISIBLE);
								holder.setText(R.id.txt_label2, sy[1]);
							case 1:
								holder.getView(R.id.txt_label1).setVisibility(View.VISIBLE);
								holder.setText(R.id.txt_label1, sy[0]);
								break;
						}
					}
				};
				break;
			case 2:
			case 3:
				mAdapter = new BaseRecyclerAdapter<SearchBean>(mContext, R.layout.adapter_news_one_right_pic) {
					@Override
					public void bindData(RecyclerViewHolder holder, final int position, final SearchBean item) {
						holder.getView(R.id.ll_user).setVisibility(View.GONE);
						TextView tv_title = holder.getView(R.id.txtTitle);
						tv_title.setText(item.getTitle());
						holder.setText(R.id.txtComment, item.getTotal_ct() + "");
						holder.setText(R.id.tv_time_game, TimeUtil.getFoolishTime2("" + item.getPubdate_at() + "000"));
						holder.getView(R.id.tv_time_game).setVisibility(View.VISIBLE);
						//				holder.getView(R.id.img_time_game).setVisibility(View.VISIBLE);
						//				holder.setImageByUrl(R.id.img_head,item.getUser().avatarstr);
						holder.setImageByUrl(R.id.imgArrayOne, item.getLitpic());
					}
				};
				break;
			case 4:
				mAdapter = new BaseRecyclerAdapter<SearchBean>(mContext, R.layout.adapter_forum_rank) {
					@Override
					public void bindData(RecyclerViewHolder holder, final int position, final SearchBean forumBean) {
						holder.setImageByUrl(R.id.imgArrayOne, forumBean.getLitpic());
						holder.setText(R.id.tv_title, forumBean.getTitle());
						holder.setText(R.id.tv_readnum, "今日：" + forumBean.getTodayposts());
						holder.setText(R.id.tv_title, forumBean.getTitle());
						holder.setText(R.id.tv_title, forumBean.getTitle());
					}
				};
				break;
			default:
				break;
		}
		mBinding.recyclerview.setAdapter(mAdapter);
		mAdapter.setOnClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
				Intent intent;
				SearchBean clickBean = mAdapter.getDataByPosition(position);
				switch (type) {
					case 1:
						GameBean game = new GameBean();
						game.setAid(clickBean.getAid());
						game.setArcurl(clickBean.getArcurl());
						intent = new Intent(mContext, GameHomeActivity.class);
						Bundle bundleGame = new Bundle();
						bundleGame.putString("str_game", JSON.toJSONString(game));
						intent.putExtras(bundleGame);
						startActivity(intent);
						break;
					case 2:
					case 3:
						if(clickBean.getShowtype()==6||clickBean.getShowtype()==7){
							OriginalBean originalBean = new OriginalBean();
							originalBean.setTitle(clickBean.getTitle());
							originalBean.setArcurl(clickBean.getArcurl());
							originalBean.setWebviewurl(clickBean.getWebviewurl());
							originalBean.setType(""+clickBean.getShowtype());//1新闻6评测7原创
							intent = new Intent(mContext, OriginalActivity.class);
							Bundle bundleNews = new Bundle();
							bundleNews.putSerializable("originalBean", originalBean);
							intent.putExtras(bundleNews);
							mContext.startActivity(intent);
						}else{
							NewsBean news = new NewsBean();
							news.setTitle(clickBean.getTitle());
							news.setArcurl(clickBean.getArcurl());
							news.setWebviewurl(clickBean.getWebviewurl());
							news.setType(""+clickBean.getShowtype());//1新闻6评测7原创
							intent = new Intent(mContext, NewsActivity.class);
							Bundle bundleNews = new Bundle();
							bundleNews.putSerializable("newsBean", news);
							intent.putExtras(bundleNews);
							mContext.startActivity(intent);
						}
						break;
					case 4:
//						intent = new Intent(mContext, ForumPostListActivity.class);
//						intent.putExtra("title",clickBean.getTitle());
//						intent.putExtra("fid",clickBean.getFid());
//						startActivity(intent);
						intent = new Intent(mContext, SectionPageActivity.class);
						intent.putExtra("plateId",String.valueOf(clickBean.getFid()));
						startActivity(intent);
						break;
				}
			}
		});
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						mPage = 1;
						obtainData();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						obtainData();
					}
				}, 50);
			}
		});

		mBinding.recyclerview.setRefreshing(true);
	}

	public void obtainData() {
		//获取数据
		long time = System.currentTimeMillis();
		String validate = "" + type + 10 + mPage + time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("keyword", SearchActivity.keyWord);
			obj.put("type", type);
			obj.put("pagesize", 10);
			obj.put("page", mPage);
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		UserService.Api service = retrofit.create(UserService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<NewSearchRespBean> call = service.newallso(body);
		call.enqueue(new Callback<NewSearchRespBean>() {
			@Override
			public void onResponse(Call<NewSearchRespBean> call, Response<NewSearchRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<NewSearchRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});
	}

	private void initData(NewSearchRespBean bean) {
		mBinding.recyclerview.refreshComplete();
		if (bean == null || bean.getData() == null) {
			mBinding.recyclerview.setNoMore(true);
			return;
		}
		if (mPage > 1) {
			mAdapter.appendList(bean.getData().getList());
		} else {
			totalCount=bean.getData().getTotal();
			mAdapter.clearAndAddList(bean.getData().getList());
		}
		if(mAdapter.getItemCount()==totalCount) {
			mBinding.recyclerview.setNoMore(true);
		} else {
			mBinding.recyclerview.refreshComplete();
			mPage++;
		}
	}

	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.cb_special://专区
				if (type != 1) {
					mPage = 1;
					type = 1;
					obtainData();
				}
				break;
		}
	}
	@Override
	protected void onFragmentVisibleChange(boolean isVisible) {
		super.onFragmentVisibleChange(isVisible);
		if (isVisible&&!mBinding.recyclerview.canScrollVertically(1)) {//canScrollVertically是否可向向下滑动
			obtainData();
		}
	}
}
