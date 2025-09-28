package com.inteavuthkuch.jankystuff.component.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record FilterTypeComponent(boolean blackList) {
    public static final Codec<FilterTypeComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.fieldOf("blackList").forGetter(FilterTypeComponent::blackList)
            ).apply(instance, FilterTypeComponent::new)
    );

    public static final StreamCodec<ByteBuf, FilterTypeComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            FilterTypeComponent::blackList,
            FilterTypeComponent::new
    );

    public static FilterTypeComponent BLACKLIST = new FilterTypeComponent(true);
    public static final FilterTypeComponent WHITELIST = new FilterTypeComponent(false);
}
