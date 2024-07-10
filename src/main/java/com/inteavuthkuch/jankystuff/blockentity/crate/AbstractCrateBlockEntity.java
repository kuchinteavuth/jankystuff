package com.inteavuthkuch.jankystuff.blockentity.crate;

import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.util.ComponentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCrateBlockEntity extends BaseContainerBlockEntity {

    protected NonNullList<ItemStack> items;
    private final ContainerType containerType;

    protected AbstractCrateBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, ContainerType containerType) {
        super(pType, pPos, pBlockState);
        this.items = NonNullList.withSize(containerType.getSize(), ItemStack.EMPTY);
        this.containerType = containerType;
    }


    protected void playSound(BlockPos pPos, SoundEvent pSound) {
        assert this.level != null;
        this.level.playSound(null, pPos, pSound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected Component getDefaultName() {
        return ComponentUtil.translateBlock(containerType.getId());
    }

    @Override
    public void startOpen(Player pPlayer) {
        this.playSound(this.getBlockPos(), SoundEvents.BARREL_OPEN);
    }

    @Override
    public void stopOpen(Player pPlayer) {
        super.stopOpen(pPlayer);
        this.playSound(this.getBlockPos(), SoundEvents.BARREL_CLOSE);
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
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, this.items, pRegistries);
    }
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        ContainerHelper.saveAllItems(pTag, this.items, pRegistries);
    }
    @Override
    public int getContainerSize() {
        return this.containerType.getSize();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

}
