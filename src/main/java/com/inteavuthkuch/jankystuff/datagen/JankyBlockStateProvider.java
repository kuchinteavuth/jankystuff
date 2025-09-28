package com.inteavuthkuch.jankystuff.datagen;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.block.blockaccelerator.BasicBlockAccelerator;
import com.inteavuthkuch.jankystuff.common.Constraints;
import com.inteavuthkuch.jankystuff.util.BlockSet;
import com.inteavuthkuch.jankystuff.util.datagen.BlockStateModelConfiguration;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class JankyBlockStateProvider extends BlockStateProvider {

    public JankyBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, JankyStuff.MOD_ID, exFileHelper);
    }

    private void simpleBlockWithItem(@NotNull DeferredBlock<Block> block) {
        // Create block-state, block-model, and item-model ( texture must be added otherwise it will throw an error)
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    private void blockWithCustomBlockAndItemModel(DeferredBlock<Block> block){
        simpleBlock(block.get(), new ModelFile.UncheckedModelFile(modLoc("block/" + block.getId().getPath())));
        simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(modLoc("block/" + block.getId().getPath())));
    }
    private void blockWithCustomBlockAndItemModel(DeferredBlock<Block> block, @NotNull String folder, @Nullable String customModelName){
        while(folder.endsWith("/")){
            folder = folder.substring(0, folder.length() - 1);
        }

        folder = folder.toLowerCase(); // this can do it
        String modelName = customModelName == null ? block.getId().getPath() : customModelName;
        ResourceLocation location = modLoc("block/" + folder + "/" + modelName);
        simpleBlock(block.get(), new ModelFile.UncheckedModelFile(location));
        simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(location));
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

    protected void blockWithVariantsAndItem(@NotNull DeferredBlock<Block> block, @Nullable Function<BlockState, ConfiguredModel[]> configuration) {
        String blockName = BuiltInRegistries.BLOCK.getKey(block.get()).getPath();
        getVariantBuilder(block.get())
                .forAllStates(Objects.requireNonNullElseGet(configuration, () -> state -> ConfiguredModel.builder()
                        .modelFile(models().cubeAll(blockName, blockTexture(block.get())))
                        .build()));
        simpleBlockItem(block.get(), cubeAll(block.get()));
    }

    protected void blockWithVariantsWithCustomBlockModel(@NotNull DeferredBlock<Block> block, @Nullable BlockStateModelConfiguration configuration) {
        String blockName = BuiltInRegistries.BLOCK.getKey(block.get()).getPath();
        BlockStateModelConfiguration blockConfiguration = Objects.requireNonNullElseGet(configuration,
                () -> (block1, state1)
                        -> ConfiguredModel.builder().modelFile(models().cubeAll(blockName, blockTexture(block.get()))).build());
        getVariantBuilder(block.get())
                .forAllStates(blockState -> blockConfiguration.config(block.get(), blockState));
    }

    private String getBlockName(DeferredBlock<Block> block) {
        return getBlockName(block.get());
    }

    private String getBlockName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    @Override
    protected void registerStatesAndModels() {
        BlockSet.of(ModBlocks.WOODEN_CRATE, ModBlocks.METAL_CRATE, ModBlocks.BUDDING_CERAMETRON, ModBlocks.CERAMETRON_CLUSTER_BLOCK)
                .each(this::simpleBlockWithItem);

        glassBlockWithItem(ModBlocks.PASSTHROUGH_GLASS, Constraints.RenderType.TRANSLUCENT);
        blockWithVariantsAndItem(ModBlocks.WATER_SOURCE, null);
        blockWithVariantsAndItem(ModBlocks.SKY_SHIFTER, null);

        BlockSet.of(ModBlocks.BASIC_BLOCK_ACCELERATOR, ModBlocks.ADVANCED_BLOCK_ACCELERATOR, ModBlocks.ELITE_BLOCK_ACCELERATOR, ModBlocks.ULTIMATE_BLOCK_ACCELERATOR)
                        .each(b -> blockWithVariantsWithCustomBlockModel(b, (block, state) -> {
                                    String blockName = BuiltInRegistries.BLOCK.getKey(block).getPath();
                                    Direction direction = state.getOptionalValue(BasicBlockAccelerator.FACING).orElse(Direction.NORTH);
                                    ModelFile blockModel = models()
                                            .withExistingParent(blockName, modLoc("block/block_accelerator"))
                                            .texture("side", modLoc("block/block_accelerator/" + blockName + "_side"));
                                    ConfiguredModel.Builder<?> configuredModels = ConfiguredModel.builder().modelFile(blockModel);
                                    return switch (direction){
                                        case EAST -> configuredModels.rotationY(90).build();
                                        case SOUTH -> configuredModels.rotationY(180).build();
                                        case WEST -> configuredModels.rotationY(270).build();
                                        case UP -> configuredModels.rotationX(270).build();
                                        case DOWN -> configuredModels.rotationX(90).build();
                                        default -> configuredModels.build();
                                    };
                                }))
                        .each(block -> {
                            String blockName = BuiltInRegistries.BLOCK.getKey(block.get()).getPath();
                            ModelFile blockModel = models()
                                    .withExistingParent(blockName, modLoc("block/block_accelerator"));
                            simpleBlockItem(block.get(), blockModel);
                        });

        BlockSet.of(ModBlocks.TICK_ACCELERATOR,
                ModBlocks.CORRUPTED_DIRT,
                ModBlocks.MOB_DAMAGE_PLATE,
                ModBlocks.ADVANCE_DAMAGE_PLATE,
                ModBlocks.BASIC_FLUID_TANK,
                ModBlocks.ADVANCED_FLUID_TANK,
                ModBlocks.ELITE_FLUID_TANK,
                ModBlocks.ULTIMATE_FLUID_TANK)
        .each(this::blockWithCustomBlockAndItemModel);


        BlockSet.of(ModBlocks.SMALL_CERAMETRON_BUD, ModBlocks.MEDIUM_CERAMETRON_BUD, ModBlocks.LARGE_CERAMETRON_BUD, ModBlocks.CERAMETRON_CLUSTER)
                .each(b -> {
                    blockWithVariantsWithCustomBlockModel(b, (block, state) -> {
                        Direction facing = state.getValue(AmethystClusterBlock.FACING);
                        ConfiguredModel.Builder<?> model = ConfiguredModel.builder()
                                .modelFile(
                                        models().singleTexture(
                                                getBlockName(block),
                                                ResourceLocation.withDefaultNamespace("block/cross"),
                                                "cross",
                                                blockTexture(block)
                                        ).renderType(Constraints.RenderType.CUTOUT.getName())
                                );
                        return switch (facing) {
                            case DOWN -> model.rotationX(180).build();
                            case NORTH -> model.rotationX(90).build();
                            case SOUTH -> model.rotationX(90).rotationY(180).build();
                            case WEST -> model.rotationX(90).rotationY(270).build();
                            case EAST -> model.rotationX(90).rotationY(90).build();
                            default -> model.build();
                        };
                    });
                });
        simpleBlockItem(ModBlocks.CERAMETRON_CLUSTER.get(), new ModelFile.UncheckedModelFile(
                ResourceLocation.fromNamespaceAndPath(JankyStuff.MOD_ID, "block/cerametron_cluster")
        ));
    }
}
