package com.jayjd.boxtop.enums;

import android.content.Context;
import android.content.Intent;

import com.jayjd.boxtop.utils.SPUtils;

import lombok.Getter;

@Getter
public enum AllSettings {
    SYSTEM_SETTINGS_CARDS("系统设置", Intent.class, "system_settings_cards"), // 首页卡片
    HOME_CARDS("首页卡片", Boolean.class, "home_cards"), // 首页卡片
    BOOT_OPEN_APP_FUNCTION("开机启动软件", Intent.class, "boot_open_app_function"), // 开机自启动软件
    HIDE_FUNCTION("隐私空间", Boolean.class, "hide_function"), // 隐私空间
    VIP_FUNCTION("VIP功能", Boolean.class, "vip_function");

    private final String functionName;
    private final Class<?> booleanClass;
    private final String spKey;


    AllSettings(String functionName, Class<?> booleanClass, String spKey) {
        this.functionName = functionName;
        this.booleanClass = booleanClass;
        this.spKey = spKey;
    }

    public static void setHomeValue(Context context, AllSettings allSettings, boolean isChecked) {
        SPUtils.put(context, allSettings.getSpKey(), isChecked);
    }

    public static boolean getHomeValue(Context context, AllSettings allSettings) {
        return (boolean) SPUtils.get(context, allSettings.getSpKey(), true);
    }

    public static void setBootOpenAppValue(Context context, AllSettings allSettings, String pkg) {
        SPUtils.put(context, allSettings.getSpKey(), pkg);
    }

    public static String getBootOpenAppValue(Context context, AllSettings allSettings) {
        return (String) SPUtils.get(context, allSettings.getSpKey(), "");
    }
}
