/**
  * Copyright 2020 bejson.com 
  */
package com.ws3dm.app.bean;
import java.util.List;

/**
 * Auto-generated: 2020-09-22 14:22:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class HomeTabs {

    private int code;
    private List<HomeTabsData> data;
    private String msg;
    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    public void setData(List<HomeTabsData> data) {
         this.data = data;
     }
     public List<HomeTabsData> getData() {
         return data;
     }

    public void setMsg(String msg) {
         this.msg = msg;
     }
     public String getMsg() {
         return msg;
     }

}