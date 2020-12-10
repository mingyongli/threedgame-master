package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.AvatarBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameDJitemRespBean extends BaseRespBean<GameDJitemRespBean.DataBean>{
	public static class DataBean {
		private List<AvatarBean> platlist;
		private List<AvatarBean> langlist;
		private List<AvatarBean> typelist;

		public List<AvatarBean> getPlatlist() {
			return platlist;
		}

		public void setPlatlist(List<AvatarBean> platlist) {
			this.platlist = platlist;
		}

		public List<AvatarBean> getLanglist() {
			return langlist;
		}

		public void setLanglist(List<AvatarBean> langlist) {
			this.langlist = langlist;
		}

		public List<AvatarBean> getTypelist() {
			return typelist;
		}

		public void setTypelist(List<AvatarBean> typelist) {
			this.typelist = typelist;
		}
	}
}
