package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.bean.MGListBean;
import com.ws3dm.app.bean.VersionMG;

import java.util.List;

/**
 * Author : DKjuan:手游单个游戏详情
 * <p>
 * Date :  2017/8/21  12:06
 */
public class MGAroundRespBean extends BaseRespBean<MGAroundRespBean.DataBean>{

	public static class DataBean {
		private List<VersionMG> libao;
		private List<AvatarBean> labels;
		private List<MGListBean> list;

		public List<VersionMG> getLibao() {
			return libao;
		}

		public void setLibao(List<VersionMG> libao) {
			this.libao = libao;
		}

		public List<AvatarBean> getLabels() {
			return labels;
		}

		public void setLabels(List<AvatarBean> labels) {
			this.labels = labels;
		}

		public List<MGListBean> getList() {
			return list;
		}

		public void setList(List<MGListBean> list) {
			this.list = list;
		}
	}
}
