package com.inteavuthkuch.jankystuff.inventory;

import com.inteavuthkuch.jankystuff.item.custom.IFilterItem;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class GhostSlot extends Slot {
    public GhostSlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public boolean mayPickup(Player pPlayer) {
        return false;
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return !(pStack.getItem() instanceof IFilterItem)
                && !container.hasAnyMatching(item -> item.getItem() == pStack.getItem());
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void set(ItemStack pStack) {
        if (!pStack.isEmpty()) {
            ItemStack ghost = pStack.copy();
            ghost.setCount(1);
            super.set(ghost); // writes to container
        } else {
            super.set(ItemStack.EMPTY); // clears ghost
        }
        container.setChanged();
    }
}
