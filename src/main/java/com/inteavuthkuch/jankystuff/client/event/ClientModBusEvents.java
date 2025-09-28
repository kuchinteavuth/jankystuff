package com.inteavuthkuch.jankystuff.client.event;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.client.KeyBinding;
import com.inteavuthkuch.jankystuff.menu.ModMenuType;
import com.inteavuthkuch.jankystuff.network.packet.*;
import com.inteavuthkuch.jankystuff.renderer.AdvancedQuarryBlockEntityRenderer;
import com.inteavuthkuch.jankystuff.screen.*;
import com.inteavuthkuch.jankystuff.screen.custom.FarmSimulationScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = JankyStuff.MOD_ID, value = Dist.CLIENT)
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
        event.register(ModMenuType.BLOCK_BREAKER.get(), BlockBreakerScreen::new);
        event.register(ModMenuType.ADVANCED_QUARRY.get(), AdvancedQuarryScreen::new);
        event.register(ModMenuType.BASIC_ITEM_FILTER.get(), BasicItemFilterScreen::new);
        event.register(ModMenuType.FARM_SIMULATION.get(), FarmSimulationScreen::new);
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

        registrar.playToClient(
                PlaySoundPacket.TYPE,
                PlaySoundPacket.STREAM_CODEC,
                PlaySoundPacket::handleClientSidePacket
        );

        registrar.playToServer(
                BlockEntityPacket.TYPE,
                BlockEntityPacket.STREAM_CODEC,
                BlockEntityPacket::handleServer
        );

        registrar.playToServer(
                GhostSlotUpdatePacket.TYPE,
                GhostSlotUpdatePacket.STREAM_CODEC,
                GhostSlotUpdatePacket::handleServer);

        registrar.playToClient(
                PlayerPlaySoundPacket.TYPE,
                PlayerPlaySoundPacket.STREAM_CODEC,
                PlayerPlaySoundPacket::handleClient);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntity.ADVANCED_QUARRY_BE.get(), AdvancedQuarryBlockEntityRenderer::new);
    }
}
