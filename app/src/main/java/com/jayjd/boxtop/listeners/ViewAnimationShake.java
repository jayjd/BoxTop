package com.jayjd.boxtop.listeners;

import android.content.Context;
import android.view.FocusFinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.RecyclerView;

import com.jayjd.boxtop.R;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

public class ViewAnimationShake implements TvRecyclerView.OnInBorderKeyEventListener {
    private final TvRecyclerView tvRecyclerView;
    private final Context context;
    private final int gridType;
    private final ViewAnimateListener viewAnimateListener;
    private Animation mShakeX;

    public ViewAnimationShake(TvRecyclerView tvRecyclerView, Context context, int gridType, ViewAnimateListener viewAnimateListener) {
        this.tvRecyclerView = tvRecyclerView;
        this.context = context;
        this.gridType = gridType;
        this.viewAnimateListener = viewAnimateListener;
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
            case View.FOCUS_UP:
                return viewAnimateListener.animateType(View.FOCUS_UP, gridType);
            case View.FOCUS_DOWN:
                return viewAnimateListener.animateType(View.FOCUS_DOWN, gridType);
            case View.FOCUS_LEFT:
            case View.FOCUS_RIGHT:
                boolean handled = false;
                View nextFocused = FocusFinder.getInstance().findNextFocus(tvRecyclerView, focused, direction);
                if (nextFocused == null || nextFocused == focused) {
                    shakeX(focused);
                    handled = true;
                }
                return handled;
        }
        return false;
    }
}
