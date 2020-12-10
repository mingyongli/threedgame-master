package com.ws3dm.app.bean;

import java.io.Serializable;

public class UserDataBean implements Serializable {
	/**
	 * phone:用户头像，ID，用户名，昵称
	 * 
	 * @since 1.0.0
	 *
	"gender": 1,
	"regionstr": "上海宝山区"
	 */
	public String avatarstr = "";
	public String uid = "0";
	public String regionstr = "0";
	public String username = "";
	public String nickname = "";
	public int title_level =0;//"title_level": 0
	public String title = "";// "3DMer",
	public int integral;//我的积分
	public int is_follow;
	public int user_level;
	public int auth_level;
	public String auth_title;

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

	public String getRegionstr() {
		return regionstr;
	}

	public void setRegionstr(String regionstr) {
		this.regionstr = regionstr;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getTitle_level() {
		return title_level;
	}

	public void setTitle_level(int title_level) {
		this.title_level = title_level;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public boolean isRemember() {
		return isRemember;
	}

	public void setRemember(boolean remember) {
		isRemember = remember;
	}

	public boolean isThirdPartLogin() {
		return isThirdPartLogin;
	}

	public void setThirdPartLogin(boolean thirdPartLogin) {
		isThirdPartLogin = thirdPartLogin;
	}

	public int getOpenType() {
		return openType;
	}

	public void setOpenType(int openType) {
		this.openType = openType;
	}

	public boolean isLoginStatue() {
		return loginStatue;
	}

	public void setLoginStatue(boolean loginStatue) {
		this.loginStatue = loginStatue;
	}

	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
	}

	public String getAppopenid() {
		return appopenid;
	}

	public void setAppopenid(String appopenid) {
		this.appopenid = appopenid;
	}

	public int getIs_follow() {
		return is_follow;
	}

	public void setIs_follow(int is_follow) {
		this.is_follow = is_follow;
	}

	public int getUser_level() {
		return user_level;
	}

	public void setUser_level(int user_level) {
		this.user_level = user_level;
	}

	public int getAuth_level() {
		return auth_level;
	}

	public void setAuth_level(int auth_level) {
		this.auth_level = auth_level;
	}

	public String getAuth_title() {
		return auth_title;
	}

	public void setAuth_title(String auth_title) {
		this.auth_title = auth_title;
	}






	/**
	 * password:密码
	 * 
	 * @since 1.0.0
	 */
	public String userPassword = "";
	public String mobile = "";
	/**
	 * isRemember:是否记住密码
	 * 
	 * @since 1.0.0
	 */
	public boolean isRemember;
	/**
	 * isThirdPartLogin:是否第三方登录
	 * 
	 * @since 1.0.0
	 */
	public boolean isThirdPartLogin = false;
	public int openType=0;//1qq2微博3微信(绑定类型)
	/**
	 * 登陆状态
	 * @since 1.0.0
	 */
	public boolean loginStatue = false;
	/**
	 * isThirdPartLogin:第三方登陆的openID
	 */
	public String openID ;
	public String appopenid="" ;

}
