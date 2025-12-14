package com.jayjd.boxtop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.animation.BounceInterpolator;

import androidx.core.content.ContextCompat;

import com.jayjd.boxtop.R;
import com.jayjd.boxtop.entity.AppInfo;

public class ToolUtils {
    public static void startAnimation(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.icon_selector_selected);
            view.setBackground(drawable);
        }
        view.animate().scaleX(1.05f).scaleY(1.05f).setDuration(500) // 适当延长动画时间
                .setInterpolator(new BounceInterpolator()) // 使用OvershootInterpolator
                .start();
    }

    public static void endAnimation(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.icon_selector_default);
            view.setBackground(drawable);
        }
        view.animate().scaleX(1f).scaleY(1f).setDuration(500) // 适当延长动画时间
                .setInterpolator(new BounceInterpolator()) // 使用OvershootInterpolator
                .start();
    }

    public static boolean isAppLaunchable(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        // 1. 尝试获取启动应用的Intent
        Intent launchIntent = packageManager.getLaunchIntentForPackage(packageName);

        // 2. 检查Intent是否存在
        if (launchIntent != null) {
            // Intent存在，意味着应用已安装且有启动入口
            // 接下来可以进行更细致的检查
            try {
                // 3. 额外检查：确保应用没有被禁用（组件未处于DISABLED状态）
                // 仅获取应用包信息，不获取组件，通常launchIntent不为空就够了
                packageManager.getApplicationInfo(packageName, 0);
                return true; // 应用已安装并可启动
            } catch (PackageManager.NameNotFoundException e) {
                // 虽然很少见（因为launchIntent不为空），但以防万一
                return false;
            }
        } else {
            // launchIntent为空，应用可能未安装，或没有可启动的Activity
            return false;
        }
    }

    public static void startUninstallProcess(Activity activity, String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            // 提示用户包名不能为空
            return;
        }

        try {
            // 1. 构造 Intent
            Intent intent = new Intent(Intent.ACTION_DELETE);

            // 2. 设置数据 URI，格式为 "package:com.your.package.name"
            intent.setData(Uri.parse("package:" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 3. 启动 Activity
            activity.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            // 处理可能的异常，例如系统找不到合适的Activity来处理ACTION_DELETE
        }
    }

    public static AppInfo getEmptyAppInfo(String name) {
        AppInfo appInfo = new AppInfo();
        appInfo.setName(name);
        appInfo.setPackageName("");
        return appInfo;
    }
}
