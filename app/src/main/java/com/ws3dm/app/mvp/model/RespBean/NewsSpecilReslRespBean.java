package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.NewsBean;

import java.util.List;

/**
 * Author : DKjuan:攻略专区按字母检索页数据
 * <p>
 * Date :  2017/8/21  12:06
 */
public class NewsSpecilReslRespBean extends BaseRespBean<NewsSpecilReslRespBean.DataBean>{

	public static class DataBean {
		/**
		 * total : 1
		 * list : [{"aid":183,"title":"Artifact","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20181112/1542012641_987940.jpg","showtype":19}]
		 */

		private int total;
		private List<NewsBean> list;

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
