package com.inteavuthkuch.jankystuff.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class LampBlock extends Block {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public LampBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(0.3F)
                .mapColor(MapColor.COLOR_BROWN)
                .sound(SoundType.WOOD)
                .lightLevel(state -> state.getValue(POWERED) ? 0 : 15)
        );

        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED);
    }

    @Override
    protected void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        if(pLevel.isClientSide())
            return;

        boolean isPowered = pLevel.getBestNeighborSignal(pPos) > 0;
        if(isPowered != pState.getValue(POWERED)){
            pLevel.setBlock(pPos, pState.setValue(POWERED, isPowered), 3);
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if (!pLevel.isClientSide()) {
            boolean isPowered = pLevel.getBestNeighborSignal(pPos) > 0;

            if (isPowered != pState.getValue(POWERED)) {
                pLevel.setBlock(pPos, pState.setValue(POWERED, isPowered), 3);
            }
        }

    }

    @Override
    protected BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (!pLevel.isClientSide()) {
            boolean isPowered = pLevel.getBestNeighborSignal(pPos) > 0;

            if (isPowered != pState.getValue(POWERED)) {
                return pState.setValue(POWERED, isPowered);
            }
        }

        return pState;

    }
}
