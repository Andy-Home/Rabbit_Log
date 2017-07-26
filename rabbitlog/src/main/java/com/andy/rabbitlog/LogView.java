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
        init(context);
    }

    public LogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LogView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        super.setLines(LINE);
        super.setTextColor(context.getResources().getColor(R.color.logText));
        super.setBackgroundResource(R.color.logBackground);
        super.setGravity(Gravity.END);
    }

    /**
     * 打印显示内容
     *
     * @param msg   打印内容
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
    public void setLines(int lines) {
        super.setLines(lines);
        LINE = lines;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }
}
