package com.jayjd.boxtop.cards;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jayjd.boxtop.utils.PurchaseManager;

public abstract class BaseCardFragment extends Fragment {

    private static final long DEBOUNCE_TIME = 300; // 防抖时间(ms)
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isVisibleToUser = false;
    private boolean isDebouncing = false;
    private final Runnable debounceRunnable = () -> isDebouncing = false;
    protected boolean isPro;
    protected Context appContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        appContext = context.getApplicationContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        appContext = null;
    }
    /* ---------------- 生命周期统一入口 ---------------- */

    @Override
    public void onResume() {
        super.onResume();
        if (!isAdded() || getView() == null) return;
        isPro = PurchaseManager.getInstance().isPro();
        onProStateChanged(isPro);
        tryDispatchVisible(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isAdded() || getView() == null) return;
        tryDispatchVisible(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isAdded() || getView() == null) return;
        tryDispatchVisible(!hidden);
    }

    /* ---------------- 核心分发逻辑 ---------------- */

    private void tryDispatchVisible(boolean visible) {
        if (visible == isVisibleToUser) return;

        if (isDebouncing) return;

        isDebouncing = true;
        handler.removeCallbacks(debounceRunnable);
        handler.postDelayed(debounceRunnable, DEBOUNCE_TIME);

        isVisibleToUser = visible;

        if (visible) {
            onFragmentVisible();
        } else {
            onFragmentInvisible();
        }
    }

    /* ---------------- 给子类用的回调 ---------------- */

    /**
     * Fragment 真正对用户可见（推荐在这里开始刷新 / 注册广播）
     */
    protected void onFragmentVisible() {
    }

    /**
     * Fragment 从用户视野消失（推荐在这里停止刷新 / 反注册）
     */
    protected void onFragmentInvisible() {
    }

    public void requestDefaultFocus() {

    }

    protected int getProgressDrawable(int percent) {
        int color;
        if (percent < 70) color = 0xFF4CAF50;
        else if (percent < 85) color = 0xFFFF9800;
        else color = 0xFFF44336;
        return color;
    }

    protected abstract void onProStateChanged(boolean isPro);
}
