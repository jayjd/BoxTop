package com.jayjd.boxtop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter4.BaseQuickAdapter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.jayjd.boxtop.adapter.AppIconAdapter;
import com.jayjd.boxtop.adapter.PreviewSettingsAdapter;
import com.jayjd.boxtop.adapter.SettingsIconAdapter;
import com.jayjd.boxtop.dao.FavoriteAppInfoDao;
import com.jayjd.boxtop.database.AppDataBase;
import com.jayjd.boxtop.entity.AppInfo;
import com.jayjd.boxtop.entity.HotSearchEntity;
import com.jayjd.boxtop.enums.PreviewSettings;
import com.jayjd.boxtop.enums.TopSettingsIcons;
import com.jayjd.boxtop.listeners.TvOnItemListener;
import com.jayjd.boxtop.listeners.UsbDriveListener;
import com.jayjd.boxtop.listeners.ViewAnimateListener;
import com.jayjd.boxtop.listeners.ViewAnimationShake;
import com.jayjd.boxtop.receiver.UsbBroadcastReceiver;
import com.jayjd.boxtop.utils.AppsUtils;
import com.jayjd.boxtop.utils.NetworkMonitor;
import com.jayjd.boxtop.utils.SPUtils;
import com.jayjd.boxtop.utils.ToolUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7GridLayoutManager;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements ViewAnimateListener {
    private static final String TAG = "MainActivity";
    FrameLayout previewPanel;
    ImageView previewIcon;
    TextView previewTitle;
    TextView previewDesc;
    FrameLayout allAppsContainer;
    FrameLayout favoriteAppsContainer;
    private List<AppInfo> favoriteApps = new ArrayList<>();
    TvRecyclerView appListGrid;
    TvRecyclerView favoriteAppsGrid;
    TvRecyclerView topSettingsBar;
    AppIconAdapter appListAdapter;
    AppIconAdapter favoriteAppsAdapter;
    SettingsIconAdapter topSettingsAdapter;
    private List<AppInfo> allApps = new ArrayList<>();
    private List<AppInfo> systemApps = new ArrayList<>();
    private boolean isMoveApp = false;


    AppDataBase appDataBase;
    FavoriteAppInfoDao favoriteAppInfoDao;

    private final UsbBroadcastReceiver usbReceiver = new UsbBroadcastReceiver(new UsbDriveListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onUsbDriveStateChanged(boolean isConnected) {
            //        adb shell am broadcast -a android.intent.action.MEDIA_MOUNTED -d file:///storage/usb1
//        adb shell am broadcast -a android.intent.action.MEDIA_UNMOUNTED -d file:///storage/usb1
            if (isConnected) {
                // 插入U盘
                showTopIcon(TopSettingsIcons.FLASH_DRIVE_ICON);
                ToolUtils.openFileManager(MainActivity.this);
                Toast.makeText(MainActivity.this, "U盘已插入", Toast.LENGTH_SHORT).show();
            } else {
                // 拔出U盘
                removeTopIcon(TopSettingsIcons.FLASH_DRIVE_ICON);
                Toast.makeText(MainActivity.this, "U盘已拔出", Toast.LENGTH_SHORT).show();
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onBluetoothStateChanged(boolean isConnected) {
            // 蓝牙连接
            if (isConnected) {
                if (topSettingsAdapter.itemIndexOfFirst(TopSettingsIcons.BLUETOOTH_ICON) == -1) {
                    List<TopSettingsIcons> items = topSettingsAdapter.getItems();
                    items.add(0, TopSettingsIcons.BLUETOOTH_ICON);
                    topSettingsAdapter.setItems(items);
                    topSettingsAdapter.notifyDataSetChanged();
                }
                Toast.makeText(MainActivity.this, "蓝牙已连接", Toast.LENGTH_SHORT).show();
            } else {
                // 蓝牙断开
                int index = topSettingsAdapter.itemIndexOfFirst(TopSettingsIcons.BLUETOOTH_ICON);
                if (index != -1) {
                    List<TopSettingsIcons> items = topSettingsAdapter.getItems();
                    items.remove(index);
                    topSettingsAdapter.setItems(items);
                    topSettingsAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "蓝牙已断开", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onInstalled(Context context, String pkg) {
            AppInfo appInfo = AppsUtils.getAppInfo(context, pkg);
            if (appInfo == null) return;

            List<AppInfo> allApps = appListAdapter.getItems();
            allApps.add(0, appInfo);
            appListAdapter.setItems(allApps);
            appListAdapter.notifyDataSetChanged();

            syncFavoriteOnAddOrUpdate(appInfo);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onUninstalled(Context context, String pkg) {
            List<AppInfo> allApps = appListAdapter.getItems();
            AppInfo appInfo = findByPackage(allApps, pkg);
            if (appInfo != null) {
                allApps.remove(appInfo);
                appListAdapter.setItems(allApps);
                appListAdapter.notifyDataSetChanged();
            }

            List<AppInfo> favoriteApps = favoriteAppsAdapter.getItems();
            AppInfo fav = findByPackage(favoriteApps, pkg);
            if (fav != null) {
                favoriteApps.remove(fav);
                favoriteAppsAdapter.setItems(favoriteApps);
                favoriteAppsAdapter.notifyDataSetChanged();
                new Thread(() -> favoriteAppInfoDao.deleteByPackageName(pkg)).start();
            }
        }


        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onUpdated(Context context, String pkg) {
            AppInfo appInfo = AppsUtils.getAppInfo(context, pkg);
            if (appInfo == null) return;

            List<AppInfo> allApps = appListAdapter.getItems();
            AppInfo old = findByPackage(allApps, pkg);
            if (old != null) {
                allApps.remove(old);
                allApps.add(0, appInfo);
                appListAdapter.setItems(allApps);
                appListAdapter.notifyDataSetChanged();
            }

            syncFavoriteOnAddOrUpdate(appInfo);
        }

        @Nullable
        private AppInfo findByPackage(List<AppInfo> list, String pkg) {
            if (list == null || list.isEmpty()) return null;
            for (AppInfo app : list) {
                if (app != null && pkg.equals(app.getPackageName())) {
                    return app;
                }
            }
            return null;
        }

        @SuppressLint("NotifyDataSetChanged")
        private void syncFavoriteOnAddOrUpdate(AppInfo newApp) {
            List<AppInfo> favoriteApps = favoriteAppsAdapter.getItems();
            if (favoriteApps.isEmpty()) return;

            AppInfo old = findByPackage(favoriteApps, newApp.getPackageName());
            if (old == null) return;

            int index = old.getSortIndex();
            favoriteApps.remove(old);

            newApp.setSortIndex(index);
            favoriteApps.add(index, newApp);

            favoriteAppsAdapter.setItems(favoriteApps);
            favoriteAppsAdapter.notifyDataSetChanged();

            new Thread(() -> favoriteAppInfoDao.update(old)).start();
        }
    });
    ImageView wallPager;

    @Override
    protected void onResume() {
        super.onResume();
        initWallPager();
    }

    private void initWallPager() {
        String defaultWallpaper = (String) SPUtils.get(this, "default_wallpaper", "");
        if (!defaultWallpaper.isEmpty()) {
            File file = new File(defaultWallpaper);
            if (file.exists()) {
                Glide.with(this).load(file).centerCrop().into(wallPager);
            } else {
                Glide.with(this).load(R.drawable.wallpager).centerCrop().into(wallPager);
            }
        } else {
            Glide.with(this).load(R.drawable.wallpager).centerCrop().into(wallPager);
        }
    }

    private void initView() {
        wallPager = findViewById(R.id.wall_pager);
        initWallPager();
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
        favoriteAppsGrid.setLayoutManager(new V7LinearLayoutManager(this, V7LinearLayoutManager.HORIZONTAL, false));
        appListGrid.setLayoutManager(new V7GridLayoutManager(this, 4));

        topSettingsBar.setOnInBorderKeyEventListener(new ViewAnimationShake(topSettingsBar, this, 0, this));
        favoriteAppsGrid.setOnInBorderKeyEventListener(new ViewAnimationShake(favoriteAppsGrid, this, 1, this));
        appListGrid.setOnInBorderKeyEventListener(new ViewAnimationShake(appListGrid, this, 2, this));
        allAppsContainer.post(() -> {
            int screenHeight = ScreenUtils.getScreenHeight();
            allAppsContainer.setTranslationY(screenHeight);
        });
    }

    private final Executor dbExecutor = Executors.newSingleThreadExecutor();
    NetworkMonitor networkMonitor;
    private TopSettingsIcons currentNetworkIcon = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        initDefaleHome();
        initView();
        initData();
        initListener();
        registerNetworkReceiver();
    }

    private void checkForOTGDevice() {
        StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
        boolean otgFound = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<StorageVolume> volumes = storageManager.getStorageVolumes();

            for (StorageVolume volume : volumes) {
                // 判断是否为可移动存储（U盘/OTG）
                if (volume.isRemovable()) {
                    String description = volume.getDescription(this);
                    Log.d(TAG, "Detected removable storage: " + description);
                    otgFound = true;
                }
            }
        }
        if (otgFound) {
            Log.i(TAG, "U盘/OTG设备已连接！");
            showTopIcon(TopSettingsIcons.FLASH_DRIVE_ICON);
        } else {
            Log.i(TAG, "未检测到U盘/OTG设备。");
            removeTopIcon(TopSettingsIcons.FLASH_DRIVE_ICON);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void initDeviceState() {
        List<String> permissions = new ArrayList<>();
        // OTG / U盘
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // 蓝牙（Android 12+）
        permissions.add(Manifest.permission.BLUETOOTH_CONNECT);

        PermissionUtils.permission(permissions.toArray(new String[0])).rationale((activity, shouldRequest) -> {
            new MaterialAlertDialogBuilder(activity).setTitle("权限请求").setMessage("需要存储和蓝牙权限以检测U盘和蓝牙遥控器").setPositiveButton("同意", (d, w) -> shouldRequest.again(true)).setNegativeButton("拒绝", (d, w) -> shouldRequest.again(false)).show();
        }).callback(new PermissionUtils.FullCallback() {
            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            @Override
            public void onGranted(@NonNull List<String> granted) {
                Log.d(TAG, "权限已授予: " + granted);
                if (ToolUtils.hasBluetoothPermission(granted)) {
                    checkBluetoothRemote();
                }
                if (ToolUtils.hasStoragePermission(granted)) {
                    checkForOTGDevice();
                }
            }

            @Override
            public void onDenied(@NonNull List<String> deniedForever, @NonNull List<String> denied) {
                Log.w(TAG, "权限被拒绝: " + deniedForever + " / " + denied);
            }
        }).theme(ScreenUtils::setFullScreen).request();

    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    public void checkBluetoothRemote() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();
            for (BluetoothDevice device : bondedDevices) {

                BluetoothClass btClass = device.getBluetoothClass();
                if (btClass == null) continue;

                int deviceClass = btClass.getDeviceClass();
                if (deviceClass == BluetoothClass.Device.PERIPHERAL_KEYBOARD || deviceClass == BluetoothClass.Device.PERIPHERAL_KEYBOARD_POINTING || deviceClass == BluetoothClass.Device.PERIPHERAL_POINTING) {
                    // 👉 启动时直接显示蓝牙图标
                    int index = topSettingsAdapter.itemIndexOfFirst(TopSettingsIcons.BLUETOOTH_ICON);
                    if (index == -1) {
                        List<TopSettingsIcons> items = topSettingsAdapter.getItems();
                        items.add(TopSettingsIcons.BLUETOOTH_ICON);
                        topSettingsAdapter.setItems(items);
                        topSettingsAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onStart() {
        super.onStart();
        initDeviceState();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);    // 插入
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);  // 拔出
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);    // 拔出
        filter.addAction(Intent.ACTION_MEDIA_EJECT);      // 弹出

        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED); // 蓝牙连接
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED); // 蓝牙断开

        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addDataScheme("package");

        filter.addDataScheme("file");
        registerReceiver(usbReceiver, filter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showTopIcon(TopSettingsIcons icon) {
        if (topSettingsAdapter.itemIndexOfFirst(icon) != -1) return;
        List<TopSettingsIcons> items = topSettingsAdapter.getItems();
        items.add(0, icon);
        topSettingsAdapter.setItems(items);
        topSettingsAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void removeTopIcon(TopSettingsIcons icon) {
        int index = topSettingsAdapter.itemIndexOfFirst(icon);
        if (index == -1) return;
        List<TopSettingsIcons> items = topSettingsAdapter.getItems();
        items.remove(index);
        topSettingsAdapter.setItems(items);
        topSettingsAdapter.notifyDataSetChanged();
    }

    private void updateNetworkIcon(TopSettingsIcons newIcon) {
        if (currentNetworkIcon == newIcon) return;

        removeTopIcon(TopSettingsIcons.WIFI_ICON);
        removeTopIcon(TopSettingsIcons.ETHERNET_ICON);

        if (newIcon != null) {
            showTopIcon(newIcon);
        }
        currentNetworkIcon = newIcon;
    }

    private void registerNetworkReceiver() {
        networkMonitor = new NetworkMonitor(this, new ConnectivityManager.NetworkCallback() {

            @Override
            public void onAvailable(@NonNull Network network) {
                Log.d("NetworkMonitor", "网络已连接");
            }

            @Override
            public void onLost(@NonNull Network network) {
                Log.d("NetworkMonitor", "网络已断开");
                removeTopIcon(TopSettingsIcons.WIFI_ICON);
                removeTopIcon(TopSettingsIcons.ETHERNET_ICON);
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities caps) {
                if (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    updateNetworkIcon(TopSettingsIcons.WIFI_ICON);
                } else if (caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    updateNetworkIcon(TopSettingsIcons.ETHERNET_ICON);
                }
            }
        });
        networkMonitor.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkMonitor != null) {
            networkMonitor.unregister();
        }
        if (usbReceiver != null) {
            unregisterReceiver(usbReceiver);
        }
    }

    private void initListener() {
        topSettingsAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            TopSettingsIcons item = baseQuickAdapter.getItem(i);
            if (item == TopSettingsIcons.WIFI_ICON || item == TopSettingsIcons.ETHERNET_ICON) {
                NetworkUtils.openWirelessSettings();
            } else if (item == TopSettingsIcons.FLASH_DRIVE_ICON) {
                ToolUtils.openSystemFileManager(this);
            } else if (item == TopSettingsIcons.SETTINGS_ICON) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (item == TopSettingsIcons.BLUETOOTH_ICON) {
                Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (item == TopSettingsIcons.APPS_ICON) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        appListAdapter.setOnItemLongClickListener((parent, view, position) -> {
            Log.d("MainActivity", "onItemChildLongClick position = " + position);
            return showAppSettingsDialog(parent, view, position);
        });
        appListAdapter.setOnItemClickListener((parent, view, position) -> {
            Log.d("MainActivity", "onItemClick position = " + position);
            AppInfo appInfo = parent.getItem(position);
            if (appInfo.getPackageName().isEmpty()) {
                if (appInfo.getName().equals("系统应用")) {
                    showSystemApps();
                } else if (appInfo.getName().equals("壁纸")) {
                    showWallPager();
                }
            } else {
                AppUtils.launchApp(appInfo.getPackageName());
            }
        });

        favoriteAppsAdapter.setOnItemLongClickListener((baseQuickAdapter, view, position) -> {
            Log.d("MainActivity", "onItemChildLongClick position = " + position);
            return showAppSettingsDialog(baseQuickAdapter, view, position);
        });
        favoriteAppsAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            if (isMoveApp) {
                isMoveApp = false;
                ToolUtils.endAnimation(view);
                return;
            }
            AppInfo item = baseQuickAdapter.getItem(i);
            if (item.getPackageName().isEmpty()) {
                previewPanel.setVisibility(View.GONE);
                View inflate = LayoutInflater.from(this).inflate(R.layout.activity_dialog_all_app, null);
                TvRecyclerView allDialogGrid = inflate.findViewById(R.id.all_dialog_grid);
                allDialogGrid.setLayoutManager(new V7GridLayoutManager(this, 2, V7GridLayoutManager.HORIZONTAL, false));
                AppIconAdapter dialogAppIconAdapter = new AppIconAdapter();
                allDialogGrid.setAdapter(dialogAppIconAdapter);
                allApps = Lists.newArrayList(Iterables.filter(appListAdapter.getItems(), appInfo -> {
                    if (appInfo != null) {
                        return !appInfo.getPackageName().isEmpty();
                    }
                    return false;
                }));
                dialogAppIconAdapter.setItems(allApps);
                dialogAppIconAdapter.setOnItemClickListener((baseQuickAdapter1, view1, i1) -> addFavoriteApp(baseQuickAdapter1, i1, favoriteAppsAdapter));
                allDialogGrid.setOnItemListener(new TvOnItemListener());
                allDialogGrid.requestFocus();
                showMaterialAlertDialog(this, "所有应用", inflate);
            } else {
                AppUtils.launchApp(item.getPackageName());
            }
        });

        topSettingsBar.setOnItemListener(new TvOnItemListener());
        appListGrid.setOnItemListener(new TvOnItemListener());
        favoriteAppsGrid.setOnItemListener(new TvOnItemListener());
    }

    private void showWallPager() {
        startActivity(new Intent(this, WallPagerActivity.class));
    }

    private void showSystemApps() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_dialog_all_app, null);
        TvRecyclerView allDialogGrid = inflate.findViewById(R.id.all_dialog_grid);
        allDialogGrid.setLayoutManager(new V7GridLayoutManager(this, 4));
        AppIconAdapter dialogAppIconAdapter = new AppIconAdapter();
        allDialogGrid.setAdapter(dialogAppIconAdapter);
        dialogAppIconAdapter.setItems(systemApps);
        dialogAppIconAdapter.setOnItemLongClickListener((baseQuickAdapter, view2, i) -> {
            Log.d("MainActivity", "onItemChildLongClick position = " + i);
            return showAppSettingsDialog(baseQuickAdapter, view2, i);
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
        allAppsContainer.setVisibility(View.INVISIBLE);
        previewPanel.setVisibility(View.INVISIBLE);
        showMaterialAlertDialog(this, "系统应用", inflate);
    }

    private int movePosition = 0;


    public Dialog showMaterialAlertDialog(Context context, String titleName, View rootView) {
        Dialog dialog = new Dialog(context, R.style.CustomDialogTheme);
        dialog.setContentView(rootView);
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (titleName.equals("系统应用")) {
                    allAppsContainer.setVisibility(View.VISIBLE);
                    appListGrid.requestFocus();
                } else {
                    Log.d(TAG, "showMaterialAlertDialog: " + titleName);
                }
            }
            return false;
        });
        dialog.show();
        return dialog;
    }

    @SuppressLint("NotifyDataSetChanged")
    private boolean showAppSettingsDialog(BaseQuickAdapter<AppInfo, ?> parent, View view, int position) {
        previewPanel.setVisibility(View.INVISIBLE);
        AppInfo appInfo = parent.getItem(position);
        if (appInfo.getPackageName().isEmpty()) {
            return false;
        }
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_apps_settings, null);
        Dialog dialog = showMaterialAlertDialog(this, "应用设置", inflate);

        MaterialCardView card = inflate.findViewById(R.id.card);
        card.setCardBackgroundColor(appInfo.getCardColor());
        Drawable drawable;
        ImageView imageView;
        if (appInfo.isBanner()) {
            inflate.findViewById(R.id.preview_app_icon).setVisibility(View.GONE);
            imageView = inflate.findViewById(R.id.preview_banner_icon);
            imageView.setVisibility(View.VISIBLE);
            drawable = ToolUtils.getBase64ToDrawable(appInfo.getAppBannerBase64());
        } else {
            inflate.findViewById(R.id.preview_banner_icon).setVisibility(View.GONE);
            imageView = inflate.findViewById(R.id.preview_app_icon);
            imageView.setVisibility(View.VISIBLE);
            drawable = ToolUtils.getBase64ToDrawable(appInfo.getAppIconBase64());
        }
        Glide.with(this).load(drawable).into(imageView);
        TextView previewTitle = inflate.findViewById(R.id.preview_title);
        previewTitle.setText(appInfo.getName());
        TextView previewDesc = inflate.findViewById(R.id.preview_desc);
        previewDesc.setText(appInfo.getPackageName());

        TvRecyclerView previewSettingsRecyclerview = inflate.findViewById(R.id.preview_settings_recyclerview);

        previewSettingsRecyclerview.setOnInBorderKeyEventListener(new ViewAnimationShake(previewSettingsRecyclerview, this));
        previewSettingsRecyclerview.setLayoutManager(new V7LinearLayoutManager(this, V7LinearLayoutManager.VERTICAL, false));
        previewSettingsRecyclerview.setOnItemListener(new TvOnItemListener());
        PreviewSettingsAdapter previewSettingsAdapter = new PreviewSettingsAdapter();
        previewSettingsRecyclerview.setAdapter(previewSettingsAdapter);

        previewSettingsAdapter.setItems(Arrays.asList(PreviewSettings.values()));
        previewSettingsRecyclerview.requestFocus();
        previewSettingsAdapter.setOnItemClickListener((baseQuickAdapter, view1, which) -> {
            PreviewSettings previewSettings = baseQuickAdapter.getItem(which);
            switch (previewSettings) {
                case START:
                    AppUtils.launchApp(appInfo.getPackageName());
                    break;
                case VIEW:
                    AppUtils.launchAppDetailsSettings(appInfo.getPackageName());
                    break;
                case MOVE:
                    isMoveApp = true;
                    movePosition = position;
                    break;
                case DELETE:
                    AppInfo favoriteAppInfo = favoriteAppsAdapter.getItem(position);
                    favoriteAppsAdapter.remove(favoriteAppInfo);
                    favoriteAppsAdapter.notifyDataSetChanged();
                    new Thread(() -> favoriteAppInfoDao.delete(appInfo)).start();
                    break;
                case UNINSTALL:
                    if (appInfo.isSystem()) {
                        Toast.makeText(this, "系统应用无法卸载", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ToolUtils.uninstallApp(this, appInfo.getPackageName());
//                    new MaterialAlertDialogBuilder(this).setTitle("卸载应用").setMessage("确定要卸载「" + appInfo.getName() + "」吗？").setPositiveButton("卸载", (d, w) -> {
//
//                    }).setNegativeButton("取消", null).show();
                    break;
            }
            dialog.dismiss();
        });

        return true;
    }

    private int moveToPosition = 0;

    @SuppressLint("NotifyDataSetChanged")
    private void initData() {
        appDataBase = AppDataBase.getInstance(this);
        favoriteAppInfoDao = appDataBase.getFavoriteAppInfoDao();
        topSettingsAdapter = new SettingsIconAdapter();
        topSettingsAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.SlideInLeft);
        appListAdapter = new AppIconAdapter();
        favoriteAppsAdapter = new AppIconAdapter();

        topSettingsBar.setAdapter(topSettingsAdapter);
        appListGrid.setAdapter(appListAdapter);
        favoriteAppsGrid.setAdapter(favoriteAppsAdapter);

        topSettingsAdapter.setItems(new ArrayList<>(List.of(TopSettingsIcons.getTopSettings())));

        new Thread(() -> {
            List<AppInfo> tempAllApps = AppsUtils.getAppsInfo(this);
            allApps = Lists.newArrayList(Iterables.filter(tempAllApps, appInfo -> {
                if (appInfo != null) {
                    return !appInfo.isSystem();
                }
                return false;
            }));
            systemApps = Lists.newArrayList(Iterables.filter(tempAllApps, AppAllInfo -> AppAllInfo != null && AppAllInfo.isSystem()));
            Iterator<AppInfo> iterator = systemApps.iterator();
            while (iterator.hasNext()) {
                AppInfo next = iterator.next();
                boolean appLaunchable = ToolUtils.isAppLaunchable(this, next.getPackageName());
                if (!appLaunchable) {
                    iterator.remove();
                }
            }
            allApps.add(allApps.size(), ToolUtils.getEmptyAppInfo("壁纸", ResourceUtils.getDrawable(R.drawable.ic_wall_art_24dp), Color.parseColor("#EF4444")));
            allApps.add(allApps.size(), ToolUtils.getEmptyAppInfo("系统应用", ResourceUtils.getDrawable(R.drawable.ic_apps_24dp), Color.parseColor("#0EA5E9")));
            List<AppInfo> favoriteAppInfos = favoriteAppInfoDao.getAllFavoriteAppInfo();
            Collections.sort(favoriteAppInfos, (o1, o2) -> {
                int index1 = o1.getSortIndex();
                int index2 = o2.getSortIndex();
                return Integer.compare(index1, index2);
            });
            for (AppInfo favoriteAppInfo : favoriteAppInfos) {
                Log.d(TAG, "initData: " + favoriteAppInfo.getName() + " " + favoriteAppInfo.getSortIndex());
            }

            favoriteApps.addAll(favoriteAppInfos);
            favoriteApps.add(favoriteApps.size(), ToolUtils.getEmptyAppInfo("添加应用", ResourceUtils.getDrawable(R.drawable.ic_add_24dp), Color.parseColor("#263238")));
            Log.d(TAG, "initData: 数据处理完成");
            runOnUiThread(() -> {
                Log.d(TAG, "initData: 更新UI");
                appListAdapter.setItems(allApps);
                appListAdapter.notifyDataSetChanged();
                favoriteAppsAdapter.setItems(favoriteApps);
                favoriteAppsAdapter.notifyDataSetChanged();
                favoriteAppsGrid.requestFocus();
            });
        }).start();

        OkGo.<String>get("https://node.video.qq.com/x/api/hot_search").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body();
                        Log.d(TAG, "onSuccess: " + body);
                        HotSearchEntity hotSearchEntity = new Gson().fromJson(body, HotSearchEntity.class);
                        if (hotSearchEntity != null) {
                            HotSearchEntity.DataBean data = hotSearchEntity.getData();
                            if (data.getErrCode() == 0) {
                                HotSearchEntity.DataBean.MapResultBean mapResult = data.getMapResult();
                                HotSearchEntity.DataBean.MapResultBean._$0Bean mapResult_$0 = mapResult.get_$0();
                                String channelTitle = mapResult_$0.getChannelTitle();
                                List<HotSearchEntity.DataBean.MapResultBean._$0Bean.ListInfoBean> listInfo = mapResult_$0.getListInfo();
                                for (HotSearchEntity.DataBean.MapResultBean._$0Bean.ListInfoBean listInfoBean : listInfo) {
                                    Log.d(TAG, "onSuccess: " + channelTitle + " " + listInfoBean.getTitle());
                                }
                                return;
                            }
                        }
                    }
                } catch (Exception ignored) {
                }
                Toast.makeText(MainActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Toast.makeText(MainActivity.this, "获取异常", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addFavoriteApp(@NonNull BaseQuickAdapter<AppInfo, ?> baseQuickAdapter, int i, AppIconAdapter favoriteAppsAdapter) {
        AppInfo adapterItem = baseQuickAdapter.getItem(i);
        List<AppInfo> items = favoriteAppsAdapter.getItems();
        if (!adapterItem.getPackageName().isEmpty()) {
            ArrayList<AppInfo> appInfoArrayList = Lists.newArrayList(Iterables.filter(items, appInfo -> {
                if (appInfo != null) {
                    return appInfo.getPackageName().equals(adapterItem.getPackageName());
                }
                return false;
            }));
            if (appInfoArrayList.isEmpty()) {
                int itemCount = favoriteAppsAdapter.getItemCount();
                favoriteAppsAdapter.add(itemCount - 1, adapterItem);
                adapterItem.setSortIndex(itemCount - 1);
                new Thread(() -> favoriteAppInfoDao.insert(adapterItem)).start();
            } else {
                Toast.makeText(this, adapterItem.getName() + " 已添加", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isMoveApp) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (movePosition > 0) {
                    moveToPosition = movePosition - 1;
                    moveItem();
                }
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (movePosition < favoriteApps.size() - 2) {
                    moveToPosition = movePosition + 1;
                    moveItem();
                }
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            showSettings();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void showSettings() {
        Toast.makeText(this, "打开软件设置", Toast.LENGTH_SHORT).show();
    }

    private void moveItem() {
        if (movePosition == moveToPosition) return;

        List<AppInfo> list = favoriteAppsAdapter.getItems();

        AppInfo fromItem = list.get(movePosition);
        AppInfo toItem = list.get(moveToPosition);

        // 1. 交换 sortIndex（以 UI 顺序为准）
        int fromIndex = fromItem.getSortIndex();
        fromItem.setSortIndex(toItem.getSortIndex());
        toItem.setSortIndex(fromIndex);

        // 2. 交换列表顺序
        Collections.swap(list, movePosition, moveToPosition);

        // 3. 通知 RecyclerView
        favoriteAppsAdapter.notifyItemMoved(movePosition, moveToPosition);

        // 4. 修正 position
        movePosition = moveToPosition;

        // 6. 串行更新数据库（防抖）
        updateSortIndexAsync(fromItem, toItem);
    }

    private void updateSortIndexAsync(AppInfo a, AppInfo b) {
        dbExecutor.execute(() -> {
            favoriteAppInfoDao.update(a);
            favoriteAppInfoDao.update(b);
        });
    }

    @SuppressLint("SetTextI18n")
    private void showPreview(AppInfo appInfo) {
        previewPanel.setVisibility(View.VISIBLE);
        Drawable drawable = ToolUtils.getBase64ToDrawable(appInfo.isBanner() ? appInfo.getAppBannerBase64() : appInfo.getAppIconBase64());
        Glide.with(this).load(drawable).into(previewIcon);
        previewTitle.setText(appInfo.getName());
        previewDesc.setText(appInfo.getPackageName() + " " + appInfo.getVersionName());

        // 动画：淡入 + 放大
        previewPanel.setScaleX(0.9f);
        previewPanel.setScaleY(0.9f);
        previewPanel.setAlpha(0f);

        previewPanel.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(200).start();
    }

    private void showAllApps() {
        if (previewPanel.getVisibility() == View.VISIBLE) previewPanel.setVisibility(View.GONE);
        topSettingsBar.setVisibility(View.GONE);
        int screenHeight = ScreenUtils.getScreenHeight();
        Log.d(TAG, "showAllApps: " + screenHeight);
        favoriteAppsContainer.animate().translationY(-screenHeight).setDuration(500).start();
        allAppsContainer.animate().translationY(0).setDuration(500).start();
        appListGrid.requestFocus();
    }

    private void showHomeApps() {
        topSettingsBar.setVisibility(View.VISIBLE);
        int screenHeight = ScreenUtils.getScreenHeight();
        favoriteAppsContainer.animate().translationY(0).setDuration(500).start();
        allAppsContainer.animate().translationY(screenHeight).setDuration(500).start();
        favoriteAppsGrid.requestFocus();
    }


    private void initDefaleHome() {
        boolean defaultHome = ToolUtils.isDefaultHome(this);
        Log.d("MainActivity", "defaultHome = " + defaultHome);
        if (!defaultHome) {
            new MaterialAlertDialogBuilder(this, R.style.CustomDialogTheme).setTitle("设置为默认桌面").setMessage("是否要设置为默认桌面？").setPositiveButton("是", (dialog, which) -> ToolUtils.goToHomeSettings(this)).setNegativeButton("否", null).show();
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