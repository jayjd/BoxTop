package com.jayjd.boxtop.cards.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.jayjd.boxtop.R;
import com.jayjd.boxtop.entity.DeviceInfo;

public class DeviceInfoAdapter extends BaseQuickAdapter<DeviceInfo, QuickViewHolder> {

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.item_card_device, viewGroup);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable DeviceInfo deviceInfo) {
        if (deviceInfo == null) return;

        quickViewHolder.setImageResource(R.id.iv_icon, deviceInfo.getIcon());
        quickViewHolder.setText(R.id.tv_title, deviceInfo.getName());
        quickViewHolder.setText(R.id.tv_status, deviceInfo.getStatus());

        // 关键：信息卡 item 永远不抢焦点
        quickViewHolder.itemView.setFocusable(false);
        quickViewHolder.itemView.setClickable(false);
    }
}
