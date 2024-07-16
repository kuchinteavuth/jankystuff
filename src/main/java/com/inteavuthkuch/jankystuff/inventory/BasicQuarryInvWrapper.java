package com.inteavuthkuch.jankystuff.inventory;

import com.inteavuthkuch.jankystuff.blockentity.BasicQuarryBlockEntity;
import com.inteavuthkuch.jankystuff.item.upgrade.BaseUpgradeItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class BasicQuarryInvWrapper extends InvWrapper {
    public BasicQuarryInvWrapper(Container inv) {
        super(inv);
    }

    public static BasicQuarryInvWrapper create (Level level, BlockPos pPos, BlockState pState, BlockEntity be, Direction direction) {
        return new BasicQuarryInvWrapper((BasicQuarryBlockEntity)be);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getBurnTime(null) > 0 || stack.getItem() instanceof BaseUpgradeItem;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(getStackInSlot(slot).is(Items.BUCKET)){
            // Handle regular bucket not sure about modded bucket
            return super.extractItem(slot, amount, simulate);
        }
        else {
            // Not allow any extraction from the quarry by capability
            return ItemStack.EMPTY;
        }
    }
}
