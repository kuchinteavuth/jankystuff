package com.inteavuthkuch.jankystuff.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class ItemStackUtil {
    private static final Random random = new Random();

    /**
     * Get random 1 ItemStack from the collection
     * This is not accounted for wight system yet
     * @param stacks ItemStack collection
     * @return ItemStack from the provided collection
     */
    public static ItemStack getRandomItem(@NotNull List<ItemStack> stacks) {
        if(stacks.isEmpty())
            return ItemStack.EMPTY;
        int randomIndex = random.nextInt(stacks.size());
        return stacks.get(randomIndex);
    }

    @Nullable
    public static ItemContainerContents getItemContainerContents(ItemStack itemStack) {
        return itemStack.get(DataComponents.CONTAINER);
    }

    @Nullable
    public static CustomData getCustomData(ItemStack itemStack) {
        return itemStack.get(DataComponents.CUSTOM_DATA);
    }
}
