package com.ws3dm.app.activity;

import com.ws3dm.app.R;
import com.ws3dm.app.databinding.AcDownlistBinding;

//import com.tpnet.downmanager.download.DownInfo;

/**
 * Describution :用于测试
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/30 14:48
 **/
public class DownListActivity extends BaseActivity {
	private AcDownlistBinding mBinding;
//	private ListAdapter adapter;
	private int errorTime=0;

	@Override
	protected void init() {
		mBinding = bindView(R.layout.ac_downlist);
//		mBinding.setHandler(this);

//		initView();
	}
	
//	//界面初始化
//	private void initView() {
//		mBinding.imgReturn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				onBackPressed();
//			}
//		});
//		
//		if("1".equals(SharedUtil.getSharedPreferencesData("needRD")))
//			mBinding.tvDel.setVisibility(View.GONE);
//		mBinding.tvDel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				delAll();
//				SharedUtil.setSharedPreferencesData("needRD","1");
//				ToastUtil.show("下次启动将自动删除！");
//				mBinding.tvDel.setVisibility(View.GONE);
//			}
//		});
//		
//		mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
//		adapter = new ListAdapter();
//
//		mBinding.recyclerview.setAdapter(adapter);
//
//		//查询当前所有的下载
//		DBUtil.getInstance().getAllDown()
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(adapter);
//	}
//
//	class ListAdapter extends RecyclerView.Adapter<downViewHolder> implements Action1<List<DownInfo>> {
//		List<DownInfo> list = new ArrayList<>();
//		@Override
//		public downViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//			return new downViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_down, parent, false));
//		}
//
//		@Override
//		public void onBindViewHolder(final downViewHolder holder, final int position) {
//			//在Holder里面设置数据
//			holder.setData(list.get(position), position);
//		}
//
//		@Override
//		public int getItemCount() {
//			return list.size();
//		}
//
//		//Rxjava查询回调
//		@Override
//		public void call(List<DownInfo> downInfos) {
//			Log.e("@@","查询到列表数量"+downInfos.size());
//			this.list = downInfos;
//			Collections.reverse(list);
//			notifyDataSetChanged();
//		}
//	}
//	
//	public void delAll(){
//		//查询当前所有的下载
//		DownloadUtil.delAll();
//		File downPath = new File(Environment.getExternalStorageDirectory(), "3DM/Download");
//		if (downPath.exists()) {
//			FileUtil.clearDir(downPath,null);
//		}
//	}
//
//	class downViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//		private Button mBtHandle;
//		private TextView mTvName;
//		private ImageView img_down_del;
//		private ImageView mImg;
//		private TextView mTvDownLength;
//		private ProgressBar mPrbDown;
//
//		private TextView mTvDownStartTime;
//		private TextView mTvDownFinishTime;
//		private TextView mTvState;
//
//		private DownInfo downInfo;
//
//		public downViewHolder(View itemView) {
//			super(itemView);
//			mBtHandle = (Button) itemView.findViewById(R.id.bt_handle);
//			mTvName = (TextView) itemView.findViewById(R.id.tv_name);
//			img_down_del = (ImageView) itemView.findViewById(R.id.img_down_del);
//			mImg = (ImageView) itemView.findViewById(R.id.img_game);
//			mTvDownLength = (TextView) itemView.findViewById(R.id.tv_down_length);
//			mPrbDown = (ProgressBar) itemView.findViewById(R.id.prb_down);
//			mTvDownStartTime = (TextView) itemView.findViewById(R.id.tv_down_start_time);
//			mTvDownFinishTime = (TextView) itemView.findViewById(R.id.tv_down_finish_time);
//			mTvState = (TextView) itemView.findViewById(R.id.tv_state);
//			mBtHandle.setOnClickListener(this);
//			img_down_del.setOnClickListener(this);
//
//			//因为(downInfo.downUrl()用来传递进度信息，这里使用两个(downInfo.downUrl()进行标识
//			RxBus.with().setEvent(DownManager.DOWN_ADD_SUBSCRIBE)
//					.onNext(new Action1<Events<?>>() {
//						@Override
//						public void call(Events<?> events) {
//							//添加监听器，在列表点击开始回调
//							String link = events.getContent();
//							link = link.replace(DownManager.DOWN_ADD_SUBSCRIBE, "");
//							Log.e("@@", "rxbus添加监听器");
//							DownManager.getInstance().addListener(link, listener);
//
//						}
//					}).create();
//		}
//
//		public void setData(DownInfo data, int position) {
//			this.downInfo = data;
//
//			switch (downInfo.downState()) {
//				case DownInfo.DOWN_ING:
//					mBtHandle.setText("暂停");
//					mTvState.setText("下载中..");
//					DownManager.getInstance().startDown(downInfo);
//
//					break;
//				case DownInfo.DOWN_START:
//					mBtHandle.setText("暂停");
//					mTvState.setText("链接中...");
//					DownManager.getInstance().startDown(downInfo);
//					break;
//				case DownInfo.DOWN_STOP:
//					mTvState.setText("停止中");
//					mBtHandle.setText("开始");
//					break;
//				case DownInfo.DOWN_PAUSE:
//					mBtHandle.setText("开始");
//					mTvState.setText("暂停中");
//					break;
//				case DownInfo.DOWN_ERROR:
//					mBtHandle.setText("重试");
//					mTvState.setText("出现错误");
//					break;
//				case DownInfo.DOWN_FINISH:
//					mBtHandle.setText("打开");
//					mTvState.setText("下载完成");
//
////					mTvDownFinishTime.setVisibility(View.VISIBLE);
////					//设置下载完成时间
////					mTvDownFinishTime.setText(
////							String.format("完成时间: %s",
////									new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(downInfo.finishTime())
////							)
////					);
//					break;
//			}
//			//恭喜View监听器
//			DownManager.getInstance().addListener(downInfo.downUrl(), listener);
//			if (TextUtils.isEmpty(downInfo.downName())) {
//				//查询名字
////				DatabaseUtil.getInstance().getName(downInfo.downUrl())
////						.observeOn(AndroidSchedulers.mainThread())
////						.subscribe(new Action1<String>() {
////							@Override
////							public void call(String s) {
////								mTvName.setText(s);
////							}
////						});
//			} else {
//				try {
//					mTvName.setText(downInfo.downName().split("\\|")[0]);
//					GlideUtil.loadImage(mContext,downInfo.downName().split("\\|")[1],mImg);
//				} catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}
//			}
//			//设置进度文本
//			mTvDownLength.setText(
//					String.format("%s/%s"
//							, FileUtil.getFormatSize(downInfo.downLength()), FileUtil.getFormatSize(downInfo.totalLength())));
//			//计算进度
//			if (downInfo.totalLength() == 0) {
//				mPrbDown.setProgress(100);
//			} else {
//				mPrbDown.setProgress((int) (downInfo.downLength() * 100 / downInfo.totalLength()));
//			}
//
////			//设置开始下载时间
////			if (downInfo.startTime() > 0) {
////				mTvDownStartTime.setText(
////						String.format("开始时间: %s",
////								new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(downInfo.startTime())
////						)
////				);
////			}
//		}
//
//		// 下载view回调
//		IOnDownloadListener<DownInfo> listener = new IOnDownloadListener<DownInfo>() {
//			@Override
//			public void onNext(DownInfo baseDownEntity) {
//				Log.e("@@", "listsner onNext下载完成");
//				mBtHandle.setText("打开");
//				mTvState.setText("下载完成");
//				downInfo = DownInfo.create(downInfo)
//						.downState(DownInfo.DOWN_FINISH)
//						.build();
////				mTvDownFinishTime.setVisibility(View.VISIBLE);
////				//设置下载完成时间
////				mTvDownFinishTime.setText(
////						String.format("完成时间: %s",
////								SimpleDateFormat.getInstance().format(new Date(downInfo.finishTime()))
////						)
////				);
//			}
//
//			@Override
//			public void onStart() {
//				Log.e("@@", "listsner onStart开始下载");
//				mBtHandle.setText("暂停");
//				mTvState.setText("链接中...");
//
//				downInfo = DownInfo.create(downInfo)
//						.downState(DownInfo.DOWN_START)
//						.build();
////				mTvDownStartTime.setText(
////						String.format("开始时间: %s",
////								SimpleDateFormat.getInstance().format(new Date(downInfo.startTime()))
////						)
////				);
//			}
//
//			@Override
//			public void onComplete() {
//				Log.e("@@", "listsner onComplete完成");
//				//mBtHandle.setText("完成");
//			}
//
//			@Override
//			public void onError(Throwable e) {
//				Log.e("@@", "listsner onError下载错误"+e.getMessage());
//				super.onError(e);
//				mBtHandle.setText("重试");
//				mTvState.setText("出现错误");
//
//				if(errorTime==1) {
//					errorTime=0;
//					downInfo = DownInfo.create(downInfo)
//							.downState(DownInfo.DOWN_ERROR)
//							.build();
//				}else {
//					errorTime=1;
//					DownManager.getInstance().startDown(downInfo);
//				}
//
//			}
//			@Override
//			public void onPuase() {
//				Log.e("@@", "listsner onPause下载暂停:" + downInfo.downState());
//				super.onPuase();
//				mBtHandle.setText("开始");
//				mTvState.setText("暂停中");
//
//				downInfo = DownInfo.create(downInfo)
//						.downState(DownInfo.DOWN_PAUSE)
//						.build();
//			}
//
//			@Override
//			public void onStop() {
//				Log.e("@@", "listsner onPause下载停止");
//				super.onStop();
//				mBtHandle.setText("开始");
//				mTvState.setText("停止中");
//				downInfo = DownInfo.create(downInfo)
//						.downState(DownInfo.DOWN_STOP)
//						.build();
//			}
//
//			@Override
//			public void updateLength(long readLength, long totalLength, int percent) {
//				//Log.e("@@", "listsner updateLength下载中:" + percent + " " + readLength + " " + totalLength);
//				mTvState.setText("下载中..."+percent+" %");
//				//设置文本
//				mTvDownLength.setText(
//						String.format("%s/%s"
//								, FileUtil.getFormatSize(readLength), FileUtil.getFormatSize(totalLength)));
//			}
//
//			@Override
//			public void updatePercent(int percent) {
//				Log.e("@@", "listsner updatePercent更新进度:" + percent);
//				mBtHandle.setText("暂停");
//				mTvState.setText("下载中...");
//				//计算进度
//				mPrbDown.setProgress(percent);
//			}
//		};
//
//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//				case R.id.img_down_del:
////					DownManager.getInstance().delDown(downInfo);
//					break;
//				case R.id.bt_handle:
//					switch (downInfo.downState()) {
//						case DownInfo.DOWN_ING:
//						case DownInfo.DOWN_START:
//							//需要暂停
//							Log.e("@@", "点击了暂停");
//							DownManager.getInstance().pauseDown(downInfo);
//							break;
//						case DownInfo.DOWN_STOP:
//						case DownInfo.DOWN_PAUSE:
//						case DownInfo.DOWN_ERROR:
//							//需要开始
//							Log.e("@@", "点击了 开始下载");
//							//需要设置监听器，
//							//downInfo.setListener(listener);
//							DownManager.getInstance().startDown(downInfo);
//							break;
//						case DownInfo.DOWN_FINISH:
//							//需要打开
//							Log.e("@@", "点击了 完成");
//							if (FileUtil.getExtensionName(downInfo.savePath()).equals("apk")) {
//								//如果是安装包、
////							Intent intent = new Intent(Intent.ACTION_VIEW);
////							intent.setDataAndType(Uri.fromFile(new File(downInfo.savePath())),
////									"application/vnd.android.package-archive");
////							mBtHandle.getContext().startActivity(intent);
//								AppUtil.installApp(mBtHandle.getContext(), downInfo.savePath());
//							} else if (downInfo.downType().equals("application/octet-stream")) {
//
//								ToastUtil.show("文件类型: 二进制流，不知道文件类型。" + downInfo.downType());
//							} else {
//								ToastUtil.show("文件类型: " + downInfo.downType());
//							}
//							break;
//					}
//					break;
//				default:
//					break;
//			}
//		}
//	}
}
