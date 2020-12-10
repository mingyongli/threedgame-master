package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.SoftGameBean;

import java.util.List;

/**
 * Author : DKjuan:手游汉化
 * <p>
 * Date :  2017/8/21  12:06
 */
public class MGChinaRespBean extends BaseRespBean<MGChinaRespBean.DataBean>{

	public static class DataBean {
		/**
		 * total : 3
		 * list : [{"aid":"10071","arcurl":"http://shouyou2.3dmgame.com/android/10071.html","title":"肥皂清洁队 汉化版","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0421/1524280126745140.png","soft_size":"71.51MB","soft_ver":"1.0.1","desc":"","downurl":"https://down4.cudown.com/app/feizaoqingjiedui.apk","showtype":4},{"aid":"10070","arcurl":"http://shouyou2.3dmgame.com/android/10070.html","title":"王国保卫战 3DM汉化版","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0421/1524279890709444.png","soft_size":"177.63MB","soft_ver":"1.1.3","desc":"","downurl":"https://down4.cudown.com/app/wangguobaoweizhan3dmhanhuaban.apk","showtype":4},{"aid":"10056","arcurl":"http://shouyou2.3dmgame.com/android/10056.html","title":"滚来滚去 3DM汉化版","litpic":"http://shouyou2.3dmgame.com/uploadimg/ico/2018/0418/1524036966997681.png","soft_size":"46.84MB","soft_ver":"1.2.4","desc":"","downurl":"https://down4.cudown.com/app/gunlaigunquhanhuaban.apk","showtype":4}]
		 */

		private String total;
		private List<SoftGameBean> list;

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public List<SoftGameBean> getList() {
			return list;
		}

		public void setList(List<SoftGameBean> list) {
			this.list = list;
		}
	}
}
