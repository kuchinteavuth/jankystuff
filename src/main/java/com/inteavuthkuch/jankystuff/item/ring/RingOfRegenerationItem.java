package com.inteavuthkuch.jankystuff.item.ring;

import com.inteavuthkuch.jankystuff.integration.CuriosIntegration;
import com.inteavuthkuch.jankystuff.integration.ExternalMod;
import com.inteavuthkuch.jankystuff.util.ComponentUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class RingOfRegenerationItem extends Item{
    public RingOfRegenerationItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(ComponentUtil.translateItem("ring_of_regeneration.description").withStyle(ChatFormatting.GRAY));

        if(!ExternalMod.CURIOS.isLoaded()){
            pTooltipComponents.add(ComponentUtil.translateItem("need_curios.description").withStyle(ChatFormatting.DARK_RED));
        }
    }
}
