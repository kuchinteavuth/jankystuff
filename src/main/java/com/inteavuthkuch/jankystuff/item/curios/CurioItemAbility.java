package com.inteavuthkuch.jankystuff.item.curios;

import com.inteavuthkuch.jankystuff.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Custom for CurioItemAbility in JankyStuff
 * some ability is hard-coded so yeah idk lol
 */
public enum CurioItemAbility {
    FIRE_RESISTANCE("fire_resistance",ModItems.RING_OF_FIRE.get(), MobEffects.FIRE_RESISTANCE),
    NIGHT_VISION( "night_vision",ModItems.RING_OF_TRUE_SIGHT.get(), MobEffects.NIGHT_VISION),
    SATURATION( "saturation",ModItems.RING_OF_SATURATION.get(), MobEffects.SATURATION),
    REGENERATION( "regeneration",ModItems.RING_OF_REGENERATION.get(), MobEffects.REGENERATION),
    WATER_BREATHING( "water_breath",ModItems.RING_OF_WATER.get(), MobEffects.WATER_BREATHING),
    RESISTANCE( "resistance",null, MobEffects.DAMAGE_RESISTANCE),
    ABSORPTION( "absorption",null, MobEffects.ABSORPTION),
    FLIGHT(CurioItemAbility.FLIGHT_ABILITY,ModItems.RING_OF_THE_SKY.get(), null);

    private final Holder<MobEffect> mobEffect;
    private final Item curioItem;
    private final String abilityName;

    static final String FLIGHT_ABILITY = "flight";

    CurioItemAbility(String abilityName, @NotNull Item curioItem, @Nullable Holder<MobEffect> mobEffect) {
        this.abilityName = abilityName;
        this.mobEffect = mobEffect;
        this.curioItem = curioItem;
    }

    public String getAbilityName() {
        return abilityName;
    }

    public boolean isMobEffect() {
        return mobEffect != null;
    }

    public Holder<MobEffect> getMobEffect() {
        return this.mobEffect;
    }
    public Item getItem() {
        return this.curioItem;
    }

    public boolean isFlightAbility() {
        return getAbilityName().equals(CurioItemAbility.FLIGHT_ABILITY);
    }

    @SuppressWarnings("deprecation")
    // Using deprecated property here might change later in the future
    public void enableAbility(Player entity){
        if(this.isMobEffect()){
            entity.addEffect(new MobEffectInstance(this.mobEffect, 20 * 15, 0, false,false, false, null));
        }
        else{
            if(isFlightAbility()){
                if(entity.isCreative() || entity.isSpectator()) return;
                if(entity.getAbilities().mayfly) return;

                entity.getAbilities().mayfly = true;
                entity.onUpdateAbilities();
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void disableAbility(Player entity){
        if(isFlightAbility()){
            if(entity.isCreative() || entity.isSpectator()) return;
            entity.getAbilities().mayfly = false;
            entity.getAbilities().flying = false;
            entity.onUpdateAbilities();
        }
    }

}
