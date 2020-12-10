package com.ws3dm.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.HistoryAdapter;
import com.ws3dm.app.bean.HistoryBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.OriginalBean;
import com.ws3dm.app.databinding.AcHistoryBinding;
import com.ws3dm.app.sqlite.NewsFile;
import com.ws3dm.app.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Describution :HistoryActivity
 * 
 * Author : DKjuan
 * 
 * Date : 2017/10/23 10:53
 **/
public class HistoryActivity extends BaseActivity{
	
	private AcHistoryBinding mBinding;
	private NewsFile mNewsFile;
	private List<NewsBean> listData = new ArrayList<>();
	private HistoryAdapter mListAdapter;
	private NewsBean news=new NewsBean();
	private OriginalBean originalBean=new OriginalBean();
	private HistoryBean historyBean=new HistoryBean();

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_history);
		mBinding.setHandler(this);

		initView();
	}
	
	//界面初始化
	private void initView() {
		mNewsFile=new NewsFile(mContext);
		listData=mNewsFile.queryHistory();
		System.out.println("kkljjlljk");

		if(listData==null||listData.size()==0){
			mBinding.tvTip.setVisibility(View.VISIBLE);
		}else{
			//排序 -- 按日期降序
			Collections.sort(listData,new Comparator<NewsBean>(){
				public int compare(NewsBean arg1, NewsBean arg0) {
					return arg0.getSeeDate().compareTo(arg1.getSeeDate());
				}
			});
//		Collections.reverse(listData);//反序列表

			// 初始化数据
			mListAdapter = new HistoryAdapter(mContext, listData);
			mBinding.historyListview.setAdapter(mListAdapter);
			mListAdapter.setItemClickListener(new HistoryAdapter.OnItemClickListener() {
				@Override
				public void ItemClickListener(HistoryBean bean) {
					if("原创".equals(bean.lmfl)){
						originalBean.setArcurl(bean.arcurl);
						originalBean.setWebviewurl(bean.webviewurl);
						originalBean.setTitle(bean.title);
						originalBean.setType(bean.lmfl);
						originalBean.setShowtype(7);
						Intent intent = new Intent(mContext, OriginalActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("originalBean",originalBean);
						intent.putExtras(bundle);
						startActivity(intent);
					}else{
						news.setArcurl(bean.arcurl);
						news.setWebviewurl(bean.webviewurl);
						news.setTitle(bean.title);
						news.setType(bean.lmfl);
						Intent intent = new Intent(mContext, NewsActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("newsBean",news);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
			});
			/*mBinding.historyListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
					historyBean= mListAdapter.getItem(position);
					if("原创".equals(historyBean.lmfl)){
						originalBean.setArcurl(historyBean.arcurl);
						originalBean.setWebviewurl(historyBean.webviewurl);
						originalBean.setTitle(historyBean.title);
						originalBean.setType(historyBean.lmfl);
						originalBean.setShowtype(7);
						Intent intent = new Intent(mContext, OriginalActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("originalBean",originalBean);
						intent.putExtras(bundle);
						startActivity(intent);
					}else{
						news.setArcurl(historyBean.arcurl);
						news.setWebviewurl(historyBean.webviewurl);
						news.setTitle(historyBean.title);
						news.setType(historyBean.lmfl);
						Intent intent = new Intent(mContext, NewsActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("newsBean",news);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
			});*/
		}
	}

	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.tv_clear:
				mNewsFile.clearHistory();
				ToastUtil.showToast(mContext,"记录已删除！");
				onBackPressed();
				break;
			case R.id.imgReturn:
				finish();
				break;
		}
	}
}
