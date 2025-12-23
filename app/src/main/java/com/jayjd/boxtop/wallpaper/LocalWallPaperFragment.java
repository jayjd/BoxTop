package com.jayjd.boxtop.wallpaper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jayjd.boxtop.R;
import com.jayjd.boxtop.cards.BaseCardFragment;
import com.jayjd.boxtop.listeners.TvOnItemListener;
import com.jayjd.boxtop.listeners.ViewAnimationShake;
import com.jayjd.boxtop.utils.SPUtils;
import com.jayjd.boxtop.wallpaper.adapter.LocalWallAdapter;
import com.jayjd.boxtop.wallpaper.adapter.WallPaperUtils;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;

import java.io.File;
import java.util.List;

public class LocalWallPaperFragment extends BaseCardFragment {

    private LocalWallAdapter localWallAdapter;
    private TvRecyclerView localWallList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_wall_paper, container, false);
        initView(view);
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initView(View view) {
        localWallList = view.findViewById(R.id.local_wall_list);
        localWallList.setLayoutManager(new V7LinearLayoutManager(appContext, V7LinearLayoutManager.HORIZONTAL, false));
        localWallAdapter = new LocalWallAdapter();
        localWallList.setAdapter(localWallAdapter);
        localWallList.setOnInBorderKeyEventListener(new ViewAnimationShake(localWallList, appContext) {
            @Override
            public boolean onInBorderKeyEvent(int direction, View focused) {
                if (direction == View.FOCUS_UP) {
                    return false;
                }
                return super.onInBorderKeyEvent(direction, focused);
            }
        });
        localWallList.setOnItemListener(new TvOnItemListener());
        localWallAdapter.setOnItemLongClickListener((baseQuickAdapter, view1, i) -> {
            File item = baseQuickAdapter.getItem(i);
            String defaultWallpaper = (String) SPUtils.get(appContext, "default_wallpaper", "");
            if (defaultWallpaper.contains(item.getName())) {
                Toast.makeText(appContext, "已设为桌面壁纸，不可删除！", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (item.exists()) {
                boolean delete = item.delete();
                Toast.makeText(appContext, delete ? "删除成功" : "删除失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(appContext, "壁纸文件不存在", Toast.LENGTH_SHORT).show();
            }
            baseQuickAdapter.remove(item);
            baseQuickAdapter.notifyDataSetChanged();
            return true;
        });
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        initData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initData() {
        List<File> localWallPaperList = WallPaperUtils.getLocalWallPaperList(appContext);
        localWallAdapter.submitList(localWallPaperList);
        localWallAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onFragmentInvisible() {
        super.onFragmentInvisible();
    }
    @Override
    public void requestDefaultFocus() {
        super.requestDefaultFocus();
        localWallList.requestFocus();
    }
}