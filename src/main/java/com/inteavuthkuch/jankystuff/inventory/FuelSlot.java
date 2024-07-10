package com.inteavuthkuch.jankystuff.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FuelSlot extends Slot {
    public FuelSlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if(stack.getBurnTime(null) > 0)
            return super.mayPlace(stack);
        return false;
    }
}
