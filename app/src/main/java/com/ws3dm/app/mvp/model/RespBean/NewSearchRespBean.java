package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.SearchBean;

import java.util.List;

/**
 * Describution :
 * 
 * Author : DKjuan
 * 
 * Date : 2018/3/27 18:36
 **/
public class NewSearchRespBean extends BaseRespBean<NewSearchRespBean.DataBean>{


	public static class DataBean {
		private int total;
		private List<SearchBean> list;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public List<SearchBean> getList() {
			return list;
		}

		public void setList(List<SearchBean> list) {
			this.list = list;
		}
	}
}
