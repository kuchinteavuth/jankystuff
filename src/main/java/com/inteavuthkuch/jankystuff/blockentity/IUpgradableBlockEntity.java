package com.inteavuthkuch.jankystuff.blockentity;

import com.inteavuthkuch.jankystuff.common.UpgradeType;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IUpgradableBlockEntity {
    int upgradeSlotCount();
    List<Integer> upgradeSlotsIndex();
    boolean canAccept(int slot, UpgradeType upgradeType);
    boolean insertUpgrade(int slot, ItemStack itemStack);
}
