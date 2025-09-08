package com.inteavuthkuch.jankystuff.blockentity.fluidtank;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.capability.JankyFluidTank;
import com.inteavuthkuch.jankystuff.common.FluidTankTier;
import com.inteavuthkuch.jankystuff.component.ModComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class FluidTankBlockEntityBase extends BlockEntity {
    private final FluidTankTier tankTier;
    private FluidTank fluidTank;

    public FluidTankBlockEntityBase(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, @NotNull FluidTankTier tankTier) {
        super(pType, pPos, pBlockState);
        this.tankTier = tankTier;
        fluidTank = new JankyFluidTank(tankTier, this::setChanged);
    }

    public FluidTankTier getTankTier() {
        return tankTier;
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    @Nullable
    public static FluidTank getFluidHandler(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity, Direction direction) {
        if(blockEntity instanceof FluidTankBlockEntityBase tank)
            return tank.getFluidTank();
        return null;
    }

    @ParametersAreNonnullByDefault
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        fluidTank.writeToNBT(pRegistries, pTag);
    }

    @ParametersAreNonnullByDefault
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        fluidTank = new JankyFluidTank(tankTier, this::setChanged);
        fluidTank.readFromNBT(pRegistries, pTag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void applyImplicitComponents(@NotNull DataComponentInput pComponentInput) {
        super.applyImplicitComponents(pComponentInput);
        CustomData data =  pComponentInput.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (level != null) {
            CompoundTag tag = data.copyTag();
            fluidTank.readFromNBT(level.registryAccess(), tag);
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder pComponents) {
        super.collectImplicitComponents(pComponents);
        if (level != null && !fluidTank.getFluid().isEmpty()) {
            CompoundTag tag = fluidTank.writeToNBT(level.registryAccess(), new CompoundTag());
            pComponents.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }
}
