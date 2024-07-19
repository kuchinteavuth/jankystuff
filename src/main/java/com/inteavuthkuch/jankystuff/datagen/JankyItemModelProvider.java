package com.inteavuthkuch.jankystuff.datagen;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class JankyItemModelProvider extends ItemModelProvider {

    public JankyItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, JankyStuff.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.REINFORCED_COMPOUND);
        simpleItem(ModItems.REINFORCED_SMITHING_TEMPLATE);
        simpleItem(ModItems.INFINITY_SMITHING_TEMPLATE);
        simpleItem(ModItems.ROASTED_APPLE);
        simpleItem(ModItems.DRIED_FLESH);
        simpleItem(ModItems.COAL_PIECE);
        simpleItem(ModItems.CHARCOAL_PIECE);
        simpleItem(ModItems.RING_OF_THE_SKY);
        simpleItem(ModItems.RING_OF_TRUE_SIGHT);
        simpleItem(ModItems.RING_OF_FIRE);
        simpleItem(ModItems.RING_OF_REGENERATION);
        simpleItem(ModItems.RING_OF_SATURATION);
        simpleItem(ModItems.RING_OF_WATER);

        basicHandHeldItem(ModItems.REINFORCED_IRON_SWORD);
        basicHandHeldItem(ModItems.REINFORCED_IRON_PICKAXE);
        basicHandHeldItem(ModItems.REINFORCED_IRON_AXE);
        basicHandHeldItem(ModItems.REINFORCED_IRON_SHOVEL);
        basicHandHeldItem(ModItems.REINFORCED_IRON_PAXEL);

        basicHandHeldItem(ModItems.REINFORCED_DIAMOND_SWORD);
        basicHandHeldItem(ModItems.REINFORCED_DIAMOND_PICKAXE);
        basicHandHeldItem(ModItems.REINFORCED_DIAMOND_AXE);
        basicHandHeldItem(ModItems.REINFORCED_DIAMOND_SHOVEL);
        basicHandHeldItem(ModItems.REINFORCED_DIAMOND_PAXEL);

        basicHandHeldItem(ModItems.REINFORCED_NETHERITE_SWORD);
        basicHandHeldItem(ModItems.REINFORCED_NETHERITE_PICKAXE);
        basicHandHeldItem(ModItems.REINFORCED_NETHERITE_AXE);
        basicHandHeldItem(ModItems.REINFORCED_NETHERITE_SHOVEL);
        basicHandHeldItem(ModItems.REINFORCED_NETHERITE_PAXEL);

        basicHandHeldItem(ModItems.INFINITY_SWORD);
        basicHandHeldItem(ModItems.INFINITY_PICKAXE);
        basicHandHeldItem(ModItems.INFINITY_AXE);
        basicHandHeldItem(ModItems.INFINITY_SHOVEL);
        basicHandHeldItem(ModItems.INFINITY_PAXEL);

        basicHandHeldItem(ModItems.PORTABLE_CRATE);
        simpleItem(ModItems.MAGNET);
        simpleItem(ModItems.SPEED_UPGRADE);
        simpleItem(ModItems.ADVANCE_SPEED_UPGRADE);
    }

    protected void basicHandHeldItem(@NotNull DeferredItem<Item> item){
        ResourceLocation location = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.get()));
        getBuilder(location.toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "item/" + location.getPath()));
    }

    protected void simpleItem(@NotNull DeferredItem<Item> item){
        basicItem(item.get());
    }
}
