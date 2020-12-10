package com.ws3dm.app.mvp.model.RespBean;

import java.util.List;

/**
 * Author : DKjuan:新闻页详情列表 数据bean
 * <p>
 * Date :  2017/8/18  18:04
 */
public class FgNewsDetailRespBean extends BaseRespBean<FgNewsDetailRespBean.ChannelBean>{

	/**
	 * code : 1
	 * banner : {"code":1,"html":[{"arcurl":"http://m.3dmgame.com/news/201708/3680777_app.html","title":"《刺客信条：起源》最新截图放出 黑魂巫师3合体","litpic":"http://aimg.3dmgame.com/uploads/allimg/170222/11111111111111111111111111111111111111111316-1F222155107.jpg","id":"3680777","senddate":"2014-07-14","description":"《刺客信条：起源》最新截图放出 黑魂巫师3合体","lmfl":"幻灯","changyan_id":"news_3680777"},{"arcurl":"http://m.3dmgame.com/news/201708/3680844_app.html","title":"《贼海》PC版本全新演示 21:9显示器玩游戏超震撼","litpic":"http://aimg.3dmgame.com/uploads/allimg/170222/111111111111111111111111111111111111111111316-1F222155107-50.jpg","id":"3680844","senddate":"2014-07-14","description":"《贼海》PC版本全新演示 21:9显示器玩游戏超震撼","lmfl":"幻灯","changyan_id":"news_3680844"},{"arcurl":"http://m.3dmgame.com/news/201708/3680819_app.html","title":"近距离感受核泄漏灾难 《切尔诺贝利VR计划》情报","litpic":"http://aimg.3dmgame.com/uploads/allimg/170222/111111111111111111111111111111111111111111111316-1F222155107-51.jpg","id":"3680819","senddate":"2014-07-14","description":"近距离感受核泄漏灾难 《切尔诺贝利VR计划》情报","lmfl":"幻灯","changyan_id":"news_3680819"},{"arcurl":"http://m.3dmgame.com/news/201708/3680861_app.html","title":"《零之轨迹》+《碧之轨迹》8月24日登陆WeGame","litpic":"http://aimg.3dmgame.com/uploads/allimg/170222/1111111111111111111111111111111111316-1F222155107-52.jpg","id":"3680861","senddate":"2014-07-14","description":"《零之轨迹》+《碧之轨迹》8月24日登陆WeGame","lmfl":"幻灯","changyan_id":"news_3680861"},{"arcurl":"http://m.3dmgame.com/news/201708/3680832_app.html","title":"游戏画面大变化 玩家痴迷于《无人深空》新版草地","litpic":"http://aimg.3dmgame.com/uploads/allimg/170222/111111111111111111111111111111111111111111316-1F222155107-53.jpg","id":"3680832","senddate":"2014-07-14","description":"游戏画面大变化 玩家痴迷于《无人深空》新版草地","lmfl":"幻灯","changyan_id":"news_3680832"}]}
	 * channel : {"code":1,"totalrow":500,"html":[{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680967_app.html","title":"游戏晚报|《刺客信条》最新截图 卡普空周末促销","senddate":"16分钟前","feedback":"0","id":"3680967","changyan_id":"news_3680967","description":"单机RPG《暗影：觉醒》公布 首批细节和截图；\r\n《刺客信条：起源》最新截图放出 黑魂巫师3合体；\r\n游戏太恐怖 3DM《阴暗森林》正式版免安装未加密版","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170515/322_170515183640_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818162846_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818162846_2_app.jpg"]],"arttype":2},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680963_app.html","title":"《绝地求生》FPS模式很棒 但这是款第三人称游戏","senddate":"26分钟前","feedback":"0","id":"3680963","changyan_id":"news_3680963","description":"我依然非常喜欢《绝地求生：大逃杀》的第一人称模式，而且以后我可能基本都会用第一人称去玩，但是这不代表游戏已经针对第一人称做好了设计。这就是一款为第三人称设计的游戏，各方面表现都是如此。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/154_170818173906_1_app.jpg"]],"arttype":1},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680931_app.html","title":"3DM汉化组制作《深圳I/O》完整汉化版发布下载","senddate":"1小时前","feedback":"0","id":"3680931","changyan_id":"news_3680931","description":"《深圳I/O（SHENZHEN I/O）》3DM汉化组简体汉化补丁v1.0及汉化硬盘版，本补丁仅作学习和交流之用，任何个人及组织未经本组同意，不得用作商业用途。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/369_170818163532_1_app.png"],["http://aimg.3dmgame.com/uploads/allimg/170818/369_170818163800_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/369_170818163800_2_app.jpg"]],"arttype":2},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680917_app.html","title":"Twitch黑科技：观众可搜索使用想看英雄的直播间","senddate":"1小时前","feedback":"0","id":"3680917","changyan_id":"news_3680917","description":"观众可以直接在Twitch的《守望先锋》目录里根据自己想要看的英雄来选择直播间。在《炉石传说》目录里，玩家可以根据英雄职业和游戏模式来选择直播间，比如竞技场直播和天梯直播会分开。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/154_170818160545_1.jpeg"]],"arttype":1},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680916_app.html","title":"《莎木3》新图像：男女主角亮相 小镇和长城好美","senddate":"1小时前","feedback":"0","id":"3680916","changyan_id":"news_3680916","description":"昨日《莎木3》官方发布公告，确定与Deep Silver合作，Deep Silver将负责《莎木3》的全球发行任务。今日出版商发布了一份新闻稿，包括 《莎木3》游戏的新图像放出。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818160123_1_app.jpg"]],"arttype":1},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680915_app.html","title":"《英雄传说 闪之轨迹3》原声限定版公开试听影像","senddate":"2小时前","feedback":"0","id":"3680915","changyan_id":"news_3680915","description":"日本Falcom预定于2017年9月28日发售PS4平台游戏《英雄传说 闪之轨迹3》初回限定KISEKI BOX同捆的迷你原声限定版公开试听影像，视频中还包含了不少游戏新画面。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818155331_1_app.jpg"]],"arttype":1},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680914_app.html","title":"日本市场软硬件销量周榜：Switch销量即将破百万","senddate":"2小时前","feedback":"0","id":"3680914","changyan_id":"news_3680914","description":"任天堂Switch上周在日本的销量比前一周提升了10%，这是来自《FAMI通》的数据，看来虽然近期没有太强力的新作，但玩家对任天堂新主机的购买欲依然不减。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170813/369_170813205457_1_app.jpg"]],"arttype":1},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680908_app.html","title":"国产武侠《隐龙传：影踪》今日正式登陆国行PS4","senddate":"2小时前","feedback":"0","id":"3680908","changyan_id":"news_3680908","description":"PlayStation®4武侠动作游戏《隐龙传：影踪》国行实体版与数字版今日正式发售。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818153430_1_app.png"],["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818153407_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818153605_1_app.jpg"]],"arttype":2},{"typeid":"199","arcurl":"http://m.3dmgame.com/events/201708/3680904_app.html","title":"新乐视召开管理层闭门宣贯会 全新团队正式亮相","senddate":"2小时前","feedback":"0","id":"3680904","changyan_id":"news_3680904","description":"虽然围绕乐视公司的一系列风波并没有停止，但似乎乐视本身正在进行一系列的变革，如今他们的全新核心团队已经组建完成，新的工作也逐步开展了起来。","lmfl":"时事","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/154_170818153155_1_app.jpg"]],"arttype":1},{"typeid":"151","arcurl":"http://m.3dmgame.com/zt/201708/3680901_app.html","title":"爆笑雷人囧图速度欣赏 夏天狼多妹子出门要注意了","senddate":"2小时前","feedback":"23","id":"3680901","changyan_id":"news_3680901","description":"周五来临，小编今天为大家带来一组新的爆笑雷人囧图：夏天狼多，妹子出门要注意防范！","lmfl":"杂谈","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818150645_1_app.gif"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818150645_2_app.gif"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818150645_3_app.jpg"]],"arttype":2},{"typeid":"152","arcurl":"http://m.3dmgame.com/hardware/201708/3680891_app.html","title":"三星Note 8泄露：手机拍照被改写 6288元安卓无敌","senddate":"3小时前","feedback":"0","id":"3680891","changyan_id":"news_3680891","description":"8月23日，三星将于纽约举办新品发布会，正式发布新一代旗舰机Galaxy Note 8。屏占比更大，S-pen手写笔升级，最拿手的拍照也将得到全面提升。","lmfl":"硬件","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818145454_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818145514_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818145527_1_app.jpg"]],"arttype":2},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680890_app.html","title":"周五Steam上架游戏一览 冒险解谜角色扮演应有尽有","senddate":"3小时前","feedback":"0","id":"3680890","changyan_id":"news_3680890","description":"近日Steam游戏平台上架游戏一览，有冒险解谜，角色扮演各种各样的游戏哦！","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818142800_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818142954_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818142954_2_app.jpg"]],"arttype":2},{"typeid":"151","arcurl":"http://m.3dmgame.com/zt/201708/3680886_app.html","title":"周五清凉福利美图欣赏 黑丝美腿妹纸不一样的诱惑","senddate":"3小时前","feedback":"10","id":"3680886","changyan_id":"news_3680886","description":"又到周五了，工作之余来看看美女的黑丝美腿，实在是太诱惑人了，让人忍不住想上去舔一舔!一起来看看今天的清凉福利美图。","lmfl":"杂谈","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818144624_1_app.JPG"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818144624_2_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818144624_3_app.jpg"]],"arttype":2},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680880_app.html","title":"国外妹子Cos《守望先锋》D.VA美照 在家也这么可爱","senddate":"3小时前","feedback":"0","id":"3680880","changyan_id":"news_3680880","description":"美国Coser\u201cMegan Coffey\u201dCOS过很多《守望先锋》角色的作品，其中她COS的D.VA受到了不少粉丝的好评，与其他修图高手不同，妹子在家的自拍非常可爱，毫无PS痕迹。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818144207_1_app.png"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818144207_2_app.png"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818144208_3_app.png"]],"arttype":2},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680878_app.html","title":"龙腾世纪制作人后悔游戏登陆旧主机 想效仿巫师3","senddate":"3小时前","feedback":"0","id":"3680878","changyan_id":"news_3680878","description":"《龙腾世纪》的制作人Darrah曾表示自己对于旧主机版《龙腾世纪：审判》不是很满意，《龙腾世纪：审判》受到了一些旧主机机能的影响，无法发挥出全力。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818143413_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818143705_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818143705_2_app.jpg"]],"arttype":2},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680874_app.html","title":"动作新作《赦免者》最新演示公布 中国武侠风酷炫","senddate":"3小时前","feedback":"0","id":"3680874","changyan_id":"news_3680874","description":"《赦免者》是一款在动作风格上和中国传统武术非常类似的游戏。今日官方发布了游戏新演示，介绍了在玩家进入游戏前的角色自定义系统和多人模式特色。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818142753_1_app.jpg"]],"arttype":1},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680870_app.html","title":"充满惊喜的宇宙 《永恒空间》更新包与豪华版发布","senddate":"3小时前","feedback":"0","id":"3680870","changyan_id":"news_3680870","description":"今日，Rockfish Games旗下游戏《永恒空间》发布了1.1版更新，同时官方还公布了游戏的数字版艺术图册和游戏原声，豪华版也登陆Steam平台。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818141858_1_app.jpg"]],"arttype":1},{"typeid":"199","arcurl":"http://m.3dmgame.com/events/201708/3680867_app.html","title":"曝《战狼2》富二代原本找王思聪演遭拒绝：没时间","senddate":"3小时前","feedback":"18","id":"3680867","changyan_id":"news_3680867","description":"《战狼2》电影里张翰演富二代卓亦凡，当初这个角色吴京打算找王思聪来演，但王思聪拒绝了，说是没时间。没想到拒绝了吴京之后，王思聪反而有大把时间了。","lmfl":"时事","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818141109_1_app.png"]],"arttype":1},{"typeid":"151","arcurl":"http://m.3dmgame.com/zt/201708/3680863_app.html","title":"日本妹纸真島なおみ美照欣赏 长相甜美丰满迷人！","senddate":"3小时前","feedback":"8","id":"3680863","changyan_id":"news_3680863","description":"日本漂亮妹子挺多，今天小编就给大家介绍一位，她就是日本模特及主播\u2014\u2014真島なおみ!虽然她年龄只有19岁，身高却已经高达170cm，姣好身材加上甜美长相让人追捧。","lmfl":"杂谈","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818140558_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818140558_2_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818140558_3_app.jpg"]],"arttype":2},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680861_app.html","title":"《零之轨迹》+《碧之轨迹》8月24日登陆WeGame","senddate":"4小时前","feedback":"0","id":"3680861","changyan_id":"news_3680861","description":"腾讯TGP网站上出现了《零之轨迹》《碧之轨迹》的主题页面（点击进入），两款游戏将正式登陆腾讯Wegame平台。此外腾讯也发布了一则官方预告。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818135547_1_app.jpg"]],"arttype":1}]}
	 */
	private BannerBean banner;
	private ChannelBean channel;

