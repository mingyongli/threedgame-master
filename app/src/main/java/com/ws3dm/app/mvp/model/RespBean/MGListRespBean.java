package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.bean.MGListBean;
import com.ws3dm.app.bean.SlidesBean;
import com.ws3dm.app.bean.SoftGameBean;

import java.util.List;

/**
 * Describution :手游的游戏和软件
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/20 15:17
 **/
public class MGListRespBean extends BaseRespBean<MGListRespBean.DataBean>{

	public static class DataBean {
		private List<MGListBean> list;
		private List<SlidesBean> slides;
		private List<SoftGameBean> hotgame;

		public List<MGListBean> getList() {
			return list;
		}

		public void setList(List<MGListBean> list) {
			this.list = list;
		}

		public List<SlidesBean> getSlides() {
			return slides;
		}

		public void setSlides(List<SlidesBean> slides) {
			this.slides = slides;
		}

		public List<SoftGameBean> getHotgame() {
			return hotgame;
		}

		public void setHotgame(List<SoftGameBean> hotgame) {
			this.hotgame = hotgame;
		}
	}
}
