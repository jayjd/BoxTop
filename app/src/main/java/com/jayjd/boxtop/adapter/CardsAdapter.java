package com.jayjd.boxtop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.jayjd.boxtop.R;

public class CardsAdapter extends BaseQuickAdapter<Fragment, QuickViewHolder> {

    private final FragmentManager fragmentManager;

    public CardsAdapter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(
            @NonNull Context context,
            @NonNull ViewGroup parent,
            int viewType
    ) {
        return new QuickViewHolder(R.layout.item_fragment_cards, parent);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull QuickViewHolder holder,
            int position,
            @Nullable Fragment fragment
    ) {
        if (fragment == null) return;

        FragmentContainerView container =
                holder.getView(R.id.fragment_container);

        // ⚠️ RecyclerView 中必须保证唯一
        int containerId = View.generateViewId();
        container.setId(containerId);

        String tag = "card_fragment_" + getItemId(position);

        Fragment exist = fragmentManager.findFragmentByTag(tag);

        fragmentManager.executePendingTransactions();

        if (exist == null) {
            fragmentManager.beginTransaction()
                    .add(containerId, fragment, tag)
                    .setReorderingAllowed(true)
                    .commit();
        } else if (exist.isDetached()) {
            fragmentManager.beginTransaction()
                    .attach(exist)
                    .setReorderingAllowed(true)
                    .commit();
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (!(holder instanceof QuickViewHolder qh)) return;

        FragmentContainerView container =
                qh.getView(R.id.fragment_container);

        Fragment fragment =
                fragmentManager.findFragmentById(container.getId());

        if (fragment != null && fragment.isDetached()) {
            fragmentManager.beginTransaction()
                    .attach(fragment)
                    .commit();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        if (!(holder instanceof QuickViewHolder qh)) return;

        FragmentContainerView container =
                qh.getView(R.id.fragment_container);

        Fragment fragment =
                fragmentManager.findFragmentById(container.getId());

        if (fragment != null && fragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .detach(fragment)
                    .commit();
        }
    }
}

