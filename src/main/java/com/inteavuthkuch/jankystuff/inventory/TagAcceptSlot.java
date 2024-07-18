package com.inteavuthkuch.jankystuff.inventory;

import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TagAcceptSlot extends Slot {

    private final TagKey<Item> tag;

    public TagAcceptSlot(Container pContainer, int pSlot, int pX, int pY, TagKey<Item> tag) {
        super(pContainer, pSlot, pX, pY);
        this.tag = tag;
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return pStack.is(tag);
    }
}
