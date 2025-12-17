package com.jayjd.boxtop.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
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
import android.provider.Settings;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.jayjd.boxtop.entity.AppInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class ToolUtils {
    public static void startAnimation(View view) {
        view.animate().scaleX(1.1f).scaleY(1.1f).setDuration(500) // 适当延长动画时间
                .setInterpolator(new BounceInterpolator()) // 使用OvershootInterpolator
                .start();
    }

    public static void endAnimation(View view) {
        view.animate().scaleX(1f).scaleY(1f).setDuration(500) // 适当延长动画时间
                .setInterpolator(new BounceInterpolator()) // 使用OvershootInterpolator
                .start();
    }

    public static byte[] desDecrypt(byte[] encryptText, String desKeyParameter) {
        try {
            SecureRandom sr = new SecureRandom();
            byte[] rawKeyData = desKeyParameter.getBytes();
            DESKeySpec dks = new DESKeySpec(rawKeyData);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key, sr);
            return cipher.doFinal(encryptText);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException |
                 NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            return "".getBytes();
        }
    }

//    public static String base64ToString(String entity) {
//        byte[] bytes;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            bytes = desDecrypt(Base64.getDecoder().decode(entity), JNIUtils.getDecodePwd());
//        } else {
//            bytes = desDecrypt(android.util.Base64.decode(entity, android.util.Base64.DEFAULT), JNIUtils.getDecodePwd());
//        }
//        return new String(bytes);
//    }

    public static String calculateApkHash(Context context) {
        try {
            String apkPath = context.getApplicationContext().getPackageCodePath();

            // 创建 SHA-256 的 MessageDigest 实例
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            InputStream is = new FileInputStream(new File(apkPath));
            byte[] buffer = new byte[1024];
            int read;

            // 读取文件并更新 MessageDigest
            while ((read = is.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }

            is.close();

            // 获取哈希值
            byte[] hashBytes = digest.digest();

            // 将哈希值转换为十六进制字符串
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hashString.append('0');
                hashString.append(hex);
            }

            return hashString.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Drawable getBase64ToDrawable(String iconBase64) {
        byte[] bytes = EncodeUtils.base64Decode(iconBase64);
        return ConvertUtils.bytes2Drawable(bytes);
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
            AppUtils.uninstallApp(packageName);
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

    public static AppInfo getEmptyAppInfo(String name, Drawable drawable, int cardColor) {
        AppInfo appInfo = new AppInfo();
        appInfo.setName(name);
        appInfo.setPackageName("");
        appInfo.setAppBanner(drawable);
        if (cardColor != 0) {
            appInfo.setCardColor(cardColor);
        }
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
//        if (icon == null) {
//            icon = appInfo.loadIcon(pm);
//        }

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

    /**
     * 跳转到系统设置中更改 Home 默认应用的界面。
     * 兼容 Android 5.0 及以上设备。
     */
    public static void goToHomeSettings(Activity activity) {
        Intent intent;

        // 优先级 1: 尝试直接跳转到 "Home 应用设置" (Settings.ACTION_HOME_SETTINGS)
        // 这是 Android 官方 API，在许多原生和轻定制系统中有效。
        try {
            intent = new Intent(Settings.ACTION_HOME_SETTINGS);
            activity.startActivity(intent);
            Toast.makeText(activity, "请选择您的桌面应用", Toast.LENGTH_LONG).show();
            return;
        } catch (ActivityNotFoundException e1) {
            // Log.e("Launcher", "ACTION_HOME_SETTINGS not found", e1);
        }

        // 优先级 2: 尝试跳转到“默认应用管理”界面 (Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
        // 此 Intent 在 Android 6.0 (API 23) 及以上版本中更常见，但作为回退选项兼容性更好。
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent = new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
                activity.startActivity(intent);
                Toast.makeText(activity, "请在默认应用设置中更改桌面", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (ActivityNotFoundException e2) {
            // Log.e("Launcher", "ACTION_MANAGE_DEFAULT_APPS_SETTINGS not found", e2);
        }

        // 优先级 3 (最后的通用回退): 跳转到主设置界面
        // 如果前两种都失败，让用户自己去寻找。
        try {
            intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivity(intent);
            Toast.makeText(activity, "请手动进入设置中查找并更改默认桌面", Toast.LENGTH_LONG).show();
        } catch (ActivityNotFoundException e3) {
            Toast.makeText(activity, "无法打开系统设置", Toast.LENGTH_SHORT).show();
        }
    }

    public static int normalizeBold(int color) {
        if (Color.alpha(color) < 255) {
            return Color.parseColor("#444444");
        }

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        // ✅ 保留主色调（不动 H）
        // hsv[0] 不动

        // ✅ 饱和度：只稍微收一点
        hsv[1] = Math.min(hsv[1], 0.75f);

        // ✅ 亮度：电视防发白
        if (hsv[2] > 0.85f) {
            hsv[2] = 0.85f;
        } else if (hsv[2] < 0.35f) {
            hsv[2] = 0.35f;
        }

        return Color.HSVToColor(255, hsv);
    }

    public static int normalizeForBackground(int color) {
        if (Color.alpha(color) < 255) {
            return Color.parseColor("#263238");
        }

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        // 降饱和，防止刺眼
        hsv[1] = Math.min(hsv[1], 0.30f);

        // 控亮度，电视非常重要
        hsv[2] = Math.max(0.22f, Math.min(hsv[2], 0.38f));

        return Color.HSVToColor(255, hsv);
    }

}
