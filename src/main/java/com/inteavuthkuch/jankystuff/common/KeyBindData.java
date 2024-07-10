package com.inteavuthkuch.jankystuff.common;

public enum KeyBindData {
    MAGNET("magnet"),
    OPEN_PERSONAL_CRATE("open_personal_crate")
    ;

    private final String name;
    KeyBindData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
