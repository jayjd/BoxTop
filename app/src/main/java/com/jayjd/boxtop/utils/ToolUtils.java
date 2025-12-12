package com.jayjd.boxtop.utils;

import android.view.View;
import android.view.animation.BounceInterpolator;

public class ToolUtils {
    public static void startAnimation(View view) {
        view.animate().scaleX(1.05f).scaleY(1.05f).setDuration(500) // 适当延长动画时间
                .setInterpolator(new BounceInterpolator()) // 使用OvershootInterpolator
                .start();
    }

    public static void endAnimation(View view) {
        view.animate().scaleX(1f).scaleY(1f).setDuration(500) // 适当延长动画时间
                .setInterpolator(new BounceInterpolator()) // 使用OvershootInterpolator
                .start();
    }
}
