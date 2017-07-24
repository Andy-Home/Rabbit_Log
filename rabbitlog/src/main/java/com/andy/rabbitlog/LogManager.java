package com.andy.rabbitlog;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 主要是面向用户的接口 管理日志内容
 * <p>
 * Created by andy on 17-7-19.
 */

public class LogManager {
    /**
     * 暂存在内存中的数据的最大量
     */
    private static int MAX_CONTENTS = 100;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams params;
    private static LogView view;
    private static LogFile file;
    private static LogManager mLogManager;
    private static LogCat mLogCat;
    private Context context;
    static SimpleDateFormat format;

    private LogManager(Context context) {
        this.context = context;
        mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        // 设置这样的flag才能使得WindowManager不拦截点击事件
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.alpha = 1.0f;
        params.format = PixelFormat.TRANSLUCENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        if (view == null) {
            view = new LogView(context);
        }

        if (file == null) {
            file = new LogFile(context);
        }

        if (mLogCat == null) {
            mLogCat = new LogCat(context);
        }
        contents = new ArrayList<>();
        format = new SimpleDateFormat("yyyyMMddHHmmss");
    }

    public static LogManager getInstance(Context context) {
        if (mLogManager == null) {
            mLogManager = new LogManager(context.getApplicationContext());
        }
        return mLogManager;
    }

    /**
     * 是否显示的标志 用来控制向LogView输入数据
     */
    private static boolean display_flag = false;

    public LogManager start() {
        if (!display_flag) {
            mWindowManager.addView(view, params);
        }
        display_flag = true;
        return mLogManager;
    }

    public LogManager stop() {
        if (display_flag) {
            mWindowManager.removeView(view);
        }
        display_flag = false;
        return mLogManager;
    }

    public void finish() {
        view = null;
        mLogManager = null;
        contents = null;
    }

    /**
     * 临时数据
     */
    private static ArrayList<String> contents;

    public static void setText(String msg) {
        if (display_flag) {
            view.setText(msg);
        }

        contents.add(msg + "\n");
        if (contents.size() > MAX_CONTENTS) {
            ArrayList<String> copy = (ArrayList<String>) contents.clone();
            file.save(copy, LogFile.SAVE_CACHE_FILE);
            contents.clear();
        }
    }

    /**
     * 设置屏幕上能够显示的信息的行数
     *
     * @param line  行数
     */
    public LogManager setLine(int line) {
        view.setLines(line);
        return mLogManager;
    }

    /**
     * 设置View的背景颜色
     *
     * @param color 颜色值
     */
    public LogManager setBackground(int color) {
        view.setBackgroundColor(color);
        return mLogManager;
    }

    /**
     * 设置字体颜色
     *
     * @param color 颜色资源值
     */
    public LogManager setTextColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setTextColor(context.getResources().getColor(color, null));
        } else {
            view.setTextColor(context.getResources().getColor(color));
        }
        return mLogManager;
    }

    /**
     * 设置缓存日志文件大小
     *
     * @param max 单位为Byte
     */
    public LogManager setCache(long max) {
        file.setCacheValue(max);
        return mLogManager;
    }

    /**
     * 保存缓存的日志文件到本地
     */
    public static void saveLog() {
        if (contents.size() > 0) {
            ArrayList<String> copy = (ArrayList<String>) contents.clone();
            file.save(copy, LogFile.SAVE_EXTERNAL_FILE);
            contents.clear();
        }
    }

    /**
     * 保存系统日志
     *
     * @param level 保存的日志最低级别
     *              使用{@link LogCat}中的值,例如{@link LogCat#ASSERT}
     */
    public static void saveLogCatInfo(int level) {
        mLogCat.setLevel(level);
        mLogCat.start();
    }

    /**
     * 获取存储到外部存储的文件列表
     */
    public static List<String> findFileList() {
        return file.findFileList();
    }
}
