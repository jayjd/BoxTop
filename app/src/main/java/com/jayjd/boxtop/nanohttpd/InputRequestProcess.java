package com.jayjd.boxtop.nanohttpd;


import com.jayjd.boxtop.nanohttpd.interfas.DataReceiver;
import com.jayjd.boxtop.nanohttpd.interfas.RequestProcess;

import java.util.Map;
import java.util.Objects;

import fi.iki.elonen.NanoHTTPD;

/**
 * @author pj567
 * @date :2021/1/5
 * @description: 响应按键和输入
 */

public class InputRequestProcess implements RequestProcess {
    private final RemoteServer remoteServer;
    private final String TAG = InputRequestProcess.class.getSimpleName();

    public InputRequestProcess(RemoteServer remoteServer) {
        this.remoteServer = remoteServer;
    }

    @Override
    public boolean isRequest(NanoHTTPD.IHTTPSession session, String fileName) {
        if (session.getMethod() == NanoHTTPD.Method.POST) {
            return fileName.equals("/action");
        }
        return false;
    }

    @Override
    public NanoHTTPD.Response doResponse(NanoHTTPD.IHTTPSession session, String fileName, Map<String, String> params, Map<String, String> files) {
        DataReceiver mDataReceiver = remoteServer.getDataReceiver();
        if (fileName.equals("/action")) {
            if (params.get("do") != null && mDataReceiver != null) {
                String action = params.get("do");
                switch (Objects.requireNonNull(action)) {
                    case "HDKPush" -> mDataReceiver.onHDKPush(Objects.requireNonNull(params.get("word")).trim());
                    case "DouYuPush" -> mDataReceiver.onDouYuPush(Objects.requireNonNull(params.get("word")).trim());
                    case "TvLivePush" -> mDataReceiver.onTvLivePush(Objects.requireNonNull(params.get("word")).trim());
                    case "FanLivePush" -> mDataReceiver.onFanLivePush(Objects.requireNonNull(params.get("word")).trim());
                    case "CookiePush" -> mDataReceiver.onCookiePush(Objects.requireNonNull(params.get("word")).trim());
                }
                return RemoteServer.createPlainTextResponse(NanoHTTPD.Response.Status.OK, "推送成功");
            }else{
                return RemoteServer.createPlainTextResponse(NanoHTTPD.Response.Status.NOT_FOUND, "Error 404, file not found.");
            }
        }
        return RemoteServer.createPlainTextResponse(NanoHTTPD.Response.Status.NOT_FOUND, "Error 404, file not found.");
    }
}
