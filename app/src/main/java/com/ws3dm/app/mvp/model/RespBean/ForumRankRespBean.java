package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.ForumBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  9:52
 */
public class ForumRankRespBean extends BaseRespBean<ForumRankRespBean.DataBean>{

	public static class DataBean {
		private List<ForumBean> list;

		public List<ForumBean> getList() {
			return list;
		}

		public void setList(List<ForumBean> list) {
			this.list = list;
		}
	}
}
