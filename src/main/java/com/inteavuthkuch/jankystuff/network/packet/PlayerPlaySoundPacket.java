package com.inteavuthkuch.jankystuff.network.packet;

import com.inteavuthkuch.jankystuff.JankyStuff;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PlayerPlaySoundPacket(SoundEvent soundEvent, float volume, float pitch) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayerPlaySoundPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(JankyStuff.MOD_ID, "jankystuff_player_play_sound_packet")
    );

    public static final StreamCodec<ByteBuf, PlayerPlaySoundPacket> STREAM_CODEC = StreamCodec.composite(
            SoundEvent.DIRECT_STREAM_CODEC,
            PlayerPlaySoundPacket::soundEvent,
            ByteBufCodecs.FLOAT,
            PlayerPlaySoundPacket::volume,
            ByteBufCodecs.FLOAT,
            PlayerPlaySoundPacket::pitch,
            PlayerPlaySoundPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleClient(PlayerPlaySoundPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
                Player player = context.player();
                player.playSound(packet.soundEvent(), packet.volume(), packet.pitch());
        })
        .exceptionally(ex -> {
            context.disconnect(Component.translatable("networking.jankystuff.failed", ex.getMessage()));
            return null;
        });
    }
}
