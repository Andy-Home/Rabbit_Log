package com.andy.rabbitlog;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志信息 包括输入日志实体类以及系统信息类
 * <p>
 * Created by andy on 17-7-25.
 */

public class Logs {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");

    public static class InputLog {
        private String msg;
        private String time;

        public InputLog() {
            time = format.format(new Date(System.currentTimeMillis()));
        }

        public void setMsg(String msg) {
            this.msg = msg + "\n";
        }

        public String getMsg() {
            return msg;
        }

        public String getTime() {
            return time;
        }
    }

    public static class SystemLog {
        private static SystemLog mSystemLog;
        private String phoneName;       //手机名称
        private String sdkVersion;      //SDK版本
        private String jdkVersion;      //JDK版本
        private String kernelVersion;   //内核版本
        private String softVersion;     //软件版本
        private Context context;
        private String[] javaVersion = {"1.1", "1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8"};

        private SystemLog(Context context) {
            this.context = context;

            phoneName = Build.MODEL;
            sdkVersion = Build.VERSION.RELEASE;
            int jdk = Integer.valueOf(System.getProperty("java.class.version").substring(0, 2));
            if ((jdk - 45) > 7) {
                jdkVersion = context.getResources().getString(R.string.unknown_version);
            } else {
                jdkVersion = javaVersion[jdk - 45];
            }
            kernelVersion = System.getProperty("os.version");
            try {
                softVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                softVersion = context.getString(R.string.software_version_not_found);
            }
        }

        public static SystemLog getInstance(Context context) {
            if (mSystemLog == null) {
                mSystemLog = new SystemLog(context);
            }
            return mSystemLog;
        }

        public String getInfo() {
            String info = context.getString(R.string.phone_name) + ":" + phoneName + "\n"
                    + context.getString(R.string.sdk_version) + ":Android " + sdkVersion + "\n"
                    + context.getString(R.string.jdk_version) + ":JDK " + jdkVersion + "\n"
                    + context.getString(R.string.kernel_version) + ":" + kernelVersion + "\n"
                    + context.getString(R.string.software_version) + ":" + softVersion + "\n"
                    + context.getString(R.string.file_create_time) + ":" + format.format(new Date(System.currentTimeMillis())) + "\n"
                    + "=======================================\n"
                    + "=======================================\n"
                    + "=======================================\n";
            return info;
        }
    }
}
