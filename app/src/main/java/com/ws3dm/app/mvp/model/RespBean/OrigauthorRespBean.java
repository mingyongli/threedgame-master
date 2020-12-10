package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.AuthorTeamBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class OrigauthorRespBean extends BaseRespBean<OrigauthorRespBean.DataBean>{

	public static class DataBean {
		/**
		 * total : 10
		 * list : [{"aid":5,"name":"我是老瓜皮","litpic":"https://work.3dmgame.com/uploads/images/users/17.jpg","desc":"表达自己对电竞、电影、动漫与游戏界各类时事的见解\r\n皮而不瓜是唯一信条","showtype":16,"total_pub":"39"},{"aid":13,"name":"嘉言","litpic":"https://work.3dmgame.com/uploads/images/users/191.jpg","desc":"一个树洞","showtype":16,"total_pub":"31"},{"aid":16,"name":"liugaotian","litpic":"https://work.3dmgame.com/uploads/images/users/200.jpg","desc":"Biang！","showtype":16,"total_pub":"11"},{"aid":19,"name":"银河正义使者","litpic":"https://work.3dmgame.com/uploads/images/users/67.jpg","desc":"电子游戏救世界","showtype":16,"total_pub":"356"},{"aid":20,"name":"四氧化三铁","litpic":"https://work.3dmgame.com/uploads/images/users/21.jpg","desc":"Bang！","showtype":16,"total_pub":"56"},{"aid":23,"name":"海涅","litpic":"https://work.3dmgame.com/uploads/images/users/216.jpg","desc":"于心于寂","showtype":16,"total_pub":"17"},{"aid":25,"name":"小黑麦","litpic":"https://work.3dmgame.com/uploads/images/users/221.jpg","desc":"3DM刘德华，有且只有一个。","showtype":16,"total_pub":"13"},{"aid":27,"name":"沼雀","litpic":"https://work.3dmgame.com/uploads/images/users/20181126/1543209365_932917.jpg","desc":"止于砚沼","showtype":16,"total_pub":"10"},{"aid":31,"name":"店点","litpic":"https://work.3dmgame.com/uploads/images/users/20190312/1552376540_340412.jpg","desc":"老实的游戏玩家","showtype":16,"total_pub":"20"},{"aid":41,"name":"木大木大木大","litpic":"https://work.3dmgame.com/uploads/images/users/20190709/1562659096_921611.jpg","desc":"想安安静静的","showtype":16,"total_pub":"13"}]
		 */

		private int total;
		private List<AuthorTeamBean> list;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public List<AuthorTeamBean> getList() {
			return list;
		}

		public void setList(List<AuthorTeamBean> list) {
			this.list = list;
		}
	}
}
