package com.inteavuthkuch.jankystuff.blockentity.fluidtank;

import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.common.FluidTankTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BasicFluidTankBlockEntity extends FluidTankBlockEntityBase{
    public BasicFluidTankBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.BASIC_FLUID_TANK_BE.get(), pPos, pBlockState, FluidTankTier.BASIC);
    }
}
