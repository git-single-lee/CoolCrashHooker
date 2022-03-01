package com.cool.crashreporter.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cool.crashreporter.R;

/**
 * author : coolqi.li
 * e-mail : coolqi.li@forever.com
 * date   : 2021/9/30-5:27 下午
 * desc   :
 * version: 1.0
 */
public class MainUi extends Activity {
    private TextView textservice = null;
    private TextView textservice1 = null;

    private TextView handlerThread = null;
    private TextView runable = null;
    private TextView thread = null;
    private TextView appAsyncTask = null;
    private AppHandlerThread appHandlerThread = new AppHandlerThread("test");
    private AppHandler appHandler = null;
    private AppAsyncTask mTask = null;
    private AppThread mThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        appHandlerThread.start();
        appHandler = new AppHandler(appHandlerThread.getLooper());
        handlerThread = findViewById(R.id.handlerThread);
        runable = findViewById(R.id.runable);
        thread = findViewById(R.id.thread);
        appAsyncTask = findViewById(R.id.appAsyncTask);

        handlerThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appHandler.sendEmptyMessageDelayed(100, 5000);
            }
        });

        runable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new AppRunable()).start();
            }
        });
        thread.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mThread = new AppThread();
                mThread.start();
            }
        });
        appAsyncTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mTask = new AppAsyncTask();
                mTask.execute("");
            }
        });
    }

    public class AppHandler extends Handler {

        public AppHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            for (int i = 0; i < 10 ; ++i) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i == 5) {
                    throw new RuntimeException("AppHandler error");
                }
            }
        }
    }
}
