package com.ws3dm.app.mvp.model.ReqBean;

import java.io.Serializable;

public class AuthReqBean implements Serializable {

    /**
     * platform : android
     * version : v1.0.1
     * uuid : CF325F32-845C-46BC-B823-5A1B9CED191A
     * channel : string
     */

    private String platform;
    private String version;
    private String uuid;
    private String channel;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
