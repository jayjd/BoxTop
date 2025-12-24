package com.jayjd.boxtop.wallpaper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jayjd.boxtop.R;
import com.jayjd.boxtop.cards.BaseCardFragment;
import com.jayjd.boxtop.entity.WallPagerEntity;
import com.jayjd.boxtop.listeners.FileCallBack;
import com.jayjd.boxtop.listeners.TvOnItemListener;
import com.jayjd.boxtop.listeners.ViewAnimationShake;
import com.jayjd.boxtop.utils.ApiConfig;
import com.jayjd.boxtop.utils.SPUtils;
import com.jayjd.boxtop.utils.ToolUtils;
import com.jayjd.boxtop.wallpaper.adapter.WallPagerAdapter;
import com.jayjd.boxtop.wallpaper.adapter.WallPaperUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;

import java.io.File;
import java.util.List;


public class BuiltInWallPaperFragment extends BaseCardFragment {

    private static final String TAG = "WallPagerActivity";
    ImageView wallPagerPreview, wallPagerDefault;
    TextView downloadProgress;
    private WallPagerAdapter wallPagerAdapter;
    private TvRecyclerView trWallList;
    private boolean hasLoaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_built_in_wall_paper, container, false);
        initView(inflate);
        return inflate;
    }

    private final Handler debounceHandler = new Handler(Looper.getMainLooper());
    private Runnable debounceRunnable;

    private void initData() {
        OkGo.<String>get(ApiConfig.WALLPAPER_URL).execute(new StringCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body();
                        WallPagerEntity wallPagerEntity = new Gson().fromJson(json, WallPagerEntity.class);
                        if (wallPagerEntity.getCode() == 200) {
                            List<WallPagerEntity.DataBean.ListBean> list = wallPagerEntity.getData().getList();
                            wallPagerAdapter.setItems(list);
                            wallPagerAdapter.notifyDataSetChanged();
                            String img = list.get(0).getImg();
                            Glide.with(appContext).load(img).into(wallPagerPreview);
                            return;
                        }
                    } catch (JsonSyntaxException ignored) {
                    }
                }
                Toast.makeText(appContext, "加载壁纸列表异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Toast.makeText(appContext, "加载壁纸列表失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View inflate) {
        wallPagerPreview = inflate.findViewById(R.id.wall_pager_preview);
        wallPagerDefault = inflate.findViewById(R.id.wall_pager_default);
        downloadProgress = inflate.findViewById(R.id.download_progress);
        trWallList = inflate.findViewById(R.id.tr_wall_list);
        trWallList.setLayoutManager(new V7LinearLayoutManager(appContext, V7LinearLayoutManager.HORIZONTAL, false));
        wallPagerAdapter = new WallPagerAdapter();
        trWallList.setAdapter(wallPagerAdapter);
        trWallList.setOnItemListener(new TvOnItemListener() {
            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                super.onItemSelected(parent, itemView, position);
                // 2. 移除之前的回调
                if (debounceRunnable != null) {
                    debounceHandler.removeCallbacks(debounceRunnable);
                }

                // 3. 创建新的任务
                debounceRunnable = () -> {
                    downloadProgress.setVisibility(View.GONE);
                    ImageView imageView = itemView.findViewById(R.id.iv_wall_pager);
                    if (imageView != null && imageView.getDrawable() != null) {
                        wallPagerPreview.setImageDrawable(imageView.getDrawable());
                    }
                };

                // 4. 延迟执行（200-300ms 是 TV 端比较舒适的反馈时间）
                debounceHandler.postDelayed(debounceRunnable, 250);
            }
        });
        trWallList.setOnInBorderKeyEventListener(new ViewAnimationShake(trWallList, appContext) {
            @Override
            public boolean onInBorderKeyEvent(int direction, View focused) {
                if (direction == View.FOCUS_UP) {
                    return false;
                }
                return super.onInBorderKeyEvent(direction, focused);
            }
        });

        wallPagerAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            WallPagerEntity.DataBean.ListBean item = baseQuickAdapter.getItem(i);
            String img = item.getImg();
            if (img != null && !img.isEmpty()) {
                downloadWallPager(img.replace(".jpg", ".png"));
            }
        });
    }

    private void downloadWallPager(String img) {
        File wallDirFile = WallPaperUtils.getLocalWallPath(appContext);
        if (!wallDirFile.exists()) {
            boolean mkdirs = wallDirFile.mkdirs();
        }
        String fileName = img.substring(img.lastIndexOf("/") + 1);
        OkGo.<File>get(img).execute(new FileCallBack(wallDirFile, fileName) {
            @Override
            public void onStart(Request<File, ? extends Request> request) {
                super.onStart(request);
                downloadProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(Response<File> response) {
                if (response.isSuccessful()) {
                    File downloadedFile = response.body();
                    if (downloadedFile != null && downloadedFile.exists()) {
                        downloadProgress.setText("下载完成");
                        Log.d(TAG, "onSuccess: " + downloadedFile.getAbsolutePath());
                        SPUtils.put(appContext, "default_wallpaper", downloadedFile.getAbsolutePath());
                        Toast.makeText(appContext, "壁纸下载成功", Toast.LENGTH_SHORT).show();
                        ToolUtils.initWallPager(appContext, wallPagerDefault);
                    }
                }
            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);
                downloadProgress.setText("壁纸下载失败");
                Toast.makeText(appContext, "壁纸下载失败", Toast.LENGTH_SHORT).show();
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void downloadProgress(Progress progress) {
                Log.d(TAG, "downloadProgress: " + progress.fraction);
                downloadProgress.setText(String.format("%.2f%%", progress.fraction * 100));
            }
        });
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        if (!hasLoaded) {
            initData();
            hasLoaded = true;
        }
        ToolUtils.initWallPager(appContext, wallPagerDefault);
    }

    @Override
    protected void onFragmentInvisible() {
        super.onFragmentInvisible();
    }

    @Override
    public void requestDefaultFocus() {
        super.requestDefaultFocus();
        trWallList.requestFocus();
    }

    @Override
    protected void onProStateChanged(boolean isPro) {
        Log.d("TAG", "onProStateChanged: " + isPro);
    }
}