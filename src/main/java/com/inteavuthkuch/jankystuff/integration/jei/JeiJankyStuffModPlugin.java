package com.inteavuthkuch.jankystuff.integration.jei;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.recipe.FluidTankUpgradeRecipe;
import com.inteavuthkuch.jankystuff.recipe.ModRecipes;
import com.inteavuthkuch.jankystuff.recipe.custom.FarmSimulationRecipe;
import com.inteavuthkuch.jankystuff.screen.custom.FarmSimulationScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class JeiJankyStuffModPlugin implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(JankyStuff.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addExtension(FluidTankUpgradeRecipe.class,
                new FluidTankUpgradeRecipeCraftingExtension());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FarmSimulationRecipeCategory(registration));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<FarmSimulationRecipe> farmSimulationRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.FARM_SIMULATION_TYPE.get())
                .stream().map(RecipeHolder::value).toList();

        registration.addRecipes(FarmSimulationRecipeCategory.RECIPE_TYPE, farmSimulationRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(FarmSimulationScreen.class, 25,35,33,17,
                FarmSimulationRecipeCategory.RECIPE_TYPE);
    }
}
