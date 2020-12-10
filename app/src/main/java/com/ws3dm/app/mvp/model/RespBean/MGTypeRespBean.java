package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.AvatarBean;

import java.io.Serializable;
import java.util.List;

/**
 * Author : DKjuan:手游分类
 * <p>
 * Date :  2017/8/21  12:06
 */
public class MGTypeRespBean extends BaseRespBean<MGTypeRespBean.DataBean>{

	public static class DataBean {
		private List<ListBean> list;

		public List<ListBean> getList() {
			return list;
		}

		public void setList(List<ListBean> list) {
			this.list = list;
		}

		public static class ListBean implements Serializable {
			/**
			 * typeid : 5
			 * type : 动作竞技
			 * labels : [{"id":3,"name":"偶像"},{"id":8,"name":"战争"},{"id":21,"name":"躲避"}]
			 */

			private int typeid;
			private String type;
			private List<AvatarBean> labels;

			public int getTypeid() {
				return typeid;
			}

			public void setTypeid(int typeid) {
				this.typeid = typeid;
			}

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
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
