package com.andy.rabbit_log;

import android.app.Application;

import com.andy.rabbitlog.LogManager;

/**
 * 自定义 Application
 *
 * Created by andy on 17-7-20.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogManager.getInstance()
                .init(getApplicationContext())
                .setLine(10)
                .setCache(10 * 1024 * 1024)
                .enabledCrashHandler();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogManager.getInstance()
                .stop()
                .finish();
    }
}
