package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.SlidesBean;
import com.ws3dm.app.bean.UserDataBean;

import java.util.List;

/**
 * Author : DKjuan:新闻页分类列表
 * <p>
 * Date :  2017/8/21  12:06
 */
public class NewsListRespBean extends BaseRespBean<NewsListRespBean.DataBean>{

	public static class DataBean {
		/**
		 * list : [{"aid":10047,"arcurl":"http://www2.3dmgame.com/gl/10047.html","title":"《孤岛惊魂5》再来一篇很屌的攻略看看","litpic":"http://www2.3dmgame.com/uploads/images/thumbnews/20180411/1523432485_197451.jpg","showtype":2,"click":3,"total_ct":0,"pubdate_at":1523432490,"webviewurl":"http://www2.3dmgame.com/app/gl/10047.html","user":{"id":12,"nickname":"ChunTian","avatarstr":"http://work.3dmgame.com/uploads/images/users/12.jpg"}},{"aid":10049,"arcurl":"http://www2.3dmgame.com/gl/10049.html","title":"《孤岛惊魂5》强势一点点的攻略内容","litpic":"http://www2.3dmgame.com/uploads/images/thumbnews/20180411/1523434180_454519.jpg","showtype":2,"click":1,"total_ct":0,"pubdate_at":1523434190,"webviewurl":"http://www2.3dmgame.com/app/gl/10049.html","user":{"id":12,"nickname":"ChunTian","avatarstr":"http://work.3dmgame.com/uploads/images/users/12.jpg"}},{"aid":10050,"arcurl":"http://www2.3dmgame.com/gl/10050.html","title":"《孤岛惊魂5》公里额俩阿萨德群翁","litpic":"http://www2.3dmgame.com/uploads/images/thumbnews/20180411/1523434718_657689.jpg","showtype":2,"click":1,"total_ct":0,"pubdate_at":1523434740,"webviewurl":"http://www2.3dmgame.com/app/gl/10050.html","user":{"id":12,"nickname":"ChunTian","avatarstr":"http://work.3dmgame.com/uploads/images/users/12.jpg"}},{"aid":10051,"arcurl":"http://www2.3dmgame.com/gl/10051.html","title":"《孤岛惊魂5》和投入为而且其 阿萨德天","litpic":"http://www2.3dmgame.com/uploads/images/thumbnews/20180411/1523434786_706275.jpg","showtype":2,"click":4,"total_ct":0,"pubdate_at":1523434790,"webviewurl":"http://www2.3dmgame.com/app/gl/10051.html","user":{"id":12,"nickname":"ChunTian","avatarstr":"http://work.3dmgame.com/uploads/images/users/12.jpg"}},{"aid":10041,"arcurl":"http://www2.3dmgame.com/gl/10041.html","title":"《孤岛惊魂5》隐藏任务位置及所有任务列表2","litpic":"http://www2.3dmgame.com/uploads/images/thumbnews/20180410/1523337550_171900.jpg","showtype":2,"click":23,"total_ct":0,"pubdate_at":1523337555,"webviewurl":"http://www2.3dmgame.com/app/gl/10041.html","user":{"id":12,"nickname":"ChunTian","avatarstr":"http://work.3dmgame.com/uploads/images/users/12.jpg"}},{"aid":10001,"arcurl":"http://www2.3dmgame.com/bagua/10001.html","title":"《怪物猎人》主题餐厅开业 还有美女亲自为你服务","litpic":"http://www2.3dmgame.com/uploads/images/thumbnews/20180405/1522907995_621157.jpg","showtype":5,"click":32,"total_ct":1,"pubdate_at":1522907896,"webviewurl":"http://www2.3dmgame.com/app/bagua/10001.html","user":{"id":13,"nickname":"笨南北","avatarstr":"http://work.3dmgame.com/uploads/images/users/13.jpg"}},{"aid":10000,"arcurl":"http://www2.3dmgame.com/bagua/10000.html","title":"《云巅》漫画剧情-战神陨落","litpic":"http://www2.3dmgame.com/uploads/images/thumbnews/20180330/1522387272_587081.jpg","showtype":5,"click":17,"total_ct":1,"pubdate_at":1522387276,"webviewurl":"http://www2.3dmgame.com/app/bagua/10000.html","user":{"id":12,"nickname":"ChunTian","avatarstr":"http://work.3dmgame.com/uploads/images/users/12.jpg"}},{"aid":10002,"arcurl":"http://www2.3dmgame.com/score/10002.html","title":"《孤岛惊魂5》评测7.4分：希望郡很美，但不够惊艳","litpic":"http://www2.3dmgame.com/uploads/images/thumbpcfirst/20180406/1523001245_637123.jpg","showtype":7,"click":5,"total_ct":0,"pubdate_at":1523001258,"webviewurl":"http://www2.3dmgame.com/app/score/10002.html","user":{"id":12,"nickname":"ChunTian","avatarstr":"http://work.3dmgame.com/uploads/images/users/12.jpg"}},{"aid":10004,"arcurl":"http://www2.3dmgame.com/score/10004.html","title":"《最终幻想15》评测8.9分：偏科的\u201c自驾游\u201d世界","litpic":"http://www2.3dmgame.com/uploads/images/thumbpcfirst/20180406/1523006495_364252.png","showtype":7,"click":6,"total_ct":1,"pubdate_at":1523006506,"webviewurl":"http://www2.3dmgame.com/app/score/10004.html","user":{"id":12,"nickname":"ChunTian","avatarstr":"http://work.3dmgame.com/uploads/images/users/12.jpg"}},{"aid":10005,"arcurl":"http://www2.3dmgame.com/score/10005.html","title":"《陷阵之志》评测：小巧精悍的像素\u201c环太平洋\u201d","litpic":"http://www2.3dmgame.com/uploads/images/thumbpcfirst/20180409/1523257773_560090.jpg","showtype":7,"click":0,"total_ct":0,"pubdate_at":1523257792,"webviewurl":"http://www2.3dmgame.com/app/score/10005.html","user":{"id":12,"nickname":"ChunTian","avatarstr":"http://work.3dmgame.com/uploads/images/users/12.jpg"}}]
		 * total : 14
		 * slides : [{"arcurl":"http://www2.3dmgame.com/news/201804/10018.html","litpic":"/uploads/images/thumbnews/20180408/1523196114_567513.jpg","title":"3DM汉化组制作《拳皇97：全球对决》汉化补丁发布","showtype":"1","webviewurl":"http://www2.3dmgame.com/app/news/201804/10018.html"}]
		 */

		private int total;
		private List<NewsBean> list;
		private List<SlidesBean> slides;
		private NewsBean vote;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public List<NewsBean> getList() {
			return list;
		}

		public void setList(List<NewsBean> list) {
			this.list = list;
		}

		public List<SlidesBean> getSlides() {
			return slides;
		}

		public void setSlides(List<SlidesBean> slides) {
			this.slides = slides;
		}

		public NewsBean getVote() {
			return vote;
		}

		public void setVote(NewsBean vote) {
			this.vote = vote;
		}
	}
}
