package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.GonglueBean;

import java.util.List;

/**
 * Author : DKjuan:获取攻略专区攻略标签详情数据
 * <p>
 * Date :  2017/8/21  12:06
 */
public class NewsGLzqlabelpaReslRespBean extends BaseRespBean<NewsGLzqlabelpaReslRespBean.DataBean>{

	public static class DataBean {
		private List<GonglueBean> list;

		public List<GonglueBean> getList() {
			return list;
		}

		public void setList(List<GonglueBean> list) {
			this.list = list;
		}
	}
}
