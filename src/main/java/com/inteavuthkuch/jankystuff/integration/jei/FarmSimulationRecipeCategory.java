package com.inteavuthkuch.jankystuff.integration.jei;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.item.ModItems;
import com.inteavuthkuch.jankystuff.recipe.custom.FarmSimulationRecipe;
import com.inteavuthkuch.jankystuff.util.ChanceItemStack;
import com.inteavuthkuch.jankystuff.util.ChanceItemStackList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public class FarmSimulationRecipeCategory implements IRecipeCategory<FarmSimulationRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(JankyStuff.MOD_ID, "farm_simulation");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(JankyStuff.MOD_ID,
            "textures/gui/integration/farm_simulation_for_jei.png");
    public static final RecipeType<FarmSimulationRecipe> RECIPE_TYPE = RecipeType.create(JankyStuff.MOD_ID, "farm_simulation", FarmSimulationRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public FarmSimulationRecipeCategory(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        //this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 131,56);
        this.background = guiHelper.drawableBuilder(TEXTURE, 0,0, 131, 56).setTextureSize(131, 56).build();
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.CERAMETRON_SHARD.get()));
    }

    @Override
    public RecipeType<FarmSimulationRecipe> getRecipeType() {
        return FarmSimulationRecipeCategory.RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.jankystuff.farm_simulation");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @SuppressWarnings("removal")
    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FarmSimulationRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 2, 2)
                .addIngredients(recipe.input())
                .addRichTooltipCallback((v, b) -> {
                    b.add(Component.translatable("jei.jankystuff.duration", recipe.duration()));
                })
                ;
        builder.addSlot(RecipeIngredientRole.INPUT, 2, 38).addIngredients(recipe.catalyst());


        int columns = 6;
        int rows = 3;
        final int slotSize = 18;
        final int maxRenderSlot = rows * columns;
        final int startX = 23;
        final int startY = 2;

        ChanceItemStackList result = new ChanceItemStackList(recipe.results());
        result.sort(Comparator.comparing(ChanceItemStack::chance).reversed());

        for(int i=0; i< Math.min(result.size(), maxRenderSlot); i++) {
            int row = i / columns;
            int col = i % columns;

            int x = startX + col * slotSize;
            int y = startY + row * slotSize;

            ChanceItemStack item = result.get(i);
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                    .addItemStack(item.itemStack())
                    .addRichTooltipCallback((v, b) -> {
                        if(item.chance() < 1.0f)
                        {
                            int percent = Math.round(item.chance() * 100);
                            b.add(Component.translatable("jei.jankystuff.chance", percent));
                        }
                    });
        }
    }
}
