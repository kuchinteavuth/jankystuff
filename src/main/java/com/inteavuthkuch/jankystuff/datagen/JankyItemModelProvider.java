package com.inteavuthkuch.jankystuff.datagen;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.item.ModItems;
import com.inteavuthkuch.jankystuff.util.BlockSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

public class JankyItemModelProvider extends ItemModelProvider {

    public JankyItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, JankyStuff.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        BlockSet.of(
                ModItems.REINFORCED_COMPOUND,
                ModItems.REINFORCED_SMITHING_TEMPLATE,
                ModItems.INFINITY_SMITHING_TEMPLATE,
                ModItems.ROASTED_APPLE,
                ModItems.DRIED_FLESH,
                ModItems.COAL_PIECE,
                ModItems.CHARCOAL_PIECE,
                ModItems.RING_OF_THE_SKY,
                ModItems.RING_OF_TRUE_SIGHT,
                ModItems.RING_OF_FIRE,
                ModItems.RING_OF_REGENERATION,
                ModItems.RING_OF_SATURATION,
                ModItems.RING_OF_WATER,
                ModItems.ENERGY_DRINK,
                ModItems.EXPERIENCE_BAG,
                ModItems.MAGNET,
                ModItems.SPEED_UPGRADE,
                ModItems.ADVANCE_SPEED_UPGRADE,
                ModItems.BASIC_ITEM_FILTER,
                ModItems.CERAMETRON_SHARD
        ).each(this::simpleItem);

        BlockSet.of(
                ModItems.REINFORCED_IRON_SWORD,
                ModItems.REINFORCED_IRON_PICKAXE,
                ModItems.REINFORCED_IRON_AXE,
                ModItems.REINFORCED_IRON_SHOVEL,
                ModItems.REINFORCED_IRON_PAXEL,

                ModItems.REINFORCED_DIAMOND_SWORD,
                ModItems.REINFORCED_DIAMOND_PICKAXE,
                ModItems.REINFORCED_DIAMOND_AXE,
                ModItems.REINFORCED_DIAMOND_SHOVEL,
                ModItems.REINFORCED_DIAMOND_PAXEL,

                ModItems.REINFORCED_NETHERITE_SWORD,
                ModItems.REINFORCED_NETHERITE_PICKAXE,
                ModItems.REINFORCED_NETHERITE_AXE,
                ModItems.REINFORCED_NETHERITE_SHOVEL,
                ModItems.REINFORCED_NETHERITE_PAXEL,

                ModItems.INFINITY_SWORD,
                ModItems.INFINITY_PICKAXE,
                ModItems.INFINITY_AXE,
                ModItems.INFINITY_SHOVEL,
                ModItems.INFINITY_PAXEL,

                ModItems.PORTABLE_CRATE
        ).each(this::basicHandHeldItem);

        basicHandHeldItem(ModItems.MINER_STONE_PICKAXE, Items.STONE_PICKAXE);
        basicHandHeldItem(ModItems.MINER_IRON_PICKAXE, Items.IRON_PICKAXE);
        basicHandHeldItem(ModItems.MINER_DIAMOND_PICKAXE, Items.DIAMOND_PICKAXE);

        simpleBlockItem(ModBlocks.CERAMETRON_CLUSTER.get(), "item/generated",
                ResourceLocation.fromNamespaceAndPath(JankyStuff.MOD_ID, "block/cerametron_cluster"));
    }

    protected void basicHandHeldItem(@NotNull DeferredItem<Item> item){
        ResourceLocation location = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.get()));
        getBuilder(location.toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "item/" + location.getPath()));
    }

    protected void basicHandHeldItem(@NotNull DeferredItem<Item> item, Item forTexture) {
        ResourceLocation itemLocation = BuiltInRegistries.ITEM.getKey(item.get());
        ResourceLocation textureLocation = BuiltInRegistries.ITEM.getKey(forTexture);

        getBuilder(itemLocation.toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(textureLocation.getNamespace(), "item/" + textureLocation.getPath()));
    }

    protected void simpleItem(@NotNull DeferredItem<Item> item){
        basicItem(item.get());
    }

    public ItemModelBuilder simpleBlockItem(Block block, String parent, ResourceLocation parentTexture) {
        ItemModelBuilder builder = this.getBuilder(block.asItem().toString());
        return builder
                .parent(new ModelFile.UncheckedModelFile(parent))
                .texture("layer0", parentTexture);
    }
}
