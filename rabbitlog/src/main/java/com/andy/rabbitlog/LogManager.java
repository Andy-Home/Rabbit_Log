package com.andy.rabbitlog;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 主要是面向用户的接口 管理日志内容
 * <p>
 * Created by andy on 17-7-19.
 */

public class LogManager {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams params;
    private static LogView view;
    private LogFile file;
    private static LogManager mLogManager;
    private LogCat mLogCat;
    private CrashHandler mCrashHandler;
    static SimpleDateFormat format;

    private LogManager() {
        contents = new ArrayList<>();
        format = new SimpleDateFormat("yyyyMMddHHmmss");
    }

    /**
     * 初始化
     */
    public LogManager init(Context context) {
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

        if (mCrashHandler == null) {
            mCrashHandler = new CrashHandler(context);
        }

        return mLogManager;
    }

    public static LogManager getInstance() {
        if (mLogManager == null) {
            mLogManager = new LogManager();
        }
        return mLogManager;
    }

    /**
     * 是否显示的标志 用来控制向LogView输入数据
     */
    private boolean display_flag = false;

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
     * 启用自定义异常处理器
     */
    public void enabledCrashHandler() {
        mCrashHandler.useUncaughtExceptionHandler();
    }

    /**
     * 临时数据
     */
    private static ArrayList<Logs.InputLog> contents;

    public void setText(String msg) {
        if (display_flag) {
            view.setText(msg);
        }
        Logs.InputLog inputLog = new Logs.InputLog();
        inputLog.setMsg(msg);
        contents.add(inputLog);
        if (contents.size() > 100) {
            ArrayList<Logs.InputLog> copy = new ArrayList<>(contents);
            file.save(copy, LogFile.SAVE_CACHE_FILE);
            contents.clear();
        }
    }

    /**
     * 设置屏幕上能够显示的信息的行数
     *
     * @param line 行数
     */
    public LogManager setLine(int line) {
        view.setLines(line);
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
    public void saveLog() {
        if (contents.size() > 0) {
            ArrayList<Logs.InputLog> copy = new ArrayList<>(contents);
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
    public void saveLogCatInfo(int level) {
        mLogCat.setLevel(level);
        mLogCat.start();
    }

    /**
     * 获取存储到外部存储的文件列表
     */
    public List<File> findFileList() {
        return file.findFileList();
    }
}
