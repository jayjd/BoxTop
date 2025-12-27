# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


########################################
# 1️⃣ 异常定位（最重要）
########################################

# 保留行号（没有这个，堆栈没法定位到代码）
-keepattributes SourceFile,LineNumberTable

# 保留异常信息
-keepattributes Exceptions

# 保留泛型信息（Room / Gson 需要）
-keepattributes Signature

########################################
# 2️⃣ 保留你自己的代码（可混淆但不删）
########################################

-keep,allowobfuscation class com.jayjd.boxtop.** { *; }
-keep class com.jayjd.boxtop.entity.** { *; }
-keep class com.jayjd.boxtop.cards.entity.** { *; }

# 保留 StringFog 实现类及接口方法
-keep class com.jayjd.stringfogcustom.AdvanceStringFogImpl { *; }

########################################
# 3️⃣ Android TV / AppCompat / Material
########################################

-keep class androidx.** { *; }
-dontwarn androidx.**

########################################
# 4️⃣ Gson（JSON 反射必须保留）
########################################

-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

########################################
# 5️⃣ Room 数据库（必须）
########################################

-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-keep @androidx.room.Database class *

########################################
# 6️⃣ Glide（图片加载）
########################################

-keep class com.bumptech.glide.** { *; }
-dontwarn com.bumptech.glide.**

########################################
# 7️⃣ OkGo + OkHttp
########################################

-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }

########################################
# 8️⃣ NanoHTTPD（局域网服务器）
########################################

-keep class fi.iki.elonen.** { *; }
-dontwarn fi.iki.elonen.**

########################################
# 9️⃣ ZXing（二维码）
########################################

-keep class com.google.zxing.** { *; }

########################################
# 🔟 Lombok（编译期，运行不需要）
########################################

-dontwarn lombok.**

########################################
# 11️⃣ 日志（Release 可选）
########################################

# 如果你想保留 Log（调试方便）
-keep class android.util.Log { *; }

########################################
# 12️⃣ 避免误删（兜底）
########################################

-dontwarn **
