package com.jayjd.boxtop.nanohttpd;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jayjd.boxtop.R;
import com.jayjd.boxtop.nanohttpd.interfas.DataReceiver;
import com.jayjd.boxtop.nanohttpd.interfas.RequestProcess;
import com.jayjd.boxtop.utils.FileUtils;
import com.jayjd.boxtop.wallpaper.adapter.WallPaperUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

import fi.iki.elonen.NanoHTTPD;

public class RemoteServer extends NanoHTTPD {


    private static final String KEYSTORE_FILE = "keystore.p12";
    private static final String KEYSTORE_PASSWORD = "1234qwer@A";
    public static int serverPort = 8585;
    private final ArrayList<RequestProcess> getRequestList = new ArrayList<>();
    private final ArrayList<RequestProcess> postRequestList = new ArrayList<>();
    private final Context context;
    private boolean isStarted = false;
    private DataReceiver mDataReceiver;

    public RemoteServer(int port, Context context) {
        super(port);
        this.context = context;
//        makeSecure(createSSLSocketFactory(), null);
        addGetRequestProcess();
        addPostRequestProcess();
    }

    public static String getLocalIPAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        if (ipAddress == 0) {
            try {
                Enumeration<NetworkInterface> enumerationNi = NetworkInterface.getNetworkInterfaces();
                while (enumerationNi.hasMoreElements()) {
                    NetworkInterface networkInterface = enumerationNi.nextElement();
                    String interfaceName = networkInterface.getDisplayName();
                    if (interfaceName.equals("eth0") || interfaceName.equals("wlan0")) {
                        Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses();
                        while (enumIpAddr.hasMoreElements()) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        } else {
            return String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        }
        return "0.0.0.0";
    }

    public static Response createPlainTextResponse(Response.IStatus status, String text) {
        Response response = newFixedLengthResponse(status, NanoHTTPD.MIME_PLAINTEXT, text);
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        return response;
    }

    private String getKeystoreFilePath() {
        File file = new File(context.getFilesDir(), KEYSTORE_FILE);
        if (!file.exists()) {
            try {
                InputStream inputStream = context.getResources().openRawResource(R.raw.keystore);
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }

    private SSLServerSocketFactory createSSLSocketFactory() {
        try {
            // 加载密钥库
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            System.out.println("Trying to load keystore from: " + getKeystoreFilePath());
            keyStore.load(new FileInputStream(getKeystoreFilePath()), KEYSTORE_PASSWORD.toCharArray());
            System.out.println("Keystore loaded successfully.");

            // 初始化 KeyManagerFactory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, KEYSTORE_PASSWORD.toCharArray());
            System.out.println("KeyManagerFactory initialized successfully.");

            // 初始化 SSL 上下文
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
            System.out.println("SSLContext initialized successfully.");

            return sslContext.getServerSocketFactory();
        } catch (Exception e) {
            System.err.println("Error creating SSLSocketFactory: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void addGetRequestProcess() {
        getRequestList.add(new RawRequestProcess(this.context, "/", R.raw.index, "", NanoHTTPD.MIME_HTML));
        getRequestList.add(new RawRequestProcess(this.context, "/index.html", R.raw.index, "", NanoHTTPD.MIME_HTML));
        getRequestList.add(new RawRequestProcess(this.context, "/style.css", R.raw.style, "", "text/css"));
        getRequestList.add(new RawRequestProcess(this.context, "/weui.css", R.raw.weui, "", "text/css"));
        getRequestList.add(new RawRequestProcess(this.context, "/naicha.png", R.raw.naicha, "", "png"));
        getRequestList.add(new RawRequestProcess(this.context, "/wechat_reward_qrcode.jpg", R.raw.wechat_reward_qrcode, "", "jpg"));
        getRequestList.add(new RawRequestProcess(this.context, "/hy.png", R.raw.hy, "", "png"));
        getRequestList.add(new RawRequestProcess(this.context, "/dy.png", R.raw.dy, "", "png"));
        getRequestList.add(new RawRequestProcess(this.context, "/jquery.js", R.raw.jquery, "", "application/x-javascript"));
        getRequestList.add(new RawRequestProcess(this.context, "/script.js", R.raw.script, "", "application/x-javascript"));
        getRequestList.add(new RawRequestProcess(this.context, "/favicon.ico", R.mipmap.ic_launcher, "", "image/x-icon"));


    }

    private void addPostRequestProcess() {
        postRequestList.add(new InputRequestProcess(this));
    }

    public String getLoadAddress() {
        return "http://127.0.0.1:" + RemoteServer.serverPort + "/";
    }

    public String getServerAddress() {
        String ipAddress = getLocalIPAddress(context);
        return "http://" + ipAddress + ":" + RemoteServer.serverPort + "/";
    }

    public DataReceiver getDataReceiver() {
        return mDataReceiver;
    }

    @Override
    public void start(int timeout, boolean daemon) throws IOException {
        isStarted = true;
        super.start(timeout, daemon);
    }

    @Override
    public void stop() {
        super.stop();
        isStarted = false;
    }

    public boolean isStarted() {
        return isStarted;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String fileName = session.getUri().trim();

        Log.d("TAG", fileName);
        if (fileName.equals("/status")) {
            return createPlainTextResponse(NanoHTTPD.Response.Status.OK, "OK");
        }
        if (session.getMethod() == Method.GET) {
            for (RequestProcess process : getRequestList) {
                if (process.isRequest(session, fileName)) {
                    Map<String, String> params = new HashMap<>();
                    Map<String, List<String>> parameters = session.getParameters();
                    for (String key : parameters.keySet()) {
                        List<String> list = parameters.get(key);
                        if (list != null && !list.isEmpty()) {
                            params.put(key, list.get(0));
                        }
                    }
                    return process.doResponse(session, fileName, params, null);
                }
            }
        } else if (session.getMethod() == Method.POST) {

            Map<String, String> files = new HashMap<>();
            try {
                if (session.getHeaders().containsKey("content-type")) {
                    String hd = session.getHeaders().get("content-type");
                    if (hd != null) {
                        // cuke: 修正中文乱码问题
                        if (hd.toLowerCase().contains("multipart/form-data") && !hd.toLowerCase().contains("charset=")) {
                            Matcher matcher = Pattern.compile("[ |\t]*(boundary[ |\t]*=[ |\t]*['|\"]?[^\"^'^;^,]*['|\"]?)", Pattern.CASE_INSENSITIVE).matcher(hd);
                            String boundary = matcher.find() ? matcher.group(1) : null;
                            if (boundary != null) {
                                session.getHeaders().put("content-type", "multipart/form-data; charset=utf-8; " + boundary);
                            }
                        }
                    }
                }
                session.parseBody(files);
            } catch (IOException IOExc) {
                return createPlainTextResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: IOException: " + IOExc.getMessage());
            } catch (NanoHTTPD.ResponseException rex) {
                return createPlainTextResponse(rex.getStatus(), rex.getMessage());
            }
            for (RequestProcess process : postRequestList) {
                if (process.isRequest(session, fileName)) {
                    Map<String, String> params = new HashMap<>();
                    Map<String, List<String>> parameters = session.getParameters();
                    for (String key : parameters.keySet()) {
                        List<String> list = parameters.get(key);
                        if (list != null && !list.isEmpty()) {
                            params.put(key, list.get(0));
                        }
                    }
                    return process.doResponse(session, fileName, params, files);
                }
            }

            try {
                Map<String, List<String>> params = session.getParameters();
                Log.d("TAG", fileName);
                if (fileName.equals("/upload")) {
                    for (String k : params.keySet()) {
                        if (k.startsWith("files-")) {
                            List<String> list = params.get(k);
                            String tmpFile = files.get(k);
                            if (list == null || list.isEmpty()) {
                                return createPlainTextResponse(NanoHTTPD.Response.Status.OK, "OK");
                            }
                            for (String tmpFileItem : list) {
                                File tmp = new File(tmpFile);
                                Log.d("TAG", tmp.getAbsolutePath());
                                String absoluteFile;
                                if (tmpFileItem.toLowerCase().contains(".apk")) {
                                    tmpFileItem = tmpFileItem.split(".apk")[0] + ".apk";
                                    absoluteFile = WallPaperUtils.getDownloadStringPath(context);
                                } else {
                                    absoluteFile = getFileDir(context);
                                }

                                File file = new File(absoluteFile + "/" + tmpFileItem);
                                Log.d("TAG", file.getAbsolutePath());
                                if (file.exists()) file.delete();
                                if (tmp.exists()) {
                                    if (tmpFileItem.toLowerCase().endsWith(".apk")) {
                                        FileUtils.copyFile(tmp, file);
                                        Log.d("TAG", "apk - " + file.getAbsolutePath());
                                        mDataReceiver.onInstallApk(context, absoluteFile, tmpFileItem);
//                                        ToolsUtils.installApk(context, file.getAbsolutePath());
                                    } else {
                                        byte[] bytes = FileUtils.readSimple(tmp);
                                        FileUtils.writeSimple(bytes, file);
                                    }
                                }
                                if (tmp.exists()) {
                                    tmp.delete();
                                }
                            }
                        }
                    }
                    return createPlainTextResponse(NanoHTTPD.Response.Status.OK, "推送成功");
                }
            } catch (Throwable th) {
                return createPlainTextResponse(NanoHTTPD.Response.Status.OK, "异常：" + th.getMessage());
            }
        }
        return getRequestList.get(0).doResponse(session, "", null, null);
    }

    public String getFileDir(@NonNull Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    public void setmDataReceiver(DataReceiver dataReceiver) {
        this.mDataReceiver = dataReceiver;
    }
}
