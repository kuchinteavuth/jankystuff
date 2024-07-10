package com.inteavuthkuch.jankystuff.integration;

import com.inteavuthkuch.jankystuff.item.curios.CurioItemAbility;
import com.inteavuthkuch.jankystuff.item.curios.IJankyCurioItem;
import com.inteavuthkuch.jankystuff.item.curios.RingCurioItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.*;

public class CuriosIntegration {
    private static void registerEvent(final FMLCommonSetupEvent event) {
        registerCurio(new RingCurioItem(CurioItemAbility.NIGHT_VISION));
        registerCurio(new RingCurioItem(CurioItemAbility.FIRE_RESISTANCE));
        registerCurio(new RingCurioItem(CurioItemAbility.REGENERATION));
        registerCurio(new RingCurioItem(CurioItemAbility.SATURATION));
        registerCurio(new RingCurioItem(CurioItemAbility.WATER_BREATHING));
        registerCurio(new RingCurioItem(CurioItemAbility.FLIGHT));
    }

    private static void registerCurio(IJankyCurioItem curioItem) {
        CuriosApi.registerCurio(curioItem.ability().getItem(), curioItem);
    }

    public static Optional<List<ItemStack>> getEquippedCurios(Player player) {
        Optional<ICuriosItemHandler> handler = CuriosApi.getCuriosInventory(player);
        if(handler.isPresent()){
            var curios = handler.get().getEquippedCurios();
            List<ItemStack> result = new ArrayList<>();
            for(int i=0; i < curios.getSlots(); i++){
                ItemStack itemStack = curios.getStackInSlot(i);
                if(!itemStack.isEmpty()){
                    result.add(itemStack);
                }
            }
            return Optional.of(result);
        }
        return Optional.empty();
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(CuriosIntegration::registerEvent);
    }
}
