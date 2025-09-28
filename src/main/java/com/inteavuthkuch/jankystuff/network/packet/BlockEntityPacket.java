package com.inteavuthkuch.jankystuff.network.packet;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.blockentity.AdvancedQuarryBlockEntity;
import com.inteavuthkuch.jankystuff.common.JankyBlockEntityType;
import com.inteavuthkuch.jankystuff.common.QuarryActionCode;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BlockEntityPacket(BlockPos pos, int code, int blockEntityType) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BlockEntityPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(JankyStuff.MOD_ID, "jankystuff_block_entity_packet")
    );

    public static final StreamCodec<ByteBuf, BlockEntityPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            BlockEntityPacket::pos,
            ByteBufCodecs.INT,
            BlockEntityPacket::code,
            ByteBufCodecs.INT,
            BlockEntityPacket::blockEntityType,
            BlockEntityPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleServer(BlockEntityPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerLevel level = (ServerLevel) context.player().level();
            BlockEntity blockEntity = level.getBlockEntity(packet.pos());

            if(JankyBlockEntityType.get(packet.blockEntityType()) == JankyBlockEntityType.ADVANCED_QUARRY) {
                if(blockEntity instanceof AdvancedQuarryBlockEntity quarry) {
                    switch (packet.code()){
                        case QuarryActionCode.PAUSE -> quarry.setPause(true);
                        case QuarryActionCode.RESUME -> quarry.setPause(false);
                        case QuarryActionCode.RESET -> quarry.resetQuarry();
                        default -> JankyStuff.LOGGER.warn("Unknown code: {}", packet.code());
                    }
                }
            }
        }).exceptionally(ex -> {
            context.disconnect(Component.translatable("networking.jankystuff.failed", ex.getMessage()));
            return null;
        });
    }
}
