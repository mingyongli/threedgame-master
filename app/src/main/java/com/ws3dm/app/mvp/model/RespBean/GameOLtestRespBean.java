package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.GameTestBean;

import java.util.List;

/**
 * Author : DKjuan:单机用的实体类
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameOLtestRespBean extends BaseRespBean<GameOLtestRespBean.DataBean>{
	public static class DataBean {
		/**
		 * total : 12
		 * list : [{"aid":0,"arcurl":"","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20181031/1540952430_480270.jpg","showtype":0,"title":"完美国际2","webviewurl":"","type":"","firm":"完美世界","state":"人羽新篇","pubdate_at":1540915200,"score":0},{"aid":60,"arcurl":"https://ol.3dmgame.com/taiyss/","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20190531/1559285028_395192.jpg","showtype":21,"title":"泰亚史诗","webviewurl":"","type":"策略","firm":"网易游戏","state":"全新大区不删档","pubdate_at":1540483200,"score":8.2},{"aid":0,"arcurl":"","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20181023/1540276447_348583.jpg","showtype":0,"title":"巫师之昆特牌","webviewurl":"","type":"","firm":"盖娅互娱","state":"王权的陨落","pubdate_at":1540310400,"score":0},{"aid":28,"arcurl":"https://ol.3dmgame.com/jyzj/","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20181019/1539933590_588306.jpg","showtype":21,"title":"九阴真经","webviewurl":"","type":"角色扮演","firm":"蜗牛游戏","state":"剑啸龙吟","pubdate_at":1539878400,"score":8.2},{"aid":0,"arcurl":"","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20171118/1510970658_578829.jpg","showtype":0,"title":"不朽之城","webviewurl":"","type":"","firm":"乐善科技","state":"噩梦之城","pubdate_at":1540310400,"score":0},{"aid":0,"arcurl":"","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20180814/1534229267_459008.jpg","showtype":0,"title":"仙武","webviewurl":"","type":"","firm":"创星网络","state":"征战","pubdate_at":1540569600,"score":0},{"aid":14,"arcurl":"https://ol.3dmgame.com/nish/","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20190625/1561453383_350137.jpg","showtype":21,"title":"逆水寒","webviewurl":"","type":"角色扮演","firm":"网易游戏","state":"怒涛中的剑影","pubdate_at":1539878400,"score":9},{"aid":0,"arcurl":"","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20171228/1514453659_870413.jpg","showtype":0,"title":"永恒魔法","webviewurl":"","type":"","firm":"多益网络","state":"荣耀之战","pubdate_at":1539878400,"score":0},{"aid":0,"arcurl":"","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20171228/1514453659_870413.jpg","showtype":0,"title":"永恒魔法","webviewurl":"","type":"","firm":"多益网络","state":"自由之城","pubdate_at":1539878400,"score":0},{"aid":0,"arcurl":"","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20180908/1536370032_920974.jpg","showtype":0,"title":"蜀缘","webviewurl":"","type":"","firm":"云蟾游戏","state":"封测删档","pubdate_at":1540483200,"score":0}]
		 */

		private int total;
		private List<GameTestBean> list;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public List<GameTestBean> getList() {
			return list;
		}

		public void setList(List<GameTestBean> list) {
			this.list = list;
		}
	}
}
