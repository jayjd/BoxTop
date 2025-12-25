package com.jayjd.boxtop.listeners;

import android.view.View;

import com.jayjd.boxtop.utils.ToolUtils;

public class ViewFocusListener implements View.OnFocusChangeListener {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            ToolUtils.startWaveAnimation(v);
        } else {
            ToolUtils.endWaveAnimation(v);
        }
    }
}
