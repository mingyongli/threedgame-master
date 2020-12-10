package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.GameGiftBean;

import java.util.List;

/**
 * Author : DKjuan:手游礼包,游戏分类跳转的列表共用
 * <p>
 * Date :  2017/8/21  12:06
 */
public class MGGiftRespBean extends BaseRespBean<MGGiftRespBean.DataBean>{

	public static class DataBean {
		/**
		 * total : 8
		 * list : [{"aid":"10010","arcurl":"http://shouyou2.3dmgame.com/ka/10010.html","title":"《王者荣耀》情谊礼包3","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0405/1522908353854181.png","enddate":1524214837,"surplusper":0,"showtype":9},{"aid":"10009","arcurl":"http://shouyou2.3dmgame.com/ka/10009.html","title":"《王者荣耀》情谊礼包2","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0405/1522908353854181.png","enddate":1524213510,"surplusper":0,"showtype":9},{"aid":"10008","arcurl":"http://shouyou2.3dmgame.com/ka/10008.html","title":"炉石传说3DM礼包","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0420/1524205398552125.jpg","enddate":1524212267,"surplusper":66,"showtype":9},{"aid":"10007","arcurl":"http://shouyou2.3dmgame.com/ka/10007.html","title":"《王者荣耀》情谊礼包1","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0405/1522908353854181.png","enddate":1524212009,"surplusper":66,"showtype":9},{"aid":"10006","arcurl":"http://shouyou2.3dmgame.com/ka/10006.html","title":"《绝地求生：刺激战场》3DM独家礼包","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0420/1524205924434066.png","enddate":1525057223,"surplusper":0,"showtype":9},{"aid":"10005","arcurl":"http://shouyou2.3dmgame.com/ka/10005.html","title":"《绝地求生：刺激战场》3DM独家礼包","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0420/1524205924434066.png","enddate":1525056941,"surplusper":0,"showtype":9},{"aid":"10004","arcurl":"http://shouyou2.3dmgame.com/ka/10004.html","title":"《王者荣耀》3DM礼包","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0405/1522908353854181.png","enddate":1523437197,"surplusper":80,"showtype":9},{"aid":"10000","arcurl":"http://shouyou2.3dmgame.com/ka/10000.html","title":"《王者荣耀》情谊礼包","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0405/1522908353854181.png","enddate":1524119458,"surplusper":75,"showtype":9}]
		 */

		private String total;
		private List<GameGiftBean> list;

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public List<GameGiftBean> getList() {
			return list;
		}

		public void setList(List<GameGiftBean> list) {
			this.list = list;
		}
	}
}