	public BannerBean getBanner() {
		return banner;
	}

	public void setBanner(BannerBean banner) {
		this.banner = banner;
	}

	public ChannelBean getChannel() {
		return channel;
	}

	public void setChannel(ChannelBean channel) {
		this.channel = channel;
	}

	public static class BannerBean {
		/**
		 * code : 1
		 * html : [{"arcurl":"http://m.3dmgame.com/news/201708/3680777_app.html","title":"《刺客信条：起源》最新截图放出 黑魂巫师3合体","litpic":"http://aimg.3dmgame.com/uploads/allimg/170222/11111111111111111111111111111111111111111316-1F222155107.jpg","id":"3680777","senddate":"2014-07-14","description":"《刺客信条：起源》最新截图放出 黑魂巫师3合体","lmfl":"幻灯","changyan_id":"news_3680777"},{"arcurl":"http://m.3dmgame.com/news/201708/3680844_app.html","title":"《贼海》PC版本全新演示 21:9显示器玩游戏超震撼","litpic":"http://aimg.3dmgame.com/uploads/allimg/170222/111111111111111111111111111111111111111111316-1F222155107-50.jpg","id":"3680844","senddate":"2014-07-14","description":"《贼海》PC版本全新演示 21:9显示器玩游戏超震撼","lmfl":"幻灯","changyan_id":"news_3680844"},{"arcurl":"http://m.3dmgame.com/news/201708/3680819_app.html","title":"近距离感受核泄漏灾难 《切尔诺贝利VR计划》情报","litpic":"http://aimg.3dmgame.com/uploads/allimg/170222/111111111111111111111111111111111111111111111316-1F222155107-51.jpg","id":"3680819","senddate":"2014-07-14","description":"近距离感受核泄漏灾难 《切尔诺贝利VR计划》情报","lmfl":"幻灯","changyan_id":"news_3680819"},{"arcurl":"http://m.3dmgame.com/news/201708/3680861_app.html","title":"《零之轨迹》+《碧之轨迹》8月24日登陆WeGame","litpic":"http://aimg.3dmgame.com/uploads/allimg/170222/1111111111111111111111111111111111316-1F222155107-52.jpg","id":"3680861","senddate":"2014-07-14","description":"《零之轨迹》+《碧之轨迹》8月24日登陆WeGame","lmfl":"幻灯","changyan_id":"news_3680861"},{"arcurl":"http://m.3dmgame.com/news/201708/3680832_app.html","title":"游戏画面大变化 玩家痴迷于《无人深空》新版草地","litpic":"http://aimg.3dmgame.com/uploads/allimg/170222/111111111111111111111111111111111111111111316-1F222155107-53.jpg","id":"3680832","senddate":"2014-07-14","description":"游戏画面大变化 玩家痴迷于《无人深空》新版草地","lmfl":"幻灯","changyan_id":"news_3680832"}]
		 */

