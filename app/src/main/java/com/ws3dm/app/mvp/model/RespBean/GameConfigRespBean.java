package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.AvatarBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameConfigRespBean extends BaseRespBean<GameConfigRespBean.DataBean>{
	public static class DataBean {
		private List<ConfiglistBean> list;

		public List<ConfiglistBean> getList() {
			return list;
		}

		public void setList(List<ConfiglistBean> list) {
			this.list = list;
		}

		public static class ConfiglistBean {
			/**
			 * type : 最低配置
			 * configlist : [{"title":"操作系统","content":"Windows 7 64bit or better"},{"title":"CPU","content":"Intel Core i5-2300 @ 2.80 GHz"},{"title":"内存","content":"6GB RAM"},{"title":"显卡","content":"NVIDIA GeForce GTX 460"},{"title":"存储空间","content":"需要45GB可用空间"}]
			 */

			private String type;
			private List<AvatarBean> configlist;

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public List<AvatarBean> getConfiglist() {
				return configlist;
			}

			public void setConfiglist(List<AvatarBean> configlist) {
				this.configlist = configlist;
			}
		}
	}
}
