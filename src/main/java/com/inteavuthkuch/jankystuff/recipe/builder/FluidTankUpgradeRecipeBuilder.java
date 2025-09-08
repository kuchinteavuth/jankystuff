package com.inteavuthkuch.jankystuff.recipe.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inteavuthkuch.jankystuff.recipe.FluidTankUpgradeRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FluidTankUpgradeRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final ItemStack resultStack;
    private final List<String> rows;
    private final Map<Character, Ingredient> key;
    private final Map<String, Criterion<?>> criteria;
    @javax.annotation.Nullable
    private String group;
    private boolean showNotification;

    public FluidTankUpgradeRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
        this(category, new ItemStack(result, count));
    }

    public FluidTankUpgradeRecipeBuilder(RecipeCategory category, ItemStack result) {
        this.rows = Lists.newArrayList();
        this.key = Maps.newLinkedHashMap();
        this.criteria = new LinkedHashMap<>();
        this.showNotification = true;
        this.category = category;
        this.result = result.getItem();
        this.resultStack = result;
    }


    public static FluidTankUpgradeRecipeBuilder shaped(RecipeCategory pCategory, ItemLike pResult) {
        return shaped(pCategory, pResult, 1);
    }

    public static FluidTankUpgradeRecipeBuilder shaped(RecipeCategory pCategory, ItemLike pResult, int pCount) {
        return new FluidTankUpgradeRecipeBuilder(pCategory, pResult, pCount);
    }

    public static FluidTankUpgradeRecipeBuilder shaped(RecipeCategory p_251325_, ItemStack result) {
        return new FluidTankUpgradeRecipeBuilder(p_251325_, result);
    }

    public FluidTankUpgradeRecipeBuilder define(Character pSymbol, TagKey<Item> pTag) {
        return this.define(pSymbol, Ingredient.of(pTag));
    }

    public FluidTankUpgradeRecipeBuilder define(Character pSymbol, ItemLike pItem) {
        return this.define(pSymbol, Ingredient.of(new ItemLike[]{pItem}));
    }

    public FluidTankUpgradeRecipeBuilder define(Character pSymbol, Ingredient pIngredient) {
        if (this.key.containsKey(pSymbol)) {
            throw new IllegalArgumentException("Symbol '" + pSymbol + "' is already defined!");
        } else if (pSymbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(pSymbol, pIngredient);
            return this;
        }
    }

    public FluidTankUpgradeRecipeBuilder pattern(String pPattern) {
        if (!this.rows.isEmpty() && pPattern.length() != ((String)this.rows.getFirst()).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(pPattern);
            return this;
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    public @NotNull RecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        this.criteria.put(pName, pCriterion);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    public FluidTankUpgradeRecipeBuilder showNotification(boolean pShowNotification) {
        this.showNotification = pShowNotification;
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return this.result;
    }

    @ParametersAreNonnullByDefault
    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceLocation pId) {
        ShapedRecipePattern shapedrecipepattern = this.ensureValid(pId);
        Advancement.Builder advancementBuilder = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);

        Objects.requireNonNull(advancementBuilder);
        this.criteria.forEach(advancementBuilder::addCriterion);
        FluidTankUpgradeRecipe recipe = new FluidTankUpgradeRecipe((String)Objects.requireNonNullElse(this.group, ""), RecipeBuilder.determineBookCategory(this.category), shapedrecipepattern, this.resultStack, this.showNotification);
        pRecipeOutput.accept(pId, recipe, advancementBuilder.build(pId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private ShapedRecipePattern ensureValid(ResourceLocation pId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(pId));
        } else {
            return ShapedRecipePattern.of(this.key, this.rows);
        }
    }
}
