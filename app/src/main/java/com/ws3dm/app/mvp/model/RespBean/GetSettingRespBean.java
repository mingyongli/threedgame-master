package com.ws3dm.app.mvp.model.RespBean;

import com.google.gson.annotations.SerializedName;
import com.ws3dm.app.bean.AvatarBean;

import java.util.List;

/**
 * Author : DKjuan:
 * <p>
 * Date :  2017/8/17  16:22
 */
public class GetSettingRespBean extends BaseRespBean<GetSettingRespBean.DataBean>{

	/**
	 * data : {"uid":12973244,"username":"3dm_12973244","nickname":"啦咯啦咯拒绝啦","iscannick":0,"mobile":"13816190598","avatarstr":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520233126_327057.jpg","avatar":20,"gender":1,"birthday":"2018-07-01","personalized":"策略,对战,模拟,日系,中世纪","region_province":2,"region":823,"isqq":1,"issina":1,"iswechat":1,"integral":5,"title":"3DMer","title_level":0,"level2":0,"level3":0,"level4":0,"up_nick":0,"app_login":1,"hobbyconfig":"动作,射击,独立,开放世界,休闲,解谜,恋爱,塔防,音乐,回合制,生存,平衡,潜行,联机,对战,中世纪,模拟,篮球,足球,即时战略,策略,经营,竞速,街机,角色扮演,格斗,二次元,日系,赛车,建造,改编,丧尸,空战,朋克,仙侠,沙盒,战棋","avatarlist":[{"id":2,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217758_739449.jpg"},{"id":3,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217768_628945.jpg"},{"id":4,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217777_798542.jpg"},{"id":5,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217789_633223.jpg"},{"id":6,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217801_102761.jpg"},{"id":7,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217812_196357.jpg"},{"id":8,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217823_981397.jpg"},{"id":9,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217838_811451.jpg"},{"id":10,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217848_877411.jpg"},{"id":11,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217860_121300.jpg"},{"id":12,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217870_320082.jpg"},{"id":13,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217880_524517.jpg"},{"id":16,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520232975_796322.jpg"},{"id":17,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520233039_189446.jpg"},{"id":18,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520233075_589995.jpg"},{"id":19,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520233114_736551.jpg"},{"id":20,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520233126_327057.jpg"},{"id":21,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520233197_668598.jpg"},{"id":22,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520233211_831056.jpg"},{"id":23,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243170_447394.jpg"},{"id":24,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243185_750447.jpg"},{"id":25,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243225_999792.jpg"},{"id":26,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243257_602628.jpg"},{"id":27,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243286_729563.jpg"},{"id":28,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243709_576602.jpg"},{"id":29,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243941_196384.jpg"},{"id":30,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243983_993755.jpg"},{"id":31,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520244014_478921.jpg"},{"id":32,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520244031_395636.jpg"},{"id":33,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520244088_787903.jpg"},{"id":34,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520244109_532368.jpg"},{"id":35,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313453_452960.jpg"},{"id":36,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313478_485877.jpg"},{"id":37,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313496_167955.jpg"},{"id":38,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313521_287095.jpg"},{"id":39,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313553_940646.jpg"},{"id":40,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313584_755575.jpg"},{"id":41,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313622_100092.jpg"},{"id":42,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313635_174476.jpg"},{"id":43,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313675_919765.jpg"},{"id":45,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313721_150268.jpg"},{"id":46,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313775_760501.jpg"},{"id":47,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313795_484139.jpg"},{"id":48,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313805_563535.jpg"},{"id":51,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314167_887988.jpg"},{"id":52,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314217_712766.jpg"},{"id":53,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314228_841670.jpg"},{"id":54,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314238_406247.jpg"},{"id":55,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314275_716276.jpg"},{"id":56,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314288_368028.jpg"},{"id":57,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314302_964323.jpg"},{"id":58,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314314_810660.jpg"},{"id":59,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314362_136742.jpg"},{"id":60,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314372_405865.jpg"},{"id":63,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314474_428372.jpg"},{"id":64,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314484_925851.jpg"},{"id":65,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314513_661303.jpg"},{"id":66,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314523_314420.jpg"},{"id":67,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314534_616643.jpg"},{"id":68,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314545_468328.png"},{"id":69,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314649_754042.jpg"},{"id":70,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314660_212796.jpg"},{"id":72,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314694_309381.jpg"},{"id":73,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316309_816609.jpg"},{"id":74,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316319_693646.jpg"},{"id":75,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316329_100416.jpg"},{"id":76,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316339_956533.jpg"},{"id":77,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316347_801838.jpg"},{"id":78,"url":"https//my.3dmgame.com/uploads/images/avatar/20180306/1520316356_132588.jpg"},{"id":79,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316365_599823.jpg"},{"id":80,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316374_493945.jpg"},{"id":81,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316383_934619.jpg"},{"id":82,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316396_877853.jpg"},{"id":83,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316599_196401.jpg"},{"id":84,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316765_100438.jpg"},{"id":85,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316776_675417.jpg"},{"id":86,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316883_748792.jpg"},{"id":87,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316892_400120.jpg"},{"id":88,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316903_817314.jpg"},{"id":89,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520317139_876085.jpg"},{"id":90,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520317392_598634.jpg"},{"id":91,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520317609_864490.jpg"},{"id":92,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520317619_224261.jpg"},{"id":93,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520317631_793028.jpg"},{"id":94,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520317650_856771.jpg"},{"id":95,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520317814_911375.jpg"},{"id":96,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319710_970909.jpg"},{"id":97,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319732_470025.jpg"},{"id":98,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319773_888866.jpg"},{"id":99,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319784_672092.jpg"},{"id":100,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319798_202705.jpg"},{"id":101,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319812_951426.jpg"},{"id":102,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319821_505185.jpg"},{"id":103,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319837_213834.jpg"},{"id":104,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319960_578028.jpg"},{"id":105,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320023_713177.jpg"},{"id":106,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320191_104815.jpg"},{"id":107,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320201_458827.jpg"},{"id":108,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320211_921442.jpg"},{"id":109,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320228_714457.jpg"},{"id":110,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320330_327743.jpg"},{"id":111,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320357_511747.jpg"},{"id":112,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320368_467151.jpg"},{"id":113,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320379_981968.jpg"},{"id":114,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320388_980888.jpg"},{"id":115,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320440_599906.jpg"},{"id":116,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320466_926933.jpg"},{"id":117,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320496_936333.jpg"},{"id":118,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320507_871522.jpg"},{"id":119,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320519_187432.jpg"},{"id":120,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320532_500406.jpg"},{"id":121,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320543_203350.jpg"},{"id":122,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320558_227129.jpg"},{"id":123,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320600_491566.jpg"},{"id":124,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320625_403108.jpg"},{"id":125,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320657_546330.jpg"},{"id":126,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320675_741853.jpg"},{"id":127,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321420_178834.jpg"},{"id":128,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321429_817421.jpg"},{"id":129,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321553_645070.jpg"},{"id":130,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321563_192969.jpg"},{"id":131,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321573_975824.jpg"},{"id":132,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321605_970385.jpg"},{"id":133,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321671_302460.jpg"},{"id":134,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321682_102571.jpg"},{"id":135,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321697_716354.jpg"},{"id":136,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321811_988178.jpg"},{"id":137,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321820_120392.jpg"},{"id":138,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321831_554277.jpg"},{"id":139,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321844_234519.jpg"},{"id":140,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321854_892170.jpg"}],"levels":{"level1":["3DMer","初出茅庐","菜鸟玩家"],"level2":["PC党","微软铁粉","索尼拥趸","任天堂死忠","资深云玩家","剁手一族","优惠券爱好者","安卓控"],"level3":["首席杠精","女装大佬","钢铁直男","舌战群儒","键盘狂舞","千手观音","老司机","死肥宅","小仙女","人工智能","粗鄙刁民"],"level4":["业界达人","游戏圣者","机神降临","秋名山车神","守护小岩喵"]},"upnickintegral":5}
	 */

