package com.inteavuthkuch.jankystuff.block;

import com.inteavuthkuch.jankystuff.blockentity.AdvancedQuarryBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.config.JankyStuffCommonConfig;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AdvancedQuarryBlock extends BaseEntityBlock {
    public static final MapCodec<AdvancedQuarryBlock> CODEC = simpleCodec(AdvancedQuarryBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AdvancedQuarryBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        pTooltipComponents.add(Component.translatable("block.jankystuff.advanced_quarry.description").withStyle(ChatFormatting.GRAY));
        if(JankyStuffCommonConfig.DISABLE_QUARRY.get()){
            pTooltipComponents.add(Component.literal(""));
            pTooltipComponents.add(Component.translatable("text.jankystuff.disable_in_config")
                    .withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if(pLevel.isClientSide())
            return;

        if(pLevel.getBlockEntity(pPos) instanceof AdvancedQuarryBlockEntity quarryBlock) {
            quarryBlock.setPause(true);
        }
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AdvancedQuarryBlockEntity(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) return null;
        return createTickerHelper(pBlockEntityType, ModBlockEntity.ADVANCED_QUARRY_BE.get(), IBlockEntityTicker.getTickerHelper());
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock()){
            if(pLevel.getBlockEntity(pPos) instanceof AdvancedQuarryBlockEntity quarry) {
                quarry.drops(pLevel, pPos);
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide()) {
            if(pLevel.getBlockEntity(pPos) instanceof AdvancedQuarryBlockEntity quarry) {
                pPlayer.openMenu(new SimpleMenuProvider(quarry, Component.translatable("block.jankystuff.advanced_quarry")), pPos);
            }else{
                throw new IllegalStateException("Missing Container provider");
            }
        }
        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
    }
}
