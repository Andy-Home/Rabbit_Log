package com.andy.rabbitlog;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.andy.rabbitlog.LogManager.format;

/**
 * 日志文件
 * <p>
 * Created by andy on 17-7-20.
 */
class LogFile {
    private static final String tag = "LogFile";
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
    private final static String PREFIX = "log_file_";
    private final static String SUFFIX = ".txt";

    private Context context;

    LogFile(Context context) {
        this.context = context;
    }

    /**
     * 将缓存文件保存到外部存储器中
     */
    private void saveFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheFile = new File(context.getCacheDir(), CACHE_FILE);
            Log.v("LogFile", "缓存文件大小：" + cacheFile.length());
            String fileName = PREFIX + format.format(new Date(System.currentTimeMillis())) + SUFFIX;
            File file;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
            } else {
                file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
            }

            try {
                BufferedReader from = new BufferedReader(new FileReader(cacheFile));
                BufferedWriter dest = new BufferedWriter(new FileWriter(file));

                dest.write(Logs.SystemLog.getInstance(context).getInfo());
                String line;
                while ((line = from.readLine()) != null) {
                    dest.write(line);
                    dest.newLine();
                }
                from.close();
                dest.flush();
                dest.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (cacheFile.delete()) {
                Log.v(tag, "删除缓存文件成功");
            }
        } else {
            Log.e(tag, "没有外部存储权限");
        }
    }

    static final int SAVE_CACHE_FILE = 1;
    static final int SAVE_EXTERNAL_FILE = 2;

    /**
     * 保存数据
     *
     * @param msg   需要保存的数据
     * @param flag 标志位判断,当值为{@value SAVE_CACHE_FILE}时,数据保存到缓存中
     *             当值为{@value SAVE_EXTERNAL_FILE}时,数据保存到存储空间中
     */
    void save(final ArrayList<Logs.InputLog> msg, final int flag) {
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
                    for (Logs.InputLog content : msg) {
                        fw.write(content.getTime() + " " + content.getMsg());
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
    void setCacheValue(long num) {
        this.CACHE_MAX_VALUE = num;
    }

    List<File> findFileList() {
        List<File> list = new ArrayList<>();
        File dir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        } else {
            dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        }
        File[] files = new File[0];
        if (dir != null) {
            files = dir.listFiles();
        }
        Collections.addAll(list, files);
        return list;
    }
}
