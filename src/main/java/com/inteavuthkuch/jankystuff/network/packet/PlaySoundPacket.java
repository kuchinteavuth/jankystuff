package com.inteavuthkuch.jankystuff.network.packet;

import com.inteavuthkuch.jankystuff.JankyStuff;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public record PlaySoundPacket(BlockPos blockPos, SoundEvent soundEvent, String soundSourceName, float volume, float pitch)
implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<PlaySoundPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(JankyStuff.MOD_ID, "janky_play_sound_packet")
    );

    public static final StreamCodec<ByteBuf, PlaySoundPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            PlaySoundPacket::blockPos,
            SoundEvent.DIRECT_STREAM_CODEC,
            PlaySoundPacket::soundEvent,
            ByteBufCodecs.STRING_UTF8,
            PlaySoundPacket::soundSourceName,
            ByteBufCodecs.FLOAT,
            PlaySoundPacket::volume,
            ByteBufCodecs.FLOAT,
            PlaySoundPacket::pitch,
            PlaySoundPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleClientSidePacket(PlaySoundPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();

            SoundSource soundSource = Arrays.stream(SoundSource.values())
                    .filter(s -> s.getName().equals(packet.soundSourceName))
                    .findFirst()
                    .orElse(SoundSource.MASTER);

            LocalPlayer localPlayer = minecraft.player;
            ClientLevel level = minecraft.level;

            if(level != null)
                level.playSound(localPlayer, packet.blockPos, packet.soundEvent, soundSource, packet.volume, packet.pitch);
        })
        .exceptionally(ex -> {
            context.disconnect(Component.translatable("networking.jankystuff.failed", ex.getMessage()));
            return null;
        });
    }
}
