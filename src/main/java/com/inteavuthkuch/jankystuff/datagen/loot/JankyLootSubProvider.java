package com.inteavuthkuch.jankystuff.datagen.loot;

import com.inteavuthkuch.jankystuff.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class JankyLootSubProvider extends BlockLootSubProvider {

    public JankyLootSubProvider(HolderLookup.Provider lookupProvider) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.TICK_ACCELERATOR.get());
        this.dropSelf(ModBlocks.WOODEN_CRATE.get());
        this.dropSelf(ModBlocks.LAMP.get());
        this.dropSelf(ModBlocks.BASIC_QUARRY.get());
        this.dropSelf(ModBlocks.BLOCK_BREAKER.get());
        this.dropSelf(ModBlocks.PASSTHROUGH_GLASS.get());
        this.dropSelf(ModBlocks.MOB_DAMAGE_PLATE.get());
        this.dropSelf(ModBlocks.ADVANCE_DAMAGE_PLATE.get());
        this.dropSelf(ModBlocks.THE_VOID.get());
        this.add(ModBlocks.METAL_CRATE.get(), this::createShulkerBoxDrop);
        this.add(ModBlocks.CORRUPTED_DIRT.get(), b -> createSingleItemTableWithSilkTouch(b, Items.DIRT));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value).toList();
    }
}
