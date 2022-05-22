package com.cool.crashreporter.ui;

import android.os.AsyncTask;

import com.cool.crashcapture_core.core.CrashCaptureDoor;

public class AppAsyncTask2 extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... strings) {
        try {
            String name = "1";
            for (int i = 0; i < 10 ; ++i) {
                if (i == 5) {
                    throw new RuntimeException("AppAsyncTask error");
                }
            }
            return name;
        } catch (Exception e) {
            CrashCaptureDoor.getInstance().uploadThreadError("", e);
            return null;
        }
    }


}
