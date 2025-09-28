package com.inteavuthkuch.jankystuff.recipe.custom;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record FarmSimulationRecipeInput(ItemStack input, ItemStack catalyst) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        return switch (i) {
            case 0 -> input();
            case 1 -> catalyst();
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + i);
        };
    }

    @Override
    public int size() {
        return 2;
    }

    public boolean isEmpty() {
        return input().isEmpty() && catalyst().isEmpty();
    }
}
