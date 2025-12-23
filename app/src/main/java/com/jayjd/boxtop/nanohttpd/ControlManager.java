package com.jayjd.boxtop.nanohttpd;

import android.annotation.SuppressLint;
import android.content.Context;

import com.jayjd.boxtop.nanohttpd.interfas.DataReceiver;

import java.io.IOException;

public class ControlManager {
    private static final String TAG = ControlManager.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    @SuppressLint("StaticFieldLeak")
    private static ControlManager instance;
    private RemoteServer remoteServer;

    public static void init(Context context) {
        mContext = context;
    }

    public static ControlManager get() {
        if (instance == null) {
            synchronized (ControlManager.class) {
                if (instance == null) {
                    instance = new ControlManager();
                }
            }
        }
        return instance;
    }

    public String getAddress(boolean local) {
        return local ? remoteServer.getLoadAddress() : remoteServer.getServerAddress();
    }

    public void startServer(DataReceiver dataReceiver) {
        if (remoteServer != null && remoteServer.isStarted()) return;
        do {
            remoteServer = new RemoteServer(RemoteServer.serverPort, mContext);

            remoteServer.setmDataReceiver(dataReceiver);

            try {
                remoteServer.start();
                break;
            } catch (IOException e) {
                RemoteServer.serverPort++;
                remoteServer.stop();
            }
        } while (RemoteServer.serverPort < 9999);
    }

    public void stopServer() {
        if (remoteServer != null && remoteServer.isStarted()) {
            remoteServer.stop();
        }
    }

}
