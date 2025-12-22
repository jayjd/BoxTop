package com.jayjd.boxtop.cards;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jayjd.boxtop.R;
import com.jayjd.boxtop.cards.tvdesktop.WaveformView;
import com.jayjd.boxtop.utils.cpu.CpuMonitor;

import java.util.List;
import java.util.Locale;


public class CardPerformance extends BaseCardFragment {
    CpuMonitor cpuMonitor;
    private TextView systemCpuPercent, appCpuPercent;
    private WaveformView waveformView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_performance, container, false);
        systemCpuPercent = view.findViewById(R.id.system_cpu_percent);
        appCpuPercent = view.findViewById(R.id.app_cpu_percent);
        waveformView = view.findViewById(R.id.waveform_cpu);
        waveformView.init(3);
        waveformView.setLineColor(0, 0xFF4CAF50); // CPU
        waveformView.setLineColor(1, 0xFF2196F3); // 内存
        cpuMonitor = CpuMonitor.getInstance(appContext);
        return view;
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        Log.d("CardPerformance", "onFragmentVisible() called");
        startUpdating();
    }

    @Override
    protected void onFragmentInvisible() {
        super.onFragmentInvisible();
        Log.d("CardPerformance", "onFragmentInvisible() called");
        stopUpdating();
    }

    private void startUpdating() {
        cpuMonitor.startMonitoring(1000, new CpuMonitor.CpuUsageListener() {
            @Override
            public void onSystemCpuUsage(float cpuUsage) {
                systemCpuPercent.setText(String.format(Locale.getDefault(), "系统: %.1f%%", cpuUsage));
                // 更新波形
                float normalized = cpuUsage / 50f;
                normalized = Math.max(0f, Math.min(1f, normalized));
                waveformView.pushValue(0, normalized);
            }

            @Override
            public void onAppCpuUsage(float usedMem) {
                appCpuPercent.setText(String.format(Locale.getDefault(), "软件: %.1f%%", usedMem));
                // 更新波形
                float normalized = usedMem / 50f;
                normalized = Math.max(0f, Math.min(1f, normalized));
                waveformView.pushValue(1, normalized);
            }

            @Override
            public void onPerCoreCpuUsage(List<Float> coreUsages) {
                StringBuilder coreStr = new StringBuilder();
                for (int j = 0; j < coreUsages.size(); j++) {
                    if (j > 0) coreStr.append(", ");
                    coreStr.append(String.format("%.0f%%", coreUsages.get(j)));
                }

            }
        });
    }

    private void stopUpdating() {
        cpuMonitor.stopMonitoring();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cpuMonitor.release();
    }
}