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
import com.ws3dm.app.activity.ForumPostListActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.ForumBean;
import com.ws3dm.app.databinding.FgForumRankBinding;
import com.ws3dm.app.mvp.model.RespBean.ForumRankRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.ForumService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :论坛排行 
 * 
 * Author : DKjuan
 * 
 * Date : 2017/10/9 9:23
 **/
public class FragmentForumRank extends BaseFragment {
    private FgForumRankBinding mBinding;
//    private Context mContext;
    private CommonRecyclerAdapter<ForumBean> mAdapter;
    private int mPage=1, clickPosition;//点击的位置
    private List<ForumBean> listData=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        AppCompatActivity activity = (AppCompatActivity) getActivity();

//        mContext=getActivity();
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_forum_rank, container, false);

        initView();
//        obtainData(mPage);
        
        return mBinding.getRoot();
    }
    
    public void initView(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerview.setLayoutManager(layoutManager);

        mBinding.recyclerview.setPullRefreshEnabled(false);
        mBinding.recyclerview.setLoadingMoreEnabled(false);
//        mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);     //原来就注释掉的
//        mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
//        mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

        mAdapter = new CommonRecyclerAdapter<ForumBean>(mContext, R.layout.adapter_forum_rank) {
            @Override
            public void bindData(RecyclerViewHolder holder, final int position, final ForumBean forumBean) {
                holder.setImageByUrl(R.id.imgArrayOne,forumBean.getLitpic());
                holder.setText(R.id.tv_title, forumBean.getTitle());
                holder.setText(R.id.tv_readnum, "今日："+forumBean.getTodayposts());
                holder.setText(R.id.tv_title, forumBean.getTitle());
                holder.setText(R.id.tv_title, forumBean.getTitle());
//                ImageView img_collect=holder.getView(R.id.img_collect);
//                if(forumBean.getIsfavorite()==0){
//                    img_collect.setImageResource(R.drawable.click_collect);
//                }else{
//                    img_collect.setImageResource(R.drawable.collect);
//                }
//                img_collect.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(MyApplication.getUserData().loginStatue){
//                            clickPosition=position;
//                            addDelFavorite(forumBean.getIsfavorite()==0?1:2);
//                        }else {
//                            ToastUtil.showToast(mContext,"请先登录！");
//                        }
//                    }
//                });
            }
        };
        mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent intent = new Intent(mContext, ForumPostListActivity.class);
                intent.putExtra("title",listData.get(position).getTitle());
                intent.putExtra("fid",listData.get(position).getFid());
                startActivity(intent);
            }
        });

        mBinding.recyclerview.setAdapter(mAdapter);

        obtainData(mPage);
    }
    
    public void obtainData(int page){
        //获取数据
        long time=System.currentTimeMillis();
        String uid=MyApplication.getUserData().loginStatue?MyApplication.getUserData().uid:"0";
        String validate= ""+uid+time;
        String sign= StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", uid);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        mForumPresenter.getForumRank(obj.toString());
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        ForumService.Api service = retrofit.create(ForumService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<ForumRankRespBean> call = service.getForumRank(body);
        call.enqueue(new Callback<ForumRankRespBean>() {
            @Override
            public void onResponse(Call<ForumRankRespBean> call, Response<ForumRankRespBean> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                ForumRankRespBean bean= response.body();
                if(bean!=null&&bean.getData()!=null) {
                    listData = bean.getData().getList();
//                        if(mPage>1)
//                            mAdapter.appendList(bean.getData().getList());
//                        else
                    mAdapter.clearAndAddList(listData);
                    mPage++;
                }
            }

            @Override
            public void onFailure(Call<ForumRankRespBean> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
            }
        });
    }

    public void addDelFavorite(final int act){//1添加2删除
        String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
        int uid=Integer.parseInt(temp);
        int fid = mAdapter.getDataByPosition(clickPosition).getFid();
        long time=System.currentTimeMillis();
        String validate= ""+uid+fid+act+time;
        String sign= StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", MyApplication.getUserData().uid);
            obj.put("fid",fid);
            obj.put("act",act);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        ForumService.Api service = retrofit.create(ForumService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<ResponseBody> call = service.setFidFavorite(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json=response.body().string();
                    Log.e("requestSuccess", "-----------------------" + json);
                    JSONObject jsonObject=new JSONObject(json);
                    if(jsonObject.optInt("code")==1){
                        mAdapter.getDataByPosition(clickPosition).setIsfavorite(act==1?1:0);
                        mAdapter.notifyDataSetChanged();
                    }else{
                        ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
            }
        });
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        mBinding.recyclerview.setRefreshing(true);//refresh的监听处理中已经有加载数据的操作
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mAdapter != null)
//            mAdapter.notifyDataSetChanged();
//    }

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