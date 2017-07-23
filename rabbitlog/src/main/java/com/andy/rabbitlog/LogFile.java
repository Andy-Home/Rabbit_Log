package com.andy.rabbitlog;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日志文件
 * <p>
 * Created by andy on 17-7-20.
 */

public class LogFile {
    /**
     * 保存的缓存文件名
     */
    private final static String CACHE_FILE = "cache_data.txt";
    /**
     * 缓存文件的最大值 默认1MB
     */
    private long CACHE_MAX_VALUE = 1024 * 1024;
    /**
     * 外部存储文件前缀名
     */
    private final static String PREFIX = "log_";
    private final static String SUFFIX = ".txt";

    private static LogFile mLogFile;

    private Context context;

    private LogFile(Context context) {
        this.context = context;
    }

    public static LogFile getInstance(Context context) {
        if (mLogFile == null) {
            mLogFile = new LogFile(context);
        }
        return mLogFile;
    }

    /**
     * 将缓存文件保存到外部存储器中
     */
    public void saveFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheFile = new File(context.getCacheDir(), CACHE_FILE);
            Log.v("LogFile", "缓存文件大小：" + cacheFile.length());
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = PREFIX + format.format(new Date()) + SUFFIX;
            File file;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
            } else {
                file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
            }

            try {
                InputStream from = new FileInputStream(cacheFile);
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
            Log.v("LogFile", "保存文件大小：" + file.length());
            Log.v("LogFile", "保存文件路径：" + file.getAbsolutePath());
            if (cacheFile.delete()) {
                Log.v("LogFile", "删除缓存文件成功");
            }
        } else {
            Log.e("LogFile", "没有外部存储权限");
        }
    }

    public static final int SAVE_CACHE_FILE = 1;
    public static final int SAVE_EXTERNAL_FILE = 2;

    /**
     * 保存数据
     *
     * @param msg
     * @param flag 标志位判断,当值为{@value SAVE_CACHE_FILE}时,数据保存到缓存中
     *             当值为{@value SAVE_EXTERNAL_FILE}时,数据保存到存储空间中
     */
    public void save(final ArrayList<String> msg, final int flag) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File cacheFile = new File(context.getCacheDir(), CACHE_FILE);
                try {
                    //当缓存文件内容大于设置的最大值时清空文件内容
                    if (cacheFile.length() > CACHE_MAX_VALUE) {
                        FileWriter fw = new FileWriter(cacheFile);
                        fw.write("");
                        fw.flush();
                        fw.close();
                    }
                    FileWriter fw = new FileWriter(cacheFile, true);
                    for (String content : msg) {
                        Log.v("写入数据", content);
                        fw.write(content);
                    }
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (flag == SAVE_EXTERNAL_FILE) {
                    saveFile();
                }
            }
        }).start();
    }

    /**
     * 设置日志缓存文件的最大值
     * 超过该值时清空缓存文件
     *
     * @param num 单位为Byte
     */
    public void setCacheValue(long num) {
        this.CACHE_MAX_VALUE = num;
    }

    public List<String> findFileList() {
        List<String> list = new ArrayList<>();
        File dir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        } else {
            dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            list.add(file.getName());
        }
        return list;
    }
}
