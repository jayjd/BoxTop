package com.jayjd.boxtop.utils.animation;

import android.view.View;

import com.jayjd.boxtop.utils.ToolUtils;

public class NormalItemAnimation implements ItemAnimation {

    @Override
    public void start(View view) {
        ToolUtils.startNormalAnimation(view);
    }

    @Override
    public void end(View view) {
        ToolUtils.endNormalAnimation(view);
    }
}