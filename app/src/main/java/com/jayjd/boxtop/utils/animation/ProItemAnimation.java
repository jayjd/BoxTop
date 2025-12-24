package com.jayjd.boxtop.utils.animation;

import android.view.View;

import com.jayjd.boxtop.utils.ToolUtils;

public class ProItemAnimation implements ItemAnimation {

    @Override
    public void start(View view) {
        ToolUtils.startWaveAnimation(view);   // 波形 / 呼吸 / 高级特效
    }

    @Override
    public void end(View view) {
        ToolUtils.endWaveAnimation(view);
    }
}
