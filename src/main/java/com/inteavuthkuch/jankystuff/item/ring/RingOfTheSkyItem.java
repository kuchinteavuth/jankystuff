package com.inteavuthkuch.jankystuff.item.ring;

import com.inteavuthkuch.jankystuff.integration.CuriosIntegration;
import com.inteavuthkuch.jankystuff.integration.ExternalMod;
import com.inteavuthkuch.jankystuff.util.ComponentUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class RingOfTheSkyItem extends Item {
    public RingOfTheSkyItem() {
        super(
                new Properties()
                        .rarity(Rarity.RARE)
                        .stacksTo(1)
                        .fireResistant()
                        .setNoRepair()
                        .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
        );
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(ComponentUtil.translateItem("ring_of_the_sky.description").withStyle(ChatFormatting.GRAY));
        if(!ExternalMod.CURIOS.isLoaded()){
            pTooltipComponents.add(ComponentUtil.translateItem("need_curios.description").withStyle(ChatFormatting.DARK_RED));
        }
    }


}
