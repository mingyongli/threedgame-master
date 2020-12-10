package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ws3dm.app.R;
import com.ws3dm.app.activity.ForumBoardListActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.ForumGidBean;
import com.ws3dm.app.databinding.FgBaseRecyclerviewBinding;
import com.ws3dm.app.mvp.model.RespBean.ForumGidRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.ForumService;
import com.ws3dm.app.util.StringUtil;

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
 * Describution :板块分区（论坛） fragment
 * <p>
 * Author : DKjuan
 * <p>
 * Date : 2017/8/18 17:22
 **/
public class FragmentBoard extends BaseFragment {
	private FgBaseRecyclerviewBinding mBinding;
	private CommonRecyclerAdapter<ForumGidBean> mAdapter;
	public List<ForumGidBean> listData = new ArrayList<ForumGidBean>();

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		AppCompatActivity activity = (AppCompatActivity) getActivity();

		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_base_recyclerview, container, false);
//		mBinding.setHandler(this);

		initView();

		return mBinding.getRoot();
	}

	public void initView() {

		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setPullRefreshEnabled(false);
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);原来就注释掉的    y
//		mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
//		mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

		mAdapter = new CommonRecyclerAdapter<ForumGidBean>(mContext, R.layout.adapter_board) {
			@Override
			public void bindData(RecyclerViewHolder holder, int position, final ForumGidBean bean) {
				holder.setText(R.id.tv_name, bean.getTitle());
				holder.setText(R.id.tv_num, bean.getForumscount()+"个分类板块");
			}
		};
		mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int position) {
                Intent intent = new Intent(mContext, ForumBoardListActivity.class);
				intent.putExtra("title",listData.get(position).getTitle());
				intent.putExtra("gid",listData.get(position).getGid());
                startActivity(intent);
			}
		});

		mBinding.recyclerview.setAdapter(mAdapter);

		obtainData();
	}

	public void obtainData() {
		//获取数据
		long time = System.currentTimeMillis();
		String validate = ""+ time;
		String sign = StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		mForumPresenter.getForumGid(obj.toString());
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		ForumService.Api service = retrofit.create(ForumService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<ForumGidRespBean> call = service.getForumGid(body);
		call.enqueue(new Callback<ForumGidRespBean>() {
			@Override
			public void onResponse(Call<ForumGidRespBean> call, Response<ForumGidRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				ForumGidRespBean bean= response.body();
//                        if(mPage>1)
//                            mAdapter.appendList(bean.getData().getList());
//                        else
				if(bean!=null&&bean.getData()!=null)
					listData=bean.getData().getList();
				mAdapter.clearAndAddList(listData);
			}

			@Override
			public void onFailure(Call<ForumGidRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
			}
		});
	}

	/**
	 * Describution :收藏 目标：forum    显示view：txtCollect
	 **/
//    protected void Collect(final Forum forum,TextView txtCollect){
//        // 0：没有收藏  1：已收藏
//        if (SharedUtil.getSharedPreferencesData("forum" + forum.getFid()).equals("0")) {
//            if (txtCollect.getText().toString().trim().equals("已收藏"))
//                txtCollect.setText("收藏");
//            else {
//                forumFile.add(forum);
//                SharedUtil.setSharedPreferencesData("forum" + forum.getFid(), "1");
//                txtCollect.setText("已收藏");
//            }
//        } else {
//            if (txtCollect.getText().toString().trim().equals("收藏"))
//                txtCollect.setText("已收藏");
//            else {
//                forumFile.delete(forum.getFid());
//                SharedUtil.setSharedPreferencesData("forum" + forum.getFid(), "0");
//                txtCollect.setText("收藏");
//            }
//        }
//    }
	@Override
	protected void onFragmentFirstVisible() {
		super.onFragmentFirstVisible();
		mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null)
			mAdapter.notifyDataSetChanged();
	}

//    public void loadForumByPage(Boolean isRefresh, int page){
//        JSONObject obj = new JSONObject();
//        String jsonString;
//        try {
//            // 搜索
//            if (strType.equals("SearchActivity")) {
//                obj.put("key", strTitle);
//                obj.put("page", page + "");
//                obj.put("module", "forumssearch");
//            } else {
//                obj.put("fid", strFid);
//                obj.put("module", "forums");
//            }
////            mGamePresenter.getGameData(obj.toString());
//            //同步请求
//            Retrofit retrofit= RetrofitFactory.getRetrofit(0,true);
//            ForumService.Api service=retrofit.create(ForumService.Api.class);
//            RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj.toString());
//            Call<ResponseBody> call=service.getForumList(body);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    mPage++;
//                    try {
//                        String jsonString=response.body().string();
//                        init(jsonString);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
//                    Log.e("requestFailure",throwable.getMessage()+"");
//                    mBinding.recyclerview.loadMoreError();
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}