package com.inteavuthkuch.jankystuff.recipe.builder;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.recipe.custom.FarmSimulationRecipe;
import com.inteavuthkuch.jankystuff.util.ChanceItemStack;
import com.inteavuthkuch.jankystuff.util.ChanceItemStackList;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class FarmSimulationRecipeBuilder {

    private final ChanceItemStackList results;
    private final Map<String, Criterion<?>> criteria;

    private Ingredient input;
    private Ingredient catalyst;
    private int duration = 200;

    public FarmSimulationRecipeBuilder(NonNullList<ChanceItemStack> results){
        this.results = new ChanceItemStackList(results);
        this.criteria = new LinkedHashMap<>();
    }

    public FarmSimulationRecipeBuilder input(Ingredient input) {
        this.input = input;
        return this;
    }

    public FarmSimulationRecipeBuilder input(ItemLike input) {
        this.input = Ingredient.of(input);
        return this;
    }

    public FarmSimulationRecipeBuilder catalyst(ItemLike catalyst) {
        this.catalyst = Ingredient.of(catalyst);
        return this;
    }

    public FarmSimulationRecipeBuilder catalyst(TagKey<Item> itemTag) {
        this.catalyst = Ingredient.of(itemTag);
        return this;
    }

    public FarmSimulationRecipeBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public FarmSimulationRecipeBuilder unlockBy(String pName, Criterion<?> pCriterion) {
        this.criteria.put(pName, pCriterion);
        return this;
    }

    public void save(RecipeOutput pRecipeOutput) {
        save(pRecipeOutput, getItemNameForMod(input.getItems()[0].getItem(), "farm_simulation"));
    }

    public void save(RecipeOutput pRecipeOutput, String pRecipeId) {
        save(pRecipeOutput, ResourceLocation.parse(pRecipeId));
    }

    public void save(RecipeOutput pRecipeOutput, ResourceLocation pRecipeId) {
        AdvancementHolder advancementHolder = null;

        if(!criteria.isEmpty()){
            Advancement.Builder builder = pRecipeOutput.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
                    .rewards(AdvancementRewards.Builder.recipe(pRecipeId))
                    .requirements(AdvancementRequirements.Strategy.OR);

            this.criteria.forEach(builder::addCriterion);
            advancementHolder = builder.build(pRecipeId.withPrefix("recipes/"));
        }

        FarmSimulationRecipe recipe = new FarmSimulationRecipe(input, catalyst, duration, results.itemStacks());
        pRecipeOutput.accept(pRecipeId, recipe, advancementHolder);
    }

    public static FarmSimulationRecipeBuilder create(NonNullList<ChanceItemStack> results) {
        return new FarmSimulationRecipeBuilder(results);
    }

    public static FarmSimulationRecipeBuilder create(Set<ChanceItemStack> results) {
        NonNullList<ChanceItemStack> itemStacks = NonNullList.create();
        itemStacks.addAll(results);
        return create(itemStacks);
    }

    public static FarmSimulationRecipeBuilder create(ChanceItemStack... chanceItemStacks) {
        NonNullList<ChanceItemStack> itemStacks = NonNullList.create();
        Collections.addAll(itemStacks, chanceItemStacks);
        return create(itemStacks);
    }

    public static FarmSimulationRecipeBuilder createSingleOutput(ItemLike item) {
        return createSingleOutput(item, 1);
    }

    public static FarmSimulationRecipeBuilder createSingleOutput(ItemLike item, int count) {
        NonNullList<ChanceItemStack> itemStacks = NonNullList.create();
        itemStacks.add(ChanceItemStack.of(item, count));
        return new FarmSimulationRecipeBuilder(itemStacks);
    }

    static ResourceLocation getId(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    static String getItemNameForMod(ItemLike item, String folder) {
        return String.format("%s:%s/%s", JankyStuff.MOD_ID, folder, getId(item).getPath());
    }
}
