package com.ws3dm.app.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ws3dm.app.R;
import com.ws3dm.app.MyApplication;
import com.ws3dm.app.bean.AddressBean;
import com.ws3dm.app.databinding.AcAddPlaceBinding;
import com.ws3dm.app.network.RetrofitFactory;
import com.ws3dm.app.network.service.UserService;
import com.ws3dm.app.util.StringUtil;
import com.ws3dm.app.util.ToastUtil;
import com.ws3dm.app.view.dialog.AbstractWheelTextAdapter;
import com.ws3dm.app.view.dialog.AddressData;
import com.ws3dm.app.view.dialog.ArrayWheelAdapter;
import com.ws3dm.app.view.dialog.MyAlertDialog;
import com.ws3dm.app.view.dialog.OnWheelChangedListener;
import com.ws3dm.app.view.dialog.WheelView;
import com.ws3dm.app.view.dialog.WheelViewTime;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Describution : 添加地址
 * 
 * Author : DKjuan
 * 
 * Date : 2018/12/5 16:34
 **/
public class AddPlaceActivity extends BaseActivity {
	private AcAddPlaceBinding mBinding;
	private AddressBean mAddressBean;
	private int provinceID,cityID,areaID,provinceIndex,cityIndex,areaIndex;//省ID,城市ID,县ID,位置,省市区
	private String cityTxt;//用户头衔
	private boolean isModify;

	@Override
	protected void init() {
		mBinding=bindView(R.layout.ac_add_place);
		mBinding.setHandler(this);

		initView();
		
	}

	public void initView(){
		if(getIntent().hasExtra("place")){
			isModify=true;
			mAddressBean= (AddressBean) getIntent().getSerializableExtra("place");
			
			mBinding.tvConcat.setText(mAddressBean.getConcat());
			mBinding.tvMobile.setText(mAddressBean.getMobile());
			mBinding.btnSelectArea.setText(mAddressBean.getRegion_str());
			mBinding.tvDetail.setText(mAddressBean.getAddr());
			mBinding.tvPostal.setText(mAddressBean.getPostal());
			mBinding.tvAddressName.setText(mAddressBean.getTitle());
			mBinding.tvSteam.setText(mAddressBean.getSteam_url());

			provinceID=mAddressBean.getRegion_province();
			provinceIndex=mAddressBean.getRegion_province()-1;
//			cityID=mAddressBean.getRegion_city();
//			cityIndex=Arrays.binarySearch(AddressData.C_ID[provinceIndex],cityID);
//			areaID=mAddressBean.getRegion_area();
//			areaIndex=Arrays.binarySearch(AddressData.C_C_ID[provinceIndex][cityIndex],areaID);
		}else {
			isModify=false;
		}
	}
	
