package com.inteavuthkuch.jankystuff.recipe;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mezz.jei.api.constants.RecipeTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Set;

public class FluidTankUpgradeRecipe implements CraftingRecipe {

    private final String group;
    private final CraftingBookCategory category;
    private final ShapedRecipePattern pattern;
    private final ItemStack result;
    private final boolean showNotification;

    public FluidTankUpgradeRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification){
        this.group = group;
        this.category = category;
        this.pattern = pattern;
        this.result = result;
        this.showNotification = showNotification;
    }

    public FluidTankUpgradeRecipe(String pGroup, CraftingBookCategory pCategory, ShapedRecipePattern pPattern, ItemStack pResult){
        this(pGroup, pCategory, pPattern, pResult, true);
    }

    @ParametersAreNonnullByDefault
    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        return this.pattern.matches(craftingInput);
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput craftingInput, HolderLookup.@NotNull Provider provider) {
        ItemStack resultStack =  this.getResultItem(provider).copy();

        for(int i=0; i< craftingInput.items().size(); i++) {
            ItemStack input = craftingInput.getItem(i);
            if(isFluidTank(input) && isTierUp(input, resultStack)){
                CustomData data = input.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                if(!data.isEmpty()) {
                    resultStack.set(DataComponents.CUSTOM_DATA, CustomData.of(data.copyTag()));
                }
                break;
            }
        }

        return resultStack;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return pattern.ingredients();
    }

    @NotNull
    @Override
    public ItemStack getResultItem(@Nullable HolderLookup.Provider provider) {
        return this.result;
    }
    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth >= this.pattern.width() && pHeight >= this.pattern.height();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.FLUID_TANK_UPGRADE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        //return ModRecipes.FLUID_TANK_UPGRADE_TYPE.get();
        return RecipeType.CRAFTING;
    }

    @Override
    public CraftingBookCategory category() {
        return this.category;
    }

    public ShapedRecipePattern getPattern() {
        return this.pattern;
    }

    private static final Set<String> FLUID_TANKS = Set.of(
            "jankystuff:basic_fluid_tank",
            "jankystuff:advanced_fluid_tank",
            "jankystuff:elite_fluid_tank",
            "jankystuff:ultimate_fluid_tank"
    );

    private static final Map<String, String> TANK_MAP = Map.of(
            "jankystuff:basic_fluid_tank", "jankystuff:advanced_fluid_tank",
            "jankystuff:advanced_fluid_tank", "jankystuff:elite_fluid_tank",
            "jankystuff:elite_fluid_tank", "jankystuff:ultimate_fluid_tank"
    );


    private static boolean isFluidTank(@NotNull ItemStack itemStack) {
        ResourceLocation itemKey = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        return FLUID_TANKS.contains(itemKey.toString());
    }
    private static boolean isTierUp(@NotNull ItemStack input, @NotNull ItemStack result) {
        ResourceLocation itemKey = BuiltInRegistries.ITEM.getKey(input.getItem());
        ResourceLocation resultKey = BuiltInRegistries.ITEM.getKey(result.getItem());

        String tank = TANK_MAP.get(itemKey.toString());
        return resultKey.toString().equals(tank);
    }

    public static class Serializer implements RecipeSerializer<FluidTankUpgradeRecipe> {
        public static final MapCodec<FluidTankUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(
                (instance) -> instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter((x) -> x.group),
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter((x) -> x.category),
                        ShapedRecipePattern.MAP_CODEC.forGetter((x) -> x.pattern),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter((x) -> x.result),
                        Codec.BOOL.optionalFieldOf("show_notification", true).forGetter((x) -> x.showNotification))
                        .apply(instance, FluidTankUpgradeRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, FluidTankUpgradeRecipe> STREAM_CODEC = StreamCodec.of(FluidTankUpgradeRecipe.Serializer::toNetwork, FluidTankUpgradeRecipe.Serializer::fromNetwork);

        @Override
        public @NotNull MapCodec<FluidTankUpgradeRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, FluidTankUpgradeRecipe> streamCodec() {
            return STREAM_CODEC;
        }


        private static FluidTankUpgradeRecipe fromNetwork(RegistryFriendlyByteBuf byteBuf) {
            String s = byteBuf.readUtf();
            CraftingBookCategory craftingbookcategory = (CraftingBookCategory)byteBuf.readEnum(CraftingBookCategory.class);
            ShapedRecipePattern shapedrecipepattern = (ShapedRecipePattern)ShapedRecipePattern.STREAM_CODEC.decode(byteBuf);
            ItemStack itemstack = (ItemStack)ItemStack.STREAM_CODEC.decode(byteBuf);
            boolean flag = byteBuf.readBoolean();
            return new FluidTankUpgradeRecipe(s, craftingbookcategory, shapedrecipepattern, itemstack, flag);
        }

        private static void toNetwork(RegistryFriendlyByteBuf byteBuf, FluidTankUpgradeRecipe recipe) {
            byteBuf.writeUtf(recipe.group);
            byteBuf.writeEnum(recipe.category);
            ShapedRecipePattern.STREAM_CODEC.encode(byteBuf, recipe.pattern);
            ItemStack.STREAM_CODEC.encode(byteBuf, recipe.result);
            byteBuf.writeBoolean(recipe.showNotification);
        }
    }
}