	public static class DataBean {
		/**
		 * uid : 12973244
		 * username : 3dm_12973244
		 * nickname : 啦咯啦咯拒绝啦
		 * iscannick : 0
		 * mobile : 13816190598
		 * avatarstr : https://my.3dmgame.com/uploads/images/avatar/20180305/1520233126_327057.jpg
		 * avatar : 20
		 * gender : 1
		 * birthday : 2018-07-01
		 * personalized : 策略,对战,模拟,日系,中世纪
		 * region_province : 2
		 * region : 823
		 * isqq : 1
		 * issina : 1
		 * iswechat : 1
		 * integral : 5
		 * title : 3DMer
		 * title_level : 0
		 * level2 : 0
		 * level3 : 0
		 * level4 : 0
		 * up_nick : 0
		 * app_login : 1
		 * hobbyconfig : 动作,射击,独立,开放世界,休闲,解谜,恋爱,塔防,音乐,回合制,生存,平衡,潜行,联机,对战,中世纪,模拟,篮球,足球,即时战略,策略,经营,竞速,街机,角色扮演,格斗,二次元,日系,赛车,建造,改编,丧尸,空战,朋克,仙侠,沙盒,战棋
		 * avatarlist : [{"id":2,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217758_739449.jpg"},{"id":3,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217768_628945.jpg"},{"id":4,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217777_798542.jpg"},{"id":5,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217789_633223.jpg"},{"id":6,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217801_102761.jpg"},{"id":7,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217812_196357.jpg"},{"id":8,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217823_981397.jpg"},{"id":9,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217838_811451.jpg"},{"id":10,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217848_877411.jpg"},{"id":11,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217860_121300.jpg"},{"id":12,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217870_320082.jpg"},{"id":13,"url":"https://my.3dmgame.com/uploads/images/avatar/20171202/1512217880_524517.jpg"},{"id":16,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520232975_796322.jpg"},{"id":17,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520233039_189446.jpg"},{"id":18,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520233075_589995.jpg"},{"id":19,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520233114_736551.jpg"},{"id":20,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520233126_327057.jpg"},{"id":21,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520233197_668598.jpg"},{"id":22,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520233211_831056.jpg"},{"id":23,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243170_447394.jpg"},{"id":24,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243185_750447.jpg"},{"id":25,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243225_999792.jpg"},{"id":26,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243257_602628.jpg"},{"id":27,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243286_729563.jpg"},{"id":28,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243709_576602.jpg"},{"id":29,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243941_196384.jpg"},{"id":30,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520243983_993755.jpg"},{"id":31,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520244014_478921.jpg"},{"id":32,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520244031_395636.jpg"},{"id":33,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520244088_787903.jpg"},{"id":34,"url":"https://my.3dmgame.com/uploads/images/avatar/20180305/1520244109_532368.jpg"},{"id":35,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313453_452960.jpg"},{"id":36,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313478_485877.jpg"},{"id":37,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313496_167955.jpg"},{"id":38,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313521_287095.jpg"},{"id":39,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313553_940646.jpg"},{"id":40,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313584_755575.jpg"},{"id":41,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313622_100092.jpg"},{"id":42,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313635_174476.jpg"},{"id":43,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313675_919765.jpg"},{"id":45,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313721_150268.jpg"},{"id":46,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313775_760501.jpg"},{"id":47,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313795_484139.jpg"},{"id":48,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520313805_563535.jpg"},{"id":51,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314167_887988.jpg"},{"id":52,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314217_712766.jpg"},{"id":53,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314228_841670.jpg"},{"id":54,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314238_406247.jpg"},{"id":55,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314275_716276.jpg"},{"id":56,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314288_368028.jpg"},{"id":57,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314302_964323.jpg"},{"id":58,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314314_810660.jpg"},{"id":59,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314362_136742.jpg"},{"id":60,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314372_405865.jpg"},{"id":63,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314474_428372.jpg"},{"id":64,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314484_925851.jpg"},{"id":65,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314513_661303.jpg"},{"id":66,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314523_314420.jpg"},{"id":67,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314534_616643.jpg"},{"id":68,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314545_468328.png"},{"id":69,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314649_754042.jpg"},{"id":70,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314660_212796.jpg"},{"id":72,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520314694_309381.jpg"},{"id":73,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316309_816609.jpg"},{"id":74,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316319_693646.jpg"},{"id":75,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316329_100416.jpg"},{"id":76,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316339_956533.jpg"},{"id":77,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316347_801838.jpg"},{"id":78,"url":"https//my.3dmgame.com/uploads/images/avatar/20180306/1520316356_132588.jpg"},{"id":79,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316365_599823.jpg"},{"id":80,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316374_493945.jpg"},{"id":81,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316383_934619.jpg"},{"id":82,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316396_877853.jpg"},{"id":83,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316599_196401.jpg"},{"id":84,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316765_100438.jpg"},{"id":85,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316776_675417.jpg"},{"id":86,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316883_748792.jpg"},{"id":87,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316892_400120.jpg"},{"id":88,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520316903_817314.jpg"},{"id":89,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520317139_876085.jpg"},{"id":90,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520317392_598634.jpg"},{"id":91,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520317609_864490.jpg"},{"id":92,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520317619_224261.jpg"},{"id":93,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520317631_793028.jpg"},{"id":94,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520317650_856771.jpg"},{"id":95,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520317814_911375.jpg"},{"id":96,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319710_970909.jpg"},{"id":97,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319732_470025.jpg"},{"id":98,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319773_888866.jpg"},{"id":99,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319784_672092.jpg"},{"id":100,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319798_202705.jpg"},{"id":101,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319812_951426.jpg"},{"id":102,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319821_505185.jpg"},{"id":103,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319837_213834.jpg"},{"id":104,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520319960_578028.jpg"},{"id":105,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320023_713177.jpg"},{"id":106,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320191_104815.jpg"},{"id":107,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320201_458827.jpg"},{"id":108,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320211_921442.jpg"},{"id":109,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320228_714457.jpg"},{"id":110,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320330_327743.jpg"},{"id":111,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320357_511747.jpg"},{"id":112,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320368_467151.jpg"},{"id":113,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320379_981968.jpg"},{"id":114,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320388_980888.jpg"},{"id":115,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320440_599906.jpg"},{"id":116,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320466_926933.jpg"},{"id":117,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320496_936333.jpg"},{"id":118,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320507_871522.jpg"},{"id":119,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320519_187432.jpg"},{"id":120,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320532_500406.jpg"},{"id":121,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320543_203350.jpg"},{"id":122,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320558_227129.jpg"},{"id":123,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320600_491566.jpg"},{"id":124,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320625_403108.jpg"},{"id":125,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320657_546330.jpg"},{"id":126,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520320675_741853.jpg"},{"id":127,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321420_178834.jpg"},{"id":128,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321429_817421.jpg"},{"id":129,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321553_645070.jpg"},{"id":130,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321563_192969.jpg"},{"id":131,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321573_975824.jpg"},{"id":132,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321605_970385.jpg"},{"id":133,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321671_302460.jpg"},{"id":134,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321682_102571.jpg"},{"id":135,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321697_716354.jpg"},{"id":136,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321811_988178.jpg"},{"id":137,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321820_120392.jpg"},{"id":138,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321831_554277.jpg"},{"id":139,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321844_234519.jpg"},{"id":140,"url":"https://my.3dmgame.com/uploads/images/avatar/20180306/1520321854_892170.jpg"}]
		 * levels : {"level1":["3DMer","初出茅庐","菜鸟玩家"],"level2":["PC党","微软铁粉","索尼拥趸","任天堂死忠","资深云玩家","剁手一族","优惠券爱好者","安卓控"],"level3":["首席杠精","女装大佬","钢铁直男","舌战群儒","键盘狂舞","千手观音","老司机","死肥宅","小仙女","人工智能","粗鄙刁民"],"level4":["业界达人","游戏圣者","机神降临","秋名山车神","守护小岩喵"]}
		 * upnickintegral : 5
		 */

