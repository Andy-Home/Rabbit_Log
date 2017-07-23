package com.andy.rabbit_log;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.andy.rabbitlog.LogManager;
import com.anni.rabbit_log.R;


public class MainActivity extends Activity {
    private ListView mListView;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.testList);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        arrayAdapter.addAll(LogManager.findFileList());
        mListView.setAdapter(arrayAdapter);
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
