package com.inteavuthkuch.jankystuff.blockentity;

import com.inteavuthkuch.jankystuff.block.BlockBreakerBlock;
import com.inteavuthkuch.jankystuff.block.IBlockEntityTicker;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

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

    private List<EnchantmentInstance> getEnchantmentList(RegistryAccess p_345264_, ItemStack p_39472_, int p_39473_, int p_39474_) {
        Optional<HolderSet.Named<Enchantment>> optional = p_345264_.registryOrThrow(Registries.ENCHANTMENT).getTag(EnchantmentTags.IN_ENCHANTING_TABLE);
        if (optional.isEmpty()) {
            return List.of();
        } else {
            BuiltInRegistries.ENCHANTMENT_LEVEL_BASED_VALUE_TYPE.
        }
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

    private Optional<Container> getContainerAt(Level pLevel, BlockPos pos) {
        BlockState state = pLevel.getBlockState(pos);
        Block block = state.getBlock();

        if(state.hasBlockEntity() && pLevel.getBlockEntity(pos) instanceof Container container) {
            if(container instanceof ChestBlockEntity && block instanceof ChestBlock){
                Container chestContainer = ChestBlock.getContainer((ChestBlock) block, state, pLevel, pos, true);
                assert chestContainer != null;
                return Optional.of(chestContainer);
            }
            else{
                return Optional.of(container);
            }
        }

        return Optional.empty();
    }

    private boolean canMergeItems(ItemStack stack1, ItemStack stack2) {
        return stack1.getCount() + stack2.getCount() <= stack1.getMaxStackSize() && ItemStack.isSameItemSameComponents(stack1, stack2);
    }

    private boolean isCanInsertToInventory(Container container, ItemStack itemStack) {
        boolean flag = false;
        for(int i=0;i<container.getContainerSize();i++){
            if(container.canPlaceItem(i, itemStack)){
                ItemStack currentStack = container.getItem(i);
                if(currentStack.isEmpty()){
                    container.setItem(i, itemStack);
                    flag = true;
                    break;
                }
                else if(canMergeItems(currentStack, itemStack)){
                    currentStack.grow(itemStack.getCount());
                    container.setItem(i, currentStack);
                    flag = true;
                    break;
                }
            }
        }
        return flag;
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

                var items = breakFaceState.getDrops(builder);
                getContainerAt(pLevel, pPos.above()).ifPresentOrElse(container -> {
                    for(ItemStack item : items) {
                        if(!item.isEmpty()){
                            if(isCanInsertToInventory(container, item)){
                                pLevel.destroyBlock(breakFace, false);
                            }
                        }
                    }
                    setChanged();
                }, () -> {
                    for(ItemStack item : items) {
                        if(!item.isEmpty()){
                            Containers.dropItemStack(pLevel, breakFace.getX(), breakFace.getY(), breakFace.getZ(), item);
                            pLevel.destroyBlock(breakFace, false);
                        }
                    }
                    setChanged();
                });
            }

//            BlockState stateAbove = pLevel.getBlockState(pPos.above());
//
//            if(!stateAbove.isEmpty()){
//                //BlockPos breakFace = pPos.relative(pState.getValue(BlockStateProperties.FACING));
//
//            }
//            else{
//                getContainerBelow(pLevel, pPos).ifPresent(container -> {
//                    for(int i=0;i<container.getContainerSize();i++){
//                        ItemStack item = container.getItem(i);
//                        if(!item.isEmpty() && item.getItem() instanceof BlockItem blockItem) {
//                            pLevel.setBlockAndUpdate(pPos.above(), blockItem.getBlock().defaultBlockState());
//                            item.shrink(1);
//                            setChanged();
//                            break;
//                        }
//                    }
//                });
//            }

            setCooldown(20);
        }
    }
}
