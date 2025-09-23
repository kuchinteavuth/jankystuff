package com.inteavuthkuch.jankystuff.datagen.loot;

import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.component.ModComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

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
        this.dropSelf(ModBlocks.BASIC_BLOCK_ACCELERATOR.get());
        this.dropSelf(ModBlocks.ADVANCED_BLOCK_ACCELERATOR.get());
        this.dropSelf(ModBlocks.ELITE_BLOCK_ACCELERATOR.get());
        this.dropSelf(ModBlocks.ULTIMATE_BLOCK_ACCELERATOR.get());
        this.dropSelf(ModBlocks.WATER_SOURCE.get());
        this.add(ModBlocks.BASIC_FLUID_TANK.get(), this::createFluidTankLoot);
        this.add(ModBlocks.ADVANCED_FLUID_TANK.get(), this::createFluidTankLoot);
        this.add(ModBlocks.ELITE_FLUID_TANK.get(), this::createFluidTankLoot);
        this.add(ModBlocks.ULTIMATE_FLUID_TANK.get(), this::createFluidTankLoot);
        this.add(ModBlocks.METAL_CRATE.get(), this::createShulkerBoxDrop);
        this.add(ModBlocks.CORRUPTED_DIRT.get(), b -> createSingleItemTableWithSilkTouch(b, Items.DIRT));
        this.add(ModBlocks.SKY_SHIFTER.get(), b -> copyComponents(b, Set.of(DataComponents.CUSTOM_DATA)));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value).toList();
    }

    private LootTable.Builder createFluidTankLoot(Block block) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                        .include(DataComponents.CUSTOM_DATA))
                                .apply(ApplyExplosionDecay.explosionDecay())
                        )
                );
    }

    private LootTable.Builder copyComponents(Block block, @NotNull Set<DataComponentType<?>> dataComponentTypes) {
        CopyComponentsFunction.Builder copyComponentsFunction = CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY);

        for(DataComponentType<?> dataComponentType : dataComponentTypes) {
            copyComponentsFunction.include(dataComponentType);
        }

        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(block)
                                .apply(copyComponentsFunction)
                                .apply(ApplyExplosionDecay.explosionDecay())
                        )
                );
    }
}
