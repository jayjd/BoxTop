package com.jayjd.boxtop.utils;

import android.content.Context;

public class App {
    // 是否付费
    public static boolean isPaid() {
        return PurchaseManager.getInstance().isPro();
    }


    // 是否显示软件的名称
    public static boolean isShowAppName(Context context) {
        return (boolean) SPUtils.get(context, "is_show_app_name", false);
    }

    public static void setShowAppName(Context context, boolean isShow) {
        SPUtils.put(context, "is_show_app_name", isShow);
    }

}
