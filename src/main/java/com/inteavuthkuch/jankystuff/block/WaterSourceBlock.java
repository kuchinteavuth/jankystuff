package com.inteavuthkuch.jankystuff.block;

import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.WaterSourceBlockEntity;
import com.inteavuthkuch.jankystuff.network.packet.PlaySoundPacket;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class WaterSourceBlock extends BaseEntityBlock {
    public static final MapCodec<WaterSourceBlock> CODEC = simpleCodec(p -> new WaterSourceBlock());
    public static final BooleanProperty AUTO_EXPORT = BooleanProperty.create("auto_export");

    public WaterSourceBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BROWN)
                .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                .strength(2F, 3600000.0F)
                .lightLevel(state -> 15)
                .sound(SoundType.METAL));

        this.registerDefaultState(this.stateDefinition.any().setValue(AUTO_EXPORT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AUTO_EXPORT);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return defaultBlockState().setValue(AUTO_EXPORT, false);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(
                Component.translatable("block.jankystuff.water_source.description")
                        .withStyle(ChatFormatting.GRAY)
        );
    }

    @NotNull
    @Override
    protected RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @ParametersAreNonnullByDefault
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WaterSourceBlockEntity(blockPos, blockState);
    }

    @ParametersAreNonnullByDefault
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) return null;
        return createTickerHelper(pBlockEntityType, ModBlockEntity.WATER_SOURCE_BE.get(), IBlockEntityTicker.getTickerHelper());
    }

    @ParametersAreNonnullByDefault
    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pLevel.isClientSide())
            return ItemInteractionResult.SUCCESS;

        ItemStack heldItem = pPlayer.getItemInHand(pHand);
        if(heldItem.isEmpty()){
            boolean isAutoExport = pState.getValue(WaterSourceBlock.AUTO_EXPORT);
            if(pPlayer.isShiftKeyDown()){
                isAutoExport = !isAutoExport;
                BlockState newState = pState.setValue(WaterSourceBlock.AUTO_EXPORT, isAutoExport);
                pLevel.setBlock(pPos, newState, Block.UPDATE_ALL);
                PacketDistributor.sendToPlayer((ServerPlayer) pPlayer, new PlaySoundPacket(pPos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS.getName(), 0.5f, 1f));
                pPlayer.displayClientMessage(
                        Component.translatable(isAutoExport ? "state.jankystuff.auto_export_enabled": "state.jankystuff.auto_export_disabled")
                                .withStyle(style -> style.withColor(ChatFormatting.GOLD).withItalic(true))
                        , true);
            }
            else{
                pPlayer.displayClientMessage(
                        Component.translatable(isAutoExport ? "state.jankystuff.auto_export_enabled": "state.jankystuff.auto_export_disabled")
                                .withStyle(style -> style.withColor(ChatFormatting.GOLD).withItalic(true))
                        , true);
            }
            return ItemInteractionResult.SUCCESS;
        }
        else if(heldItem.is(Items.GLASS_BOTTLE)){
            heldItem.shrink(1);

            ItemStack waterBottle = new ItemStack(Items.POTION);
            waterBottle.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
            if(!pPlayer.getInventory().add(waterBottle)) {
                pPlayer.drop(waterBottle, false);
            }

            PacketDistributor.sendToPlayer((ServerPlayer) pPlayer, new PlaySoundPacket(pPos, SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL.getName(), 1.0F, 1.0F));
            return ItemInteractionResult.SUCCESS;
        }
        else {
            PotionContents potionContents = heldItem.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            if(potionContents.is(Potions.WATER)){
                heldItem.shrink(1);
                ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE, 1);
                if(!pPlayer.getInventory().add(glassBottle)) {
                    pPlayer.drop(glassBottle, false);
                }
                PacketDistributor.sendToPlayer((ServerPlayer) pPlayer, new PlaySoundPacket(pPos, SoundEvents.BOTTLE_EMPTY, SoundSource.NEUTRAL.getName(), 1.0F, 1.0F));
                return ItemInteractionResult.SUCCESS;
            }

            if(FluidUtil.interactWithFluidHandler(pPlayer, pHand, pLevel, pPos, pHitResult.getDirection()))
                return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
