package com.inteavuthkuch.jankystuff.item.tool;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class ModToolTiers {

    public static final Tier REINFORCED_IRON = new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 1024 * 2, 6.0F, 2.0F, 14, () -> Ingredient.of(Items.IRON_INGOT));
    public static final Tier REINFORCED_DIAMOND = new SimpleTier(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1024 * 4, 8.0F, 3.0F, 14, () -> Ingredient.of(Items.DIAMOND));
    public static final Tier REINFORCED_NETHERITE = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1024 * 8, 9.0F, 4.0F, 15, () -> Ingredient.of(Items.NETHERITE_INGOT));
    public static final Tier INFINITY = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1024 * 10, 12.0F, 5.0F, 15, () -> Ingredient.of(Items.NETHER_STAR));
}
