package com.jayjd.boxtop.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class App {

    public static void startActivity(Context context, String action) {
        try {
            Intent intent = new Intent(action);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("TAG", "startActivity: ", e);
        }
    }


}
