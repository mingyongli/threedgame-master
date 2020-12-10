package com.ws3dm.app.mvp.model.RespBean;

import com.google.gson.annotations.SerializedName;
import com.ws3dm.app.bean.CommentBean;
import com.ws3dm.app.bean.MyCommentBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/17  16:22
 */
public class MyCommentRespBean extends BaseRespBean<MyCommentRespBean.DataBean>{


	/**
	 * data : {"total":15,"list":[{"id":"16","content":"《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末","aid":"10066","arcurl":"http://ol.3dmgame.com/news/201712/10066.html","title":"《DNF》板砖适合解决问题  魔盒正确的打开方式","showtype":1,"total_ct":"0","pubdate_at":"1512638676","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/news/201712/10066.html"},{"id":"15","content":"《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末","aid":3,"arcurl":"http://ol.3dmgame.com/dnf","title":"地下城与勇士","showtype":0,"total_ct":"1","pubdate_at":"1512638637","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/dnf"},{"id":"14","content":"《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换","aid":"10068","arcurl":"http://ol.3dmgame.com/news/201712/10068.html","title":"《英雄联盟》12月8日免费英雄更换","showtype":1,"total_ct":"0","pubdate_at":"1512633145","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/news/201712/10068.html"},{"id":"13","content":"镇魔曲 媒体专属礼包镇魔曲 媒体专属礼包镇魔曲 媒体专属礼包镇魔曲 媒体专属礼包镇魔曲 媒体专属礼包镇魔曲 媒体专属礼包镇魔曲 媒体专属礼包镇魔曲 媒体专属礼包","aid":"10004","arcurl":"http://ol.3dmgame.com/hao/10004.html","title":"《镇魔曲》 媒体专属礼包","showtype":0,"total_ct":"1","pubdate_at":"1512566631","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/hao/10004.html"},{"id":"12","content":"《DNF》剑魂现版本百科全书 你想要的这里都有！(2)分页评论有bug吗 没有吧","aid":"10263","arcurl":"http://ol.3dmgame.com/gl/10263.html","title":"《DNF》剑魂现版本百科全书 你想要的这里都有！","showtype":0,"total_ct":"3","pubdate_at":"1512566182","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/gl/10263.html"},{"id":"11","content":"《DNF》剑魂现版本百科全书 你想要的这里都有！(2)分页评论有bug吗 没有吧 《DNF》剑魂现版本百科全书","aid":"10263","arcurl":"http://ol.3dmgame.com/gl/10263.html","title":"《DNF》剑魂现版本百科全书 你想要的这里都有！","showtype":0,"total_ct":"3","pubdate_at":"1512566106","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/gl/10263.html"},{"id":"10","content":"《DNF》剑魂现版本百科全书 你想要的这里都有！(2)分页评论有bug吗 没有吧","aid":"10263","arcurl":"http://ol.3dmgame.com/gl/10263.html","title":"《DNF》剑魂现版本百科全书 你想要的这里都有！","showtype":0,"total_ct":"3","pubdate_at":"1512565059","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/gl/10263.html"},{"id":"9","content":"至今，DNF已是一款有九年历史的老游戏了，游戏玩法基本不再变化，每天上线无非就是做着各种各样的任务活动。尽管如此，DNF仍保持着它的热度，这正是源于一种情怀。当知道DNF要出动画的时候，很多玩家的内心都是无比激动的。","aid":"10057","arcurl":"http://ol.3dmgame.com/news/201712/10057.html","title":"《DNF》正史是否更吸引人？《阿拉德：宿命之门》第二季","showtype":1,"total_ct":"2","pubdate_at":"1512562513","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/news/201712/10057.html"},{"id":"8","content":"至今，DNF已是一款有九年历史的老游戏了，游戏玩法基本不再变化，每天上线无非就是做着各种各样的任务活动。尽管如此，DNF仍保持着它的热度，这正是源于一种情怀。当知道DNF要出动画的时候，很多玩家的内心都是无比激动的。","aid":"10057","arcurl":"http://ol.3dmgame.com/news/201712/10057.html","title":"《DNF》正史是否更吸引人？《阿拉德：宿命之门》第二季","showtype":1,"total_ct":"2","pubdate_at":"1512561529","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/news/201712/10057.html"},{"id":"7","content":"《冒险岛2》年度诚意版本 - 新副本及新装备篇222222","aid":"10051","arcurl":"http://ol.3dmgame.com/news/201712/10051.html","title":"《冒险岛2》年度诚意版本 - 新副本及新装备篇","showtype":1,"total_ct":"7","pubdate_at":"1512358269","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/news/201712/10051.html"}]}
	 */

