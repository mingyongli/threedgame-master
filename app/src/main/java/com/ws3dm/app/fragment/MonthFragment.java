package com.ws3dm.app.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.ws3dm.app.R;
import com.ws3dm.app.activity.GameHomeActivity;
import com.ws3dm.app.adapter.CommonRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.databinding.FgGameMonthBinding;
import com.ws3dm.app.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Describution :游戏库 按月份游戏列表
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/16 13:20
 **/

public class MonthFragment extends BaseFragment {

    private FgGameMonthBinding mBinding;
    private CustomViewPager vp;
    private CommonRecyclerAdapter<GameBean> mAdapter;
    private String pre_mon,next_mon,currentDay="";
    private int TotalPosition;
    private List<GameBean> mList=new ArrayList<>();

    public MonthFragment() {}

    @SuppressLint("ValidFragment")
    public MonthFragment(CustomViewPager vp) {
        this.vp = vp;
    }

    @SuppressLint("ValidFragment")
    public MonthFragment(int childCount, CustomViewPager vp) {
        this.TotalPosition=childCount;
        this.vp = vp;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fg_game_month, container, false);
        int position=getArguments().getInt("position");
        vp.setObjectForPosition(mBinding.getRoot(),position);
        initView();
        return mBinding.getRoot();
    }

    public void initView() {
        pre_mon=(String) getArguments().get("pre_mon");
        next_mon=(String) getArguments().get("next_mon");
        mList=(List<GameBean>)getArguments().getSerializable("monList");
        
        mBinding.tvPreMon.setText(pre_mon);
        mBinding.tvNextMon.setText(next_mon);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerview.setLayoutManager(layoutManager);
        mBinding.recyclerview.setNestedScrollingEnabled(false);
//		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
//		recyclerView.setLayoutManager(layoutManager);

//		//添加分割线
//		if (mListV3DividerItemDecoration == null) {
//			mListV3DividerItemDecoration = new DividerItemDecorationAdapter(mContext);
//		} else {
//			mBinding.recyclerview.removeItemDecoration(mListV3DividerItemDecoration);
//		}
        
//		mBinding.recyclerview.addItemDecoration(mListV3DividerItemDecoration);

        mAdapter = new CommonRecyclerAdapter<GameBean>(mContext, R.layout.item_game_month) {
            @Override
            public void bindData(RecyclerViewHolder holder, final int position, final GameBean gameBean) {
                if(currentDay.equals(gameBean.getPubday())){
                    holder.setText(R.id.tv_day, "");
                    holder.getView(R.id.line_bottom).setVisibility(View.INVISIBLE);
                }else {
                    currentDay=gameBean.getPubday();
                    holder.setText(R.id.tv_day, currentDay);
                    holder.getView(R.id.line_bottom).setVisibility(View.VISIBLE);
                }
                if(position==0)
                    holder.getView(R.id.line_bottom).setVisibility(View.INVISIBLE);
                
                holder.setImageByUrl(R.id.imgCover,gameBean.getLitpic());
                holder.setText(R.id.tv_title, gameBean.getTitle());
                holder.setText(R.id.tv_type, gameBean.getType());
                holder.setText(R.id.tv_system, gameBean.getSystem());
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

        mAdapter.clearAndAddList(mList);

        mBinding.tvPreMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vp.getCurrentItem()!=0)
                    vp.setCurrentItem(vp.getCurrentItem()-1);
            }
        });
        mBinding.tvNextMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vp.getCurrentItem()!=TotalPosition)
                    vp.setCurrentItem(vp.getCurrentItem()+1);
            }
        });
    }
}
