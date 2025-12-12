package com.jayjd.boxtop;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.blankj.utilcode.util.AppUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.Iterables;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.Lists;
import com.jayjd.boxtop.utils.ToolUtils;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7GridLayoutManager;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;

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
        AppIconAdapter appIconAdapter = new AppIconAdapter();
        tvRecyclerView.setAdapter(appIconAdapter);
        List<AppUtils.AppInfo> appsInfo = AppUtils.getAppsInfo();
        for (AppUtils.AppInfo appInfo : appsInfo) {
            Log.d("MainActivity", "appInfo.name = " + appInfo.getName() + " appInfo.packageName = " + appInfo.getPackageName() + " appInfo.isSystem = " + (appInfo.isSystem() ? "是" : "否"));
        }
        allAppsGrid.setAdapter(appIconAdapter);
        appIconAdapter.setItems(appsInfo);
        appsInfo = Lists.newArrayList(Iterables.filter(appsInfo, appInfo -> !appInfo.isSystem()));

        getAllAppsBanner(appsInfo);

        appIconAdapter.setItems(appsInfo);

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

        appIconAdapter.addOnItemChildLongClickListener(R.id.root_layout, (parent, view, position) -> {
            Log.d("MainActivity", "onItemChildLongClick position = " + position);
            AppUtils.AppInfo appInfo = parent.getItem(position);
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
        });
        appIconAdapter.setOnItemClickListener((parent, view, position) -> {
            Log.d("MainActivity", "onItemClick position = " + position);
            AppUtils.AppInfo appInfo = parent.getItem(position);
            AppUtils.launchApp(appInfo.getPackageName());
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
                    showPreview(adapter.getItem(position));
                }
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
            }
        });
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
        PackageManager pm = this.getPackageManager();
        for (AppUtils.AppInfo appInfo : appsInfo) {
            Drawable banner = getTvBanner(pm, appInfo.getPackageName());
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
    }

    private void showHomeApps() {
        allAppsContainer.setVisibility(View.GONE);
        allAppsContainer.setAlpha(0f);
        allAppsContainer.animate().alpha(1f).setDuration(200).start();

        // 全屏 Grid 自动获取焦点
        previewPanel.setVisibility(View.VISIBLE);
        tvRecyclerView.setVisibility(View.VISIBLE);
        tvRecyclerView.requestFocus();
        tvRecyclerView.setSelectionWithSmooth(0);
    }

    /**
     * 获取 Android TV 应用 Banner（Leanback 海报优先）
     */
    private Drawable getTvBanner(PackageManager pm, String packageName) {
        try {
            // 1. 先找 Leanback Launcher
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
            intent.setPackage(packageName);

            List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA | PackageManager.GET_RESOLVED_FILTER);

            for (ResolveInfo resolveInfo : resolveInfos) {
                ActivityInfo activityInfo = resolveInfo.activityInfo;

                // —— ① Banner 属性 ——
                int bannerId = activityInfo.banner;
                if (bannerId != 0) {
                    Drawable d = pm.getDrawable(packageName, bannerId, activityInfo.applicationInfo);
                    if (d != null) return d;
                }

                // —— ② meta-data Banner ——
                if (activityInfo.metaData != null) {
                    int metaBanner = activityInfo.metaData.getInt("android.app.banner", 0);
                    if (metaBanner != 0) {
                        Drawable d = pm.getDrawable(packageName, metaBanner, activityInfo.applicationInfo);
                        if (d != null) return d;
                    }
                }

                // —— ③ icon（某些 TV app 没 banner） ——
                int iconId = activityInfo.getIconResource();
                if (iconId != 0) {
                    Drawable d = pm.getDrawable(packageName, iconId, activityInfo.applicationInfo);
                    if (d != null) return d;
                }
            }

            // 2. 找不到 Leanback 海报 → 尝试普通 Launcher Activity
            Intent normalIntent = new Intent(Intent.ACTION_MAIN);
            normalIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            normalIntent.setPackage(packageName);

            List<ResolveInfo> normalList = pm.queryIntentActivities(normalIntent, PackageManager.GET_META_DATA);

            if (!normalList.isEmpty()) {
                ActivityInfo ai = normalList.get(0).activityInfo;
                int iconId = ai.getIconResource();
                if (iconId != 0) {
                    return pm.getDrawable(packageName, iconId, ai.applicationInfo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}