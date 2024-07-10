package com.inteavuthkuch.jankystuff.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

public class DataComponentUtil {

    /**
     * Get {@link CustomData} component from {@link ItemStack}
     * @param itemStack Item input
     * @return {@link CompoundTag} of current item if exists else will create new {@link CompoundTag}
     */
    public static @NotNull CompoundTag tryGetCustomDataComponent(@NotNull ItemStack itemStack) {
        CustomData component = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return component.copyTag();
    }

    /**
     * <b>Note</b>: This method will replace old component data of current {@link ItemStack} with provided {@link CompoundTag}
     * @param itemStack Item input
     * @param tag {@link CompoundTag} (known as nbt tag)
     */
    public static void saveCustomDataComponent(@NotNull ItemStack itemStack, @NotNull CompoundTag tag) {
        CustomData component = CustomData.of(tag);
        itemStack.set(DataComponents.CUSTOM_DATA, component);
    }
}
