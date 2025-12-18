package com.jayjd.boxtop.listeners;

public interface UsbDriveListener {
    void onUsbDriveStateChanged(boolean isConnected);

    void onBluetoothStateChanged(boolean isConnected);
}
