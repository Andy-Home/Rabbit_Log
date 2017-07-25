package com.andy.rabbitlog;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import static com.andy.rabbitlog.LogManager.format;

/**
 * 关于{@link android.util.Log}的管理操作
 * <p>
 * Created by andy on 17-7-24.
 */
public class LogCat {
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    private final static String PREFIX = "log_cat_";
    private final static String SUFFIX = ".txt";

    /**
     * Log日志打印命令
     */
    private String cmd;
    private String packageName;
    private Context context;

    LogCat(Context context) {
        packageName = context.getPackageName();
        v_cmd();
        this.context = context;
    }

    /**
     * 设置打印信息级别
     *
     * @param level {@value VERBOSE} < {@value DEBUG}
     *              < {@value INFO} < {@value WARN}
     *              < {@value ERROR} <{@value ASSERT}
     */
    void setLevel(int level) {
        switch (level) {
            case VERBOSE:
                v_cmd();
                break;
            case DEBUG:
                d_cmd();
                break;
            case INFO:
                i_cmd();
                break;
            case WARN:
                w_cmd();
                break;
            case ERROR:
                e_cmd();
                break;
            case ASSERT:
                wtf_cmd();
                break;
            default:
                this.cmd = "";
                break;
        }
        //过滤日志信息,只打印当前应用的信息
        if (!cmd.equals("")) {
            this.cmd += "| grep " + packageName;
        }
    }

    /**
     * 打印{@value VERBOSE}以及以上级别的日志命令
     */
    private void v_cmd() {
        this.cmd = "logcat -d *:v ";
    }

    /**
     * 打印{@value DEBUG}以及以上级别的日志命令
     */
    private void d_cmd() {
        this.cmd = "logcat -d *:d ";
    }

    /**
     * 打印{@value INFO}以及以上级别的日志命令
     */
    private void i_cmd() {
        this.cmd = "logcat -d *:i ";
    }

    /**
     * 打印{@value WARN}以及以上级别的日志命令
     */
    private void w_cmd() {
        this.cmd = "logcat -d *:w ";
    }

    /**
     * 打印{@value ERROR}以及以上级别的日志命令
     */
    private void e_cmd() {
        this.cmd = "logcat -d *:e ";
    }

    /**
     * 打印{@value ASSERT}级别的日志命令
     */
    private void wtf_cmd() {
        this.cmd = "logcat -d *:f ";
    }

    void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String fileName = PREFIX + format.format(new Date(System.currentTimeMillis())) + SUFFIX;
                File file;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
                } else {
                    file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
                }

                try {
                    Process process = Runtime.getRuntime().exec(cmd);
                    BufferedReader from = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    BufferedWriter dest = new BufferedWriter(new FileWriter(file));
                    System.out.println("日志开始写入");
                    dest.write(Logs.SystemLog.getInstance(context).getInfo());
                    String line;
                    while ((line = from.readLine()) != null) {
                        dest.write(line);
                        dest.newLine();
                    }
                    from.close();
                    dest.flush();
                    dest.close();
                    System.out.println("日志写入结束");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
