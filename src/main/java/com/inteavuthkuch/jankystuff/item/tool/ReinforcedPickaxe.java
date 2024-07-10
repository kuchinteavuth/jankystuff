package com.inteavuthkuch.jankystuff.item.tool;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import java.util.List;

public class ReinforcedPickaxe extends PickaxeItem {

    public ReinforcedPickaxe(Tier tier, float atkDamage, float atkSpeed) {
        super(tier, new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .attributes(PickaxeItem.createAttributes(tier, atkDamage, atkSpeed)));
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable("tooltip.jankystuff.reinforced_tools"));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

}
