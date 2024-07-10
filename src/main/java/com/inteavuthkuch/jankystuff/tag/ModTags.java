package com.inteavuthkuch.jankystuff.tag;

import com.inteavuthkuch.jankystuff.JankyStuff;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> RINGS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(JankyStuff.MODID, "rings"));
    }
    public static class Blocks {
        public static final TagKey<Block> BUDDING = BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", "budding"));
        public static final TagKey<Block> ALLOW_ACCELERATION =
                BlockTags.create(ResourceLocation.fromNamespaceAndPath(JankyStuff.MODID, "allow_acceleration"));
    }
}
