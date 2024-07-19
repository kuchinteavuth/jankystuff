package com.inteavuthkuch.jankystuff.capability;

import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.blockentity.TheVoidBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.crate.AbstractCrateBlockEntity;
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
                Capabilities.ItemHandler.BLOCK,
                TheVoidBlockEntity::handleItem,
                ModBlocks.THE_VOID.get()
        );

        event.registerBlock(
                Capabilities.FluidHandler.BLOCK,
                TheVoidBlockEntity::handleFluid,
                ModBlocks.THE_VOID.get()
        );
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(CapabilityHandler::registerCapabilities);
    }
}
