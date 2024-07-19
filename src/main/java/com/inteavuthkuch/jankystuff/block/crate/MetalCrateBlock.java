package com.inteavuthkuch.jankystuff.block.crate;

import com.inteavuthkuch.jankystuff.blockentity.crate.MetalCrateBlockEntity;
import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.util.ComponentUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MetalCrateBlock extends AbstractCrateBlock {
    public static final MapCodec<AbstractCrateBlock> CODEC = simpleCodec(MetalCrateBlock::new);

    public MetalCrateBlock() {
        this(Blocks.BARREL.properties().sound(SoundType.METAL));
    }

    public MetalCrateBlock(Properties properties) {
        super(Blocks.BARREL.properties(), ContainerType.METAL);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTootipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTootipComponents, pTooltipFlag);
        // idk just copy minecraft shulker box description
        int i=0;
        int j=0;
        for (ItemStack itemstack : pStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).nonEmptyItems()) {
            j++;
            if (i < 4) {
                i++;
                pTootipComponents.add(
                        Component.translatable("block.jankystuff.metal_crate.content", itemstack.getHoverName(), itemstack.getCount())
                                .withStyle(ChatFormatting.GRAY)
                );
            }
        }

        if (j - i > 0) {
            pTootipComponents.add(
                    Component.translatable("block.jankystuff.metal_crate.more", j - i)
                            .withStyle(ChatFormatting.ITALIC)
                            .withStyle(ChatFormatting.GRAY)
            );
        }
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MetalCrateBlockEntity(pPos, pState);
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.hasBlockEntity() && (!pState.is(pNewState.getBlock()) || !pNewState.hasBlockEntity())) {
            pLevel.removeBlockEntity(pPos);
        }
    }
}
