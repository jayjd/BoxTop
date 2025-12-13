package com.jayjd.boxtop;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.blankj.utilcode.util.AppUtils;
import com.chad.library.adapter4.BaseQuickAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Predicate;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.Iterables;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.jayjd.boxtop.enums.TopSettingsIcons;
import com.jayjd.boxtop.utils.ToolUtils;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7GridLayoutManager;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    FrameLayout previewPanel;
    ImageView previewIcon;
    TextView previewTitle;
    TextView previewDesc;
    FrameLayout allAppsContainer;
    TvRecyclerView allAppsGrid;
    TvRecyclerView tvRecyclerView;
    List<AppUtils.AppInfo> appsInfo;
    TvRecyclerView topSettingsLists;
    private final List<AppUtils.AppInfo> topAppInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        topSettingsLists = findViewById(R.id.top_settings_lists);
        topSettingsLists.setLayoutManager(new V7LinearLayoutManager(this, V7LinearLayoutManager.HORIZONTAL, false));

        SettingsIconAdapter settingsIconAdapter = new SettingsIconAdapter();
        topSettingsLists.setAdapter(settingsIconAdapter);
        settingsIconAdapter.setItems(List.of(TopSettingsIcons.values()));
        settingsIconAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<TopSettingsIcons>() {
            @Override
            public void onClick(@NonNull BaseQuickAdapter<TopSettingsIcons, ?> baseQuickAdapter, @NonNull View view, int i) {

            }
        });

        previewPanel = findViewById(R.id.preview_panel);
        previewIcon = findViewById(R.id.preview_icon);
        previewTitle = findViewById(R.id.preview_title);
        previewDesc = findViewById(R.id.preview_desc);

        allAppsContainer = findViewById(R.id.all_apps_container);
        allAppsGrid = findViewById(R.id.all_apps_grid);
        allAppsGrid.setLayoutManager(new V7GridLayoutManager(this, 4));


        tvRecyclerView = findViewById(R.id.tv_recyclerview);
        tvRecyclerView.getBackground().mutate().setAlpha(128);
        tvRecyclerView.setLayoutManager(new V7LinearLayoutManager(this, V7LinearLayoutManager.HORIZONTAL, false));
        AppIconAdapter allAppAdapter = new AppIconAdapter();
        appsInfo = AppUtils.getAppsInfo();
        for (AppUtils.AppInfo appInfo : appsInfo) {
            Log.d("MainActivity", "appInfo.name = " + appInfo.getName() + " appInfo.packageName = " + appInfo.getPackageName() + " appInfo.isSystem = " + (appInfo.isSystem() ? "是" : "否"));
        }
        allAppsGrid.setAdapter(allAppAdapter);
        appsInfo = Lists.newArrayList(Iterables.filter(appsInfo, appInfo -> !appInfo.isSystem()));

        getAllAppsBanner(appsInfo);

        allAppAdapter.setItems(appsInfo);

        AppIconAdapter topAppAdapter = new AppIconAdapter();
        tvRecyclerView.setAdapter(topAppAdapter);
        topAppInfo.add(topAppAdapter.getItemCount(), getEmptyAppInfo());
        topAppAdapter.setItems(topAppInfo);

        allAppsGrid.setOnInBorderKeyEventListener((direction, focused) -> {
            if (direction == View.FOCUS_LEFT) {
                Log.d(TAG, "onInBorderKeyEvent: 左");
            } else if (direction == View.FOCUS_RIGHT) {
                Log.d(TAG, "onInBorderKeyEvent: 右");
            } else if (direction == View.FOCUS_UP) {
                Log.d(TAG, "onInBorderKeyEvent: 上");
                showHomeApps();
            } else if (direction == View.FOCUS_DOWN) {
                Log.d(TAG, "onInBorderKeyEvent: 下");
            }
            return false;
        });
        tvRecyclerView.setOnInBorderKeyEventListener((direction, focused) -> {
            if (direction == View.FOCUS_LEFT) {
                Log.d(TAG, "onInBorderKeyEvent: 左");
            } else if (direction == View.FOCUS_RIGHT) {
                Log.d(TAG, "onInBorderKeyEvent: 右");
            } else if (direction == View.FOCUS_UP) {
                Log.d(TAG, "onInBorderKeyEvent: 上");
            } else if (direction == View.FOCUS_DOWN) {
                Log.d(TAG, "onInBorderKeyEvent: 下");
                showAllApps();
            }
            return false;
        });

        allAppAdapter.setOnItemLongClickListener((parent, view, position) -> {
            Log.d("MainActivity", "onItemChildLongClick position = " + position);
            return showAppSettingsDialog(parent, position);
        });
        allAppAdapter.setOnItemClickListener((parent, view, position) -> {
            Log.d("MainActivity", "onItemClick position = " + position);
            AppUtils.AppInfo appInfo = parent.getItem(position);
            AppUtils.launchApp(appInfo.getPackageName());
        });
        topAppAdapter.setOnItemLongClickListener((baseQuickAdapter, view, position) -> {
            Log.d("MainActivity", "onItemChildLongClick position = " + position);
            return showAppSettingsDialog(baseQuickAdapter, position);
        });
        topAppAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            AppUtils.AppInfo item = baseQuickAdapter.getItem(i);
            if (item.getPackageName().isEmpty()) {
                View inflate = LayoutInflater.from(this).inflate(R.layout.activity_dialog_all_app, null);
                TvRecyclerView allDialogGrid = inflate.findViewById(R.id.all_dialog_grid);
                allDialogGrid.setLayoutManager(new V7GridLayoutManager(this, 4));
                AppIconAdapter dialogAppIconAdapter = new AppIconAdapter();
                allDialogGrid.setAdapter(dialogAppIconAdapter);
                dialogAppIconAdapter.setItems(appsInfo);
                dialogAppIconAdapter.setOnItemClickListener((baseQuickAdapter1, view1, i1) -> addTopAppInfo(baseQuickAdapter1, i1, topAppAdapter));
                allDialogGrid.requestFocus();
                new MaterialAlertDialogBuilder(this, R.style.CustomDialogTheme)
                        .setTitle("選擇應用")
                        .setView(inflate)
                        .show();

            } else {
                AppUtils.launchApp(item.getPackageName());
            }
        });

        allAppsGrid.setOnItemListener(new TvRecyclerView.OnItemListener() {
            @Override
            public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
                Log.d("MainActivity", "onItemPreSelected position = " + position);
                ToolUtils.endAnimation(itemView);
            }

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                Log.d("MainActivity", "onItemSelected position = " + position);
                ToolUtils.startAnimation(itemView);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
            }
        });

        tvRecyclerView.setOnItemListener(new TvRecyclerView.OnItemListener() {
            @Override
            public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
                Log.d("MainActivity", "onItemPreSelected position = " + position);
                ToolUtils.endAnimation(itemView);
            }

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                Log.d("MainActivity", "onItemSelected position = " + position);
                ToolUtils.startAnimation(itemView);
                AppIconAdapter adapter = (AppIconAdapter) parent.getAdapter();
                if (adapter != null) {
                    AppUtils.AppInfo item = adapter.getItem(position);
                    if (!item.getPackageName().isEmpty()) {
                        showPreview(item);
                    }
                }
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
            }
        });
    }

    private boolean showAppSettingsDialog(BaseQuickAdapter<AppUtils.AppInfo, ?> parent, int position) {
        AppUtils.AppInfo appInfo = parent.getItem(position);
        if (appInfo.getPackageName().isEmpty()) {
            return false;
        }
        new MaterialAlertDialogBuilder(this) //
                .setTitle("操作") //
                .setItems(new CharSequence[]{"启动", "查看", "卸载"}, (dialog, which) -> {
                    Log.d("MainActivity", "onClick position = " + position);
                    if (which == 0) {
                        AppUtils.launchApp(appInfo.getPackageName());
                    } else if (which == 1) {
                        AppUtils.launchAppDetailsSettings(appInfo.getPackageName());
                    } else if (which == 2) {
                        AppUtils.uninstallApp(appInfo.getPackageName());
                    }
                }).show();
        return true;
    }

    private void addTopAppInfo(@NonNull BaseQuickAdapter<AppUtils.AppInfo, ?> baseQuickAdapter, int i, AppIconAdapter topAppAdapter) {
        AppUtils.AppInfo dialogAppInfo = baseQuickAdapter.getItem(i);
        List<AppUtils.AppInfo> items = topAppAdapter.getItems();
        if (!dialogAppInfo.getPackageName().isEmpty()) {
            ArrayList<AppUtils.AppInfo> appInfos = Lists.newArrayList(Iterables.filter(items, appInfo -> appInfo.getPackageName().equals(dialogAppInfo.getPackageName())));
            if (appInfos.isEmpty()) {
                topAppAdapter.add(0, dialogAppInfo);
            } else {
                Toast.makeText(this, dialogAppInfo.getName() + " 已添加", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static AppUtils.AppInfo getEmptyAppInfo() {
        return new AppUtils.AppInfo(
                "",
                "",
                new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.UNKNOWN;
                    }

                    @Override
                    public void setAlpha(int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }
                },
                "",
                "",
                0,
                0,
                0,
                false
        );
    }


    private void showPreview(AppUtils.AppInfo appInfo) {
        previewPanel.setVisibility(View.VISIBLE);

        previewIcon.setImageDrawable(appInfo.getIcon());
        previewTitle.setText(appInfo.getName());
        previewDesc.setText(appInfo.getPackageName() + " " + appInfo.getVersionName());

        // 动画：淡入 + 放大
        previewPanel.setScaleX(0.9f);
        previewPanel.setScaleY(0.9f);
        previewPanel.setAlpha(0f);

        previewPanel.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(200).start();
    }

    private void getAllAppsBanner(List<AppUtils.AppInfo> appsInfo) {
        for (AppUtils.AppInfo appInfo : appsInfo) {
            Drawable banner = getTvAppIcon(this, appInfo.getPackageName());
            if (banner != null) {
                appInfo.setIcon(banner);
            }
        }
    }

    private void showAllApps() {
        allAppsContainer.setVisibility(View.VISIBLE);
        allAppsContainer.setAlpha(0f);
        allAppsContainer.animate().alpha(1f).setDuration(200).start();

        // 全屏 Grid 自动获取焦点
        allAppsGrid.requestFocus();
        allAppsGrid.setSelectionWithSmooth(0);
        tvRecyclerView.setVisibility(View.GONE);
        previewPanel.setVisibility(View.GONE);
        topSettingsLists.setVisibility(View.INVISIBLE);
    }

    private void showHomeApps() {
        allAppsContainer.setVisibility(View.GONE);
        allAppsContainer.setAlpha(0f);
        allAppsContainer.animate().alpha(1f).setDuration(200).start();

        // 全屏 Grid 自动获取焦点
        previewPanel.setVisibility(View.VISIBLE);
        tvRecyclerView.setVisibility(View.VISIBLE);
        topSettingsLists.setVisibility(View.VISIBLE);
        tvRecyclerView.requestFocus();
    }

    /**
     * 获取 Android TV 应用 Banner（Leanback 海报优先）
     */
    public static Drawable getTvAppIcon(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();

        try {
            // 获取应用信息
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
            return getAppIconWithFallback(pm, appInfo);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return pm.getDefaultActivityIcon(); // 返回默认图标
        }
    }

    /**
     * 带降级的图标加载
     *
     * @param pm      PackageManager
     * @param appInfo 应用信息
     * @return 图标 Drawable
     */
    public static Drawable getAppIconWithFallback(PackageManager pm, ApplicationInfo appInfo) {
        Drawable icon = null;

        // 优先级1: 尝试加载 Banner（TV 专用）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            icon = appInfo.loadBanner(pm);
        }

        // 优先级2: 从元数据中获取 TV Banner
        if (icon == null && appInfo.metaData != null) {
            int tvBannerId = appInfo.metaData.getInt("com.google.android.tv.banner", 0);
            if (tvBannerId != 0) {
                try {
                    icon = pm.getResourcesForApplication(appInfo).getDrawable(tvBannerId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 优先级3: 尝试加载 Logo
        if (icon == null) {
            icon = appInfo.loadLogo(pm);
        }

        // 优先级4: 加载普通应用图标
        if (icon == null) {
            icon = appInfo.loadIcon(pm);
        }

        return icon;
    }
}