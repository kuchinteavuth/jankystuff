package com.inteavuthkuch.jankystuff.block.custom;

import com.inteavuthkuch.jankystuff.block.IBlockEntityTicker;
import com.inteavuthkuch.jankystuff.blockentity.AdvancedQuarryBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.custom.FarmSimulationBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FarmSimulationBlock extends BaseEntityBlock {

    public static final MapCodec<FarmSimulationBlock> CODEC = simpleCodec(FarmSimulationBlock::new);

    public FarmSimulationBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock()){
            if(pLevel.getBlockEntity(pPos) instanceof FarmSimulationBlockEntity farmSimulationBlockEntity) {
                farmSimulationBlockEntity.drops(pLevel, pPos);
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide()) {
            if(pLevel.getBlockEntity(pPos) instanceof FarmSimulationBlockEntity farmSimulationBlockEntity) {
                pPlayer.openMenu(new SimpleMenuProvider(farmSimulationBlockEntity,
                        Component.translatable("block.jankystuff.farm_simulation")), pPos);
            }else{
                throw new IllegalStateException("Missing Container provider");
            }
        }
        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FarmSimulationBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) return null;
        return createTickerHelper(pBlockEntityType, ModBlockEntity.FARM_SIMULATION_BE.get(), IBlockEntityTicker.getTickerHelper());
    }
}
