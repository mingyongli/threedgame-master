package com.ws3dm.app.mvp.model.RespBean;

import com.google.gson.annotations.SerializedName;
import com.ws3dm.app.bean.AvatarBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameDJLabelRespBean extends BaseRespBean<GameDJLabelRespBean.DataBean>{

	public static class DataBean {
		private List<ListBean> list;

		public List<ListBean> getList() {
			return list;
		}

		public void setList(List<ListBean> list) {
			this.list = list;
		}

		public static class ListBean {
			/**
			 * name : A
			 * labels : [{"id":"81","name":"暗黑"}]
			 */

			private String name;
			private List<AvatarBean> labels;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public List<AvatarBean> getLabels() {
				return labels;
			}

			public void setLabels(List<AvatarBean> labels) {
				this.labels = labels;
			}
		}
	}
}
