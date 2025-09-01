package com.inteavuthkuch.jankystuff.blockentity.blockaccelerator;

import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.common.BlockAcceleratorTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EliteBlockAcceleratorBlockEntity extends BlockAcceleratorBlockEntityBase {

    public EliteBlockAcceleratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.ELITE_BLOCK_ACCELERATOR_BE.get(), pPos, pBlockState, BlockAcceleratorTier.ELITE);
    }
}
