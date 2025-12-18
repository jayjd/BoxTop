package com.jayjd.boxtop.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.jayjd.boxtop.R;
import com.jayjd.boxtop.enums.PreviewSettings;

public class PreviewSettingsAdapter extends BaseQuickAdapter<PreviewSettings, QuickViewHolder> {
    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.item_preview_settings, viewGroup);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable PreviewSettings previewSettings) {
        if (previewSettings == null) return;
        quickViewHolder.setText(R.id.tv_preview_setting, previewSettings.getDisplayName());
    }
}
