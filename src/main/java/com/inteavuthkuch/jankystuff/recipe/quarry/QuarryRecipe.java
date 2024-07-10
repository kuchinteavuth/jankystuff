package com.inteavuthkuch.jankystuff.recipe.quarry;

import com.inteavuthkuch.jankystuff.recipe.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record QuarryRecipe(Ingredient input, int tier, ItemStack output) implements Recipe<SingleRecipeInput> {

    @Override
    public boolean matches(SingleRecipeInput rInput, Level level) {
        if(level.isClientSide())
            return false;

        return input.test(rInput.getItem(0));
    }

    @Override
    public ItemStack assemble(SingleRecipeInput rInput, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.Serializer.QUARRY.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.Type.QUARRY.get();
    }
}
