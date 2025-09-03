package com.inteavuthkuch.jankystuff.common;

import com.inteavuthkuch.jankystuff.util.Conversion;

public enum FluidTankTier {
    BASIC(128),
    ADVANCED(256),
    ELITE(512),
    ULTIMATE(1024)
    ;


    private final int capacity;
    private final int maxInsert;
    private final int maxExtract;

    FluidTankTier(int capacity) {
        this(capacity, -1 , -1);
    }

    FluidTankTier(int capacity, int maxInsert, int maxExtract) {
        this.capacity = capacity;
        this.maxExtract = maxExtract;
        this.maxInsert = maxInsert;
    }

    public int getCapacity() {
        return Conversion.fluidInMb(capacity);
    }

    public int getBucketCapacity() {
        return capacity;
    }

    public int getMaxInsert() {
        return maxInsert;
    }
    public int getMaxExtract() {
        return maxExtract;
    }
}
