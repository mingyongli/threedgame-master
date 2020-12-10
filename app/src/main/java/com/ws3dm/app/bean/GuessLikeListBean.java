package com.ws3dm.app.bean;

import java.util.List;

public class GuessLikeListBean {
    /**
     * code : 1
     * data : {"list":[{"aid":2,"title":"每日囧图轻松一刻","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/20200918/1600411937_606156.jpg","pubdate_at":1527670307},{"aid":1,"title":"PS5主机情报汇总","litpic":"https://img.3dmgame.com/uploads/images/thumbnews/20200918/1600411776_414924.jpg","pubdate_at":1533130656}]}
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
         * aid : 2
         * title : 每日囧图轻松一刻
         * litpic : https://img.3dmgame.com/uploads/images/thumbnews/20200918/1600411937_606156.jpg
         * pubdate_at : 1527670307
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private int aid;
            private String title;
            private String litpic;
            private int pubdate_at;

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

            public int getPubdate_at() {
                return pubdate_at;
            }

            public void setPubdate_at(int pubdate_at) {
                this.pubdate_at = pubdate_at;
            }
        }
    }
}
