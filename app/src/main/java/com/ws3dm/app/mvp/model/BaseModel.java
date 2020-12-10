package com.ws3dm.app.mvp.model;

import java.io.Serializable;

public class BaseModel<T> implements Serializable {

    private int    ec;
    private String message;
    private T      res;

    public int getEc() {
        return ec;
    }

    public void setEc(int ec) {
        this.ec = ec;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getRes() {
        return res;
    }

    public void setRes(T res) {
        this.res = res;
    }
}
