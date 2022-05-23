package com.cool.crashreporter.ui;

import android.os.AsyncTask;

import com.cool.crashcapture_core.core.CrashCaptureDoor;

import java.util.ArrayList;
import java.util.List;

public class AppAsyncTask3 extends AsyncTask<String, Integer, List<String>> {

    @Override
    protected List<String> doInBackground(String... strings) {
        String name = "1";
        for (int i = 0; i < 10 ; ++i) {
            if (i == 5) {
                throw new RuntimeException("AppAsyncTask error");
            }
        }
        return new ArrayList<String>();
    }
}
