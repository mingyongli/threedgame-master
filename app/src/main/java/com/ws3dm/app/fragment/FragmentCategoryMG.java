package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.MGCategoryListActivity;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.bean.CategoryBean;
import com.ws3dm.app.databinding.FgBaseRecyclerviewBinding;
import com.ws3dm.app.mvp.model.RespBean.MGTypeRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.MGService;
import com.ws3dm.app.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :分类fragment，手游分类
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/18 17:22
 **/
public class FragmentCategoryMG extends BaseFragment {
	private FgBaseRecyclerviewBinding mBinding;
//	private Context mContext;
	private BaseRecyclerAdapter<MGTypeRespBean.DataBean.ListBean> mRecyclerAdapter;
	private BaseRecyclerAdapter<AvatarBean> mSubRecyclerAdapter;
	public ArrayList<CategoryBean> listData = new ArrayList<CategoryBean>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		AppCompatActivity activity = (AppCompatActivity) getActivity();

//		mContext=getActivity();
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_base_recyclerview, container, false);

		initView();
		
		return mBinding.getRoot();
	}

	public void initView(){
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mBinding.recyclerview.setLayoutManager(layoutManager);

		mBinding.recyclerview.setPullRefreshEnabled(false);

		mRecyclerAdapter = new BaseRecyclerAdapter<MGTypeRespBean.DataBean.ListBean>(mContext, R.layout.adapter_hot_cate) {
			@Override
			public void bindData(RecyclerViewHolder holder, final int position, final MGTypeRespBean.DataBean.ListBean item) {
				holder.setText(R.id.txt_cate_name, item.getType());

				RecyclerView recyclerView = holder.getView(R.id.recycler_hot_cates);
				GridLayoutManager manager = new GridLayoutManager(mContext, 4);
				recyclerView.setLayoutManager(manager);

				mSubRecyclerAdapter= new BaseRecyclerAdapter<AvatarBean>(mContext, R.layout.adapter_cate_child) {
					@Override
					public void bindData(RecyclerViewHolder holder, int position, AvatarBean child) {
						holder.setText(R.id.tv_label, child.getName());
					}
				};
				mSubRecyclerAdapter.setOnClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(View itemView, int posi) {
						Intent intent = new Intent(mContext, MGCategoryListActivity.class);
						intent.putExtra("currentPosi",posi+1);
						Bundle bundle = new Bundle();
						bundle.putSerializable("categoryData", item);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});
				recyclerView.setAdapter(mSubRecyclerAdapter);
				mSubRecyclerAdapter.clearAndAddList(item.getLabels());
			}
		};
		mBinding.recyclerview.setAdapter(mRecyclerAdapter);
		mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						obtainData();
					}
				}, 50);            //refresh data here
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable(){
					public void run() {
						mBinding.recyclerview.setNoMore(true);
					}
				}, 50);
			}
		});
	}
	
	public void obtainData(){
		//获取数据
		long time=System.currentTimeMillis();
		String validate= ""+time;
		String sign= StringUtil.MD5(validate);
		JSONObject obj = new JSONObject();
		try {
			obj.put("time", time);
			obj.put("sign", sign);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//        mForumPresenter.getForumRank(obj.toString());
		//同步请求
		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
		MGService.Api service = retrofit.create(MGService.Api.class);
		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
		Call<MGTypeRespBean> call = service.getTypeMG(body);
		call.enqueue(new Callback<MGTypeRespBean>() {
			@Override
			public void onResponse(Call<MGTypeRespBean> call, Response<MGTypeRespBean> response) {
				Log.e("requestSuccess", "-----------------------" + response.body());
				initData(response.body());
			}

			@Override
			public void onFailure(Call<MGTypeRespBean> call, Throwable throwable) {
				Log.e("requestFailure", throwable.getMessage() + "");
				mBinding.recyclerview.loadMoreError();
			}
		});
	}

	private void initData(MGTypeRespBean bean) {
		mBinding.recyclerview.setNoMore(true);
		if(bean==null||bean.getData().getList()==null){
			return;
		}
		listData.clear();
		mRecyclerAdapter.clearAndAddList(bean.getData().getList());
	}

	@Override
	protected void onFragmentFirstVisible() {
		super.onFragmentFirstVisible();
		obtainData();
	}
}