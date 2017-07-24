package com.andy.rabbitlog;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    private int mPid;
    private Context context;

    LogCat(Context context) {
        mPid = android.os.Process.myPid();
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
            this.cmd += "| grep \"(" + mPid + ")\"";
        }
    }

    /**
     * 打印{@value VERBOSE}以及以上级别的日志命令
     */
    private void v_cmd() {
        this.cmd = "logcat *:v *:d *:i *:w *:e *:f *:s ";
    }

    /**
     * 打印{@value DEBUG}以及以上级别的日志命令
     */
    private void d_cmd() {
        this.cmd = "logcat *:d *:i *:w *:e *:f *:s ";
    }

    /**
     * 打印{@value INFO}以及以上级别的日志命令
     */
    private void i_cmd() {
        this.cmd = "logcat *:i *:w *:e *:f *:s ";
    }

    /**
     * 打印{@value WARN}以及以上级别的日志命令
     */
    private void w_cmd() {
        this.cmd = "logcat *:w *:e *:f *:s ";
    }

    /**
     * 打印{@value ERROR}以及以上级别的日志命令
     */
    private void e_cmd() {
        this.cmd = "logcat *:e *:f *:s ";
    }

    /**
     * 打印{@value ASSERT}级别的日志命令
     */
    private void wtf_cmd() {
        this.cmd = "logcat *:f *:s ";
    }

    void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String fileName = PREFIX + format.format(new Date()) + SUFFIX;
                File file;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
                } else {
                    file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
                }

                try {
                    Process process = Runtime.getRuntime().exec(cmd);
                    InputStream from = process.getInputStream();
                    OutputStream dest = new FileOutputStream(file);

                    byte[] b = new byte[1024];
                    int c;
                    while ((c = from.read(b)) > 0) {
                        dest.write(b, 0, c);
                    }
                    from.close();
                    dest.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
