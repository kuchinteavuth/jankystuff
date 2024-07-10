package com.inteavuthkuch.jankystuff.datagen;

import com.inteavuthkuch.jankystuff.item.ModItems;
import com.inteavuthkuch.jankystuff.tag.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class JankyItemTagProvider extends ItemTagsProvider {

    public JankyItemTagProvider(
            PackOutput pOutput,
            CompletableFuture<HolderLookup.Provider> pLookupProvider,
            CompletableFuture<TagLookup<Block>> pBlockTags) {
        super(pOutput, pLookupProvider, pBlockTags);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        this.tag(ItemTags.PICKAXES)
                .add(ModItems.REINFORCED_IRON_PICKAXE.get(),
                        ModItems.REINFORCED_DIAMOND_PICKAXE.get(),
                        ModItems.INFINITY_PICKAXE.get());

        this.tag(ItemTags.SWORDS)
                .add(ModItems.REINFORCED_IRON_SWORD.get())
                .add(ModItems.REINFORCED_DIAMOND_SWORD.get())
                .add(ModItems.REINFORCED_NETHERITE_SWORD.get());

        this.tag(ModTags.Items.RINGS)
                .add(ModItems.RING_OF_FIRE.get())
                .add(ModItems.RING_OF_TRUE_SIGHT.get())
                .add(ModItems.RING_OF_REGENERATION.get())
                .add(ModItems.RING_OF_SATURATION.get())
                .add(ModItems.RING_OF_WATER.get())
                .add(ModItems.RING_OF_THE_SKY.get());
    }
}
