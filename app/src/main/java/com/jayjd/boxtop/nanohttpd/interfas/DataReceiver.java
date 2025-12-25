package com.jayjd.boxtop.nanohttpd.interfas;

public interface DataReceiver {
    void onDownloadApk(String type, String url);

    void onDownloadWallpaper(String type, String url);

    void onLocalInstallApk(String absoluteFile);

    void onLocalWallpaper(String absolutePath);
}