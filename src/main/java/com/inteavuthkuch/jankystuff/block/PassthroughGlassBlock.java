package com.inteavuthkuch.jankystuff.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TintedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PassthroughGlassBlock extends TintedGlassBlock {
    public PassthroughGlassBlock() {
        super(Blocks.TINTED_GLASS.properties());
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if(pContext instanceof EntityCollisionContext context) {
            Entity entity = context.getEntity();
            if(entity instanceof Player){
                return Shapes.empty();
            }
        }
        return Shapes.block();
    }
}
