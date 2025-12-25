package com.jayjd.boxtop.settings.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.jayjd.boxtop.R;
import com.jayjd.boxtop.enums.AllSettings;

public class SettingsAdapter extends BaseQuickAdapter<AllSettings, QuickViewHolder> {
    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.activity_settings_item, viewGroup);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable AllSettings allSettings) {
        if (allSettings == null) return;
        quickViewHolder.setText(R.id.tv_function_name, allSettings.getFunctionName());
        Class<?> booleanClass = allSettings.getBooleanClass();
        if (booleanClass == Boolean.class) {
            quickViewHolder.getView(R.id.sw_function).setVisibility(View.VISIBLE);
            quickViewHolder.getView(R.id.intent_function).setVisibility(View.INVISIBLE);
        } else if (booleanClass == Intent.class) {
            quickViewHolder.getView(R.id.intent_function).setVisibility(View.VISIBLE);
            quickViewHolder.getView(R.id.sw_function).setVisibility(View.INVISIBLE);
        }
    }
}
