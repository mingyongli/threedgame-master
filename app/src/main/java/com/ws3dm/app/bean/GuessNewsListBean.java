package com.ws3dm.app.bean;

import java.util.List;

public class GuessNewsListBean {

    /**
     * code : 1
     * data : {"list":[{"arcurl":"https://www.3dmgame.com/news/202011/3802859.html","aid":3802859,"title":"PS4玩家哀嚎！ 找PS5代领游戏的玩家遭大量封号","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/20201126/1606361651_760393.jpg","showtype":1,"pubdate_at":1606361758,"webviewurl":"https://m.3dmgame.com/webview/news/202011/3802859.html","is_video":0},{"arcurl":"https://www.3dmgame.com/news/202011/3802857.html","aid":3802857,"title":"英国屯3500台PS5黄牛公司赚了200万英镑 一台赚100英镑","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/20201126/1606358801_358515.jpg","showtype":1,"pubdate_at":1606359994,"webviewurl":"https://m.3dmgame.com/webview/news/202011/3802857.html","is_video":0},{"arcurl":"https://www.3dmgame.com/news/202011/3802826.html","aid":3802826,"title":"索尼：PS5首发是我们有史以来游戏主机首发最多的一次","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/2020/1125/1606310424496.jpg","showtype":1,"pubdate_at":1606310479,"webviewurl":"https://m.3dmgame.com/webview/news/202011/3802826.html","is_video":0},{"arcurl":"https://www.3dmgame.com/news/202011/3802818.html","aid":3802818,"title":"PS5主机再度发布系统更新 提升系统性能表现","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/20201125/1606300587_510941.jpg","showtype":1,"pubdate_at":1606300721,"webviewurl":"https://m.3dmgame.com/webview/news/202011/3802818.html","is_video":0},{"arcurl":"https://www.3dmgame.com/news/202011/3802808.html","aid":3802808,"title":"樱井政博谈对PS5印象 机体够大SSD略小总体满意","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/2020/1125/1606293005347.png","showtype":1,"pubdate_at":1606293009,"webviewurl":"https://m.3dmgame.com/webview/news/202011/3802808.html","is_video":0},{"arcurl":"https://www.3dmgame.com/news/202011/3802802.html","aid":3802802,"title":"外媒估算PS5首发当日全球销量210-250万台 近XSX/S两倍","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/20201125/1606288407_776165.jpg","showtype":1,"pubdate_at":1606290275,"webviewurl":"https://m.3dmgame.com/webview/news/202011/3802802.html","is_video":0},{"arcurl":"https://www.3dmgame.com/news/202011/3802742.html","aid":3802742,"title":"索尼PS电视广告在美国的曝光量是Xbox的3倍","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/2020/1124/1606224278251.png","showtype":1,"pubdate_at":1606224456,"webviewurl":"https://m.3dmgame.com/webview/news/202011/3802742.html","is_video":0},{"arcurl":"https://www.3dmgame.com/news/202011/3802706.html","aid":3802706,"title":"传索尼2021年下半年将推出廉价版主机PS5 lite","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/20201124/1606196367_247350.jpg","showtype":1,"pubdate_at":1606196688,"webviewurl":"https://m.3dmgame.com/webview/news/202011/3802706.html","is_video":0},{"arcurl":"https://www.3dmgame.com/news/202011/3802688.html","aid":3802688,"title":"PS5《刺客信条:英灵殿》支持键鼠操作 但并不完美","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/2020/1124/1606185472319.png","showtype":1,"pubdate_at":1606185623,"webviewurl":"https://m.3dmgame.com/webview/news/202011/3802688.html","is_video":0},{"arcurl":"https://www.3dmgame.com/news/202011/3802678.html","aid":3802678,"title":"新传闻：国行PS5将于2021年1月21日发售 锁服不锁区 3499元","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/20201124/1606180399_347380.jpg","showtype":1,"pubdate_at":1606182306,"webviewurl":"https://m.3dmgame.com/webview/news/202011/3802678.html","is_video":0}]}
     * msg : 成功
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * arcurl : https://www.3dmgame.com/news/202011/3802859.html
         * aid : 3802859
         * title : PS4玩家哀嚎！ 找PS5代领游戏的玩家遭大量封号
         * litpic : https://img.3dmgame.com/uploads/images/thumbnews/20201126/1606361651_760393.jpg
         * showtype : 1
         * pubdate_at : 1606361758
         * webviewurl : https://m.3dmgame.com/webview/news/202011/3802859.html
         * is_video : 0
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String arcurl;
            private int aid;
            private String title;
            private String litpic;
            private int showtype;
            private int pubdate_at;
            private String webviewurl;
            private int is_video;

            public String getArcurl() {
                return arcurl;
            }

            public void setArcurl(String arcurl) {
                this.arcurl = arcurl;
            }

            public int getAid() {
                return aid;
            }

            public void setAid(int aid) {
                this.aid = aid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLitpic() {
                return litpic;
            }

            public void setLitpic(String litpic) {
                this.litpic = litpic;
            }

            public int getShowtype() {
                return showtype;
            }

            public void setShowtype(int showtype) {
                this.showtype = showtype;
            }

            public int getPubdate_at() {
                return pubdate_at;
            }

            public void setPubdate_at(int pubdate_at) {
                this.pubdate_at = pubdate_at;
            }

            public String getWebviewurl() {
                return webviewurl;
            }

            public void setWebviewurl(String webviewurl) {
                this.webviewurl = webviewurl;
            }

            public int getIs_video() {
                return is_video;
            }

            public void setIs_video(int is_video) {
                this.is_video = is_video;
            }
        }
    }
}
