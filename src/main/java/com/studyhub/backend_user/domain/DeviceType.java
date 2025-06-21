package com.studyhub.backend_user.domain;

public enum DeviceType {
    WEB,
    MOBILE,
    TABLET,
    OTHER;

    public static DeviceType fromString(String text) {
        if (text == null) {
            return OTHER;
        }
        for (DeviceType type : DeviceType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return OTHER;
    }
}
