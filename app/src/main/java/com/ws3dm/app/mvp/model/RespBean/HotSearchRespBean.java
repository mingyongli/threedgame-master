package com.ws3dm.app.mvp.model.RespBean;

import java.util.List;

/**
 * Author : DKjuan:热门搜索
 * <p>
 * Date :  2017/8/21  12:06
 */
public class HotSearchRespBean extends BaseRespBean<HotSearchRespBean.DataBean>{

	public static class DataBean {
		private List<SearchBean> list;

		public List<SearchBean> getList() {
			return list;
		}

		public void setList(List<SearchBean> list) {
			this.list = list;
		}

		public static class SearchBean {
			/**
			 * id : 1
			 * keyword : 上古卷轴
			 */

			private int id;
			private String keyword;

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getKeyword() {
				return keyword;
			}

			public void setKeyword(String keyword) {
				this.keyword = keyword;
			}
			@Override
			public boolean equals(Object o) {
				SearchBean inItem = (SearchBean)o;
				return keyword.equals(inItem.getKeyword());
			}
		}
	}
}
