package com.jayjd.boxtop.utils;

import android.content.Context;

import com.jayjd.boxtop.enums.LicenseLevel;

import lombok.Getter;

@Getter
public class PurchaseManager {

    private static volatile PurchaseManager instance;
    private LicenseLevel licenseLevel = LicenseLevel.FREE;

    private PurchaseManager() {}

    public static PurchaseManager getInstance() {
        if (instance == null) {
            synchronized (PurchaseManager.class) {
                if (instance == null) {
                    instance = new PurchaseManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        licenseLevel = LicenseStore.load(context);
    }

    public boolean isPro() {
        return licenseLevel == LicenseLevel.PRO;
    }

    public void unlockPro(Context context) {
        licenseLevel = LicenseLevel.PRO;
        LicenseStore.save(context, LicenseLevel.PRO);
        LicenseEvent.notifyChanged(context);
    }

    /**
     * 验证激活码
     */
    public boolean verifyActivationCode(String code) {
        // 简单示例，可改成动态生成或加密
        return "BOXTOP-PRO-2025".equalsIgnoreCase(code.trim());
    }

    /**
     * 使用激活码尝试解锁
     */
    public boolean unlockWithCode(Context context, String code) {
        if (verifyActivationCode(code)) {
            unlockPro(context);
            return true;
        }
        return false;
    }

    public void lockPro(Context context) {
        licenseLevel = LicenseLevel.FREE;
        LicenseStore.save(context, LicenseLevel.FREE);
        LicenseEvent.notifyChanged(context);
    }
}
