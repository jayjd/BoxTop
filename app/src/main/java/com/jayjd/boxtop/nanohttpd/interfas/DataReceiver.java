package com.jayjd.boxtop.nanohttpd.interfas;

import android.content.Context;

public interface DataReceiver {
    void onDouYuPush(String word);

    void onHDKPush(String word);

    void onTvLivePush(String word);

    void onFanLivePush(String word);

    void onCookiePush(String word);

    void onInstallApk(Context context, String absoluteFile, String tmpFileItem);

}