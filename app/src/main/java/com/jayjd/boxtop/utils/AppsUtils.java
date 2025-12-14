package com.jayjd.boxtop.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.jayjd.boxtop.entity.AppInfo;

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

        // 2. 遍历应用列表，获取详细信息并填充 AppInfo 对象
        for (ApplicationInfo app : applications) {

            // 排除应用信息为空的情况
            if (app == null) continue;

            AppInfo info = new AppInfo();

            // --- A. 直接从 ApplicationInfo 获取的属性 ---
            info.setPackageName(app.packageName);
            // packagePath (源文件路径)
            info.setPackagePath(app.sourceDir);

            // name (应用名/标签名)
            info.setName(app.loadLabel(packageManager).toString());

            // icon (图标)
            info.setIcon(app.loadIcon(packageManager));

            // isSystem (判断是否是系统应用)
            // ApplicationInfo.FLAG_SYSTEM 用于标记系统应用
            info.setSystem((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0);


            // --- B. 需要通过 PackageInfo 获取的属性 ---
            try {
                // 通过包名获取 PackageInfo，需要包含版本和配置信息
                PackageInfo packageInfo = packageManager.getPackageInfo(
                        app.packageName,
                        PackageManager.GET_PERMISSIONS | PackageManager.GET_CONFIGURATIONS
                );

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

            } catch (Exception e) {
                e.printStackTrace();
                // 如果找不到包信息（极少发生，通常是包被卸载或查询时出现权限问题）
                continue; // 跳过这个应用
            } catch (Throwable e) {
                e.printStackTrace();
                continue;
            }
            // 3. 将完整的 AppInfo 对象添加到列表中
            appInfoList.add(info);
        }
        return appInfoList;
    }
}
