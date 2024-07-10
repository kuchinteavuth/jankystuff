package com.inteavuthkuch.jankystuff.item.curios;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

public record RingCurioItem(CurioItemAbility ability) implements IJankyCurioItem {

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_GENERIC.value(), 1, 0.5f);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (stack.is(this.ability.getItem())) {
            if (slotContext.entity() instanceof Player player) {
                if(!ability.isFlightAbility())
                    ability.enableAbility(player);
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (stack.is(this.ability.getItem())) {
            if (slotContext.entity() instanceof Player player) {
                ability.disableAbility(player);
            }
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            Optional<ICuriosItemHandler> optCuriosInv = CuriosApi.getCuriosInventory(player);
            if (optCuriosInv.isPresent()) {
                boolean hasRingEquipped = optCuriosInv.get().isEquipped(this.ability.getItem());
                if (hasRingEquipped) {
                    ability.enableAbility(player);
                } else {
                    ability.disableAbility(player);
                }
            }
        }
    }
}
