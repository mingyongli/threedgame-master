package com.ws3dm.app.mvp.model.RespBean;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/17  16:22
 */
public class UserRespBean extends BaseRespBean<UserRespBean.DataBean>{

	public static class DataBean {
		/**
		 * nickname : 啦咯啦咯拒绝啦
		 * avatarstr : https://my.3dmgame.com/uploads/images/avatar/20180305/1520243225_999792.jpg
		 * uid : 12973244
		 * username : 3dm_12973244
		 * mobile : 13816190598
		 * integral : 499
		 * title : 钢铁直男
		 * title_level : 3
		 * level2 : 1
		 * level3 : 1
		 * level4 : 0
		 * up_nick : 0
		 * app_login : 1
		 * integralmsg : 
		 */

		private String nickname;
		private String avatarstr;
		private String uid;
		private String username;
		private String mobile;
		private int integral;
		private String title;
		private int title_level;
		private int level2;
		private int level3;
		private int level4;
		private int up_nick;
		private int app_login;
		private String integralmsg;

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

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public int getIntegral() {
			return integral;
		}

		public void setIntegral(int integral) {
			this.integral = integral;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getTitle_level() {
			return title_level;
		}

		public void setTitle_level(int title_level) {
			this.title_level = title_level;
		}

		public int getLevel2() {
			return level2;
		}

		public void setLevel2(int level2) {
			this.level2 = level2;
		}

		public int getLevel3() {
			return level3;
		}

		public void setLevel3(int level3) {
			this.level3 = level3;
		}

		public int getLevel4() {
			return level4;
		}

		public void setLevel4(int level4) {
			this.level4 = level4;
		}

		public int getUp_nick() {
			return up_nick;
		}

		public void setUp_nick(int up_nick) {
			this.up_nick = up_nick;
		}

		public int getApp_login() {
			return app_login;
		}

		public void setApp_login(int app_login) {
			this.app_login = app_login;
		}

		public String getIntegralmsg() {
			return integralmsg;
		}

		public void setIntegralmsg(String integralmsg) {
			this.integralmsg = integralmsg;
		}
	}
}
