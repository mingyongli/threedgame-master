package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.NewsBean;

import java.util.List;

/**
 * Author : DKjuan:用于获取攻略封面页数据
 * <p>
 * Date :  2017/8/21  12:06
 */
public class NewsGLspecialRespBean extends BaseRespBean<NewsGLspecialRespBean.DataBean>{

	public static class DataBean {
		private List<ListGong> list;

		public List<ListGong> getList() {
			return list;
		}

		public void setList(List<ListGong> list) {
			this.list = list;
		}

		public static class ListGong {
			/**
			 * name : A
			 * list : [{"aid":183,"title":"Artifact","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20181112/1542012641_987940.jpg","showtype":19}]
			 */

			private String name;
			private List<NewsBean> list;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public List<NewsBean> getList() {
				return list;
			}

			public void setList(List<NewsBean> list) {
				this.list = list;
			}
		}
	}
}
