package com.andy.rabbitlog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志信息 包括输入日志实体类以及系统信息类
 * <p>
 * Created by andy on 17-7-25.
 */

class Logs {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");

    static class InputLog {
        private String msg;
        private String time;

        InputLog() {
            time = format.format(new Date(System.currentTimeMillis()));
        }

        void setMsg(String msg) {
            this.msg = msg + "\n";
        }

        String getMsg() {
            return msg;
        }

        public String getTime() {
            return time;
        }
    }

    static class SystemLog {
        private static SystemLog mSystemLog;
        private String phoneName;       //手机名称
        private String phoneNameTip;
        private String sdkVersion;      //SDK版本
        private String sdkVersionTip;
        private String jdkVersion;      //JDK版本
        private String jdkVersionTip;
        private String softVersion;     //软件版本
        private String softVersionTip;
        private String softCode;        //软件版本号
        private String softCodeTip;

        private String createTimeTip;
        private String[] javaVersion = {"1.1", "1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8"};

        private SystemLog(Context context) {
            phoneName = Build.MODEL;
            phoneNameTip = context.getString(R.string.phone_name);

            sdkVersion = Build.VERSION.RELEASE;
            sdkVersionTip = context.getString(R.string.sdk_version);

            int jdk = Integer.valueOf(System.getProperty("java.class.version").substring(0, 2));
            if ((jdk - 45) > 7) {
                jdkVersion = context.getResources().getString(R.string.unknown_version);
            } else {
                jdkVersion = javaVersion[jdk - 45];
            }
            jdkVersionTip = context.getString(R.string.jdk_version);
            PackageInfo info;
            try {
                info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                softVersion = info.versionName;
                softCode = String.valueOf(info.versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                softVersion = context.getString(R.string.software_version_not_found);
                softCode = context.getString(R.string.software_code_not_found);
            }
            softVersionTip = context.getString(R.string.software_version);
            softCodeTip = context.getString(R.string.software_code);

            createTimeTip = context.getString(R.string.file_create_time);
        }

        static SystemLog getInstance(Context context) {
            if (mSystemLog == null) {
                mSystemLog = new SystemLog(context);
            }
            return mSystemLog;
        }

        public String getInfo() {
            return phoneNameTip + ":" + phoneName + "\n"
                    + sdkVersionTip + ":Android " + sdkVersion + "\n"
                    + jdkVersionTip + ":JDK " + jdkVersion + "\n"
                    + softVersionTip + ":" + softVersion + "\n"
                    + softCodeTip + ":" + softCode + "\n"
                    + createTimeTip + ":" + format.format(new Date(System.currentTimeMillis())) + "\n"
                    + "=====*=====**====*=****====*****====****====*****=****====\n"
                    + "====*=*====*=*===*=*===**==*===**==**==**=====*===*===**==\n"
                    + "===*****===*==*==*=*=====*=*****==*======*====*===*=====*=\n"
                    + "==*=====*==*===*=*=*===**==*===*===**==**=====*===*===**==\n"
                    + "=*=======*=*====**=****====*====*===****====*****=****====\n";
        }
    }
}
