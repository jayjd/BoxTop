package com.jayjd.boxtop.nanohttpd;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import lombok.Getter;

public class RemoteServer extends NanoHTTPD {


    private static final String KEYSTORE_FILE = "keystore.p12";
    private static final String KEYSTORE_PASSWORD = "1234qwer@A";
    public static int serverPort = 8585;
    private final ArrayList<RequestProcess> getRequestList = new ArrayList<>();
    private final ArrayList<RequestProcess> postRequestList = new ArrayList<>();
    private final Context context;
    @Getter
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
        getRequestList.add(new RawRequestProcess(this.context, "/wechat_reward_qrcode.jpg", R.raw.wechat_reward_qrcode, "", "jpg"));
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

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri().trim();
        Log.d("NanoHTTPD", "uri = " + uri);

        if ("/status".equals(uri)) {
            return createPlainTextResponse(Response.Status.OK, "OK");
        }

        if (session.getMethod() == Method.GET) {
            for (RequestProcess process : getRequestList) {
                if (process.isRequest(session, uri)) {
                    Map<String, String> params = new HashMap<>();
                    Map<String, List<String>> parameters = session.getParameters();
                    for (String key : parameters.keySet()) {
                        List<String> list = parameters.get(key);
                        if (list != null && !list.isEmpty()) {
                            params.put(key, list.get(0));
                        }
                    }
                    return process.doResponse(session, uri, params, null);
                }
            }
        } else if (session.getMethod() == Method.POST) {
            try {
                // ✅ 这里直接粘贴我给你的“统一解析 + 本地/网络分发”代码
                Map<String, String> files = new HashMap<>();
                Map<String, String> params = new HashMap<>();

                try {
                    // 修正 multipart 中文乱码
                    if (session.getHeaders().containsKey("content-type")) {
                        String hd = session.getHeaders().get("content-type");
                        if (hd != null && hd.toLowerCase().contains("multipart/form-data") && !hd.toLowerCase().contains("charset=")) {
                            Matcher matcher = Pattern.compile("[ |\t]*(boundary[ |\t]*=[ |\t]*['|\"]?[^\"^'^;^,]*['|\"]?)", Pattern.CASE_INSENSITIVE).matcher(hd);
                            String boundary = matcher.find() ? matcher.group(1) : null;
                            if (boundary != null) {
                                session.getHeaders().put("content-type", "multipart/form-data; charset=utf-8; " + boundary);
                            }
                            session.parseBody(files);

                            for (Map.Entry<String, List<String>> entry : session.getParameters().entrySet()) {
                                if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                                    params.put(entry.getKey(), entry.getValue().get(0));
                                }
                            }
                        } else if (hd != null && hd.toLowerCase().contains("application/json")) {
                            // ✅ JSON 不需要修正，直接 parseBody 即可
                            session.parseBody(files);
                            String jsonStr = files.get("postData");
                            if (jsonStr == null || jsonStr.isEmpty()) {
                                return createPlainTextResponse(Response.Status.BAD_REQUEST, "数据异常");
                            }
                            // 2️⃣ 转成 JsonObject
                            JsonObject obj = JsonParser.parseString(jsonStr).getAsJsonObject();
                            String type = obj.has("type") ? obj.get("type").getAsString() : null;       // apk / wallpaper
                            String source = obj.has("source") ? obj.get("source").getAsString() : null; // url
                            String url = obj.has("url") ? obj.get("url").getAsString() : null;
                            if (type == null || source == null || url == null) {
                                return createPlainTextResponse(Response.Status.BAD_REQUEST, "缺少 type / source / url");
                            }
                            params.put("type", type);
                            params.put("source", source);
                            params.put("url", url);
                        }
                    }

                } catch (Exception e) {
                    return createPlainTextResponse(Response.Status.INTERNAL_ERROR, "POST parse error: " + e.getMessage());
                }

                // ===== 处理本地 / 网络分发 =====
                String type = params.get("type");    // apk / wallpaper
                String source = params.get("source");  // local / url

                if (type == null || source == null) {
                    return createPlainTextResponse(Response.Status.BAD_REQUEST, "missing type or source");
                }

                if ("local".equals(source)) {
                    // 本地文件逻辑（multipart）
                    String saveDir;
                    if ("apk".equals(type)) {
                        saveDir = WallPaperUtils.getLocalDownloadPath(context).getAbsolutePath();
                    } else {
                        saveDir = WallPaperUtils.getLocalWallPath(context).getAbsolutePath(); // 壁纸目录
                    }

                    for (Map.Entry<String, String> entry : files.entrySet()) {
                        String formKey = entry.getKey();      // files
                        String tempPath = entry.getValue();   // 临时文件
                        File tmp = new File(tempPath);
                        if (!tmp.exists()) continue;

                        List<String> names = session.getParameters().get(formKey);
                        if (names == null || names.isEmpty()) continue;
                        String fileNameReal = names.get(0);

                        File dest = new File(saveDir, fileNameReal);
                        if (dest.exists()) dest.delete();
                        FileUtils.copyFile(tmp, dest);
                        tmp.delete();
                        Log.d("TAG", "save file -> " + dest.getAbsolutePath());
                        params.put("localPath", dest.getAbsolutePath());
                        for (RequestProcess requestProcess : postRequestList) {
                            if (requestProcess.isRequest(session, uri)) {
                                requestProcess.doResponse(session, uri, params, null);
                            }
                        }
                    }
                    return createPlainTextResponse(Response.Status.OK, "推送壁纸成功");
                } else {
                    Log.d("TAG", "serve: " + postRequestList.size());
                    for (RequestProcess requestProcess : postRequestList) {
                        if (requestProcess.isRequest(session, uri)) {
                            return requestProcess.doResponse(session, uri, params, null);
                        }
                    }
                }
                return createPlainTextResponse(Response.Status.OK, "处理失败");
            } catch (IOException e) {
                return createPlainTextResponse(Response.Status.INTERNAL_ERROR, "异常：" + e.getMessage());
            }
        }
        return createPlainTextResponse(Response.Status.NOT_FOUND, "Not Found");
    }

    private boolean isJsonRequest(IHTTPSession session) {
        String contentType = session.getHeaders().get("content-type");
        return contentType != null && contentType.contains("application/json");
    }

    public String getFileDir(@NonNull Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    public void setmDataReceiver(DataReceiver dataReceiver) {
        this.mDataReceiver = dataReceiver;
    }
}
