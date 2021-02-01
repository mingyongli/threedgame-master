package com.ws3dm.app.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.ForumDetailActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.ForumDetailBean;
import com.ws3dm.app.databinding.FgForumTopBinding;
import com.ws3dm.app.mvp.model.RespBean.ForumTopRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.ForumService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.glide.GlideUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution :论坛置顶列表-----全站置顶
 * 
 * Author : DKjuan
 * 
 * Date : 2017/10/9 9:23
 **/
public class FragmentForumTopthread extends BaseFragment {
    private FgForumTopBinding mBinding;
//    private Context mContext;
    private CommonRecyclerAdapter<ForumDetailBean> mAdapter;
    private ArrayList<ForumDetailBean> listAll=new ArrayList<>();
    private ArrayList<ForumDetailBean> listIn=new ArrayList<>();
    private ArrayList<ForumDetailBean> listJing=new ArrayList<>();
    private int type=0;//0 全部  1，站内   2，精华

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        AppCompatActivity activity = (AppCompatActivity) getActivity();

//        mContext=getActivity();
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_forum_top, container, false);
        mBinding.setHandler(this);
        
        initView();
        
        return mBinding.getRoot();
    }
    
    public void initView(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerview.setLayoutManager(layoutManager);

        mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

        mAdapter = new CommonRecyclerAdapter<ForumDetailBean>(mContext, R.layout.adapter_forum_detail) {
            @Override
            public void bindData(RecyclerViewHolder holder, final int position, final ForumDetailBean forum) {
//                holder.setImageByUrl(R.id.img_head,forum.getUser().avatarstr);
                GlideUtil.loadCircleImage(mContext,forum.getUser().avatarstr, (ImageView) holder.getView(R.id.img_head));
                holder.setText(R.id.tv_name, forum.getUser().nickname);
                int length=forum.getType().length();
                holder.setText(R.id.tv_tag, forum.getType());
                if(length<=0){
                    holder.getView(R.id.tv_tag).setVisibility(View.GONE);
                    holder.setText(R.id.tv_content, forum.getTitle());
                }else if(length>3){
                    holder.getView(R.id.tv_tag).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_content, "          "+"          "+forum.getTitle());
                }else{
                    holder.getView(R.id.tv_tag).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_content, "          "+forum.getTitle());
                }
                holder.setText(R.id.tv_time, TimeUtil.getTime(forum.getPubdate_at()));
                holder.setText(R.id.tv_commentnum, forum.getReplies());
                holder.setText(R.id.tv_read, forum.getViews());
            }
        };
        mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                MobclickAgent.onEvent(mContext,"11");
                Intent intent = new Intent(mContext, ForumDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("forumDetailBean", mAdapter.getDataByPosition(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mBinding.recyclerview.setAdapter(mAdapter);
        mBinding.recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        mBinding.recyclerview.refreshComplete();
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
        ForumService.Api service = retrofit.create(ForumService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<ForumTopRespBean> call = type==0?service.getForumTopthread(body):type==1?service.getForumTopnotece(body):service.getForumTopdigest(body);
        call.enqueue(new Callback<ForumTopRespBean>() {
            @Override
            public void onResponse(Call<ForumTopRespBean> call, Response<ForumTopRespBean> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                ForumTopRespBean bean= response.body();
//                        if(mPage>1)
//                            mAdapter.appendList(bean.getData().getList());
//                        else
                if(type==0&&bean.getCode()!=0)
                    listAll.addAll(bean.getData().getList());
                else if(type==1&&bean.getCode()!=0)
                    listIn.addAll(bean.getData().getList());
                else if(type==2&&bean.getCode()!=0)
                    listJing.addAll(bean.getData().getList());
                
                if(bean.getData()!=null)
                    mAdapter.clearAndAddList(bean.getData().getList());
            }

            @Override
            public void onFailure(Call<ForumTopRespBean> call, Throwable throwable) {
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

    //点击事件
    public void clickHandler(View view) {
        switch (view.getId()) {
            case R.id.rd_all:
                if(type!=0){
                    type=0;
                    if(listAll.size()==0) {
                        obtainData();
                    }else{
                        mAdapter.clearAndAddList(listAll);
                    }
                }
                break;
            case R.id.rd_in:
                if(type!=1){
                    type=1;
                    if(listIn.size()==0) {
                        obtainData();
                    }else{
                        mAdapter.clearAndAddList(listIn);
                    }
                }
                break;
            case R.id.rd_jing:
                if(type!=2){
                    type=2;
                    if(listJing.size()==0) {
                        obtainData();
                    }else{
                        mAdapter.clearAndAddList(listJing);
                    }
                }
                break;
        }
    }
}