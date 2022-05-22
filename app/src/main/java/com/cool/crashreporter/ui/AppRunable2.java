package com.cool.crashreporter.ui;

import com.cool.crashcapture_core.core.CrashCaptureDoor;

public class AppRunable2 implements Runnable {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; ++i) {
                if (i == 5) {
                    throw new RuntimeException("AppRunable error");
                }
            }
        } catch (Exception e) {
            CrashCaptureDoor.getInstance().uploadThreadError("", e);
        }
    }
}
