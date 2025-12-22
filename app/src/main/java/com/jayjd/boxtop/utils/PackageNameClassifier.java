package com.jayjd.boxtop.utils;

import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;

import com.jayjd.boxtop.entity.AppInfo;
import com.jayjd.boxtop.enums.AppType;

import java.util.HashMap;
import java.util.Map;

public final class PackageNameClassifier {

    private static final Map<AppType, String[]> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put(AppType.VIDEO, new String[]{"video", "tv", "player", "movie", "cinema"});

        KEYWORDS.put(AppType.LIVE, new String[]{"live", "iptv", "stream"});

        KEYWORDS.put(AppType.GAME, new String[]{"game", "play", "arena", "rpg", "fps"});

        KEYWORDS.put(AppType.MUSIC, new String[]{"music", "audio", "fm", "radio"});

        KEYWORDS.put(AppType.KIDS, new String[]{"kids", "child", "baby", "edu"});

        KEYWORDS.put(AppType.EDUCATION, new String[]{"learn", "study", "school", "edu"});

        KEYWORDS.put(AppType.SOCIAL, new String[]{"chat", "social", "message", "im"});

        KEYWORDS.put(AppType.TOOL, new String[]{"tool", "manager", "file", "setting"});

        KEYWORDS.put(AppType.SYSTEM, new String[]{"android", "system", "launcher", "settings"});
    }

    private PackageNameClassifier() {
    }

    public static AppType classify(ApplicationInfo applicationInfo, AppInfo appInfo) {
        if (applicationInfo == null) return AppType.UNKNOWN;
        if (appInfo == null) return AppType.UNKNOWN;

        if (appInfo.getIsSystem() == 1) return AppType.SYSTEM;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AppType appType = map(applicationInfo.category);
            if (appType != AppType.UNKNOWN) return appType;
        }
        String lower = appInfo.getPackageName().toLowerCase();

        for (Map.Entry<AppType, String[]> entry : KEYWORDS.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (lower.contains(keyword)) {
                    return entry.getKey();
                }
            }
        }
        return AppType.UNKNOWN;
    }

    public static AppType map(int category) {
        Log.d("TAG", "map: " + category);
        return switch (category) {
            case ApplicationInfo.CATEGORY_GAME -> AppType.GAME;
            case ApplicationInfo.CATEGORY_AUDIO ->
                // 音乐、电台、播客
                    AppType.MUSIC;
            case ApplicationInfo.CATEGORY_VIDEO ->
                // 影视、短视频、点播
                    AppType.VIDEO;
            case ApplicationInfo.CATEGORY_IMAGE ->
                // 相册、图片浏览
                    AppType.TOOL;
            case ApplicationInfo.CATEGORY_SOCIAL -> AppType.SOCIAL;
            case ApplicationInfo.CATEGORY_NEWS ->
                // TV 上新闻 ≈ 视频内容
                    AppType.VIDEO;
            case ApplicationInfo.CATEGORY_MAPS ->
                // 地图 / 出行，在 TV 上是功能
                    AppType.TOOL;
            case ApplicationInfo.CATEGORY_PRODUCTIVITY ->
                // 文件管理 / 设置 / 管理类
                    AppType.TOOL;
            default -> AppType.UNKNOWN;
        };
    }
}
