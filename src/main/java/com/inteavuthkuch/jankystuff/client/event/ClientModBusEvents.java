package com.inteavuthkuch.jankystuff.client.event;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.client.KeyBinding;
import com.inteavuthkuch.jankystuff.menu.ModMenuType;
import com.inteavuthkuch.jankystuff.network.packet.ItemTogglePacket;
import com.inteavuthkuch.jankystuff.screen.BasicQuarryScreen;
import com.inteavuthkuch.jankystuff.screen.MetalCrateScreen;
import com.inteavuthkuch.jankystuff.screen.PortableCrateScreen;
import com.inteavuthkuch.jankystuff.screen.WoodenCrateScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = JankyStuff.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientModBusEvents {
    @SubscribeEvent
    public static void onKeysRegister(RegisterKeyMappingsEvent event) {
        event.register(KeyBinding.TOGGLE_MAGNET);
        event.register(KeyBinding.OPEN_PERSONAL_CRATE);
    }

    @SubscribeEvent
    public static void onScreensRegister(RegisterMenuScreensEvent event) {
        event.register(ModMenuType.WOODEN_CRATE.get(), WoodenCrateScreen::new);
        event.register(ModMenuType.METAL_CRATE.get(), MetalCrateScreen::new);
        event.register(ModMenuType.PORTABLE_CRATE.get(), PortableCrateScreen::new);
        event.register(ModMenuType.BASIC_QUARRY.get(), BasicQuarryScreen::new);
    }

    @SubscribeEvent
    public static void onPacketsRegister(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                ItemTogglePacket.TYPE,
                ItemTogglePacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ItemTogglePacket::handleClientPacket,
                        ItemTogglePacket::handleServerPacket
                )
        );
    }
}
