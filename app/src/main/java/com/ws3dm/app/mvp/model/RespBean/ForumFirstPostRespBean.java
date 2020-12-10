package com.ws3dm.app.mvp.model.RespBean;

import java.util.List;

/**
 * Author : DKjuan: 获取板块类型标签列表 bean
 * <p>
 * Date :  2017/8/18  14:57
 */
public class ForumFirstPostRespBean extends BaseRespBean<ForumFirstPostRespBean.DataBean>{

	public static class DataBean {
		/**
		 * fid : 2359
		 * tid : 5728660
		 * pid : 209909678
		 * typeid : 0
		 * title : 【测试数据】 这是修改的测试数据有变化
		 * content : 活动内容：获得大量夺岛卡和扫荡卡
		 8.消费有礼：
		 参加条件：活动期间内，消费钻石达到指定目标
		 活动内容：赠送大量、蓝色、紫色碎片
		 9.天天充值有礼：
		 参加条件：活动期间内充值天数达到置顶目标
		 活动内容：赠送大量士兵、碎片、钻石
		 10.幸运大转盘：
		 参加条件：每日3次免费转盘，超过后每次100钻石
		 活动内容：摇动转盘，获得随机士兵奖励，最高可获得航空母舰
		 11.疯狂翻翻乐：
		 参加条件：每日3次免费翻翻乐机会，超过后每次20钻石
		 活动内容：翻牌后可获得碎片或者士兵奖励


		 * attachimgs : [{"attachid":4464588,"attachurl":"http://att.3dmgame.com/att/forum201804/03/214714k30fddq0do3gmgvq.png"},{"attachid":4464593,"attachurl":"http://att.3dmgame.com/att/forum201804/03/215043dzmnyzpcymdkj83a.png"}]
		 */

		private int fid;
		private int tid;
		private int pid;
		private int typeid;
		private String title;
		private String content;
		private List<AttachimgsBean> attachimgs;

		public int getFid() {
			return fid;
		}

		public void setFid(int fid) {
			this.fid = fid;
		}

		public int getTid() {
			return tid;
		}

		public void setTid(int tid) {
			this.tid = tid;
		}

		public int getPid() {
			return pid;
		}

		public void setPid(int pid) {
			this.pid = pid;
		}

		public int getTypeid() {
			return typeid;
		}

		public void setTypeid(int typeid) {
			this.typeid = typeid;
		}

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

		public List<AttachimgsBean> getAttachimgs() {
			return attachimgs;
		}

		public void setAttachimgs(List<AttachimgsBean> attachimgs) {
			this.attachimgs = attachimgs;
		}

		public static class AttachimgsBean {
			/**
			 * attachid : 4464588
			 * attachurl : http://att.3dmgame.com/att/forum201804/03/214714k30fddq0do3gmgvq.png
			 */

			private int attachid;
			private String attachurl;

			public int getAttachid() {
				return attachid;
			}

			public void setAttachid(int attachid) {
				this.attachid = attachid;
			}

			public String getAttachurl() {
				return attachurl;
			}

			public void setAttachurl(String attachurl) {
				this.attachurl = attachurl;
			}
		}
	}
}
