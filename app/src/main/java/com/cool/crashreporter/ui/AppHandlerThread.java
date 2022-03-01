package com.cool.crashreporter.ui;

import android.os.HandlerThread;

public class AppHandlerThread extends HandlerThread {
    public AppHandlerThread(String name) {
        super(name);
    }

    public AppHandlerThread(String name, int priority) {
        super(name, priority);
    }
}
