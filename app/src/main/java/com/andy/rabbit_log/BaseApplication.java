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
                .setLine(10)                    //设置在屏幕上显示日志的行数
                .setCache(10 * 1024 * 1024)     //设置缓存的内存
                .enabledCrashHandler();         //启用自定义异常处理器
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogManager.getInstance()
                .stop()
                .finish();          //释放内存
    }
}
