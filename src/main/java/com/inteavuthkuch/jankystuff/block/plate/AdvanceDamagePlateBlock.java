package com.inteavuthkuch.jankystuff.block.plate;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class AdvanceDamagePlateBlock extends AbstractPlateBaseBlock{

    public AdvanceDamagePlateBlock() {
        super(true, AdvanceDamagePlateBlock.DEFAULT_PROFILE);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTootipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTootipComponents, pTooltipFlag);
        pTootipComponents.add(Component.translatable("block.jankystuff.advance_damage_plate.description").withStyle(ChatFormatting.GRAY));
    }
}
