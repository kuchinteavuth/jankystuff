package com.inteavuthkuch.jankystuff.blockentity;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.IBlockEntityTicker;
import com.inteavuthkuch.jankystuff.component.ModComponentTypes;
import com.inteavuthkuch.jankystuff.config.JankyStuffCommonConfig;
import com.inteavuthkuch.jankystuff.menu.AdvancedQuarryMenu;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AdvancedQuarryBlockEntity extends BlockEntity implements IBlockEntityTicker, MenuProvider {
    private Iterator<BlockPos> blocksToMine = null;
    private List<BlockPos> allChunkPositions = null;
    private BlockCapabilityCache<IItemHandler, @Nullable Direction> cache;

    private int scanCursor = 0;
    private boolean noInventory;
    private boolean missingInventory;
    private boolean isPause;
    private boolean completedWork;
    private int progress;
    private int maxProgress;
    private boolean isDisabled;

    private long firstTickTime = -1;
    private final NonNullList<ItemStack> inventoryBuffer = NonNullList.create();
    private final ContainerData containerData;
    private final ItemStackHandler itemHandler;

    private static final int SCAN_BATCH_SIZE = 200;
    private static final int BLOCK_PER_TICK = 4;
    private static final int STARTUP_DELAY = 80;

    // FOR DATA INDEX
    public static final int NO_INVENTORY = 0;
    public static final int MISSION_INVENTORY = 1;
    public static final int IS_PAUSE = 2;
    public static final int PROGRESS = 3;
    public static final int MAX_PROGRESS = 4;
    public static final int COMPLETED_WORK = 5;
    public static final int IS_DISABLED = 6;

    public AdvancedQuarryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.ADVANCED_QUARRY_BE.get(), pPos, pBlockState);
        containerData = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> AdvancedQuarryBlockEntity.this.noInventory ? 1 : 0;
                    case 1 -> AdvancedQuarryBlockEntity.this.missingInventory ? 1 : 0;
                    case 2 -> AdvancedQuarryBlockEntity.this.isPause ? 1 : 0;
                    case 3 -> AdvancedQuarryBlockEntity.this.progress;
                    case 4 -> AdvancedQuarryBlockEntity.this.maxProgress;
                    case 5 -> AdvancedQuarryBlockEntity.this.completedWork ? 1 : 0;
                    case 6 -> AdvancedQuarryBlockEntity.this.isDisabled ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> AdvancedQuarryBlockEntity.this.noInventory = (value == 1);
                    case 1 -> AdvancedQuarryBlockEntity.this.missingInventory = (value == 1);
                    case 2 -> AdvancedQuarryBlockEntity.this.isPause = (value == 1);
                    case 3 -> AdvancedQuarryBlockEntity.this.progress = value;
                    case 4 -> AdvancedQuarryBlockEntity.this.maxProgress = value;
                    case 5 -> AdvancedQuarryBlockEntity.this.completedWork = (value == 1);
                    case 6 -> AdvancedQuarryBlockEntity.this.isDisabled = (value == 1);
                }
                AdvancedQuarryBlockEntity.this.setChanged();
                if (AdvancedQuarryBlockEntity.this.level != null) {
                    AdvancedQuarryBlockEntity.this.level.sendBlockUpdated(
                            AdvancedQuarryBlockEntity.this.getBlockPos(),
                            AdvancedQuarryBlockEntity.this.getBlockState(),
                            AdvancedQuarryBlockEntity.this.getBlockState(),
                            3
                    );
                }

            }

            @Override
            public int getCount() {
                return 7;
            }
        };
        this.itemHandler = new ItemStackHandler(9){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if(level != null && !level.isClientSide()){
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
                }
            }
        };
    }

    public IItemHandler getItemHandler() {
        return this.itemHandler;
    }

    private void initialScan(Level level, BlockPos quarryPos) {
        LevelChunk currentChunk = level.getChunkAt(quarryPos);
        ChunkPos chunkPos = currentChunk.getPos();
        int minY = currentChunk.getMinBuildHeight() + 1;
        int maxY = quarryPos.getY() - 1;

        allChunkPositions = new ArrayList<>();
        BlockPos quarryBaseStart = quarryPos.offset(-1, -1, -1);
        BlockPos quarryBaseEnd = quarryPos.offset(1, 0, 1);

        for (int x = chunkPos.getMinBlockX(); x <= chunkPos.getMaxBlockX(); x++) {
            for (int z = chunkPos.getMinBlockZ(); z <= chunkPos.getMaxBlockZ(); z++) {
                for (int y = maxY; y >= minY; y--) {
                    // ⛔ Skip blocks inside the 3×1×3 bounding box
                    if (x >= quarryBaseStart.getX() && x <= quarryBaseEnd.getX() &&
                            y >= quarryBaseStart.getY() && y <= quarryBaseEnd.getY() &&
                            z >= quarryBaseStart.getZ() && z <= quarryBaseEnd.getZ()) {
                        continue;
                    }

                    allChunkPositions.add(new BlockPos(x, y, z));
                    maxProgress = allChunkPositions.size();
                }
            }
        }

        JankyStuff.LOGGER.info("Scan cursor: {}, total: {}" ,scanCursor, allChunkPositions.size());
    }

    private List<BlockPos> scanForNextBatch(){
        if (allChunkPositions == null || scanCursor >= allChunkPositions.size()) return Collections.emptyList();

        int end = Math.min(scanCursor + SCAN_BATCH_SIZE, allChunkPositions.size());
        List<BlockPos> batch = allChunkPositions.subList(scanCursor, end);
        scanCursor = end;
        return batch;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("ScanCursor", scanCursor);
        pTag.putBoolean("NoInventory", noInventory);
        pTag.putBoolean("CompletedWork", completedWork);
        pTag.putBoolean("MissingInventory", missingInventory);
        pTag.putBoolean("IsPause", isPause);
        pTag.putInt("Progress", progress);
        pTag.putInt("MaxProgress", maxProgress);
        pTag.putBoolean("IsDisabled", isDisabled);

        CompoundTag nbt = new CompoundTag();
        ContainerHelper.saveAllItems(nbt, inventoryBuffer, pRegistries);
        pTag.put("InventoryBuffer", nbt);

        CompoundTag inventoryNbt = itemHandler.serializeNBT(pRegistries);
        pTag.put("QuarryInventory", inventoryNbt); // Prepare for filter items
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        scanCursor = pTag.getInt("ScanCursor");
        noInventory = pTag.getBoolean("NoInventory");
        completedWork = pTag.getBoolean("CompletedWork");
        missingInventory = pTag.getBoolean("MissingInventory");
        isPause = pTag.getBoolean("IsPause");
        progress = pTag.getInt("Progress");
        maxProgress = pTag.getInt("MaxProgress");
        isDisabled = pTag.getBoolean("IsDisabled");

        CompoundTag nbt = pTag.getCompound("InventoryBuffer");
        ContainerHelper.loadAllItems(nbt, inventoryBuffer, pRegistries);

        CompoundTag inventoryNbt = pTag.getCompound("QuarryInventory");
        itemHandler.deserializeNBT(pRegistries, inventoryNbt);
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
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
    }

    @Override
    public void onLoad() {
        if(level == null || level.isClientSide()) return;
        blocksToMine = null;

        if(allChunkPositions == null)
            initialScan(level, worldPosition);

        if(cache == null){
            cache = BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, (ServerLevel) level, worldPosition.above(), Direction.DOWN);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        blocksToMine = null;
    }

    public boolean isNoInventory() {
        return containerData.get(NO_INVENTORY) > 0;
    }
    public boolean isMissionInventory() {
        return containerData.get(MISSION_INVENTORY) > 0;
    }
    public boolean isPause() {
        return containerData.get(IS_PAUSE) > 0;
    }
    public boolean isCompletedWork() {
        return containerData.get(COMPLETED_WORK) > 0;
    }
    public boolean isDisabled() {
        return containerData.get(IS_DISABLED) > 0;
    }

    public void resetQuarry() {
        if(level == null || level.isClientSide()) return;
        blocksToMine = null;
        allChunkPositions = null;
        scanCursor = 0;
        completedWork = false;
        isPause = true;
    }

    private void mineBlock(Level pLevel, BlockPos pPos) {
        BlockState state = pLevel.getBlockState(pPos);
        FluidState fluidState = pLevel.getFluidState(pPos);

        if(!fluidState.isEmpty()){
            pLevel.destroyBlock(pPos, false);
            progress++;
            return;
        }

        if(!state.isEmpty() && !state.hasBlockEntity() && canBreak(pLevel, pPos, state)){

            LootParams.Builder lootBuilder = new LootParams.Builder((ServerLevel) pLevel)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pPos))
                    .withParameter(LootContextParams.TOOL, new ItemStack(Items.DIAMOND_PICKAXE))
                    .withParameter(LootContextParams.BLOCK_STATE, state)
                    .withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(pPos));

            List<ItemStack> droppedItems = state.getDrops(lootBuilder);
            IItemHandler itemHandler = cache.getCapability();

            for(ItemStack droppedItem : droppedItems) {
                ItemStack remaining = ItemHandlerHelper.insertItem(itemHandler, droppedItem, false);
                if(!remaining.isEmpty()){
                    inventoryBuffer.add(remaining);
                    noInventory = true;
                }
            }
            pLevel.destroyBlock(pPos, false);
            progress ++;
        }
    }

    private void onMiningCompleted() {
        completedWork = true;
        JankyStuff.LOGGER.info("Quarry is completed");
        containerData.set(COMPLETED_WORK, 1);
        if(level != null){
            level.playSound(
                    null, getBlockPos(),
                    SoundEvents.BEACON_ACTIVATE,
                    SoundSource.BLOCKS,
                    1.0F, 1.0F
            );
        }
    }

    private boolean canBreak(Level pLevel, BlockPos pPos, BlockState pState) {
        return !(pState.getBlock() instanceof LiquidBlock) || pState.getDestroySpeed(pLevel, pPos) < 0f;
    }

    public void drops(Level pLevel, BlockPos pPos) {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(pLevel, pPos, inventory);
    }

    public void setPause(boolean isPause) {
        this.isPause = isPause;
        containerData.set(IS_PAUSE, isPause ? 1 : 0);
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(!(pLevel instanceof ServerLevel serverLevel)) return;
        if(!serverLevel.isPositionEntityTicking(pPos)) return;

        if(JankyStuffCommonConfig.DISABLE_QUARRY.get()) {
            containerData.set(IS_DISABLED, 1);
            return;
        }else{
            containerData.set(IS_DISABLED, 0);
        }

        if(completedWork) return;
        if(isPause) return;

        long currentTime = pLevel.getGameTime();
        if (firstTickTime == -1) firstTickTime = currentTime;
        if (currentTime - firstTickTime < STARTUP_DELAY) {
            return; // ⛔ Wait until delay passes
        }


        if(!inventoryBuffer.isEmpty()){
            for(int i=0; i<inventoryBuffer.size();i++) {
                ItemStack itemStack = inventoryBuffer.get(i);
                ItemStack remaining = ItemHandlerHelper.insertItem(cache.getCapability(), itemStack, false);
                if (remaining.isEmpty()) {
                    inventoryBuffer.remove(i);
                    i--;
                } else {
                    inventoryBuffer.set(i, remaining);
                    noInventory = true;
                    containerData.set(NO_INVENTORY, 1);
                }
            }
        }

        if(inventoryBuffer.isEmpty())
        {
            noInventory = false;
            containerData.set(NO_INVENTORY, 0);
        }

        if(cache == null){
            cache = BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, (ServerLevel) level, worldPosition.above(), Direction.DOWN);
        }

        if(cache.getCapability() == null)
        {
            containerData.set(MISSION_INVENTORY, 1);
            return;
        }
        else{
            containerData.set(MISSION_INVENTORY, 0);
        }

        // For safe
        if(allChunkPositions == null)
            initialScan(pLevel, pPos);

        if(blocksToMine == null || !blocksToMine.hasNext()){
            List<BlockPos> blockPosList = scanForNextBatch();
            JankyStuff.LOGGER.debug("Getting next block batch: {}", blockPosList.size());

            if (blockPosList.isEmpty()) {
                blocksToMine = null;
                allChunkPositions = null;
                scanCursor = 0;
                onMiningCompleted(); // ✅ Trigger completion
                return;
            }else{
                blocksToMine = blockPosList.iterator();
            }
        }

        if(noInventory){
            if(level.getGameTime() % 20 == 0){
                JankyStuff.LOGGER.debug("Inventory above was full");
            }
            return;
        }

        for(int i = 0; i < BLOCK_PER_TICK; i++) {
            if(blocksToMine.hasNext()){
                BlockPos pos = blocksToMine.next();
                if(level.getGameTime() % 100 == 0){
                    JankyStuff.LOGGER.debug("Current Position: {}", pos);
                }
                mineBlock(pLevel, pos);
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.jankystuff.advanced_quarry");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new AdvancedQuarryMenu(i, inventory, this, this.containerData);
    }
}
