package com.jayjd.boxtop.wallpaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jayjd.boxtop.R;
import com.jayjd.boxtop.cards.BaseCardFragment;
import com.jayjd.boxtop.listeners.TvOnItemListener;
import com.jayjd.boxtop.listeners.ViewAnimationShake;
import com.jayjd.boxtop.nanohttpd.ControlManager;
import com.jayjd.boxtop.nanohttpd.QRCodeGen;
import com.jayjd.boxtop.nanohttpd.interfas.DataReceiver;
import com.jayjd.boxtop.utils.SPUtils;
import com.jayjd.boxtop.utils.ToolUtils;
import com.jayjd.boxtop.wallpaper.adapter.LocalWallAdapter;
import com.jayjd.boxtop.wallpaper.adapter.WallPaperUtils;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;

import java.io.File;
import java.util.List;

public class LocalWallPaperFragment extends BaseCardFragment {

    private LocalWallAdapter localWallAdapter;
    private TvRecyclerView localWallList;

    ImageView ivQrCode, localWallPagerDefault;
    TextView tvHttpAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_wall_paper, container, false);
        initView(view);
        initRemoteServer();
        return view;
    }

    private void initRemoteServer() {
        ControlManager.get().startServer(new DataReceiver() {
            @Override
            public void onDouYuPush(String word) {

            }

            @Override
            public void onHDKPush(String word) {

            }

            @Override
            public void onTvLivePush(String word) {

            }

            @Override
            public void onFanLivePush(String word) {

            }

            @Override
            public void onCookiePush(String word) {

            }

            @Override
            public void onInstallApk(Context context, String absoluteFile, String tmpFileItem) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initView(View view) {
        localWallList = view.findViewById(R.id.local_wall_list);
        ivQrCode = view.findViewById(R.id.iv_qr_code);
        tvHttpAddress = view.findViewById(R.id.tv_http_address);
        localWallPagerDefault = view.findViewById(R.id.local_wall_pager_default);
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
        localWallAdapter.setOnItemClickListener((baseQuickAdapter, view2, i) -> {
            File item = baseQuickAdapter.getItem(i);
            SPUtils.put(appContext, "default_wallpaper", item.getAbsolutePath());
            ToolUtils.initWallPager(appContext, localWallPagerDefault);
            Toast.makeText(appContext, "壁纸已设为桌面壁纸", Toast.LENGTH_SHORT).show();
        });
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
    public void onStart() {
        super.onStart();
        String address = ControlManager.get().getAddress(false);
        Bitmap bitmap = QRCodeGen.generateBitmap(address, 300, 300, 0, Color.WHITE, Color.TRANSPARENT);
        Glide.with(appContext).load(bitmap).into(ivQrCode);
        tvHttpAddress.setText(address);
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        initData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initData() {
        List<File> localWallPaperList = WallPaperUtils.getLocalWallPaperList(appContext);
        if (!localWallPaperList.isEmpty()) {
            localWallAdapter.submitList(localWallPaperList);
            localWallAdapter.notifyDataSetChanged();
        }
        ToolUtils.initWallPager(appContext, localWallPagerDefault);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ControlManager.get().stopServer();
    }
}