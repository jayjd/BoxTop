package com.jayjd.boxtop.utils;

import android.os.Handler;
import android.os.Looper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Android TV 桌面时间刷新工具
 * 用于 tv_home_time 等 UI
 */
public class TvTimeHelper {

    private static final long INTERVAL = 1000L;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final SimpleDateFormat formatter =
            new SimpleDateFormat("HH:mm", Locale.getDefault());
    private OnTimeChangedListener listener;
    private boolean running = false;
    private final Runnable ticker = new Runnable() {
        @Override
        public void run() {
            if (!running) return;

            String time = formatter.format(new Date());
            if (listener != null) {
                listener.onTimeChanged(time);
            }

            // 对齐到下一秒，防止时间漂移
            long now = System.currentTimeMillis();
            long delay = INTERVAL - (now % INTERVAL);
            handler.postDelayed(this, delay);
        }
    };

    /**
     * 开始刷新
     */
    public void start(OnTimeChangedListener listener) {
        if (running) return;

        this.listener = listener;
        this.running = true;
        handler.post(ticker);
    }

    /**
     * 停止刷新（Activity/Fragment onStop 调用）
     */
    public void stop() {
        running = false;
        handler.removeCallbacksAndMessages(null);
    }

    public interface OnTimeChangedListener {
        void onTimeChanged(String time);
    }
}
