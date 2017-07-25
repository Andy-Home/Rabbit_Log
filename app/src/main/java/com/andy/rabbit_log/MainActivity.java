package com.andy.rabbit_log;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.andy.rabbitlog.LogManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private ListView mListView;
    private Handler mHandler = new Handler();
    private List<File> files;
    private List<String> strings = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.testList);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        files = LogManager.findFileList();
        for (File file : files) {
            strings.add(file.getName());
        }
        arrayAdapter.addAll(strings);
        mListView.setAdapter(arrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                File file = files.get(i);

                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.andy.rabbit_log.fileprovider", file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //设置intent的Action属性
                intent.setAction(Intent.ACTION_VIEW);
                //设置intent的data和Type属性。
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                intent.setDataAndType(photoURI, "text/plain");
                //跳转
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        display();
    }

    int flag = 0;
    boolean lock = true;

    public void display() {
        flag++;
        if (lock) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogManager.setText(flag + ":打印信息");
                    display();
                }
            }, 300);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        lock = false;
    }
}
