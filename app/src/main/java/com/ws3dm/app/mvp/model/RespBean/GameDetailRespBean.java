package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.bean.GameGiftBean;
import com.ws3dm.app.bean.GllistBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.OriginalBean;
import com.ws3dm.app.bean.PiclistBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class GameDetailRespBean extends BaseRespBean<GameDetailRespBean.DataBean>{
	public static class DataBean {
		/**
		 * aid : 3319098
		 * arcurl : https://www.3dmgame.com/games/theforest/
		 * title : 森林
		 * ename : forest
		 * litpic : https://img.3dmgame.com/uploads/images/thumbkwdfirst/20180728/1532743375_888439.jpg
		 * bgpic : https://img.3dmgame.com/uploads/images/thumbkwdfirst/20180728/1532743379_685487.jpg
		 * click : 1632271
		 * system : PC/PS4
		 * score : 9.1
		 * pubdate_at : 1525017600
		 * typeid : 4
		 * type : 动作游戏
		 * made : Endnight Games Ltd
		 * publisher : Endnight Games Ltd
		 * language : 英文
		 * labels : [{"id":16,"name":"生存"},{"id":22,"name":"探险"}]
		 * imgs : ["https://img.3dmgame.com/uploads/images/thumbnews/2018/1206/1544074446983.jpg","https://www.3dmgame.com/uploads/allimg/170713/321-1FG3192246.jpg","https://www.3dmgame.com/uploads/allimg/170713/321-1FG3192247.jpg","https://www.3dmgame.com/uploads/allimg/170713/321-1FG3192248.jpg","https://www.3dmgame.com/uploads/allimg/170713/321-1FG3192248-50.jpg","https://www.3dmgame.com/uploads/allimg/170713/321-1FG3192249.jpg","https://www.3dmgame.com/uploads/allimg/170713/321-1FG3192249-50.jpg"]
		 * smallimgs : [{"url":"https://img.3dmgame.com/uploads/images/thumbnews/2018/1206/1544074446983.jpg","aid":3750888,"showtype":10},{"url":"https://www.3dmgame.com/uploads/allimg/170713/r_321-1FG3192246.jpg","aid":0,"showtype":0},{"url":"https://www.3dmgame.com/uploads/allimg/170713/r_321-1FG3192247.jpg","aid":0,"showtype":0},{"url":"https://www.3dmgame.com/uploads/allimg/170713/r_321-1FG3192248.jpg","aid":0,"showtype":0},{"url":"https://www.3dmgame.com/uploads/allimg/170713/r_321-1FG3192248-50.jpg","aid":0,"showtype":0},{"url":"https://www.3dmgame.com/uploads/allimg/170713/r_321-1FG3192249.jpg","aid":0,"showtype":0},{"url":"https://www.3dmgame.com/uploads/allimg/170713/r_321-1FG3192249-50.jpg","aid":0,"showtype":0}]
		 * lowconfig : [{"title":"操作系统","content":"Windows 7"},{"title":"CPU","content":"Intel Dual-Core 2.4 GHz"},{"title":"内存","content":"4 GB RAM"},{"title":"显卡","content":"NVIDIA GeForce 8800GT"},{"title":"存储空间","content":"5 GB available space"}]
		 * highconfig : [{"title":"操作系统","content":"Windows 7"},{"title":"CPU","content":"Quad Core Processor"},{"title":"内存","content":"4 GB RAM"},{"title":"显卡","content":"NVIDIA GeForce GTX 560"},{"title":"存储空间","content":"5 GB available space"}]
		 * gllist : [{"aid":"3769436","arcurl":"https://www.3dmgame.com/gl/3769436.html","title":"森林怎么拆建筑","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/2019/0426/1556266901673.png","click":1376,"pubdate_at":1556266903,"created_userid":219,"showtype":2,"webviewurl":"https://m.3dmgame.com/webview/gl/3769436.html","total_ct":0,"type":"攻略"},{"aid":"3769432","arcurl":"https://www.3dmgame.com/gl/3769432.html","title":"森林电锯在哪","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/2019/0426/1556266530839.png","click":4119,"pubdate_at":1556266543,"created_userid":219,"showtype":2,"webviewurl":"https://m.3dmgame.com/webview/gl/3769432.html","total_ct":0,"type":"攻略"}]
		 * evaluatelist : [{"aid":3318898,"arcurl":"https://www.3dmgame.com/score/3318898.html","title":"3DM《森林》试玩版详细评测：活下来就是胜利","litpic":"https://img.3dmgame.com/uploads/images/thumbpcfirst/20180711/1531299169_953641.jpg","showtype":6,"webviewurl":"https://m.3dmgame.com/webview/score/3318898.html","type":"评测"}]
		 * newslist : [{"aid":"3751329","arcurl":"https://www.3dmgame.com/news/201812/3751329.html","title":"恐怖生存游戏《森林》免费更新 加入新怪物新要素","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/2018/1212/1544595485550.jpg","click":7868,"pubdate_at":1544595516,"created_userid":48,"showtype":1,"webviewurl":"https://m.3dmgame.com/webview/news/201812/3751329.html","total_ct":7,"type":"新闻"},{"aid":"3751116","arcurl":"https://www.3dmgame.com/news/201812/3751116.html","title":"生存游戏《森林》五年后仍在更新 开发组公布计划","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/2018/1209/1544339880966.jpg","click":5566,"pubdate_at":1544339885,"created_userid":49,"showtype":1,"webviewurl":"https://m.3dmgame.com/webview/news/201812/3751116.html","total_ct":20,"type":"新闻"},{"aid":"3748797","arcurl":"https://www.3dmgame.com/news/201811/3748797.html","title":"《森林》Steam销量超500万 初始预算只有12.5万美元","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/2018/1106/1541506687274.jpg","click":7524,"pubdate_at":1541506794,"created_userid":64,"showtype":1,"webviewurl":"https://m.3dmgame.com/webview/news/201811/3748797.html","total_ct":5,"type":"新闻"}]
		 * showtype : 3
		 * content : 《森林(The Forest)》是一款由Endnight Games  Limited制作并发行的恐怖冒险类游戏，游戏中玩家必须建造设施，探索世界，生存下去。《森林》将打造一个活生生的，气候多变，植被动态生长凋零，地下洞穴错综复杂的森林，等待玩家探索。
		 玩家必须砍树建造营地，生火取暖，收集食物，甚至还可以种植农作物。玩家可以藏匿行踪躲避敌人，也可以用石块和木棒制作武器，与敌人正面交战。而玩家的敌人是一群变异人，他们和正常人类一样，有着自己的家庭和道德观。
		 */

		private int aid;
		private String arcurl;
		private String title;
		private String ename;
		private String litpic;
		private String bgpic;
		private int click;
		private String system;
		private String score;
		private int pubdate_at;
		private int typeid;
		private String firm;
		private String type;
		private String betatime;
		private String state;
		private String made;
		private String publisher;
		private String language;
		private int showtype;
		private String content;
		private String version;
		private List<AvatarBean> labels;
		private List<String> imgs;
		private List<SmallimgsBean> smallimgs;
		private List<LowconfigBean> lowconfig;
		private List<HighconfigBean> highconfig;
		private List<NewsBean> gllist;
		private List<OriginalBean> evaluatelist;
		private List<NewsBean> newslist;
		private List<PiclistBean> glpictlist;
		private List<GllistBean> gltxtlist;
		private List<GameGiftBean> libao;

		public int getAid() {
			return aid;
		}

		public void setAid(int aid) {
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

		public String getEname() {
			return ename;
		}

		public void setEname(String ename) {
			this.ename = ename;
		}

		public String getLitpic() {
			return litpic;
		}

		public void setLitpic(String litpic) {
			this.litpic = litpic;
		}

		public String getBgpic() {
			return bgpic;
		}

		public void setBgpic(String bgpic) {
			this.bgpic = bgpic;
		}

		public int getClick() {
			return click;
		}

		public void setClick(int click) {
			this.click = click;
		}

		public String getSystem() {
			return system;
		}

		public void setSystem(String system) {
			this.system = system;
		}

		public String getScore() {
			return score;
		}

		public void setScore(String score) {
			this.score = score;
		}

		public int getPubdate_at() {
			return pubdate_at;
		}

		public void setPubdate_at(int pubdate_at) {
			this.pubdate_at = pubdate_at;
		}

		public int getTypeid() {
			return typeid;
		}

		public void setTypeid(int typeid) {
			this.typeid = typeid;
		}

		public String getFirm() {
			return firm;
		}

		public void setFirm(String firm) {
			this.firm = firm;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getBetatime() {
			return betatime;
		}

		public void setBetatime(String betatime) {
			this.betatime = betatime;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getMade() {
			return made;
		}

		public void setMade(String made) {
			this.made = made;
		}

		public String getPublisher() {
			return publisher;
		}

		public void setPublisher(String publisher) {
			this.publisher = publisher;
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public int getShowtype() {
			return showtype;
		}

		public void setShowtype(int showtype) {
			this.showtype = showtype;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public List<AvatarBean> getLabels() {
			return labels;
		}

		public void setLabels(List<AvatarBean> labels) {
			this.labels = labels;
		}

		public List<String> getImgs() {
			return imgs;
		}

		public void setImgs(List<String> imgs) {
			this.imgs = imgs;
		}

		public List<SmallimgsBean> getSmallimgs() {
			return smallimgs;
		}

		public void setSmallimgs(List<SmallimgsBean> smallimgs) {
			this.smallimgs = smallimgs;
		}

		public List<LowconfigBean> getLowconfig() {
			return lowconfig;
		}

		public void setLowconfig(List<LowconfigBean> lowconfig) {
			this.lowconfig = lowconfig;
		}

		public List<HighconfigBean> getHighconfig() {
			return highconfig;
		}

		public void setHighconfig(List<HighconfigBean> highconfig) {
			this.highconfig = highconfig;
		}

		public List<NewsBean> getGllist() {
			return gllist;
		}

		public void setGllist(List<NewsBean> gllist) {
			this.gllist = gllist;
		}

		public List<OriginalBean> getEvaluatelist() {
			return evaluatelist;
		}

		public void setEvaluatelist(List<OriginalBean> evaluatelist) {
			this.evaluatelist = evaluatelist;
		}

		public List<NewsBean> getNewslist() {
			return newslist;
		}

		public void setNewslist(List<NewsBean> newslist) {
			this.newslist = newslist;
		}

		public List<PiclistBean> getGlpictlist() {
			return glpictlist;
		}

		public void setGlpictlist(List<PiclistBean> glpictlist) {
			this.glpictlist = glpictlist;
		}

		public List<GllistBean> getGltxtlist() {
			return gltxtlist;
		}

		public void setGltxtlist(List<GllistBean> gltxtlist) {
			this.gltxtlist = gltxtlist;
		}

		public List<GameGiftBean> getLibao() {
			return libao;
		}

		public void setLibao(List<GameGiftBean> libao) {
			this.libao = libao;
		}

		public static class SmallimgsBean extends NewsBean {
			/**
			 * aid : 10047
			 * arcurl : http://www2.3dmgame.com/gl/10047.html
			 * title : 《孤岛惊魂5》再来一篇很屌的攻略看看
			 * litpic : http://www2.3dmgame.com/uploads/images/thumbnews/20180411/1523432485_197451.jpg
			 * showtype : 2
			 * webviewurl : http://www2.3dmgame.com/app/gl/10047.html
			 */

			private String url;

			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}
		}

		public static class LowconfigBean {
			/**
			 * title : 操作系统
			 * content : Windows 7
			 */

			private String title;
			private String content;

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
			}
		}

		public static class HighconfigBean {
			/**
			 * title : 操作系统
			 * content : Windows 7
			 */

			private String title;
			private String content;

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
			}
		}
	}
}
