package com.jayjd.boxtop.enums;

import android.content.Intent;

import lombok.Getter;

@Getter
public enum AllSettings {
    SYSTEM_SETTINGS_CARDS("系统设置", Intent.class), // 首页卡片
    HOME_CARDS("首页卡片", Boolean.class), // 首页卡片
    HIDE_FUNCTION("隐私空间", Boolean.class), // 隐私空间
    VIP_FUNCTION("VIP功能", Boolean.class);

    private final String functionName;
    private final Class<?> booleanClass;


    AllSettings(String functionName, Class<?> booleanClass) {
        this.functionName = functionName;
        this.booleanClass = booleanClass;
    }

}
