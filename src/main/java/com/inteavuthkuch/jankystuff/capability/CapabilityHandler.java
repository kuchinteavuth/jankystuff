package com.inteavuthkuch.jankystuff.capability;

import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.blockentity.crate.AbstractCrateBlockEntity;
import com.inteavuthkuch.jankystuff.inventory.BasicQuarryInvWrapper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class CapabilityHandler {

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(
                Capabilities.ItemHandler.BLOCK,
                (level, pos, state, be, side) -> new InvWrapper((AbstractCrateBlockEntity)be),
                ModBlocks.WOODEN_CRATE.get()
        );

        event.registerBlock(
                Capabilities.ItemHandler.BLOCK,
                (level, pos, state, be, side) -> new InvWrapper((AbstractCrateBlockEntity)be),
                ModBlocks.METAL_CRATE.get()
        );

        event.registerBlock(
                Capabilities.ItemHandler.BLOCK,
                BasicQuarryInvWrapper::create,
                ModBlocks.BASIC_QUARRY.get()
        );
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(CapabilityHandler::registerCapabilities);
    }
}
