package com.inteavuthkuch.jankystuff.item.tool;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

import java.util.List;

public class ReinforcedSword extends SwordItem {
    public ReinforcedSword(Tier pTier, float atkDamage, float atkSpeed) {
        super(pTier, new Item.Properties().rarity(Rarity.UNCOMMON)
                .attributes(SwordItem.createAttributes(pTier, atkDamage, atkSpeed))
        );
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable("tooltip.jankystuff.reinforced_tools"));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {

        return super.onLeftClickEntity(stack, player, entity);
    }
}
