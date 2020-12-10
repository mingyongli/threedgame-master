package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.AvatarBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameCategoryRespBean extends BaseRespBean<GameCategoryRespBean.DataBean>{

	public static class DataBean {
		private List<AvatarBean> platlist;
		private List<AvatarBean> typelist;
		private List<AvatarBean> labellist;

		public List<AvatarBean> getPlatlist() {
			return platlist;
		}

		public void setPlatlist(List<AvatarBean> platlist) {
			this.platlist = platlist;
		}

		public List<AvatarBean> getTypelist() {
			return typelist;
		}

		public void setTypelist(List<AvatarBean> typelist) {
			this.typelist = typelist;
		}

		public List<AvatarBean> getLabellist() {
			return labellist;
		}

		public void setLabellist(List<AvatarBean> labellist) {
			this.labellist = labellist;
		}
	}
}
