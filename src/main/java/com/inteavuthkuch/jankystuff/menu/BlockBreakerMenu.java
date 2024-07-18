package com.inteavuthkuch.jankystuff.menu;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.inventory.FuelSlot;
import com.inteavuthkuch.jankystuff.inventory.OutputSlot;
import com.inteavuthkuch.jankystuff.inventory.TagAcceptSlot;
import com.inteavuthkuch.jankystuff.inventory.UpgradeSlot;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class BlockBreakerMenu extends AbstractContainerMenuBase {

    private final ContainerType containerType;

    public BlockBreakerMenu(int pContainerId, Inventory inventory) {
        this(pContainerId, inventory, new SimpleContainer(ContainerType.BLOCK_BREAKER.getSize()));
    }

    public BlockBreakerMenu(int pContainerId, Inventory inventory, Container container) {
        super(ModMenuType.BLOCK_BREAKER.get(), pContainerId, container, ContainerType.BLOCK_BREAKER.getAdditionalSlot());

        this.containerType = ContainerType.BLOCK_BREAKER;
        checkContainerSize(this.container, containerType.getSize());
        this.container.startOpen(inventory.player);

        createSlotContainer();
        createPlayerInventory(inventory);
        createPlayerHotbar(inventory);
    }

    @Override
    protected void createSlotContainer() {
        for(int i = 0; i < this.containerType.getRow(); i++) {
            for(int j = 0; j < this.containerType.getCol(); j++){
                int index = j + i * this.containerType.getCol();
                this.addSlot(new OutputSlot(container, index, 62 + j * 18, 18 + i * 18));

            }
        }

        int toolSlotIndex = this.containerType.getCol() * this.containerType.getRow();
        this.addSlot(new TagAcceptSlot(container, toolSlotIndex, 26,36, ItemTags.PICKAXES));
        for(int i=0; i<this.containerType.getAdditionalSlot()-1;i++){
            int index = toolSlotIndex + i + 1;
            this.addSlot(new UpgradeSlot(container, index, 182, 5 + i * 18));
        }
    }
}
