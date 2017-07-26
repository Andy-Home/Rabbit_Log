package com.andy.rabbit_log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.andy.rabbitlog.LogCat;
import com.andy.rabbitlog.LogManager;

/**
 * 功能列表界面
 * <p>
 * Created by andy on 17-7-20.
 */

public class TestActivity extends Activity implements View.OnClickListener {
    private LogManager mLogManager;
    private Button open, close, save, find, saveCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        open = findViewById(R.id.open);
        close = findViewById(R.id.close);
        save = findViewById(R.id.save);
        find = findViewById(R.id.find);
        saveCat = findViewById(R.id.save_cat);
        setListener();
        mLogManager = LogManager.getInstance();
    }

    private void setListener() {
        open.setOnClickListener(this);
        close.setOnClickListener(this);
        save.setOnClickListener(this);
        find.setOnClickListener(this);
        saveCat.setOnClickListener(this);
    }

    private int flag = 0;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open:
                mLogManager.start();
                mLogManager.setText("开启日志界面显示");
                break;
            case R.id.close:
                mLogManager.setText("关闭日志界面显示");
                mLogManager.stop();
                break;
            case R.id.save:
                mLogManager.setText("保存日志内容");
                mLogManager.saveLog();
                break;
            case R.id.find:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.save_cat:
                mLogManager.setText("保存系统日志");
                if (flag == 0) {
                    mLogManager.saveLogCatInfo(LogCat.VERBOSE);
                } else if (flag == 1) {
                    mLogManager.saveLogCatInfo(LogCat.DEBUG);
                } else if (flag == 2) {
                    mLogManager.saveLogCatInfo(LogCat.INFO);
                } else if (flag == 3) {
                    mLogManager.saveLogCatInfo(LogCat.WARN);
                } else if (flag == 4) {
                    mLogManager.saveLogCatInfo(LogCat.ERROR);
                } else if (flag == 5) {
                    mLogManager.saveLogCatInfo(LogCat.ASSERT);
                    flag = 0;
                }
                flag++;
                break;
        }
    }
}
