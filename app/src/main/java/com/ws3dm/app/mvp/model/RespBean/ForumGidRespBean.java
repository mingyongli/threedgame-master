package com.ws3dm.app.mvp.model.RespBean;

import com.google.gson.annotations.SerializedName;
import com.ws3dm.app.bean.ForumGidBean;

import java.util.List;

/**
 * Author : DKjuan: 论坛板块分区列表
 * <p>
 * Date :  2017/8/21  9:52
 */
public class ForumGidRespBean extends BaseRespBean<ForumGidRespBean.DataBean>{

	/**
	 * data : {"list":[{"gid":"437","title":"自营H5游戏","forumscount":16},{"gid":"1","title":"站务论坛","forumscount":19},{"gid":"1009","title":"资源发布","forumscount":10},{"gid":"3","title":"综合讨论","forumscount":8},{"gid":"441","title":"热门游戏","forumscount":35},{"gid":"502","title":"经典游戏","forumscount":139},{"gid":"438","title":"即将发行","forumscount":79},{"gid":"2082","title":"推荐手游","forumscount":10},{"gid":"1962","title":"热门手游","forumscount":13},{"gid":"2279","title":"独立游戏","forumscount":153},{"gid":"353","title":"角色扮演","forumscount":251},{"gid":"354","title":"动作游戏","forumscount":502},{"gid":"355","title":"射击游戏","forumscount":209},{"gid":"504","title":"即时战略","forumscount":52},{"gid":"357","title":"策略战棋","forumscount":151},{"gid":"387","title":"模拟经营","forumscount":71},{"gid":"360","title":"体育竞速","forumscount":102},{"gid":"229","title":"电玩专题","forumscount":81},{"gid":"542","title":"动漫专题","forumscount":4},{"gid":"201","title":"娱乐休闲","forumscount":8},{"gid":"478","title":"3DM工作室论坛","forumscount":3}]}
	 */
	public static class DataBean {
		private List<ForumGidBean> list;

		public List<ForumGidBean> getList() {
			return list;
		}

		public void setList(List<ForumGidBean> list) {
			this.list = list;
		}

	}
}
