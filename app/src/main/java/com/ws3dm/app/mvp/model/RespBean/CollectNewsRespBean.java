package com.ws3dm.app.mvp.model.RespBean;

import com.google.gson.annotations.SerializedName;
import com.ws3dm.app.bean.NewsBean;

import java.util.List;

/**
 * Describution :
 * 
 * Author : DKjuan
 * 
 * Date : 2018/3/27 18:36
 **/
public class CollectNewsRespBean extends BaseRespBean<CollectNewsRespBean.DataBean>{


	public static class DataBean {
		private List<NewsBean> list;

		public List<NewsBean> getList() {
			return list;
		}

		public void setList(List<NewsBean> list) {
			this.list = list;
		}
	}
}
