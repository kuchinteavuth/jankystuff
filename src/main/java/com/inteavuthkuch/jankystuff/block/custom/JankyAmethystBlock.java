package com.inteavuthkuch.jankystuff.block.custom;

import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class JankyAmethystBlock extends AmethystBlock {

    private final Block smallBud;
    private final Block mediumBud;
    private final Block largeBud;
    private final Block cluster;

    public JankyAmethystBlock(Properties properties, Block smallBud, Block mediumBud, Block largeBud, Block cluster) {
        super(properties);

        this.smallBud = smallBud;
        this.mediumBud = mediumBud;
        this.largeBud = largeBud;
        this.cluster = cluster;
    }

    @Override
    public MapCodec<? extends AmethystBlock> codec() {
        return simpleCodec(props ->
                new JankyAmethystBlock(props,
                        smallBud,
                        mediumBud,
                        largeBud,
                        cluster
                ));
    }

    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        Direction direction = Direction.values()[pRandom.nextInt(Direction.values().length)];
        BlockPos blockpos = pPos.relative(direction);
        BlockState blockstate = pLevel.getBlockState(blockpos);
        Block block = null;

        if (canClusterGrowAtState(blockstate)) {
            block = smallBud;
        } else if (blockstate.is(smallBud) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
            block = mediumBud;
        } else if (blockstate.is(mediumBud) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
            block = largeBud;
        } else if (blockstate.is(largeBud) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
            block = cluster;
        }

        if (block != null) {
            BlockState newState = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction)
                .setValue(AmethystClusterBlock.WATERLOGGED, blockstate.getFluidState().getType() == Fluids.WATER);
            pLevel.setBlockAndUpdate(blockpos, newState);
        }
    }

    // In case if minecraft decided to change this from Amethyst Block
    protected static boolean canClusterGrowAtState(BlockState pState) {
        return pState.isAir() || pState.is(Blocks.WATER) && pState.getFluidState().getAmount() == 8;
    }
}
