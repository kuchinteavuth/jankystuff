package com.inteavuthkuch.jankystuff.network.packet;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.integration.CuriosIntegration;
import com.inteavuthkuch.jankystuff.integration.ExternalMod;
import com.inteavuthkuch.jankystuff.inventory.SaveDataContainer;
import com.inteavuthkuch.jankystuff.item.portable.MagnetItem;
import com.inteavuthkuch.jankystuff.item.portable.PortableCrateItem;
import com.inteavuthkuch.jankystuff.menu.PortableCrateMenu;
import com.inteavuthkuch.jankystuff.util.ComponentUtil;
import com.inteavuthkuch.jankystuff.util.PlayerUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record ItemTogglePacket(int itemType, int slot) implements CustomPacketPayload {

    public static final int TOGGLE_MAGNET = 0;
    public static final int TOGGLE_PORTABLE_CRATE = 1;

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ItemTogglePacket.TYPE;
    }

    public static ItemTogglePacket createInstance(int type, int slot) {
        return new ItemTogglePacket(type, slot);
    }

    public static final CustomPacketPayload.Type<ItemTogglePacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(JankyStuff.MODID, "janky_item_toggle_packet")
    );

    public static final StreamCodec<ByteBuf, ItemTogglePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ItemTogglePacket::itemType,
            ByteBufCodecs.INT,
            ItemTogglePacket::slot,
            ItemTogglePacket::new
    );

    public static void handleClientPacket(ItemTogglePacket packet, IPayloadContext context) {

    }

    public static void handleServerPacket(ItemTogglePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (packet.itemType == TOGGLE_MAGNET) {
                handleMagnet(context.player());
            }
            else if(packet.itemType == TOGGLE_PORTABLE_CRATE){
                handlePersonalCrate(context.player());
            }
        })
        .exceptionally(ex -> {
               context.disconnect(Component.translatable("networking.jankystuff.failed", ex.getMessage()));
               return null;
        });
    }

    private static @NotNull MenuProvider getMenuProvider(ItemStack item) {
        SaveDataContainer container = new SaveDataContainer(ContainerType.PORTABLE, item);
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return ComponentUtil.translateItem(ContainerType.PORTABLE.getId());
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
                return new PortableCrateMenu(pContainerId, pPlayerInventory, container);
            }
        };
    }

    private static void handleMagnet(Player serverPlayer) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        boolean curioFlag = false;

        if(ExternalMod.CURIOS.isLoaded()){
            Optional<List<ItemStack>> curios = CuriosIntegration.getEquippedCurios(serverPlayer);
            if(curios.isPresent()){
                for(ItemStack item : curios.get()){
                    if(item.getItem() instanceof MagnetItem) {
                        MagnetItem.toggleMagnet(localPlayer, item, true);
                        curioFlag = true;
                        break;
                    }
                }
            }
        }

        if(!curioFlag){
            NonNullList<ItemStack> items = serverPlayer.getInventory().items;
            for(ItemStack item : items) {
                if(item.getItem() instanceof MagnetItem) {
                    // current player on client, idk but using client player so that I can play sound since server player doesn't work
                    MagnetItem.toggleMagnet(localPlayer, item, true);
                    break;
                }
            }
        }
    }

    private static void handlePersonalCrate(Player serverPlayer) {
        boolean flag = false;

        if(ExternalMod.CURIOS.isLoaded()){
            Optional<List<ItemStack>> curios = CuriosIntegration.getEquippedCurios(serverPlayer);
            if(curios.isPresent()){
                for(ItemStack item : curios.get()){
                    if(item.getItem() instanceof PortableCrateItem) {
                        MenuProvider menu = getMenuProvider(item);
                        PlayerUtil.tryOpenMenu(serverPlayer, menu);
                        flag = true;
                        break;
                    }
                }
            }
        }

        if(!flag){
            NonNullList<ItemStack> items = serverPlayer.getInventory().items;
            for(ItemStack item : items) {
                if(item.getItem() instanceof PortableCrateItem) {
                    MenuProvider menu = getMenuProvider(item);
                    PlayerUtil.tryOpenMenu(serverPlayer, menu);
                    break;
                }
            }
        }
    }
}
