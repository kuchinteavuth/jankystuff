package com.inteavuthkuch.jankystuff.blockentity.blockaccelerator;

import com.inteavuthkuch.jankystuff.block.IBlockEntityTicker;
import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BasicBlockAcceleratorBlockEntity extends BlockAcceleratorBlockEntityBase implements IBlockEntityTicker {

    public BasicBlockAcceleratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.BASIC_BLOCK_ACCELERATOR_BE.get(), pPos, pBlockState, 4);
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(pLevel.isClientSide()) return;

        BlockPos abovePos = pPos.above();
        BlockEntity blockEntity = pLevel.getBlockEntity(abovePos);
        BlockState aboveState = pLevel.getBlockState(abovePos);

        if(blockEntity instanceof BlockAcceleratorBlockEntityBase) return;
        if(blockEntity != null && !aboveState.is(ModTags.Blocks.BLOCK_ACCELERATION_BLACKLIST))
        {
            BlockEntityType<?> blockEntityType = blockEntity.getType();
            BlockEntityTicker<?> blockEntityTicker = blockEntity.getBlockState().getTicker(pLevel, blockEntityType);

            if(blockEntityTicker != null) {
                try {
                    @SuppressWarnings("unchecked")
                    BlockEntityTicker<BlockEntity> safeTicker = (BlockEntityTicker<BlockEntity>) blockEntityTicker;
                    for(int i=0; i < getTickModifier(); i++){
                        safeTicker.tick(pLevel, abovePos, aboveState, blockEntity);
                    }
                } catch (ClassCastException e){
                    // Optional: log or ignore if incompatible
                }
            }
        }
    }
}
