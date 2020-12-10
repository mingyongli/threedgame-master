package com.ws3dm.app.mvp.model.RespBean;

import com.google.gson.annotations.SerializedName;
import com.ws3dm.app.bean.ForumGidBean;
import com.ws3dm.app.bean.NewsBar;

import java.util.List;

/**
 * Author : DKjuan: 获取板块类型标签列表 bean
 * <p>
 * Date :  2017/8/18  14:57
 */
public class ForumTidTypeRespBean extends BaseRespBean<ForumTidTypeRespBean.DataBean>{

	public static class DataBean {
		private List<ForumGidBean> list;

		public List<ForumGidBean> getList() {
			return list;
		}

		public void setList(List<ForumGidBean> list) {
			this.list = list;
		}
	}
}