		private int code;
		private List<HtmlBean> html;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public List<HtmlBean> getHtml() {
			return html;
		}

		public void setHtml(List<HtmlBean> html) {
			this.html = html;
		}

		public static class HtmlBean {
			/**
			 * arcurl : http://m.3dmgame.com/news/201708/3680777_app.html
			 * title : 《刺客信条：起源》最新截图放出 黑魂巫师3合体
			 * litpic : http://aimg.3dmgame.com/uploads/allimg/170222/11111111111111111111111111111111111111111316-1F222155107.jpg
			 * id : 3680777
			 * senddate : 2014-07-14
			 * description : 《刺客信条：起源》最新截图放出 黑魂巫师3合体
			 * lmfl : 幻灯
			 * changyan_id : news_3680777
			 */

			private String arcurl;
			private String title;
			private String litpic;
			private String id;
			private String senddate;
			private String description;
			private String lmfl;
			private String changyan_id;

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

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getSenddate() {
				return senddate;
			}

			public void setSenddate(String senddate) {
				this.senddate = senddate;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public String getLmfl() {
				return lmfl;
			}

			public void setLmfl(String lmfl) {
				this.lmfl = lmfl;
			}

			public String getChangyan_id() {
				return changyan_id;
			}

			public void setChangyan_id(String changyan_id) {
				this.changyan_id = changyan_id;
			}
		}
	}

