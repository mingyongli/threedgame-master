package com.ws3dm.app.mvvm.bean;

public class AdexposureBean {

    /**
     * code : 245
     * data : {"num":0,"unittype":"meizu16th Plus","uuid":"3179d128-75f2-4a4c-a4b9-acf3fbd0200b","http":0}
     * msg : 用户已访问过
     */

    private int code;
    /**
     * num : 0
     * unittype : meizu16th Plus
     * uuid : 3179d128-75f2-4a4c-a4b9-acf3fbd0200b
     * http : 0
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
        private int num;
        private String unittype;
        private String uuid;
        private int http;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getUnittype() {
            return unittype;
        }

        public void setUnittype(String unittype) {
            this.unittype = unittype;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public int getHttp() {
            return http;
        }

        public void setHttp(int http) {
            this.http = http;
        }
    }
}
