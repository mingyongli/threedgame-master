package com.ws3dm.app.mvp.model.RespBean;

import java.util.List;

/**
 * Author : DKjuan:单机用的实体类
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameEsportRespBean extends BaseRespBean<GameEsportRespBean.DataBean>{

	public static class DataBean {
		/**
		 * total : 0
		 * list : [{"aid":13,"arcurl":"https://ol.3dmgame.com/esports/lck2019/","litpic":"https://ol.3dmgame.com/uploads/images/thumbgamezt/20190424/1556084708_966866.jpg","showtype":23,"title":"英雄联盟2019LCK春季赛专题","webviewurl":"https://m.3dmgame.com/ol/webview/esports/lck2019/","reward":"\u20a92250000000","place":"韩国","start_date":1547568000,"end_date":1553961600},{"aid":12,"arcurl":"https://ol.3dmgame.com/esports/lpl2019/","litpic":"https://ol.3dmgame.com/uploads/images/thumbgamezt/20190114/1547446815_543559.jpg","showtype":23,"title":"英雄联盟2019LPL春季赛专题","webviewurl":"https://m.3dmgame.com/ol/webview/esports/lpl2019/","reward":"奖金+奖杯","place":"全国","start_date":1547395200,"end_date":1555689600},{"aid":11,"arcurl":"https://ol.3dmgame.com/esports/dmxyb2018d/","litpic":"https://ol.3dmgame.com/uploads/images/thumbgamezt/20181217/1545023522_343976.jpg","showtype":23,"title":"英雄联盟2018德玛西亚杯冬季赛","webviewurl":"https://m.3dmgame.com/ol/webview/esports/dmxyb2018d/","reward":"奖金+奖杯","place":"西安","start_date":1545235200,"end_date":1545494400},{"aid":10,"arcurl":"https://ol.3dmgame.com/esports/s8/","litpic":"https://ol.3dmgame.com/uploads/images/thumbgamezt/20180920/1537439149_976688.jpg","showtype":23,"title":"英雄联盟S8全球总决赛专题","webviewurl":"https://m.3dmgame.com/ol/webview/esports/s8/","reward":"奖金+奖杯","place":"韩国","start_date":1538323200,"end_date":1541174400},{"aid":9,"arcurl":"https://ol.3dmgame.com/esports/yjdyyh/","litpic":"https://ol.3dmgame.com/uploads/images/thumbgamezt/20180825/1535175467_705935.jpg","showtype":23,"title":"英雄联盟雅加达亚运会赛事专题","webviewurl":"https://m.3dmgame.com/ol/webview/esports/yjdyyh/","reward":"冠军奖杯","place":"印度尼西亚","start_date":1535299200,"end_date":1538150400},{"aid":8,"arcurl":"https://ol.3dmgame.com/esports/lck2018x/","litpic":"https://ol.3dmgame.com/uploads/images/thumbgamezt/20180702/1530523747_113614.jpg","showtype":23,"title":"英雄联盟2018LCK夏季赛专题","webviewurl":"https://m.3dmgame.com/ol/webview/esports/lck2018x/","reward":"\u20a92250000000","place":"韩国","start_date":1528819200,"end_date":1533398400},{"aid":7,"arcurl":"https://ol.3dmgame.com/esports/lpl2018x/","litpic":"https://ol.3dmgame.com/uploads/images/thumbgameteam/20180604/1528096979_340497.jpg","showtype":23,"title":"英雄联盟2018LPL夏季赛专题","webviewurl":"https://m.3dmgame.com/ol/webview/esports/lpl2018x/","reward":"￥350W","place":"上海","start_date":1528646400,"end_date":1535212800},{"aid":6,"arcurl":"https://ol.3dmgame.com/esports/dmxyb2018/","litpic":"https://ol.3dmgame.com/uploads/images/thumbgameteam/20180530/1527643404_707574.jpg","showtype":23,"title":"英雄联盟2018德玛西亚杯夏季赛","webviewurl":"https://m.3dmgame.com/ol/webview/esports/dmxyb2018/","reward":"1500000","place":"珠海横琴","start_date":1527696000,"end_date":1527955200},{"aid":5,"arcurl":"https://ol.3dmgame.com/esports/zhoujs2018/","litpic":"https://ol.3dmgame.com/uploads/images/thumbgamezt/20180705/1530787757_851822.jpg","showtype":23,"title":"英雄联盟2018LOL亚洲对抗赛","webviewurl":"https://m.3dmgame.com/ol/webview/esports/zhoujs2018/","reward":"暂定","place":"大连","start_date":1530460800,"end_date":1530979200},{"aid":4,"arcurl":"https://ol.3dmgame.com/esports/msi2018/","litpic":"https://ol.3dmgame.com/uploads/images/thumbgameteam/20180428/1524899883_785446.jpg","showtype":23,"title":"英雄联盟2018MSI季中冠军赛","webviewurl":"https://m.3dmgame.com/ol/webview/esports/msi2018/","reward":"250,000美元","place":"德国柏林、法国巴黎","start_date":1525276800,"end_date":1526745600}]
		 */

		private int total;
		private List<ListBean> list;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public List<ListBean> getList() {
			return list;
		}

		public void setList(List<ListBean> list) {
			this.list = list;
		}

		public static class ListBean {
			/**
			 * aid : 13
			 * arcurl : https://ol.3dmgame.com/esports/lck2019/
			 * litpic : https://ol.3dmgame.com/uploads/images/thumbgamezt/20190424/1556084708_966866.jpg
			 * showtype : 23
			 * title : 英雄联盟2019LCK春季赛专题
			 * webviewurl : https://m.3dmgame.com/ol/webview/esports/lck2019/
			 * reward : ₩2250000000
			 * place : 韩国
			 * start_date : 1547568000
			 * end_date : 1553961600
			 */

			private int aid;
			private String arcurl;
			private String litpic;
			private int showtype;
			private String title;
			private String webviewurl;
			private String reward;
			private String place;
			private int start_date;
			private int end_date;

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

			public int getShowtype() {
				return showtype;
			}

			public void setShowtype(int showtype) {
				this.showtype = showtype;
			}

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getWebviewurl() {
				return webviewurl;
			}

			public void setWebviewurl(String webviewurl) {
				this.webviewurl = webviewurl;
			}

			public String getReward() {
				return reward;
			}

			public void setReward(String reward) {
				this.reward = reward;
			}

			public String getPlace() {
				return place;
			}

			public void setPlace(String place) {
				this.place = place;
			}

			public int getStart_date() {
				return start_date;
			}

			public void setStart_date(int start_date) {
				this.start_date = start_date;
			}

			public int getEnd_date() {
				return end_date;
			}

			public void setEnd_date(int end_date) {
				this.end_date = end_date;
			}
		}
	}
}
