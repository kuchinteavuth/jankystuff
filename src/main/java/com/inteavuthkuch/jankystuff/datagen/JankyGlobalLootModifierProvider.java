package com.inteavuthkuch.jankystuff.datagen;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.item.ModItems;
import com.inteavuthkuch.jankystuff.common.loot.AddItemModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class JankyGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public JankyGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, JankyStuff.MODID);
    }

    @Override
    protected void start() {
        add("infinity_smithing_template_from_wither", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/wither")).build(),
                LootItemRandomChanceCondition.randomChance(0.2f).build()
        }, ModItems.INFINITY_SMITHING_TEMPLATE.get()));

        add("ring_of_the_sky_from_enderman", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/enderman")).build(),
                LootItemRandomChanceCondition.randomChance(0.2f).build()
        }, ModItems.RING_OF_THE_SKY.get()));

        add("ring_of_the_sky_from_end_city_chest", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/end_city_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.1f).build()
        }, ModItems.RING_OF_THE_SKY.get()));

        add("ring_of_true_sight_from_dungeon", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/simple_dungeon")).build(),
                LootItemRandomChanceCondition.randomChance(0.3f).build()
        }, ModItems.RING_OF_TRUE_SIGHT.get()));

        add("ring_of_regeneration_from_dungeon", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/simple_dungeon")).build(),
                LootItemRandomChanceCondition.randomChance(0.3f).build()
        }, ModItems.RING_OF_REGENERATION.get()));

        add("ring_of_saturation_from_dungeon", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/simple_dungeon")).build(),
                LootItemRandomChanceCondition.randomChance(0.3f).build()
        }, ModItems.RING_OF_SATURATION.get()));

        add("ring_of_water_from_dungeon", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/simple_dungeon")).build(),
                LootItemRandomChanceCondition.randomChance(0.3f).build()
        }, ModItems.RING_OF_WATER.get()));

        add("ring_of_fire_from_fortress", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/nether_bridge")).build(),
                LootItemRandomChanceCondition.randomChance(0.3f).build()
        }, ModItems.RING_OF_FIRE.get()));
    }
}
