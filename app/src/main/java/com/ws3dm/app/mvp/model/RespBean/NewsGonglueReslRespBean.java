package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.GonglueBean;

import java.util.List;

/**
 * Author : DKjuan:获取攻略专区攻略标签详情数据
 * <p>
 * Date :  2017/8/21  12:06
 */
public class NewsGonglueReslRespBean extends BaseRespBean<NewsGonglueReslRespBean.DataBean>{
	public static class DataBean {
		private List<GLbean> list;

		public List<GLbean> getList() {
			return list;
		}

		public void setList(List<GLbean> list) {
			this.list = list;
		}

		public static class GLbean {
			/**
			 * id : 2565
			 * name : 活动攻略
			 * total : 23
			 * list : [{"aid":149863,"title":"OF-EX6","arcurl":"https://shouyou.3dmgame.com/gl/149863.html","webviewurl":"https://app.3dmgame.com/webview/gl/149863.html","showtype":2},{"aid":149853,"title":"OF-EX5","arcurl":"https://shouyou.3dmgame.com/gl/149853.html","webviewurl":"https://app.3dmgame.com/webview/gl/149853.html","showtype":2},{"aid":149835,"title":"OF-EX6无箱子过法","arcurl":"https://shouyou.3dmgame.com/gl/149835.html","webviewurl":"https://app.3dmgame.com/webview/gl/149835.html","showtype":2},{"aid":149830,"title":"OF-EX4","arcurl":"https://shouyou.3dmgame.com/gl/149830.html","webviewurl":"https://app.3dmgame.com/webview/gl/149830.html","showtype":2},{"aid":149821,"title":"OF-EX3","arcurl":"https://shouyou.3dmgame.com/gl/149821.html","webviewurl":"https://app.3dmgame.com/webview/gl/149821.html","showtype":2},{"aid":149811,"title":"OF-EX2","arcurl":"https://shouyou.3dmgame.com/gl/149811.html","webviewurl":"https://app.3dmgame.com/webview/gl/149811.html","showtype":2},{"aid":149802,"title":"OF-EX1","arcurl":"https://shouyou.3dmgame.com/gl/149802.html","webviewurl":"https://app.3dmgame.com/webview/gl/149802.html","showtype":2},{"aid":149056,"title":"OF-F4","arcurl":"https://shouyou.3dmgame.com/gl/149056.html","webviewurl":"https://app.3dmgame.com/webview/gl/149056.html","showtype":2},{"aid":148890,"title":"OF-8","arcurl":"https://shouyou.3dmgame.com/gl/148890.html","webviewurl":"https://app.3dmgame.com/webview/gl/148890.html","showtype":2},{"aid":148849,"title":"OF-7","arcurl":"https://shouyou.3dmgame.com/gl/148849.html","webviewurl":"https://app.3dmgame.com/webview/gl/148849.html","showtype":2},{"aid":148814,"title":"OF-6","arcurl":"https://shouyou.3dmgame.com/gl/148814.html","webviewurl":"https://app.3dmgame.com/webview/gl/148814.html","showtype":2},{"aid":148792,"title":"火蓝之心规划黑曜石","arcurl":"https://shouyou.3dmgame.com/gl/148792.html","webviewurl":"https://app.3dmgame.com/webview/gl/148792.html","showtype":2},{"aid":148776,"title":"OF-5","arcurl":"https://shouyou.3dmgame.com/gl/148776.html","webviewurl":"https://app.3dmgame.com/webview/gl/148776.html","showtype":2},{"aid":148762,"title":"火蓝之心活动奖励","arcurl":"https://shouyou.3dmgame.com/gl/148762.html","webviewurl":"https://app.3dmgame.com/webview/gl/148762.html","showtype":2},{"aid":148749,"title":"OF-4","arcurl":"https://shouyou.3dmgame.com/gl/148749.html","webviewurl":"https://app.3dmgame.com/webview/gl/148749.html","showtype":2},{"aid":148730,"title":"OF-F3","arcurl":"https://shouyou.3dmgame.com/gl/148730.html","webviewurl":"https://app.3dmgame.com/webview/gl/148730.html","showtype":2},{"aid":148722,"title":"OF-3","arcurl":"https://shouyou.3dmgame.com/gl/148722.html","webviewurl":"https://app.3dmgame.com/webview/gl/148722.html","showtype":2},{"aid":148710,"title":"OF-F2","arcurl":"https://shouyou.3dmgame.com/gl/148710.html","webviewurl":"https://app.3dmgame.com/webview/gl/148710.html","showtype":2},{"aid":148700,"title":"OF-F1","arcurl":"https://shouyou.3dmgame.com/gl/148700.html","webviewurl":"https://app.3dmgame.com/webview/gl/148700.html","showtype":2},{"aid":148678,"title":"OF-2","arcurl":"https://shouyou.3dmgame.com/gl/148678.html","webviewurl":"https://app.3dmgame.com/webview/gl/148678.html","showtype":2}]
			 */

			private int id;
			private String name;
			private int total;
			private List<GonglueBean> list;

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public int getTotal() {
				return total;
			}

			public void setTotal(int total) {
				this.total = total;
			}

			public List<GonglueBean> getList() {
				return list;
			}

			public void setList(List<GonglueBean> list) {
				this.list = list;
			}
		}
	}
}
