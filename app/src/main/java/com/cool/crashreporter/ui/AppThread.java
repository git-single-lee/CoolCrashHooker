package com.cool.crashreporter.ui;

public class AppThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10 ; ++i) {
            if (i == 5) {
                throw new RuntimeException("AppThread error");
            }
        }
    }
}