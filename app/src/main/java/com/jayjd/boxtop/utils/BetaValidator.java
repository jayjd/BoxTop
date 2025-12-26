package com.jayjd.boxtop.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.jayjd.boxtop.R;
import com.jayjd.boxtop.entity.BetaConfig;
import com.jayjd.boxtop.listeners.BetaStateListener;
import com.jayjd.boxtop.nanohttpd.ControlManager;
import com.jayjd.boxtop.nanohttpd.QRCodeGen;
import com.jayjd.boxtop.nanohttpd.interfas.BaseDataReceiver;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

/**
 * 内测验证工具类
 * 用于本地校验 deviceId 是否允许使用
 */
public final class BetaValidator {
    private static Dialog dialog;
    private final Context context;
    private BetaStateListener betaStateListener;

    public BetaValidator(Context context) {
        this.context = context;
    }

    private static boolean isExpireAt(BetaConfig config) {
        long eat = TimeUtils.string2Millis(config.getExpire_at());
        return System.currentTimeMillis() > eat;
    }

    public void startServer() {
        ControlManager.get().startServer(new BaseDataReceiver());
    }

    public void stopServer() {
        if (dialog != null) {
            dialog.dismiss();
        }
        ControlManager.get().stopServer();
    }

    public void getBaseConfigData(BetaStateListener betaStateListener) {
        this.betaStateListener = betaStateListener;
//        https://bitbucket.org/hyyyyy/hy/raw/master/beta.json
//        https://gitee.com/jayjd/boxtop/releases/download/0.0.1/beta.json
//        https://gh.xxooo.cf/https://raw.githubusercontent.com/jayjd/jayjd.github.io/refs/heads/main/hy/beta.json
        OkGo.<String>get("https://gitee.com/jayjd/boxtop/releases/download/0.0.1/beta.json").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (response.isSuccessful()) {
                    String body = response.body();
                    if (body == null) {
                        return;
                    }
                    BetaConfig betaConfig = new Gson().fromJson(body, BetaConfig.class);
                    String deviceId = DeviceFingerprintUtil.getDeviceFingerprint(context).toUpperCase();
                    SPUtils.put(context, "deviceId", deviceId);
                    boolean deviceAllowed = isDeviceAllowed(context, betaConfig);
                    betaStateListener.onBetaStateChanged(deviceAllowed);
                    if (deviceAllowed) {
                        stopServer();
                        Toast.makeText(context, "欢迎加入内测", Toast.LENGTH_SHORT).show();
                    } else {
                        showFailDialog(deviceId, betaConfig);
                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                new MaterialAlertDialogBuilder(context, R.style.ProDialogTheme).setTitle("验证异常").setMessage(response.getException().getMessage())
                        .setNegativeButton("退出", (dialog, which) -> betaStateListener.onExitApp())
                        .setCancelable(false).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showFailDialog(String deviceId, BetaConfig betaConfig) {
        if (dialog == null) {
            startServer();
            View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_validator, null);
            TextView tvDeviceId = inflate.findViewById(R.id.tv_device_id);
            TextView tvExpireTime = inflate.findViewById(R.id.tv_expire_time);
            TextView tvHttp = inflate.findViewById(R.id.tv_http);
            ImageView addressImage = inflate.findViewById(R.id.address_image);

            TextView tvRefreshStatus = inflate.findViewById(R.id.tv_refresh_status);
            TextView tvExit = inflate.findViewById(R.id.tv_exit);
            tvRefreshStatus.setOnClickListener(v -> getBaseConfigData(betaStateListener));
            tvExit.setOnClickListener(v -> {
                stopServer();
                betaStateListener.onExitApp();
            });
            tvDeviceId.setText(deviceId);
            tvExpireTime.setText("内测结束时间：" + (isExpireAt(betaConfig) ? "内测已结束！" : betaConfig.getExpire_at()));
            String address = ControlManager.get().getAddress(false);
            Bitmap bitmap = QRCodeGen.generateBitmap(address, 80, 80, 0, Color.WHITE, Color.TRANSPARENT);
            tvHttp.setText(address);
            Glide.with(context).load(bitmap).into(addressImage);
            dialog = new Dialog(context, R.style.CustomDialogTheme);
            dialog.setContentView(inflate);
            dialog.setCancelable(false);
            dialog.show();
        } else {
            String msg = isExpireAt(betaConfig) ? "内测已结束！" : "未获得授权，请联系开发者！";
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 核心方法：校验设备是否允许使用
     *
     * @param context 上下文，用于生成 deviceId
     * @param config  从 Bitbucket 拉下来的 BetaConfig
     * @return true = 允许使用，false = 拒绝
     */
    private boolean isDeviceAllowed(Context context, BetaConfig config) {
        if (config == null) return false;

        // 1️⃣ 内测开关
        if (!config.isBeta_enabled()) return false;

        // 2️⃣ 内测过期
        if (isExpireAt(config)) return false;

        // 3️⃣ 生成设备指纹
        String deviceId = DeviceFingerprintUtil.getDeviceFingerprint(context).toUpperCase();
        if (TextUtils.isEmpty(deviceId)) return false;

        // 4️⃣ 撤销列表
        List<String> revoked = config.getRevoked_devices();
        if (revoked != null && revoked.contains(deviceId)) return false;

        // 5️⃣ 白名单列表
        List<String> allowed = config.getAllowed_devices();
        return allowed != null && allowed.contains(deviceId);
    }
}
