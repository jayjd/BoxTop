package com.jayjd.boxtop.cards.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.jayjd.boxtop.R;
import com.jayjd.boxtop.entity.AppInfo;
import com.jayjd.boxtop.utils.ToolUtils;

public class AppUsageRankAdapter extends BaseQuickAdapter<AppInfo, QuickViewHolder> {
    int maxCount = 0;

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.item_app_rank, viewGroup);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int position, @Nullable AppInfo appInfo) {
        if (appInfo == null) return;
        if (position == 0) {
            maxCount = appInfo.getOpenAppCount();
        }
        quickViewHolder.setText(R.id.tv_app_name, appInfo.getName());
        if (position == 0) {
            quickViewHolder.setBackgroundResource(R.id.tv_rank, R.drawable.bg_rank_first);
            quickViewHolder.setTextColor(R.id.tv_rank, R.color.md_theme_inverseOnSurface_highContrast);
        } else if (position == 1) {
            quickViewHolder.setBackgroundResource(R.id.tv_rank, R.drawable.bg_rank_second);
            quickViewHolder.setTextColor(R.id.tv_rank, R.color.md_theme_inverseOnSurface_highContrast);
        } else if (position == 2) {
            quickViewHolder.setBackgroundResource(R.id.tv_rank, R.drawable.bg_rank_third);
            quickViewHolder.setTextColor(R.id.tv_rank, R.color.md_theme_inverseOnSurface_highContrast);
        }
        quickViewHolder.setText(R.id.tv_rank, String.valueOf(position + 1));

        ImageView ivIcon = quickViewHolder.getView(R.id.iv_icon);
        Drawable drawable;
        if (appInfo.isBanner()) {
            drawable = ToolUtils.getBase64ToDrawable(appInfo.getAppBannerBase64());
        } else {
            drawable = ToolUtils.getBase64ToDrawable(appInfo.getAppIconBase64());
        }
        Glide.with(quickViewHolder.itemView).load(drawable).into(ivIcon);
        quickViewHolder.setText(R.id.tv_count, String.valueOf(appInfo.getOpenAppCount()));


        View bar = quickViewHolder.getView(R.id.view_bar);

        int maxWidth = dp(quickViewHolder.itemView, 120);
        int width = (int) (maxWidth * (appInfo.getOpenAppCount() * 1f / maxCount));

        ViewGroup.LayoutParams lp = bar.getLayoutParams();
        lp.width = Math.max(width, dp(quickViewHolder.itemView, 6));
        bar.setLayoutParams(lp);

        // Top3 特殊颜色
        if (position == 0) {
            bar.setBackgroundResource(R.drawable.bg_rank_bar_gold);
        } else if (position == 1) {
            bar.setBackgroundResource(R.drawable.bg_rank_bar_silver);
        } else if (position == 2) {
            bar.setBackgroundResource(R.drawable.bg_rank_bar_bronze);
        } else {
            bar.setBackgroundResource(R.drawable.bg_rank_bar);
        }

    }

    private int dp(View v, int dp) {
        return (int) (dp * v.getResources().getDisplayMetrics().density);
    }
}
