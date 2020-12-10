package com.ws3dm.app;


public class Constant {

    public static final int VIEW_TYPE_LIST_H_3 = 1;
    public static final int VIEW_TYPE_LIST_V_3 = 2;
    public static final int VIEW_TYPE_PAGES    = 3;
    public static final int VIEW_TYPE_GRID_4   = 4;
    public static final int VIEW_TYPE_PIC      = 5;
    public static final int FIX_VIEW           = 100;

    public static final int SELECT_SOME = 0;
    public static final int SELECT_NONE = 1;
    public static final int SELECT_ALL  = 2;

    //    API = "http://m.3dmgame.com/y3wap/" + API + ".php";     //线上
//            API = "http://101.81.97.0:880/y3wap/" + API + ".php";  //测试更新2
//            API = "http://192.168.1.155/y3wap/" + API + ".php";  //测试更新
	private static String mToken;
//    public static final String NEWHOST = "https://mytest.3dmgame.com/app/";
    public static final String NEWHOST = "https://my.3dmgame.com/app/";
    public static final String NEWHOSTV4 = "https://my.3dmgame.com/app4/";
    public static final String HOST = "http://m.3dmgame.com/y3wap/";
    public static final String BBS = "http://bbs.3dmgame.com/api/3dmapp/";

	public void setToken(String token) {
		this.mToken = token;
	}

	public static String getToken() {
		return mToken;
	}

	public class Event {

        public static final String NETWORK = "network";
        public static final String DEFAULT_FAILURE = "default_failure";
        
        public static final String APP_UPDATE = "app_update";
        
        /*新闻相关*/
        public static final String NEWS_COMMENT_LIST_SUCCESS = "news_comment_list_success";//评论列表
        public static final String NEWS_COMMENT_LISTR_FAILURE = "news_comment_list_failure";
        public static final String NEWS_ABOUT_SUCCESS = "news_about_success";//获取内容页相关内容数据
        public static final String NEWS_ABOUT_FAILURE = "news_about_failure";
        public static final String NEWS_CHANNEL_DETAIL = "news_channel_detail";//新闻中分类列表页详情
        public static final String NEWS_VIDEO = "news_video";//新闻中分类列表页详情
        public static final String NEWS_WEB = "news_web";//新闻中顶部网页
        public static final String NEWS_HOTCOMMENT_LIST_SUCCESS = "news_hotcomment_list_success";//热门评论列表
        public static final String NEWS_HOTCOMMENT_LIST_FAILURE = "news_hotcomment_list_failure";
        public static final String NEWS_GLPAGE2 = "news_glpage2";//用于获取攻略封面页数据
        
        /*游戏相关*/
        public static final String GAME_HOT_SUCCESS = "game_hot_success";//获取单机首页页面数据(热门)
        public static final String GAME_HOT_FAILURE = "game_hot_failure";
        public static final String GAME_CATEGORY_SUCCESS = "game_category_success";//获取单机分类
        public static final String GAME_CATEGORY_FAILURE = "game_category_failure";
        public static final String GAME_CHOICE_SUCCESS = "game_choice_success";//获取分类列表详情
        public static final String GAME_CHOICE_FAILURE = "game_choice_failure";
        public static final String GAME_DETAIL_SUCCESS = "game_detail_success";//获取游戏详情
        public static final String GAME_DETAIL_FAILURE = "game_detail_failure";
        public static final String GAME_DJHOME_SUCCESS = "game_djhome_success";//单机封面页
        public static final String GAME_DJHOME_FAILURE = "game_djhome_failure";
        public static final String GAME_DJITEM_SUCCESS = "game_djitem_success";//用于获取单机分类、语言、类型筛选项
        public static final String GAME_DJITEM_FAILURE = "game_djitem_failure";
        public static final String GAME_OLITEM_SUCCESS = "game_olitem_success";//获取网游类型、标签、运营商筛选项
        public static final String GAME_OLITEM_FAILURE = "game_olitem_failure";
        public static final String GAME_SYITEM_SUCCESS = "game_syitem_success";//获取手游类型、标签、运营商筛选项
        public static final String GAME_SYITEM_FAILURE = "game_syitem_failure";
        public static final String GAME_DETAIL = "game_detail";
        
