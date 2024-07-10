package com.inteavuthkuch.jankystuff.datagen;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.tag.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class JankyBlockTagProvider extends BlockTagsProvider {

    public JankyBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, JankyStuff.MODID ,existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        this.tag(Tags.Blocks.STORAGE_BLOCKS)
                .add(ModBlocks.TICK_ACCELERATOR.get(), ModBlocks.LAMP.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.TICK_ACCELERATOR.get(),
                        ModBlocks.METAL_CRATE.get(),
                        ModBlocks.BASIC_QUARRY.get());

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.WOODEN_CRATE.get());

        this.tag(ModTags.Blocks.BUDDING)
                .add(Blocks.AMETHYST_BLOCK);

        this.tag(ModTags.Blocks.ALLOW_ACCELERATION)
                .addTag(ModTags.Blocks.BUDDING)
                .addTag(Tags.Blocks.BUDDING_BLOCKS)
                .addTag(BlockTags.CROPS);
    }
}
