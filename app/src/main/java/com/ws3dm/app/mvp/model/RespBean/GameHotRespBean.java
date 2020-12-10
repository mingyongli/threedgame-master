package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.SlidesBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameHotRespBean extends BaseRespBean<GameHotRespBean.DataBean>{
	public static class DataBean {
		private List<GameBean> newgame;
		private List<GameBean> expectgame;
		private List<GameBean> classicgame;
		private List<GameBean> hotgame;
		private List<SlidesBean> slides;

		public List<GameBean> getNewgame() {
			return newgame;
		}

		public void setNewgame(List<GameBean> newgame) {
			this.newgame = newgame;
		}

		public List<GameBean> getExpectgame() {
			return expectgame;
		}

		public void setExpectgame(List<GameBean> expectgame) {
			this.expectgame = expectgame;
		}

		public List<GameBean> getClassicgame() {
			return classicgame;
		}

		public void setClassicgame(List<GameBean> classicgame) {
			this.classicgame = classicgame;
		}

		public List<GameBean> getHotgame() {
			return hotgame;
		}

		public void setHotgame(List<GameBean> hotgame) {
			this.hotgame = hotgame;
		}

		public List<SlidesBean> getSlides() {
			return slides;
		}

		public void setSlides(List<SlidesBean> slides) {
			this.slides = slides;
		}
	}
}
