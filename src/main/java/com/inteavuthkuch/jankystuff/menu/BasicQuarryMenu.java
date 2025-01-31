package com.inteavuthkuch.jankystuff.menu;

import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.blockentity.BasicQuarryBlockEntity;
import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.inventory.FuelSlot;
import com.inteavuthkuch.jankystuff.inventory.UpgradeSlot;
import com.inteavuthkuch.jankystuff.util.ComponentUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BasicQuarryMenu extends AbstractContainerMenuBase {

    private final ContainerType containerType;
    private final ContainerData data;

    public BasicQuarryMenu(int pContainerId, Inventory inventory) {
        this(pContainerId, inventory, new SimpleContainer(ContainerType.BASIC_QUARRY.getSize()), new SimpleContainerData(2));
    }

    public BasicQuarryMenu(int pContainerId, Inventory pPlayerInventory, Container container, ContainerData data) {
        super(ModMenuType.BASIC_QUARRY.get(), pContainerId, container);
        this.containerType = ContainerType.BASIC_QUARRY;
        this.data = data;

        checkContainerSize(this.container, containerType.getSize());
        this.container.startOpen(pPlayerInventory.player);

        createSlotContainer();
        createPlayerInventory(pPlayerInventory);
        createPlayerHotbar(pPlayerInventory);

        addDataSlots(data);
    }

    @Override
    protected void createSlotContainer() {
        for(int i = 0; i < this.containerType.getRow(); i++) {
            for(int j = 0; j < this.containerType.getCol(); j++){
                int index = j + i * this.containerType.getCol();
                this.addSlot(new FuelSlot(container, index, 8 + j * 18, 17 + i * 18));
            }
        }

        // for upgrade slot
        int slotIndex = this.containerType.getCol() * this.containerType.getRow();
        for(int i=0; i<this.containerType.getAdditionalSlot();i++){
            int index = slotIndex + i;
            this.addSlot(new UpgradeSlot(container, index, 182, 5 + i * 18));
        }
    }

    public int getBurnTime() {
        return this.data.get(BasicQuarryBlockEntity.BURN_TIME_DATA_SLOT);
    }

    public boolean hasErrorCode() {
        return this.data.get(BasicQuarryBlockEntity.ERROR_CODE_DATA_SLOT) > 0;
    }

    public Component getQuarryState() {

        return switch (this.data.get(BasicQuarryBlockEntity.ERROR_CODE_DATA_SLOT)){
            case BasicQuarryBlockEntity.CODE_MISSING_TOP_INVENTORY
                    -> ComponentUtil.translateBlock(ModBlocks.BASIC_QUARRY, "code.missing_top_inventory")
                    .withStyle(ChatFormatting.DARK_RED);

            case BasicQuarryBlockEntity.CODE_NOT_ENOUGH_SPACE
                    -> ComponentUtil.translateBlock(ModBlocks.BASIC_QUARRY, "code.not_enough_space")
                    .withStyle(ChatFormatting.DARK_RED);

            case BasicQuarryBlockEntity.CODE_NOT_ENOUGH_FUEL
                    -> ComponentUtil.translateBlock(ModBlocks.BASIC_QUARRY, "code.not_enough_fuel")
                    .withStyle(ChatFormatting.DARK_RED);

            case BasicQuarryBlockEntity.CODE_PAUSE
                    -> ComponentUtil.translateBlock(ModBlocks.BASIC_QUARRY, "code.pause");

            case BasicQuarryBlockEntity.CODE_FINISHED
                  -> ComponentUtil.translateBlock(ModBlocks.BASIC_QUARRY, "code.finished");
            default
                    -> ComponentUtil.translateBlock(ModBlocks.BASIC_QUARRY, "code.normal")
                    ;
        };
    }
}
