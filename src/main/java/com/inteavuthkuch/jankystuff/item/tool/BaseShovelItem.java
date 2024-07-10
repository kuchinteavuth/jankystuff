package com.inteavuthkuch.jankystuff.item.tool;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import java.util.List;

public class BaseShovelItem extends ShovelItem {
    private boolean showTooltip = true;

    public BaseShovelItem(Tier tier, float atkDamage, float atkSpeed) {
        this(tier, Rarity.COMMON, atkDamage, atkSpeed, new Item.Properties());
    }
    public BaseShovelItem(Tier tier, Rarity rarity, float atkDamage, float atkSpeed) {
        this(tier, rarity, atkDamage, atkSpeed, new Properties());
    }

    public BaseShovelItem(Tier tier, Rarity rarity, float atkDamage, float atkSpeed, Item.Properties properties) {
        super(tier, properties
                .rarity(rarity)
                .attributes(ShovelItem.createAttributes(tier, atkDamage, atkSpeed)));
    }
    public void showTooltip(boolean value) {
        this.showTooltip = value;
    }
    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if(this.showTooltip) {
            pTooltipComponents.add(Component.translatable("tooltip.jankystuff.reinforced_tools"));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
