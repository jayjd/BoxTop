package com.jayjd.boxtop.utils;

import android.content.Context;
import android.content.Intent;

public class LicenseEvent {

    public static final String ACTION_PRO_UNLOCKED =
            "com.jayjd.boxtop.ACTION_PRO_UNLOCKED";

    public static void notifyChanged(Context context) {
        context.sendBroadcast(new Intent(ACTION_PRO_UNLOCKED));
    }
}