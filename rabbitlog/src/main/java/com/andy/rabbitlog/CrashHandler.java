package com.andy.rabbitlog;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import static com.andy.rabbitlog.LogManager.format;

/**
 * 系统崩溃时日志收集处理
 * <p>
 * Created by andy on 17-7-26.
 */

class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Context context;
    /**
     * 外部存储文件前缀名
     */
    private final static String PREFIX = "log_crash_";
    private final static String SUFFIX = ".txt";

    CrashHandler(Context context) {
        this.context = context;
    }

    /**
     * 设置系统默认使用该异常处理器
     */
    void useUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        //提示用户
        Toast.makeText(context, R.string.program_crash, Toast.LENGTH_LONG).show();
        //写文件
        String fileName = PREFIX + format.format(new Date(System.currentTimeMillis())) + SUFFIX;
        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
        } else {
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
        }
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.write(Logs.SystemLog.getInstance(context).getInfo());
            throwable.printStackTrace(writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //退出程序
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
