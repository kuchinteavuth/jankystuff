package com.inteavuthkuch.jankystuff.blockentity.fluidtank;

import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.common.FluidTankTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EliteFluidTankBlockEntity extends FluidTankBlockEntityBase{
    public EliteFluidTankBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.ELITE_FLUID_TANK_BE.get(), pPos, pBlockState, FluidTankTier.ELITE);
    }
}
