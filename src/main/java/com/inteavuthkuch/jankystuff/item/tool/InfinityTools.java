package com.inteavuthkuch.jankystuff.item.tool;

import com.inteavuthkuch.jankystuff.common.ModToolTiers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.Unbreakable;

public class InfinityTools {

    private static Item.Properties getUnbreakableProperty() {
        return new Item.Properties()
                .fireResistant()
                .setNoRepair()
                .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .component(DataComponents.UNBREAKABLE, new Unbreakable(true));
    }

    public static BasePickaxeItem createPickaxe() {
        BasePickaxeItem tool = new BasePickaxeItem(ModToolTiers.INFINITY, Rarity.EPIC, 5.0f, 2.0f, getUnbreakableProperty());
        tool.showTooltip(false);
        return tool;
    }

    public static BaseAxeItem createAxe() {
        BaseAxeItem tool = new BaseAxeItem(ModToolTiers.INFINITY, Rarity.EPIC, 15.0f, 2.0f, getUnbreakableProperty());
        tool.showTooltip(false);
        return tool;
    }

    public static BaseShovelItem createShovel() {
        BaseShovelItem tool = new BaseShovelItem(ModToolTiers.INFINITY, Rarity.EPIC, 5.0f, 1.0f, getUnbreakableProperty());
        tool.showTooltip(false);
        return tool;
    }

    public static BaseSwordItem createSword() {
        BaseSwordItem tool = new BaseSwordItem(ModToolTiers.INFINITY, Rarity.EPIC, 20.0f, 3.0f, getUnbreakableProperty());
        tool.showTooltip(false);
        return tool;
    }

    public static PaxelItem createPaxel() {
        PaxelItem tool = new PaxelItem(ModToolTiers.INFINITY, Rarity.EPIC, 20.0f, 3.0f, getUnbreakableProperty());
        tool.showTooltip(false);
        return tool;
    }
}
