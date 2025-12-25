package com.jayjd.boxtop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
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
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

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
import com.jayjd.boxtop.adapter.AppIconAdapter;
import com.jayjd.boxtop.adapter.InfoCardPagerAdapter;
import com.jayjd.boxtop.adapter.PreviewSettingsAdapter;
import com.jayjd.boxtop.adapter.SettingsIconAdapter;
import com.jayjd.boxtop.cards.CardConnectivity;
import com.jayjd.boxtop.cards.CardDevice;
import com.jayjd.boxtop.cards.CardPerformance;
import com.jayjd.boxtop.cards.CardStorage;
import com.jayjd.boxtop.cards.CardSystem;
import com.jayjd.boxtop.cards.CardWeather;
import com.jayjd.boxtop.dao.AllAppsInfoDao;
import com.jayjd.boxtop.dao.FavoriteAppInfoDao;
import com.jayjd.boxtop.database.AppDataBase;
import com.jayjd.boxtop.entity.AppInfo;
import com.jayjd.boxtop.entity.FavoriteApp;
import com.jayjd.boxtop.enums.PreviewSettings;
import com.jayjd.boxtop.enums.TopSettingsIcons;
import com.jayjd.boxtop.listeners.TvOnItemListener;
import com.jayjd.boxtop.listeners.UsbDriveListener;
import com.jayjd.boxtop.listeners.ViewAnimationShake;
import com.jayjd.boxtop.receiver.UsbBroadcastReceiver;
import com.jayjd.boxtop.utils.AppsUtils;
import com.jayjd.boxtop.utils.BlurCompat;
import com.jayjd.boxtop.utils.DotContainerUtils;
import com.jayjd.boxtop.utils.LicenseEvent;
import com.jayjd.boxtop.utils.NetworkMonitor;
import com.jayjd.boxtop.utils.PrivacyPasswordManager;
import com.jayjd.boxtop.utils.ProDialog;
import com.jayjd.boxtop.utils.PurchaseManager;
import com.jayjd.boxtop.utils.ToolUtils;
import com.jayjd.boxtop.utils.cpu.CpuMonitor;
import com.jayjd.boxtop.wallpaper.WallpaperActivity;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7GridLayoutManager;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    LinearLayout allAppsContainer;
    ConstraintLayout favoriteAppsContainer;
    private final List<AppInfo> favoriteApps = new ArrayList<>();
    TvRecyclerView appListGrid;
    TvRecyclerView favoriteAppsGrid;
    TvRecyclerView topSettingsBar;
    AppIconAdapter appListAdapter;
    AppIconAdapter favoriteAppsAdapter;
    SettingsIconAdapter topSettingsAdapter;
    private List<AppInfo> allApps = new ArrayList<>();
    private List<AppInfo> systemApps = new ArrayList<>();
    AllAppsInfoDao allAppsInfoDao;
    private boolean isMoveApp = false;


    AppDataBase appDataBase;
    FavoriteAppInfoDao favoriteAppInfoDao;
    AppIconAdapter dialogAppIconAdapter;
    private List<AppInfo> hiddenApps = new ArrayList<>();
    ImageView wallPager;
    ViewPager2 viewPagerCards;
    CpuMonitor cpuMonitor;


    @Override
    protected void onResume() {
        super.onResume();
        ToolUtils.initWallPager(this, wallPager);
    }

    @Override
    protected void onPause() {
        super.onPause();

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT);
        }
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
                        topSettingsAdapter.submitList(items);
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
        topSettingsAdapter.submitList(items);
        topSettingsAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void removeTopIcon(TopSettingsIcons icon) {
        int index = topSettingsAdapter.itemIndexOfFirst(icon);
        if (index == -1) return;
        List<TopSettingsIcons> items = topSettingsAdapter.getItems();
        items.remove(index);
        topSettingsAdapter.submitList(items);
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

    @NonNull
    private static List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CardWeather());
        fragments.add(new CardStorage());
        fragments.add(new CardPerformance());
        fragments.add(new CardDevice());
        fragments.add(new CardConnectivity());
        fragments.add(new CardSystem());
        return fragments;
    }

    FrameLayout viewPagerContainer;
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
                topSettingsAdapter.add(0, TopSettingsIcons.BLUETOOTH_ICON);
                topSettingsAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "蓝牙已连接", Toast.LENGTH_SHORT).show();
            } else {
                // 蓝牙断开
                topSettingsAdapter.remove(TopSettingsIcons.BLUETOOTH_ICON);
                topSettingsAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "蓝牙已断开", Toast.LENGTH_SHORT).show();
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onInstalled(Context context, String pkg) {
            AppInfo appInfo = AppsUtils.getAppInfo(context, pkg);
            if (appInfo == null) return;
            appListAdapter.add(0, appInfo);
            appListAdapter.notifyDataSetChanged();
            new Thread(() -> allAppsInfoDao.insert(appInfo)).start();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onUninstalled(Context context, String pkg) {
            List<AppInfo> allApps = appListAdapter.getItems();
            AppInfo appInfo = findByPackage(allApps, pkg);
            if (appInfo != null) {
                appListAdapter.remove(appInfo);
                appListAdapter.notifyDataSetChanged();
                new Thread(() -> allAppsInfoDao.deleteByPackageName(pkg)).start();
            }

            List<AppInfo> favoriteApps = favoriteAppsAdapter.getItems();
            AppInfo fav = findByPackage(favoriteApps, pkg);
            if (fav != null) {
                favoriteAppsAdapter.remove(fav);
                favoriteAppsAdapter.notifyDataSetChanged();
                new Thread(() -> favoriteAppInfoDao.deleteByPackageName(pkg)).start();
            }
            if (dialogAppIconAdapter != null) {
                List<AppInfo> items = dialogAppIconAdapter.getItems();
                AppInfo hide = findByPackage(items, pkg);
                if (hide != null) {
                    dialogAppIconAdapter.remove(hide);
                    dialogAppIconAdapter.notifyDataSetChanged();
                    new Thread(() -> allAppsInfoDao.deleteByPackageName(pkg)).start();
                }
            }
        }


        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onUpdated(Context context, String pkg) {

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
    });

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
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
            AppInfo appInfo = parent.getItem(position);
            if (appInfo.getIsSystem() == 1) return false;
            return showAppSettingsDialog(parent, position, PreviewSettings.getAllAppsSettings());
        });
        appListAdapter.setOnItemClickListener((parent, view, position) -> {
            Log.d("MainActivity", "onItemClick position = " + position);
            AppInfo appInfo = parent.getItem(position);
            if (appInfo.getPackageName().isEmpty()) {
                switch (appInfo.getName()) {
                    case "系统应用" -> showSystemApps();
                    case "壁纸" -> showWallPager();
                    case "隐私空间" -> showPrivacySpace();
                }
            } else {
                AppUtils.launchApp(appInfo.getPackageName());
                // 根据 OpenAppCount 排序
                sortData(appInfo);
            }
        });

        favoriteAppsAdapter.setOnItemLongClickListener((baseQuickAdapter, view, position) -> {
            Log.d("MainActivity", "onItemChildLongClick position = " + position);
            AppInfo item = baseQuickAdapter.getItem(position);
            return showAppSettingsDialog(baseQuickAdapter, position, PreviewSettings.getFavoriteSettings(item.getIsSystem() == 1));
        });
        favoriteAppsAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            if (isMoveApp) {
                isMoveApp = false;
                ToolUtils.endNormalAnimation(view);
                return;
            }
            AppInfo item = baseQuickAdapter.getItem(i);
            if (item.getPackageName().isEmpty()) {
                View inflate = LayoutInflater.from(this).inflate(R.layout.activity_dialog_all_app, null);
                TvRecyclerView allDialogGrid = inflate.findViewById(R.id.all_dialog_grid);
                int or = appListAdapter.getItemCount() <= 5 ? 1 : 2;
                allDialogGrid.setLayoutManager(new V7GridLayoutManager(this, or, V7GridLayoutManager.HORIZONTAL, false));
                AppIconAdapter dialogAppIconAdapter = new AppIconAdapter();
                allDialogGrid.setAdapter(dialogAppIconAdapter);
                List<AppInfo> tempAppList = Lists.newArrayList(Iterables.filter(appListAdapter.getItems(), appInfo -> {
                    if (appInfo != null) {
                        return !appInfo.getPackageName().isEmpty();
                    }
                    return false;
                }));
                tempAppList.addAll(tempAppList.size(), systemApps);
                dialogAppIconAdapter.submitList(tempAppList);
                dialogAppIconAdapter.setOnItemClickListener((baseQuickAdapter1, view1, i1) -> addFavoriteApp(baseQuickAdapter1, i1, favoriteAppsAdapter));
                allDialogGrid.setOnInBorderKeyEventListener(new ViewAnimationShake(allDialogGrid, this));
                allDialogGrid.setOnItemListener(new TvOnItemListener());
                allDialogGrid.requestFocus();
                showMaterialAlertDialog(this, "所有应用", inflate);
            } else {
                AppUtils.launchApp(item.getPackageName());
                // 根据 OpenAppCount 排序
                sortData(item);
            }
        });

        topSettingsBar.setOnItemListener(new TvOnItemListener());
        appListGrid.setOnItemListener(new TvOnItemListener());
        favoriteAppsGrid.setOnItemListener(new TvOnItemListener());

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                refreshAll();
            }
        }, new IntentFilter(LicenseEvent.ACTION_PRO_UNLOCKED));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void sortData(AppInfo appInfo) {
        appInfo.setOpenAppCount(appInfo.getOpenAppCount() + 1);
        if (PurchaseManager.getInstance().isPro()) {
            Collections.sort(appListAdapter.getItems(), (o1, o2) -> Long.compare(o2.getOpenAppCount(), o1.getOpenAppCount()));
            dbExecutor.execute(() -> allAppsInfoDao.updateOpenAppCountByPackageName(appInfo.getPackageName(), appInfo.getOpenAppCount()));
        }
        appListAdapter.notifyDataSetChanged();
    }

    private void showWallPager() {
        startActivity(new Intent(this, WallpaperActivity.class));
    }

    private void showError(TextView tvError, String msg) {
        tvError.setText(msg);
        tvError.setVisibility(View.VISIBLE);
    }

    private void showPrivacySpace() {
        if (!PurchaseManager.getInstance().isPro()) {
            ProDialog.show(this);
            return;
        }

        PrivacyPasswordManager privacyPasswordManager = new PrivacyPasswordManager(this);
        if (!privacyPasswordManager.hasPassword()) {
            // 引导用户设置密码
            configPrivacyPwd(privacyPasswordManager);
        } else {
            View inflate = LayoutInflater.from(this).inflate(R.layout.privacy_verify_password, null);
            Dialog dialog = showMaterialAlertDialog(this, "隐私空间 - 输入密码", inflate);
            TextView etPassword = inflate.findViewById(R.id.et_password);
            Button btnConfirm = inflate.findViewById(R.id.btn_confirm);
            TextView tvError = inflate.findViewById(R.id.tv_error);

            etPassword.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
                    btnConfirm.requestFocus();
                    return true;
                }
                return false;
            });

            etPassword.requestFocus();
            btnConfirm.setOnClickListener(v -> {
                String pwd = etPassword.getText().toString();
                if (pwd.isEmpty()) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean b = privacyPasswordManager.verifyPassword(pwd);
                if (!b) {
                    tvError.setVisibility(View.VISIBLE);
                } else {
                    tvError.setVisibility(View.GONE);
                    dialog.dismiss();
                    showPrivacyContent();
                }
            });
        }
    }

    private void initView() {
        wallPager = findViewById(R.id.wall_pager);
        // 功能区域
        viewPagerCards = findViewById(R.id.view_pager_cards);
        LinearLayout dotContainer = findViewById(R.id.dot_container);
        viewPagerContainer = findViewById(R.id.view_pager_container);
        List<Fragment> fragments = getFragments();
        DotContainerUtils.bindViewPager(viewPagerCards, dotContainer, fragments.size());
        InfoCardPagerAdapter infoCardPagerAdapter = new InfoCardPagerAdapter(this, fragments);
        viewPagerCards.setAdapter(infoCardPagerAdapter);
        viewPagerContainer.setOnFocusChangeListener((v, hasFocus) -> {
            float scale = hasFocus ? 1.05f : 1.0f;
            v.animate().scaleX(scale).scaleY(scale).setDuration(150).start();
            v.setElevation(hasFocus ? 20f : 0f);
        });
        // ❗TV 必须关掉用户滑动（用遥控器控制）
//        viewPagerCards.setUserInputEnabled(false);

        // 预加载（避免切换卡顿）
        viewPagerCards.setOffscreenPageLimit(fragments.size());

        viewPagerContainer.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN) {
                return false;
            }

            int current = viewPagerCards.getCurrentItem();

            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (current < infoCardPagerAdapter.getItemCount() - 1) {
                    viewPagerCards.setCurrentItem(current + 1, true);
                }
                return true; // ✅ 消费事件
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (current > 0) {
                    viewPagerCards.setCurrentItem(current - 1, true);
                    return true; // ✅ 消费事件
                }
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                topSettingsBar.requestFocus();
                return true; // ✅ 消费事件
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                favoriteAppsGrid.requestFocus();
                return true; // ✅ 消费事件
            } else if (keyCode == KeyEvent.KEYCODE_MENU) {
                int currentItem = viewPagerCards.getCurrentItem();
                Log.d(TAG, "当前展示的界面是：" + currentItem);
                return true; // ✅ 消费事件
            }
            return false; // ❗ 其他情况放行
        });

        viewPagerCards.setPageTransformer((page, position) -> {
            float scale = 0.9f + (1 - Math.abs(position)) * 0.1f;
            page.setScaleX(scale);
            page.setScaleY(scale);
        });


        // 顶部设置按钮
        topSettingsBar = findViewById(R.id.top_settings_lists);
        // 常用的软件
        favoriteAppsContainer = findViewById(R.id.favorite_apps_container);
        favoriteAppsGrid = findViewById(R.id.favorite_apps_grid);
        // 所有软件的布局和列表
        allAppsContainer = findViewById(R.id.all_apps_container);
        appListGrid = findViewById(R.id.all_apps_grid);

        topSettingsBar.setLayoutManager(new V7LinearLayoutManager(this, V7LinearLayoutManager.HORIZONTAL, false));
        favoriteAppsGrid.setLayoutManager(new V7LinearLayoutManager(this, V7LinearLayoutManager.HORIZONTAL, false));
        appListGrid.setLayoutManager(new V7GridLayoutManager(this, 5));
        topSettingsBar.setOnInBorderKeyEventListener(new ViewAnimationShake(topSettingsBar, this) {
            @Override
            public boolean onInBorderKeyEvent(int direction, View focused) {
                if (direction == View.FOCUS_DOWN) {
                    viewPagerContainer.requestFocus();
                    return true;
                }
                return super.onInBorderKeyEvent(direction, focused);
            }
        });
        favoriteAppsGrid.setOnInBorderKeyEventListener(new ViewAnimationShake(favoriteAppsGrid, this) {
            @Override
            public boolean onInBorderKeyEvent(int direction, View focused) {
                if (direction == View.FOCUS_DOWN) {
                    showAllApps();
                    return true;
                } else if (direction == View.FOCUS_UP) {
                    viewPagerContainer.requestFocus();
                    return true;
                }
                return super.onInBorderKeyEvent(direction, focused);
            }
        });
        appListGrid.setOnInBorderKeyEventListener(new ViewAnimationShake(appListGrid, this) {
            @Override
            public boolean onInBorderKeyEvent(int direction, View focused) {
                if (direction == View.FOCUS_UP) {
                    showHomeApps();
                    return true;
                } else if (direction == View.FOCUS_DOWN) {
                    return true;
                }
                return super.onInBorderKeyEvent(direction, focused);
            }
        });
        allAppsContainer.post(() -> {
            int screenHeight = ScreenUtils.getScreenHeight();
            allAppsContainer.setTranslationY(screenHeight);
            allAppsContainer.setVisibility(View.VISIBLE);
        });
    }

    private void showPrivacyContent() {
        if (hiddenApps.isEmpty()) {
            Toast.makeText(this, "隐私空间无应用", Toast.LENGTH_SHORT).show();
            return;
        }
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_dialog_all_app, null);
        showMaterialAlertDialog(this, "隐私空间", inflate);
        TvRecyclerView allDialogGrid = inflate.findViewById(R.id.all_dialog_grid);
        allDialogGrid.setLayoutManager(new V7GridLayoutManager(this, 5));
        dialogAppIconAdapter = new AppIconAdapter();
        allDialogGrid.setAdapter(dialogAppIconAdapter);
        dialogAppIconAdapter.submitList(hiddenApps);
        dialogAppIconAdapter.setOnItemLongClickListener((baseQuickAdapter, view2, i) -> {
            Log.d("MainActivity", "onItemChildLongClick position = " + i);
            return showAppSettingsDialog(baseQuickAdapter, i, PreviewSettings.getHideAppsSettings());
        });
        dialogAppIconAdapter.setOnItemClickListener((baseQuickAdapter1, view1, i1) -> {
            AppInfo item = baseQuickAdapter1.getItem(i1);
            if (item.getPackageName().isEmpty()) {
                return;
            }
            AppUtils.launchApp(item.getPackageName());
            sortData(item);
        });
        allDialogGrid.setOnInBorderKeyEventListener(new ViewAnimationShake(allDialogGrid, this));
        allDialogGrid.setOnItemListener(new TvOnItemListener());
        allDialogGrid.requestFocus();
        allAppsContainer.setVisibility(View.INVISIBLE);
    }

    private void configPrivacyPwd(PrivacyPasswordManager privacyPasswordManager) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.privacy_set_password, null);
        Dialog dialog = showMaterialAlertDialog(this, "隐私空间 - 设置密码", inflate);
        dialog.setCancelable(false);
        EditText etPassword = inflate.findViewById(R.id.et_password);
        EditText etConfirm = inflate.findViewById(R.id.et_confirm_password);
        TextView tvError = inflate.findViewById(R.id.tv_error);
        Button btnConfirm = inflate.findViewById(R.id.btn_confirm);
        etPassword.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        etConfirm.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

        etPassword.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
                etConfirm.requestFocus();
                return true;
            }
            return false;
        });

        etConfirm.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
                etPassword.requestFocus();
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
                btnConfirm.requestFocus();
                return true;
            }
            return false;
        });


        /* ===== 默认焦点（TV 必须） ===== */
        etPassword.requestFocus();

        /* ===== 确认逻辑 ===== */
        btnConfirm.setOnClickListener(v -> {
            String p1 = etPassword.getText().toString().trim();
            String p2 = etConfirm.getText().toString().trim();

            if (p1.length() < 4) {
                showError(tvError, "密码至少 4 位");
                etPassword.requestFocus();
                return;
            }

            if (!p1.equals(p2)) {
                showError(tvError, "两次密码不一致");
                etConfirm.requestFocus();
                return;
            }

            tvError.setVisibility(View.GONE);

            // 保存密码
            privacyPasswordManager.setPassword(p1);
            dialog.dismiss();
        });

        /* ===== 遥控器 Enter 键支持 ===== */
        etConfirm.setOnEditorActionListener((v, actionId, event) -> {
            btnConfirm.performClick();
            return true;
        });
    }

    private void showSystemApps() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_dialog_all_app, null);
        TvRecyclerView allDialogGrid = inflate.findViewById(R.id.all_dialog_grid);
        allDialogGrid.setLayoutManager(new V7GridLayoutManager(this, 5));
        AppIconAdapter dialogAppIconAdapter = new AppIconAdapter();
        allDialogGrid.setAdapter(dialogAppIconAdapter);
        dialogAppIconAdapter.submitList(systemApps);
        dialogAppIconAdapter.setOnItemLongClickListener((baseQuickAdapter, view2, i) -> {
            Log.d("MainActivity", "onItemChildLongClick position = " + i);
            AppInfo appInfo = baseQuickAdapter.getItem(i);
            if (appInfo.getIsSystem() == 1) return false;
            return showAppSettingsDialog(baseQuickAdapter, i, PreviewSettings.getAllAppsSettings());
        });
        dialogAppIconAdapter.setOnItemClickListener((baseQuickAdapter1, view1, i1) -> {
            AppInfo item = baseQuickAdapter1.getItem(i1);
            if (item.getPackageName().isEmpty()) {
                return;
            }
            // 系统应用不参与排序
            AppUtils.launchApp(item.getPackageName());
        });
        allDialogGrid.setOnInBorderKeyEventListener(new ViewAnimationShake(allDialogGrid, this));
        allDialogGrid.setOnItemListener(new TvOnItemListener());
        allDialogGrid.requestFocus();
        allAppsContainer.setVisibility(View.INVISIBLE);
        showMaterialAlertDialog(this, "系统应用", inflate);
    }

    private int movePosition = 0;


    public Dialog showMaterialAlertDialog(Context context, String titleName, View rootView) {
        int styleId;
        if (titleName.equals("应用设置")) styleId = R.style.CustomDialogTheme;
        else styleId = R.style.CustomAppDialogTheme;
        TextView allAppsTitle = rootView.findViewById(R.id.all_apps_title);
        if (allAppsTitle != null) allAppsTitle.setText(titleName);
        Dialog dialog = new Dialog(context, styleId);
        dialog.setContentView(rootView);
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (titleName.equals("系统应用")) {
                    allAppsContainer.setVisibility(View.VISIBLE);
                    appListGrid.requestFocus();
                } else if (titleName.equals("隐私空间")) {
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

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private boolean showAppSettingsDialog(BaseQuickAdapter<AppInfo, ?> parent, int position, PreviewSettings[] previewSettings) {
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
        if (appInfo.getIsBanner() == 1) {
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
        TextView previewVersion = inflate.findViewById(R.id.preview_version);
        previewVersion.setText("版本 " + appInfo.getVersionName() + "(" + appInfo.getVersionCode() + ") 启动次数 " + appInfo.getOpenAppCount());


        TvRecyclerView previewSettingsRecyclerview = inflate.findViewById(R.id.preview_settings_recyclerview);

        previewSettingsRecyclerview.setOnInBorderKeyEventListener(new ViewAnimationShake(previewSettingsRecyclerview, this));
        previewSettingsRecyclerview.setLayoutManager(new V7GridLayoutManager(this, 2));
        previewSettingsRecyclerview.setOnItemListener(new TvOnItemListener());
        PreviewSettingsAdapter previewSettingsAdapter = new PreviewSettingsAdapter();
        previewSettingsRecyclerview.setAdapter(previewSettingsAdapter);

        previewSettingsAdapter.submitList(Arrays.asList(previewSettings));
        previewSettingsRecyclerview.requestFocus();
        previewSettingsAdapter.setOnItemClickListener((baseQuickAdapter, view1, which) -> {
            PreviewSettings settings = baseQuickAdapter.getItem(which);
            switch (settings) {
                case START:
                    AppUtils.launchApp(appInfo.getPackageName());
                    if (appInfo.getIsSystem() == 0) sortData(appInfo);
                    break;
                case VIEW:
                    AppUtils.launchAppDetailsSettings(appInfo.getPackageName());
                    break;
                case SHOW:
                    if (appInfo.getIsSystem() == 1) {
                        Toast.makeText(this, "系统应用无法显示", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    appInfo.setIsHidden(1);
                    appListAdapter.add(0, appInfo);
                    appListAdapter.notifyDataSetChanged();
                    parent.remove(appInfo);
                    parent.notifyDataSetChanged();
                    new Thread(() -> allAppsInfoDao.updateIsHiddenByPackageName(appInfo.getPackageName(), false)).start();
                    break;
                case HIDE:
                    if (!PurchaseManager.getInstance().isPro()) {
                        ProDialog.show(this);
                        return;
                    }
                    favoriteAppsAdapter.remove(appInfo);
                    favoriteAppsAdapter.notifyDataSetChanged();
                    appListAdapter.remove(appInfo);
                    appListAdapter.notifyDataSetChanged();
                    appInfo.setIsHidden(0);
                    hiddenApps.add(appInfo);
                    new Thread(() -> {
                        favoriteAppInfoDao.deleteByPackageName(appInfo.getPackageName());
                        allAppsInfoDao.updateIsHiddenByPackageName(appInfo.getPackageName(), true);
                    }).start();
                    break;
                case MOVE:
                    isMoveApp = true;
                    movePosition = position;
                    break;
                case DELETE:
                    AppInfo favoriteAppInfo = favoriteAppsAdapter.getItem(position);
                    favoriteAppsAdapter.remove(favoriteAppInfo);
                    favoriteAppsAdapter.notifyDataSetChanged();
                    new Thread(() -> favoriteAppInfoDao.deleteByPackageName(appInfo.getPackageName())).start();
                    break;
                case UNINSTALL:
                    if (appInfo.getIsSystem() == 1) {
                        Toast.makeText(this, "系统应用无法卸载", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ToolUtils.uninstallApp(this, appInfo.getPackageName());
                    break;
            }
            dialog.dismiss();
        });

        return true;
    }

    private int moveToPosition = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkMonitor != null) {
            networkMonitor.unregister();
        }
        if (cpuMonitor != null) {
            cpuMonitor.release();
        }
        if (usbReceiver != null) {
            unregisterReceiver(usbReceiver);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initData() {

        cpuMonitor = CpuMonitor.getInstance(this);
        appDataBase = AppDataBase.getInstance(this);
        favoriteAppInfoDao = appDataBase.getFavoriteAppInfoDao();
        allAppsInfoDao = appDataBase.getAllAppsInfoDao();

        topSettingsAdapter = new SettingsIconAdapter();
        topSettingsAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.SlideInLeft);
        appListAdapter = new AppIconAdapter();
        favoriteAppsAdapter = new AppIconAdapter();

        topSettingsBar.setAdapter(topSettingsAdapter);
        appListGrid.setAdapter(appListAdapter);
        favoriteAppsGrid.setAdapter(favoriteAppsAdapter);

        topSettingsAdapter.submitList(new ArrayList<>(List.of(TopSettingsIcons.getTopSettings())));

        new Thread(() -> {
            List<AppInfo> tempAllApps = allAppsInfoDao.getAllAppInfo();
            if (tempAllApps.isEmpty()) {
                tempAllApps = AppsUtils.getAppsInfo(this);
                allAppsInfoDao.insertAll(tempAllApps);
            }
            // 所有的应用列表 过滤掉系统应用和隐藏应用
            allApps = Lists.newArrayList(Iterables.filter(tempAllApps, appInfo -> {
                if (appInfo != null) {
                    return appInfo.getIsSystem() == 0 && appInfo.getIsHidden() == 0;
                }
                return false;
            }));
            // 排序
            Collections.sort(allApps, (o1, o2) -> {
                long index1 = o1.getOpenAppCount();
                long index2 = o2.getOpenAppCount();
                return Long.compare(index2, index1);
            });
            allApps.add(allApps.size(), ToolUtils.getEmptyAppInfo("壁纸", ResourceUtils.getDrawable(R.drawable.ic_wall_art_24dp), Color.parseColor("#EF4444")));
            allApps.add(allApps.size(), ToolUtils.getEmptyAppInfo("隐私空间", ResourceUtils.getDrawable(R.drawable.ic_lock_24dp), Color.parseColor("#2B2F4A")));
            allApps.add(allApps.size(), ToolUtils.getEmptyAppInfo("系统应用", ResourceUtils.getDrawable(R.drawable.ic_apps_24dp), Color.parseColor("#0EA5E9")));
            // 从常用表根据包名获取详细的应用软件信息
            List<AppInfo> favoriteAppInfos = favoriteAppInfoDao.getFavoriteApps();
            favoriteApps.addAll(favoriteAppInfos);
            favoriteApps.add(favoriteApps.size(), ToolUtils.getEmptyAppInfo("添加应用", ResourceUtils.getDrawable(R.drawable.ic_add_24dp), Color.parseColor("#263238")));
            for (AppInfo favoriteApp : favoriteApps) {
                Log.d(TAG, "initData: " + favoriteApp);
            }

            // 根据所有应用列表 过滤出系统应用 并移除系统不可启动的应用
            systemApps = Lists.newArrayList(Iterables.filter(tempAllApps, AppAllInfo -> AppAllInfo != null && AppAllInfo.getIsSystem() == 1));
            Iterator<AppInfo> iterator = systemApps.iterator();
            while (iterator.hasNext()) {
                AppInfo next = iterator.next();
                boolean appLaunchable = ToolUtils.isAppLaunchable(this, next.getPackageName());
                if (!appLaunchable) {
                    iterator.remove();
                }
            }
            // 过滤出所有隐藏的应用列表
            hiddenApps = Lists.newArrayList(Iterables.filter(tempAllApps, AppAllInfo -> AppAllInfo != null && AppAllInfo.getIsHidden() == 1));
            Log.d(TAG, "initData: 数据处理完成");
            runOnUiThread(() -> {
                Log.d(TAG, "initData: 更新UI");
                appListAdapter.submitList(allApps);
                appListAdapter.notifyDataSetChanged();
                favoriteAppsAdapter.submitList(favoriteApps);
                favoriteAppsAdapter.notifyDataSetChanged();
                favoriteAppsGrid.requestFocus();
            });
        }).start();
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
                new Thread(() -> {
                    allAppsInfoDao.updateSortIndexByPackageName(adapterItem.getPackageName(), adapterItem.getSortIndex());
                    favoriteAppInfoDao.insert(new FavoriteApp(adapterItem.getPackageName()));
                }).start();
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
        if (movePosition == moveToPosition) {
            Log.d(TAG, "moveItem: " + movePosition + " " + moveToPosition);
            return;

        }

        List<AppInfo> list = favoriteAppsAdapter.getItems();

        AppInfo fromItem = list.get(movePosition);
        AppInfo toItem = list.get(moveToPosition);

        Log.d(TAG, "moveItem: " + fromItem.getId() + " " + fromItem.getName() + " " + fromItem.getSortIndex());
        Log.d(TAG, "moveItem: " + toItem.getId() + " " + toItem.getName() + " " + toItem.getSortIndex());

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
//            allAppsInfoDao.update(a);
//            allAppsInfoDao.update(b);
            Log.d(TAG, "updateSortIndexAsync: " + a.getSortIndex() + " " + b.getSortIndex());
            allAppsInfoDao.updateSortIndexByPackageName(a.getPackageName(), a.getSortIndex());
            allAppsInfoDao.updateSortIndexByPackageName(b.getPackageName(), b.getSortIndex());
        });
    }

    private void showAllApps() {
        BlurCompat.setBlur(wallPager, allAppsContainer, 20);
        topSettingsBar.setVisibility(View.GONE);
        int screenHeight = ScreenUtils.getScreenHeight();
        Log.d(TAG, "showAllApps: " + screenHeight);
        favoriteAppsContainer.animate().translationY(-screenHeight).setDuration(500).start();
        allAppsContainer.animate().translationY(0).setDuration(500).start();
        appListGrid.requestFocus();
    }

    private void showHomeApps() {
        BlurCompat.cancelBlur(wallPager);
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
}