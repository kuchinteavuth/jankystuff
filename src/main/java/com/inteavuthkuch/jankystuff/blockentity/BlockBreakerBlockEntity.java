package com.inteavuthkuch.jankystuff.blockentity;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.BlockBreakerBlock;
import com.inteavuthkuch.jankystuff.block.IBlockEntityTicker;
import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.common.UpgradeType;
import com.inteavuthkuch.jankystuff.item.upgrade.BaseUpgradeItem;
import com.inteavuthkuch.jankystuff.menu.BlockBreakerMenu;
import com.inteavuthkuch.jankystuff.util.ComponentUtil;
import com.inteavuthkuch.jankystuff.util.ContainerUtil;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlockBreakerBlockEntity extends BaseContainerBlockEntity implements IBlockEntityTicker, IUpgradableBlockEntity {
    private int cooldown = -1;
    private NonNullList<ItemStack> items;
    private final ContainerType containerType;

    private static final int BREAKER_COOLDOWN = 60;

    public BlockBreakerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.BLOCK_BREAKER_BE.get(), pPos, pBlockState);
        this.containerType = ContainerType.BLOCK_BREAKER;
        this.items = NonNullList.withSize(this.containerType.getSize(), ItemStack.EMPTY);
    }

    private boolean isOnCooldown(){
        return cooldown > 0;
    }
    private void setCooldown(int value) {
        this.cooldown = value;
    }

    private ItemStack getSuitableTool(BlockState pState) {
        int toolIndex = this.containerType.getRow() * this.containerType.getCol();
        ItemStack tool = this.items.get(toolIndex);
        if(!tool.isEmpty())
            return tool;

        if(pState.is(BlockTags.MINEABLE_WITH_SHOVEL))
            return new ItemStack(Items.DIAMOND_SHOVEL);
        else if(pState.is(BlockTags.MINEABLE_WITH_AXE))
            return new ItemStack(Items.DIAMOND_AXE);
        else if(pState.is(BlockTags.MINEABLE_WITH_HOE))
            return new ItemStack(Items.DIAMOND_HOE);
        else {
            return new ItemStack(Items.DIAMOND_PICKAXE);
        }
    }

    private Optional<List<BaseUpgradeItem>> getUpgradeItem() {
        List<BaseUpgradeItem> upgradeItems = new ArrayList<>();
        boolean found = false;
        for(ItemStack item : this.items){
            if(item.getItem() instanceof BaseUpgradeItem){
                upgradeItems.add((BaseUpgradeItem) item.getItem());
                found = true;
            }
        }
        return found ? Optional.of(upgradeItems) : Optional.empty();
    }

    private void checkForUpgrade(int baseCooldown) {
        Optional<List<BaseUpgradeItem>> upgrades = getUpgradeItem();
        if(upgrades.isPresent()){
            double speedUsage = 0;
            for(BaseUpgradeItem upgrade : upgrades.get()){
                if(upgrade.getUpgradeType() == UpgradeType.SPEED){
                    speedUsage += baseCooldown * upgrade.speedUsage();
                }
            }

            int finalCooldown = Math.max(1, baseCooldown - (int)speedUsage);
            setCooldown(finalCooldown);
        }
        else{
            setCooldown(baseCooldown);
        }
    }
    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(isOnCooldown()){
            this.cooldown--;
        }
        else{

            checkForUpgrade(BREAKER_COOLDOWN);

            BlockPos breakFace = this.getBlockPos().relative(this.getBlockState().getValue(BlockBreakerBlock.FACING));
            if(!pLevel.getBlockState(breakFace).isEmpty()){
                BlockState breakFaceState = pLevel.getBlockState(breakFace);
                LootParams.Builder builder = new LootParams.Builder((ServerLevel) pLevel);
                builder.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(breakFace));
                builder.withParameter(LootContextParams.TOOL, getSuitableTool(breakFaceState));
                builder.withOptionalParameter(LootContextParams.BLOCK_ENTITY, pLevel.getBlockEntity(breakFace));

                var items = breakFaceState.getDrops(builder);
                for(ItemStack item : items) {
                    if(!ContainerUtil.canInsertItemStack(this.items, item)){
                        //it will drop item on the ground If it cannot insert to it own inventory
                        ContainerUtil.dropItemStack(pLevel, breakFace, item);
                    }
                }
                pLevel.destroyBlock(breakFace, false);
                setChanged();
            }
        }
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.cooldown = pTag.getInt("cooldown");
        ContainerHelper.loadAllItems(pTag, this.items, pRegistries);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("cooldown", this.cooldown);
        ContainerHelper.saveAllItems(pTag, this.items, pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected Component getDefaultName() {
        return ComponentUtil.translateBlock(containerType.getId());
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItems) {
        this.items = pItems;
    }

    @Override
    public boolean canPlaceItem(int pSlot, ItemStack pStack) {
        return false;
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new BlockBreakerMenu(pContainerId, pInventory, this);
    }

    @Override
    public int getContainerSize() {
        return this.containerType.getSize();
    }

    // IUpgradable Block Entity
    @Override
    public int upgradeSlotCount() {
        return ContainerType.BLOCK_BREAKER.getAdditionalSlot() - 1;
    }

    @Override
    public List<Integer> upgradeSlotsIndex() {
        int toolSlot = ContainerType.BLOCK_BREAKER.getCol() * ContainerType.BLOCK_BREAKER.getRow();
        return List.of(toolSlot + 1, toolSlot + 2, toolSlot + 3);
    }

    @Override
    public boolean canAccept(int slot, UpgradeType upgradeType) {
        return upgradeType == UpgradeType.SPEED;
    }

    @Override
    public boolean insertUpgrade(int slot, ItemStack itemStack) {
        if(this.items.get(slot).isEmpty()){
            this.items.set(slot, itemStack.copy());
            itemStack.shrink(1);
            return true;
        }
        return false;
    }
}
