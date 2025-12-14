package com.jayjd.boxtop;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.palette.graphics.Palette;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter4.BaseQuickAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.jayjd.boxtop.adapter.AppIconAdapter;
import com.jayjd.boxtop.adapter.SettingsIconAdapter;
import com.jayjd.boxtop.entity.AppInfo;
import com.jayjd.boxtop.enums.TopSettingsIcons;
import com.jayjd.boxtop.listeners.TvOnItemListener;
import com.jayjd.boxtop.listeners.ViewAnimateListener;
import com.jayjd.boxtop.listeners.ViewAnimationShake;
import com.jayjd.boxtop.utils.AppsUtils;
import com.jayjd.boxtop.utils.ToolUtils;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7GridLayoutManager;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewAnimateListener {
    private static final String TAG = "MainActivity";
    FrameLayout previewPanel;
    ImageView previewIcon;
    TextView previewTitle;
    TextView previewDesc;
    FrameLayout allAppsContainer;
    FrameLayout favoriteAppsContainer;
    private final List<AppInfo> favoriteApps = new ArrayList<>();
    TvRecyclerView appListGrid;
    TvRecyclerView favoriteAppsGrid;
    TvRecyclerView topSettingsBar;
    AppIconAdapter appListAdapter;
    AppIconAdapter favoriteAppsAdapter;
    SettingsIconAdapter topSettingsAdapter;
    private List<AppInfo> allApps = new ArrayList<>();
    private List<AppInfo> systemApps = new ArrayList<>();


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
        initDefaleHome();
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        topSettingsAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {

        });
        appListAdapter.setOnItemLongClickListener((parent, view, position) -> {
            Log.d("MainActivity", "onItemChildLongClick position = " + position);
            return showAppSettingsDialog(parent, position);
        });
        appListAdapter.setOnItemClickListener((parent, view, position) -> {
            Log.d("MainActivity", "onItemClick position = " + position);
            AppInfo appInfo = parent.getItem(position);
            if (appInfo.getPackageName().isEmpty()) {
                View inflate = LayoutInflater.from(this).inflate(R.layout.activity_dialog_all_app, null);
                TvRecyclerView allDialogGrid = inflate.findViewById(R.id.all_dialog_grid);
                allDialogGrid.setLayoutManager(new V7GridLayoutManager(this, 4));
                AppIconAdapter dialogAppIconAdapter = new AppIconAdapter();
                allDialogGrid.setAdapter(dialogAppIconAdapter);
                dialogAppIconAdapter.setItems(systemApps);
                dialogAppIconAdapter.setOnItemLongClickListener((baseQuickAdapter, view2, i) -> {
                    Log.d("MainActivity", "onItemChildLongClick position = " + i);
                    return showAppSettingsDialog(baseQuickAdapter, i);
                });
                dialogAppIconAdapter.setOnItemClickListener((baseQuickAdapter1, view1, i1) -> {
                    AppInfo item = baseQuickAdapter1.getItem(i1);
                    if (item.getPackageName().isEmpty()) {
                        return;
                    }
                    AppUtils.launchApp(item.getPackageName());
                });
                allDialogGrid.setOnItemListener(new TvOnItemListener());
                allDialogGrid.requestFocus();
                showMaterialAlertDialog(this, "系统应用", inflate);
            } else {
                AppUtils.launchApp(appInfo.getPackageName());
            }
        });
        favoriteAppsAdapter.setOnItemLongClickListener((baseQuickAdapter, view, position) -> {
            Log.d("MainActivity", "onItemChildLongClick position = " + position);
            return showAppSettingsDialog(baseQuickAdapter, position);
        });
        favoriteAppsAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            AppInfo item = baseQuickAdapter.getItem(i);
            if (item.getPackageName().isEmpty()) {
                View inflate = LayoutInflater.from(this).inflate(R.layout.activity_dialog_all_app, null);
                TvRecyclerView allDialogGrid = inflate.findViewById(R.id.all_dialog_grid);
                allDialogGrid.setLayoutManager(new V7GridLayoutManager(this, 4));
                AppIconAdapter dialogAppIconAdapter = new AppIconAdapter();
                allDialogGrid.setAdapter(dialogAppIconAdapter);
                dialogAppIconAdapter.setItems(allApps);
                dialogAppIconAdapter.setOnItemClickListener((baseQuickAdapter1, view1, i1) -> addTopAppInfo(baseQuickAdapter1, i1, favoriteAppsAdapter));
                allDialogGrid.setOnItemListener(new TvOnItemListener());
                allDialogGrid.requestFocus();
                showMaterialAlertDialog(this, "所有应用", inflate);
            } else {
                AppUtils.launchApp(item.getPackageName());
            }
        });

        appListGrid.setOnItemListener(new TvOnItemListener());
        favoriteAppsGrid.setOnItemListener(new TvRecyclerView.OnItemListener() {
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
                    AppInfo item = adapter.getItem(position);
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

    private void initView() {
        ImageView wallPager = findViewById(R.id.wall_pager);
        Glide.with(this).load(R.drawable.wallpager).centerCrop().into(wallPager);
        // 顶部设置按钮
        topSettingsBar = findViewById(R.id.top_settings_lists);
        // 选中后预览的界面
        previewPanel = findViewById(R.id.preview_panel);
        previewIcon = findViewById(R.id.preview_icon);
        previewTitle = findViewById(R.id.preview_title);
        previewDesc = findViewById(R.id.preview_desc);
        // 常用的软件
        favoriteAppsContainer = findViewById(R.id.favorite_apps_container);
        favoriteAppsGrid = findViewById(R.id.favorite_apps_grid);
        // 所有软件的布局和列表
        allAppsContainer = findViewById(R.id.all_apps_container);
        appListGrid = findViewById(R.id.all_apps_grid);

        topSettingsBar.setLayoutManager(new V7LinearLayoutManager(this, V7LinearLayoutManager.HORIZONTAL, false));
        topSettingsBar.setOnInBorderKeyEventListener(new ViewAnimationShake(topSettingsBar, this, 0, this));
        favoriteAppsGrid.setLayoutManager(new V7LinearLayoutManager(this, V7LinearLayoutManager.HORIZONTAL, false));
        favoriteAppsGrid.setOnInBorderKeyEventListener(new ViewAnimationShake(favoriteAppsGrid, this, 1, this));
        appListGrid.setLayoutManager(new V7GridLayoutManager(this, 4));
        appListGrid.setOnInBorderKeyEventListener(new ViewAnimationShake(appListGrid, this, 2, this));

        favoriteAppsGrid.getBackground().mutate().setAlpha(128);
    }

    private void initData() {
        topSettingsAdapter = new SettingsIconAdapter();
        appListAdapter = new AppIconAdapter();
        favoriteAppsAdapter = new AppIconAdapter();

        topSettingsBar.setAdapter(topSettingsAdapter);
        appListGrid.setAdapter(appListAdapter);
        favoriteAppsGrid.setAdapter(favoriteAppsAdapter);

        topSettingsAdapter.setItems(List.of(TopSettingsIcons.values()));

        new Thread(() -> {
            List<AppInfo> tempAllApps = AppsUtils.getAppsInfo(this);
            getAllAppsBanner(tempAllApps);
            allApps = Lists.newArrayList(Iterables.filter(tempAllApps, appInfo -> {
                if (appInfo != null) {
                    return !appInfo.isSystem();
                }
                return false;
            }));
            systemApps = Lists.newArrayList(Iterables.filter(tempAllApps, appInfo -> appInfo != null && appInfo.isSystem()));
            Iterator<AppInfo> iterator = systemApps.iterator();
            while (iterator.hasNext()) {
                AppInfo next = iterator.next();
                boolean appLaunchable = ToolUtils.isAppLaunchable(this, next.getPackageName());
                if (!appLaunchable) {
                    iterator.remove();
                }
            }
            allApps.add(allApps.size(), ToolUtils.getEmptyAppInfo("system"));
            favoriteApps.add(favoriteAppsAdapter.getItemCount(), ToolUtils.getEmptyAppInfo("add"));
            Log.d(TAG, "initData: 数据处理完成");
            new Handler(Looper.getMainLooper()).post(() -> {
                Log.d(TAG, "initData: 更新UI");
                appListAdapter.setItems(allApps);
                appListAdapter.notifyDataSetChanged();
                favoriteAppsAdapter.setItems(favoriteApps);
                favoriteAppsAdapter.notifyDataSetChanged();
                favoriteAppsGrid.requestFocus();
//                favoriteAppsGrid.post(() -> {
//                    favoriteAppsGrid.scrollToPosition(0);
//                    RecyclerView.ViewHolder viewHolderForAdapterPosition = favoriteAppsGrid.findViewHolderForAdapterPosition(0);
//                    if (viewHolderForAdapterPosition != null) {
//                        viewHolderForAdapterPosition.itemView.requestFocus();
//                    } else {
//                        favoriteAppsGrid.requestFocus();
//                    }
//                });
                Log.d(TAG, "initData: 更新UI完成");
            });
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        allAppsContainer.post(() -> {
            int screenHeight = ScreenUtils.getScreenHeight();
            allAppsContainer.setTranslationY(screenHeight);
        });
    }

    /**
     * 跳转到系统设置中更改 Home 默认应用的界面。
     * 兼容 Android 5.0 及以上设备。
     */
    private void goToHomeSettings() {
        Intent intent;

        // 优先级 1: 尝试直接跳转到 "Home 应用设置" (Settings.ACTION_HOME_SETTINGS)
        // 这是 Android 官方 API，在许多原生和轻定制系统中有效。
        try {
            intent = new Intent(Settings.ACTION_HOME_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "请选择您的桌面应用", Toast.LENGTH_LONG).show();
            return;
        } catch (ActivityNotFoundException e1) {
            // Log.e("Launcher", "ACTION_HOME_SETTINGS not found", e1);
        }

        // 优先级 2: 尝试跳转到“默认应用管理”界面 (Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
        // 此 Intent 在 Android 6.0 (API 23) 及以上版本中更常见，但作为回退选项兼容性更好。
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent = new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
                startActivity(intent);
                Toast.makeText(this, "请在默认应用设置中更改桌面", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (ActivityNotFoundException e2) {
            // Log.e("Launcher", "ACTION_MANAGE_DEFAULT_APPS_SETTINGS not found", e2);
        }

        // 优先级 3 (最后的通用回退): 跳转到主设置界面
        // 如果前两种都失败，让用户自己去寻找。
        try {
            intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "请手动进入设置中查找并更改默认桌面", Toast.LENGTH_LONG).show();
        } catch (ActivityNotFoundException e3) {
            Toast.makeText(this, "无法打开系统设置", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean showAppSettingsDialog(BaseQuickAdapter<AppInfo, ?> parent, int position) {
        AppInfo appInfo = parent.getItem(position);
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
                        if (appInfo.isSystem()) {
                            Toast.makeText(this, "系统应用无法卸载", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        new MaterialAlertDialogBuilder(this)
                                .setTitle("卸载应用")
                                .setMessage("确定要卸载「" + appInfo.getName() + "」吗？")
                                .setPositiveButton("卸载", (d, w) -> {
                                    ToolUtils.uninstallApp(this, appInfo.getPackageName());
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    }
                }).show();
        return true;
    }

    public View initCustomTitle(Context context, String title) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.activity_custom_title, null);
        TextView customTextView = inflate.findViewById(R.id.custom_textview);
        customTextView.setText(title);
        return inflate;
    }

    private void addTopAppInfo(@NonNull BaseQuickAdapter<AppInfo, ?> baseQuickAdapter, int i, AppIconAdapter topAppAdapter) {
        AppInfo dialogAppInfo = baseQuickAdapter.getItem(i);
        List<AppInfo> items = topAppAdapter.getItems();
        if (!dialogAppInfo.getPackageName().isEmpty()) {
            ArrayList<AppInfo> appInfos = Lists.newArrayList(Iterables.filter(items, appInfo -> {
                if (appInfo != null) {
                    return appInfo.getPackageName().equals(dialogAppInfo.getPackageName());
                }
                return false;
            }));
            if (appInfos.isEmpty()) {
                topAppAdapter.add(0, dialogAppInfo);
            } else {
                Toast.makeText(this, dialogAppInfo.getName() + " 已添加", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showMaterialAlertDialog(Context context, String titleName, View rootView) {
        Dialog dialog = new Dialog(context, R.style.CustomDialogTheme);
        dialog.setContentView(rootView);
        dialog.show();
    }

    private void getAllAppsBanner(List<AppInfo> appsInfo) {
        for (AppInfo appInfo : appsInfo) {
            Drawable banner = ToolUtils.getTvAppIcon(this, appInfo.getPackageName());
            Drawable icon = banner != null ? banner : appInfo.getIcon();
            if (icon == null) continue;
            if (banner != null) {
                appInfo.setIcon(banner);
            }
            Bitmap bitmap = ToolUtils.drawableToSmallBitmap(icon, 24);
            if (bitmap == null) continue;
            appInfo.setBitmapIcon(bitmap);
            Palette generate = Palette.from(bitmap).generate();
            int dominantColor = generate.getDominantColor(Color.parseColor("#263238"));
            int normalizeColor = ToolUtils.normalizeColor(dominantColor);
            appInfo.setCardColor(normalizeColor);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showPreview(AppInfo appInfo) {
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

    private void showAllApps() {
        if (previewPanel.getVisibility() == View.VISIBLE)
            previewPanel.setVisibility(View.GONE);
        topSettingsBar.setVisibility(View.GONE);
        int screenHeight = ScreenUtils.getScreenHeight();
        Log.d(TAG, "showAllApps: " + screenHeight);
        favoriteAppsContainer.animate().translationY(-screenHeight).setDuration(500).start();
        allAppsContainer.animate().translationY(-screenHeight).setDuration(500).start();
        appListGrid.requestFocus();
        appListGrid.setSelectionWithSmooth(0);
    }

    private void showHomeApps() {
        topSettingsBar.setVisibility(View.VISIBLE);
        int screenHeight = ScreenUtils.getScreenHeight();
        favoriteAppsContainer.animate().translationY(0).setDuration(500).start();
        allAppsContainer.animate().translationY(screenHeight).setDuration(500).start();
        favoriteAppsGrid.requestFocus();
        favoriteAppsGrid.setSelectionWithSmooth(0);
    }


    private void initDefaleHome() {
        boolean defaultHome = ToolUtils.isDefaultHome(this);
        Log.d("MainActivity", "defaultHome = " + defaultHome);
        if (!defaultHome) {
            new MaterialAlertDialogBuilder(this, R.style.CustomDialogTheme)
                    .setTitle("设置为默认桌面")
                    .setMessage("是否要设置为默认桌面？")
                    .setPositiveButton("是", (dialog, which) -> goToHomeSettings())
                    .setNegativeButton("否", null)
                    .show();
        }
    }

    @Override
    public boolean animateType(int viewAction, int gridType) {
        if (viewAction == View.FOCUS_DOWN && gridType == 1) {
            showAllApps();
            return true;
        } else if (viewAction == View.FOCUS_UP && gridType == 2) {
            showHomeApps();
            return true;
        }
        Log.d(TAG, "animateType: 下放控件");
        return false;
    }
}