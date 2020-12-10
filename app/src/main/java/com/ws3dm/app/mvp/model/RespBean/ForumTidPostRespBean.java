package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.RepliesBean;
import com.ws3dm.app.bean.UserDataBean;

import java.util.List;

/**
 * Author : DKjuan: 展示回帖列表 bean
 * <p>
 * Date :  2017/8/18  14:57
 */
public class ForumTidPostRespBean extends BaseRespBean<ForumTidPostRespBean.DataBean>{

	public static class DataBean {
		/**
		 * list : [{"pid":189136119,"position":5,"pubdate_at":1492082472,"content":"这~~怎么没人分享啊","user":{"uid":6892446,"nickname":"无尽的灾难","avatarstr":"http://user.3dmgame.com/data/avatar/006/89/24/46_avatar_small.jpg"},"replies":[{"pid":189135538,"content":"有重制版的。。。http://www.nexusmods.com/skyrimspecialedition/mods/7897/?","pubdate_at":1492081980,"user":{"nickname":"言小夏"}}]},{"pid":189136988,"position":7,"pubdate_at":1492083039,"content":"不管了，好歹这个也是v3.11的版本，那个是v1.1的，也不算是重复分流了~","user":{"uid":6892446,"nickname":"无尽的灾难","avatarstr":"http://user.3dmgame.com/data/avatar/006/89/24/46_avatar_small.jpg"},"replies":[{"pid":189136407,"content":"有啊，搜索\u201c宠物王国\u201d","pubdate_at":1492082580,"user":{"nickname":"言小夏"}}]},{"pid":189139807,"position":10,"pubdate_at":1492085036,"content":"这个好像是早期版本的分流？","user":{"uid":6892446,"nickname":"无尽的灾难","avatarstr":"http://user.3dmgame.com/data/avatar/006/89/24/46_avatar_small.jpg"},"replies":[{"pid":189138713,"content":"并没有搜到重置版的分流或者汉化","pubdate_at":1492084320,"user":{"nickname":"小恶童"}}]},{"pid":189142476,"position":14,"pubdate_at":1492086900,"content":"好吧，之前也没注意，随它吧，发都发了~","user":{"uid":6892446,"nickname":"无尽的灾难","avatarstr":"http://user.3dmgame.com/data/avatar/006/89/24/46_avatar_small.jpg"},"replies":[{"pid":189141213,"content":"看到了，那个也是新版的...N网上的重置版和原版的版本号不一样而已。算是小撞车吧 ...","pubdate_at":1492086060,"user":{"nickname":"小恶童"}}]}]
		 * total : 4
		 */

		private int total;
		private int isfavorite;//0未收藏1已收藏
		private List<RepliesBean> list;

		public int getIsfavorite() {
			return isfavorite;
		}

		public void setIsfavorite(int isfavorite) {
			this.isfavorite = isfavorite;
		}

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public List<RepliesBean> getList() {
			return list;
		}

		public void setList(List<RepliesBean> list) {
			this.list = list;
		}
	}
}
