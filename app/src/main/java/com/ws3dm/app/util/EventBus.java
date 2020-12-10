package com.ws3dm.app.util;

import com.hwangjr.rxbus.Bus;

public final class EventBus {

    private static Bus sBus;

    public static synchronized Bus getDefault() {
        if (sBus == null) {
            sBus = new Bus();
        }
        return sBus;
    }
}