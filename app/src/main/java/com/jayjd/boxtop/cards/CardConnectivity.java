package com.jayjd.boxtop.cards;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jayjd.boxtop.R;

import java.util.Locale;
import java.util.Set;

public class CardConnectivity extends BaseCardFragment {
    private View rowNetworkState;
    private View rowIpAddress;
    private View rowBluetoothState;
    private View rowSsid;
    private View rowSpeed;

    private ImageView ivNetwork;
    private ImageView ivBluetooth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_card_connectivity, container, false);

        bindViews(root);
        updateConnectivityInfo(); // 初始填充信息

        return root;
    }


    // -----------------------------
    // 绑定控件
    // -----------------------------
    private void bindViews(View root) {
        rowNetworkState = root.findViewById(R.id.row_network_state);
        rowIpAddress = root.findViewById(R.id.row_ip_address);
        rowBluetoothState = root.findViewById(R.id.row_bluetooth_state);
        rowSsid = root.findViewById(R.id.row_ssid);
        rowSpeed = root.findViewById(R.id.row_speed);

        ivNetwork = root.findViewById(R.id.iv_network);
        ivBluetooth = root.findViewById(R.id.iv_bluetooth);
    }

    @SuppressLint("MissingPermission")
    public static String getWifiSsid(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return getSsidApi31(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getSsidApi29(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return getSsidApi26(context);
        } else {
            return getSsidLegacy(context);
        }
    }

    // -----------------------------
    // 工具方法
    // -----------------------------
    private void setRow(View row, String label, String value) {
        if (row == null) return;

        TextView tvLabel = row.findViewById(R.id.tv_label);
        TextView tvValue = row.findViewById(R.id.tv_value);

        tvLabel.setText(label);
        tvValue.setText(value);
    }

    @SuppressLint("MissingPermission")
    private static String getSsidLegacy(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wm == null || !wm.isWifiEnabled()) return "未连接";

        WifiInfo info = wm.getConnectionInfo();
        return parseSsid(info);
    }

    @SuppressLint("MissingPermission")
    private static String getSsidApi26(Context context) {
        if (!hasLocationPermission(context)) {
            return "Wi-Fi 已连接";
        }

        WifiManager wm = context.getApplicationContext()
                .getSystemService(WifiManager.class);
        WifiInfo info = wm != null ? wm.getConnectionInfo() : null;
        return parseSsid(info);
    }

    @SuppressLint("MissingPermission")
    private static String getSsidApi29(Context context) {
        if (!hasLocationPermission(context)) {
            return "Wi-Fi 已连接";
        }

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm != null ? cm.getActiveNetwork() : null;
        if (network == null) return "未连接";

        NetworkCapabilities caps = cm.getNetworkCapabilities(network);
        if (caps == null || !caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return "非 Wi-Fi";
        }

        WifiManager wm = context.getApplicationContext()
                .getSystemService(WifiManager.class);
        WifiInfo info = wm != null ? wm.getConnectionInfo() : null;
        return parseSsid(info);
    }

    @SuppressLint("MissingPermission")
    private static String getSsidApi31(Context context) {
        if (!hasLocationPermission(context)) {
            return "Wi-Fi 已连接";
        }

        ConnectivityManager cm =
                context.getSystemService(ConnectivityManager.class);
        Network network = cm != null ? cm.getActiveNetwork() : null;
        if (network == null) return "未连接";

        NetworkCapabilities caps = cm.getNetworkCapabilities(network);
        if (caps == null || !caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return "非 Wi-Fi";
        }

        WifiManager wm = context.getApplicationContext()
                .getSystemService(WifiManager.class);
        WifiInfo info = wm != null ? wm.getConnectionInfo() : null;
        return parseSsid(info);
    }

    private static boolean hasLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private static String parseSsid(WifiInfo info) {
        if (info == null) return "未连接";

        String ssid = info.getSSID();
        if (ssid == null || "<unknown ssid>".equals(ssid)) {
            return "Wi-Fi 已连接";
        }

        if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        return ssid;
    }

    // -----------------------------
    // 填充信息
    // -----------------------------
    private void updateConnectivityInfo() {
        // 网络状态
        String networkType = getNetworkType();
        String signalStrength = getSignalStrength();
        if (networkType.equals("Ethernet")) {
            setRow(rowNetworkState, "网络", networkType);
        } else {
            setRow(rowNetworkState, "网络", networkType + " · " + signalStrength);
        }
        String ssid = getWifiSsid(appContext);
        setRow(rowSsid, "SSID", ssid);

        // 速率
        String speed = getWifiSpeed();
        setRow(rowSpeed, "速率", speed);

        // IP 地址
        setRow(rowIpAddress, "IP 地址", getIpAddress());

        // 蓝牙状态
        boolean bluetoothOn = isBluetoothOn();
        int deviceCount = getBluetoothDeviceCount();
        setRow(rowBluetoothState, "蓝牙", (bluetoothOn ? "已开启" : "已关闭") + " · " + deviceCount + " 个设备");

        // 图标状态
        ivNetwork.setAlpha(networkType.equals("未连接") ? 0.4f : 1f);
        ivBluetooth.setAlpha(bluetoothOn ? 1f : 0.4f);
    }

    // 获取网络类型
    private String getNetworkType() {
        ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) return "Wi-Fi";
            if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET) return "Ethernet";
            return "移动网络";
        }
        return "未连接";
    }

    // 信号强度（简单示意，实际可用 WifiManager 获取 RSSI）
    private String getSignalStrength() {
        try {
            WifiManager wm = (WifiManager) appContext.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            if (wm == null || !wm.isWifiEnabled()) {
                return "未连接";
            }

            WifiInfo info = wm.getConnectionInfo();
            if (info == null) {
                return "未连接";
            }

            int rssi = info.getRssi(); // RSSI 单位 dBm
            // 根据 Android 官方建议：0~5 等级划分，也可自定义
            if (rssi >= -50) {
                return "强";
            } else if (rssi >= -60) {
                return "中";
            } else if (rssi >= -70) {
                return "弱";
            } else {
                return "很弱";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
        }
    }

    private String getWifiSpeed() {
        try {
            WifiManager wm = (WifiManager) appContext.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            if (wm != null && wm.isWifiEnabled()) {
                WifiInfo info = wm.getConnectionInfo();
                if (info != null) {
                    int linkSpeed = info.getLinkSpeed(); // 单位 Mbps
                    return linkSpeed + " Mbps";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "--";
    }

    // 获取 IP 地址（局域网 IPv4）
    private String getIpAddress() {
        try {
            WifiManager wm = (WifiManager) appContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wm != null) {
                int ip = wm.getConnectionInfo().getIpAddress();
                return String.format(Locale.US, "%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }

    // 蓝牙是否开启
    private boolean isBluetoothOn() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter != null && adapter.isEnabled();
    }

    // 获取已连接蓝牙设备数量
    private int getBluetoothDeviceCount() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null || !adapter.isEnabled()) return 0;

        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
        return pairedDevices != null ? pairedDevices.size() : 0;
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        Log.d("CardConnectivity", "onFragmentVisible() called");
        updateConnectivityInfo();
    }

    @Override
    protected void onFragmentInvisible() {
        super.onFragmentInvisible();
        Log.d("CardConnectivity", "onFragmentInvisible() called");

    }
}