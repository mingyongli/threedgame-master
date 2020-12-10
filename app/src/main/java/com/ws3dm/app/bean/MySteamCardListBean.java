package com.ws3dm.app.bean;

import java.util.List;

public class MySteamCardListBean {

    /**
     * code : 1
     * data : {"list":[{"appid":38400,"litpic":"http://media.steampowered.com/steamcommunity/public/images/apps/38400/ab93e9e42e069b83926c8554de635142455aa271.jpg","title":"辐射","gamehour":"0.0","recenthour":"0.0","achieve_percent":0,"aid":269415,"arcurl":"https://www.3dmgame.com/games/fallout/","showtype":3,"psn_prodid":0},{"appid":780800,"litpic":"http://media.steampowered.com/steamcommunity/public/images/apps/780800/52d68b4b74da9f4f6cd94ecbd99a0cc051f4bab1.jpg","title":"Ball laB","gamehour":"0.0","recenthour":"0.0","achieve_percent":0,"aid":0,"arcurl":"","showtype":0,"psn_prodid":0},{"appid":3590,"litpic":"http://media.steampowered.com/steamcommunity/public/images/apps/3590/85bfe66e921d89b034a3a253fb648f1a930e034a.jpg","title":"Plants vs. Zombies: Game of the Year","gamehour":"0.0","recenthour":"0.0","achieve_percent":0,"aid":0,"arcurl":"","showtype":0,"psn_prodid":0},{"appid":231430,"litpic":"http://media.steampowered.com/steamcommunity/public/images/apps/231430/1be017e709b9b0638e82b399cc97de4667f44a1e.jpg","title":"英雄连2","gamehour":"0.0","recenthour":"0.0","achieve_percent":0,"aid":2287357,"arcurl":"https://www.3dmgame.com/games/cyohs2/","showtype":3,"psn_prodid":0},{"appid":230410,"litpic":"http://media.steampowered.com/steamcommunity/public/images/apps/230410/3785f3576cdef5cf20a8b815bdf867154ccbe7d5.jpg","title":"Warframe","gamehour":"0.0","recenthour":"0.0","achieve_percent":0,"aid":0,"arcurl":"","showtype":0,"psn_prodid":0},{"appid":458250,"litpic":"http://media.steampowered.com/steamcommunity/public/images/apps/458250/a654cb3753037cb9a9b16e7e85aa8ffffdb156ff.jpg","title":"CS:GO Player Profiles: karrigan - Astralis","gamehour":"0.0","recenthour":"0.0","achieve_percent":0,"aid":0,"arcurl":"","showtype":0,"psn_prodid":0},{"appid":242700,"litpic":"http://media.steampowered.com/steamcommunity/public/images/apps/242700/405bc57977d5e6fb112765e63fffcb110dbcd751.jpg","title":"Injustice: Gods Among Us Ultimate Edition","gamehour":"0.0","recenthour":"0.0","achieve_percent":0,"aid":0,"arcurl":"","showtype":0,"psn_prodid":0},{"appid":458260,"litpic":"http://media.steampowered.com/steamcommunity/public/images/apps/458260/f47d138ea2c2a24716ea3df6a828fd984a6e43d6.jpg","title":"CS:GO Player Profiles: pashaBiceps - Virtus.Pro","gamehour":"0.0","recenthour":"0.0","achieve_percent":0,"aid":0,"arcurl":"","showtype":0,"psn_prodid":0},{"appid":769560,"litpic":"http://media.steampowered.com/steamcommunity/public/images/apps/769560/874758d41419c5b5a3e711644f1f73c0183f299f.jpg","title":"Night of the Full Moon","gamehour":"0.0","recenthour":"0.0","achieve_percent":0,"aid":0,"arcurl":"","showtype":0,"psn_prodid":0},{"appid":666140,"litpic":"http://media.steampowered.com/steamcommunity/public/images/apps/666140/de848befba4a64c96057679403e0c07f2410a9ce.jpg","title":"波西亚时光","gamehour":"0.0","recenthour":"0.0","achieve_percent":5,"aid":3667403,"arcurl":"https://www.3dmgame.com/games/mytap/","showtype":3,"psn_prodid":0}]}
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
         * appid : 38400
         * litpic : http://media.steampowered.com/steamcommunity/public/images/apps/38400/ab93e9e42e069b83926c8554de635142455aa271.jpg
         * title : 辐射
         * gamehour : 0.0
         * recenthour : 0.0
         * achieve_percent : 0
         * aid : 269415
         * arcurl : https://www.3dmgame.com/games/fallout/
         * showtype : 3
         * psn_prodid : 0
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private int appid;
            private String litpic;
            private String title;
            private String gamehour;
            private String recenthour;
            private int achieve_percent;
            private int aid;
            private String arcurl;
            private int showtype;
            private int psn_prodid;

            public int getAppid() {
                return appid;
            }

            public void setAppid(int appid) {
                this.appid = appid;
            }

            public String getLitpic() {
                return litpic;
            }

            public void setLitpic(String litpic) {
                this.litpic = litpic;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getGamehour() {
                return gamehour;
            }

            public void setGamehour(String gamehour) {
                this.gamehour = gamehour;
            }

            public String getRecenthour() {
                return recenthour;
            }

            public void setRecenthour(String recenthour) {
                this.recenthour = recenthour;
            }

            public int getAchieve_percent() {
                return achieve_percent;
            }

            public void setAchieve_percent(int achieve_percent) {
                this.achieve_percent = achieve_percent;
            }

            public int getAid() {
                return aid;
            }

            public void setAid(int aid) {
                this.aid = aid;
            }

            public String getArcurl() {
                return arcurl;
            }

            public void setArcurl(String arcurl) {
                this.arcurl = arcurl;
            }

            public int getShowtype() {
                return showtype;
            }

            public void setShowtype(int showtype) {
                this.showtype = showtype;
            }

            public int getPsn_prodid() {
                return psn_prodid;
            }

            public void setPsn_prodid(int psn_prodid) {
                this.psn_prodid = psn_prodid;
            }
        }
    }
}
