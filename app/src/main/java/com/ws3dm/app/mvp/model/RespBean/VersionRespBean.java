package com.ws3dm.app.mvp.model.RespBean;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/17  16:22
 */
public class VersionRespBean extends BaseRespBean<VersionRespBean.DataBean>{

	public static class DataBean {
		/**
		 * version : 2.0
		 * description : 1、增加论坛发帖回帖功能
		 2、新增个人中心设置
		 3、增加手游网游资讯
		 */

		private String version;
		private String description;

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}
}
