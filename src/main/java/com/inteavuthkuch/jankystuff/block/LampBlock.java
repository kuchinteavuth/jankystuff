package com.inteavuthkuch.jankystuff.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class LampBlock extends Block {

    public LampBlock() {
        super(BlockBehaviour.Properties.of()
                .lightLevel(light -> 15)
                .strength(0.3F)
                .mapColor(MapColor.COLOR_BROWN)
                .sound(SoundType.WOOD)
        );
    }
}
