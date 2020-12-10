package com.ws3dm.app.mvp.model.RespBean;

import com.google.gson.annotations.SerializedName;
import com.ws3dm.app.bean.AvatarBean;
import com.ws3dm.app.bean.NewsBean;

import java.util.List;

/**
 * Author : DKjuan:新闻数码页分类列表
 * <p>
 * Date :  2017/8/21  12:06
 */
public class NewsDigitalRespBean extends BaseRespBean<NewsDigitalRespBean.DataBean>{

	/**
	 * data : {"total":100,"list":[{"arcurl":"https://ol.3dmgame.com/gl/64065.html","aid":64065,"title":"《LOL》全球总决赛2019代币奖励预览","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569566725732.jpg","showtype":2,"click":5,"total_ct":0,"pubdate_at":1569566744,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/64065.html"},{"arcurl":"https://ol.3dmgame.com/gl/65068.html","aid":65068,"title":"《LOL》云顶之弈9.19动物园阵容搭配","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569566358773.jpg","showtype":2,"click":24,"total_ct":0,"pubdate_at":1569566361,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/65068.html"},{"arcurl":"https://ol.3dmgame.com/gl/65067.html","aid":65067,"title":"《LOL》云顶之弈9.19版本冷门吃鸡阵容","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569566259736.jpg","showtype":2,"click":23,"total_ct":0,"pubdate_at":1569566264,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/65067.html"},{"arcurl":"https://ol.3dmgame.com/gl/65066.html","aid":65066,"title":"《LOL》云顶之弈9.19版本剑士阵容","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569565924840.jpg","showtype":2,"click":24,"total_ct":0,"pubdate_at":1569565926,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/65066.html"},{"arcurl":"https://ol.3dmgame.com/gl/64719.html","aid":64719,"title":"《LOL》全球总决赛2019代币介绍","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569565833306.png","showtype":2,"click":13,"total_ct":0,"pubdate_at":1569565857,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/64719.html"},{"arcurl":"https://ol.3dmgame.com/gl/65063.html","aid":65063,"title":"《LOL》云顶之弈9.19版本潘森双龙6换形阵容搭配","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569565844662.jpg","showtype":2,"click":15,"total_ct":0,"pubdate_at":1569565846,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/65063.html"},{"arcurl":"https://ol.3dmgame.com/gl/65060.html","aid":65060,"title":"《LOL》云顶之弈9.19恶魔元素阵容站位技巧","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569565544804.jpg","showtype":2,"click":16,"total_ct":0,"pubdate_at":1569565546,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/65060.html"},{"arcurl":"https://ol.3dmgame.com/gl/64715.html","aid":64715,"title":"《LOL》全球总决赛2019门票任务预览","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569565317178.jpg","showtype":2,"click":25,"total_ct":0,"pubdate_at":1569565350,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/64715.html"},{"arcurl":"https://ol.3dmgame.com/gl/65059.html","aid":65059,"title":"《LOL》云顶之弈9.19贵族阵容布阵方案","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569565099519.jpg","showtype":2,"click":24,"total_ct":0,"pubdate_at":1569565345,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/65059.html"},{"arcurl":"https://ol.3dmgame.com/gl/64714.html","aid":64714,"title":"《LOL》全球总决赛2019门票里程碑任务介绍","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569564996556.jpg","showtype":2,"click":8,"total_ct":0,"pubdate_at":1569565013,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/64714.html"}]}
	 */
	public static class DataBean {
		/**
		 * total : 100
		 * list : [{"arcurl":"https://ol.3dmgame.com/gl/64065.html","aid":64065,"title":"《LOL》全球总决赛2019代币奖励预览","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569566725732.jpg","showtype":2,"click":5,"total_ct":0,"pubdate_at":1569566744,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/64065.html"},{"arcurl":"https://ol.3dmgame.com/gl/65068.html","aid":65068,"title":"《LOL》云顶之弈9.19动物园阵容搭配","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569566358773.jpg","showtype":2,"click":24,"total_ct":0,"pubdate_at":1569566361,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/65068.html"},{"arcurl":"https://ol.3dmgame.com/gl/65067.html","aid":65067,"title":"《LOL》云顶之弈9.19版本冷门吃鸡阵容","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569566259736.jpg","showtype":2,"click":23,"total_ct":0,"pubdate_at":1569566264,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/65067.html"},{"arcurl":"https://ol.3dmgame.com/gl/65066.html","aid":65066,"title":"《LOL》云顶之弈9.19版本剑士阵容","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569565924840.jpg","showtype":2,"click":24,"total_ct":0,"pubdate_at":1569565926,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/65066.html"},{"arcurl":"https://ol.3dmgame.com/gl/64719.html","aid":64719,"title":"《LOL》全球总决赛2019代币介绍","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569565833306.png","showtype":2,"click":13,"total_ct":0,"pubdate_at":1569565857,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/64719.html"},{"arcurl":"https://ol.3dmgame.com/gl/65063.html","aid":65063,"title":"《LOL》云顶之弈9.19版本潘森双龙6换形阵容搭配","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569565844662.jpg","showtype":2,"click":15,"total_ct":0,"pubdate_at":1569565846,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/65063.html"},{"arcurl":"https://ol.3dmgame.com/gl/65060.html","aid":65060,"title":"《LOL》云顶之弈9.19恶魔元素阵容站位技巧","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569565544804.jpg","showtype":2,"click":16,"total_ct":0,"pubdate_at":1569565546,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/65060.html"},{"arcurl":"https://ol.3dmgame.com/gl/64715.html","aid":64715,"title":"《LOL》全球总决赛2019门票任务预览","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569565317178.jpg","showtype":2,"click":25,"total_ct":0,"pubdate_at":1569565350,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/64715.html"},{"arcurl":"https://ol.3dmgame.com/gl/65059.html","aid":65059,"title":"《LOL》云顶之弈9.19贵族阵容布阵方案","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569565099519.jpg","showtype":2,"click":24,"total_ct":0,"pubdate_at":1569565345,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/65059.html"},{"arcurl":"https://ol.3dmgame.com/gl/64714.html","aid":64714,"title":"《LOL》全球总决赛2019门票里程碑任务介绍","litpic":"https://ol.3dmgame.com/uploads/images/thumbnews/2019/0927/1569564996556.jpg","showtype":2,"click":8,"total_ct":0,"pubdate_at":1569565013,"webviewurl":"https://m.3dmgame.com/ol/webview/gl/64714.html"}]
		 */

		private int total;
		private List<NewsBean> list;
		private List<AvatarBean> typelist;

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

		public List<AvatarBean> getTypelist() {
			return typelist;
		}

		public void setTypelist(List<AvatarBean> typelist) {
			this.typelist = typelist;
		}
	}
}
