package com.inteavuthkuch.jankystuff.recipe.quarry;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class QuarryRecipeSerializer implements RecipeSerializer<QuarryRecipe> {

    @Override
    public MapCodec<QuarryRecipe> codec() {
        return null;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, QuarryRecipe> streamCodec() {
        return null;
    }

}
