package com.jayjd.boxtop;

import android.view.View;

import com.jayjd.boxtop.utils.ToolUtils;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

public class TvOnItemListener implements TvRecyclerView.OnItemListener {
    @Override
    public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
        ToolUtils.endAnimation(itemView);
    }

    @Override
    public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
        ToolUtils.startAnimation(itemView);
    }

    @Override
    public void onItemClick(TvRecyclerView parent, View itemView, int position) {

    }
}
