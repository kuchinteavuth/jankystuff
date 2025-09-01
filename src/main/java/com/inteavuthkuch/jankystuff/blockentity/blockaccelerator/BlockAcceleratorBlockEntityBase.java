package com.inteavuthkuch.jankystuff.blockentity.blockaccelerator;

import com.inteavuthkuch.jankystuff.block.IBlockEntityTicker;
import com.inteavuthkuch.jankystuff.block.blockaccelerator.BasicBlockAccelerator;
import com.inteavuthkuch.jankystuff.common.BlockAcceleratorTier;
import com.inteavuthkuch.jankystuff.tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public abstract class BlockAcceleratorBlockEntityBase extends BlockEntity implements IBlockEntityTicker {
    private final BlockAcceleratorTier acceleratorTier;
    public BlockAcceleratorBlockEntityBase(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, BlockAcceleratorTier acceleratorTier) {
        super(pType, pPos, pBlockState);
        this.acceleratorTier = acceleratorTier;
    }

    public BlockAcceleratorTier getAcceleratorTier() {
        return acceleratorTier;
    }

    protected Optional<BlockEntityTicker<BlockEntity>> getTicker(BlockEntity blockEntity, Level level){
        BlockEntityType<?> blockEntityType = blockEntity.getType();
        BlockEntityTicker<?> blockEntityTicker = blockEntity.getBlockState().getTicker(level, blockEntityType);

        if(blockEntityTicker != null) {
            try {
                @SuppressWarnings("unchecked")
                BlockEntityTicker<BlockEntity> safeTicker = (BlockEntityTicker<BlockEntity>) blockEntityTicker;
                return Optional.of(safeTicker);
            } catch (ClassCastException e){
                // Optional: log or ignore if incompatible
            }
        }
        return Optional.empty();
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(pLevel.isClientSide()) return;

        Direction facing = pState.getValue(BasicBlockAccelerator.FACING);
        BlockPos targetPos = pPos.relative(facing);
        BlockEntity targetBlockEntity = pLevel.getBlockEntity(targetPos);
        BlockState targetState = pLevel.getBlockState(targetPos);

        if(targetBlockEntity instanceof BlockAcceleratorBlockEntityBase) return;
        if(targetBlockEntity != null && !targetState.is(ModTags.Blocks.BLOCK_ACCELERATION_BLACKLIST))
        {
            getTicker(targetBlockEntity, pLevel).ifPresent(ticker -> {
                for(int i=0; i < getAcceleratorTier().getTickModifier(); i++){
                    ticker.tick(pLevel, targetPos, targetState, targetBlockEntity);
                }
            });
        }
    }
}
