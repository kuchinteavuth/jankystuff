package com.inteavuthkuch.jankystuff.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class ContainerUtil {

    private static boolean canMergeItems(@NotNull ItemStack stack1, @NotNull ItemStack stack2) {
        return stack1.getCount() + stack2.getCount() <= stack1.getMaxStackSize() && ItemStack.isSameItemSameComponents(stack1, stack2);
    }

    public static Optional<Container> getContainerAt(@NotNull Level pLevel, BlockPos pos) {
        BlockState state = pLevel.getBlockState(pos);
        Block block = state.getBlock();

        if(block instanceof WorldlyContainerHolder) {
            return Optional.of(((WorldlyContainerHolder) block).getContainer(state, pLevel, pos));
        }
        else if(state.hasBlockEntity() && pLevel.getBlockEntity(pos) instanceof Container container) {
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

    public static void dropItemStack(Level level, @NotNull BlockPos pos, ItemStack @NotNull ... stacks) {
        for(ItemStack stack : stacks){
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
    }

    public static boolean canInsertItemStack(@NotNull Container container, ItemStack itemStack) {
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
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean canInsertItemStackSimulate(@NotNull Container container, ItemStack itemStack) {
        boolean flag = false;
        for(int i=0;i<container.getContainerSize();i++){
            if(container.canPlaceItem(i, itemStack)){
                ItemStack currentStack = container.getItem(i);
                if(currentStack.isEmpty()){
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

    public static boolean canInsertItemStack(@NotNull NonNullList<ItemStack> stacks, ItemStack itemStack) {
        boolean flag = false;
        for(int i=0;i<stacks.size();i++){
            ItemStack currentStack = stacks.get(i);
            if(currentStack.isEmpty()){
                stacks.set(i, itemStack);
                flag = true;
                break;
            }
            else if(canMergeItems(currentStack, itemStack)){
                currentStack.grow(itemStack.getCount());
                stacks.set(i, currentStack);
                flag = true;
                break;
            }
        }
        return flag;
    }
}
