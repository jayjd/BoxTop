package com.jayjd.boxtop.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.google.android.material.card.MaterialCardView;
import com.jayjd.boxtop.R;
import com.jayjd.boxtop.entity.AppInfo;

public class AppIconAdapter extends BaseQuickAdapter<AppInfo, QuickViewHolder> {

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.activity_icon_item, viewGroup);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable AppInfo appInfo) {
        if (appInfo != null) {
            if (appInfo.getPackageName().isEmpty()) {
                quickViewHolder.setGone(R.id.iv_icon, true);
                quickViewHolder.setGone(R.id.iv_add, false);
                if (appInfo.getName().equals("system")) {
                    quickViewHolder.setGone(R.id.tv_name, false);
                    quickViewHolder.setImageResource(R.id.iv_add, R.drawable.ic_apps_24dp);
                    quickViewHolder.setText(R.id.tv_name, "系统应用");
                } else {
                    quickViewHolder.setVisible(R.id.tv_name, false);
                    quickViewHolder.setImageResource(R.id.iv_add, R.drawable.ic_add_24dp);
                }
            } else {
                quickViewHolder.setGone(R.id.iv_icon, false);
                quickViewHolder.setGone(R.id.tv_name, false);
                MaterialCardView cardView = quickViewHolder.getView(R.id.card);
                cardView.setCardBackgroundColor(appInfo.getCardColor());
                ImageView imageView = quickViewHolder.getView(R.id.iv_icon);
                Glide.with(getContext()).load(appInfo.getIcon()).into(imageView);
                quickViewHolder.setText(R.id.tv_name, appInfo.getName());
            }
        }
    }
}
