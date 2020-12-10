package com.ws3dm.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describution :分类测试bean
 * 
 * Author : DKjuan
 * 
 * Date : 2017/9/25 15:08
 **/
public class CategoryBean implements Serializable {

    private int img;
    private int typeid;//分类ID
    private String title;
    private List<AvatarBean> label;

    public CategoryBean() {
    }

    public CategoryBean(String title, ArrayList<AvatarBean> labels) {
        this.title = title;
        this.label = labels;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AvatarBean> getLabel() {
        return label;
    }

    public void setLabel(List<AvatarBean> label) {
        this.label = label;
    }
}
