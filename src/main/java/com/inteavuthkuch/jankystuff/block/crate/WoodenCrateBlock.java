package com.inteavuthkuch.jankystuff.block.crate;

import com.inteavuthkuch.jankystuff.blockentity.crate.WoodenCrateBlockEntity;
import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.util.ComponentUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WoodenCrateBlock extends AbstractCrateBlock {
    public static final MapCodec<AbstractCrateBlock> CODEC = simpleCodec(WoodenCrateBlock::new);

    public WoodenCrateBlock() {
        this(Blocks.BARREL.properties());
    }

    public WoodenCrateBlock(BlockBehaviour.Properties properties) {
        super(Blocks.BARREL.properties(), ContainerType.WOODEN);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTootipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTootipComponents, pTooltipFlag);
        pTootipComponents.add(ComponentUtil.translateBlock("wooden_crate.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WoodenCrateBlockEntity(pPos, pState);
    }
}
