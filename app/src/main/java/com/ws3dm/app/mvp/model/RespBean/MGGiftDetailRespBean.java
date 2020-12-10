package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.GameGiftBean;
import com.ws3dm.app.bean.GameHotPhb;
import com.ws3dm.app.bean.SoftGameBean;

import java.util.List;

/**
 * Author : DKjuan:手游礼包,游戏分类跳转的列表共用
 * <p>
 * Date :  2017/8/21  12:06
 */
public class MGGiftDetailRespBean extends BaseRespBean<MGGiftDetailRespBean.DataBean>{

	public static class DataBean {
		/**
		 * aid : 10197
		 * arcurl : https://ol.3dmgame.com/hao/10197.html
		 * litpic : https://ol.3dmgame.com/uploads/images/thumbbig/20191121/1574329082_600426.jpg
		 * title : 《魔域》 全甲英豪回馈包
		 * showtype : 22
		 * webviewurl : 
		 * range_start : 2019-11-21
		 * range_end : 2019-12-06
		 * surplusper : 99
		 * is_over : 1
		 * is_empty : 0
		 * is_avail : 0
		 * content : 礼包内容： 第一天登录上线：三倍经验药水*1、极致月光贵族包*1、月之祝福*1、鸿运魔石小红包*3、蜜恋花语兽礼包X5*1第二天登录上线：晶彩魔魂礼包*2、装备锻造百元包*1、极致月光贵族包*2、暗夜魔晶礼包*1、蜜恋花语兽礼包X5*1第三天登录上线：神兽灵药幸运包*1(怀旧送副宠幸运包*1)、魔棋耀金箱*1、蜜恋花语兽礼包X5*1、幸运药水+3*1、超能魔晶礼包*2第四天登录上线等级达到110级：100小时经验石*1、魔幻灵魂礼包*1、8星O新年箱*1、鸿运魔石小红包*3、额外抽奖次数*1第五天登录上线等级达到130级：华贵星石礼盒*1(怀旧版送好运经验石*1)、65级神火碎晶*1(怀旧版送魔幻灵魂礼包*1)、神之祝福*1、传说神火秘宝匣*1(怀旧版送奇异兽精华*1)、战斗力+5*1、额外抽奖次数*2卡激活地址：登录游戏，通过激活指引员NPC进行激活有效期：11月21日-12月6日激活方式：直接登录游戏通过领奖NPC旁礼包激活指引员NPC进行激活，然后再前往领奖NPC女王特使·【领奖处】 蕾亚莉·罗兰德（220,632）领取礼包；游戏名称：魔域官网地址：http://my.99.com/index/ 开发公司：天晴数码娱乐公司运营公司：网龙网络控股有限公司游戏类型：MMORPG游戏画面：2.5D客户端下载地址：http://my.99.com/download/ 账号注册地址：https://reg.99.com/NDuser_Register_new.aspx?url=http://my.99.com 游戏介绍： 《魔域》是网龙公司自主研发的大型魔幻题材网络游戏，以一人多宠的"幻兽系统"、"导师系统"、"副本系统"以及各种职业技能、武器装备锻造系统作为主打特色，融合中西方的游戏元素，采用四版本一客户端同时运营的模式，打造出美轮美奂的游戏画面和流畅华丽的人物动作。
		 * relate_libao : [{"aid":10196,"arcurl":"https://ol.3dmgame.com/hao/10196.html","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20191121/1574329082_600426.jpg","title":"《魔域》 全甲新兵成长包","showtype":22,"webviewurl":"","range_start":"2019-11-21","range_end":"2019-12-06","surplusper":99,"is_over":1,"is_empty":0,"is_avail":0},{"aid":10184,"arcurl":"https://ol.3dmgame.com/hao/10184.html","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20191121/1574329082_600426.jpg","title":"《魔域》 欢乐奇幻回馈包","showtype":22,"webviewurl":"","range_start":"2019-10-24","range_end":"2019-11-10","surplusper":98,"is_over":1,"is_empty":0,"is_avail":0},{"aid":10183,"arcurl":"https://ol.3dmgame.com/hao/10183.html","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20191121/1574329082_600426.jpg","title":"《魔域》 欢乐奇幻成长包","showtype":22,"webviewurl":"","range_start":"2019-10-24","range_end":"2019-11-10","surplusper":99,"is_over":1,"is_empty":0,"is_avail":0}]
		 * game : []
		 */

		private int aid;
		private String arcurl;
		private String litpic;
		private String title;
		private int showtype;
		private String webviewurl;
		private String range_start;
		private String range_end;
		private int surplusper;
		private int is_over;
		private int is_empty;
		private int is_avail;
		private String content;
		private List<GameGiftBean> relate_libao;
		private GameHotPhb game;

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

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
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

		public String getRange_start() {
			return range_start;
		}

		public void setRange_start(String range_start) {
			this.range_start = range_start;
		}

		public String getRange_end() {
			return range_end;
		}

		public void setRange_end(String range_end) {
			this.range_end = range_end;
		}

		public int getSurplusper() {
			return surplusper;
		}

		public void setSurplusper(int surplusper) {
			this.surplusper = surplusper;
		}

		public int getIs_over() {
			return is_over;
		}

		public void setIs_over(int is_over) {
			this.is_over = is_over;
		}

		public int getIs_empty() {
			return is_empty;
		}

		public void setIs_empty(int is_empty) {
			this.is_empty = is_empty;
		}

		public int getIs_avail() {
			return is_avail;
		}

		public void setIs_avail(int is_avail) {
			this.is_avail = is_avail;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public List<GameGiftBean> getRelate_libao() {
			return relate_libao;
		}

		public void setRelate_libao(List<GameGiftBean> relate_libao) {
			this.relate_libao = relate_libao;
		}

		public GameHotPhb getGame() {
			return game;
		}

		public void setGame(GameHotPhb game) {
			this.game = game;
		}
	}
}
