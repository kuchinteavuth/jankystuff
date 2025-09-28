package com.inteavuthkuch.jankystuff.menu.custom;

import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.blockentity.custom.FarmSimulationBlockEntity;
import com.inteavuthkuch.jankystuff.menu.ModMenuType;
import com.inteavuthkuch.jankystuff.util.CombineItemHandler;
import com.inteavuthkuch.jankystuff.util.data.ModContainerDataIndex;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class FarmSimulationMenu extends AbstractContainerMenu {
    private final ContainerData data;
    private final Level level;
    private final FarmSimulationBlockEntity blockEntity;
    private final IItemHandler inputHandler;
    private final IItemHandler outputHandler;

    public FarmSimulationMenu(int pContainerId, Inventory inventory, FriendlyByteBuf byteBuf) {
        this(pContainerId, inventory, inventory.player.level().getBlockEntity(byteBuf.readBlockPos()), new SimpleContainerData(2));
    }

    public FarmSimulationMenu(int pContainerId, Inventory playerInventory, BlockEntity blockEntity, ContainerData data) {
        super(ModMenuType.FARM_SIMULATION.get(), pContainerId);
        this.blockEntity = (FarmSimulationBlockEntity) blockEntity;
        this.level = playerInventory.player.level();
        this.data = data;
        this.inputHandler = this.blockEntity.getInputHandler();
        this.outputHandler = this.blockEntity.getOutputHandler();

        // Input Slots
        this.addSlot(new SlotItemHandler(inputHandler, 0, 19,17) {
            @Override
            public int getMaxStackSize(ItemStack stack) {
                return 1;
            }
            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return FarmSimulationMenu.this.blockEntity.canInsertAsInput(stack);
            }
        });
        this.addSlot(new SlotItemHandler(inputHandler, 1, 19,53) {
            @Override
            public int getMaxStackSize(ItemStack stack) {
                return 1;
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
            @Override
            public boolean mayPlace(ItemStack stack) {
                return FarmSimulationMenu.this.blockEntity.canInsertAsCatalyst(stack);
            }
        });

        // Output Slots
        createOutputSlots();

        createPlayerInventory(playerInventory);
        createPlayerHotbar(playerInventory);
        addDataSlots(data);
    }

    public boolean isCrafting() {
        return this.data.get(ModContainerDataIndex.FARM_SIMULATION_PROGRESS) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(ModContainerDataIndex.FARM_SIMULATION_PROGRESS);
        int maxProgress = this.data.get(ModContainerDataIndex.FARM_SIMULATION_MAX_PROGRESS);
        int arrowWidth = 34;

        return maxProgress != 0 && progress != 0 ? progress * arrowWidth / maxProgress : 0;
    }

    private void createOutputSlots() {
        int columns = 6;
        int rows = 3;
        final int slotSize = 18;
        final int maxRenderSlot = rows * columns;
        final int startX = 61;
        final int startY = 17;

        for(int i=0; i< Math.min(outputHandler.getSlots(), maxRenderSlot); i++) {
            int row = i / columns;
            int col = i % columns;

            int x = startX + col * slotSize;
            int y = startY + row * slotSize;

            this.addSlot(new SlotItemHandler(outputHandler, i, x, y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
        }
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
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();

            int inputSize = inputHandler.getSlots();
            int outputSize = outputHandler.getSlots();
            int playerStart = inputSize + outputSize;
            int playerEnd = this.slots.size();

            if (index >= inputSize && index < inputSize + outputSize) {
                // From output to player inventory
                if (!this.moveItemStackTo(originalStack, playerStart, playerEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= playerStart && index < playerEnd) {
                // From player inventory to input
                if (!this.moveItemStackTo(originalStack, 0, inputSize, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // From input to player inventory
                if (!this.moveItemStackTo(originalStack, playerStart, playerEnd, true)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, ModBlocks.FARM_SIMULATION.get());
    }

}
