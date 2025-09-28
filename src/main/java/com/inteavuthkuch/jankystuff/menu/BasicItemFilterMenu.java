package com.inteavuthkuch.jankystuff.menu;

import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.inventory.GhostSlot;
import com.inteavuthkuch.jankystuff.item.custom.IFilterItem;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BasicItemFilterMenu extends AbstractContainerMenu {

    private final Container container;

    public BasicItemFilterMenu(int pContainerId, Inventory inventory) {
        this(pContainerId, inventory, new SimpleContainer(27));
    }

    public BasicItemFilterMenu(int pContainerId, Inventory pPlayerInventory, Container container) {
        super(ModMenuType.BASIC_ITEM_FILTER.get(), pContainerId);
        this.container = container;

        checkContainerSize(this.container, 27);
        this.container.startOpen(pPlayerInventory.player);

        createSlotContainer();
        createPlayerInventory(pPlayerInventory);
        createPlayerHotbar(pPlayerInventory);
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.container.stopOpen(pPlayer);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot clickedSlot = getSlot(index);
        ItemStack original = clickedSlot.getItem();

        if (clickedSlot instanceof GhostSlot || original.isEmpty()) {
            return ItemStack.EMPTY;
        }

        // Check if item already exists in ghost slots
        for (Slot slot : slots) {
            if (slot instanceof GhostSlot) {
                ItemStack ghost = slot.getItem();
                if (!ghost.isEmpty() && ItemStack.isSameItem(ghost, original)) {
                    return ItemStack.EMPTY; // already exists, don't add
                }
            }
        }

        // Find first empty ghost slot to add it
        for (Slot slot : slots) {
            if (slot instanceof GhostSlot && !slot.hasItem() && slot.mayPlace(original)) {
                ItemStack ghostCopy = original.copy();
                ghostCopy.setCount(1);
                slot.set(ghostCopy);
                return ItemStack.EMPTY;
            }
        }

        return ItemStack.EMPTY; // no empty slot available
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    protected void createSlotContainer() {
        for(int i = 0; i < ContainerType.BASIC_ITEM_FILTER.getRow(); i++) {
            for(int j = 0; j < ContainerType.BASIC_ITEM_FILTER.getCol(); j++){
                this.addSlot(new GhostSlot(container, j + i * ContainerType.BASIC_ITEM_FILTER.getCol(), 8 + j * 18, 18 + i * 18));
            }
        }
    }

    protected void createPlayerHotbar(Inventory pPlayerInventory) {
        for(int i=0; i<9; ++i){
            this.addSlot(new Slot(pPlayerInventory, i, 8 + i * 18, 142));
        }
    }

    protected void createPlayerInventory(Inventory pPlayerInventory) {
        for(int i=0; i<3; ++i){
            for(int j=0; j<9; ++j){
                this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }
}