	public void clickHandler(View view) {
		switch (view.getId()) {
			case R.id.imgReturn:
				onBackPressed();
				break;
			case R.id.iv_sec:
			case R.id.tv_place:
			case R.id.btn_select_area:
				View dialogView = dialogm();
				final MyAlertDialog dialog1 = new MyAlertDialog(mContext)
						.builder()
						.setTitle(mBinding.btnSelectArea.getText().toString())
						// .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
						// .setEditText("1111111111111")
						.setView(dialogView)
						.setNegativeButton("取消", new View.OnClickListener() {
							@Override
							public void onClick(View v) {

							}
						});
				dialog1.setPositiveButton("保存", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mBinding.btnSelectArea.setText(cityTxt);
					}
				});
				dialog1.show();
				break;
			case R.id.tv_submit:
				if(StringUtil.isEmpty(mBinding.tvConcat.getText().toString().trim())){
					ToastUtil.showToast(mContext,"联系人不能为空！");
					return;
				}
				if(StringUtil.isEmpty(mBinding.btnSelectArea.getText().toString().trim())){
					ToastUtil.showToast(mContext,"所在地区不能为空！");
					return;
				}
				if(StringUtil.isEmpty(mBinding.tvDetail.getText().toString().trim())){
					ToastUtil.showToast(mContext,"详细地址不能为空！");
					return;
				}
				if(StringUtil.isEmpty(mBinding.tvMobile.getText().toString().trim())){
					ToastUtil.showToast(mContext,"联系人不能为空！");
					return;
				}
				if(!StringUtil.isMobieNumber(mBinding.tvMobile.getText().toString().trim())){
					ToastUtil.showToast(mContext,"手机格式不正确！");
					return;
				}
				long time=System.currentTimeMillis();
				String validate,sign;
				JSONObject obj=null;
				if(isModify){
					validate=MyApplication.getUserData().uid+mAddressBean.getId()+provinceID+"00"+mBinding.tvMobile.getText().toString()+time;
					sign= StringUtil.MD5(validate);
					obj = new JSONObject();
					try {
						obj.put("uid", MyApplication.getUserData().uid);
						obj.put("id", mAddressBean.getId());
						obj.put("region_province", provinceID);
						obj.put("region_city", "0");//cityID
						obj.put("region_area", "0");//areaID
						obj.put("addr", mBinding.tvDetail.getText().toString().trim());
						obj.put("postal", mBinding.tvPostal.getText().toString().trim());
						obj.put("concat", mBinding.tvConcat.getText().toString().trim());
						obj.put("title", mBinding.tvAddressName.getText().toString().trim());
						obj.put("steam_url", mBinding.tvSteam.getText().toString().trim());
						obj.put("mobile",mBinding.tvMobile.getText().toString().trim());
						obj.put("time", time);
						obj.put("sign", sign);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					//同步请求
					Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
					UserService.Api service = retrofit.create(UserService.Api.class);
					RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
					Call<ResponseBody> call = service.modifyAddress(body);
					call.enqueue(new Callback<ResponseBody>() {
						@Override
						public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
							Log.e("requestSuccess", "-----------------------" + response.body());
							try {
								String jsonString=response.body().string();
								JSONObject jsonObject=new JSONObject(jsonString);
								if(jsonObject.optInt("code")==1){
									ToastUtil.showToast(mContext,"修改成功！");
									finish();
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
				}else{//添加地址
					validate=MyApplication.getUserData().uid+provinceID+"00"+mBinding.tvMobile.getText().toString()+time;
					sign= StringUtil.MD5(validate);
					obj = new JSONObject();
					try {
						obj.put("uid", MyApplication.getUserData().uid);
						obj.put("region_province", provinceID);
						obj.put("region_city", "0");//cityID
						obj.put("region_area", "0");//areaID
						obj.put("addr", mBinding.tvDetail.getText().toString().trim());
						obj.put("postal", mBinding.tvPostal.getText().toString().trim());
						obj.put("concat", mBinding.tvConcat.getText().toString().trim());
						obj.put("title", mBinding.tvAddressName.getText().toString().trim());
						obj.put("steam_url", mBinding.tvSteam.getText().toString().trim());
						obj.put("mobile",mBinding.tvMobile.getText().toString().trim());
						obj.put("time", time);
						obj.put("sign", sign);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					//同步请求
					Retrofit retrofit = RetrofitFactory.getNewRetrofit(0);
					UserService.Api service = retrofit.create(UserService.Api.class);
					RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj.toString());
					Call<ResponseBody> call = service.addAddress(body);
					call.enqueue(new Callback<ResponseBody>() {
						@Override
						public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
							Log.e("requestSuccess", "-----------------------" + response.body());
							try {
								String jsonString=response.body().string();
								JSONObject jsonObject=new JSONObject(jsonString);
								if(jsonObject.optInt("code")==1){
									ToastUtil.showToast(mContext,"添加成功！");
									finish();
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
				break;
		}
	}

	/**
	 * Adapter for countries
	 */
	private class CountryAdapter extends AbstractWheelTextAdapter {
		// Countries names
		private String countries[] = AddressData.PROVINCES;

		/**
		 * Constructor
		 */
		protected CountryAdapter(Context context) {
			super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
			setItemTextResource(R.id.wheelcity_country_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return countries.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return countries[index];
		}
	}

	/**
	 * Updates the city wheel
	 */
	private void updateCities(WheelView city, String cities[][], int index) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				cities[index]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
	}

	/**
	 * Updates the ccity wheel
	 */
	private void updatecCities(WheelView city, String ccities[][][], int index,
	                           int index2) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				ccities[index][index2]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
	}

	private View dialogm() {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.wheelcity_cities_layout, null);
		final WheelView country = (WheelView) contentView
				.findViewById(R.id.wheelcity_country);
		country.setVisibleItems(3);
		country.setViewAdapter(new CountryAdapter(this));

		final String cities[][] = AddressData.CITIES;
		final String ccities[][][] = AddressData.COUNTIES;
		final WheelView city = (WheelView) contentView
				.findViewById(R.id.wheelcity_city);
		city.setVisibleItems(3);

		// 地区选择
		final WheelView ccity = (WheelView) contentView
				.findViewById(R.id.wheelcity_ccity);
		ccity.setVisibleItems(3);// 不限城市

		country.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateCities(city, cities, newValue);
				updatecCities(ccity, ccities, newValue,0);
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
						.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
						.getCurrentItem()][ccity.getCurrentItem()];
				provinceID=AddressData.P_ID[country.getCurrentItem()];
				cityID=AddressData.C_ID[country.getCurrentItem()][city.getCurrentItem()];
				areaID=AddressData.C_C_ID[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()];
			}

			@Override
			public void onChanged(WheelViewTime wheel, int oldValue,
			                      int newValue) {
				// TODO Auto-generated method stub

			}
		});

		city.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updatecCities(ccity, ccities, country.getCurrentItem(),
						city.getCurrentItem());

				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
						.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
						.getCurrentItem()][ccity.getCurrentItem()];
				provinceID=AddressData.P_ID[country.getCurrentItem()];
				cityID=AddressData.C_ID[country.getCurrentItem()][city.getCurrentItem()];
				areaID=AddressData.C_C_ID[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()];
			}

			@Override
			public void onChanged(WheelViewTime wheel, int oldValue,
			                      int newValue) {
				// TODO Auto-generated method stub

			}
		});

		ccity.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
						.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
						.getCurrentItem()][ccity.getCurrentItem()];
				provinceID=AddressData.P_ID[country.getCurrentItem()];
				cityID=AddressData.C_ID[country.getCurrentItem()][city.getCurrentItem()];
				areaID=AddressData.C_C_ID[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()];
			}

			@Override
			public void onChanged(WheelViewTime wheel, int oldValue,
			                      int newValue) {
				// TODO Auto-generated method stub

			}
		});

		if(isModify){
			country.setCurrentItem(provinceIndex);// 设置上海
			city.setCurrentItem(cityIndex);
			ccity.setCurrentItem(areaIndex);
		}else{
			country.setCurrentItem(1);
			city.setCurrentItem(1);
			ccity.setCurrentItem(0);
		}
		
		return contentView;
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}