package com.inteavuthkuch.jankystuff.client.event;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.client.KeyBinding;
import com.inteavuthkuch.jankystuff.network.packet.ItemTogglePacket;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = JankyStuff.MODID, value = Dist.CLIENT)
public class ClientNeoForgeEvents {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if(KeyBinding.TOGGLE_MAGNET.consumeClick()){
            PacketDistributor.sendToServer(ItemTogglePacket.createInstance(0,-1));
        }

        if(KeyBinding.OPEN_PERSONAL_CRATE.consumeClick()){
            PacketDistributor.sendToServer(ItemTogglePacket.createInstance(1,-1));
        }
    }
}