        /*手游相关*/
        public static final String MG_GAME_SUCCESS = "mg_game_success";//手游游戏首页页面数据
        public static final String MG_GAME_FAILURE = "mg_game_failure";
        public static final String MG_SOFT_SUCCESS = "mg_soft_success";//手游软件首页页面数据
        public static final String MG_SOFT_FAILURE = "mg_soft_failure";
        public static final String MG_CHOICE_SUCCESS = "mg_choice_success";//手游分类筛选页面数据
        public static final String MG_CHOICE_FAILURE = "mg_choice_failure";

        /*论坛相关*/
        public static final String FORUM_BANNER_SUCCESS = "forum_banner_success";
        public static final String FORUM_BANNER_FAILURE = "forum_banner_failure";
        public static final String FORUM_RANK_SUCCESS = "forum_rank_success";//论坛排行
        public static final String FORUM_RANK_FAILURE = "forum_rank_failure";
        public static final String FORUM_GID_SUCCESS = "forum_gid_success";//论坛板块分区列表
        public static final String FORUM_GID_FAILURE = "forum_gid_failure";
        public static final String FORUM_LIST_SUCCESS = "forum_list_success";//论坛 子板块分区列表
        public static final String FORUM_LIST_FAILURE = "forum_list_failure";
        public static final String FORUM_LISTHREAD_SUCCESS = "forum_list_thread_success";//论坛 主题列表
        public static final String FORUM_LISTHREAD_FAILURE = "forum_list_thread_failure";
        public static final String FORUM_TIDTYPE_SUCCESS = "forum_tidtpye_success";////获取板块类型标签列表
        public static final String FORUM_TIDTYPE_FAILURE = "forum_tidtpye_failure";
        public static final String FORUM_TIDPOST_SUCCESS = "forum_tidpost_success";////获取回帖列表
        public static final String FORUM_TIDPOST_FAILURE = "forum_tidpost_failure";
        public static final String FORUM_GETFIRSTPOST_SUCCESS = "forum_getfirstpost_success";////当前作者修改自己的主题帖子
        public static final String FORUM_GETFIRSTPOST_FAILURE = "forum_getfirstpost_failure";

        /*用户相关*/
        public static final String USER_LOGIN_SUCCESS = "user_login_success";//用户登录
        public static final String USER_LOGIN_FAILURE = "user_login_failure";
        public static final String USER_BINDBYID_SUCCESS = "user_bindphonebyid_success";//老用户绑定手机号
        public static final String USER_BINDBYID_FAILURE = "user_bindphonebyid_failure";
        public static final String USER_BINDPHONE_SUCCESS = "user_bindphone_success";//第三方登陆绑定，非老用户
        public static final String USER_BINDPHONE_FAILURE = "user_bindphone_failure";
        public static final String USER_REGIST_SUCCESS = "user_regist_success";//用户注册
        public static final String USER_REGIST_FAILURE = "user_regist_failure";
        public static final String USER_MODIFYPASS_SUCCESS = "user_modifypass_success";//用户修改密码
        public static final String USER_MODIFYPASS_FAILURE = "user_modifypass_failure";
        public static final String USER_RESET_SUCCESS = "user_reset_success";//用户重置密码
        public static final String USER_RESET_FAILURE = "user_reset_failure";
        public static final String USER_GETSETTING_SUCCESS = "user_getsetting_success";//获取设置页信息
        public static final String USER_GETSETTING_FAILURE = "user_getsetting_failure";
        public static final String USER_MODIFYSETTING_SUCCESS = "user_modifysetting_success";//修改设置页信息
        public static final String USER_MODIFYSETTING_FAILURE = "user_modifysetting_failure";
        public static final String USER_GETMYCOMMENT_SUCCESS = "user_getmyconment_success";//获取我的评论
        public static final String USER_GETMYCOMMENT_FAILURE = "user_getmycomment_failure";
        public static final String MY_NEWS_COLLECT_SUCCESS = "my_news_connect_success";//我的新闻收藏
        public static final String MY_NEWS_COLLECT_FAILURE = "my_news_connect_failure";
        public static final String MY_GAME_COLLECT_SUCCESS = "my_game_connect_success";//我的游戏收藏
        public static final String MY_GAME_COLLECT_FAILURE = "my_game_connect_failure";
        public static final String MY_GIFT_COLLECT_SUCCESS = "my_game_connect_success";//我的礼包收藏
        public static final String MY_GIFT_COLLECT_FAILURE = "my_game_connect_failure";
        public static final String GET_VERSION_SUCCESS = "get_version_success";//获取版本号
        public static final String GET_VERSION_FAILURE = "get_version_failure";
        
