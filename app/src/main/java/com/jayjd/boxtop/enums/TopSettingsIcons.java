package com.jayjd.boxtop.enums;

import com.jayjd.boxtop.R;

import lombok.Getter;

@Getter
public enum TopSettingsIcons {
    WIFI_ICON(R.drawable.ic_wifi_24dp),
    SETTINGS_ICON(R.drawable.ic_settings_24dp),
    BLUETOOTH_ICON(R.drawable.ic_bluetooth_connected_24dp),
    ETHERNET_ICON(R.drawable.ic_settings_ethernet_24dp);

    private final int iconId;

    TopSettingsIcons(int iconId) {
        this.iconId = iconId;
    }
}
