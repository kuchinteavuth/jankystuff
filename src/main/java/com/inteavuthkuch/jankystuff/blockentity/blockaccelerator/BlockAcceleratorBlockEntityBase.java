package com.inteavuthkuch.jankystuff.blockentity.blockaccelerator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockAcceleratorBlockEntityBase extends BlockEntity {
    private final int tickModifier;
    public BlockAcceleratorBlockEntityBase(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int tickModifier) {
        super(pType, pPos, pBlockState);
        this.tickModifier = tickModifier;
    }

    public int getTickModifier() {
        return tickModifier;
    }
}
