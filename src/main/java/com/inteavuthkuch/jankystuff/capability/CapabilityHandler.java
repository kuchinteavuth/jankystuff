package com.inteavuthkuch.jankystuff.capability;

import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.blockentity.WaterSourceBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.crate.AbstractCrateBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.custom.FarmSimulationBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.fluidtank.FluidTankBlockEntityBase;
import com.inteavuthkuch.jankystuff.inventory.BasicQuarryInvWrapper;
import com.inteavuthkuch.jankystuff.inventory.BlockBreakerInvWrapper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class CapabilityHandler {

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(
                Capabilities.ItemHandler.BLOCK,
                (level, pos, state, be, face) -> new InvWrapper((AbstractCrateBlockEntity)be),
                ModBlocks.WOODEN_CRATE.get()
        );

        event.registerBlock(
                Capabilities.ItemHandler.BLOCK,
                (level, pos, state, be, face) -> new InvWrapper((AbstractCrateBlockEntity)be),
                ModBlocks.METAL_CRATE.get()
        );

        event.registerBlock(
                Capabilities.ItemHandler.BLOCK,
                BasicQuarryInvWrapper::create,
                ModBlocks.BASIC_QUARRY.get()
        );

        event.registerBlock(
                Capabilities.ItemHandler.BLOCK,
                BlockBreakerInvWrapper::create,
                ModBlocks.BLOCK_BREAKER.get()
        );

        event.registerBlock(
                Capabilities.FluidHandler.BLOCK,
                FluidTankBlockEntityBase::getFluidHandler,
                ModBlocks.BASIC_FLUID_TANK.get(),
                ModBlocks.ADVANCED_FLUID_TANK.get(),
                ModBlocks.ELITE_FLUID_TANK.get(),
                ModBlocks.ULTIMATE_FLUID_TANK.get()
        );

        event.registerBlock(
                Capabilities.FluidHandler.BLOCK,
                WaterSourceBlockEntity::getCapability,
                ModBlocks.WATER_SOURCE.get()
        );

        event.registerBlock(
                Capabilities.ItemHandler.BLOCK,
                FarmSimulationBlockEntity::getCapability,
                ModBlocks.FARM_SIMULATION.get()
        );
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(CapabilityHandler::registerCapabilities);
    }
}
