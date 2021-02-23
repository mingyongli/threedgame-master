package com.ws3dm.app.bean;

public class UpLoadImageBean {

    /**
     * code : 1
     * data : {"attachid":5266588,"attachurl":"http://att.3dmgame.com/att/forum/202102/22/135438bnnvrs82txi52foz.jpg"}
     * msg : 成功
     */

    private int code;
    /**
     * attachid : 5266588
     * attachurl : http://att.3dmgame.com/att/forum/202102/22/135438bnnvrs82txi52foz.jpg
     */

    private DataDTO data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataDTO {
        private int attachid;
        private String attachurl;

        public int getAttachid() {
            return attachid;
        }

        public void setAttachid(int attachid) {
            this.attachid = attachid;
        }

        public String getAttachurl() {
            return attachurl;
        }

        public void setAttachurl(String attachurl) {
            this.attachurl = attachurl;
        }
    }
}
