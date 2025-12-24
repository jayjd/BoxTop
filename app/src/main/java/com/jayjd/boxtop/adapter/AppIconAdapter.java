package com.jayjd.boxtop.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.jayjd.boxtop.utils.PurchaseManager;
import com.jayjd.boxtop.utils.ToolUtils;

public class AppIconAdapter extends BaseQuickAdapter<AppInfo, QuickViewHolder> {

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.activity_icon_item, viewGroup);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable AppInfo appInfo) {
        if (appInfo != null) {
            MaterialCardView cardView = quickViewHolder.getView(R.id.card);
            cardView.setCardBackgroundColor(appInfo.getCardColor());
            // TODO: 2025/12/24 这里后续要在设置里面 进行设置后 判断是否付费 或者在软件设置的时候就判断是否pro
            boolean tvShow = PurchaseManager.getInstance().isPro();
            quickViewHolder.setText(R.id.tv_name, !tvShow ? appInfo.getName() : "");
            quickViewHolder.setGone(R.id.tv_name, tvShow);
            if (appInfo.getPackageName().isEmpty()) {
                quickViewHolder.setGone(R.id.app_icon, true);
                quickViewHolder.setGone(R.id.app_banner, true);
                quickViewHolder.setGone(R.id.iv_add, false);
                ImageView imageView = quickViewHolder.getView(R.id.iv_add);
                Glide.with(getContext()).load(appInfo.getAppBanner()).into(imageView);
            } else {
                quickViewHolder.setGone(R.id.iv_add, true);
                quickViewHolder.setGone(R.id.app_icon, appInfo.getIsBanner() == 1);
                quickViewHolder.setGone(R.id.app_banner, appInfo.getIsBanner() == 0);
                Drawable drawable;
                ImageView imageView;
                if (appInfo.getIsBanner() == 1) {
                    imageView = quickViewHolder.getView(R.id.app_banner);
                    drawable = ToolUtils.getBase64ToDrawable(appInfo.getAppBannerBase64());
                } else {
                    imageView = quickViewHolder.getView(R.id.app_icon);
                    drawable = ToolUtils.getBase64ToDrawable(appInfo.getAppIconBase64());
                }
                Glide.with(getContext()).load(drawable).into(imageView);

            }
        }
    }
}
