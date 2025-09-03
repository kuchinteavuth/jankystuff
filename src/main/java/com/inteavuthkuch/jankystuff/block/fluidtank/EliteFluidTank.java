package com.inteavuthkuch.jankystuff.block.fluidtank;

import com.inteavuthkuch.jankystuff.blockentity.fluidtank.EliteFluidTankBlockEntity;
import com.inteavuthkuch.jankystuff.common.FluidTankTier;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class EliteFluidTank extends FluidTankBase{
    public static final MapCodec<FluidTankBase> CODEC = simpleCodec(p -> new EliteFluidTank());

    public EliteFluidTank() {
        super(FluidTankTier.ELITE);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @ParametersAreNonnullByDefault
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EliteFluidTankBlockEntity(blockPos, blockState);
    }
}
