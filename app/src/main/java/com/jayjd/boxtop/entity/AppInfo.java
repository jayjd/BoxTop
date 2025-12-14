package com.jayjd.boxtop.entity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import lombok.Data;

@Data
public class AppInfo {
    private String packageName;
    private String name;
    private Drawable icon;
    private Bitmap bitmapIcon;
    private int cardColor;
    private String packagePath;
    private String versionName;
    private int versionCode;
    private int minSdkVersion;
    private int targetSdkVersion;
    private boolean isSystem;
}