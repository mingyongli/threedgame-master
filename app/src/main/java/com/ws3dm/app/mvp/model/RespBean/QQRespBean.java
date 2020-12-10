package com.ws3dm.app.mvp.model.RespBean;

/**
 * Author : DKjuan:第三方登陆时返回信息，qq登陆，畅言登陆
 * <p>
 * Date :  2017/8/17  16:22
 */
public class QQRespBean extends BaseRespBean<QQRespBean.HtmlBean>{

	/**
	 * html : {"uid":"11994019","username":"dkjuan","credits":"0","grouptitle":"游戏菜鸟","posts":"0","threads":"0","authimg":"http://user.3dmgame.com/avatar.php?uid=11994019&size=middle"}
	 */

	private HtmlBean html;

	public HtmlBean getHtml() {
		return html;
	}

	public void setHtml(HtmlBean html) {
		this.html = html;
	}

	public static class HtmlBean {
		/**
		 * uid : 11994019
		 * username : dkjuan
		 * credits : 0
		 * grouptitle : 游戏菜鸟
		 * posts : 0
		 * threads : 0
		 * authimg : http://user.3dmgame.com/avatar.php?uid=11994019&size=middle
		 */

		private String uid;
		private String username;
		private String credits;
		private String grouptitle;
		private String posts;
		private String threads;
		private String authimg;

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getCredits() {
			return credits;
		}

		public void setCredits(String credits) {
			this.credits = credits;
		}

		public String getGrouptitle() {
			return grouptitle;
		}

		public void setGrouptitle(String grouptitle) {
			this.grouptitle = grouptitle;
		}

		public String getPosts() {
			return posts;
		}

		public void setPosts(String posts) {
			this.posts = posts;
		}

		public String getThreads() {
			return threads;
		}

		public void setThreads(String threads) {
			this.threads = threads;
		}

		public String getAuthimg() {
			return authimg;
		}

		public void setAuthimg(String authimg) {
			this.authimg = authimg;
		}
	}
}
