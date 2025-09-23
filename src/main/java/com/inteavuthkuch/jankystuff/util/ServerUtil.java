package com.inteavuthkuch.jankystuff.util;

import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class ServerUtil {
    private ServerUtil(){}

    public static boolean isDayTime(@NotNull ServerLevel level) {
        // just to be safe
        if(level.isClientSide()) return false;
        long serverTime = level.getDayTime() % 24000;
        return serverTime >= 0 && serverTime < 13000;
    }

    public static boolean isNightTime(@NotNull ServerLevel level) {
        return !isDayTime(level);
    }
}
