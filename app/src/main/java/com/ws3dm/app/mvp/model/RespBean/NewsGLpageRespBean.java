package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.bean.NewsBean;

import java.util.List;

/**
 * Author : DKjuan:用于获取攻略封面页数据
 * <p>
 * Date :  2017/8/21  12:06
 */
public class NewsGLpageRespBean extends BaseRespBean<NewsGLpageRespBean.DataBean>{
	public static class DataBean {
		private List<NewsBean> danjilist;
		private List<NewsBean> shouyoulist;
		private List<NewsBean> wangyoulist;

		public List<NewsBean> getDanjilist() {
			return danjilist;
		}

		public void setDanjilist(List<NewsBean> danjilist) {
			this.danjilist = danjilist;
		}

		public List<NewsBean> getShouyoulist() {
			return shouyoulist;
		}

		public void setShouyoulist(List<NewsBean> shouyoulist) {
			this.shouyoulist = shouyoulist;
		}

		public List<NewsBean> getWangyoulist() {
			return wangyoulist;
		}

		public void setWangyoulist(List<NewsBean> wangyoulist) {
			this.wangyoulist = wangyoulist;
		}
	}
}
