package com.inteavuthkuch.jankystuff.datagen;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.common.Constraints;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.NeoForgeRenderTypes;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
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

    private void glassBlockWithItem(@NotNull DeferredBlock<Block> block) {
        Block b = block.get();
        ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(b);
        String blockName = blockKey.getPath();
        var model = models().cubeAll(blockName, blockTexture(b)).renderType("minecraft:cutout");
        simpleBlockItem(b, model);
    }

    private void glassBlockWithItem(@NotNull DeferredBlock<Block> block, Constraints.RenderType renderType) {
        Block b = block.get();
        ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(b);
        String blockName = blockKey.getPath();
        var model = models().cubeAll(blockName, blockTexture(b)).renderType(renderType.getName());
        simpleBlockWithItem(b, model);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithCustomBlockModel(ModBlocks.TICK_ACCELERATOR);
        simpleBlockWithItem(ModBlocks.WOODEN_CRATE);
        simpleBlockWithItem(ModBlocks.METAL_CRATE);
        simpleBlockWithItem(ModBlocks.LAMP);
        blockWithCustomBlockModel(ModBlocks.CORRUPTED_DIRT);
        blockWithCustomBlockModel(ModBlocks.MOB_DAMAGE_PLATE);
        blockWithCustomBlockModel(ModBlocks.ADVANCE_DAMAGE_PLATE);
        glassBlockWithItem(ModBlocks.PASSTHROUGH_GLASS, Constraints.RenderType.TRANSLUCENT);

    }
}
