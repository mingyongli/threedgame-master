package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.NewsBean;

import java.util.List;

/**
 * Author : DKjuan:新闻页分类列表
 * <p>
 * Date :  2017/8/21  12:06
 */
public class OriginalListRespBean extends BaseRespBean<OriginalListRespBean.DataBean>{

	public static class DataBean {
		/**
		 * info : {"aid":6,"arcurl":"http://www2.3dmgame.com/original/xinxianshi","litpic":"http://www2.3dmgame.com/uploads/images/thumbcolumnfirst/20180528/1527507586_403360.jpg","desc":"在发现之前喋喋不休","pubdate_at":1527504975,"showtype":11,"webviewurl":"http://www2.3dmgame.com/webview/original/xinxianshi","user":{"id":21,"nickname":"四氧化三铁","avatarstr":"http://work.3dmgame.com/uploads/images/users/21.jpg"}}
		 * total : 0
		 * list : []
		 */

		private InfoBean info;
		private String total;
		private List<NewsBean> list;

		public InfoBean getInfo() {
			return info;
		}

		public void setInfo(InfoBean info) {
			this.info = info;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public List<NewsBean> getList() {
			return list;
		}

		public void setList(List<NewsBean> list) {
			this.list = list;
		}

		public static class InfoBean {
			/**
			 * aid : 6
			 * arcurl : http://www2.3dmgame.com/original/xinxianshi
			 * litpic : http://www2.3dmgame.com/uploads/images/thumbcolumnfirst/20180528/1527507586_403360.jpg
			 * desc : 在发现之前喋喋不休
			 * pubdate_at : 1527504975
			 * showtype : 11
			 * webviewurl : http://www2.3dmgame.com/webview/original/xinxianshi
			 * user : {"id":21,"nickname":"四氧化三铁","avatarstr":"http://work.3dmgame.com/uploads/images/users/21.jpg"}
			 */

			private int aid;
			private String arcurl;
			private String litpic;
			private String desc;
			private int pubdate_at;
			private int showtype;
			private String webviewurl;
			private UserBean user;

			public int getAid() {
				return aid;
			}

			public void setAid(int aid) {
				this.aid = aid;
			}

			public String getArcurl() {
				return arcurl;
			}

			public void setArcurl(String arcurl) {
				this.arcurl = arcurl;
			}

			public String getLitpic() {
				return litpic;
			}

			public void setLitpic(String litpic) {
				this.litpic = litpic;
			}

			public String getDesc() {
				return desc;
			}

			public void setDesc(String desc) {
				this.desc = desc;
			}

			public int getPubdate_at() {
				return pubdate_at;
			}

			public void setPubdate_at(int pubdate_at) {
				this.pubdate_at = pubdate_at;
			}

			public int getShowtype() {
				return showtype;
			}

			public void setShowtype(int showtype) {
				this.showtype = showtype;
			}

			public String getWebviewurl() {
				return webviewurl;
			}

			public void setWebviewurl(String webviewurl) {
				this.webviewurl = webviewurl;
			}

			public UserBean getUser() {
				return user;
			}

			public void setUser(UserBean user) {
				this.user = user;
			}

			public static class UserBean {
				/**
				 * id : 21
				 * nickname : 四氧化三铁
				 * avatarstr : http://work.3dmgame.com/uploads/images/users/21.jpg
				 */

				private int id;
				private String nickname;
				private String avatarstr;

				public int getId() {
					return id;
				}

				public void setId(int id) {
					this.id = id;
				}

				public String getNickname() {
					return nickname;
				}

				public void setNickname(String nickname) {
					this.nickname = nickname;
				}

				public String getAvatarstr() {
					return avatarstr;
				}

				public void setAvatarstr(String avatarstr) {
					this.avatarstr = avatarstr;
				}
			}
		}
	}
}
