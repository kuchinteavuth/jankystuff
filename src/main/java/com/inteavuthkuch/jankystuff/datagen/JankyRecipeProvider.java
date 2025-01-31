package com.inteavuthkuch.jankystuff.datagen;

import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.datagen.util.RecipeProviderExtension;
import com.inteavuthkuch.jankystuff.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.HTML;
import java.util.concurrent.CompletableFuture;

public class JankyRecipeProvider extends RecipeProviderExtension {

    public JankyRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {

        smokingRecipe(output, RecipeCategory.FOOD, ModItems.ROASTED_APPLE, Items.APPLE ,0.35f);
        smeltingRecipe(output, RecipeCategory.FOOD, ModItems.ROASTED_APPLE, Items.APPLE , 0.35f);
        campfireRecipe(output, RecipeCategory.FOOD, ModItems.ROASTED_APPLE, Items.APPLE);
        smeltingRecipe(output, RecipeCategory.MISC, ModItems.DRIED_FLESH, Items.ROTTEN_FLESH , 0.1f);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LEATHER)
                .pattern(" # ")
                .pattern("#S#")
                .pattern(" # ")
                .define('S', Items.STICK)
                .define('#', ModItems.DRIED_FLESH)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .unlockedBy(getHasName(ModItems.DRIED_FLESH), has(ModItems.DRIED_FLESH))
                .save(output, getItemNameForMod(Items.LEATHER));

        simpleShapelessWithUncrafting(output, ModItems.CHARCOAL_PIECE, 8, Items.CHARCOAL, 1);
        simpleShapelessWithUncrafting(output, ModItems.COAL_PIECE, 8, Items.COAL, 1);

