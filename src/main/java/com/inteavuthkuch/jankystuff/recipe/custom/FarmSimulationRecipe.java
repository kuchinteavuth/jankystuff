package com.inteavuthkuch.jankystuff.recipe.custom;

import com.inteavuthkuch.jankystuff.common.ModCodecs;
import com.inteavuthkuch.jankystuff.recipe.ModRecipes;
import com.inteavuthkuch.jankystuff.util.ChanceItemStack;
import com.inteavuthkuch.jankystuff.util.ChanceItemStackList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record FarmSimulationRecipe(Ingredient input, Ingredient catalyst, int duration, NonNullList<ChanceItemStack> results)
        implements Recipe<FarmSimulationRecipeInput> {

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(input);
        return list;
    }

    @Override
    public boolean matches(FarmSimulationRecipeInput recipeInput, Level level) {
        if(level.isClientSide())
            return false;

        if(!recipeInput.isEmpty()){
            return input.test(recipeInput.input()) && catalyst.test(recipeInput.catalyst());
        }

        return false;
    }

    @Override
    public ItemStack assemble(FarmSimulationRecipeInput recipeInput, HolderLookup.Provider provider) {
        return results.get(0).itemStack();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        var chanceItemList = new ChanceItemStackList(results());
        var itemResults = chanceItemList.getGuaranteedItems();
        if(itemResults != null)
            return itemResults.getFirst().itemStack();
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FARM_SIMULATION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FARM_SIMULATION_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<FarmSimulationRecipe> {
        // I have
        public static final MapCodec<FarmSimulationRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(FarmSimulationRecipe::input),
                Ingredient.CODEC_NONEMPTY.fieldOf("catalyst").forGetter(FarmSimulationRecipe::catalyst),
                Codec.INT.fieldOf("duration").forGetter(FarmSimulationRecipe::duration),
                NonNullList.codecOf(ChanceItemStack.CODEC).fieldOf("results").forGetter(FarmSimulationRecipe::results))
                .apply(inst, FarmSimulationRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, FarmSimulationRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, FarmSimulationRecipe::input,
                Ingredient.CONTENTS_STREAM_CODEC, FarmSimulationRecipe::catalyst,
                ByteBufCodecs.INT, FarmSimulationRecipe::duration,
                ModCodecs.CHANCE_ITEM_STACK_STREAM_CODEC, FarmSimulationRecipe::results,
                FarmSimulationRecipe::new
        );

        @Override
        public MapCodec<FarmSimulationRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FarmSimulationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
