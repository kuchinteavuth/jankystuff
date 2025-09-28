package com.inteavuthkuch.jankystuff.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Optional;


public record ChanceItemStackList(NonNullList<ChanceItemStack> itemStacks) {

    public NonNullList<ChanceItemStack> getGuaranteedItems() {
        if(itemStacks().isEmpty())
            return null;

        NonNullList<ChanceItemStack> guaranteedList = NonNullList.create();
        for(ChanceItemStack itemStack : itemStacks()){
            if(itemStack.chance() >= 1.0f){
                guaranteedList.add(itemStack);
            }
        }

        return guaranteedList;
    }

    @Nullable
    public NonNullList<ChanceItemStack> getChanceItems() {
        if(itemStacks().isEmpty())
            return null;

        NonNullList<ChanceItemStack> chanceItemStacks = NonNullList.create();
        for(ChanceItemStack itemStack : itemStacks()){
            if(itemStack.chance() < 1.0f){
                chanceItemStacks.add(itemStack);
            }
        }

        return chanceItemStacks.isEmpty() ? null : chanceItemStacks;
    }

    public int size() {
        return itemStacks().size();
    }

    public ChanceItemStack get(int index) {
        return itemStacks().get(index);
    }

    public void sort(@Nullable Comparator<? super ChanceItemStack> comparator) {
        itemStacks.sort(comparator);
    }

    public Optional<NonNullList<ChanceItemStack>> getOptionalGuaranteedItems() {
        return Optional.ofNullable(getGuaranteedItems());
    }
    public Optional<NonNullList<ChanceItemStack>> getOptionalChanceItems() {
        return Optional.ofNullable(getChanceItems());
    }

    public static final Codec<ChanceItemStackList> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            NonNullList.codecOf(ChanceItemStack.CODEC).fieldOf("items").forGetter(ChanceItemStackList::itemStacks)
    ).apply(inst , ChanceItemStackList::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ChanceItemStackList> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ChanceItemStackList decode(RegistryFriendlyByteBuf byteBuf) {
            int size = byteBuf.readVarInt();
            NonNullList<ChanceItemStack> list = NonNullList.create();
            for(int i = 0; i<size; i++){
                list.add(i, ChanceItemStack.STREAM_CODEC.decode(byteBuf));
            }
            return new ChanceItemStackList(list);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf byteBuf, ChanceItemStackList chanceItemStackList) {
            byteBuf.writeVarInt(chanceItemStackList.itemStacks().size());
            for(ChanceItemStack itemStack : chanceItemStackList.itemStacks()){
                ChanceItemStack.STREAM_CODEC.encode(byteBuf, itemStack);
            }
        }
    };
}
