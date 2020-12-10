package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.MGListBean;
import com.ws3dm.app.bean.SoftGameBean;
import com.ws3dm.app.bean.VersionMG;

import java.util.List;

/**
 * Author : DKjuan:手游单个游戏详情
 * <p>
 * Date :  2017/8/21  12:06
 */
public class MGDetailRespBean extends BaseRespBean<MGDetailRespBean.DataBean>{

	public static class DataBean {
		/**
		 * aid : 10000
		 * arcurl : http://shouyou2.3dmgame.com/android/10000.html
		 * title : 王者荣耀
		 * litpic : http://shouyou2.3dmgame.com/uploadimg/ico/2018/0329/1522300652618348.png
		 * click : 2
		 * soft_size : 185.03MB
		 * soft_ver : 1.0
		 * pubdate_at : 1522300417
		 * typeid : 5
		 * type : 动作竞技
		 * language : 中文
		 * content : 
		 * downurl : http://dl6.cudown.com/3DMGAME_Travel_Adventures_World_Wonders.EN.Green.rar
		 * isfavorite : 1
		 * showtype : 4
		 * imgs : ["http://shouyou2.3dmgame.com/uploadimg/img/2018/0405/1522892840896390.jpg","http://shouyou2.3dmgame.com/uploadimg/img/2018/0405/1522892840509045.jpg","http://shouyou2.3dmgame.com/uploadimg/img/2018/0405/1522892840393345.jpg"]
		 * otbanben : [{"aid":"10014","arcurl":"http://shouyou2.3dmgame.com/android/10014.html","title":"其它版"},{"aid":"10016","arcurl":"http://shouyou2.3dmgame.com/android/10016.html","title":"九游版"},{"aid":"10017","arcurl":"http://shouyou2.3dmgame.com/android/10017.html","title":"百度版"},{"aid":"10019","arcurl":"http://shouyou2.3dmgame.com/android/10019.html","title":"其它版"}]
		 * othergames : [{"typeid":1,"type":"类似游戏","softlist":[{"aid":"10003","arcurl":"http://shouyou2.3dmgame.com/android/10003.html","title":"碧蓝航线","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0402/1522649149852538.jpg","soft_size":"356.48MB","soft_ver":"1.5.0","downurl":"https://down2.cudown.com/app/bilanhangxian.apk"},{"aid":"10005","arcurl":"http://shouyou2.3dmgame.com/android/10005.html","title":"圣城曙光","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0402/1522651157753137.png","soft_size":"130.22MB","soft_ver":"2.0.0","downurl":"http://directpage.paojiao.com/lzsh_kj2_zcl_001/index.html"},{"aid":"10008","arcurl":"http://shouyou2.3dmgame.com/android/10008.html","title":"孤岛先锋","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0404/1522832092526780.png","soft_size":"523.21MB","soft_ver":"1.0.1","downurl":"https://down4.cudown.com/app/gudaoxianfeng.apk"},{"aid":"10011","arcurl":"http://shouyou2.3dmgame.com/android/10011.html","title":"第五人格","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0405/1522891471770383.png","soft_size":"531.60MB","soft_ver":"1.5.4","downurl":"http://ugame.9game.cn/game/downloadGame?pack.cooperateModelId=87347&pack.id=18239014"},{"aid":"10069","arcurl":"http://shouyou2.3dmgame.com/android/10069.html","title":"绝地求生：刺激战场 中文","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0420/1524205662874622.png","soft_size":"669MB","soft_ver":"0.3.2","downurl":"http://down.s.qq.com/download/1106467070/apk/10023758_com.tencent.tmgp.pubgmhd.apk"}]},{"typeid":2,"type":"热门游戏","softlist":[{"aid":"10000","arcurl":"http://shouyou2.3dmgame.com/android/10000.html","title":"王者荣耀","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0329/1522300652618348.png","soft_size":"185.03MB","soft_ver":"1.0","desc":"","downurl":"http://dl6.cudown.com/3DMGAME_Travel_Adventures_World_Wonders.EN.Green.rar"},{"aid":"10003","arcurl":"http://shouyou2.3dmgame.com/android/10003.html","title":"碧蓝航线","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0402/1522649149852538.jpg","soft_size":"356.48MB","soft_ver":"1.5.0","desc":"","downurl":"https://down2.cudown.com/app/bilanhangxian.apk"},{"aid":"10004","arcurl":"http://shouyou2.3dmgame.com/android/10004.html","title":"聚仕通","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0402/1522649711558941.jpg","soft_size":"24.80MB","soft_ver":"1.1.1","desc":"","downurl":"https://down4.cudown.com/app/jushitong.apk"}]}]
		 */

		private String aid;
		private String arcurl;
		private String title;
		private String litpic;
		private int click;
		private String soft_size;
		private String soft_ver;
		private int pubdate_at;
		private int typeid;
		private String type;
		private String language;
		private String content;
		private String downurl;
		private int isfavorite;
		private int showtype;
		private List<String> imgs;
		private List<VersionMG> otbanben;
		private List<MGListBean> othergames;

		public String getAid() {
			return aid;
		}

		public void setAid(String aid) {
			this.aid = aid;
		}

		public String getArcurl() {
			return arcurl;
		}

		public void setArcurl(String arcurl) {
			this.arcurl = arcurl;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getLitpic() {
			return litpic;
		}

		public void setLitpic(String litpic) {
			this.litpic = litpic;
		}

		public int getClick() {
			return click;
		}

		public void setClick(int click) {
			this.click = click;
		}

		public String getSoft_size() {
			return soft_size;
		}

		public void setSoft_size(String soft_size) {
			this.soft_size = soft_size;
		}

		public String getSoft_ver() {
			return soft_ver;
		}

		public void setSoft_ver(String soft_ver) {
			this.soft_ver = soft_ver;
		}

		public int getPubdate_at() {
			return pubdate_at;
		}

		public void setPubdate_at(int pubdate_at) {
			this.pubdate_at = pubdate_at;
		}

		public int getTypeid() {
			return typeid;
		}

		public void setTypeid(int typeid) {
			this.typeid = typeid;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getDownurl() {
			return downurl;
		}

		public void setDownurl(String downurl) {
			this.downurl = downurl;
		}

		public int getIsfavorite() {
			return isfavorite;
		}

		public void setIsfavorite(int isfavorite) {
			this.isfavorite = isfavorite;
		}

		public int getShowtype() {
			return showtype;
		}

		public void setShowtype(int showtype) {
			this.showtype = showtype;
		}

		public List<String> getImgs() {
			return imgs;
		}

		public void setImgs(List<String> imgs) {
			this.imgs = imgs;
		}

		public List<VersionMG> getOtbanben() {
			return otbanben;
		}

		public void setOtbanben(List<VersionMG> otbanben) {
			this.otbanben = otbanben;
		}

		public List<MGListBean> getOthergames() {
			return othergames;
		}

		public void setOthergames(List<MGListBean> othergames) {
			this.othergames = othergames;
		}
	}
}
