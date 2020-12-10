package com.ws3dm.app.bean;

import java.io.Serializable;

/**
 * 目录item bean
 */

public class HistoryBean implements Serializable {

    public static final int ITEM_VIEW_TYPE_HEADER  = 0;
    public static final int ITEM_VIEW_TYPE_BODY = 1;

    public int type;//区分大标题小标题
    /**
     * arcurl : http://m.3dmgame.com/news/201709/3686714_app.html
     * title : 《实况足球2018》前场TOP5梅罗双骄 中场魔笛领衔 
     * senddate : 1天前
     * feedback : 0
     * id : 3686714
     * changyan_id : news_3686714
     * description : KONAMI放出了《实况足球2018》前场最强球员TOP 5名单，梅西和C罗当仁不让的成为了最高分的球员，两人以94分的分数并列第一。
     * lmfl : 游戏库
     * litpic : [["http://aimg.3dmgame.com/uploads/allimg/170912/382_170912093718_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170912/382_170912093705_1_app.jpg"],["http://aimg.3dmgame.com/uploads/allimg/170912/382_170912094010_1_app.jpg"]]
     * arttype : 2
     */
    public String arcurl;
    public String title;
    public String senddate;
    public String feedback;
    public String id;
    public String webviewurl;
    public String description;
    public String lmfl;
    public int arttype;
    public int typeid;
    public String cyId;
}
