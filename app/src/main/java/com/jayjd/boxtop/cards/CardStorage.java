package com.jayjd.boxtop.cards;

import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jayjd.boxtop.R;

import java.io.File;
import java.util.Locale;


public class CardStorage extends BaseCardFragment {

    private ProgressBar pbStorage;
    private TextView tvStoragePercent;
    private TextView tvStorageSize;

    // 新增 U 盘控件
    private ProgressBar pbUsb;
    private TextView tvUsbPercent;
    private TextView tvUsbStatus;
    private TextView tvUsbSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_storage, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        pbStorage = view.findViewById(R.id.pb_storage);
        tvStoragePercent = view.findViewById(R.id.tv_storage_percent);
        tvStorageSize = view.findViewById(R.id.tv_storage_size);

        pbUsb = view.findViewById(R.id.usb_storage);
        tvUsbPercent = view.findViewById(R.id.tv_usb_percent);
        tvUsbStatus = view.findViewById(R.id.tv_usb_status);
        tvUsbSize = view.findViewById(R.id.tv_usb_size);
    }

    private void updateUsbStorage() {
        File[] externalDirs = appContext.getExternalFilesDirs(null);
        File usbDir = null;
        for (File dir : externalDirs) {
            if (dir != null && Environment.isExternalStorageRemovable(dir)) {
                usbDir = dir;
                break;
            }
        }

        if (usbDir != null && usbDir.exists()) {
            StatFs statFs = new StatFs(usbDir.getPath());
            long total = statFs.getTotalBytes();
            long available = statFs.getAvailableBytes();
            long used = total - available;
            int percent = (int) ((used * 100f) / total);

            pbUsb.setProgressTintList(ColorStateList.valueOf(getProgressDrawable(percent)));
            pbUsb.setProgress(percent);
            tvUsbPercent.setText(percent + "%");
            tvUsbSize.setText(formatSize(used) + " / " + formatSize(total));
            tvUsbSize.setVisibility(View.VISIBLE);

            tvUsbStatus.setVisibility(View.GONE);

            ObjectAnimator animator = ObjectAnimator.ofInt(pbUsb, "progress", 0, percent);
            animator.setDuration(600);
            animator.start();
        } else {
            // 未插入
            tvUsbStatus.setVisibility(View.VISIBLE);
            pbUsb.setProgress(0);
            tvUsbPercent.setText("--");
            tvUsbSize.setVisibility(View.GONE);
            tvUsbStatus.setText("未插入");
        }
    }

    private void updateInternalStorage() {
        try {
            File dataDir = Environment.getDataDirectory();
            StatFs statFs = new StatFs(dataDir.getPath());

            long totalBytes = statFs.getTotalBytes();
            long availableBytes = statFs.getAvailableBytes();
            long usedBytes = totalBytes - availableBytes;

            int percent = (int) ((usedBytes * 100f) / totalBytes);
            // 根据使用率变色（可选）
            pbStorage.setProgressTintList(ColorStateList.valueOf(getProgressDrawable(percent)));

            // UI 更新
            pbStorage.setProgress(percent);
            tvStoragePercent.setText(percent + "%");
            tvStorageSize.setText(formatSize(usedBytes) + " / " + formatSize(totalBytes));

            ObjectAnimator animator = ObjectAnimator.ofInt(pbStorage, "progress", 0, percent);
            animator.setDuration(600);
            animator.start();
        } catch (Exception e) {
            Log.e("CardStorage", "updateInternalStorage error", e);
        }
    }

    private String formatSize(long bytes) {
        float gb = bytes / 1024f / 1024f / 1024f;
        return String.format(Locale.getDefault(), "%.1f GB", gb);
    }



    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        Log.d("CardStorage", "onFragmentVisible() called");
        updateInternalStorage();
        updateUsbStorage();
    }

    @Override
    protected void onFragmentInvisible() {
        super.onFragmentInvisible();
        Log.d("CardStorage", "onFragmentInvisible() called");
    }

    @Override
    protected void onProStateChanged(boolean isPro) {
        Log.d("TAG", "onProStateChanged: " + isPro);
    }
}