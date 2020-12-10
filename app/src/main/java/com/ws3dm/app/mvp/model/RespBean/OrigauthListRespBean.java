package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.AuthorTeamBean;
import com.ws3dm.app.bean.OriginalBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class OrigauthListRespBean extends BaseRespBean<OrigauthListRespBean.DataBean>{

	public static class DataBean {
		/**
		 * total : 11
		 * user : {"nickname":"liugaotian","desc":"Biang！","avatarstr":"https://work.3dmgame.com/uploads/images/users/200.jpg","firstpic":"https://img.3dmgame.com/uploads/images/thumbcolumnfirst/20180628/1530180950_331567_1.jpg"}
		 * list : [{"aid":3741392,"arcurl":"https://www.3dmgame.com/original/3741392.html","title":"《天命奇御》先行试玩：探索江湖的乐趣","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/2018/0801/1533106834351.png","pubdate_at":1533106980,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3741392.html","total_ct":19},{"aid":3733939,"arcurl":"https://www.3dmgame.com/original/3733939.html","title":"《二之国2幽灵国度》：猫耳少年的温馨复国之旅","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20180716/1531750362_383074.png","pubdate_at":1527064326,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3733939.html","total_ct":0},{"aid":3719851,"arcurl":"https://www.3dmgame.com/original/3719851.html","title":"《魔兽争霸3》新补丁，和我的电竞追忆","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20180601/1527862341_655229.png","pubdate_at":1519893978,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3719851.html","total_ct":0},{"aid":3716042,"arcurl":"https://www.3dmgame.com/original/3716042.html","title":"《蔚蓝Celeste》：超硬核黑暗童话系2D平台跳跃杰作","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20180601/1527862339_671797.png","pubdate_at":1517551170,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3716042.html","total_ct":0},{"aid":3715160,"arcurl":"https://www.3dmgame.com/original/3715160.html","title":"从上古之战到军团再临 探究伊利丹如何被暴雪洗白","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20180601/1527862338_899108.png","pubdate_at":1517208337,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3715160.html","total_ct":0},{"aid":3708018,"arcurl":"https://www.3dmgame.com/original/3708018.html","title":"3DM测评《画中世界》值得被\u201c过度解读\u201d的作品","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20180601/1527862335_171757.png","pubdate_at":1513839845,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3708018.html","total_ct":0},{"aid":3707571,"arcurl":"https://www.3dmgame.com/original/3707571.html","title":"《终结者2：审判日》连载四：猫捉老鼠 双排惨案","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20180601/1527862335_341217.png","pubdate_at":1513678731,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3707571.html","total_ct":0},{"aid":3707236,"arcurl":"https://www.3dmgame.com/original/3707236.html","title":"《终结者2：审判日》连载三：分道扬镳，无缘吃鸡","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20180601/1527862334_557493.png","pubdate_at":1513565816,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3707236.html","total_ct":0},{"aid":3637243,"arcurl":"https://www.3dmgame.com/original/3637243.html","title":"烟雾散去 暗影无踪 漫谈《幽灵行动》的前世今生","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20180601/1527862323_846189.png","pubdate_at":1488767497,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3637243.html","total_ct":0},{"aid":3635642,"arcurl":"https://www.3dmgame.com/original/3635642.html","title":"PS4独占3A大作《地平线：零之黎明》凌晨正式解锁","litpic":"https://img.3dmgame.com/uploads/images/thumborigfirst/20180601/1527862323_382584.png","pubdate_at":1488211329,"showtype":7,"webviewurl":"https://m.3dmgame.com/webview/original/3635642.html","total_ct":0}]
		 */

		private int total;
		private AuthorTeamBean user;
		private List<OriginalBean> list;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public AuthorTeamBean getUser() {
			return user;
		}

		public void setUser(AuthorTeamBean user) {
			this.user = user;
		}

		public List<OriginalBean> getList() {
			return list;
		}

		public void setList(List<OriginalBean> list) {
			this.list = list;
		}
	}
}
