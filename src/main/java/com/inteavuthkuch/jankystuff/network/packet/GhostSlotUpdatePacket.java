package com.inteavuthkuch.jankystuff.network.packet;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.menu.BasicItemFilterMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GhostSlotUpdatePacket(int index, ItemStack item) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<GhostSlotUpdatePacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(JankyStuff.MOD_ID, "jankystuff_ghost_slot_update")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, GhostSlotUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            GhostSlotUpdatePacket::index,
            ItemStack.OPTIONAL_STREAM_CODEC,
            GhostSlotUpdatePacket::item,
            GhostSlotUpdatePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleServer(GhostSlotUpdatePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            if(player.containerMenu instanceof BasicItemFilterMenu menu){
                Slot slot = menu.getSlot(packet.index());
                slot.set(packet.item());
                slot.container.setChanged();
                menu.broadcastChanges();
            }
        }).exceptionally(ex -> {
            context.disconnect(Component.translatable("networking.jankystuff.failed", ex.getMessage()));
            return null;
        });
    }
}
