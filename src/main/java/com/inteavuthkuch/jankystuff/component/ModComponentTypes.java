package com.inteavuthkuch.jankystuff.component;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.component.custom.FilterTypeComponent;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModComponentTypes {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, JankyStuff.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FilterTypeComponent>> FILTER_TYPE = DATA_COMPONENTS.registerComponentType("filter_type",
                    builder -> builder.persistent(FilterTypeComponent.CODEC).networkSynchronized(FilterTypeComponent.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> IS_DISABLED = DATA_COMPONENTS.registerComponentType("is_disabled",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}
