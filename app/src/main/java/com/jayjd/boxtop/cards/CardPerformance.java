package com.jayjd.boxtop.cards;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jayjd.boxtop.R;
import com.jayjd.boxtop.cards.entity.RamInfo;
import com.jayjd.boxtop.cards.tvdesktop.WaveformView;

import java.util.Locale;


public class CardPerformance extends BaseCardFragment {
    private final Handler handler = new Handler();
    private WaveformView waveformView;
    private final long UPDATE_INTERVAL = 1000; // 2 秒
    private TextView systemRamPercent;
    private boolean isUpdating = false;
    private ProgressBar pbRam;
    private TextView tvRamPercent, tvRamUse, tvRamTotal;
    private final Runnable ramUpdateTask = new Runnable() {
        @Override
        public void run() {
            if (!isAdded() || getView() == null) {
                return;
            }

            RamInfo info = getRamInfo(appContext);


            // 1️⃣ 百分比

            pbRam.setProgress(info.usedPercent);
            tvRamPercent.setText(info.usedPercent + "%");
            pbRam.setProgressTintList(ColorStateList.valueOf(getProgressDrawable(info.usedPercent)));

            String format = String.format(Locale.getDefault(), "%.2f GB 总共", info.totalMB / 1024.0);
            systemRamPercent.setText(format);

            tvRamUse.setText(String.format(Locale.getDefault(), "已用：%d MB", info.usedMB));
            tvRamTotal.setText(String.format(Locale.getDefault(), "可用：%d MB", info.availMB));

            // 2️⃣ 波形图（0~100）
            waveformView.pushValue(0, info.usedPercent);

            // 3️⃣ 下次刷新
            handler.postDelayed(this, UPDATE_INTERVAL);
        }
    };

    public static RamInfo getRamInfo(Context context) {
        ActivityManager am =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);

        long totalMB = mi.totalMem / 1024 / 1024;
        long availMB = mi.availMem / 1024 / 1024;
        long usedMB = totalMB - availMB;
        int percent = (int) (usedMB * 100f / totalMB);

        return new RamInfo(totalMB, usedMB, availMB, percent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_performance, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        pbRam = view.findViewById(R.id.pb_ram);
        tvRamPercent = view.findViewById(R.id.tv_ram_percent);
        systemRamPercent = view.findViewById(R.id.system_ram_percent);
        tvRamUse = view.findViewById(R.id.tv_ram_use);
        tvRamTotal = view.findViewById(R.id.tv_ram_total);


        waveformView = view.findViewById(R.id.waveform_cpu);
        waveformView.init(1);
        waveformView.setLineColor(0, 0xFF4CAF50); // CPU
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        Log.d("CardPerformance", "onFragmentVisible() called");
        startRamUpdating();
    }

    @Override
    protected void onFragmentInvisible() {
        super.onFragmentInvisible();
        Log.d("CardPerformance", "onFragmentInvisible() called");
        stopRamUpdating();
    }

    public void startRamUpdating() {
        if (isUpdating) return;

        isUpdating = true;
        handler.removeCallbacks(ramUpdateTask);
        handler.post(ramUpdateTask); // 立即执行一次
    }

    private void stopRamUpdating() {
        isUpdating = false;
        handler.removeCallbacks(ramUpdateTask);
    }
    @Override
    protected void onProStateChanged(boolean isPro) {
        Log.d("TAG", "onProStateChanged: " + isPro);
    }
}