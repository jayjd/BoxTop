package com.jayjd.boxtop.utils;

import android.content.Context;

import com.jayjd.boxtop.enums.LicenseLevel;

public class LicenseStore {

    private static final String SP_NAME = "boxtop_license";
    private static final String KEY_PRO = "pro_unlocked";

    public static void save(Context context, LicenseLevel level) {
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_PRO, level == LicenseLevel.PRO)
                .apply();
    }

    public static LicenseLevel load(Context context) {
        boolean unlocked = context
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_PRO, false);
        return unlocked ? LicenseLevel.PRO : LicenseLevel.FREE;
    }
}
