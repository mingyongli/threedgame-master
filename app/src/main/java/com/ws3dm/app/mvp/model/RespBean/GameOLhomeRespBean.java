package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.bean.GameGiftBean;
import com.ws3dm.app.bean.GameHotPhb;
import com.ws3dm.app.bean.GameTestBean;
import com.ws3dm.app.bean.NewsBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameOLhomeRespBean extends BaseRespBean<GameOLhomeRespBean.DataBean>{

	public static class DataBean {
		private List<GameTestBean> newtest;
		private List<GameTestBean> esport_zt;
		private List<GameGiftBean> libbao;
		private List<GameHotPhb> hot_phb;
		private List<GameTestBean> slides;

		public List<GameTestBean> getNewtest() {
			return newtest;
		}

		public void setNewtest(List<GameTestBean> newtest) {
			this.newtest = newtest;
		}

		public List<GameTestBean> getEsport_zt() {
			return esport_zt;
		}

		public void setEsport_zt(List<GameTestBean> esport_zt) {
			this.esport_zt = esport_zt;
		}

		public List<GameGiftBean> getLibbao() {
			return libbao;
		}

		public void setLibbao(List<GameGiftBean> libbao) {
			this.libbao = libbao;
		}

		public List<GameHotPhb> getHot_phb() {
			return hot_phb;
		}

		public void setHot_phb(List<GameHotPhb> hot_phb) {
			this.hot_phb = hot_phb;
		}

		public List<GameTestBean> getSlides() {
			return slides;
		}

		public void setSlides(List<GameTestBean> slides) {
			this.slides = slides;
		}
	}
}
