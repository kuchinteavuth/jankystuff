package com.inteavuthkuch.jankystuff.item.food;

import com.inteavuthkuch.jankystuff.util.Conversion;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EnergyDrink extends Item {

    public static final FoodProperties FOOD_PROPERTIES =
            new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationModifier(1.0f)
                    .alwaysEdible()
                    .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, Conversion.tickFromMinute(1), 0), 1.0F)
                    .build();

    public EnergyDrink() {
        super(
                new Properties()
                        .food(FOOD_PROPERTIES)
                        .rarity(Rarity.COMMON)
        );
    }

    @Override
    public @NotNull SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @ParametersAreNonnullByDefault
    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        FoodProperties foodProperties = getFoodProperties(pStack, null);
        if(foodProperties != null) {
            List<FoodProperties.PossibleEffect> effects = new ArrayList<>(foodProperties.effects()); // Fix error while fetching food effects, because effects is ImmutableList
            effects.sort(Comparator.comparing(FoodProperties.PossibleEffect::probability).reversed());
            if (!effects.isEmpty()) {
                for (FoodProperties.PossibleEffect pair : effects) {
                    MobEffectInstance effect = pair.effect();
                    MutableComponent effectName = Component.translatable(effect.getDescriptionId());

                    if (effect.getAmplifier() > 0) {
                        effectName = Component.translatable("potion.withAmplifier", effectName,
                                Component.translatable("potion.potency." + effect.getAmplifier()));

                    }
                    MutableComponent effectNameAndDuration = Component.translatable("potion.withDuration",
                            effectName,
                            Component.literal(Conversion.formatDuration(effect.getDuration())));

                    MutableComponent effectTooltip = effectNameAndDuration.withStyle(ChatFormatting.BLUE);

                    // Add chance if it's less than 100%
                    float effectChance = pair.probability();
                    if (effectChance < 1.0f) {
                        int percent = Math.round(effectChance * 100);

                        ChatFormatting chanceColor = effectChance > 0.75f ? ChatFormatting.GREEN :
                                effectChance > 0.5f ? ChatFormatting.YELLOW :
                                        ChatFormatting.RED;

                        effectTooltip.append(Component.literal(" (" + percent + "%)")
                                .withStyle(chanceColor));
                    }

                    pTooltipComponents.add(effectTooltip);
                }
            }
        }
    }
}
