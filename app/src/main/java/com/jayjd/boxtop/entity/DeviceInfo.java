package com.jayjd.boxtop.entity;

import lombok.Data;

@Data
public class DeviceInfo {
    public int icon;
    public String name;
    public String status;

    public DeviceInfo(int icon, String name, String status) {
        this.icon = icon;
        this.name = name;
        this.status = status;
    }
}
