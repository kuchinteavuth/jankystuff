package com.inteavuthkuch.jankystuff.blockentity.crate;

import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.menu.MetalCrateMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MetalCrateBlockEntity extends AbstractCrateBlockEntity {

    public MetalCrateBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.METAL_CRATE_BE.get(), pPos, pBlockState, ContainerType.METAL);
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new MetalCrateMenu(pContainerId, pInventory, this);
    }


}
