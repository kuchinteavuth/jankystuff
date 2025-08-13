package com.inteavuthkuch.jankystuff.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class MinerLightBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public MinerLightBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .strength(0.1F, 3600000.0F)
                        .mapColor(MapColor.COLOR_GRAY)
                        .sound(SoundType.METAL)
                        .noOcclusion()
                        .noLootTable()
                        .lightLevel(state -> 15)
                        .isValidSpawn((a,b,c,d) -> false)
                        .isRedstoneConductor((a, b, c) -> false)
                        .isSuffocating((a, b, c) -> false)
        );

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getClickedFace());
    }

    @ParametersAreNonnullByDefault
    @Override
    public void wasExploded(Level pLevel, BlockPos pPos, Explosion pExplosion) {
        // Nothing
    }

    @ParametersAreNonnullByDefault
    @Override
    public boolean canDropFromExplosion(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return false;
    }

    @ParametersAreNonnullByDefault
    @Override
    protected boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        Direction facing = pState.getValue(FACING);
        BlockPos supportPos = pPos.relative(facing.getOpposite());
        BlockState supportState = pLevel.getBlockState(supportPos);

        // ❌ Don't allow placement on another MinerLightBlock
        return !(supportState.getBlock() instanceof MinerLightBlock);
    }

    @ParametersAreNonnullByDefault
    @Override
    protected @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction facing = pState.getValue(FACING);
        return switch (facing) {
            case UP -> Block.box(6.0, 0.0, 6.0, 10.0, 0.5, 10.0); // floor
            case DOWN -> Block.box(6.0, 15.5, 6.0, 10.0, 16.0, 10.0); // ceiling
            case NORTH -> Block.box(6.0, 6.0, 15.5, 10.0, 10.0, 16.0);
            case SOUTH -> Block.box(6.0, 6.0, 0.0, 10.0, 10.0, 0.5);
            case WEST -> Block.box(15.5, 6.0, 6.0, 16.0, 10.0, 10.0);
            case EAST -> Block.box(0.0, 6.0, 6.0, 0.5, 10.0, 10.0);
        };
    }
}
