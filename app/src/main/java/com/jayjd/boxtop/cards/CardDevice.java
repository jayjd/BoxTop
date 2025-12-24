package com.jayjd.boxtop.cards;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jayjd.boxtop.R;


public class CardDevice extends BaseCardFragment {

    private View rowDeviceName;
    private View rowSystemType;
    private View rowAndroidVersion;
    private View rowResolution;
    private View rowModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_card_device, container, false);
        bindViews(root);
        fillStaticInfo();   // 初始填充
        return root;
    }

    private void fillStaticInfo() {
        setRow(rowDeviceName, "设备名称", Build.HARDWARE + " / " + Build.DEVICE);

        setRow(rowSystemType, "系统类型", isGoogleTV() ? "Google TV" : "Android TV");

        setRow(rowAndroidVersion, "Android 版本", "Android " + Build.VERSION.RELEASE);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        setRow(rowResolution, "分辨率", dm.widthPixels + " × " + dm.heightPixels);

        setRow(rowModel, "设备型号", Build.MODEL);

    }

    private void setRow(View row, String label, String value) {
        if (row == null) return;

        TextView tvLabel = row.findViewById(R.id.tv_label);
        TextView tvValue = row.findViewById(R.id.tv_value);

        tvLabel.setText(label);
        tvValue.setText(value);
    }

    private boolean isGoogleTV() {
        return appContext.getPackageManager().hasSystemFeature("com.google.android.tv");
    }

    private String getRamInfo() {
        ActivityManager am = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(info);

        long totalGb = info.totalMem / 1024 / 1024 / 1024;
        return totalGb + " GB";
    }

    private void bindViews(View root) {
        rowDeviceName = root.findViewById(R.id.row_device_name);
        rowSystemType = root.findViewById(R.id.row_system_type);
        rowAndroidVersion = root.findViewById(R.id.row_android_version);
        rowResolution = root.findViewById(R.id.row_resolution);
        rowModel = root.findViewById(R.id.row_model);
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        Log.d("CardDevice", "onFragmentVisible() called");
    }

    @Override
    protected void onFragmentInvisible() {
        super.onFragmentInvisible();
        Log.d("CardDevice", "onFragmentInvisible() called");
    }

    @Override
    protected void onProStateChanged(boolean isPro) {
        Log.d("TAG", "onProStateChanged: " + isPro);
    }
}
