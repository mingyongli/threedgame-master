package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.GameGiftBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameGiftRespBean extends BaseRespBean<GameGiftRespBean.DataBean>{

	public static class DataBean {
		/**
		 * total : 35
		 * list : [{"aid":10195,"arcurl":"https://ol.3dmgame.com/hao/10195.html","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20191120/1574214074_688219.jpg","showtype":22,"title":"《古剑奇谭网络版》 怀仙寻梦礼包","webviewurl":"","range_start":1574265600,"range_end":1577721600,"surplusper":99,"is_over":1,"is_empty":0,"is_avail":0},{"aid":10194,"arcurl":"https://ol.3dmgame.com/hao/10194.html","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20191115/1573801585_697792.jpg","showtype":22,"title":"《醉逍遥》 独步天下礼包","webviewurl":"","range_start":1573747200,"range_end":1579363200,"surplusper":99,"is_over":1,"is_empty":0,"is_avail":0},{"aid":10193,"arcurl":"https://ol.3dmgame.com/hao/10193.html","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20190905/1567646473_715711.jpg","showtype":22,"title":"《刀剑英雄》 战神成长礼包","webviewurl":"","range_start":1573747200,"range_end":1579795200,"surplusper":8,"is_over":1,"is_empty":0,"is_avail":0},{"aid":10191,"arcurl":"https://ol.3dmgame.com/hao/10191.html","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20191107/1573096600_221177.jpg","showtype":22,"title":"《战意》 急先锋成长助力礼包","webviewurl":"","range_start":1573056000,"range_end":1604678400,"surplusper":91,"is_over":1,"is_empty":0,"is_avail":0},{"aid":10190,"arcurl":"https://ol.3dmgame.com/hao/10190.html","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20191106/1573029587_706985.jpg","showtype":22,"title":"《奇迹MU》 艾米丽斯3DM专属礼包","webviewurl":"","range_start":1573056000,"range_end":1604678400,"surplusper":53,"is_over":1,"is_empty":0,"is_avail":0},{"aid":10189,"arcurl":"https://ol.3dmgame.com/hao/10189.html","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20191104/1572844419_960881.jpg","showtype":22,"title":"《决战》 漫游星域物资箱","webviewurl":"","range_start":1572537600,"range_end":1577808000,"surplusper":92,"is_over":1,"is_empty":0,"is_avail":0},{"aid":10188,"arcurl":"https://ol.3dmgame.com/hao/10188.html","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20191101/1572593135_799564.jpg","showtype":22,"title":"《武魂2》 秘宝寻踪礼包","webviewurl":"","range_start":1572537600,"range_end":1575129600,"surplusper":99,"is_over":1,"is_empty":0,"is_avail":0},{"aid":10187,"arcurl":"https://ol.3dmgame.com/hao/10187.html","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20191030/1572422482_320126.jpg","showtype":22,"title":"《幻想全明星》 3DM新手礼包","webviewurl":"","range_start":1572364800,"range_end":1603987200,"surplusper":99,"is_over":1,"is_empty":0,"is_avail":0},{"aid":10186,"arcurl":"https://ol.3dmgame.com/hao/10186.html","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20191030/1572418423_947790.jpg","showtype":22,"title":"《红莲之王》 万圣节福利礼包","webviewurl":"","range_start":1571846400,"range_end":1577635200,"surplusper":95,"is_over":1,"is_empty":0,"is_avail":0},{"aid":10185,"arcurl":"https://ol.3dmgame.com/hao/10185.html","litpic":"https://ol.3dmgame.com/uploads/images/thumbbig/20180104/1515057789_420951.jpg","showtype":22,"title":"《新天龙八部》 新手卡","webviewurl":"","range_start":1572192000,"range_end":1603814400,"surplusper":66,"is_over":1,"is_empty":0,"is_avail":0}]
		 */

		private int total;
		private List<GameGiftBean> list;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
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
