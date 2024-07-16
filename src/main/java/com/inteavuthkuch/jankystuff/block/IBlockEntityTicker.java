package com.inteavuthkuch.jankystuff.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

public interface IBlockEntityTicker {
    void tick(Level pLevel, BlockPos pPos, BlockState pState);

    static <T extends BlockEntity> BlockEntityTicker<T> getTickerHelper(){
        return (pLevel1, pPos, pState1, pBlockEntity) -> ((IBlockEntityTicker)pBlockEntity).tick(pLevel1, pPos, pState1);
    }
}
