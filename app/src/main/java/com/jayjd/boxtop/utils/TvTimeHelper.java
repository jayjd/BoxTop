package com.jayjd.boxtop.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Android TV 桌面时间刷新工具（支持时区/时间变化）
 */
public class TvTimeHelper {

    private static final long INTERVAL = 60_000L; // 分钟级刷新更合理

    private final Context appContext;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private SimpleDateFormat formatter;
    private OnTimeChangedListener listener;
    private boolean running = false;
    private final Runnable ticker = new Runnable() {
        @Override
        public void run() {
            if (!running) return;

            dispatchTime();

            long now = System.currentTimeMillis();
            long delay = INTERVAL - (now % INTERVAL);
            handler.postDelayed(this, delay);
        }
    };
    /**
     * 系统时间 / 时区变化监听
     */
    private final BroadcastReceiver timeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 重新获取时区并立刻刷新
            initFormatter();
            dispatchTime();
        }
    };

    public TvTimeHelper(Context context) {
        this.appContext = context.getApplicationContext();
        initFormatter();
    }

    private void initFormatter() {
        formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getDefault());
    }

    private void dispatchTime() {
        if (listener != null) {
            listener.onTimeChanged(formatter.format(new Date()));
        }
    }

    public void start(OnTimeChangedListener listener) {
        if (running) return;

        this.listener = listener;
        this.running = true;

        // 注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            appContext.registerReceiver(timeReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            appContext.registerReceiver(timeReceiver, filter);
        }
        handler.post(ticker);
    }

    public void stop() {
        running = false;
        handler.removeCallbacksAndMessages(null);

        try {
            appContext.unregisterReceiver(timeReceiver);
        } catch (Exception ignore) {
        }
    }

    public interface OnTimeChangedListener {
        void onTimeChanged(String time);
    }
}
