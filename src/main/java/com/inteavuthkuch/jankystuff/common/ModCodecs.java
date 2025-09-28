package com.inteavuthkuch.jankystuff.common;

import com.inteavuthkuch.jankystuff.util.ChanceItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ModCodecs {
    public static final StreamCodec<RegistryFriendlyByteBuf, NonNullList<ChanceItemStack>> CHANCE_ITEM_STACK_STREAM_CODEC =
            new StreamCodec<>() {
        @Override
        public NonNullList<ChanceItemStack> decode(RegistryFriendlyByteBuf pBuffer) {
            int size = pBuffer.readVarInt();
            NonNullList<ChanceItemStack> list = NonNullList.create();
            for(int i = 0; i<size; i++){
                list.add(ChanceItemStack.STREAM_CODEC.decode(pBuffer));
            }
            return list;
        }

        @Override
        public void encode(RegistryFriendlyByteBuf pBuffer, NonNullList<ChanceItemStack> pValue) {
            pBuffer.writeVarInt(pValue.size());
            for(ChanceItemStack itemStack : pValue) {
                ChanceItemStack.STREAM_CODEC.encode(pBuffer, itemStack);
            }
        }
    };
}
