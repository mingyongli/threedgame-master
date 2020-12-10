package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.SoftGameBean;

import java.util.List;

/**
 * Author : DKjuan:单机用的实体类
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameSYhotRespBean extends BaseRespBean<GameSYhotRespBean.DataBean>{
	public static class DataBean {
		private List<SoftGameBean> list;

		public List<SoftGameBean> getList() {
			return list;
		}

		public void setList(List<SoftGameBean> list) {
			this.list = list;
		}
	}
}
