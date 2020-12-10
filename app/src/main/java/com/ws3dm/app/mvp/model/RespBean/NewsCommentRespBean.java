package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.CommentBean;

import java.util.List;

/**
 * Author : DKjuan:新闻页评论列表
 * <p>
 * Date :  2017/8/21  12:06
 */
public class NewsCommentRespBean extends BaseRespBean<NewsCommentRespBean.DataBean>{

	public static class DataBean {
		/**
		 * list : [{"id":32,"content":"《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费","position":3,"goodcount":0,"badcount":0,"pubdate_at":1522410082,"user":{"uid":9663679,"nickname":"3DM用户","avatarstr":"http://my.3dmgame.com/uploads/images/avatar/default.jpg","gender":1,"regionstr":"上海宝山区"},"replies":[{"id":14,"position":1,"goodcount":0,"badcount":0,"pubdate_at":1512633145,"user":{"uid":9663679,"nickname":"3DM用户","avatarstr":"http://my.3dmgame.com/uploads/images/avatar/default.jpg","gender":1,"regionstr":"上海宝山区"},"content":"《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换","praise":0}],"praise":0},{"id":30,"content":"测试评论app的评论接口添加评论接口","position":2,"goodcount":0,"badcount":0,"pubdate_at":1522158685,"user":{"uid":9663679,"nickname":"3DM用户","avatarstr":"http://my.3dmgame.com/uploads/images/avatar/default.jpg","gender":1,"regionstr":"上海宝山区"},"replies":[],"praise":0},{"id":14,"content":"《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换","position":1,"goodcount":0,"badcount":0,"pubdate_at":1512633145,"user":{"uid":9663679,"nickname":"3DM用户","avatarstr":"http://my.3dmgame.com/uploads/images/avatar/default.jpg","gender":1,"regionstr":"上海宝山区"},"replies":[],"praise":0}]
		 * total : 2
		 * c_sid : 7
		 */

		private int total;
		private int c_sid;
		private List<CommentBean> list;
		private List<CommentBean> timelist;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public int getC_sid() {
			return c_sid;
		}

		public void setC_sid(int c_sid) {
			this.c_sid = c_sid;
		}

		public List<CommentBean> getList() {
			return list;
		}

		public void setList(List<CommentBean> list) {
			this.list = list;
		}

		public List<CommentBean> getTimelist() {
			return timelist;
		}

		public void setTimelist(List<CommentBean> timelist) {
			this.timelist = timelist;
		}
	}
}
