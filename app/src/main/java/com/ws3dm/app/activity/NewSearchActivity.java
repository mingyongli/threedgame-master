package com.ws3dm.app.activity;

/**
 * Describution :搜索界面 二逼版
 * 
 * Author : DKjuan
 * 
 * Date : 2017/9/4 16:32
 **/
public abstract class NewSearchActivity extends BaseActivity{

//	private AcNewsearchBinding mBinding;
//	private List<String> tabs = new ArrayList<>();//tablayout的标签数组
//	private List<Fragment> fragments = new ArrayList<>();
//
//	private String title[] = {"单机", "手游", "网游"};
//	private BaseRecyclerAdapter<SearchBean> mRecyclerAdapter;
//	private BaseRecyclerAdapter<HotSearchRespBean.DataBean.ListBean.ShouyouBean> mSubRecyclerAdapter;
//	private List<SearchBean> listData=new ArrayList<>();//所有的对象合集，包括历史记录和热门记录
//	private List<HotSearchRespBean.DataBean.ListBean.ShouyouBean> searchData;//本地存的历史搜索记录
//	public static String lastKey="",keyWord="";
//	
//	@Override
//	protected void init() {
//		mBinding=bindView(R.layout.ac_newsearch);
//		mBinding.setHandler(this);
//		
//		initView();
//	}
//	
//	private void initView() {
//		mBinding.etSearch.addTextChangedListener(new TextWatcher() {
//			@Override
//			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//			}
//
//			@Override
//			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//				if (charSequence.length() > 0)
//					mBinding.imgClean.setVisibility(View.VISIBLE);
//				else {
//					mBinding.imgClean.setVisibility(View.GONE);
//				}
//				//刷新界面
//				mBinding.mRecycleView.setVisibility(View.VISIBLE);
//				mBinding.rlContent.setVisibility(View.GONE);
//			}
//
//			@Override
//			public void afterTextChanged(Editable editable) {
//
//			}
//		});
//
//		mBinding.etSearch.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				mBinding.etSearch.setFocusable(true);
//				mBinding.etSearch.setFocusableInTouchMode(true);
//				mBinding.etSearch.requestFocus();
//			}
//		});
//		mBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				if(actionId== EditorInfo.IME_ACTION_SEARCH){
//					AppUtil.closeKeyboard(mContext);
//					
//					keyWord=mBinding.etSearch.getText().toString();
//					mBinding.rlContent.setVisibility(View.VISIBLE);
//					mBinding.mRecycleView.setVisibility(View.GONE);
//					if(keyWord.length()>0&&!keyWord.equals(lastKey)){
//						lastKey=keyWord;
//						//添加到本地历史搜索记录
//						HotSearchRespBean.DataBean.ListBean.ShouyouBean newBean=new HotSearchRespBean.DataBean.ListBean.ShouyouBean();
//						newBean.setId("1");
//						newBean.setKeyword(mBinding.etSearch.getText().toString());
//						String json = SharedUtil.readPreferences(mContext, "search_data");
//						if (!StringUtil.isEmpty(json)) {
//							searchData=JSON.parseArray(json, HotSearchRespBean.DataBean.ListBean.ShouyouBean.class);
//							if(!json.contains(mBinding.etSearch.getText().toString()))
//								searchData.add(newBean);
//							else{
//								int tempSize=searchData.size();
//								for(int i=0;i<tempSize;i++){
//									if(searchData.get(i).getKeyword().equals(newBean.getKeyword())){
//										searchData.remove(i);
//										break;
//									}
//								}
//								searchData.add(newBean);
//							}
//							if(searchData.size()>10)
//								searchData.remove(0);
//						} else {
//							searchData=new ArrayList<>();
//							searchData.add(newBean);
//						}
//						SharedUtil.writePreferences(mContext, "search_data",JSON.toJSONString(searchData));
//
//						initFragment();
//					}
//				}
//				return false;
//			}
//		});
//
//		//初始化recyclerview
//		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//		mBinding.mRecycleView.setLayoutManager(layoutManager);
//
//		mBinding.mRecycleView.setPullRefreshEnabled(false);
//
//		mRecyclerAdapter = new BaseRecyclerAdapter<SearchBean>(mContext, R.layout.adapter_hot_cate) {
//			@Override
//			public void bindData(RecyclerViewHolder holder, final int position, final SearchBean item) {
//				holder.setText(R.id.txt_cate_name, item.getTitle());
//				TextView more=holder.getView(R.id.more);
//				holder.getView(R.id.more).setOnClickListener(new View.OnClickListener() {//最期待->发售  热门跳转排行
//					@Override
//					public void onClick(View view) {
////						ToastUtil.showToast(mContext,"删除记录");
//						searchData=new ArrayList<>();
//						SharedUtil.writePreferences(mContext, "search_data",JSON.toJSONString(searchData));
//						if(listData.get(0).getTitle().equals("最近搜索")){
//							listData.remove(0);
//							mRecyclerAdapter.clearAndAddList(listData);
//						}
//					}
//				});
//				if(item.getTitle().equals("最近搜索")){
//					more.setVisibility(View.VISIBLE);
//					more.setText("删除历史记录");
//				}else{
//					more.setVisibility(View.GONE);
//				}
//
//				RecyclerView recyclerView = holder.getView(R.id.recycler_hot_cates);
//				LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//				layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//				recyclerView.setLayoutManager(layoutManager);
//
//				mSubRecyclerAdapter= new BaseRecyclerAdapter<HotSearchRespBean.DataBean.ListBean.ShouyouBean>(mContext, R.layout.item_history_body) {
//					@Override
//					public void bindData(RecyclerViewHolder holder, int position, HotSearchRespBean.DataBean.ListBean.ShouyouBean child) {
//						holder.getView(R.id.label).setVisibility(View.GONE);
//						holder.setText(R.id.body_title,child.getKeyword());
//					}
//				};
//				mSubRecyclerAdapter.setOnClickListener(new OnItemClickListener() {
//					@Override
//					public void onItemClick(View itemView, int posi) {
////						ToastUtil.showToast(mContext,"点击历史记录");
//						keyWord=item.getList().get(posi).getKeyword();
//						mBinding.etSearch.setText(keyWord);
//						mBinding.etSearch.setSelection(mBinding.etSearch.getText().length());
//						//刷新界面
////						mBinding.rlContent.setVisibility(View.VISIBLE);
////						mBinding.mRecycleView.setVisibility(View.GONE);
//						AppUtil.closeKeyboard(mContext);
//					}
//				});
//				recyclerView.setAdapter(mSubRecyclerAdapter);
//				mSubRecyclerAdapter.clearAndAddList(item.getList());
//				
//				holder.getView(R.id.txtLine).setVisibility(View.GONE);
//			}
//		};
//		mBinding.mRecycleView.setAdapter(mRecyclerAdapter);
//
//		//设置TabLayout的模式
//		mBinding.mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//		initFragment();
//
//		mBinding.mTabLayout.post(new Runnable() {
//			@Override
//			public void run() {
//				AppUtil.setIndicator(mContext,mBinding.mTabLayout,35);
//			}
//		});
//
//		if(TimeUtil.dateDayNow().equals(SharedUtil.getSharedPreferencesData("searchtoday"))){
//			initData(JSON.parseObject(SharedUtil.getSharedPreferencesData("searchdata"),HotSearchRespBean.class));
//		}else{
//			obtainData();
//		}
//	}
//	
//	private void initFragment(){
//		if(fragments.size()>0)
//			fragments.clear();
//		//初始化viewpage
//		tabs= Arrays.asList(title);
//		for (int i=0;i<3;i++){
//			NewSearchFragment searchFragment= new NewSearchFragment();//汉化
//			Bundle bundleApp = new Bundle();
//			bundleApp.putString("showCategory",""+i);//0,单机 1，手游 2.网游
//			searchFragment.setArguments(bundleApp);
//			fragments.add(searchFragment);
//		}
//		
////		//设置分割线
////		LinearLayout linearLayout = (LinearLayout) mBinding.mTabLayout.getChildAt(0);
////		linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
////		linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
////				R.drawable.divider)); //设置分割线的样式
////		linearLayout.setDividerPadding(dip2px(10)); //设置分割线间隔
//
//		mBinding.mViewPager.setAdapter(new TabAdapter(getSupportFragmentManager(),fragments,tabs));
//		mBinding.mViewPager.setOffscreenPageLimit(0);
//		mBinding.mTabLayout.setupWithViewPager(mBinding.mViewPager);
//	}
//	private void obtainData(){
////		int sotype=3;// 1新闻2单机3手游4网游5页游
//		long time=System.currentTimeMillis();
//		String validate=""+time;
//		String sign= StringUtil.MD5(validate);
//		JSONObject obj = new JSONObject();
//		try {
////			obj.put("sotype",sotype);// 1新闻2单机3手游4网游5页游
//			obj.put("time", time);
//			obj.put("sign", sign);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		//同步请求
//		Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
//		NewsService.Api service = retrofit.create(NewsService.Api.class);
//		RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
//		Call<HotSearchRespBean> call = service.getHotSearch(body);
//		call.enqueue(new Callback<HotSearchRespBean>() {
//			@Override
//			public void onResponse(Call<HotSearchRespBean> call, Response<HotSearchRespBean> response) {
//				Log.e("requestSuccess", "-----------------------" + response.body());
//				initData(response.body());
//			}
//
//			@Override
//			public void onFailure(Call<HotSearchRespBean> call, Throwable throwable) {
//				Log.e("requestFailure", throwable.getMessage() + "");
//				Log.e("requestFailure", "请求失败！");
//			}
//		});
//	}
//
//	private void initData(HotSearchRespBean bean){
//		if(bean==null||bean.getData()==null){
//			return;
//		}
//		if(!TimeUtil.dateDayNow().equals(SharedUtil.getSharedPreferencesData("searchtoday"))){
//			SharedUtil.setSharedPreferencesData("searchtoday",TimeUtil.dateDayNow());
//			SharedUtil.setSharedPreferencesData("searchdata", JSON.toJSONString(bean));
//		}
//		SearchBean oldBean=new SearchBean();
//		oldBean.setTitle("最近搜索");
//		//从share里面读取历史记录
//		String json = SharedUtil.readPreferences(mContext, "search_data");
//		if (!StringUtil.isEmpty(json)&&json.length()>10) {
//			searchData=JSON.parseArray(json, HotSearchRespBean.DataBean.ListBean.ShouyouBean.class);
//			oldBean.setList(searchData);
//			listData.add(oldBean);
////		} else {
////			List<HotSearchRespBean.DataBean.ListBean.ShouyouBean> tempList=new ArrayList<>();
////			oldBean.setList(tempList);
//		}
//
//		SearchBean hotBean=new SearchBean();
//		hotBean.setTitle("热门搜索");
//		hotBean.setList(bean.getData().getList().getShouyou());
//		listData.add(hotBean);
//		
//		mRecyclerAdapter.clearAndAddList(listData);
//	}
//	
//	public void clickHandler(View view) {
//		switch (view.getId()) {
//			case R.id.imgReturn:
//				AppUtil.closeKeyboard(mContext);
//				new Handler().postDelayed(new Runnable(){
//					public void run() {
//						onBackPressed();
//					}
//				}, 100);
//				break;
//			case R.id.imgClean:
//				mBinding.etSearch.setText("");
//				mBinding.rlContent.setVisibility(View.GONE);
//				mBinding.mRecycleView.setVisibility(View.VISIBLE);
////				if (charSequence.length() > 0){
////					mBinding.imgClean.setVisibility(View.VISIBLE);
////					mBinding.mViewPager.setVisibility(View.GONE);
////					mBinding.mRecycleView.setVisibility(View.VISIBLE);
////				}else {
////					mBinding.imgClean.setVisibility(View.GONE);
////					mBinding.mViewPager.setVisibility(View.GONE);
////					mBinding.mRecycleView.setVisibility(View.VISIBLE);
////				}
//				break;
//			case R.id.txtSearch://点击搜索
//				if(mBinding.etSearch.getText().toString().length()>0){
//					AppUtil.closeKeyboard(mContext);
//					//刷新界面
//					mBinding.rlContent.setVisibility(View.VISIBLE);
//					mBinding.mRecycleView.setVisibility(View.GONE);
//
//					keyWord=mBinding.etSearch.getText().toString();
//					if(keyWord.equals(lastKey)){
//						return;
//					}
//					lastKey=keyWord;
//					//添加到本地历史搜索记录
//					HotSearchRespBean.DataBean.ListBean.ShouyouBean newBean=new HotSearchRespBean.DataBean.ListBean.ShouyouBean();
//					newBean.setId("1");
//					newBean.setKeyword(mBinding.etSearch.getText().toString());
//					String json = SharedUtil.readPreferences(mContext, "search_data");
//					if (!StringUtil.isEmpty(json)) {
//						searchData=JSON.parseArray(json, HotSearchRespBean.DataBean.ListBean.ShouyouBean.class);
//						if(!json.contains(mBinding.etSearch.getText().toString()))
//							searchData.add(newBean);
//						else{
//							int tempSize=searchData.size();
//							for(int i=0;i<tempSize;i++){
//								if(searchData.get(i).getKeyword().equals(newBean.getKeyword())){
//									searchData.remove(i);
//									break;
//								}
//							}
//							searchData.add(newBean);
//						}
//						if(searchData.size()>10)
//							searchData.remove(0);
//					} else {
//						searchData=new ArrayList<>();
//						searchData.add(newBean);
//					}
//					SharedUtil.writePreferences(mContext, "search_data",JSON.toJSONString(searchData));
//
//					initFragment();
//
////					Intent aggreement=new Intent(mContext,SingleWebActivity.class);
////					aggreement.putExtra("title","搜狗结果");
////					aggreement.putExtra("url","https://wap.sogou.com/web/sl?keyword="+mBinding.etSearch.getText().toString());
////					startActivity(aggreement);
//				} 
//				break;
//		}
//	}
//	
//	private class SearchBean{
//		private String title;
//		private List<HotSearchRespBean.DataBean.ListBean.ShouyouBean> list;
//
//		public String getTitle() {
//			return title;
//		}
//
//		public void setTitle(String title) {
//			this.title = title;
//		}
//
//		public List<HotSearchRespBean.DataBean.ListBean.ShouyouBean> getList() {
//			return list;
//		}
//
//		public void setList(List<HotSearchRespBean.DataBean.ListBean.ShouyouBean> list) {
//			this.list = list;
//		}
//	}
}