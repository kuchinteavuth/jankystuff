package com.inteavuthkuch.jankystuff.common;

public class Constraints {
    public static final int ACCELERATOR_TICK_DELAY = 20;
    public static final int TICK_PER_SECOND = 20;
    public static final int LIQUID_PER_BUCKET = 1000;


    public static class Config {
        public static final String CLIENT = "jankystuff-client.toml";
        public static final String COMMON = "jankystuff-common.toml";
        public static final String SERVER = "jankystuff-server.toml";
    }

    public enum RenderType {
        SOLID("minecraft:solid"),
        CUTOUT("minecraft:cutout"),
        CUTOUT_MIPPED("minecraft:cutout_mipped"),
        CUTOUT_MIPPED_ALL("minecraft:cutout_mipped_all"),
        TRANSLUCENT("minecraft:translucent"),
        TRIPWIRE("minecraft:tripwire")
        ;

        private final String name;
        RenderType(String name){
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
    }
}
