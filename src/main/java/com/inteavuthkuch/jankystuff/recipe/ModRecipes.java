package com.inteavuthkuch.jankystuff.recipe;

import com.inteavuthkuch.jankystuff.JankyStuff;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, JankyStuff.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, JankyStuff.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FluidTankUpgradeRecipe>> FLUID_TANK_UPGRADE_SERIALIZER =
            SERIALIZERS.register("fluid_tank_upgrade", FluidTankUpgradeRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<FluidTankUpgradeRecipe>> FLUID_TANK_UPGRADE_TYPE =
            TYPES.register("fluid_tank_upgrade", () -> new RecipeType<FluidTankUpgradeRecipe>() {
                @Override
                public String toString() {
                    return "fluid_tank_upgrade";
                }
            });

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
        TYPES.register(bus);
    }
}
