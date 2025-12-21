package com.jayjd.boxtop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class InfoCardPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragments;

    public InfoCardPagerAdapter(
            @NonNull FragmentActivity activity,
            @NonNull List<Fragment> fragments
    ) {
        super(activity);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
