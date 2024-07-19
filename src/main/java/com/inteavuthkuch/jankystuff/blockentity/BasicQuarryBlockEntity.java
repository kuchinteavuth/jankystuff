package com.inteavuthkuch.jankystuff.blockentity;

import com.google.common.collect.AbstractIterator;
import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.BasicQuarryBlock;
import com.inteavuthkuch.jankystuff.block.IBlockEntityTicker;
import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.common.UpgradeType;
import com.inteavuthkuch.jankystuff.config.JankyStuffCommonConfig;
import com.inteavuthkuch.jankystuff.item.upgrade.BaseUpgradeItem;
import com.inteavuthkuch.jankystuff.menu.BasicQuarryMenu;
import com.inteavuthkuch.jankystuff.util.ComponentUtil;
import com.inteavuthkuch.jankystuff.util.ContainerUtil;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BasicQuarryBlockEntity extends BaseContainerBlockEntity implements IUpgradableBlockEntity, IBlockEntityTicker {

    private NonNullList<ItemStack> items;
    private final ContainerType containerType;
    private final ContainerData data;

    private int burnTime = 0;
    private int errorCode;
    private int cooldown = -1;
    private boolean waiting;
    private Iterator<BlockPos> blocksToMine = null;
    private List<ItemStack> itemBuffer = null;

    public static final int CODE_NORMAL = 0;
    public static final int CODE_MISSING_TOP_INVENTORY = 1;
    public static final int CODE_NOT_ENOUGH_SPACE = 2;
    public static final int CODE_NOT_ENOUGH_FUEL = 3;
    public static final int CODE_PAUSE = 4;
    public static final int CODE_FINISHED = 5;

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

        for (int i=0; i<items.size(); i++) {
            ItemStack item = items.get(i);
            if (FurnaceBlockEntity.isFuel(item)) {
                this.burnTime = item.getBurnTime(null);
                if(item.hasCraftingRemainingItem()){
                    items.set(i, item.getCraftingRemainingItem());
                    this.setChanged();
                }else{
                    item.shrink(1);
                }
                return true;
            }
        }
        return false;
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

    private void checkForUpgrade() {
        Optional<List<BaseUpgradeItem>> upgrades = getUpgradeItem();
        if(upgrades.isPresent()){
            double speedUsage = 0;
            for(BaseUpgradeItem upgrade : upgrades.get()){
                if(upgrade.getUpgradeType() == UpgradeType.SPEED){
                    speedUsage += JankyStuffCommonConfig.QUARRY_COOLDOWN.get() * upgrade.speedUsage();
                }
            }

            int finalSpeed = Math.max(0, JankyStuffCommonConfig.QUARRY_COOLDOWN.get() - (int)speedUsage);
            setCooldown(finalSpeed);
        }
        else{
            setCooldown(JankyStuffCommonConfig.QUARRY_COOLDOWN.get());
        }
    }

    private boolean canBeBreak(Level level, BlockState state, BlockPos pos) {
        return !(state.getBlock() instanceof LiquidBlock) || state.getDestroySpeed(level, pos) < 0f;
    }

    private Iterable<BlockPos> scanChunkForMine(Level pLevel, BlockPos pPos) {
        LevelChunk currentChunk = pLevel.getChunkAt(pPos);
        ChunkPos chunkPos = currentChunk.getPos();
        final int maxY = pPos.getY() - 1; // below quarry
        final int minY = currentChunk.getMinBuildHeight() + 1;
        BlockPos pos1 = new BlockPos(chunkPos.x * 16 , maxY, chunkPos.z * 16);
        BlockPos pos2 = new BlockPos(pos1.getX() + 15, minY, pos1.getZ() + 15);

        return BlockPos.betweenClosed(pos1, pos2);
    }

    private void startQuarry(@NotNull Container container, Level pLevel, BlockState pState, BlockPos pPos) {

        if(errorCode == CODE_FINISHED) return; // need to break and put back again

        if(blocksToMine == null){
            blocksToMine = scanChunkForMine(pLevel, pPos).iterator();
        }

        if(blocksToMine.hasNext()){
            BlockPos pos = blocksToMine.next();
            BlockState state = pLevel.getBlockState(pos);
            FluidState fluid = pLevel.getFluidState(pPos);
            JankyStuff.LOGGER.debug("Pos: X:{},Y:{},Z:{}", pos.getX(), pos.getY(), pos.getZ());
            if(!fluid.isEmpty()){
                pLevel.destroyBlock(pPos, false);
            }
            if(!state.isEmpty() && !state.hasBlockEntity()){
                if(canBeBreak(pLevel, pState, pos)){
                    LootParams.Builder builder = new LootParams.Builder((ServerLevel) pLevel);
                    builder.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos));
                    builder.withParameter(LootContextParams.TOOL, new ItemStack(Items.DIAMOND_PICKAXE));
                    builder.withOptionalParameter(LootContextParams.BLOCK_ENTITY, pLevel.getBlockEntity(pos));
                    var items = state.getDrops(builder);
                    for(ItemStack item : items) {
                        if(ContainerUtil.canInsertItemStackSimulate(container, item)){
                            ContainerUtil.canInsertItemStack(container, item);
                        }
                        else{
                            if(this.itemBuffer == null)
                            {
                                this.itemBuffer = new ArrayList<>();
                            }

                            itemBuffer.add(item);
                            this.data.set(ERROR_CODE_DATA_SLOT, CODE_NOT_ENOUGH_SPACE);
                        }
                    }
                    pLevel.destroyBlock(pos, false);
                }
                checkForUpgrade();
            }
        }
        else
        {
            blocksToMine = null;
            this.data.set(ERROR_CODE_DATA_SLOT, CODE_FINISHED);
        }
    }


    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        var container = ContainerUtil.getContainerAt(pLevel, pPos.above());

        if(container.isPresent()) {

            this.data.set(ERROR_CODE_DATA_SLOT, pState.getValue(BasicQuarryBlock.ENABLED) ? CODE_NORMAL : CODE_PAUSE);
            if(this.errorCode == CODE_PAUSE) return;

            if(itemBuffer != null) {
                boolean canInsertFlag = true;
                for(ItemStack item : itemBuffer) {
                    boolean flag = ContainerUtil.canInsertItemStackSimulate(container.get(), item);
                    if(!flag){
                        canInsertFlag = false;
                        break;
                    }
                }

                if(canInsertFlag) {
                    for (ItemStack item : itemBuffer){
                        ContainerUtil.canInsertItemStack(container.get(), item.copy());
                    }
                    itemBuffer = null;
                    this.data.set(ERROR_CODE_DATA_SLOT, CODE_NORMAL);
                }else
                    return;
            }

            if(this.burnTime <= 0){
                if(consumeBurnItem(pLevel)){
                    this.data.set(ERROR_CODE_DATA_SLOT, CODE_NORMAL);
                }else{
                    this.data.set(ERROR_CODE_DATA_SLOT, CODE_NOT_ENOUGH_FUEL);
                    pState = pState.setValue(BasicQuarryBlock.ENABLED, false);
                    pLevel.setBlock(pPos, pState, 2);
                    return;
                }
            }

            burnTime--;

            if(!isOnCooldown()) {
                startQuarry(container.get(), pLevel, pState, pPos);
            }
            else {
                cooldown--;
            }

        }
        else{
            this.data.set(ERROR_CODE_DATA_SLOT, CODE_MISSING_TOP_INVENTORY);
            pState = pState.setValue(BasicQuarryBlock.ENABLED, Boolean.FALSE);
            pLevel.setBlock(pPos, pState, 2);
        }
    }


    /**
     *  Old implementation please check tick() method instead <br/>
     *  Might roll back and use old method again
     */
    @Deprecated
    public void tickServer(Level pLevel, BlockPos pPos, BlockState pState) {
        boolean isEnabled = pState.getValue(BasicQuarryBlock.ENABLED);
        if(!isEnabled) {
            this.data.set(ERROR_CODE_DATA_SLOT, CODE_PAUSE);
            pLevel.setBlock(pPos, pState.setValue(BasicQuarryBlock.ENABLED, Boolean.FALSE), 2);
            return;
        }

        if(this.burnTime <= 0){
            if(consumeBurnItem(pLevel)){
                this.data.set(ERROR_CODE_DATA_SLOT, CODE_NORMAL);
            }else{
                this.data.set(ERROR_CODE_DATA_SLOT, CODE_NOT_ENOUGH_FUEL);
            }
        }
        else{
            Optional<Container> topContainer = ContainerUtil.getContainerAt(pLevel, pPos.above());
            if(topContainer.isPresent()) {
                if(errorCode == CODE_NORMAL)
                    this.burnTime--;
                this.cooldown--;

                if(!isOnCooldown()){

                    // Check for upgrade items
                    checkForUpgrade();

                    List<ItemStack> stacks = TagUtil.getCommonResources();
                    ItemStack itemToGet = ItemStackUtil.getRandomItem(stacks);

                    if(!ContainerUtil.canInsertItemStack(topContainer.get(),itemToGet)){
                        this.data.set(ERROR_CODE_DATA_SLOT, CODE_NOT_ENOUGH_SPACE);
                        pState = pState.setValue(BasicQuarryBlock.ENABLED, Boolean.FALSE);
                    }else{
                        this.data.set(ERROR_CODE_DATA_SLOT, CODE_NORMAL);
                        pState = pState.setValue(BasicQuarryBlock.ENABLED, Boolean.TRUE);
                        setChanged(pLevel, pPos, pState);
                    }
                    pLevel.setBlock(pPos, pState, 2);
                }
            }
            else
            {
                this.data.set(ERROR_CODE_DATA_SLOT, CODE_MISSING_TOP_INVENTORY);
                pState = pState.setValue(BasicQuarryBlock.ENABLED, Boolean.FALSE);
                pLevel.setBlock(pPos, pState, 2);
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
        return this.containerType.getAdditionalSlot();
    }
}
