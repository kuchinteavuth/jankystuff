package com.inteavuthkuch.jankystuff.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Set;

public record ChanceItemStack(ItemStack itemStack, float chance) implements WeightedEntry {

    public static ChanceItemStack of(ItemLike item, int count, float chance) {
        return new ChanceItemStack(new ItemStack(item, count), chance);
    }
    public static ChanceItemStack of(ItemLike item, int count) {
        return new ChanceItemStack(new ItemStack(item, count), 1.0f);
    }

    public static ChanceItemStack of(ItemLike item) {
        return new ChanceItemStack(new ItemStack(item, 1), 1.0f);
    }

    public static ChanceItemStack of(ItemLike item, float chance) {
        return new ChanceItemStack(new ItemStack(item, 1), chance);
    }

    public static final Codec<ChanceItemStack> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ItemStack.CODEC.fieldOf("item").forGetter(ChanceItemStack::itemStack),
            Codec.FLOAT.fieldOf("chance").forGetter(ChanceItemStack::chance)
    ).apply(inst, ChanceItemStack::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ChanceItemStack> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, ChanceItemStack::itemStack,
            ByteBufCodecs.FLOAT, ChanceItemStack::chance,
            ChanceItemStack::new
    );

    public static final ChanceItemStack EMPTY = new ChanceItemStack(ItemStack.EMPTY, 0.0f);

    @Override
    public Weight getWeight() {
        float finalChance = Math.max(chance(), 0.01f);
        return Weight.of(Math.round(finalChance * 100));
    }
}