	public static class DataBean {
		/**
		 * total : 15
		 * list : [{"id":"16","content":"《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末","aid":"10066","arcurl":"http://ol.3dmgame.com/news/201712/10066.html","title":"《DNF》板砖适合解决问题  魔盒正确的打开方式","showtype":1,"total_ct":"0","pubdate_at":"1512638676","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/news/201712/10066.html"},{"id":"15","content":"《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末《地下城与勇士》DNF助手双旦签到福利狂送 引爆年末","aid":3,"arcurl":"http://ol.3dmgame.com/dnf","title":"地下城与勇士","showtype":0,"total_ct":"1","pubdate_at":"1512638637","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/dnf"},{"id":"14","content":"《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换《英雄联盟》12月8日免费英雄更换","aid":"10068","arcurl":"http://ol.3dmgame.com/news/201712/10068.html","title":"《英雄联盟》12月8日免费英雄更换","showtype":1,"total_ct":"0","pubdate_at":"1512633145","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/news/201712/10068.html"},{"id":"13","content":"镇魔曲 媒体专属礼包镇魔曲 媒体专属礼包镇魔曲 媒体专属礼包镇魔曲 媒体专属礼包镇魔曲 媒体专属礼包镇魔曲 媒体专属礼包镇魔曲 媒体专属礼包镇魔曲 媒体专属礼包","aid":"10004","arcurl":"http://ol.3dmgame.com/hao/10004.html","title":"《镇魔曲》 媒体专属礼包","showtype":0,"total_ct":"1","pubdate_at":"1512566631","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/hao/10004.html"},{"id":"12","content":"《DNF》剑魂现版本百科全书 你想要的这里都有！(2)分页评论有bug吗 没有吧","aid":"10263","arcurl":"http://ol.3dmgame.com/gl/10263.html","title":"《DNF》剑魂现版本百科全书 你想要的这里都有！","showtype":0,"total_ct":"3","pubdate_at":"1512566182","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/gl/10263.html"},{"id":"11","content":"《DNF》剑魂现版本百科全书 你想要的这里都有！(2)分页评论有bug吗 没有吧 《DNF》剑魂现版本百科全书","aid":"10263","arcurl":"http://ol.3dmgame.com/gl/10263.html","title":"《DNF》剑魂现版本百科全书 你想要的这里都有！","showtype":0,"total_ct":"3","pubdate_at":"1512566106","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/gl/10263.html"},{"id":"10","content":"《DNF》剑魂现版本百科全书 你想要的这里都有！(2)分页评论有bug吗 没有吧","aid":"10263","arcurl":"http://ol.3dmgame.com/gl/10263.html","title":"《DNF》剑魂现版本百科全书 你想要的这里都有！","showtype":0,"total_ct":"3","pubdate_at":"1512565059","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/gl/10263.html"},{"id":"9","content":"至今，DNF已是一款有九年历史的老游戏了，游戏玩法基本不再变化，每天上线无非就是做着各种各样的任务活动。尽管如此，DNF仍保持着它的热度，这正是源于一种情怀。当知道DNF要出动画的时候，很多玩家的内心都是无比激动的。","aid":"10057","arcurl":"http://ol.3dmgame.com/news/201712/10057.html","title":"《DNF》正史是否更吸引人？《阿拉德：宿命之门》第二季","showtype":1,"total_ct":"2","pubdate_at":"1512562513","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/news/201712/10057.html"},{"id":"8","content":"至今，DNF已是一款有九年历史的老游戏了，游戏玩法基本不再变化，每天上线无非就是做着各种各样的任务活动。尽管如此，DNF仍保持着它的热度，这正是源于一种情怀。当知道DNF要出动画的时候，很多玩家的内心都是无比激动的。","aid":"10057","arcurl":"http://ol.3dmgame.com/news/201712/10057.html","title":"《DNF》正史是否更吸引人？《阿拉德：宿命之门》第二季","showtype":1,"total_ct":"2","pubdate_at":"1512561529","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/news/201712/10057.html"},{"id":"7","content":"《冒险岛2》年度诚意版本 - 新副本及新装备篇222222","aid":"10051","arcurl":"http://ol.3dmgame.com/news/201712/10051.html","title":"《冒险岛2》年度诚意版本 - 新副本及新装备篇","showtype":1,"total_ct":"7","pubdate_at":"1512358269","has_reply":0,"webviewurl":"http://ol.3dmgame.com/app/news/201712/10051.html"}]
		 */

		private int total;
		private List<MyCommentBean> list;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public List<MyCommentBean> getList() {
			return list;
		}

		public void setList(List<MyCommentBean> list) {
			this.list = list;
		}
	}
}
