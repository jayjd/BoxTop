package com.jayjd.boxtop.utils;

import android.content.Context;

import com.jayjd.boxtop.enums.LicenseLevel;

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

    public LicenseLevel getLicenseLevel() {
        return licenseLevel;
    }
}
