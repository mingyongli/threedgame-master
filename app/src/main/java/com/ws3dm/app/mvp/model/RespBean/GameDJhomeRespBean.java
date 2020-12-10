package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.GameBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameDJhomeRespBean extends BaseRespBean<GameDJhomeRespBean.DataBean>{
	public static class DataBean {
		private List<SalegameBean> salegame;
		private List<GameBean> classicgame;
		private List<GameBean> chinesegame;
		private List<GameBean> slides;

		public List<SalegameBean> getSalegame() {
			return salegame;
		}

		public void setSalegame(List<SalegameBean> salegame) {
			this.salegame = salegame;
		}

		public List<GameBean> getClassicgame() {
			return classicgame;
		}

		public void setClassicgame(List<GameBean> classicgame) {
			this.classicgame = classicgame;
		}

		public List<GameBean> getChinesegame() {
			return chinesegame;
		}

		public void setChinesegame(List<GameBean> chinesegame) {
			this.chinesegame = chinesegame;
		}

		public List<GameBean> getSlides() {
			return slides;
		}

		public void setSlides(List<GameBean> slides) {
			this.slides = slides;
		}

		public static class SalegameBean {
			/**
			 * month : 7
			 * list : [{"aid":"3741977","arcurl":"https://www.3dmgame.com/games/shingeki2/","title":"进击的巨人2：最终之战","litpic":"https://img.3dmgame.com/uploads/images/thumbkwdfirst/20190315/1552636310_580288.jpg","pubdate_at":1562256000,"showtype":3,"system":"PC Switch PS4","type":"动作游戏","pubday":"5"},{"aid":"3742109","arcurl":"https://www.3dmgame.com/games/beyondts/","title":"超凡双生","litpic":"https://img.3dmgame.com/uploads/images/thumbkwdfirst/20190701/1561973640_235742.jpg","pubdate_at":1563724800,"showtype":3,"system":"PC PS3","type":"冒险游戏","pubday":"22"},{"aid":"3548534","arcurl":"https://www.3dmgame.com/games/mbatck/","title":"怪物男孩和诅咒王国","litpic":"https://img.3dmgame.com/uploads/images/thumbkwdfirst/20180713/1531465335_527071.jpg","pubdate_at":1564070400,"showtype":3,"system":"PC Switch PS4 XBOXONE","type":"动作游戏","pubday":"26"},{"aid":"3737131","arcurl":"https://www.3dmgame.com/games/fireemblemthreehouse/","title":"火焰纹章：风花雪月","litpic":"https://img.3dmgame.com/uploads/images/thumbkwdfirst/20190215/1550212761_446133.jpg","pubdate_at":1564070400,"showtype":3,"system":"Switch","type":"角色扮演","pubday":"26"},{"aid":"3737409","arcurl":"https://www.3dmgame.com/games/wolfensteinyoungbloo/","title":"德军总部：新血液","litpic":"https://img.3dmgame.com/uploads/images/thumbkwdfirst/20180716/1531713426_730020.jpg","pubdate_at":1564070400,"showtype":3,"system":"PC Switch PS4 XBOXONE","type":"第一人称射击","pubday":"26"}]
			 */

			private String month;
			private List<GameBean> list;

			public String getMonth() {
				return month;
			}

			public void setMonth(String month) {
				this.month = month;
			}

			public List<GameBean> getList() {
				return list;
			}

			public void setList(List<GameBean> list) {
				this.list = list;
			}

			public static class ListBean {
				/**
				 * aid : 3741977
				 * arcurl : https://www.3dmgame.com/games/shingeki2/
				 * title : 进击的巨人2：最终之战
				 * litpic : https://img.3dmgame.com/uploads/images/thumbkwdfirst/20190315/1552636310_580288.jpg
				 * pubdate_at : 1562256000
				 * showtype : 3
				 * system : PC Switch PS4
				 * type : 动作游戏
				 * pubday : 5
				 */

				private String aid;
				private String arcurl;
				private String title;
				private String litpic;
				private int pubdate_at;
				private int showtype;
				private String system;
				private String type;
				private String pubday;

				public String getAid() {
					return aid;
				}

				public void setAid(String aid) {
					this.aid = aid;
				}

				public String getArcurl() {
					return arcurl;
				}

				public void setArcurl(String arcurl) {
					this.arcurl = arcurl;
				}

				public String getTitle() {
					return title;
				}

				public void setTitle(String title) {
					this.title = title;
				}

				public String getLitpic() {
					return litpic;
				}

				public void setLitpic(String litpic) {
					this.litpic = litpic;
				}

				public int getPubdate_at() {
					return pubdate_at;
				}

				public void setPubdate_at(int pubdate_at) {
					this.pubdate_at = pubdate_at;
				}

				public int getShowtype() {
					return showtype;
				}

				public void setShowtype(int showtype) {
					this.showtype = showtype;
				}

				public String getSystem() {
					return system;
				}

				public void setSystem(String system) {
					this.system = system;
				}

				public String getType() {
					return type;
				}

				public void setType(String type) {
					this.type = type;
				}

				public String getPubday() {
					return pubday;
				}

				public void setPubday(String pubday) {
					this.pubday = pubday;
				}
			}
		}
	}
}
