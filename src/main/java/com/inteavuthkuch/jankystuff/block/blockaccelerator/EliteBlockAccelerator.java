package com.inteavuthkuch.jankystuff.block.blockaccelerator;

import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.blockaccelerator.EliteBlockAcceleratorBlockEntity;
import com.inteavuthkuch.jankystuff.common.BlockAcceleratorTier;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class EliteBlockAccelerator extends BlockAcceleratorBase{

    public static final MapCodec<EliteBlockAccelerator> CODEC = simpleCodec(p -> new EliteBlockAccelerator());

    public EliteBlockAccelerator() {
        super(BlockAcceleratorTier.ELITE);
    }

    @Override
    protected BlockEntityType<? extends BlockEntity> getBlockEntityType() {
        return ModBlockEntity.ELITE_BLOCK_ACCELERATOR_BE.get();
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @ParametersAreNonnullByDefault
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EliteBlockAcceleratorBlockEntity(blockPos, blockState);
    }
}
