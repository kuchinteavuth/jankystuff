package com.inteavuthkuch.jankystuff.item.tool;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.Unbreakable;

public class InfinityPickaxe extends PickaxeItem {
    public InfinityPickaxe(float atkDamage, float atkSpeed) {
        super(ModToolTiers.INFINITY,
                new Properties()
                        .fireResistant()
                        .rarity(Rarity.EPIC)
                        .setNoRepair()
                        .attributes(PickaxeItem.createAttributes(ModToolTiers.INFINITY, atkDamage, atkSpeed))
                        .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
        );
    }
}
