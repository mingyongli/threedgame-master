package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.CollectGamebean;
import com.ws3dm.app.bean.SearchGamebean;

import java.util.List;

/**
 * Describution :收藏结果类
 * 
 * Author : DKjuan
 * 
 * Date : 2018/3/27 18:36
 **/
public class CollectGameRespBean extends BaseRespBean<CollectGameRespBean.DataBean>{

	public static class DataBean {
		/**
		 * total : 11
		 * list : [{"aid":"267619","showtype":3,"arcurl":"https://www.3dmgame.com/games/tes4/","title":"上古卷轴4：湮没","litpic":"https://img.3dmgame.com/uploads/images/thumbkwdfirst/20180528/1527486460_440299.jpg","type":"角色扮演","system":"PC PS3 XBOX360","label":"沙盒 魔幻 开放世界 暴力","pubdate_at":1142870400,"score":"9.2","webviewurl":""},{"aid":"268454","showtype":3,"arcurl":"https://www.3dmgame.com/games/elderscrolls5skyrim/","title":"上古卷轴5：天际","litpic":"https://img.3dmgame.com/uploads/images/thumbkwdfirst/20180529/1527578329_606706.jpg","type":"角色扮演","system":"PC PS4 XBOXONE PS3 XBOX360","label":"剧情 开放世界 经典","pubdate_at":1320940800,"score":"9.2","webviewurl":""},{"aid":"3577703","showtype":3,"arcurl":"https://www.3dmgame.com/games/es6/","title":"上古卷轴6","litpic":"https://img.3dmgame.com/uploads/images/thumbkwdfirst/20180711/1531273655_978490.jpg","type":"角色扮演","system":"PC","label":"魔幻 神话 经典","pubdate_at":1468653293,"score":"8.1","webviewurl":""}]
		 */

		private int total;
		private List<CollectGamebean> list;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public List<CollectGamebean> getList() {
			return list;
		}

		public void setList(List<CollectGamebean> list) {
			this.list = list;
		}

	}
}
