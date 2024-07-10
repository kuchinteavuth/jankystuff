package com.inteavuthkuch.jankystuff.menu;

import com.inteavuthkuch.jankystuff.common.ContainerType;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class WoodenCrateMenu extends AbstractContainerMenuBase {

    public WoodenCrateMenu(int pContainerId, Inventory playerInv) {
        this(pContainerId, playerInv, new SimpleContainer(ContainerType.WOODEN.getSize()));
    }

    public WoodenCrateMenu(int pContainerId, Inventory pPlayerInventory, Container container) {
        super(ModMenuType.WOODEN_CRATE.get(), pContainerId, container);

        checkContainerSize(this.container, ContainerType.WOODEN.getSize());
        this.container.startOpen(pPlayerInventory.player);

        createSlotContainer();
        createPlayerInventory(pPlayerInventory);
        createPlayerHotbar(pPlayerInventory);
    }

    @Override
    protected void createSlotContainer(){
        for(int i = 0; i < ContainerType.WOODEN.getRow(); i++) {
            for(int j = 0; j < ContainerType.WOODEN.getCol(); j++){
                this.addSlot(new Slot(container, j + i * ContainerType.WOODEN.getCol(), 8 + j * 18, 18 + i * 18));
            }
        }
    }

    @Override
    protected void createPlayerHotbar(Inventory pPlayerInventory) {
        int z = (ContainerType.WOODEN.getRow() - 4) * 18;

        for(int i=0; i<9; ++i){
            this.addSlot(new Slot(pPlayerInventory, i, 26 + i * 18, 162 + z));
        }
    }

    @Override
    protected void createPlayerInventory(Inventory pPlayerInventory) {
        int z = (ContainerType.WOODEN.getRow() - 4) * 18;

        for(int i=0; i<3; ++i){
            for(int j=0; j<9; ++j){
                this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 26 + j * 18, 104 + i * 18 + z));
            }
        }
    }
}
