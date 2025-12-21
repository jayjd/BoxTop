package com.jayjd.boxtop.utils;

import android.view.View;
import android.widget.LinearLayout;

import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.SizeUtils;
import com.jayjd.boxtop.R;

public class DotContainerUtils {
    public static void setupDots(LinearLayout dotContainer, int count) {
        dotContainer.removeAllViews();

        for (int i = 0; i < count; i++) {
            View dot = new View(dotContainer.getContext());

            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(
                            SizeUtils.dp2px(10f),
                            SizeUtils.dp2px(10f)
                    );
            lp.setMargins(6, 0, 6, 0);
            dot.setLayoutParams(lp);
            dot.setBackgroundResource(
                    i == 0 ? R.drawable.dot_selected : R.drawable.dot_normal
            );
            dotContainer.addView(dot);
        }
    }

    public static void bindViewPager(ViewPager2 viewPager, LinearLayout dotContainer, int pageCount) {

        setupDots(dotContainer, pageCount);

        viewPager.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        updateDots(dotContainer,position);
                    }
                }
        );
    }

    public static void updateDots(LinearLayout dotContainer, int selected) {

        for (int i = 0; i < dotContainer.getChildCount(); i++) {
            View dot = dotContainer.getChildAt(i);
            dot.setBackgroundResource(
                    i == selected
                            ? R.drawable.dot_selected
                            : R.drawable.dot_normal
            );
        }
    }
}
