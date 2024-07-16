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
        public static final TagKey<Item> PAXELS = createTag("minecraft", "paxels");

        private static TagKey<Item> createTag(String mod, String tagName) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(mod, tagName));
        }
        private static TagKey<Item> createTag(String tagName) {
            return createTag(JankyStuff.MODID, tagName);
        }
    }
    public static class Blocks {
        public static final TagKey<Block> BUDDING = BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", "budding"));
        public static final TagKey<Block> ALLOW_ACCELERATION =
                BlockTags.create(ResourceLocation.fromNamespaceAndPath(JankyStuff.MODID, "allow_acceleration"));
        public static final TagKey<Block> MINEABLE_WITH_PAXEL = createTag("minecraft", "mineable/paxel");

        private static TagKey<Block> createTag(String mod, String tagName) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(mod, tagName));
        }
        private static TagKey<Block> createTag(String tagName) {
            return createTag(JankyStuff.MODID, tagName);
        }
    }
}
