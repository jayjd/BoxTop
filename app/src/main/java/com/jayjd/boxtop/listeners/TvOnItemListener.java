package com.jayjd.boxtop.listeners;

import android.view.View;
import android.widget.TextView;

import com.jayjd.boxtop.R;
import com.jayjd.boxtop.utils.animation.AnimationFactory;
import com.jayjd.boxtop.utils.animation.ItemAnimation;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

public class TvOnItemListener implements TvRecyclerView.OnItemListener {

    private final ItemAnimation animation = AnimationFactory.get();
    @Override
    public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
        animation.end(itemView);
    }

    @Override
    public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
        TextView textView = itemView.findViewById(R.id.tv_name);
        if (textView != null) textView.setSelected(true);
        TextView categoryName = itemView.findViewById(R.id.category_name);
        if (categoryName != null) categoryName.setSelected(true);
        animation.start(itemView);
    }

    @Override
    public void onItemClick(TvRecyclerView parent, View itemView, int position) {

    }
}
