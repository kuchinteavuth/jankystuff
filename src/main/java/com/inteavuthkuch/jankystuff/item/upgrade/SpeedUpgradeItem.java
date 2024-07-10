package com.inteavuthkuch.jankystuff.item.upgrade;

import com.inteavuthkuch.jankystuff.common.UpgradeType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class SpeedUpgradeItem extends BaseUpgradeItem {
    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        BaseUpgradeItem item = (BaseUpgradeItem) pStack.getItem();
        Component tooltip = Component.translatable("item.jankystuff.speed_upgrade.description", item.speedUsage() * 100).withStyle(ChatFormatting.GRAY);
        pTooltipComponents.add(tooltip);
    }

    @Override
    public UpgradeType getUpgradeType() {
        return UpgradeType.SPEED;
    }

    @Override
    public double speedUsage() {
        return 0.2D;
    }
}
