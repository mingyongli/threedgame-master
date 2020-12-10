package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.GameBean;
import com.ws3dm.app.bean.NewsBean;

import java.util.List;

/**
 * Describution :获取内容页相关内容数据
 * 
 * Author : DKjuan
 * 
 * Date : 2018/4/23 17:38
 **/
public class NewsAboutRespBean extends BaseRespBean<NewsAboutRespBean.DataBean>{

	public static class DataBean {
		/**
		 * game : {"aid":"10022","arcurl":"http://www2.3dmgame.com/games/gd5/","title":"孤岛惊魂5","litpic":"http://www2.3dmgame.com/uploads/images/thumbkwdfirst/20180328/1522209294_996947.jpg","system":"PC PS4","score":9.1,"pubdate_at":1523635200,"showtype":3,"labels":[{"id":"7","name":"仙侠"},{"id":"4","name":"动作"},{"id":"1","name":"卖萌"},{"id":"2","name":"可爱"},{"id":"6","name":"奇幻"},{"id":"3","name":"恐怖"},{"id":"5","name":"魔幻"}]}
		 * list : [{"aid":"10037","arcurl":"http://www2.3dmgame.com/news/201804/10037.html","title":"孤岛惊魂5好看视频2","litpic":"http://www2.3dmgame.com/uploads/images/thumbnews/20180412/1523499641_125900.jpg","pubdate_at":1523499709,"showtype":1,"webviewurl":"http://www2.3dmgame.com/app/news/201804/10037.html","total_ct":0},{"aid":"10036","arcurl":"http://www2.3dmgame.com/news/201804/10036.html","title":"\t孤岛惊魂5好看视频1","litpic":"http://www2.3dmgame.com/uploads/images/thumbnews/20180412/1523497590_983106.jpg","pubdate_at":1523497594,"showtype":1,"webviewurl":"http://www2.3dmgame.com/app/news/201804/10036.html","total_ct":0},{"aid":"10028","arcurl":"http://www2.3dmgame.com/news/201804/10028.html","title":"玩家们脑洞大开 《孤岛惊魂5》自制地图十分惊艳","litpic":"http://www2.3dmgame.com/uploads/images/thumbnews/20180412/1523496931_844253.png","pubdate_at":1523433787,"showtype":1,"webviewurl":"http://www2.3dmgame.com/app/news/201804/10028.html","total_ct":0}]
		 */

		private GameBean game;
		private List<NewsBean> list;
		private List<VotetopicBean> votetopic;

		public GameBean getGame() {
			return game;
		}

		public void setGame(GameBean game) {
			this.game = game;
		}

		public List<NewsBean> getList() {
			return list;
		}

		public void setList(List<NewsBean> list) {
			this.list = list;
		}

		public List<VotetopicBean> getVotetopic() {
			return votetopic;
		}

		public void setVotetopic(List<VotetopicBean> votetopic) {
			this.votetopic = votetopic;
		}

		public static class VotetopicBean {
			/**
			 * topic_id : 112
			 * topic_title : 正当，Steam抽成过高，Epic是市场拯救者
			 * topic_num : 7
			 */

			private int topic_id;
			private String topic_title;
			private int topic_num;

			public int getTopic_id() {
				return topic_id;
			}

			public void setTopic_id(int topic_id) {
				this.topic_id = topic_id;
			}

			public String getTopic_title() {
				return topic_title;
			}

			public void setTopic_title(String topic_title) {
				this.topic_title = topic_title;
			}

			public int getTopic_num() {
				return topic_num;
			}

			public void setTopic_num(int topic_num) {
				this.topic_num = topic_num;
			}
		}

	}
}
