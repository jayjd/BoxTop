package com.jayjd.boxtop.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.palette.graphics.Palette;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.jayjd.boxtop.entity.AppInfo;
import com.jayjd.boxtop.enums.AppType;

import java.util.ArrayList;
import java.util.List;

public class AppsUtils {

    /**
     * 获取所有应用列表
     *
     * @param context 上下文
     * @return 返回所有应用列表
     */
    public static List<AppInfo> getAppsInfo(Context context) {

        PackageManager packageManager = context.getPackageManager();

        // 1. 获取所有安装的应用列表（只包含基本信息）
        // 标志位 0 表示获取基本信息，可以减少查询时间。
        // 对于 PackageInfo 标志位应使用 PackageManager.GET_META_DATA
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(0);

        // 存储最终结果的列表
        List<AppInfo> appInfoList = new ArrayList<>();
        for (ApplicationInfo app : applications) {
            if (app == null) continue;
            if (app.packageName.equals(context.getPackageName())) continue;
            AppInfo info = getAppInfo(context, app.packageName);
            if (info == null) continue;
            appInfoList.add(info);
        }
        return appInfoList;
    }

    public static AppInfo getAppInfo(Context context, String pkg) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo app = packageManager.getApplicationInfo(pkg, PackageManager.GET_META_DATA);
            AppInfo info = new AppInfo();

            // --- A. 直接从 ApplicationInfo 获取的属性 ---
            info.setPackageName(app.packageName);
            // packagePath (源文件路径)
            info.setPackagePath(app.sourceDir);

            // name (应用名/标签名)
            info.setName(app.loadLabel(packageManager).toString());

            // icon (图标)
            info.setAppIcon(app.loadIcon(packageManager));
            // isSystem (判断是否是系统应用)
            // ApplicationInfo.FLAG_SYSTEM 用于标记系统应用 1 是 0 否
            info.setIsSystem((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0 ? 1 : 0);


            // --- B. 需要通过 PackageInfo 获取的属性 ---
            try {
                // 通过包名获取 PackageInfo，需要包含版本和配置信息
                PackageInfo packageInfo = packageManager.getPackageInfo(app.packageName, PackageManager.GET_PERMISSIONS | PackageManager.GET_CONFIGURATIONS);

                // versionName
                info.setVersionName(packageInfo.versionName);

                // versionCode
                // 注意：对于较旧的 Android 版本，使用 packageInfo.versionCode
                // 对于 API 28 及以上，应使用 packageInfo.getLongVersionCode()
                info.setVersionCode(packageInfo.versionCode);

                // minSdkVersion 和 targetSdkVersion
                // 这些信息位于 ApplicationInfo 的结构中，但通常从 PackageInfo 获取更完整
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    info.setMinSdkVersion(app.minSdkVersion); // 需要 API 24 (Nougat) 或以上
                } else {
                    info.setMinSdkVersion(0);
                }
                info.setTargetSdkVersion(app.targetSdkVersion);
                AppType classify = PackageNameClassifier.classify(app, info);
                info.setAppType(classify);
                configAppIcon(context, info);
                return info;
            } catch (Throwable ignored) {
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return null;
    }

    public static void configAppIcon(Context context, AppInfo appInfo) {
        Drawable banner = ToolUtils.getTvAppIcon(context, appInfo.getPackageName());
        if (banner != null) {
            byte[] bytes = ConvertUtils.drawable2Bytes(banner);
            String bannerStr = EncodeUtils.base64Encode2String(bytes);
            appInfo.setAppBannerBase64(bannerStr);
            appInfo.setAppBanner(banner);
            appInfo.setBanner(true);
        } else {
            if (appInfo.getAppIcon() != null) {
                byte[] bytes = ConvertUtils.drawable2Bytes(appInfo.getAppIcon());
                String iconStr = EncodeUtils.base64Encode2String(bytes);
                appInfo.setAppIconBase64(iconStr);
                Bitmap bitmap = ConvertUtils.drawable2Bitmap(appInfo.getAppIcon());
                if (bitmap == null) return;
                Palette generate = Palette.from(bitmap).generate();
                Palette.Swatch dominantSwatch = generate.getDominantSwatch();
                if (dominantSwatch != null) {
                    int normalizeColor = ToolUtils.normalizeBold(dominantSwatch.getRgb());
                    appInfo.setCardColor(normalizeColor);
                } else {
                    appInfo.setCardColor(Color.parseColor("#263238"));
                }
            } else {
                appInfo.setCardColor(Color.parseColor("#263238"));
            }
        }
    }
}
