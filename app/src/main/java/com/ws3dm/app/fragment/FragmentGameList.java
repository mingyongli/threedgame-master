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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.CustomRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.GameHomeActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.databinding.FgGameDetailBinding;
import com.ws3dm.app.mvp.model.RespBean.GameListRespBean;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.GameService;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.TimeUtil;
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
 * Describution :单个游戏页详情（只有游戏列表）
 * 
 * Author : DKjuan
 * 
 * Date : 2017/9/13 8:50
 **/
public class FragmentGameList extends BaseFragment {
    private FgGameDetailBinding mBinding;
//    private Context mContext;
    private CommonRecyclerAdapter<GameBean> mAdapter;
    public List<GameBean> listData = new ArrayList<GameBean>();
    private int mPage=1,f_sid,showType,clickPosition;//1:发售表  2.汉化  3.排行    点击的位置
    private int onSale=1;//1:发售,2:未发售
    private GameBean mGameBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        AppCompatActivity activity = (AppCompatActivity) getActivity();

//        mContext=getActivity();
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_game_detail, container, false);

        //获取 Arguments
        showType = getArguments().getInt("type");

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerview.setLayoutManager(layoutManager);

        mBinding.recyclerview.setRefreshHeader(new CustomRefreshHeader(mContext));
//		mBinding.recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mBinding.recyclerview.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mBinding.recyclerview.setArrowImageView(com.jcodecraeer.xrecyclerview.R.drawable.ic_pulltorefresh_arrow);

        mAdapter = new CommonRecyclerAdapter<GameBean>(mContext, R.layout.adapter_game_rank) {
            @Override
            public void bindData(RecyclerViewHolder holder, final int position, final GameBean game) {
                holder.setImageByUrl(R.id.imgCover,game.getLitpic());
                holder.setText(R.id.txtName, game.getTitle());
                holder.setText(R.id.tv_type, "类型：" +game.getType());
                holder.setText(R.id.txtTime, "发售："+ TimeUtil.getFormatTimeSimple(game.getPubdate_at()));
                
//                LinearLayout ll_score=holder.getView(R.id.ll_score);
//                TextView tv_type=holder.getView(R.id.tv_type);
//                TextView txtTime=holder.getView(R.id.txtTime);

                TextView txt_label1=holder.getView(R.id.txt_label1);
                TextView txt_label2=holder.getView(R.id.txt_label2);
                TextView txt_label3=holder.getView(R.id.txt_label3);
                TextView txt_label4=holder.getView(R.id.txt_label4);
                txt_label1.setVisibility(View.GONE);
                txt_label2.setVisibility(View.GONE);
                txt_label3.setVisibility(View.GONE);
                txt_label4.setVisibility(View.GONE);

                if(showType==1){
                    TextView txtPlatform=holder.getView(R.id.txtPlatform);
                    txtPlatform.setVisibility(View.VISIBLE);
                    txtPlatform.setText(game.getSystem());
                    int num=game.getLabels().size()>3?4:game.getLabels().size();
                    switch (num) {
                        case 4:
                            txt_label4.setText(game.getLabels().get(3).getName());
                            txt_label4.setVisibility(View.VISIBLE);
                        case 3:
                            txt_label3.setText(game.getLabels().get(2).getName());
                            txt_label3.setVisibility(View.VISIBLE);
                        case 2:
                            txt_label2.setText(game.getLabels().get(1).getName());
                            txt_label2.setVisibility(View.VISIBLE);
                        case 1:
                            txt_label1.setText(game.getLabels().get(0).getName());
                            txt_label1.setVisibility(View.VISIBLE);
                    }
                }else if(showType==2){
                    TextView tv_language=holder.getView(R.id.tv_language);
                    tv_language.setVisibility(View.VISIBLE);
                    tv_language.setText(game.getLanguage());
                    int num=game.getLabels().size()>3?4:game.getLabels().size();
                    switch (num) {
                        case 4:
                            txt_label4.setText(game.getLabels().get(3).getName());
                            txt_label4.setVisibility(View.VISIBLE);
                        case 3:
                            txt_label3.setText(game.getLabels().get(2).getName());
                            txt_label3.setVisibility(View.VISIBLE);
                        case 2:
                            txt_label2.setText(game.getLabels().get(1).getName());
                            txt_label2.setVisibility(View.VISIBLE);
                        case 1:
                            txt_label1.setText(game.getLabels().get(0).getName());
                            txt_label1.setVisibility(View.VISIBLE);
                    }
                }else if(showType==3){
                    LinearLayout ll_label=holder.getView(R.id.ll_label);
                    ll_label.setVisibility(View.GONE);
//                    ImageView img_collect=holder.getView(R.id.img_collect);
                    RelativeLayout ll_hot=holder.getView(R.id.ll_hot);
                    TextView txtPlatform=holder.getView(R.id.txtPlatform);
                    txtPlatform.setText("平台：" +game.getSystem());
                    txtPlatform.setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_hot,"人气值：" +game.getClick()+"");
//                    if(game.getIsfavorite()==1)//0未收藏1已收藏
//                        img_collect.setImageResource(R.drawable.collect);
//                    else
//                        img_collect.setImageResource(R.drawable.click_collect);
//                    img_collect.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if(MyApplication.getUserData().loginStatue) {
//                                if(MyApplication.getUserData().mobile.length()==0){
//                                    Intent intent=new Intent(mContext, ForgetPassActivity.class);
//                                    intent.putExtra("type",1);//1,绑定手机号，2修改密码，3忘记密码
//                                    startActivity(intent);
//                                }else{
//                                    clickPosition=position;
//                                    mGameBean=game;
//                                    if(MyApplication.getUserData().loginStatue) {
//                                        addDelFavorite(game.getIsfavorite()==0?1:2);
//                                    }else {
//                                        ToastUtil.showToast(mContext,"请先登录！");
//                                    }
//                                }
//                            }else{
//                                ToastUtil.showToast(mContext,"请先登录！");
//                                startActivity(new Intent(mContext,LoginActivity.class));
//                            }
//                        }
//                    });
                    ll_hot.setVisibility(View.VISIBLE);
                }
//                holder.setText(R.id.txtTime, "发售：" + game.getPubdate_at());
//                holder.setText(R.id.txtType, "类型：" + game.getType());
//                holder.setText(R.id.txtPlatform, "平台：" + game.getSystem());
//
//                double score= Keep1DecimalPlaces(10*Math.random());
//                holder.setText(R.id.score, score+"");
//                ImageView imgScore=holder.getView(R.id.img_score);
//                // 获得ClipDrawable对象  
//                ClipDrawable clipDrawable = (ClipDrawable) imgScore.getBackground();
//                // 设置裁剪级别，Clip类型的图片默认裁剪级别为0，此时是全部裁剪，图片看不见；  
//                // 当级别为10000时，不裁剪图片，图片全部可见  
//                // 当全部显示后，设置不可见  
//                clipDrawable.setLevel((int)(score*1000));
            }
        };
        mAdapter.setOnClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent intent = new Intent(mContext, GameHomeActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putSerializable("game", mAdapter.getDataByPosition(position));
//					bundle.putSerializable("game", finalGame);//数据太大，超出系统限制
                GameBean tempGame=mAdapter.getDataByPosition(position);
                bundle.putString("str_game", JSON.toJSONString(tempGame));
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
                        mPage=1;
                        obtainData(mPage);
                    }
                }, 50);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        obtainData(mPage);
                    }
                }, 50);
            }
        });
                
        return mBinding.getRoot();
    }

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

    public void addDelFavorite(final int isAdd){//1添加2删除
        String temp = MyApplication.getUserData().loginStatue ? MyApplication.getUserData().uid : "0";
        int uid=Integer.parseInt(temp);
        String arcurl = mGameBean.getArcurl();
        final int showtype=isAdd==2?0:3;
        long time=System.currentTimeMillis();
        String validate= ""+uid+arcurl+f_sid+showtype+isAdd+time;
        String sign= StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", MyApplication.getUserData().uid);
            obj.put("arcurl",arcurl);
            obj.put("f_sid",f_sid);
            obj.put("showtype",showtype);//展示类型:1新闻2攻略3游戏4手游5杂谈6评测7原创8安利9礼包(删除收藏时可传0)
            obj.put("act",isAdd);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        UserService.Api service = retrofit.create(UserService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<ResponseBody> call = service.setArcFavorite(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    if(jsonObject.optInt("code")==1){
                        JSONObject jsonSub=new JSONObject(jsonObject.opt("data").toString());
                        f_sid=jsonSub.optInt("code");
//                        favorite=3-favorite;
                        if(isAdd==1){
                            mAdapter.getDataByPosition(clickPosition).setIsfavorite(1);
//                            mBinding.imgCollect.setImageResource(R.drawable.collect);
                            ToastUtil.showToast(mContext, "收藏成功！");
                        }else{
                            mAdapter.getDataByPosition(clickPosition).setIsfavorite(0);
//                            mBinding.imgCollect.setImageResource(R.drawable.click_collect);
                        }
                        mAdapter.notifyDataSetChanged();
                    }else{
                        //ToastUtil.showToast(mContext,jsonObject.opt("msg")+"");
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

    public void obtainData(int page){
        //获取数据
        long time=System.currentTimeMillis();
        String uid=MyApplication.getUserData().loginStatue?MyApplication.getUserData().uid:"0";
        String validate=showType==3?uid+"10"+page+time:"10"+page+time;
        String sign= StringUtil.MD5(validate);
        JSONObject obj = new JSONObject();
        try {
            if(showType==3){
                obj.put("uid", MyApplication.getUserData().loginStatue?MyApplication.getUserData().uid:0);
            }
            obj.put("pagesize", 10);
            obj.put("page", page);
            obj.put("time", time);
            obj.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        mForumPresenter.getForumRank(obj.toString());
        //同步请求
        Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
        GameService.Api service = retrofit.create(GameService.Api.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Call<GameListRespBean> call=null;
        if(showType==1){
            call= onSale==1?service.getGameSale(body):service.getGameUnSale(body);
        }else if(showType==2){
            call= service.getGameChinese(body);
        }else if(showType==3){
            call= service.getGameRank(body);
        }
        call.enqueue(new Callback<GameListRespBean>() {
            @Override
            public void onResponse(Call<GameListRespBean> call, Response<GameListRespBean> response) {
                Log.e("requestSuccess", "-----------------------" + response.body());
                initData(response.body());//临时注释 完整处理返回结果 正常处理返回结果
            }

            @Override
            public void onFailure(Call<GameListRespBean> call, Throwable throwable) {
                Log.e("requestFailure", throwable.getMessage() + "");
                mBinding.recyclerview.loadMoreError();
            }
        });
    }
    
    private void initData(GameListRespBean bean) {
        mBinding.recyclerview.refreshComplete();
        if(bean!=null&&bean.getData()!=null) {
            listData = bean.getData().getList();
        }
        if(mPage>1) {
            mAdapter.appendList(listData);
        }else {
            mAdapter.clearAndAddList(listData);
        }
        if (listData==null||listData.size()<1) {
            mBinding.recyclerview.setNoMore(true);
        } else {
            mPage++;
        }
    }
}