package com.cool.crashcapture_core.core;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class CrashCaptureDoor {
    private CrashCapture crashCapture = null;

    private static class SingletonClassInstance {
        private static final CrashCaptureDoor instance = new CrashCaptureDoor();
    }

    public static CrashCaptureDoor getInstance() {
        return SingletonClassInstance.instance;
    }


    public void setListener(CrashCapture crashCapture) {
        this.crashCapture = crashCapture;
    }

    public void uploadThreadError(String key, Exception error) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        error.printStackTrace(printWriter);
        if (crashCapture != null) {
            crashCapture.crashCapture(stringWriter.toString());
        }
        Log.e("CrashCaptureDoor", stringWriter.toString());
    }

    public interface CrashCapture {
        public void crashCapture(String error);
    }
}
