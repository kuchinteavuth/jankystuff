package com.inteavuthkuch.jankystuff.item.smithing;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ReinforceSmithingTemplateItem extends Item {
    public ReinforceSmithingTemplateItem() {
        super(new Properties());
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        pTooltipComponents.add(Component.translatable("item.jankystuff.reinforced_smithing_template.base_slot").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(CommonComponents.EMPTY);
        pTooltipComponents.add(Component.translatable("item.jankystuff.smithing_template.applies_to_text").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(CommonComponents.space().append(Component.translatable("item.jankystuff.reinforced_smithing_template.applies_to").withStyle(ChatFormatting.BLUE)));
        pTooltipComponents.add(Component.translatable("item.jankystuff.smithing_template.ingredients_text").append(":").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(CommonComponents.space().append(Component.translatable("item.jankystuff.reinforced_smithing_template.ingredients").withStyle(ChatFormatting.BLUE)));
    }
}
