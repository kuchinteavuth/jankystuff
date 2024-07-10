package com.inteavuthkuch.jankystuff.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractContainerMenuBase extends AbstractContainerMenu {

    protected final Container container;
    protected final int upgradeCount;

    public AbstractContainerMenuBase(@Nullable MenuType<?> pMenuType, int pContainerId, Container container) {
        super(pMenuType, pContainerId);
        this.container = container;
        this.upgradeCount = 0;
    }

    public AbstractContainerMenuBase(@Nullable MenuType<?> pMenuType, int pContainerId, Container container, int upgradeCount) {
        super(pMenuType, pContainerId);
        this.container = container;
        this.upgradeCount = upgradeCount;
    }

    /**
     * Override if you have different GUI size or different from vanilla
     * @param pPlayerInventory inventory of player
     */
    protected void createPlayerHotbar(Inventory pPlayerInventory) {
        for(int i=0; i<9; ++i){
            this.addSlot(new Slot(pPlayerInventory, i, (26 - 18)  + i * 18, 142));
        }
    }

    /**
     * Override if you have different GUI size or different from vanilla
     * @param pPlayerInventory inventory of player
     */
    protected void createPlayerInventory(Inventory pPlayerInventory) {
        for(int i=0; i<3; ++i){
            for(int j=0; j<9; ++j){
                this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    protected abstract void createSlotContainer();

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if(slot.hasItem()){
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if(pIndex < this.container.getContainerSize()){
                if(!this.moveItemStackTo(originalStack, this.container.getContainerSize(), this.slots.size(), true)){
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.moveItemStackTo(originalStack, 0, this.container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if(originalStack.isEmpty()){
                slot.set(ItemStack.EMPTY);
            }
            else{
                slot.setChanged();
            }
        }
        return newStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.container.stillValid(pPlayer);
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.container.stopOpen(pPlayer);
    }

    public Container getContainer() {
        return container;
    }
}
