package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.AvatarBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class OrigLabelRespBean extends BaseRespBean<OrigLabelRespBean.DataBean>{

	public static class DataBean {
		private List<AvatarBean> list;

		public List<AvatarBean> getList() {
			return list;
		}

		public void setList(List<AvatarBean> list) {
			this.list = list;
		}
	}
}
