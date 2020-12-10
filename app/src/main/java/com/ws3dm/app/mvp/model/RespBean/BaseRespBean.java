package com.ws3dm.app.mvp.model.RespBean;

import java.io.Serializable;

public class BaseRespBean<T> implements Serializable {

    private int    code;
    private String msg;
    private String message;
    private T      data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean hasData() {
        return (data != null);
    }
}
