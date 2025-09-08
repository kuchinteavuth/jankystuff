package com.inteavuthkuch.jankystuff.block.fluidtank;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.blockentity.fluidtank.FluidTankBlockEntityBase;
import com.inteavuthkuch.jankystuff.common.FluidTankTier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public abstract class FluidTankBase extends BaseEntityBlock {
    private final FluidTankTier tier;

    public FluidTankBase(FluidTankTier tier) {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BROWN)
                .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                .strength(2F, 3600000.0F)
                .sound(SoundType.METAL));
        this.tier = tier;
    }

    public FluidTankTier getTankTier() {
        return tier;
    }

    @ParametersAreNonnullByDefault
    @Override
    protected @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @ParametersAreNonnullByDefault
    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        CustomData data = pStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = data.copyTag();
        Level level = pContext.level();
        if(!data.isEmpty() && level != null && !tag.isEmpty()) {
            FluidStack fluid = FluidStack.parseOptional(level.registryAccess(), tag.getCompound("Fluid"));
            if (!fluid.isEmpty()) {
                pTooltipComponents.add(
                        Component.translatable("block.jankystuff.fluid_tank_content", fluid.getAmount(), fluid.getHoverName().getString())
                );
            }
        }else{
            pTooltipComponents.add(
                    Component.translatable("block.jankystuff.fluid_tank_description", tier.getBucketCapacity())
                            .withStyle(ChatFormatting.GRAY)
            );
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pLevel.isClientSide())
            return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());

        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if(blockEntity instanceof FluidTankBlockEntityBase fluidTank) {
            if(pPlayer.getItemInHand(pHand).isEmpty() || !FluidUtil.interactWithFluidHandler(pPlayer, pHand, pLevel, pPos, pHitResult.getDirection())){
                if(fluidTank.getFluidTank().isEmpty()){
                    pPlayer.displayClientMessage(Component.literal("Tank is Empty"), true);
                }
                else{
                    FluidTank tank = fluidTank.getFluidTank();
                    pPlayer.displayClientMessage(
                            Component.translatable("block.jankystuff.fluid_tank_content", tank.getFluidInTank(0).getAmount(), tank.getFluidInTank(0).getHoverName().getString())
                            , true);
                }
            }
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
