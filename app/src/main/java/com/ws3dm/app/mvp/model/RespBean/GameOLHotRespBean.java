package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.GameHotPhb;
import com.ws3dm.app.bean.GameTestBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameOLHotRespBean extends BaseRespBean<GameOLHotRespBean.DataBean> {

	public static class DataBean {

		private int total;

		private List<GameHotPhb> list;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public List<GameHotPhb> getList() {
			return list;
		}

		public void setList(List<GameHotPhb> list) {
			this.list = list;
		}
	}
}
