package com.jayjd.boxtop.listeners;

import android.content.Context;
import android.view.FocusFinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.RecyclerView;

import com.jayjd.boxtop.R;
import com.jayjd.boxtop.utils.App;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

public class ViewAnimationShake implements TvRecyclerView.OnInBorderKeyEventListener {
    private final TvRecyclerView tvRecyclerView;
    private final Context context;
    private Animation mShakeX;

    public ViewAnimationShake(TvRecyclerView tvRecyclerView, Context context) {
        this.tvRecyclerView = tvRecyclerView;
        this.context = context;
    }


    private void shakeX(View currentFocused) {
        if (currentFocused != null && tvRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
            if (mShakeX == null) {
                mShakeX = AnimationUtils.loadAnimation(context, R.anim.host_shake);
            }
            currentFocused.clearAnimation();
            currentFocused.startAnimation(mShakeX);
        }
    }

    @Override
    public boolean onInBorderKeyEvent(int direction, View focused) {
        switch (direction) {
            case View.FOCUS_LEFT:
            case View.FOCUS_RIGHT:
                boolean handled = false;
                View nextFocused = FocusFinder.getInstance().findNextFocus(tvRecyclerView, focused, direction);
                if (nextFocused == null || nextFocused == focused) {
                    if (App.isPaid(context)) {
                        shakeX(focused);
                    }
                    handled = true;
                }
                return handled;
        }
        return false;
    }
}
