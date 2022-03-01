package com.cool.crashreporter.ui;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.cool.crashreporter.ui.callback.CrashCaptureDoor;

import java.util.logging.Handler;

public class App extends Application implements CrashCaptureDoor.CrashCapture {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashCaptureDoor.instance().setListener(this);
    }

    @Override
    public void crashCapture(String error) {
        Log.d("CrashCapture", "error: " + error);

    }
}
