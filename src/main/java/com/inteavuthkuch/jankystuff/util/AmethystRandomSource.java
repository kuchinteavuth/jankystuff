package com.inteavuthkuch.jankystuff.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

import java.util.Random;

/// Made for amethyst block tick only, not suitable for normal RandomSource
public class AmethystRandomSource implements RandomSource {
    private int intValue;
    private float floatValue;
    private long longValue;
    private double doubleValue;
    private double gaussianValue;
    private boolean booleanValue;
    private final Random random = new Random();

    public AmethystRandomSource(int value) {
        this.intValue = value;
        floatValue = value;
        longValue = value;
        doubleValue = value;
        gaussianValue = value;
        booleanValue = true;
    }

    public AmethystRandomSource(int intValue, float floatValue, long longValue, double doubleValue, double gaussianValue, boolean booleanValue){
        this.intValue = intValue;
        this.floatValue = floatValue;
        this.longValue = longValue;
        this.doubleValue = doubleValue;
        this.gaussianValue = gaussianValue;
        this.booleanValue = booleanValue;
    }

    @Override
    public RandomSource fork() {
        return new AmethystRandomSource(intValue, floatValue, longValue, doubleValue, gaussianValue, booleanValue);
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return new LegacyRandomSource.LegacyPositionalRandomFactory(0);
    }

    @Override
    public void setSeed(long l) {

    }

    @Override
    public int nextInt() {
        return intValue;
    }

    @Override
    public int nextInt(int bound) {
        if (bound == 5) return 0; // Always trigger growth
        return random.nextInt(bound);
    }

    @Override
    public long nextLong() {
        return longValue;
    }

    @Override
    public boolean nextBoolean() {
        return booleanValue;
    }

    @Override
    public float nextFloat() {
        return floatValue;
    }

    @Override
    public double nextDouble() {
        return doubleValue;
    }

    @Override
    public double nextGaussian() {
        return gaussianValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public void setGaussianValue(double gaussianValue) {
        this.gaussianValue = gaussianValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public static AmethystRandomSource createFixSource(int value) {
        return new AmethystRandomSource(value);
    }
}
