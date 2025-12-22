package com.jayjd.boxtop.cards;

import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.Fragment;

public abstract class BaseCardFragment extends Fragment {

    private static final long DEBOUNCE_TIME = 300; // 防抖时间(ms)
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isVisibleToUser = false;
    private boolean isDebouncing = false;
    private final Runnable debounceRunnable = () -> isDebouncing = false;

    /* ---------------- 生命周期统一入口 ---------------- */

    @Override
    public void onResume() {
        super.onResume();
        tryDispatchVisible(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        tryDispatchVisible(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
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
    protected void onFragmentVisible() {}

    /**
     * Fragment 从用户视野消失（推荐在这里停止刷新 / 反注册）
     */
    protected void onFragmentInvisible() {}
}
