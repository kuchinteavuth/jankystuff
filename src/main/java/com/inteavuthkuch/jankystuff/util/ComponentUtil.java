package com.inteavuthkuch.jankystuff.util;

import com.inteavuthkuch.jankystuff.JankyStuff;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ComponentUtil {
    public static @NotNull MutableComponent translateItem(@NotNull String path) {
        return Component.translatable(String.format("item.%s.%s", JankyStuff.MODID, path));
    }
    public static @NotNull MutableComponent translateItem(@NotNull String... paths) {
        return Component.translatable(String.format("item.%s.%s", JankyStuff.MODID, String.join(".", paths)));
    }

    public static @NotNull MutableComponent translateItem(DeferredItem<Item> item, @Nullable String... paths) {
        ResourceLocation location = item.getId();
        if(paths == null)
            return Component.translatable(String.format("item.%s.%s", location.getNamespace(), location.getPath()));
        return Component.translatable(String.format("item.%s.%s.%s", location.getNamespace(), location.getPath(), String.join(".", paths)));
    }

    public static MutableComponent translateBlock(@NotNull String path) {
        return Component.translatable(String.format("block.%s.%s", JankyStuff.MODID, path));
    }

    public static @NotNull MutableComponent translateBlock(DeferredBlock<Block> block, @Nullable String... paths) {
        ResourceLocation location = block.getId();
        if(paths == null)
            return Component.translatable(String.format("block.%s.%s", location.getNamespace(), location.getPath()));
        return Component.translatable(String.format("block.%s.%s.%s", location.getNamespace(), location.getPath(), String.join(".", paths)));
    }
}
