package com.inteavuthkuch.jankystuff.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class JankyStuffCommonConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> QUARRY_COOLDOWN;
    public static final ModConfigSpec.ConfigValue<Integer> MAGNET_RANGE;
    public static final ModConfigSpec.ConfigValue<Integer> CORRUPTED_DIRT_SPAWN_DELAY;
    public static final ModConfigSpec.ConfigValue<Integer> CORRUPTED_DIRT_MAX_ENTITY;

    static {
        BUILDER.push("Configs for JankyStuff Mod");

        // Define configs here
        QUARRY_COOLDOWN = BUILDER.comment("Determine how fast quarry get resources (Default it will generate resource 1 per 80 game ticks)")
                        .defineInRange("Basic Quarry speed (tick speed)", 80,10, Integer.MAX_VALUE);

        MAGNET_RANGE = BUILDER.comment("Determine how far magnet can absorb item (Default 8 blocks radius)")
                        .defineInRange("Magnet range", 8, 1, 64);

        CORRUPTED_DIRT_SPAWN_DELAY = BUILDER.comment("Speed which corrupted dirt spawn mob")
                        .defineInRange("Corrupted Dirt spawn delay", 200, 1, Integer.MAX_VALUE);

        CORRUPTED_DIRT_MAX_ENTITY = BUILDER.comment("Corrupted dirt will not spawn anymore mob if total mob cab is met")
                        .defineInRange("Corrupted Dirt entity cap", 24, 1, 64);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
