package com.inteavuthkuch.jankystuff.common;

import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public enum TimePeriod {

    DAY(0, 11999),
    SUNSET(12000, 12999),
    NIGHT(13000, 23999),
    INVALID(-1, -1)
    ;

    private final long start, end;
    TimePeriod(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public boolean contains(long time) {
        return time >= start && time <= end;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return name() + " [" + start + "–" + end + "]";
    }

    public static TimePeriod of(long time) {
        for(TimePeriod period : values()){
            if(period.contains(time))
                return period;
        }

        return INVALID;
    }

    public boolean is(TimePeriod period) {
        return this == period && period != INVALID;
    }

    public static TimePeriod get(@NotNull ServerLevel serverLevel) {
        if(serverLevel.isClientSide()) return TimePeriod.INVALID;
        long time = serverLevel.getDayTime() % 24000;
        return TimePeriod.of(time);
    }
}
