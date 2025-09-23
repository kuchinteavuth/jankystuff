package com.inteavuthkuch.jankystuff.tag;

import com.inteavuthkuch.jankystuff.JankyStuff;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> RINGS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(JankyStuff.MOD_ID, "rings"));
        public static final TagKey<Item> PAXELS = createTag("minecraft", "paxels");

        private static TagKey<Item> createTag(String mod, String tagName) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(mod, tagName));
        }
        private static TagKey<Item> createTag(String tagName) {
            return createTag(JankyStuff.MOD_ID, tagName);
        }
    }
    public static class Blocks {
        @Deprecated(forRemoval = true, since = "NeoForge already has budding tag, so no need for custom tag")
        public static final TagKey<Block> BUDDING = BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", "budding"));
        public static final TagKey<Block> ALLOW_ACCELERATION =
                BlockTags.create(ResourceLocation.fromNamespaceAndPath(JankyStuff.MOD_ID, "allow_acceleration"));

        public static final TagKey<Block> BLOCK_ACCELERATION_BLACKLIST =
                BlockTags.create(ResourceLocation.fromNamespaceAndPath(JankyStuff.MOD_ID, "block_acceleration_blacklist"));

        public static final TagKey<Block> MINEABLE_WITH_PAXEL = createTag("minecraft", "mineable/paxel");

        private static TagKey<Block> createTag(String mod, String tagName) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(mod, tagName));
        }
        private static TagKey<Block> createTag(String tagName) {
            return createTag(JankyStuff.MOD_ID, tagName);
        }
    }
    public static class Entities {
        @Deprecated(forRemoval = true, since = "I want to prevent climbable mob from climbing the wall but it doesn't work")
        public static final TagKey<EntityType<?>> PREVENT_CLIMBABLE = createTag("prevent_climbable");

        private static TagKey<EntityType<?>> createTag(String tagName) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JankyStuff.MOD_ID, tagName));
        }
    }
}
