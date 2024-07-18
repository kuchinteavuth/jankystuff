package com.inteavuthkuch.jankystuff.inventory;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.BlockBreakerBlock;
import com.inteavuthkuch.jankystuff.blockentity.BlockBreakerBlockEntity;
import com.inteavuthkuch.jankystuff.common.ContainerType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

public class BlockBreakerInvWrapper extends InvWrapper {

    public BlockBreakerInvWrapper(Container inv) {
        super(inv);
    }

    public static @Nullable BlockBreakerInvWrapper create(Level level, BlockPos pos, BlockState state, BlockEntity be, Direction direction) {
        if(be != null){
            Direction blockFace = be.getBlockState().getValue(BlockBreakerBlock.FACING);
            if(direction != blockFace)
                return new BlockBreakerInvWrapper((BlockBreakerBlockEntity) be);
        }
        return null;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        int deniedSlot = ContainerType.BLOCK_BREAKER.getCol() * ContainerType.BLOCK_BREAKER.getRow();
        //JankyStuff.LOGGER.debug("Tried extract slot: {}", slot);
        if(slot < deniedSlot){
            return super.extractItem(slot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return false;
    }
}
