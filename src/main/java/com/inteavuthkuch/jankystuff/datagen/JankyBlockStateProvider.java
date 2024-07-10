package com.inteavuthkuch.jankystuff.datagen;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

public class JankyBlockStateProvider extends BlockStateProvider {

    public JankyBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, JankyStuff.MODID, exFileHelper);
    }

    private void simpleBlockWithItem(@NotNull DeferredBlock<Block> block) {
        // Create block-state, block-model, and item-model ( texture must be added otherwise it will throw an error)
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    private void blockWithCustomBlockModel(DeferredBlock<Block> block){
        simpleBlock(block.get(), new ModelFile.UncheckedModelFile(modLoc("block/" + block.getId().getPath())));
        simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(modLoc("block/" + block.getId().getPath())));
    }
    private void blockWithCustomBlockModel(DeferredBlock<Block> block, String parent){
        simpleBlock(block.get(), new ModelFile.UncheckedModelFile(parent));
        simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(parent));
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithCustomBlockModel(ModBlocks.TICK_ACCELERATOR);
        simpleBlockWithItem(ModBlocks.WOODEN_CRATE);
        simpleBlockWithItem(ModBlocks.METAL_CRATE);
        simpleBlockWithItem(ModBlocks.LAMP);
    }
}
