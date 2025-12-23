package com.jayjd.boxtop.enums;

import lombok.Getter;

@Getter
public enum PreviewSettings {
    START("启动软件"),
    VIEW("查看信息"),
    MOVE("移动软件"),
    HIDE("加入隐私"),
    SHOW("移除隐私"),
    DELETE("删除常用"),
    UNINSTALL("卸载软件");
    private final String displayName;

    PreviewSettings(String displayName) {
        this.displayName = displayName;
    }

    public static PreviewSettings[] getFavoriteSettings(boolean system) {
        if (system) {
            return new PreviewSettings[]{START, VIEW, MOVE, DELETE};
        }
        return new PreviewSettings[]{START, VIEW, HIDE, MOVE, DELETE, UNINSTALL};
    }

    public static PreviewSettings[] getAllAppsSettings() {
        return new PreviewSettings[]{START, VIEW, HIDE, UNINSTALL};
    }


    public static PreviewSettings[] getHideAppsSettings() {
        return new PreviewSettings[]{START, VIEW, SHOW, UNINSTALL};
    }
}
