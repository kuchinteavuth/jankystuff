package com.inteavuthkuch.jankystuff.recipe;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.recipe.quarry.QuarryRecipe;
import com.inteavuthkuch.jankystuff.recipe.quarry.QuarryRecipeSerializer;
import com.inteavuthkuch.jankystuff.recipe.quarry.QuarryRecipeType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static class Type {
        private static DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, JankyStuff.MODID);
        public static DeferredHolder<RecipeType<?>, RecipeType<QuarryRecipe>> QUARRY = TYPES.register("quarry", QuarryRecipeType::new);
    }


    public static class Serializer {
        private static DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, JankyStuff.MODID);
        public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<QuarryRecipe>> QUARRY = SERIALIZERS.register("quarry", QuarryRecipeSerializer::new);
    }


    /**
     * Register both custom {@link RecipeType} type and {@link RecipeSerializer}
     * @param eventBus NeoForge event bus
     */
    public static void register(IEventBus eventBus) {
        Type.TYPES.register(eventBus);
        Serializer.SERIALIZERS.register(eventBus);
    }
}
