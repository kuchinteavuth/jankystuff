package com.inteavuthkuch.jankystuff.util.datagen;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

@FunctionalInterface
public interface BlockStateModelConfiguration {
    ConfiguredModel[] config(Block block, BlockState blockState);
}
