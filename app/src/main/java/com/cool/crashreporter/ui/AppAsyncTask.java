package com.cool.crashreporter.ui;

import android.os.AsyncTask;

public class AppAsyncTask extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... strings) {
        String name = "1";
        for (int i = 0; i < 10 ; ++i) {
            if (i == 5) {
                throw new RuntimeException("AppAsyncTask error");
            }
        }
        return name;
    }
}
