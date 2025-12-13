package com.jayjd.boxtop;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.jayjd.boxtop.enums.TopSettingsIcons;

public class SettingsIconAdapter extends BaseQuickAdapter<TopSettingsIcons, QuickViewHolder> {
    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.activity_settings_icon_item, viewGroup);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable TopSettingsIcons topSettingsIcons) {
        if (topSettingsIcons != null)
            quickViewHolder.setImageResource(R.id.iv_icon, topSettingsIcons.getIconId());
    }
}
