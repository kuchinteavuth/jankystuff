package com.inteavuthkuch.jankystuff.blockentity.crate;

import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.menu.WoodenCrateMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class WoodenCrateBlockEntity extends AbstractCrateBlockEntity {

    public WoodenCrateBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.WOODEN_CRATE_BE.get(), pPos, pBlockState, ContainerType.WOODEN);
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new WoodenCrateMenu(pContainerId, pInventory, this);
    }
}
