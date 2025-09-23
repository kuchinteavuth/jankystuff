package com.inteavuthkuch.jankystuff.blockentity;

import com.inteavuthkuch.jankystuff.block.IBlockEntityTicker;
import com.inteavuthkuch.jankystuff.block.Skyshifter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SkyShifterBlockEntity extends BlockEntity implements IBlockEntityTicker {

    private boolean doRun = false;
    private long targetTime;
    private int cooldown = 0;

    public SkyShifterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.SKY_SHIFTER_BE.get(), pPos, pBlockState);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder pComponents) {
        super.collectImplicitComponents(pComponents);
        if(level != null) {
            boolean dayMode = getBlockState().getValue(Skyshifter.DAY_MODE);
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("IsDayMode", dayMode);
            pComponents.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }

    public void updateTime(long targetTime) {
        this.targetTime = targetTime;
        this.doRun = true;
    }

    public boolean isInCooldown() {
        return cooldown > 0;
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(!doRun || level == null || level.isClientSide()) return;

        if(isInCooldown()){
            cooldown--;
            return;
        }

        ((ServerLevel) level).setDayTime(targetTime);
        ((ServerLevel) level).sendParticles(
                ParticleTypes.END_ROD,
                pPos.getX() + 0.5, pPos.getY() + 1.5, pPos.getZ() + 0.5,
                10, 0.2, 0.5, 0.2, 0.01
        );
        level.playSound(
                null, pPos,
                SoundEvents.BEACON_ACTIVATE,
                SoundSource.BLOCKS,
                1.0F, 1.0F
        );
        doRun = false;
        cooldown = 40;
    }
}