        //原创相关
        public static final String ORIGIN_HOME_SUCCESS = "original_home_success";//原创首页数据
        public static final String ORIGIN_HOME_FAILURE = "original_home_failure";
        public static final String ORIGIN_LIST_SUCCESS = "original_list_success";//用于原创列表数据
        public static final String ORIGIN_LIST_FAILURE = "original_list_failure";
        public static final String ORIGIN_LABEL_SUCCESS = "original_label_success";//原创节目标签配置数据
        public static final String ORIGIN_LABEL_FAILURE = "original_label_failure";
        public static final String ORIGIN_PROGRAM_SUCCESS = "original_program_success";//用于原创节目列表数据
        public static final String ORIGIN_PROGRAM_FAILURE = "original_program_failure";
        public static final String ORIGIN_PROGRAMLABEL_SUCCESS = "original_programlabel_success";//用于原创节目标签列表数据
        public static final String ORIGIN_PROGRAMLABEL_FAILURE = "original_programlabel_failure";
        public static final String ORIGIN_NEWLEVAL_SUCCESS = "original_newleval_success";//用于获取新原创评测列表页面数据
        public static final String ORIGIN_NEWLEVAL_FAILURE = "original_orignewleval_failure";
        public static final String ORIGIN_COLLABEL_SUCCESS = "original_collabel_success";//用于原创专栏列表数据
        public static final String ORIGIN_COLLABEL_FAILURE = "original_collabel_failure";
        public static final String ORIGIN_NEWCOL_SUCCESS = "original_newcol_success";//专栏标签详情数据
        public static final String ORIGIN_NEWCOL_FAILURE = "original_newcol_failure";
        public static final String ORIGIN_AUTHOR_SUCCESS = "original_author_success";//我的团队
        public static final String ORIGIN_AUTHOR_FAILURE = "original_author_failure";
        public static final String ORIGIN_AUTHORLIST_SUCCESS = "original_authorlist_success";//入住作者作品列表数据
        public static final String ORIGIN_AUTHORLIST_FAILURE = "original_authorlist_failure";
        
        //友盟推送消息
        public static final String UMENG_PUSH_NEWS = "umeng_push_news";
        //添加到下载列表
        public static final String ADD_DOWNLIST = "add_downlist";
        //SteamPsn游戏平台
        public static final String RELOAD_STEAM_SUCCESS = "reload_steam_success";
    }

    public class Notify {

        // common
        public static final int LOAD_START   = -1000;
        public static final int LOAD_SUCCESS = -1001;
        public static final int LOAD_FAILURE = -1002;
        public static final int LOAD_NO_MORE = -1003;
        public static final int LOAD_NO_DATA = -1004;
        public static final int APP_VERSION  = -1005;

        //新闻 100+
        public static final int RESULT_NEWS_COMMENT_LIST = 101;//评论列表
        public static final int RESULT_NEWS_DETAIL = 102;
        public static final int RESULT_NESS_VIDEO = 103;
        public static final int RESULT_NESS_WEB = 104;
        public static final int RESULT_NEWS_ABOUT = 105;//获取内容页相关内容数据
        public static final int RESULT_NEWS_HOTCOMMENT_LIST = 106;//热门评论列表
        public static final int RESULT_NEWS_NEWS_GLPAGE2 = 107;//用于获取攻略封面页数据

