package com.inteavuthkuch.jankystuff.datagen.util;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public abstract class RecipeProviderExtension extends RecipeProvider implements IConditionBuilder {
    public RecipeProviderExtension(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    protected String getItemNameForMod(ItemLike item){
        return String.format("%s:%s", JankyStuff.MODID, getItemName(item));
    }

    protected void reinforcedSmithing(RecipeOutput output, Item base, Item addition, RecipeCategory category, Item result) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ModItems.REINFORCED_SMITHING_TEMPLATE),
                        Ingredient.of(base),
                        Ingredient.of(addition),
                        category,
                        result
                )
                .unlocks(getHasName(ModItems.REINFORCED_SMITHING_TEMPLATE), has(ModItems.REINFORCED_SMITHING_TEMPLATE))
                .save(output, getItemNameForMod(result) + "_smithing");
    }

    protected void infinitySmithing(RecipeOutput output, Item base, Item addition, RecipeCategory category, Item result) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ModItems.INFINITY_SMITHING_TEMPLATE),
                        Ingredient.of(base),
                        Ingredient.of(addition),
                        category,
                        result
                )
                .unlocks(getHasName(ModItems.INFINITY_SMITHING_TEMPLATE), has(ModItems.INFINITY_SMITHING_TEMPLATE))
                .save(output, getItemNameForMod(result) + "_smithing");
    }

    protected void smokingRecipe(RecipeOutput recipeOutput, RecipeCategory category, ItemLike result, ItemLike input) {
        SimpleCookingRecipeBuilder.smoking(
                        Ingredient.of(input.asItem()),
                        category,
                        result,
                        0.1f,
                        100
                )
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, getItemNameForMod(result) + "_from_smoking_" + getItemName(input));
    }

    protected void smokingRecipe(RecipeOutput recipeOutput, RecipeCategory category, ItemLike result, ItemLike input, float experience) {
        SimpleCookingRecipeBuilder.smoking(
                        Ingredient.of(input.asItem()),
                        category,
                        result,
                        experience,
                        100
                )
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, getItemNameForMod(result) + "_from_smoking_" + getItemName(input));
    }

    protected void smeltingRecipe(RecipeOutput recipeOutput, RecipeCategory category, ItemLike result, ItemLike input) {
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(input.asItem()),
                        category,
                        result,
                        0.1f,
                        200
                )
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, getItemNameForMod(result) + "_from_smelting_" + getItemName(input));
    }

    protected void smeltingRecipe(RecipeOutput recipeOutput, RecipeCategory category, ItemLike result, ItemLike input, float experience) {
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(input.asItem()),
                        category,
                        result,
                        experience,
                        200
                )
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, getItemNameForMod(result) + "_from_smelting_" + getItemName(input));
    }

    protected void simpleBlasting(RecipeOutput recipeOutput, RecipeCategory category, ItemLike result, ItemLike input) {
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(input.asItem()),
                        category,
                        result,
                        1.0f,
                        100
                )
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, getItemNameForMod(result) + "_from_blasting_" + getItemName(input));
    }

    protected void simpleBlasting(RecipeOutput recipeOutput, RecipeCategory category, ItemLike result, ItemLike input, float experience) {
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(input.asItem()),
                        category,
                        result,
                        experience,
                        100
                )
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, getItemNameForMod(result) + "_from_blasting_" + getItemName(input));
    }

    protected void campfireRecipe(RecipeOutput recipeOutput, RecipeCategory category, ItemLike result, ItemLike input) {
        SimpleCookingRecipeBuilder.campfireCooking(
                        Ingredient.of(input.asItem()),
                        category,
                        result,
                        0.35f,
                        600
                )
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, getItemNameForMod(result) + "_from_campfire_cooking");
    }

    protected void simpleShapelessWithUncrafting(RecipeOutput recipeOutput, ItemLike result, int resultCount, ItemLike input, int inputCount) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, resultCount)
                .requires(input, inputCount)
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, getItemNameForMod(result) + "_from_" + getItemName(input));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, input, inputCount)
                .requires(result, resultCount)
                .unlockedBy(getHasName(result), has(result))
                .save(recipeOutput, getItemNameForMod(result) + "_to_" + getItemName(input));
    }

    protected void surroundingCraft(RecipeOutput recipeOutput, ItemLike ingredient, ItemLike center, ItemLike result, int resultCount) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, resultCount)
                .pattern("###")
                .pattern("#C#")
                .pattern("###")
                .define('#', ingredient)
                .define('C', center)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .unlockedBy(getHasName(center), has(center))
                .save(recipeOutput);
    }
}
