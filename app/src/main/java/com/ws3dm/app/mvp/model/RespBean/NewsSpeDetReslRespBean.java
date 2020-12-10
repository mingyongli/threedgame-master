package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.GllistBean;
import com.ws3dm.app.bean.GonglueBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.PiclistBean;

import java.util.List;

/**
 * Author : DKjuan:用于获取攻略专区检索页数据
 * <p>
 * Date :  2017/8/21  12:06
 */
public class NewsSpeDetReslRespBean extends BaseRespBean<NewsSpeDetReslRespBean.DataBean>{
	public static class DataBean {
		private List<PiclistBean> piclist;
		private List<GllistBean> gllist;
		private int is_fullgl;//是否显示全部攻略:1是0否
		private int total;
		private List<NewsBean> list;

		public List<PiclistBean> getPiclist() {
			return piclist;
		}

		public void setPiclist(List<PiclistBean> piclist) {
			this.piclist = piclist;
		}

		public List<GllistBean> getGllist() {
			return gllist;
		}

		public void setGllist(List<GllistBean> gllist) {
			this.gllist = gllist;
		}

		public int getIs_fullgl() {
			return is_fullgl;
		}

		public void setIs_fullgl(int is_fullgl) {
			this.is_fullgl = is_fullgl;
		}

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public List<NewsBean> getList() {
			return list;
		}

		public void setList(List<NewsBean> list) {
			this.list = list;
		}
	}
}
