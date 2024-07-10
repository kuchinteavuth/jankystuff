package com.inteavuthkuch.jankystuff.util;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

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
}
