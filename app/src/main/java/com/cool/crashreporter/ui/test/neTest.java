package com.cool.crashreporter.ui.test;

import com.cool.crashreporter.ui.callback.CrashCaptureDoor;

public class neTest {
    public void showTest1() {
        try {
            showTest3();
        } catch (Exception e) {
            CrashCaptureDoor.instance().uploadThreadError("", e);
        }
    }

    public void showTest2() {
        showTest3();
    }

    public void showTest3() {

    }
}
