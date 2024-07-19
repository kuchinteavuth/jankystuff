package com.inteavuthkuch.jankystuff.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class TheVoidBlockEntity extends BlockEntity {

    private final SimpleContainer itemContainer;
    private final FluidTank fluidContainer;

    public TheVoidBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.THE_VOID_BE.get(), pPos, pBlockState);
        this.itemContainer = new SimpleContainer(64) {
            @Override
            public void setItem(int pIndex, ItemStack pStack) {
                super.setItem(pIndex, pStack);
                super.clearContent();
            }
        };

        fluidContainer = new FluidTank(Integer.MAX_VALUE){
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return true;
            }

            @Override
            public int fill(FluidStack resource, FluidAction action) {
                return resource.getAmount();
            }
        };
    }

    // Fluid Handler
    public static IFluidHandler handleFluid(Level level, BlockPos pos, BlockState state, BlockEntity be, Direction direction) {
        TheVoidBlockEntity entity = (TheVoidBlockEntity) be;
        return entity.fluidContainer;
    }

    public static InvWrapper handleItem(Level level, BlockPos pos, BlockState state, BlockEntity be, Direction direction) {
        TheVoidBlockEntity entity = (TheVoidBlockEntity) be;
        return new InvWrapper(entity.itemContainer);
    }
}
