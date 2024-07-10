package com.inteavuthkuch.jankystuff.inventory;

import com.inteavuthkuch.jankystuff.item.upgrade.BaseUpgradeItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class UpgradeSlot extends Slot {
    public UpgradeSlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return pStack.getItem() instanceof BaseUpgradeItem;
    }
}
