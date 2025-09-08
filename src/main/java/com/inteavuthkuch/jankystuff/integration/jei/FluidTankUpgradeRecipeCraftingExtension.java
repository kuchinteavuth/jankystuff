package com.inteavuthkuch.jankystuff.integration.jei;

import com.inteavuthkuch.jankystuff.recipe.FluidTankUpgradeRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class FluidTankUpgradeRecipeCraftingExtension implements ICraftingCategoryExtension<FluidTankUpgradeRecipe> {

    @ParametersAreNonnullByDefault
    @Override
    public void setRecipe(RecipeHolder<FluidTankUpgradeRecipe> recipeHolder,
                          IRecipeLayoutBuilder builder,
                          ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        FluidTankUpgradeRecipe recipe = recipeHolder.value();
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        ShapedRecipePattern pattern = recipe.getPattern();

        // Handle Input Grid
        craftingGridHelper.createAndSetIngredients(builder, ingredients, pattern.width(), pattern.height());

        // Output Result
        ItemStack result = recipe.getResultItem(null);
        craftingGridHelper.createAndSetOutputs(builder, List.of(result))
                .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    if(!isBasicTank(result)){
                        tooltip.add(
                                Component.translatable("jei.jankystuff.fluid_tank_crafting_tooltip")
                                        .withStyle(ChatFormatting.BOLD)
                        );
                    }
                });
    }

    private boolean isBasicTank(@NotNull ItemStack itemStack) {
        ResourceLocation resource = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        return resource.toString().equals("jankystuff:basic_fluid_tank");
    }

    @Override
    public int getWidth(RecipeHolder<FluidTankUpgradeRecipe> recipeHolder) {
        return recipeHolder.value().getPattern().width();
    }

    @Override
    public int getHeight(RecipeHolder<FluidTankUpgradeRecipe> recipeHolder) {
        return recipeHolder.value().getPattern().height();
    }
}