	public static class ChannelBean {
		/**
		 * code : 1
		 * totalrow : 500
		 * html : [{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680967_app.html","title":"游戏晚报|《刺客信条》最新截图 卡普空周末促销","senddate":"16分钟前","feedback":"0","id":"3680967","changyan_id":"news_3680967","description":"单机RPG《暗影：觉醒》公布 首批细节和截图；\r\n《刺客信条：起源》最新截图放出 黑魂巫师3合体；\r\n游戏太恐怖 3DM《阴暗森林》正式版免安装未加密版","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170515/322_170515183640_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818162846_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818162846_2_app.jpg"]],"arttype":2},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680963_app.html","title":"《绝地求生》FPS模式很棒 但这是款第三人称游戏","senddate":"26分钟前","feedback":"0","id":"3680963","changyan_id":"news_3680963","description":"我依然非常喜欢《绝地求生：大逃杀》的第一人称模式，而且以后我可能基本都会用第一人称去玩，但是这不代表游戏已经针对第一人称做好了设计。这就是一款为第三人称设计的游戏，各方面表现都是如此。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/154_170818173906_1_app.jpg"]],"arttype":1},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680931_app.html","title":"3DM汉化组制作《深圳I/O》完整汉化版发布下载","senddate":"1小时前","feedback":"0","id":"3680931","changyan_id":"news_3680931","description":"《深圳I/O（SHENZHEN I/O）》3DM汉化组简体汉化补丁v1.0及汉化硬盘版，本补丁仅作学习和交流之用，任何个人及组织未经本组同意，不得用作商业用途。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/369_170818163532_1_app.png"],["http://aimg.3dmgame.com/uploads/allimg/170818/369_170818163800_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/369_170818163800_2_app.jpg"]],"arttype":2},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680917_app.html","title":"Twitch黑科技：观众可搜索使用想看英雄的直播间","senddate":"1小时前","feedback":"0","id":"3680917","changyan_id":"news_3680917","description":"观众可以直接在Twitch的《守望先锋》目录里根据自己想要看的英雄来选择直播间。在《炉石传说》目录里，玩家可以根据英雄职业和游戏模式来选择直播间，比如竞技场直播和天梯直播会分开。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/154_170818160545_1.jpeg"]],"arttype":1},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680916_app.html","title":"《莎木3》新图像：男女主角亮相 小镇和长城好美","senddate":"1小时前","feedback":"0","id":"3680916","changyan_id":"news_3680916","description":"昨日《莎木3》官方发布公告，确定与Deep Silver合作，Deep Silver将负责《莎木3》的全球发行任务。今日出版商发布了一份新闻稿，包括 《莎木3》游戏的新图像放出。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818160123_1_app.jpg"]],"arttype":1},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680915_app.html","title":"《英雄传说 闪之轨迹3》原声限定版公开试听影像","senddate":"2小时前","feedback":"0","id":"3680915","changyan_id":"news_3680915","description":"日本Falcom预定于2017年9月28日发售PS4平台游戏《英雄传说 闪之轨迹3》初回限定KISEKI BOX同捆的迷你原声限定版公开试听影像，视频中还包含了不少游戏新画面。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818155331_1_app.jpg"]],"arttype":1},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680914_app.html","title":"日本市场软硬件销量周榜：Switch销量即将破百万","senddate":"2小时前","feedback":"0","id":"3680914","changyan_id":"news_3680914","description":"任天堂Switch上周在日本的销量比前一周提升了10%，这是来自《FAMI通》的数据，看来虽然近期没有太强力的新作，但玩家对任天堂新主机的购买欲依然不减。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170813/369_170813205457_1_app.jpg"]],"arttype":1},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680908_app.html","title":"国产武侠《隐龙传：影踪》今日正式登陆国行PS4","senddate":"2小时前","feedback":"0","id":"3680908","changyan_id":"news_3680908","description":"PlayStation®4武侠动作游戏《隐龙传：影踪》国行实体版与数字版今日正式发售。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818153430_1_app.png"],["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818153407_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818153605_1_app.jpg"]],"arttype":2},{"typeid":"199","arcurl":"http://m.3dmgame.com/events/201708/3680904_app.html","title":"新乐视召开管理层闭门宣贯会 全新团队正式亮相","senddate":"2小时前","feedback":"0","id":"3680904","changyan_id":"news_3680904","description":"虽然围绕乐视公司的一系列风波并没有停止，但似乎乐视本身正在进行一系列的变革，如今他们的全新核心团队已经组建完成，新的工作也逐步开展了起来。","lmfl":"时事","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/154_170818153155_1_app.jpg"]],"arttype":1},{"typeid":"151","arcurl":"http://m.3dmgame.com/zt/201708/3680901_app.html","title":"爆笑雷人囧图速度欣赏 夏天狼多妹子出门要注意了","senddate":"2小时前","feedback":"23","id":"3680901","changyan_id":"news_3680901","description":"周五来临，小编今天为大家带来一组新的爆笑雷人囧图：夏天狼多，妹子出门要注意防范！","lmfl":"杂谈","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818150645_1_app.gif"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818150645_2_app.gif"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818150645_3_app.jpg"]],"arttype":2},{"typeid":"152","arcurl":"http://m.3dmgame.com/hardware/201708/3680891_app.html","title":"三星Note 8泄露：手机拍照被改写 6288元安卓无敌","senddate":"3小时前","feedback":"0","id":"3680891","changyan_id":"news_3680891","description":"8月23日，三星将于纽约举办新品发布会，正式发布新一代旗舰机Galaxy Note 8。屏占比更大，S-pen手写笔升级，最拿手的拍照也将得到全面提升。","lmfl":"硬件","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818145454_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818145514_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818145527_1_app.jpg"]],"arttype":2},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680890_app.html","title":"周五Steam上架游戏一览 冒险解谜角色扮演应有尽有","senddate":"3小时前","feedback":"0","id":"3680890","changyan_id":"news_3680890","description":"近日Steam游戏平台上架游戏一览，有冒险解谜，角色扮演各种各样的游戏哦！","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818142800_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818142954_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818142954_2_app.jpg"]],"arttype":2},{"typeid":"151","arcurl":"http://m.3dmgame.com/zt/201708/3680886_app.html","title":"周五清凉福利美图欣赏 黑丝美腿妹纸不一样的诱惑","senddate":"3小时前","feedback":"10","id":"3680886","changyan_id":"news_3680886","description":"又到周五了，工作之余来看看美女的黑丝美腿，实在是太诱惑人了，让人忍不住想上去舔一舔!一起来看看今天的清凉福利美图。","lmfl":"杂谈","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818144624_1_app.JPG"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818144624_2_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818144624_3_app.jpg"]],"arttype":2},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680880_app.html","title":"国外妹子Cos《守望先锋》D.VA美照 在家也这么可爱","senddate":"3小时前","feedback":"0","id":"3680880","changyan_id":"news_3680880","description":"美国Coser\u201cMegan Coffey\u201dCOS过很多《守望先锋》角色的作品，其中她COS的D.VA受到了不少粉丝的好评，与其他修图高手不同，妹子在家的自拍非常可爱，毫无PS痕迹。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818144207_1_app.png"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818144207_2_app.png"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818144208_3_app.png"]],"arttype":2},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680878_app.html","title":"龙腾世纪制作人后悔游戏登陆旧主机 想效仿巫师3","senddate":"3小时前","feedback":"0","id":"3680878","changyan_id":"news_3680878","description":"《龙腾世纪》的制作人Darrah曾表示自己对于旧主机版《龙腾世纪：审判》不是很满意，《龙腾世纪：审判》受到了一些旧主机机能的影响，无法发挥出全力。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818143413_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818143705_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818143705_2_app.jpg"]],"arttype":2},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680874_app.html","title":"动作新作《赦免者》最新演示公布 中国武侠风酷炫","senddate":"3小时前","feedback":"0","id":"3680874","changyan_id":"news_3680874","description":"《赦免者》是一款在动作风格上和中国传统武术非常类似的游戏。今日官方发布了游戏新演示，介绍了在玩家进入游戏前的角色自定义系统和多人模式特色。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818142753_1_app.jpg"]],"arttype":1},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680870_app.html","title":"充满惊喜的宇宙 《永恒空间》更新包与豪华版发布","senddate":"3小时前","feedback":"0","id":"3680870","changyan_id":"news_3680870","description":"今日，Rockfish Games旗下游戏《永恒空间》发布了1.1版更新，同时官方还公布了游戏的数字版艺术图册和游戏原声，豪华版也登陆Steam平台。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818141858_1_app.jpg"]],"arttype":1},{"typeid":"199","arcurl":"http://m.3dmgame.com/events/201708/3680867_app.html","title":"曝《战狼2》富二代原本找王思聪演遭拒绝：没时间","senddate":"3小时前","feedback":"18","id":"3680867","changyan_id":"news_3680867","description":"《战狼2》电影里张翰演富二代卓亦凡，当初这个角色吴京打算找王思聪来演，但王思聪拒绝了，说是没时间。没想到拒绝了吴京之后，王思聪反而有大把时间了。","lmfl":"时事","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818141109_1_app.png"]],"arttype":1},{"typeid":"151","arcurl":"http://m.3dmgame.com/zt/201708/3680863_app.html","title":"日本妹纸真島なおみ美照欣赏 长相甜美丰满迷人！","senddate":"3小时前","feedback":"8","id":"3680863","changyan_id":"news_3680863","description":"日本漂亮妹子挺多，今天小编就给大家介绍一位，她就是日本模特及主播\u2014\u2014真島なおみ!虽然她年龄只有19岁，身高却已经高达170cm，姣好身材加上甜美长相让人追捧。","lmfl":"杂谈","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818140558_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818140558_2_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/153_170818140558_3_app.jpg"]],"arttype":2},{"typeid":"2","arcurl":"http://m.3dmgame.com/news/201708/3680861_app.html","title":"《零之轨迹》+《碧之轨迹》8月24日登陆WeGame","senddate":"4小时前","feedback":"0","id":"3680861","changyan_id":"news_3680861","description":"腾讯TGP网站上出现了《零之轨迹》《碧之轨迹》的主题页面（点击进入），两款游戏将正式登陆腾讯Wegame平台。此外腾讯也发布了一则官方预告。","lmfl":"游戏","litpic":[["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818135547_1_app.jpg"]],"arttype":1}]
		 */

