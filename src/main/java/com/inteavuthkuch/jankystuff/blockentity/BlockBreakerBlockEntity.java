package com.inteavuthkuch.jankystuff.blockentity;

import com.inteavuthkuch.jankystuff.block.BlockBreakerBlock;
import com.inteavuthkuch.jankystuff.block.IBlockEntityTicker;
import com.inteavuthkuch.jankystuff.util.ContainerUtil;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class BlockBreakerBlockEntity extends BlockEntity implements IBlockEntityTicker {
    private int cooldown = -1;

    public BlockBreakerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.BLOCK_BREAKER_BE.get(), pPos, pBlockState);
    }

    private boolean isOnCooldown(){
        return cooldown > 0;
    }
    private void setCooldown(int value) {
        this.cooldown = value;
    }

    private ItemStack getSuitableTool(BlockState pState) {
        if(pState.is(BlockTags.MINEABLE_WITH_SHOVEL))
            return new ItemStack(Items.DIAMOND_SHOVEL);
        else if(pState.is(BlockTags.MINEABLE_WITH_AXE))
            return new ItemStack(Items.DIAMOND_AXE);
        else if(pState.is(BlockTags.MINEABLE_WITH_HOE))
            return new ItemStack(Items.DIAMOND_HOE);
        else {
            ItemStack pickaxe = new ItemStack(Items.DIAMOND_PICKAXE);
            // try to add fortune but no clue man
            return pickaxe;
        }
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(isOnCooldown()){
            this.cooldown--;
        }
        else{

            BlockPos breakFace = this.getBlockPos().relative(this.getBlockState().getValue(BlockBreakerBlock.FACING));
            if(!pLevel.getBlockState(breakFace).isEmpty()){
                BlockState breakFaceState = pLevel.getBlockState(breakFace);
                LootParams.Builder builder = new LootParams.Builder((ServerLevel) pLevel);
                builder.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(breakFace));
                builder.withParameter(LootContextParams.TOOL, getSuitableTool(breakFaceState));
                builder.withOptionalParameter(LootContextParams.BLOCK_ENTITY, pLevel.getBlockEntity(breakFace));

                BlockPos checkContainer = this.getBlockPos().relative(this.getBlockState().getValue(BlockBreakerBlock.FACING).getOpposite());
                var items = breakFaceState.getDrops(builder);
                ContainerUtil.getContainerAt(pLevel, checkContainer).ifPresentOrElse(container -> {
                    for(ItemStack item : items) {
                        if(!item.isEmpty()){
                            if(ContainerUtil.canInsertItemStack(container, item)){
                                pLevel.destroyBlock(breakFace, false);
                            }
                        }
                    }
                    setChanged();
                }, () -> {
                    for(ItemStack item : items) {
                        if(!item.isEmpty()){
                            ContainerUtil.dropItemStack(pLevel, breakFace, item);
                            pLevel.destroyBlock(breakFace, false);
                        }
                    }
                    setChanged();
                });
            }
            setCooldown(20);
        }
    }
}
