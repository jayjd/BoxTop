package com.jayjd.boxtop.utils.animation;

import com.jayjd.boxtop.utils.PurchaseManager;

public class AnimationFactory {

    private static final ItemAnimation NORMAL = new NormalItemAnimation();
    private static final ItemAnimation PRO = new ProItemAnimation();

    public static ItemAnimation get() {
        return PurchaseManager.getInstance().isPro()
                ? PRO
                : NORMAL;
    }
}
