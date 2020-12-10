package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Describution :地址bean
 * 
 * Author : DKjuan
 * 
 * Date : 2018/3/28 18:30
 **/

public class AddressBean implements Serializable {

    /**
     * id : 2
     * region_province : 7
     * region_city : 0
     * region_area : 0
     * address : 台湾 第10001楼
     * addr : 第10001楼
     * postal : 000000
     * concat : 收货人20
     * mobile : 18321005045
     * order : 1
     * is_default : 1
     * created_at : 1543666856
     * updated_at : 1543667145
     */

    private int id;
    private int region_province;
    private int region_city;
    private int region_area;
    private String region_str;
    private String address;
    private String addr;
    private String postal;
    private String title;
    private String steam_url;
    private String concat;
    private String mobile;
    private int order;
    private int is_default;
    private int created_at;
    private int updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRegion_province() {
        return region_province;
    }

    public void setRegion_province(int region_province) {
        this.region_province = region_province;
    }

    public int getRegion_city() {
        return region_city;
    }

    public void setRegion_city(int region_city) {
        this.region_city = region_city;
    }

    public int getRegion_area() {
        return region_area;
    }

    public void setRegion_area(int region_area) {
        this.region_area = region_area;
    }

    public String getRegion_str() {
        return region_str;
    }

    public void setRegion_str(String region_str) {
        this.region_str = region_str;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSteam_url() {
        return steam_url;
    }

    public void setSteam_url(String steam_url) {
        this.steam_url = steam_url;
    }

    public String getConcat() {
        return concat;
    }

    public void setConcat(String concat) {
        this.concat = concat;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }
}
