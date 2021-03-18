package com.ws3dm.app.mvvm.event;

public class Message {
    public final String message;

    public static Message getInstance(String message) {
        return new Message(message);
    }

    private Message(String message) {
        this.message = message;
    }
}
