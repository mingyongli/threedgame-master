package com.ws3dm.app.mvp.model.RespBean;

import com.google.gson.annotations.SerializedName;
import com.ws3dm.app.bean.AddressBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  9:52
 */
public class AddressRespBean extends BaseRespBean<AddressRespBean.DataBean>{

	/**
	 * data : {"list":[{"id":2,"region_province":7,"region_city":0,"region_area":0,"address":"台湾 第10001楼","addr":"第10001楼","postal":"000000","concat":"收货人20","mobile":"18321005045","order":1,"is_default":1,"created_at":1543666856,"updated_at":1543667145},{"id":5,"region_province":7,"region_city":0,"region_area":0,"address":"台湾 第10001楼","addr":"第10001楼","postal":"000000","concat":"收货人","mobile":"18321005045","order":4,"is_default":0,"created_at":1543666971,"updated_at":1543666971},{"id":4,"region_province":8,"region_city":101,"region_area":0,"address":"安徽 安庆市 第10002楼","addr":"第10002楼","postal":"000000","concat":"收货人2","mobile":"18321005046","order":3,"is_default":0,"created_at":1543666944,"updated_at":1543667001}]}
	 */
	public static class DataBean {
		private List<AddressBean> list;

		public List<AddressBean> getList() {
			return list;
		}

		public void setList(List<AddressBean> list) {
			this.list = list;
		}

	}
}
