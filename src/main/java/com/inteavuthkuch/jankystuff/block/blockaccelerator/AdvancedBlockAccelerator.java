package com.inteavuthkuch.jankystuff.block.blockaccelerator;

import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.blockaccelerator.AdvanceBlockAcceleratorBlockEntity;
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

public class AdvancedBlockAccelerator extends BlockAcceleratorBase{

    public static final MapCodec<AdvancedBlockAccelerator> CODEC = simpleCodec(p -> new AdvancedBlockAccelerator());
    public AdvancedBlockAccelerator() {
        super(BlockAcceleratorTier.ADVANCE);
    }

    @Override
    protected BlockEntityType<? extends BlockEntity> getBlockEntityType() {
        return ModBlockEntity.ADVANCED_BLOCK_ACCELERATOR_BE.get();
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @ParametersAreNonnullByDefault
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AdvanceBlockAcceleratorBlockEntity(blockPos, blockState);
    }
}