		private int uid;
		private String username;
		private String nickname;
		private int iscannick;
		private String mobile;
		private String avatarstr;
		private int avatar;
		private int gender;
		private String birthday;
		private String personalized;
		private int region_province;
		private int region;
		private int region_area;
		private int isqq;
		private int issina;
		private int iswechat;
		private int integral;
		private String title;
		private int title_level;
		private int level2;
		private int level3;
		private int level4;
		private int up_nick;
		private int app_login;
		private String hobbyconfig;
		private LevelsBean levels;
		private int upnickintegral;
		private List<AvatarBean> avatarlist;

		public int getUid() {
			return uid;
		}

		public void setUid(int uid) {
			this.uid = uid;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public int getIscannick() {
			return iscannick;
		}

		public void setIscannick(int iscannick) {
			this.iscannick = iscannick;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getAvatarstr() {
			return avatarstr;
		}

		public void setAvatarstr(String avatarstr) {
			this.avatarstr = avatarstr;
		}

		public int getAvatar() {
			return avatar;
		}

		public void setAvatar(int avatar) {
			this.avatar = avatar;
		}

		public int getGender() {
			return gender;
		}

		public void setGender(int gender) {
			this.gender = gender;
		}

		public String getBirthday() {
			return birthday;
		}

		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}

		public String getPersonalized() {
			return personalized;
		}

		public void setPersonalized(String personalized) {
			this.personalized = personalized;
		}

		public int getRegion_province() {
			return region_province;
		}

		public void setRegion_province(int region_province) {
			this.region_province = region_province;
		}

		public int getRegion() {
			return region;
		}

		public void setRegion(int region) {
			this.region = region;
		}

		public int getRegion_area() {
			return region_area;
		}

		public void setRegion_area(int region_area) {
			this.region_area = region_area;
		}

		public int getIsqq() {
			return isqq;
		}

		public void setIsqq(int isqq) {
			this.isqq = isqq;
		}

		public int getIssina() {
			return issina;
		}

		public void setIssina(int issina) {
			this.issina = issina;
		}

		public int getIswechat() {
			return iswechat;
		}

		public void setIswechat(int iswechat) {
			this.iswechat = iswechat;
		}

		public int getIntegral() {
			return integral;
		}

		public void setIntegral(int integral) {
			this.integral = integral;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getTitle_level() {
			return title_level;
		}

		public void setTitle_level(int title_level) {
			this.title_level = title_level;
		}

		public int getLevel2() {
			return level2;
		}

		public void setLevel2(int level2) {
			this.level2 = level2;
		}

		public int getLevel3() {
			return level3;
		}

		public void setLevel3(int level3) {
			this.level3 = level3;
		}

		public int getLevel4() {
			return level4;
		}

		public void setLevel4(int level4) {
			this.level4 = level4;
		}

		public int getUp_nick() {
			return up_nick;
		}

		public void setUp_nick(int up_nick) {
			this.up_nick = up_nick;
		}

		public int getApp_login() {
			return app_login;
		}

		public void setApp_login(int app_login) {
			this.app_login = app_login;
		}

		public String getHobbyconfig() {
			return hobbyconfig;
		}

		public void setHobbyconfig(String hobbyconfig) {
			this.hobbyconfig = hobbyconfig;
		}

		public LevelsBean getLevels() {
			return levels;
		}

		public void setLevels(LevelsBean levels) {
			this.levels = levels;
		}

		public int getUpnickintegral() {
			return upnickintegral;
		}

		public void setUpnickintegral(int upnickintegral) {
			this.upnickintegral = upnickintegral;
		}

		public List<AvatarBean> getAvatarlist() {
			return avatarlist;
		}

		public void setAvatarlist(List<AvatarBean> avatarlist) {
			this.avatarlist = avatarlist;
		}

		public static class LevelsBean {
			private List<String> level1;
			private List<String> level2;
			private List<String> level3;
			private List<String> level4;

			public List<String> getLevel1() {
				return level1;
			}

			public void setLevel1(List<String> level1) {
				this.level1 = level1;
			}

			public List<String> getLevel2() {
				return level2;
			}

			public void setLevel2(List<String> level2) {
				this.level2 = level2;
			}

			public List<String> getLevel3() {
				return level3;
			}

			public void setLevel3(List<String> level3) {
				this.level3 = level3;
			}

			public List<String> getLevel4() {
				return level4;
			}

			public void setLevel4(List<String> level4) {
				this.level4 = level4;
			}
		}
	}
}
