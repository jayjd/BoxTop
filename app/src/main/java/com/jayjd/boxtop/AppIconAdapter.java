package com.jayjd.boxtop;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;
import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;

public class AppIconAdapter extends BaseQuickAdapter<AppUtils.AppInfo, QuickViewHolder> {

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.activity_icon_item, viewGroup);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable AppUtils.AppInfo appInfo) {
        if (appInfo != null) {
            quickViewHolder.setImageDrawable(R.id.iv_icon, appInfo.getIcon());
            quickViewHolder.setText(R.id.tv_name, appInfo.getName());
        }
    }
}
