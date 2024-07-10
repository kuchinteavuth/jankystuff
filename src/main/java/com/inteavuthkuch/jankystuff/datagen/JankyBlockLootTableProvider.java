package com.inteavuthkuch.jankystuff.datagen;

import com.inteavuthkuch.jankystuff.datagen.loot.JankyLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class JankyBlockLootTableProvider  {
    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(JankyLootSubProvider::new, LootContextParamSets.BLOCK)
        ), lookup);
    }
}