		private int code;
		private int totalrow;
		private List<HtmlBeanX> html;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public int getTotalrow() {
			return totalrow;
		}

		public void setTotalrow(int totalrow) {
			this.totalrow = totalrow;
		}

		public List<HtmlBeanX> getHtml() {
			return html;
		}

		public void setHtml(List<HtmlBeanX> html) {
			this.html = html;
		}

		public static class HtmlBeanX {
			/**
			 * typeid : 2
			 * arcurl : http://m.3dmgame.com/news/201708/3680967_app.html
			 * title : 游戏晚报|《刺客信条》最新截图 卡普空周末促销
			 * senddate : 16分钟前
			 * feedback : 0
			 * id : 3680967
			 * changyan_id : news_3680967
			 * description : 单机RPG《暗影：觉醒》公布 首批细节和截图；
			 《刺客信条：起源》最新截图放出 黑魂巫师3合体；
			 游戏太恐怖 3DM《阴暗森林》正式版免安装未加密版
			 * lmfl : 游戏
			 * litpic : [["http://aimg.3dmgame.com/uploads/allimg/170515/322_170515183640_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818162846_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170818/382_170818162846_2_app.jpg"]]
			 * arttype : 2
			 */

			private String typeid;
			private String arcurl;
			private String title;
			private String senddate;
			private String feedback;
			private String id;
			private String changyan_id;
			private String description;
			private String lmfl;
			private int arttype;
			private List<List<String>> litpic;

			public String getTypeid() {
				return typeid;
			}

			public void setTypeid(String typeid) {
				this.typeid = typeid;
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

			public String getSenddate() {
				return senddate;
			}

			public void setSenddate(String senddate) {
				this.senddate = senddate;
			}

			public String getFeedback() {
				return feedback;
			}

			public void setFeedback(String feedback) {
				this.feedback = feedback;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getChangyan_id() {
				return changyan_id;
			}

			public void setChangyan_id(String changyan_id) {
				this.changyan_id = changyan_id;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public String getLmfl() {
				return lmfl;
			}

			public void setLmfl(String lmfl) {
				this.lmfl = lmfl;
			}

			public int getArttype() {
				return arttype;
			}

			public void setArttype(int arttype) {
				this.arttype = arttype;
			}

			public List<List<String>> getLitpic() {
				return litpic;
			}

			public void setLitpic(List<List<String>> litpic) {
				this.litpic = litpic;
			}
		}
	}
}
