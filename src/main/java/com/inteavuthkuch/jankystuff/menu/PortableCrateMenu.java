package com.inteavuthkuch.jankystuff.menu;

import com.inteavuthkuch.jankystuff.common.ContainerType;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class PortableCrateMenu extends AbstractContainerMenuBase {

    public PortableCrateMenu(int pContainerId, Inventory inventory) {
        this(pContainerId, inventory, new SimpleContainer(ContainerType.PORTABLE.getSize()));
    }

    public PortableCrateMenu(int pContainerId, Inventory pPlayerInventory, Container container) {
        super(ModMenuType.PORTABLE_CRATE.get(), pContainerId, container);

        checkContainerSize(this.container, ContainerType.PORTABLE.getSize());
        this.container.startOpen(pPlayerInventory.player);

        createSlotContainer();
        createPlayerInventory(pPlayerInventory);
        createPlayerHotbar(pPlayerInventory);
    }

    @Override
    protected void createSlotContainer() {
        for(int i = 0; i < ContainerType.PORTABLE.getRow(); i++) {
            for(int j = 0; j < ContainerType.PORTABLE.getCol(); j++){
                this.addSlot(new Slot(container, j + i * ContainerType.PORTABLE.getCol(), 8 + j * 18, 18 + i * 18));
            }
        }
    }

    @Override
    protected void createPlayerHotbar(Inventory pPlayerInventory) {
        int z = (ContainerType.PORTABLE.getRow() - 4) * 18;

        for(int i=0; i<9; ++i){
            this.addSlot(new Slot(pPlayerInventory, i, 26 + i * 18, 162 + z));
        }
    }

    @Override
    protected void createPlayerInventory(Inventory pPlayerInventory) {
        int z = (ContainerType.PORTABLE.getRow() - 4) * 18;

        for(int i=0; i<3; ++i){
            for(int j=0; j<9; ++j){
                this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 26 + j * 18, 104 + i * 18 + z));
            }
        }
    }
}
