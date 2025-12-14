package com.jayjd.boxtop.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.SizeUtils;
import com.jayjd.boxtop.R;
import com.jayjd.boxtop.entity.AppInfo;

public class ToolUtils {
    public static void startAnimation(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.icon_selector_selected);
            view.setBackground(drawable);
        }
        view.animate().scaleX(1.1f).scaleY(1.1f).setDuration(500) // 适当延长动画时间
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

    public static void uninstallApp(Context context, String packageName) {
        if (packageName == null || packageName.isEmpty()) return;

        try {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "无法打开卸载界面", Toast.LENGTH_SHORT).show();
        }
    }

    public static void uninstallAppWithResult(Activity activity, String packageName) {
        if (packageName == null || packageName.isEmpty()) return;

        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + packageName));
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        activity.startActivityForResult(intent, 1001);
    }

    public static AppInfo getEmptyAppInfo(String name) {
        AppInfo appInfo = new AppInfo();
        appInfo.setName(name);
        appInfo.setPackageName("");
        return appInfo;
    }

    /**
     * 检查当前应用是否设置为默认 Home 桌面。
     * 此方法在 Android 5.0 及以上版本中兼容。
     *
     * @return true 如果是默认桌面，否则 false
     */
    public static boolean isDefaultHome(Context context) {
        // 1. 创建 Home 意图
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);

        // 2. 解析系统默认处理该意图的 Activity
        // PackageManager.MATCH_DEFAULT_ONLY 确保只匹配可以被隐式启动的 Activity
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(homeIntent, PackageManager.MATCH_DEFAULT_ONLY);

        // 3. 检查解析结果的包名是否与当前应用包名一致
        if (resolveInfo != null && resolveInfo.activityInfo != null) {
            String currentDefaultPackage = resolveInfo.activityInfo.packageName;
            // getPackageName() 始终返回当前应用的包名
            return currentDefaultPackage.equals(context.getPackageName());
        }
        return false;
    }

    /**
     * 获取 Android TV 应用 Banner（Leanback 海报优先）
     */
    public static Drawable getTvAppIcon(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();

        try {
            // 获取应用信息
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
            return getAppIconWithFallback(pm, appInfo);

        } catch (PackageManager.NameNotFoundException e) {
            return pm.getDefaultActivityIcon(); // 返回默认图标
        }
    }

    public static Bitmap drawableToSmallBitmap(Drawable drawable, int sizeDp) {
        if (drawable == null) return null;

        int sizePx = SizeUtils.dp2px(sizeDp);
        Bitmap bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, sizePx, sizePx);
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * 带降级的图标加载
     *
     * @param pm      PackageManager
     * @param appInfo 应用信息
     * @return 图标 Drawable
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable getAppIconWithFallback(PackageManager pm, ApplicationInfo appInfo) {
        Drawable icon = null;

        // 优先级1: 尝试加载 Banner（TV 专用）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            icon = appInfo.loadBanner(pm);
        }

        // 优先级2: 从元数据中获取 TV Banner
        if (icon == null && appInfo.metaData != null) {
            int tvBannerId = appInfo.metaData.getInt("com.google.android.tv.banner", 0);
            if (tvBannerId != 0) {
                try {
                    icon = pm.getResourcesForApplication(appInfo).getDrawable(tvBannerId);
                } catch (Exception ignored) {
                }
            }
        }

        // 优先级3: 尝试加载 Logo
        if (icon == null) {
            icon = appInfo.loadLogo(pm);
        }

        // 优先级4: 加载普通应用图标
        if (icon == null) {
            icon = appInfo.loadIcon(pm);
        }

        return icon;
    }
    public static int normalizeColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        // 1️⃣ 降饱和（防止刺眼）
        hsv[1] = Math.min(hsv[1], 0.25f);

        // 2️⃣ 压亮度（电视非常重要）
        hsv[2] = Math.max(0.20f, Math.min(hsv[2], 0.35f));

        // 3️⃣ 轻微偏冷（高级感）
        hsv[0] = (hsv[0] + 210) % 360;

        return Color.HSVToColor(hsv);
    }
}
