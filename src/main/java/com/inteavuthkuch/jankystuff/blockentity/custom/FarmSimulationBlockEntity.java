package com.inteavuthkuch.jankystuff.blockentity.custom;

import com.inteavuthkuch.jankystuff.block.IBlockEntityTicker;
import com.inteavuthkuch.jankystuff.blockentity.AdvancedQuarryBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.component.ModComponentTypes;
import com.inteavuthkuch.jankystuff.menu.custom.FarmSimulationMenu;
import com.inteavuthkuch.jankystuff.recipe.ModRecipes;
import com.inteavuthkuch.jankystuff.recipe.custom.FarmSimulationRecipe;
import com.inteavuthkuch.jankystuff.recipe.custom.FarmSimulationRecipeInput;
import com.inteavuthkuch.jankystuff.util.ChanceItemStack;
import com.inteavuthkuch.jankystuff.util.ChanceItemStackList;
import com.inteavuthkuch.jankystuff.util.CombineItemHandler;
import com.inteavuthkuch.jankystuff.util.RunnableItemStackHandler;
import com.inteavuthkuch.jankystuff.util.data.ModContainerDataIndex;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FarmSimulationBlockEntity extends BlockEntity implements IBlockEntityTicker, MenuProvider {
    private final ItemStackHandler inputHandler;
    private final ItemStackHandler outputHandler;
    private final ContainerData containerData;

    private int progress;
    private int maxProgress;

    public FarmSimulationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.FARM_SIMULATION_BE.get(), pPos, pBlockState);
        this.inputHandler = new RunnableItemStackHandler(2, this::itemStackHandlerContentsChanged) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if(slot == 0)
                    return FarmSimulationBlockEntity.this.canInsertAsInput(stack);
                return FarmSimulationBlockEntity.this.canInsertAsCatalyst(stack);
            }
        };

        this.outputHandler = new RunnableItemStackHandler(18, this::itemStackHandlerContentsChanged);
        this.containerData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex){
                  case 0 -> FarmSimulationBlockEntity.this.progress;
                  case 1 -> FarmSimulationBlockEntity.this.maxProgress;
                  default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex){
                    case 0 -> FarmSimulationBlockEntity.this.progress = pValue;
                    case 1 -> FarmSimulationBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Nullable
    public static IItemHandler getCapability(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity, @Nullable Direction direction) {
        return blockEntity instanceof FarmSimulationBlockEntity farmSimulationBlockEntity
                ? farmSimulationBlockEntity.getCombineHandler()
                : null;
    }

    public boolean canInsertAsInput(ItemStack itemStack) {
        List<FarmSimulationRecipe> farmSimulationRecipes = level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.FARM_SIMULATION_TYPE.get())
                .stream().map(RecipeHolder::value).toList();
        for(var recipe : farmSimulationRecipes) {
            if(recipe.input().test(itemStack))
                return true;
        }
        return false;
    }

    public boolean canInsertAsCatalyst(ItemStack itemStack) {
        List<FarmSimulationRecipe> farmSimulationRecipes = level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.FARM_SIMULATION_TYPE.get())
                .stream().map(RecipeHolder::value).toList();
        for(var recipe : farmSimulationRecipes) {
            if(recipe.catalyst().test(itemStack))
                return true;
        }
        return false;
    }

    public IItemHandler getInputHandler() {
        return this.inputHandler;
    }
    public IItemHandler getOutputHandler() {
        return this.outputHandler;
    }
    public IItemHandler getCombineHandler() {
        return new CombineItemHandler(inputHandler, outputHandler);
    }

    public void drops(Level pLevel, BlockPos pPos) {
        SimpleContainer inventory = new SimpleContainer(inputHandler.getSlots() + outputHandler.getSlots());

        for(int i = 0; i < inputHandler.getSlots(); i++) {
            inventory.setItem(i, inputHandler.getStackInSlot(i));
        }

        for(int i = 0; i < outputHandler.getSlots(); i++) {
            inventory.setItem(i, outputHandler.getStackInSlot(i));
        }

        Containers.dropContents(pLevel, pPos, inventory);
    }

    private void itemStackHandlerContentsChanged(int slot) {
        setChanged();
        if(level != null && !level.isClientSide()){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private boolean hasValidRecipe() {
        ItemStack input = inputHandler.getStackInSlot(0);
        ItemStack catalyst = inputHandler.getStackInSlot(1);

        Optional<RecipeHolder<FarmSimulationRecipe>> recipe = this.level.getRecipeManager()
                .getRecipeFor(ModRecipes.FARM_SIMULATION_TYPE.get(), new FarmSimulationRecipeInput(input, catalyst), level);

        if(recipe.isEmpty())
            return false;

        setMaxProgress(recipe.get().value().duration());
        return true;
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void resetProgress(){
        this.containerData.set(ModContainerDataIndex.FARM_SIMULATION_PROGRESS, 0);
    }

    private void setMaxProgress(int maxProgress) {
        this.containerData.set(ModContainerDataIndex.FARM_SIMULATION_MAX_PROGRESS, maxProgress);
    }

    private void setProgress(int progress) {
        this.containerData.set(ModContainerDataIndex.FARM_SIMULATION_PROGRESS, progress);
    }
    private void incrementProgress() {
        this.progress++;
    }

    private boolean craftedItems() {
        ItemStack input = inputHandler.getStackInSlot(0);
        ItemStack catalyst = inputHandler.getStackInSlot(1);

        Optional<RecipeHolder<FarmSimulationRecipe>> recipe = this.level.getRecipeManager()
                .getRecipeFor(ModRecipes.FARM_SIMULATION_TYPE.get(), new FarmSimulationRecipeInput(input, catalyst), level);

        if(recipe.isEmpty())
            return false;

        ChanceItemStackList recipeOutput = new ChanceItemStackList(recipe.get().value().results());
        NonNullList<ChanceItemStack> primaryItems = recipeOutput.getGuaranteedItems();
        NonNullList<ChanceItemStack> secondaryItems = recipeOutput.getChanceItems();

        if(primaryItems == null || primaryItems.isEmpty())
            return false;

        List<ItemStack> allOutput = new ArrayList<>();
        for(ChanceItemStack item : primaryItems){
            allOutput.add(item.itemStack());
        }

        if(secondaryItems != null && !secondaryItems.isEmpty()){
            for(ChanceItemStack secondaryItem : secondaryItems) {
                float roll = level.getRandom().nextFloat();
                if(roll < secondaryItem.chance()){
                    allOutput.add(secondaryItem.itemStack());
                }
            }
        }

        for(ItemStack itemStack : allOutput) {
            ItemStack remaining = itemStack.copy();
            for(int i=0;i<outputHandler.getSlots();i++){
                remaining = outputHandler.insertItem(i, remaining, true);
                if(remaining.isEmpty())
                    break;
            }
            if(!remaining.isEmpty())
                return false;
        }

        // Mean it can craft item - so put item in
        for(ItemStack itemStack : allOutput) {
            ItemStack remaining = itemStack.copy();
            for(int i=0;i<outputHandler.getSlots();i++){
                remaining = outputHandler.insertItem(i, remaining, false);
                if(remaining.isEmpty())
                    break;
            }
        }
        return true;
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(pLevel.isClientSide())
            return;

        if(hasValidRecipe()){
            if(hasProgressFinished()){
                if(craftedItems()){
                    resetProgress();
                }
            } else {
                incrementProgress();
            }
        } else {
            resetProgress();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        CompoundTag inputTag = inputHandler.serializeNBT(pRegistries);
        CompoundTag outputTag = outputHandler.serializeNBT(pRegistries);
        pTag.put("InputItems", inputTag);
        pTag.put("OutputItems", outputTag);
        pTag.putInt("Progress", progress);
        pTag.putInt("MaxProgress", maxProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        CompoundTag inputTag = pTag.getCompound("InputItems");
        CompoundTag outputTag = pTag.getCompound("OutputItems");
        inputHandler.deserializeNBT(pRegistries, inputTag);
        outputHandler.deserializeNBT(pRegistries, outputTag);
        progress = pTag.getInt("Progress");
        maxProgress = pTag.getInt("MaxProgress");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("block.jankystuff.farm_simulation");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new FarmSimulationMenu(pContainerId, pPlayerInventory, this, this.containerData);
    }
}