        //game 游戏库 200+
        public static final int RESULT_GAME_HOT = 201;//游戏热门
        public static final int RESULT_GAME_DETAIL = 202;
        public static final int RESULT_GAME_CATEGORY = 203;//获取单机分类;
        public static final int RESULT_GAME_CHOICE = 204;//获取分类列表详情
        public static final int RESULT_GAME_DJHOME = 205;//获取新版单机首页面
        public static final int RESULT_GAME_DJITEM = 206;//获取新版单机首页面
        public static final int RESULT_GAME_OLITEM = 207;//获取网游类型、标签、运营商筛选项
        public static final int RESULT_GAME_SYITEM = 208;//获取新版手游首页面

        //论坛 forum  300+
        public static final int RESULT_FORUM_BANNER = 301;//论坛标签列表
        public static final int RESULT_FORUM_MESSAGE = 302;//我的消息-论坛消息
        public static final int RESULT_FORUM_RANK = 303;//论坛排行
        public static final int RESULT_FORUM_GID = 304;//论坛分区列表
        public static final int RESULT_FORUM_LIST = 305;//论坛 子版块分区列表
        public static final int RESULT_FORUMTHREAD_LIST = 306;//论坛 子版块分区列表
        public static final int RESULT_FORUMTIDTYPE_LIST = 307;//获取板块类型标签列表
        public static final int RESULT_FORUMTIDPOST_LIST = 308;//获取回帖列表
        public static final int RESULT_GETFIRSTPOST = 309;//当前作者修改自己的主题帖子

        //用户    400+
        public static final int RESULT_USER_LOGIN = 401;//用户登录
        public static final int RESULT_USER_CYAN_LOGIN = 402;//畅言登陆
        public static final int RESULT_USER_REGISTER = 403;//用户注册
        public static final int RESULT_RESET_PASS = 406;//重置密码
        public static final int RESULT_MODIFY_PASS = 407;//修改密码
        public static final int RESULT_GETSETTING = 408;//获取设置页信息
        public static final int RESULT_MODITYSETTING = 409;//修改设置页信息
        public static final int RESULT_GETMYCOMMENT = 410;//获取我的评论
        public static final int RESULT_MY_NEWS_COLLECT = 411;//获取我的新闻收藏
        public static final int RESULT_MY_GAME_COLLECT = 412;//获取我的游戏收藏
        public static final int RESULT_MY_GIFT_COLLECT = 413;//获取我的礼包收藏
        public static final int RESULT_USER_BINDBYID = 414;//老用户绑定手机号
        public static final int RESULT_USER_BINDPHONE = 415;//第三方登陆绑定，非老用户
        public static final int RESULT_VERSION = 416;//获取版本号

        //手游 500+
        public static final int RESULT_MG_GAME = 501;//手游 游戏
        public static final int RESULT_MG_SOFT = 502;//手游 软件
        public static final int RESULT_MG_CHOICE = 503;//手游分类筛选

        //原创相关
        public static final int ORIGIN_HOME = 601;//原创首页数据
        public static final int ORIGIN_LIST = 602;//用于原创列表数据
        public static final int ORIGIN_LABEL = 603;//原创节目标签配置数据
        public static final int ORIGIN_PROGRAM = 604;//用于原创节目列表数据
        public static final int ORIGIN_PROGRAMLABEL = 605;//用于原创节目标签列表数据
        public static final int ORIGIN_NEWLEVAL = 606;//用于获取新原创评测列表页面数据
        public static final int ORIGIN_NEWCOL = 607;//用于原创专栏列表数据
        public static final int ORIGIN_COLLABEL = 610;//专栏标签详情数据
        public static final int ORIGIN_AUTHOR = 608;//原创作者列表数据
        public static final int ORIGIN_AUTHORLIST = 609;//入住作者作品列表数据


    }
}
