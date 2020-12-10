package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.AvatarBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameOLitemRespBean extends BaseRespBean<GameOLitemRespBean.DataBean>{

	public static class DataBean {
		private List<AvatarBean> typelist;
		private List<AvatarBean> labellist;
		private List<AvatarBean> firmlist;

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

		public List<AvatarBean> getFirmlist() {
			return firmlist;
		}

		public void setFirmlist(List<AvatarBean> firmlist) {
			this.firmlist = firmlist;
		}
	}
}
