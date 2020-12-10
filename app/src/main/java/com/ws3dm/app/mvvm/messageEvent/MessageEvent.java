package com.ws3dm.app.mvvm.messageEvent;

public class MessageEvent {
    private final String message;
    private final String data;

    public MessageEvent(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public static MessageEvent getInstance(String message, String data) {
        return new MessageEvent(message, data);
    }


}
