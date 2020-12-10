package com.ws3dm.app.mvp.model.RespBean;

import com.ws3dm.app.bean.OriginalBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/21  12:06
 */
public class OrignewlevalRespBean extends BaseRespBean<OrignewlevalRespBean.DataBean>{

	public static class DataBean {
		/**
		 * list : [{"aid":3742096,"arcurl":"https://www.3dmgame.com/score/3742096.html","title":"《德军总部：新血脉》评测：双胞胎姐妹的血腥寻父之路","litpic":"https://img.3dmgame.com/uploads/images/thumbpcfirst/2019/0809/1565319243504.jpg","showtype":6,"total_ct":15,"pubdate_at":1565319355,"score":5.8,"webviewurl":"https://m.3dmgame.com/webview/score/3742096.html","user":{"id":38,"nickname":"水古月","avatarstr":"https://work.3dmgame.com/uploads/images/users/38.jpg"},"type":"评测"},{"aid":28101,"arcurl":"https://shouyou.3dmgame.com/original/28101.html","title":"《王牌战士》评测：动漫画风下的硬核射击之魂","litpic":"https://shouyou.3dmgame.com/uploadimg/thumb/2019/0806/1565080230928.jpg","showtype":6,"total_ct":0,"pubdate_at":1565080922,"score":8.5,"webviewurl":"https://app.3dmgame.com/webview/original/28101.html","user":{"id":271,"nickname":"鹿野","avatarstr":"https://work.3dmgame.com/uploads/images/users/20190320/1553053237_246434.jpg"},"type":"评测"},{"aid":28100,"arcurl":"https://shouyou.3dmgame.com/original/28100.html","title":"《剑网3：指尖江湖》评测：星辰剑履 歧路风尘 少年江湖路","litpic":"https://shouyou.3dmgame.com/uploadimg/thumb/2019/0806/1565078465735.png","showtype":6,"total_ct":0,"pubdate_at":1565078523,"score":9,"webviewurl":"https://app.3dmgame.com/webview/original/28100.html","user":{"id":196,"nickname":"小咸鱼籽","avatarstr":"https://work.3dmgame.com/uploads/images/users/20181114/1542158506_481701.jpg"},"type":"评测"},{"aid":28099,"arcurl":"https://shouyou.3dmgame.com/original/28099.html","title":"《大话西游》龙族版本评测：云山龙绘 点睛则活","litpic":"https://shouyou.3dmgame.com/uploadimg/thumb/2019/0805/1564987605182.png","showtype":6,"total_ct":0,"pubdate_at":1564987677,"score":8.5,"webviewurl":"https://app.3dmgame.com/webview/original/28099.html","user":{"id":317,"nickname":"太空棕熊","avatarstr":"https://work.3dmgame.com/uploads/images/users/20190718/1563416259_430799.jpg"},"type":"评测"},{"aid":28096,"arcurl":"https://shouyou.3dmgame.com/original/28096.html","title":"《和平精英》评测：好马也要配好鞍","litpic":"https://shouyou.3dmgame.com/uploadimg/thumb/2019/0802/1564745525980531.jpg","showtype":6,"total_ct":20,"pubdate_at":1564745777,"score":8,"webviewurl":"https://app.3dmgame.com/webview/original/28096.html","user":{"id":267,"nickname":"廉颇","avatarstr":"https://work.3dmgame.com/uploads/images/users/20190312/1552375246_601821.jpg"},"type":"评测"},{"aid":28095,"arcurl":"https://shouyou.3dmgame.com/original/28095.html","title":"《天涯明月刀》手游评测：此是天涯最佳处，江湖何处不飞花？","litpic":"https://shouyou.3dmgame.com/uploadimg/thumb/2019/0801/1564649195359238.jpg","showtype":6,"total_ct":31,"pubdate_at":1564649542,"score":8.8,"webviewurl":"https://app.3dmgame.com/webview/original/28095.html","user":{"id":271,"nickname":"鹿野","avatarstr":"https://work.3dmgame.com/uploads/images/users/20190320/1553053237_246434.jpg"},"type":"评测"},{"aid":28094,"arcurl":"https://shouyou.3dmgame.com/original/28094.html","title":"《王牌战争》评测：末日求生十五天","litpic":"https://shouyou.3dmgame.com/uploadimg/thumb/2019/0801/1564625944932326.jpg","showtype":6,"total_ct":0,"pubdate_at":1564625994,"score":8.5,"webviewurl":"https://app.3dmgame.com/webview/original/28094.html","user":{"id":317,"nickname":"太空棕熊","avatarstr":"https://work.3dmgame.com/uploads/images/users/20190718/1563416259_430799.jpg"},"type":"评测"},{"aid":3742094,"arcurl":"https://www.3dmgame.com/score/3742094.html","title":"《神器冒险外传DX》评测：在这里重温童年的快乐","litpic":"https://img.3dmgame.com/uploads/images/thumbpcfirst/20190731/1564563795_985496.jpg","showtype":6,"total_ct":0,"pubdate_at":1564565756,"score":8,"webviewurl":"https://m.3dmgame.com/webview/score/3742094.html","user":{"id":38,"nickname":"水古月","avatarstr":"https://work.3dmgame.com/uploads/images/users/38.jpg"},"type":"评测"},{"aid":3742093,"arcurl":"https://www.3dmgame.com/score/3742093.html","title":"《猎源》评测：火候欠佳，分量十足","litpic":"https://img.3dmgame.com/uploads/images/thumbpcfirst/2019/0729/1564392606514.jpg","showtype":6,"total_ct":3,"pubdate_at":1564400786,"score":7,"webviewurl":"https://m.3dmgame.com/webview/score/3742093.html","user":{"id":284,"nickname":"木大木大木大","avatarstr":"https://work.3dmgame.com/uploads/images/users/20190709/1562659096_921611.jpg"},"type":"评测"}]
		 * total : 912
		 * big_eval : {"aid":3742097,"arcurl":"https://www.3dmgame.com/score/3742097.html","title":"《纽扣兄弟》评测：一款扎实有趣的小品游戏","litpic":"https://img.3dmgame.com/uploads/images/thumbpcfirst/20190813/1565693320_142626.jpg","showtype":6,"total_ct":0,"pubdate_at":1565691027,"score":8,"webviewurl":"https://m.3dmgame.com/webview/score/3742097.html","user":{"id":322,"nickname":"空集","avatarstr":"https://work.3dmgame.com/uploads/images/users/20190814/1565764472_905941.jpg"},"type":"评测"}
		 */

		private int total;
		private OriginalBean big_eval;
		private List<OriginalBean> list;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public OriginalBean getBig_eval() {
			return big_eval;
		}

		public void setBig_eval(OriginalBean big_eval) {
			this.big_eval = big_eval;
		}

		public List<OriginalBean> getList() {
			return list;
		}

		public void setList(List<OriginalBean> list) {
			this.list = list;
		}
	}
}
