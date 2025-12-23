package com.jayjd.boxtop.wallpaper;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jayjd.boxtop.R;
import com.jayjd.boxtop.adapter.InfoCardPagerAdapter;
import com.jayjd.boxtop.cards.BaseCardFragment;
import com.jayjd.boxtop.nanohttpd.ControlManager;

import java.util.ArrayList;
import java.util.List;

public class WallpaperActivity extends AppCompatActivity {
    TabLayout wallPaperTabLayout;
    ViewPager2 wallPaperViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wallpaper);
        initView();
        initData();
        bindTabWithViewPage();
    }

    private void bindTabWithViewPage() {
        new TabLayoutMediator(wallPaperTabLayout, wallPaperViewpager, (tab, position) -> {
            switch (position) {
                case 0 -> tab.setText("本地壁纸");
                case 1 -> tab.setText("内置壁纸");
            }
        }).attach();

        hookTabKeyDown(wallPaperTabLayout);

        wallPaperViewpager.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        Fragment fragment =
                                getSupportFragmentManager()
                                        .findFragmentByTag("f" + position);

                        if (fragment instanceof BaseCardFragment) {
                            ((BaseCardFragment) fragment).requestDefaultFocus();
                        }
                    }
                });
    }

    private void hookTabKeyDown(TabLayout tabLayout) {
        LinearLayout tabStrip = (LinearLayout) tabLayout.getChildAt(0);

        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            View tabView = tabStrip.getChildAt(i);

            tabView.setFocusable(true);
            tabView.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return false;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    // 把焦点交给 ViewPager2 里的 RecyclerView
                    ViewPager2 vp = findViewById(R.id.wall_paper_viewpager);

                    Fragment current =
                            getSupportFragmentManager()
                                    .findFragmentByTag("f" + vp.getCurrentItem());

                    if (current instanceof BaseCardFragment) {
                        ((BaseCardFragment) current).requestDefaultFocus();
                    }
                    return true; // 消费事件
                }
                return false;
            });
        }
    }

    private void initData() {
        List<Fragment> fragments = getFragments();
        InfoCardPagerAdapter wallPaperPagerAdapter = new InfoCardPagerAdapter(this, fragments);
        wallPaperViewpager.setAdapter(wallPaperPagerAdapter);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LocalWallPaperFragment());
        fragments.add(new BuiltInWallPaperFragment());
        return fragments;
    }

    private void initView() {
        ControlManager.init(this);
        wallPaperTabLayout = findViewById(R.id.wall_paper_tab_layout);
        wallPaperViewpager = findViewById(R.id.wall_paper_viewpager);
    }
}