package com.inteavuthkuch.jankystuff.block;

import com.inteavuthkuch.jankystuff.common.Constraints;
import com.inteavuthkuch.jankystuff.tag.ModTags;
import com.inteavuthkuch.jankystuff.util.ComponentUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TickAccelerator extends Block {

    public TickAccelerator() {
        super(Blocks.STONE.properties().randomTicks());
    }

    private void tickBlock(BlockState pState, ServerLevel pLevel, BlockPos pPos) {
        pState.randomTick(pLevel, pPos, pLevel.getRandom());
        pLevel.scheduleTick(pPos, pState.getBlock(), Constraints.ACCELERATOR_TICK_DELAY);
    }

    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        pLevel.scheduleTick(pPos, pState.getBlock(), Constraints.ACCELERATOR_TICK_DELAY);
    }

    @Override
    protected void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {

        BlockPos.betweenClosed(pPos.offset(-1,0,-1), pPos.offset(1,0,1)).forEach(pos -> {
            BlockState state = pLevel.getBlockState(pos);
            if(state.is(ModTags.Blocks.ALLOW_ACCELERATION)){
                tickBlock(state, pLevel, pos);
            }
        });

        pLevel.scheduleTick(pPos, pState.getBlock(), Constraints.ACCELERATOR_TICK_DELAY);
    }

    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
        if(pLevel.isClientSide()) return;
        pLevel.scheduleTick(pPos, pState.getBlock(), Constraints.ACCELERATOR_TICK_DELAY);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTootipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTootipComponents, pTooltipFlag);
        pTootipComponents.add(ComponentUtil.translateBlock("tick_accelerator.description").withStyle(ChatFormatting.GRAY));
    }
}
