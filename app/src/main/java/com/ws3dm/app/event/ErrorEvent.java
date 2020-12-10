package com.ws3dm.app.event;

public class ErrorEvent {

    public int code;
    public String message;

    public ErrorEvent(int c, String m) {
        code = c;
        message = m;
    }

    public String toString() {
        return "code: " + code + ", message: " + message;
    }

}
