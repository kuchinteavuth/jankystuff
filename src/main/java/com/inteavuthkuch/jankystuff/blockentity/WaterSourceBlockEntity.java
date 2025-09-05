package com.inteavuthkuch.jankystuff.blockentity;

import com.inteavuthkuch.jankystuff.block.IBlockEntityTicker;
import com.inteavuthkuch.jankystuff.block.WaterSourceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

public class WaterSourceBlockEntity extends BlockEntity implements IBlockEntityTicker {
    private final FluidTank fluidTank;
    private static final int EXPORT_INTERVAL = 10;
    private final Map<Direction, BlockCapabilityCache<IFluidHandler, @Nullable Direction>> capabilityCacheMap = new HashMap<>();

    public WaterSourceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.WATER_SOURCE_BE.get(), pPos, pBlockState);
        fluidTank = new FluidTank(Integer.MAX_VALUE, fluid -> fluid.is(Fluids.WATER)){
            @ParametersAreNonnullByDefault
            @Override
            public int fill(FluidStack resource, FluidAction action) {
                if(!this.validator.test(resource)) return 0;
                return resource.getAmount();
            }
            @ParametersAreNonnullByDefault
            @Override
            public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
                if(maxDrain <= 0) return FluidStack.EMPTY;
                return this.fluid.copyWithAmount(maxDrain);
            }

            @Override
            public @NotNull FluidStack getFluid() {
                return new FluidStack(Fluids.WATER, Integer.MAX_VALUE);
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
        fluidTank.setFluid(new FluidStack(Fluids.WATER, Integer.MAX_VALUE));
    }

    public IFluidHandler getFluidTank() {
        return fluidTank;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Nullable
    public static IFluidHandler getCapability(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity, @Nullable Direction direction) {
        return blockEntity instanceof WaterSourceBlockEntity waterSource
                ? waterSource.getFluidTank()
                : null;
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(pLevel instanceof ServerLevel serverLevel) {
            if (serverLevel.getGameTime() % EXPORT_INTERVAL != 0) return;

            if(pState.getValue(WaterSourceBlock.AUTO_EXPORT)) {
                for(Direction direction : Direction.values()) {
                    BlockCapabilityCache<IFluidHandler, @Nullable Direction> cache = capabilityCacheMap.computeIfAbsent(
                            direction,
                            dir -> BlockCapabilityCache.create(Capabilities.FluidHandler.BLOCK, serverLevel, pPos.relative(dir), dir.getOpposite())
                    );

                    IFluidHandler fluidHandler = cache.getCapability();
                    if(fluidHandler != null) {
                        for(int i = 0; i < fluidHandler.getTanks(); i++) {
                            int tankCapacity = fluidHandler.getTankCapacity(i);
                            FluidStack water = new FluidStack(Fluids.WATER, tankCapacity);
                            if(fluidHandler.isFluidValid(i, water)){
                                fluidHandler.fill(water, IFluidHandler.FluidAction.EXECUTE);
                            }
                        }
                    }
                }
            }
        }
    }
}
