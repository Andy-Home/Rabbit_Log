package com.andy.rabbit_log;

import android.app.Application;

import com.andy.rabbitlog.LogManager;
import com.anni.rabbit_log.R;

/**
 * Created by andy on 17-7-20.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogManager.getInstance(this.getApplicationContext())
                .setLine(10)
                .setTextColor(R.color.logText);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogManager.getInstance(this.getApplicationContext())
                .stop()
                .finish();
    }
}
