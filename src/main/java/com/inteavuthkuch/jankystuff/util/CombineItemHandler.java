package com.inteavuthkuch.jankystuff.util;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public record CombineItemHandler(IItemHandler input, IItemHandler output) implements IItemHandler {
    @Override
    public int getSlots() {
        return input.getSlots() + output.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < input.getSlots()) {
            return input.getStackInSlot(slot);
        } else {
            return output.getStackInSlot(slot - input.getSlots());
        }
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot < input.getSlots())
            return input.insertItem(slot, stack, simulate);
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot >= input.getSlots()) {
            return output.extractItem(slot - input.getSlots(), amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return slot < input.getSlots()
                ? input.getSlotLimit(slot)
                : output.getSlotLimit(slot - input.getSlots());
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return slot < input.getSlots() && input.isItemValid(slot, stack);
    }
}