        reinforcedSmithing(output, Items.IRON_SWORD, Items.IRON_INGOT, RecipeCategory.TOOLS, ModItems.REINFORCED_IRON_SWORD.get());
        reinforcedSmithing(output, Items.IRON_PICKAXE, Items.IRON_INGOT, RecipeCategory.TOOLS, ModItems.REINFORCED_IRON_PICKAXE.get());
        reinforcedSmithing(output, Items.IRON_SHOVEL, Items.IRON_INGOT, RecipeCategory.TOOLS, ModItems.REINFORCED_IRON_SHOVEL.get());
        reinforcedSmithing(output, Items.IRON_AXE, Items.IRON_INGOT, RecipeCategory.TOOLS, ModItems.REINFORCED_IRON_AXE.get());
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.REINFORCED_IRON_PAXEL)
                .pattern("ASP").pattern(" # ").pattern(" # ")
                .define('#', Tags.Items.RODS_WOODEN)
                .define('A', ModItems.REINFORCED_IRON_AXE)
                .define('S', ModItems.REINFORCED_IRON_SHOVEL)
                .define('P', ModItems.REINFORCED_IRON_PICKAXE)
                .unlockedBy(getHasName(Items.STICK), has(Tags.Items.RODS_WOODEN))
                .unlockedBy(getHasName(ModItems.REINFORCED_IRON_AXE), has(ModItems.REINFORCED_IRON_AXE))
                .unlockedBy(getHasName(ModItems.REINFORCED_IRON_SHOVEL), has(ModItems.REINFORCED_IRON_SHOVEL))
                .unlockedBy(getHasName(ModItems.REINFORCED_IRON_PICKAXE), has(ModItems.REINFORCED_IRON_PICKAXE))
                .save(output);

        reinforcedSmithing(output, Items.DIAMOND_SWORD, Items.DIAMOND, RecipeCategory.TOOLS, ModItems.REINFORCED_DIAMOND_SWORD.get());
        reinforcedSmithing(output, Items.DIAMOND_PICKAXE, Items.DIAMOND, RecipeCategory.TOOLS, ModItems.REINFORCED_DIAMOND_PICKAXE.get());
        reinforcedSmithing(output, Items.DIAMOND_SHOVEL, Items.DIAMOND, RecipeCategory.TOOLS, ModItems.REINFORCED_DIAMOND_SHOVEL.get());
        reinforcedSmithing(output, Items.DIAMOND_AXE, Items.DIAMOND, RecipeCategory.TOOLS, ModItems.REINFORCED_DIAMOND_AXE.get());
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.REINFORCED_DIAMOND_PAXEL)
                .pattern("ASP").pattern(" # ").pattern(" # ")
                .define('#', Tags.Items.RODS_WOODEN)
                .define('A', ModItems.REINFORCED_DIAMOND_AXE)
                .define('S', ModItems.REINFORCED_DIAMOND_SHOVEL)
                .define('P', ModItems.REINFORCED_DIAMOND_PICKAXE)
                .unlockedBy(getHasName(Items.STICK), has(Tags.Items.RODS_WOODEN))
                .unlockedBy(getHasName(ModItems.REINFORCED_DIAMOND_AXE), has(ModItems.REINFORCED_DIAMOND_AXE))
                .unlockedBy(getHasName(ModItems.REINFORCED_DIAMOND_SHOVEL), has(ModItems.REINFORCED_DIAMOND_SHOVEL))
                .unlockedBy(getHasName(ModItems.REINFORCED_DIAMOND_PICKAXE), has(ModItems.REINFORCED_DIAMOND_PICKAXE))
                .save(output);

        reinforcedSmithing(output, Items.NETHERITE_SWORD, Items.NETHERITE_INGOT, RecipeCategory.TOOLS, ModItems.REINFORCED_NETHERITE_SWORD.get());
        reinforcedSmithing(output, Items.NETHERITE_PICKAXE, Items.NETHERITE_INGOT, RecipeCategory.TOOLS, ModItems.REINFORCED_NETHERITE_PICKAXE.get());
        reinforcedSmithing(output, Items.NETHERITE_SHOVEL, Items.NETHERITE_INGOT, RecipeCategory.TOOLS, ModItems.REINFORCED_NETHERITE_SHOVEL.get());
        reinforcedSmithing(output, Items.NETHERITE_AXE, Items.NETHERITE_INGOT, RecipeCategory.TOOLS, ModItems.REINFORCED_NETHERITE_AXE.get());
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.REINFORCED_NETHERITE_PAXEL)
                .pattern("ASP").pattern(" # ").pattern(" # ")
                .define('#', Tags.Items.RODS_WOODEN)
                .define('A', ModItems.REINFORCED_NETHERITE_AXE)
                .define('S', ModItems.REINFORCED_NETHERITE_SHOVEL)
                .define('P', ModItems.REINFORCED_NETHERITE_PICKAXE)
                .unlockedBy(getHasName(Items.STICK), has(Tags.Items.RODS_WOODEN))
                .unlockedBy(getHasName(ModItems.REINFORCED_NETHERITE_AXE), has(ModItems.REINFORCED_NETHERITE_AXE))
                .unlockedBy(getHasName(ModItems.REINFORCED_NETHERITE_SHOVEL), has(ModItems.REINFORCED_NETHERITE_SHOVEL))
                .unlockedBy(getHasName(ModItems.REINFORCED_NETHERITE_PICKAXE), has(ModItems.REINFORCED_NETHERITE_PICKAXE))
                .save(output);

        infinitySmithing(output, ModItems.REINFORCED_NETHERITE_SWORD.get(), Items.NETHER_STAR, RecipeCategory.TOOLS, ModItems.INFINITY_SWORD.get());
        infinitySmithing(output, ModItems.REINFORCED_NETHERITE_PICKAXE.get(), Items.NETHER_STAR, RecipeCategory.TOOLS, ModItems.INFINITY_PICKAXE.get());
        infinitySmithing(output, ModItems.REINFORCED_NETHERITE_SHOVEL.get(), Items.NETHER_STAR, RecipeCategory.TOOLS, ModItems.INFINITY_SHOVEL.get());
        infinitySmithing(output, ModItems.REINFORCED_NETHERITE_AXE.get(), Items.NETHER_STAR, RecipeCategory.TOOLS, ModItems.INFINITY_AXE.get());
        infinitySmithing(output, ModItems.REINFORCED_NETHERITE_PAXEL.get(), Items.NETHER_STAR, RecipeCategory.TOOLS, ModItems.INFINITY_PAXEL.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.INFINITY_PAXEL)
                .pattern("ASP").pattern(" # ").pattern(" # ")
                .define('#', Tags.Items.RODS_WOODEN)
                .define('A', ModItems.INFINITY_AXE)
                .define('S', ModItems.INFINITY_SHOVEL)
                .define('P', ModItems.INFINITY_AXE)
                .unlockedBy(getHasName(Items.STICK), has(Tags.Items.RODS_WOODEN))
                .unlockedBy(getHasName(ModItems.INFINITY_AXE), has(ModItems.INFINITY_AXE))
                .unlockedBy(getHasName(ModItems.INFINITY_SHOVEL), has(ModItems.INFINITY_SHOVEL))
                .unlockedBy(getHasName(ModItems.INFINITY_AXE), has(ModItems.INFINITY_AXE))
                .save(output, getItemNameForMod(ModItems.INFINITY_PAXEL) + "_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.REINFORCED_COMPOUND, 4)
                .pattern("ICI")
                .pattern("C#C")
                .pattern("ICI")
                .define('C', Items.COPPER_INGOT)
                .define('I', Items.IRON_INGOT)
                .define('#', Items.CLAY_BALL)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                .save(output);

        // Reinforce Smithing Template
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.INFINITY_SMITHING_TEMPLATE, 2)
                .pattern("#S#")
                .pattern("#C#")
                .pattern("###")
                .define('S', Items.END_STONE)
                .define('#', Items.NETHER_STAR)
                .define('C', ModItems.INFINITY_SMITHING_TEMPLATE)
                .unlockedBy(getHasName(ModItems.INFINITY_SMITHING_TEMPLATE), has(ModItems.INFINITY_SMITHING_TEMPLATE))
                .save(output);

        // Reinforce Smithing Template
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.REINFORCED_SMITHING_TEMPLATE)
                .pattern(" # ")
                .pattern("#S#")
                .pattern(" # ")
                .define('#', ModItems.REINFORCED_COMPOUND)
                .define('S', Items.DEEPSLATE)
                .unlockedBy(getHasName(Items.DEEPSLATE), has(Items.DEEPSLATE))
                .unlockedBy(getHasName(ModItems.REINFORCED_COMPOUND), has(ModItems.REINFORCED_COMPOUND))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RING_OF_THE_SKY)
                .pattern("DND")
                .pattern("NEN")
                .pattern("DND")
                .define('D', Items.DIAMOND)
                .define('N', Items.NETHER_STAR)
                .define('E', Items.ELYTRA)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .unlockedBy(getHasName(Items.NETHER_STAR), has(Items.NETHER_STAR))
                .unlockedBy(getHasName(Items.ELYTRA), has(Items.ELYTRA))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TICK_ACCELERATOR)
                .pattern("BIB")
                .pattern("IDI")
                .pattern("BIB")
                .define('B', Items.BONE_BLOCK)
                .define('I', Items.IRON_INGOT)
                .define('D', Items.DIAMOND)
                .unlockedBy(getHasName(Items.BONE_BLOCK), has(Items.BONE_BLOCK))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.BONE_BLOCK))
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.WOODEN_CRATE)
                .pattern("SLS")
                .pattern("LCL")
                .pattern("SLS")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('L', ItemTags.LOGS)
                .define('C', Tags.Items.CHESTS_WOODEN)
                .unlockedBy(getHasName(Items.STICK), has(Tags.Items.RODS_WOODEN))
                .unlockedBy("had_log", has(ItemTags.LOGS))
                .unlockedBy("had_chest", has(Tags.Items.CHESTS_WOODEN))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.METAL_CRATE)
                .pattern("###")
                .pattern("#C#")
                .pattern("###")
                .define('#', ModItems.REINFORCED_COMPOUND)
                .define('C', ModBlocks.WOODEN_CRATE)
                .unlockedBy(getHasName(Items.STICK), has(Tags.Items.RODS_WOODEN))
                .unlockedBy(getHasName(ModItems.REINFORCED_COMPOUND), has(ModItems.REINFORCED_COMPOUND))
                .unlockedBy(getHasName(ModBlocks.WOODEN_CRATE), has(ModBlocks.WOODEN_CRATE))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PORTABLE_CRATE)
                .requires(ModBlocks.WOODEN_CRATE)
                .requires(Tags.Items.RODS_WOODEN)
                .unlockedBy(getHasName(ModBlocks.WOODEN_CRATE), has(ModBlocks.WOODEN_CRATE))
                .unlockedBy(getHasName(Items.STICK), has(Tags.Items.RODS_WOODEN))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.LAMP)
                .pattern("###")
                .pattern("#C#")
                .pattern("#G#")
                .define('#', ItemTags.LOGS)
                .define('C', Items.TORCH)
                .define('G', Tags.Items.GLASS_BLOCKS)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .unlockedBy(getHasName(Items.TORCH), has(Items.TORCH))
                .unlockedBy("has_glass", has(Tags.Items.GLASS_BLOCKS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BASIC_QUARRY)
                .pattern("CIC")
                .pattern("IDI")
                .pattern("CIC")
                .define('C', Tags.Items.STORAGE_BLOCKS_COAL)
                .define('I', ModItems.REINFORCED_COMPOUND)
                .define('D', Items.DIAMOND_BLOCK)
                .unlockedBy("has_coal_block", has(Tags.Items.STORAGE_BLOCKS_COAL))
                .unlockedBy(getHasName(ModItems.REINFORCED_COMPOUND), has(ModItems.REINFORCED_COMPOUND))
                .unlockedBy(getHasName(Items.DIAMOND_BLOCK), has(Items.DIAMOND_BLOCK))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MAGNET)
                .pattern("G G")
                .pattern("RER")
                .pattern("ILI")
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('E', Tags.Items.ENDER_PEARLS)
                .define('L', Tags.Items.GEMS_LAPIS)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .unlockedBy("has_ender_pearl", has(Tags.Items.ENDER_PEARLS))
                .unlockedBy("has_lapis", has(Tags.Items.GEMS_LAPIS))
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MAGNET)
                .pattern("G G")
                .pattern("RER")
                .pattern("ILI")
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('E', Blocks.DIAMOND_BLOCK)
                .define('L', Tags.Items.GEMS_LAPIS)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .unlockedBy("has_diamond_block", has(Blocks.DIAMOND_BLOCK))
                .unlockedBy("has_lapis", has(Tags.Items.GEMS_LAPIS))
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output, getItemNameForMod(ModItems.MAGNET) + "_alt");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPEED_UPGRADE)
                .pattern("#E#")
                .pattern("ELE")
                .pattern("#E#")
                .define('#', ModItems.REINFORCED_COMPOUND)
                .define('L', Tags.Items.STORAGE_BLOCKS_LAPIS)
                .define('E', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
                .unlockedBy(getHasName(ModItems.REINFORCED_COMPOUND), has(ModItems.REINFORCED_COMPOUND))
                .unlockedBy("has_lapis_brock", has(Tags.Items.STORAGE_BLOCKS_LAPIS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCE_SPEED_UPGRADE)
                .pattern("#E#")
                .pattern("ELE")
                .pattern("#E#")
                .define('#', ModItems.REINFORCED_COMPOUND)
                .define('L', ModItems.SPEED_UPGRADE)
                .define('E', Tags.Items.INGOTS_NETHERITE)
                .unlockedBy(getHasName(ModItems.SPEED_UPGRADE), has(ModItems.SPEED_UPGRADE))
                .unlockedBy(getHasName(ModItems.REINFORCED_COMPOUND), has(ModItems.REINFORCED_COMPOUND))
                .unlockedBy("has_netherite_ingot", has(Tags.Items.INGOTS_NETHERITE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PASSTHROUGH_GLASS, 8)
                .pattern("E##")
                .pattern("###")
                .pattern("###")
                .define('#', Blocks.TINTED_GLASS)
                .define('E', Items.ENDER_EYE)
                .unlockedBy(getHasName(Blocks.TINTED_GLASS), has(Blocks.TINTED_GLASS))
                .unlockedBy(getHasName(Items.ENDER_EYE), has(Items.ENDER_EYE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MOB_DAMAGE_PLATE, 4)
                .pattern("###")
                .pattern("SIS")
                .pattern("###")
                .define('#', Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE)
                .define('S', Tags.Items.SLIMEBALLS)
                .define('I', Items.IRON_SWORD)
                .unlockedBy(getHasName(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE), has(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE))
                .unlockedBy(getHasName(Items.IRON_SWORD), has(Items.IRON_SWORD))
                .unlockedBy("has_slime_ball", has(Tags.Items.SLIMEBALLS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ADVANCE_DAMAGE_PLATE, 4)
                .pattern("P#P")
                .pattern("#D#")
                .pattern("P#P")
                .define('P', Tags.Items.ENDER_PEARLS)
                .define('#', ModBlocks.MOB_DAMAGE_PLATE)
                .define('D', Items.DIAMOND_SWORD)
                .unlockedBy(getHasName(ModBlocks.MOB_DAMAGE_PLATE), has(ModBlocks.MOB_DAMAGE_PLATE))
                .unlockedBy(getHasName(Items.DIAMOND_SWORD), has(Items.DIAMOND_SWORD))
                .unlockedBy("has_ender_pearl", has(Tags.Items.ENDER_PEARLS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BLOCK_BREAKER)
                .pattern("###")
                .pattern("POP")
                .pattern("###")
                .define('#', ModItems.REINFORCED_COMPOUND)
                .define('P', Items.IRON_PICKAXE)
                .define('O', Blocks.OBSERVER)
                .unlockedBy(getHasName(ModItems.REINFORCED_COMPOUND), has(ModItems.REINFORCED_COMPOUND))
                .unlockedBy(getHasName(Items.IRON_PICKAXE), has(Items.IRON_PICKAXE))
                .unlockedBy(getHasName(Blocks.OBSERVER), has(Blocks.OBSERVER))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CORRUPTED_DIRT, 8)
                .pattern("DSD")
                .pattern("SPS")
                .pattern("DSD")
                .define('D', Blocks.DIRT)
                .define('S', Blocks.SOUL_SAND)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy(getHasName(Blocks.DIRT), has(Blocks.DIRT))
                .unlockedBy(getHasName(Blocks.SOUL_SAND), has(Blocks.SOUL_SAND))
                .unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.THE_VOID)
                .pattern("CRC")
                .pattern("LBL")
                .pattern("CRC")
                .define('C', Tags.Items.CHESTS_WOODEN)
                .define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .define('L', Items.LAVA_BUCKET)
                .define('B', Items.BUCKET)
                .unlockedBy(getHasName(Items.BUCKET), has(Items.BUCKET))
                .unlockedBy(getHasName(Items.LAVA_BUCKET), has(Items.LAVA_BUCKET))
                .unlockedBy("has_chest", has(Tags.Items.CHESTS_WOODEN))
                .unlockedBy("has_redstone_block", has(Tags.Items.STORAGE_BLOCKS_REDSTONE))
                .save(output);
    }
}
