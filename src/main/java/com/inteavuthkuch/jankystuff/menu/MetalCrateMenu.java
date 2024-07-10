package com.inteavuthkuch.jankystuff.menu;

import com.inteavuthkuch.jankystuff.common.ContainerType;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class MetalCrateMenu extends AbstractContainerMenuBase {

    public MetalCrateMenu(int pContainerId, Inventory playerInv) {
        this(pContainerId, playerInv, new SimpleContainer(ContainerType.METAL.getSize()));
    }

    public MetalCrateMenu(int pContainerId, Inventory pPlayerInventory, Container container) {
        super(ModMenuType.METAL_CRATE.get(), pContainerId, container);

        checkContainerSize(this.container, ContainerType.METAL.getSize());
        this.container.startOpen(pPlayerInventory.player);

        createSlotContainer();
        createPlayerInventory(pPlayerInventory);
        createPlayerHotbar(pPlayerInventory);
    }

    @Override
    protected void createSlotContainer(){
        for(int i = 0; i < ContainerType.METAL.getRow(); i++) {
            for(int j = 0; j < ContainerType.METAL.getCol(); j++){
                this.addSlot(new Slot(container, j + i * ContainerType.METAL.getCol(), 5 + j * 18, 5 + i * 18));
            }
        }
    }

    @Override
    protected void createPlayerHotbar(Inventory pPlayerInventory) {
        int z = (ContainerType.METAL.getRow() - 4) * 18;

        for(int i=0; i<9; ++i){
            this.addSlot(new Slot(pPlayerInventory, i, 41 + i * 18, 142 + z));
        }
    }

    @Override
    protected void createPlayerInventory(Inventory pPlayerInventory) {
        int z = (ContainerType.METAL.getRow() - 4) * 18;

        for(int i=0; i<3; ++i){
            for(int j=0; j<9; ++j){
                this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 41 + j * 18, 84 + i * 18 + z));
            }
        }
    }
}
