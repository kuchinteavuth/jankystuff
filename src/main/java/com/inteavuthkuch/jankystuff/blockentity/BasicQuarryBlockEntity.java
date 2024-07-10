package com.inteavuthkuch.jankystuff.blockentity;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.BasicQuarryBlock;
import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.common.UpgradeType;
import com.inteavuthkuch.jankystuff.config.JankyStuffCommonConfig;
import com.inteavuthkuch.jankystuff.item.upgrade.BaseUpgradeItem;
import com.inteavuthkuch.jankystuff.menu.BasicQuarryMenu;
import com.inteavuthkuch.jankystuff.util.ComponentUtil;
import com.inteavuthkuch.jankystuff.util.ItemStackUtil;
import com.inteavuthkuch.jankystuff.util.TagUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BasicQuarryBlockEntity extends BaseContainerBlockEntity implements IUpgradableBlockEntity {

    private NonNullList<ItemStack> items;
    private final ContainerType containerType;
    private final ContainerData data;

    private int burnTime = 0;
    private int errorCode;
    private int cooldown = -1;

    public static final int CODE_NORMAL = 0;
    public static final int CODE_MISSING_TOP_INVENTORY = 1;
    public static final int CODE_NOT_ENOUGH_SPACE = 2;
    public static final int CODE_NOT_ENOUGH_FUEL = 3;
    public static final int CODE_PAUSE = 4;

    public static final int BURN_TIME_DATA_SLOT = 0;
    public static final int ERROR_CODE_DATA_SLOT = 1;

    public BasicQuarryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.BASIC_QUARRY_BE.get(), pPos, pBlockState);
        this.containerType = ContainerType.BASIC_QUARRY;
        items = NonNullList.withSize(this.containerType.getSize(), ItemStack.EMPTY);
        data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> BasicQuarryBlockEntity.this.burnTime;
                    case 1 -> BasicQuarryBlockEntity.this.errorCode;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> BasicQuarryBlockEntity.this.burnTime = pValue;
                    case 1 -> BasicQuarryBlockEntity.this.errorCode = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    protected Component getDefaultName() {
        return ComponentUtil.translateBlock(containerType.getId());
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItems) {
        this.items = pItems;
    }

    @Override
    public boolean canPlaceItem(int pSlot, ItemStack pStack) {
        if(pStack.getBurnTime(null) > 0)
            return super.canPlaceItem(pSlot, pStack);
        return false;
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new BasicQuarryMenu(pContainerId, pInventory, this, data);
    }

    @Override
    public int getContainerSize() {
        return this.containerType.getSize();
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        this.burnTime = pTag.getInt("quarryBurnTime");
        this.errorCode = pTag.getInt("quarryErrorCode");
        this.cooldown = pTag.getInt("quarryCooldown");
        ContainerHelper.loadAllItems(pTag, this.items, pRegistries);
    }
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("quarryBurnTime", this.burnTime);
        pTag.putInt("quarryErrorCode", this.errorCode);
        pTag.putInt("quarryCooldown", this.cooldown);
        ContainerHelper.saveAllItems(pTag, this.items, pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private boolean isOnCooldown() {
        return this.cooldown > 0;
    }
    public void setCooldown(int value) {
        this.cooldown = value;
    }

    private boolean consumeBurnItem(Level pLevel) {
        if(items.isEmpty() || pLevel.isClientSide()) return false;

        for(int i=0;i<items.size();i++){
            ItemStack item = items.get(i);
            if(FurnaceBlockEntity.isFuel(item)){
                this.burnTime = item.getBurnTime(null);
                //this.data.set(BURN_TIME_DATA_SLOT, this.burnTime);
                this.removeItem(i, 1);
                return true;
            }
        }
        return false;
    }

    private Optional<Container> getTopContainer(Level pLevel, BlockPos pPos, BlockState pState) {
        BlockPos pos = pPos.above();
        BlockState state = pLevel.getBlockState(pos);
        Block block = state.getBlock();

        if(block instanceof WorldlyContainerHolder) {
            return Optional.of(((WorldlyContainerHolder) block).getContainer(pState, pLevel, pPos));
        }
        else if(state.hasBlockEntity() && pLevel.getBlockEntity(pos) instanceof Container container){
            if(container instanceof ChestBlockEntity && block instanceof ChestBlock) {
                Container chestContainer = ChestBlock.getContainer((ChestBlock) block, state, pLevel, pos, true);
                assert chestContainer != null;
                return Optional.of(chestContainer);
            }else{
                return Optional.of(container);
            }
        }
        return Optional.empty();
    }

    private boolean isCanInsertToInventory(Container container, ItemStack itemStack) {
        boolean flag = false;
        for(int i=0;i<container.getContainerSize();i++){
            if(container.canPlaceItem(i, itemStack)){
                ItemStack currentStack = container.getItem(i);
                if(currentStack.isEmpty()){
                    container.setItem(i, itemStack);
                    flag = true;
                    break;
                }
                else if(canMergeItems(currentStack, itemStack)){
                    currentStack.grow(itemStack.getCount());
                    container.setItem(i, currentStack);
                    flag = true;
                    break;
                }
            }
        }
        return flag;
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

    private boolean canMergeItems(ItemStack stack1, ItemStack stack2) {
        return stack1.getCount() + stack2.getCount() <= stack1.getMaxStackSize() && ItemStack.isSameItemSameComponents(stack1, stack2);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        boolean isEnabled = pState.getValue(BasicQuarryBlock.ENABLED);
        if(!isEnabled) {
            this.data.set(ERROR_CODE_DATA_SLOT, CODE_PAUSE);
            pLevel.setBlock(pPos, pState.setValue(BasicQuarryBlock.ON, Boolean.FALSE), 2);
            return;
        }

        if(this.burnTime <= 0){
            if(consumeBurnItem(pLevel)){
                this.data.set(ERROR_CODE_DATA_SLOT, CODE_NORMAL);
            }else{
                this.data.set(ERROR_CODE_DATA_SLOT, CODE_NOT_ENOUGH_FUEL);
                pLevel.setBlock(pPos, pState.setValue(BasicQuarryBlock.ON, Boolean.FALSE), 2);
            }
        }
        else{
            Optional<Container> topContainer = getTopContainer(pLevel, pPos, pState);
            if(topContainer.isPresent()) {
                if(errorCode == CODE_NORMAL)
                    this.burnTime--;
                this.cooldown--;

                if(!isOnCooldown()){

                    // Check for upgrade items
                    Optional<List<BaseUpgradeItem>> upgrades = getUpgradeItem();
                    if(upgrades.isPresent()){
                        double speedUsage = 0;
                        for(BaseUpgradeItem upgrade : upgrades.get()){
                            if(upgrade.getUpgradeType() == UpgradeType.SPEED){
                                speedUsage += JankyStuffCommonConfig.QUARRY_COOLDOWN.get() * upgrade.speedUsage();
                            }
                        }

                        int finalSpeed = Math.max(1, JankyStuffCommonConfig.QUARRY_COOLDOWN.get() - (int)speedUsage);
                        setCooldown(finalSpeed);
                    }
                    else{
                        setCooldown(JankyStuffCommonConfig.QUARRY_COOLDOWN.get());
                    }

                    // This might cause lag issue
                    List<ItemStack> stacks = TagUtil.getCommonResources();
                    ItemStack itemToGet = ItemStackUtil.getRandomItem(stacks);

                    if(!isCanInsertToInventory(topContainer.get(),itemToGet)){
                        this.data.set(ERROR_CODE_DATA_SLOT, CODE_NOT_ENOUGH_SPACE);
                        pState = pState.setValue(BasicQuarryBlock.ON, Boolean.FALSE);
                    }else{
                        //JankyStuff.LOGGER.debug("Quarry get: {}", itemToGet);
                        this.data.set(ERROR_CODE_DATA_SLOT, CODE_NORMAL);
                        pState = pState.setValue(BasicQuarryBlock.ON, Boolean.TRUE);
                        setChanged(pLevel, pPos, pState);
                    }
                    pLevel.setBlock(pPos, pState, 3);
                }
            }
            else
            {
                this.data.set(ERROR_CODE_DATA_SLOT, CODE_MISSING_TOP_INVENTORY);
                pState = pState.setValue(BasicQuarryBlock.ON, Boolean.FALSE);
                pLevel.setBlock(pPos, pState, 3);
            }

        }
    }

    // IUpgradable
    @Override
    public List<Integer> upgradeSlotsIndex() {
        int slot1 = containerType.getSize() - 3;
        int slot2 = containerType.getSize() - 2;
        int slot3 = containerType.getSize() - 1;
        return List.of(slot1, slot2, slot3);
    }

    @Override
    public boolean canAccept(int slot, UpgradeType upgradeType) {
        return upgradeType == UpgradeType.SPEED || upgradeType == UpgradeType.ENERGY;
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

    @Override
    public int upgradeSlotCount() {
        return 3;
    }
}
