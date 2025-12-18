package com.jayjd.boxtop.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jayjd.boxtop.listeners.UsbDriveListener;

public class UsbBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "UsbBroadcastReceiver";
    private final UsbDriveListener usbDriveListener;

    public UsbBroadcastReceiver(UsbDriveListener usbDriveListener) {
        this.usbDriveListener = usbDriveListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: " + action);
        if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
            // U盘插入
            usbDriveListener.onUsbDriveStateChanged(true);
        } else if (Intent.ACTION_MEDIA_UNMOUNTED.equals(action) || Intent.ACTION_MEDIA_REMOVED.equals(action) || Intent.ACTION_MEDIA_EJECT.equals(action)) {
            // U盘拔出
            usbDriveListener.onUsbDriveStateChanged(false);
        } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            // 蓝牙连接
            usbDriveListener.onBluetoothStateChanged(true);
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            // 蓝牙断开
            usbDriveListener.onBluetoothStateChanged(false);
        }
    }
}
