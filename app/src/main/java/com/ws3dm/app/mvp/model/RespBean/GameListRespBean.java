package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.GameBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameListRespBean extends BaseRespBean<GameListRespBean.DataBean>{

	public static class DataBean {
		/**
		 * total : 12
		 * list : [{"aid":"10042","arcurl":"http://www2.3dmgame.com/games/gd4/","title":"孤岛惊魂4","litpic":"http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20180411/1523429063_435494.jpg","pubdate_at":"1523462400","language":"简体中文","showtype":3,"score":9.1,"labels":[{"id":"14","name":"壁纸"},{"id":"15","name":"射击"}]},{"aid":"10009","arcurl":"http://www2.3dmgame.com/games/zcmssy/","title":"战锤末世鼠疫2","litpic":"http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20180404/1522810464_819348.jpg","pubdate_at":"1520524800","language":"简体中文","showtype":3,"score":9.1,"labels":[{"id":"4","name":"动作"},{"id":"5","name":"魔幻"}]},{"aid":"10015","arcurl":"http://www2.3dmgame.com/games/zzhx15/","title":"最终幻想15","litpic":"http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20180406/1522978579_543564.jpg","pubdate_at":"1521216000","language":"简体中文","showtype":3,"score":9.1,"labels":[{"id":"4","name":"动作"},{"id":"5","name":"魔幻"},{"id":"8","name":"角色扮演"},{"id":"9","name":"自由"},{"id":"10","name":"沙盒"}]},{"aid":"10023","arcurl":"http://www2.3dmgame.com/games/metalgearsurvive/","title":"合金装备：幸存","litpic":"http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20180406/1522982849_160022.jpg","pubdate_at":"1521475200","language":"英文","showtype":3,"score":9.1,"labels":[{"id":"4","name":"动作"},{"id":"9","name":"自由"},{"id":"10","name":"沙盒"},{"id":"15","name":"射击"}]},{"aid":"10026","arcurl":"http://www2.3dmgame.com/games/hxqs/","title":"火星求生","litpic":"http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20180406/1522990819_647704.jpg","pubdate_at":"1521043200","language":"英文","showtype":3,"score":9.1,"labels":[{"id":"9","name":"自由"},{"id":"10","name":"沙盒"},{"id":"16","name":"科技"},{"id":"17","name":"太空"}]},{"aid":"10018","arcurl":"http://www2.3dmgame.com/games/zndhx2/","title":"宅男的幻想2","litpic":"http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20180405/1522915340_820888.jpg","pubdate_at":"1522339200","language":"简体中文","showtype":3,"score":9.1,"labels":[{"id":"1","name":"卖萌"},{"id":"2","name":"可爱"},{"id":"13","name":"图片"},{"id":"14","name":"壁纸"},{"id":"12","name":"福利"},{"id":"8","name":"角色扮演"}]},{"aid":"10029","arcurl":"http://www2.3dmgame.com/games/qyrs/","title":"奇异人生","litpic":"http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20180406/1523000194_252514.jpg","pubdate_at":"1422547200","language":"简体中文","showtype":3,"score":9.1,"labels":[{"id":"4","name":"动作"},{"id":"8","name":"角色扮演"},{"id":"9","name":"自由"},{"id":"10","name":"沙盒"}]}]
		 */

		private String total;
		private List<GameBean> list;

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public List<GameBean> getList() {
			return list;
		}

		public void setList(List<GameBean> list) {
			this.list = list;
		}
	}
}
