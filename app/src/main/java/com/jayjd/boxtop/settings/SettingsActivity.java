package com.jayjd.boxtop.settings;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jayjd.boxtop.R;
import com.jayjd.boxtop.enums.AllSettings;
import com.jayjd.boxtop.listeners.TvOnItemListener;
import com.jayjd.boxtop.listeners.ViewAnimationShake;
import com.jayjd.boxtop.settings.adapter.SettingsAdapter;
import com.jayjd.boxtop.utils.ProDialog;
import com.jayjd.boxtop.utils.PurchaseManager;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        TvRecyclerView settingTrView = findViewById(R.id.settings_tr_view);
        settingTrView.setLayoutManager(new V7LinearLayoutManager(this, V7LinearLayoutManager.VERTICAL, false));
        SettingsAdapter settingsAdapter = new SettingsAdapter();
        settingTrView.setAdapter(settingsAdapter);
        AllSettings[] values = AllSettings.values();
        settingsAdapter.submitList(List.of(values));
        settingTrView.setOnInBorderKeyEventListener(new ViewAnimationShake(settingTrView, this));
        settingTrView.setOnItemListener(new TvOnItemListener());
        settingTrView.requestFocus();
        settingsAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            AllSettings allSettings = baseQuickAdapter.getItem(i);
            if (allSettings == AllSettings.SYSTEM_SETTINGS_CARDS) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (allSettings == AllSettings.HOME_CARDS) {
                SwitchMaterial switchMaterial = view.findViewById(R.id.sw_function);
                boolean checked = !AllSettings.getHomeValue(this, allSettings);
                switchMaterial.setChecked(checked);
                AllSettings.setHomeValue(this, allSettings, checked);
            } else if (allSettings == AllSettings.VIP_FUNCTION) {
                SwitchMaterial switchMaterial = view.findViewById(R.id.sw_function);
                boolean pro = PurchaseManager.getInstance().isPro();
                if (!pro) {
                    ProDialog.show(this);
                }
                switchMaterial.setChecked(pro);
            } else if (allSettings == AllSettings.BOOT_OPEN_APP_FUNCTION) {
                boolean pro = PurchaseManager.getInstance().isPro();
                if (!pro) {
                    ProDialog.show(this);
                    return;
                }
                String bootOpenAppValue = AllSettings.getBootOpenAppValue(this, allSettings);
                if (bootOpenAppValue.isEmpty()) {
                    Toast.makeText(this, "请设置开机启动APP", Toast.LENGTH_SHORT).show();
                    return;
                }
                AppUtils.launchApp(bootOpenAppValue);
            }
        });
    }
}
