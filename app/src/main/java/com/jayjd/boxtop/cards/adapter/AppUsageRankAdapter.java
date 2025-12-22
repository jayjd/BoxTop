package com.jayjd.boxtop.cards.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.jayjd.boxtop.R;
import com.jayjd.boxtop.entity.AppInfo;
import com.jayjd.boxtop.utils.ToolUtils;

public class AppUsageRankAdapter extends BaseQuickAdapter<AppInfo, QuickViewHolder> {
    long maxCount = 0;

    private int itemHeight = 0; // 新增字段

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        QuickViewHolder quickViewHolder = new QuickViewHolder(R.layout.item_app_rank, viewGroup);
        if (itemHeight > 0) {
            ViewGroup.LayoutParams lp = quickViewHolder.itemView.getLayoutParams();
            if (lp == null) lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
            else lp.height = itemHeight;
            quickViewHolder.itemView.setLayoutParams(lp);
        }
        return quickViewHolder;
    }

    private int dp(View v, int dp) {
        return (int) (dp * v.getResources().getDisplayMetrics().density);
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
        if (appInfo.getIsBanner() == 1) {
            drawable = ToolUtils.getBase64ToDrawable(appInfo.getAppBannerBase64());
        } else {
            drawable = ToolUtils.getBase64ToDrawable(appInfo.getAppIconBase64());
        }
        Glide.with(quickViewHolder.itemView).load(drawable).into(ivIcon);

        String displayText;
        long count = appInfo.getOpenAppCount();
        if (count >= 1_000_000) {
            displayText = String.format("使用:%.1fM", count / 1_000_000f);
        } else if (count >= 1_000) {
            displayText = String.format("使用:%.1fk", count / 1_000f);
        } else {
            displayText = String.format("使用:%d次", count);
        }
        quickViewHolder.setText(R.id.tv_count, displayText);

        View bar = quickViewHolder.getView(R.id.view_bar);

        int maxWidth = dp(quickViewHolder.itemView, 100);
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

    public void setItemHeight(int height) {
        this.itemHeight = height;
        notifyDataSetChanged();
    }
}
