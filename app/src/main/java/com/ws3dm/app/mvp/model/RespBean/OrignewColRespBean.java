package com.ws3dm.app.mvp.model.RespBean;

import com.google.gson.annotations.SerializedName;
import com.ws3dm.app.bean.OriginalBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class OrignewColRespBean extends BaseRespBean<OrignewColRespBean.DataBean>{

	/**
	 * data : {"list":[{"id":3,"name":"人物","litpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20180709/1531120787_519127_1.jpg","newslist":[{"aid":3741882,"arcurl":"https://www.3dmgame.com/original/3741882.html","title":"《凤凰点》总经理专访：在Epic的帮助下，我们越做越好 ","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190807/1565147092_924243.jpg","pubdate_at":1565312928,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741882.html","total_ct":18},{"aid":3741887,"arcurl":"https://www.3dmgame.com/original/3741887.html","title":"《元能失控》主策划阿满采访：做好《元能失控》并不容易","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190808/1565226143_942587.png","pubdate_at":1565239289,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741887.html","total_ct":3}]},{"id":4,"name":"怀旧","litpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20180706/1530849108_348406_1.jpg","newslist":[{"aid":3741731,"arcurl":"https://www.3dmgame.com/original/3741731.html","title":"我在暮色森林哭了","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190603/1559562400_844169.jpg","pubdate_at":1559789291,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741731.html","total_ct":72},{"aid":3741466,"arcurl":"https://www.3dmgame.com/original/3741466.html","title":"看着电影聊游戏：游戏可以重制，玩家的情怀可以吗？","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20181212/1544611433_998603.png","pubdate_at":1544610777,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741466.html","total_ct":39}]},{"id":6,"name":"新鲜事","litpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20180706/1530849088_876105_1.jpg","newslist":[{"aid":3741920,"arcurl":"https://www.3dmgame.com/original/3741920.html","title":"当抖音上播放量近亿的4399小游戏，被国内手游山寨","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190820/1566287951_913920.png","pubdate_at":1566288955,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741920.html","total_ct":7},{"aid":3741866,"arcurl":"https://www.3dmgame.com/original/3741866.html","title":"【吼狸社】Vlog：林克竟然在任天堂展台玩自拍？","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190803/1564821064_625795.jpg","pubdate_at":1564821188,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741866.html","total_ct":28}]},{"id":7,"name":"事件","litpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20180706/1530848858_550252_1.jpg","newslist":[{"aid":3741918,"arcurl":"https://www.3dmgame.com/original/3741918.html","title":"台风，FANFEST和\u201c光之战士\u201d","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190819/1566212749_620108.png","pubdate_at":1566219194,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741918.html","total_ct":5},{"aid":3741911,"arcurl":"https://www.3dmgame.com/original/3741911.html","title":"在只有四天的《魔兽世界：怀旧服》中玩家们会聊些什么","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190816/1565935631_797732.png","pubdate_at":1565939572,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741911.html","total_ct":59}]},{"id":9,"name":"为什么","litpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20180529/1527556921_558682_1.jpg","newslist":[{"aid":3741794,"arcurl":"https://www.3dmgame.com/original/3741794.html","title":"专访52TOYS CEO陈威：为什么做衍生品玩具行业？因为热爱","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190626/1561521933_298017.jpg","pubdate_at":1561521103,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741794.html","total_ct":1},{"aid":3741775,"arcurl":"https://www.3dmgame.com/original/3741775.html","title":" 《GTA5》都发售六年了，凭啥还这么火？","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190619/1560918673_280792.jpg","pubdate_at":1560925301,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741775.html","total_ct":83}]},{"id":10,"name":"文化","litpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20180529/1527557561_598805_1.jpg","newslist":[{"aid":3741844,"arcurl":"https://www.3dmgame.com/original/3741844.html","title":"在游戏中重拾童话之美","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190723/1563868792_295117.jpg","pubdate_at":1564115186,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741844.html","total_ct":48},{"aid":3741842,"arcurl":"https://www.3dmgame.com/original/3741842.html","title":"当骑士摘下假面，是否还能称为英雄","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190722/1563775786_653098.jpg","pubdate_at":1563865805,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741842.html","total_ct":58}]},{"id":14,"name":"脑洞","litpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20180530/1527675893_701811_1.jpg","newslist":[{"aid":3741470,"arcurl":"https://www.3dmgame.com/original/3741470.html","title":"3DM枪神的北欧之心，比枪林弹雨来的更让人动情","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20181219/1545186348_750919.png","pubdate_at":1545186760,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741470.html","total_ct":70},{"aid":3715790,"arcurl":"https://www.3dmgame.com/original/3715790.html","title":"《英雄联盟》从8.2版本更新看未来赛场走向","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20180601/1527862339_431909.jpg","pubdate_at":1517459027,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3715790.html","total_ct":0}]},{"id":15,"name":"推游","litpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20180608/1528460562_203100_1.jpg","newslist":[{"aid":3741645,"arcurl":"https://www.3dmgame.com/original/3741645.html","title":"《古剑奇谭3》免费拓展内容体验报告","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190416/1555378165_285579.jpg","pubdate_at":1555378153,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741645.html","total_ct":34},{"aid":3741580,"arcurl":"https://www.3dmgame.com/original/3741580.html","title":"新赛季只是噱头？《火影OL》的新版本对玩家有何影响？","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190310/1552154566_693880.png","pubdate_at":1552154653,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741580.html","total_ct":24}]},{"id":17,"name":"考据","litpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20180702/1530532815_421753_1.jpg","newslist":[{"aid":3741635,"arcurl":"https://www.3dmgame.com/original/3741635.html","title":"硬核宅物：2019年如何玩复古游戏机","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190411/1554961935_832112.jpg","pubdate_at":1554962080,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741635.html","total_ct":14},{"aid":3741444,"arcurl":"https://www.3dmgame.com/original/3741444.html","title":"探寻沉浸式模拟游戏的困境与新生","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20181030/1540864212_465046.jpg","pubdate_at":1540864334,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741444.html","total_ct":13}]},{"id":18,"name":"一周话题","litpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20180718/1531899043_759979_1.jpg","newslist":[{"aid":3741895,"arcurl":"https://www.3dmgame.com/original/3741895.html","title":"你如何看待游戏中暴力表现对现实的影响？","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190811/1565507733_593654.jpg","pubdate_at":1565507743,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741895.html","total_ct":105},{"aid":3741853,"arcurl":"https://www.3dmgame.com/original/3741853.html","title":"对于越来越多且越贵的DLC，你怎么看？","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190728/1564297646_100960.jpg","pubdate_at":1564297775,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741853.html","total_ct":115}]},{"id":22,"name":"3言两语","litpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20180911/1536656522_153703_1.jpg","newslist":[{"aid":3741426,"arcurl":"https://www.3dmgame.com/original/3741426.html","title":"3言两语|第二周|聊聊大家最近都玩了什么游戏","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20180926/1537940668_842837.jpg","pubdate_at":1537941185,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741426.html","total_ct":5},{"aid":3741416,"arcurl":"https://www.3dmgame.com/original/3741416.html","title":"3言两语|第一周|世界尽头与冷酷仙境","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20180912/1536755313_100009.jpg","pubdate_at":1536755620,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741416.html","total_ct":5}]},{"id":24,"name":"游戏的人","litpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20180919/1537336971_322935_1.jpg","newslist":[{"aid":3741614,"arcurl":"https://www.3dmgame.com/original/3741614.html","title":"凉透了的《灵魂筹码》，开始了一场自救","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190401/1554087964_487065.jpg","pubdate_at":1554088666,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741614.html","total_ct":32},{"aid":3741492,"arcurl":"https://www.3dmgame.com/original/3741492.html","title":"对话《修仙模拟器》制作人：当下见分晓","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190111/1547183164_700318.jpg","pubdate_at":1547183270,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741492.html","total_ct":75}]},{"id":28,"name":"游史以来","litpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20190101/1546347700_336575_1.jpg","newslist":[{"aid":3741922,"arcurl":"https://www.3dmgame.com/original/3741922.html","title":"游戏历史上的今天：《生化奇兵》正式发售","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190821/1566345703_929443.jpg","pubdate_at":1566352417,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741922.html","total_ct":0},{"aid":3741919,"arcurl":"https://www.3dmgame.com/original/3741919.html","title":"游戏历史上的今天：《怪形》在北美发售","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190820/1566265899_753054.jpg","pubdate_at":1566265912,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741919.html","total_ct":4}]},{"id":42,"name":"陆行鸟","litpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20190725/1564048837_621319_1.jpg","newslist":[]}]}
	 */
	public static class DataBean {
		private List<ColumBean> list;

		public List<ColumBean> getList() {
			return list;
		}

		public void setList(List<ColumBean> list) {
			this.list = list;
		}

		public static class ColumBean {
			/**
			 * id : 3
			 * name : 人物
			 * litpic : https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20180709/1531120787_519127_1.jpg
			 * newslist : [{"aid":3741882,"arcurl":"https://www.3dmgame.com/original/3741882.html","title":"《凤凰点》总经理专访：在Epic的帮助下，我们越做越好 ","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190807/1565147092_924243.jpg","pubdate_at":1565312928,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741882.html","total_ct":18},{"aid":3741887,"arcurl":"https://www.3dmgame.com/original/3741887.html","title":"《元能失控》主策划阿满采访：做好《元能失控》并不容易","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20190808/1565226143_942587.png","pubdate_at":1565239289,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741887.html","total_ct":3}]
			 */

			private int id;
			private String name;
			private String litpic;
			private List<OriginalBean> newslist;

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getLitpic() {
				return litpic;
			}

			public void setLitpic(String litpic) {
				this.litpic = litpic;
			}

			public List<OriginalBean> getNewslist() {
				return newslist;
			}

			public void setNewslist(List<OriginalBean> newslist) {
				this.newslist = newslist;
			}
		}
	}
}
