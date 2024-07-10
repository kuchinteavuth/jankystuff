package com.inteavuthkuch.jankystuff.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class TagUtil {

    public static List<ItemStack> getItemsFromTag(TagKey<Item> tag) {
        return BuiltInRegistries.ITEM.entrySet()
                .stream()
                .map(i -> new ItemStack(i.getValue()))
                .filter(i -> i.is(tag))
                .toList();
    }

    /**
     * Ores: Iron, gold, copper etc<br/>
     * Gems: Redstone, glowstone, diamond ect
     * @return List Valuable ItemStack
     */
    public static List<ItemStack> getCommonResources() {
        return getItemsFromTags(
                Tags.Items.RAW_MATERIALS,
                Tags.Items.GEMS_LAPIS,
                Tags.Items.GEMS_AMETHYST,
                Tags.Items.GEMS_EMERALD,
                Tags.Items.GEMS_DIAMOND,
                Tags.Items.DUSTS_GLOWSTONE,
                Tags.Items.DUSTS_REDSTONE,
                ItemTags.COALS
        );
    }

    @SafeVarargs
    public static @NotNull List<ItemStack> getItemsFromTags(TagKey<Item> @NotNull ... tags) {
        List<ItemStack> stacks = new ArrayList<>();

        for(TagKey<Item> tag : tags) {
            List<ItemStack> tagStacks = getItemsFromTag(tag);
            if(!tagStacks.isEmpty()) {
                stacks.addAll(tagStacks);
            }
        }

        return stacks;
    }

    /**
     * Query all items that has tag #c:raw_materials
     * @return List of raw material item that has registered
     */
    public static List<ItemStack> getRawMaterials() {
        return getItemsFromTag(Tags.Items.RAW_MATERIALS);
    }
}
