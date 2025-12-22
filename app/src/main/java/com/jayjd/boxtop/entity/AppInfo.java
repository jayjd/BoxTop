package com.jayjd.boxtop.entity;

import android.graphics.drawable.Drawable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.jayjd.boxtop.enums.AppType;

import java.io.Serializable;

import lombok.Data;

@Entity(tableName = "app_info")
@Data
public class AppInfo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String packageName;
    private String name;
    @Ignore
    private Drawable appIcon;
    @Ignore
    private Drawable appBanner;

    private String appBannerBase64;
    private String appIconBase64;

    private boolean isBanner;

    private int openAppCount;
    private int sortIndex;
    private int cardColor;
    private String packagePath;
    private String versionName;
    private int versionCode;
    private int minSdkVersion;
    private int targetSdkVersion;
    private boolean isSystem;
    private boolean isHidden;

    @Ignore
    private AppType appType;
}