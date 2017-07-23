package com.andy.rabbitlog;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 作用：显示打印信息
 * <p>
 * Created by andy on 17-7-19.
 */

public class LogView extends TextView {
    private ArrayList<String> msg = new ArrayList<>();
    /**
     * 能够显示到屏幕上的信息行数
     */
    static int LINE = 8;

    public LogView(Context context) {
        super(context);
        init();
    }

    public LogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LogView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        super.setLines(LINE);
        super.setBackgroundResource(R.color.logBackground);
        super.setGravity(Gravity.RIGHT);
    }

    /**
     * 打印显示内容
     *
     * @param msg
     */
    public void setText(String msg) {
        if (this.msg.size() > 99) {
            this.msg.clear();
        }
        this.msg.add(msg);

        String display = "";
        int flag = this.msg.size();
        for (int i = 0; i < LINE && flag > 0; i++) {
            display = this.msg.get(--flag) + "\n" + display;
        }

        super.setText(display);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }
}
