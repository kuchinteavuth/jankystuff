package com.inteavuthkuch.jankystuff.common;

public enum BlockAcceleratorTier {

    BASIC(2),
    ADVANCE(4),
    ELITE(8),
    ULTIMATE(16),
    QUANTUM(32)
    ;

    private final int tickModifier;
    private
    BlockAcceleratorTier(int tickModifier) {
        this.tickModifier = tickModifier;
    }
    public int getTickModifier() {
        return tickModifier;
    }
}
