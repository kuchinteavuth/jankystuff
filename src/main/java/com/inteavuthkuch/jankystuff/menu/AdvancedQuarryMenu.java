package com.inteavuthkuch.jankystuff.menu;

import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.blockentity.AdvancedQuarryBlockEntity;
import com.inteavuthkuch.jankystuff.item.custom.IFilterItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class AdvancedQuarryMenu extends AbstractContainerMenu {
    private final ContainerData data;
    private final Level level;
    public final AdvancedQuarryBlockEntity blockEntity;

    public AdvancedQuarryMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(7));
    }

    public AdvancedQuarryMenu(int pContainerId, Inventory inv, BlockEntity blockEntity, ContainerData data) {
        super(ModMenuType.ADVANCED_QUARRY.get(), pContainerId);
        this.blockEntity = (AdvancedQuarryBlockEntity) blockEntity;
        this.level = inv.player.level();
        this.data = data;

        for(int i=0;i<9;i++){
            this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), i, 8 + (i * 18), 54) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof IFilterItem;
                }
            });
        }
        createPlayerInventory(inv);
        createPlayerHotbar(inv);
        addDataSlots(data);
    }

    public boolean isNoInventory() {
        return data.get(AdvancedQuarryBlockEntity.NO_INVENTORY) > 0;
    }
    public boolean isMissionInventory() {
        return data.get(AdvancedQuarryBlockEntity.MISSION_INVENTORY) > 0;
    }
    public boolean isPause() {
        return data.get(AdvancedQuarryBlockEntity.IS_PAUSE) > 0;
    }
    public boolean isCompletedWork() {
        return data.get(AdvancedQuarryBlockEntity.COMPLETED_WORK) > 0;
    }
    public int getProgress() {
        return data.get(AdvancedQuarryBlockEntity.PROGRESS);
    }
    public int getMaxProgress() {
        return data.get(AdvancedQuarryBlockEntity.MAX_PROGRESS);
    }
    public boolean isDisabled() {
        return data.get(AdvancedQuarryBlockEntity.IS_DISABLED) > 0;
    }

    protected void createPlayerInventory(Inventory pPlayerInventory) {
        for(int i=0; i<3; ++i){
            for(int j=0; j<9; ++j){
                this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    protected void createPlayerHotbar(Inventory pPlayerInventory) {
        for(int i=0; i<9; ++i){
            this.addSlot(new Slot(pPlayerInventory, i, (26 - 18)  + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int pIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if(slot.hasItem()){
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if(pIndex < blockEntity.getItemHandler().getSlots()){
                if(!this.moveItemStackTo(originalStack, blockEntity.getItemHandler().getSlots(), this.slots.size(), true)){
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.moveItemStackTo(originalStack, 0, blockEntity.getItemHandler().getSlots(), false)) {
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
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.ADVANCED_QUARRY.get());
    }
}
