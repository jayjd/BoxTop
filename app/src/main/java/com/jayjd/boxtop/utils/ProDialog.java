package com.jayjd.boxtop.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jayjd.boxtop.R;
import com.jayjd.boxtop.listeners.ViewFocusListener;

public class ProDialog {

    public static void show(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pro, null);
        Dialog dialog = new Dialog(context, R.style.CustomDialogTheme);
        dialog.setContentView(view);
        dialog.show();
        TextView unlockBtn = view.findViewById(R.id.pro_btn_unlock);
        TextView unlockCloseBtn = view.findViewById(R.id.pro_btn_unlock_close);
        unlockCloseBtn.setOnFocusChangeListener(new ViewFocusListener());
        unlockCloseBtn.setOnClickListener(v -> dialog.dismiss());

        unlockBtn.setOnFocusChangeListener(new ViewFocusListener());
        unlockBtn.setOnClickListener(v -> {
            dialog.dismiss();
            // TODO: 打开购买/解锁流程
            boolean b = PurchaseManager.getInstance().unlockWithCode(context, "123123123");
            if (b) {
                Toast.makeText(context, "解锁成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "解锁失败", Toast.LENGTH_SHORT).show();
            }
        });
        // TV 聚焦优化
        unlockBtn.requestFocus();

    }

    // dp 转 px
    private static int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
