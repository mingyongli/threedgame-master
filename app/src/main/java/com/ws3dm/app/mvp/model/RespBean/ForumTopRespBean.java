package com.ws3dm.app.mvp.model.RespBean;

import com.google.gson.annotations.SerializedName;
import com.ws3dm.app.bean.ForumDetailBean;

import java.util.List;

/**
 * Author : DKjuan: 论坛置顶列表
 * <p>
 * Date :  2017/8/21  9:52
 */
public class ForumTopRespBean extends BaseRespBean<ForumTopRespBean.DataBean>{

	/**
	 * data : {"list":[{"fid":"18","tid":"5723178","arcurl":"http://bbs.3dmgame.com/thread-5723178-1.html","title":"【3DMGAME新闻中心】招聘值班编辑","pubdate_at":"1521706238","replies":"5","views":"4575","type":"公告","user":{"uid":"1056597","nickname":"NT","avatarstr":"http://user.3dmgame.com/data/avatar/001/05/65/97_avatar_small.jpg"}},{"fid":"256","tid":"5687603","arcurl":"http://bbs.3dmgame.com/thread-5687603-1.html","title":"3DMGAME\u2014\u20142018年热门单机游戏下载及汉化发布总索引[3月24日更新游戏","pubdate_at":"1514762757","replies":"169","views":"318751","type":"","user":{"uid":"883901","nickname":"星云散落","avatarstr":"http://user.3dmgame.com/data/avatar/000/88/39/01_avatar_small.jpg"}},{"fid":"1704","tid":"5697549","arcurl":"http://bbs.3dmgame.com/thread-5697549-1.html","title":"3DM汉化组直播间终极无敌死亡回旋论坛置顶传送门","pubdate_at":"1517294380","replies":"703","views":"62874","type":"","user":{"uid":"2473480","nickname":"☆暗夜未央★","avatarstr":"http://user.3dmgame.com/data/avatar/002/47/34/80_avatar_small.jpg"}},{"fid":"1704","tid":"5723546","arcurl":"http://bbs.3dmgame.com/thread-5723546-1.html","title":"3DMGAME\u2014\u2014《天龙八部手游》喜迎慕容新门派论坛活动","pubdate_at":"1521789687","replies":"534","views":"85289","type":"","user":{"uid":"1067184","nickname":"3DMGAME","avatarstr":"http://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg"}},{"fid":"1704","tid":"5724863","arcurl":"http://bbs.3dmgame.com/thread-5724863-1.html","title":"3DMGAME\u2014\u2014《神舞幻想》Steam国区解锁 晒评论就赢京东卡大奖","pubdate_at":"1522031830","replies":"265","views":"309844","type":"","user":{"uid":"1067184","nickname":"3DMGAME","avatarstr":"http://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg"}},{"fid":"1704","tid":"5722652","arcurl":"http://bbs.3dmgame.com/thread-5722652-1.html","title":"3DMGAME\u2014\u2014御龙在天页游3月23日不删档晒图爬楼赢QB和礼包","pubdate_at":"1521598485","replies":"334","views":"20836","type":"","user":{"uid":"1067184","nickname":"3DMGAME","avatarstr":"http://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg"}},{"fid":"1704","tid":"5724873","arcurl":"http://bbs.3dmgame.com/thread-5724873-1.html","title":"3DMGAME\u2014\u2014预约《轮回诀》公测赢取京东卡","pubdate_at":"1522033568","replies":"604","views":"297741","type":"","user":{"uid":"1067184","nickname":"3DMGAME","avatarstr":"http://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg"}},{"fid":"1704","tid":"5720133","arcurl":"http://bbs.3dmgame.com/thread-5720133-1.html","title":"3DMGAME\u2014\u2014《为谁而炼金》公测助力壕礼送不停！","pubdate_at":"1521164205","replies":"754","views":"542045","type":"","user":{"uid":"1067184","nickname":"3DMGAME","avatarstr":"http://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg"}},{"fid":"1704","tid":"5726361","arcurl":"http://bbs.3dmgame.com/thread-5726361-1.html","title":"3DMGAME\u2014\u2014王者荣耀为魂斗罗归来\u201c燃魂战队联赛\u201d送上史诗级大礼","pubdate_at":"1522292792","replies":"38","views":"2179","type":"","user":{"uid":"1067184","nickname":"3DMGAME","avatarstr":"http://user.3dmgame.com/data/avatar/001/06/71/84_avatar_small.jpg"}}]}
	 */
	public static class DataBean {
		private List<ForumDetailBean> list;

		public List<ForumDetailBean> getList() {
			return list;
		}

		public void setList(List<ForumDetailBean> list) {
			this.list = list;
		}
	}
}
