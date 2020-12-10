package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.GameBean;

import java.util.List;

/**
 * Author : DKjuan:单机用的实体类
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameDJRespBean extends BaseRespBean<GameDJRespBean.DataBean>{

	public static class DataBean {
		/**
		 * total : 2
		 * list : [{"aid":"3694923","arcurl":"https://www.3dmgame.com/games/moonlighter/","title":"夜勤人","litpic":"https://img.3dmgame.com/uploads/images/thumbkwdfirst/20190411/1554970569_639368.jpg","pubdate_at":1527523200,"showtype":3,"system":"PC","score":"9.7","labels":[{"id":"35","name":"像素"},{"id":"56","name":"地牢"},{"id":"75","name":"闯关"}]},{"aid":"3670399","arcurl":"https://www.3dmgame.com/games/pillarsofeternity2/","title":"永恒之柱2：死火","litpic":"https://img.3dmgame.com/uploads/images/thumbkwdfirst/20180609/1528527261_947462.jpg","pubdate_at":1525795200,"showtype":3,"system":"PC/Switch/PS4/XBOXONE","score":"8.2","labels":[{"id":"3","name":"魔幻"},{"id":"22","name":"探险"},{"id":"69","name":"剧情"}]}]
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
