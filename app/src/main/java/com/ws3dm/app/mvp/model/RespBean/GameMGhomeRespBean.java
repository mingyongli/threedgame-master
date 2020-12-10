package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.GameGiftBean;
import com.ws3dm.app.bean.GameHotPhb;
import com.ws3dm.app.bean.GameTestBean;
import com.ws3dm.app.bean.OriginalBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameMGhomeRespBean extends BaseRespBean<GameMGhomeRespBean.DataBean>{

	public static class DataBean {
		private List<OriginalBean> evaluatlist;
		private List<GameTestBean> anli;
		private List<GameGiftBean> libbao;
		private List<GameTestBean> slides;

		public List<OriginalBean> getEvaluatlist() {
			return evaluatlist;
		}

		public void setEvaluatlist(List<OriginalBean> evaluatlist) {
			this.evaluatlist = evaluatlist;
		}

		public List<GameTestBean> getAnli() {
			return anli;
		}

		public void setAnli(List<GameTestBean> anli) {
			this.anli = anli;
		}

		public List<GameGiftBean> getLibbao() {
			return libbao;
		}

		public void setLibbao(List<GameGiftBean> libbao) {
			this.libbao = libbao;
		}

		public List<GameTestBean> getSlides() {
			return slides;
		}

		public void setSlides(List<GameTestBean> slides) {
			this.slides = slides;
		}
	}
}
