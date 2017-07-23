package com.andy.rabbit_log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.andy.rabbitlog.LogManager;
import com.anni.rabbit_log.R;

/**
 * 功能列表界面
 * <p>
 * Created by andy on 17-7-20.
 */

public class TestActivity extends Activity implements View.OnClickListener {

    private Button open, close, save, find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        open = findViewById(R.id.open);
        close = findViewById(R.id.close);
        save = findViewById(R.id.save);
        find = findViewById(R.id.find);

        open.setOnClickListener(this);
        close.setOnClickListener(this);
        save.setOnClickListener(this);
        find.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open:
                LogManager.getInstance(this).start();
                LogManager.setText("开启日志界面显示");
                break;
            case R.id.close:
                LogManager.setText("关闭日志界面显示");
                LogManager.getInstance(this).stop();
                break;
            case R.id.save:
                LogManager.setText("保存日志内容");
                LogManager.saveLog();
                break;
            case R.id.find:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}