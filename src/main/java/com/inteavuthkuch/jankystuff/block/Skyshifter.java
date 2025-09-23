package com.inteavuthkuch.jankystuff.block;

import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.SkyShifterBlockEntity;
import com.inteavuthkuch.jankystuff.common.TimePeriod;
import com.inteavuthkuch.jankystuff.network.packet.PlaySoundPacket;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;


public class Skyshifter extends BaseEntityBlock {
    public static final MapCodec<Skyshifter> CODEC = simpleCodec(x -> new Skyshifter());
    public static final BooleanProperty DAY_MODE = BooleanProperty.create("day_mode");
    public static final BooleanProperty HAS_RUN = BooleanProperty.create("has_run");

    public Skyshifter() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BROWN)
                .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                .strength(2F, 3600000.0F)
                .lightLevel(state -> 15)
                .sound(SoundType.METAL));
        this.registerDefaultState(getStateDefinition().any()
                .setValue(DAY_MODE, true)
                .setValue(HAS_RUN, false)
        );
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState pState) {
        return false;
    }

    @Override
    protected boolean isSignalSource(BlockState pState) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(DAY_MODE, HAS_RUN);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        ItemStack itemStack = pContext.getItemInHand();
        CustomData data = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if(data.isEmpty()){
            return this.defaultBlockState()
                    .setValue(DAY_MODE, true)
                    .setValue(HAS_RUN, false);
        }

        CompoundTag tag = data.copyTag();
        return this.defaultBlockState()
                .setValue(DAY_MODE, tag.getBoolean("IsDayMode"))
                .setValue(HAS_RUN, false);
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        if(pLevel.isClientSide())
            return;

        boolean powered = pLevel.hasNeighborSignal(pPos);
        boolean hasRun = pState.getValue(HAS_RUN);
        boolean isDayMode = pState.getValue(DAY_MODE);
        if(powered && !hasRun) {
            ServerLevel serverLevel = (ServerLevel) pLevel;
            TimePeriod period = TimePeriod.get(serverLevel);
            if(pLevel.getBlockEntity(pPos) instanceof SkyShifterBlockEntity blockEntity) {
                if(isDayMode) {
                    if(!period.is(TimePeriod.DAY)) {
                        blockEntity.updateTime(TimePeriod.DAY.getStart());
                    }
                }
                else{
                    if(!period.is(TimePeriod.NIGHT)) {
                        blockEntity.updateTime(TimePeriod.NIGHT.getStart());
                    }
                }

                BlockState newState = pState.setValue(HAS_RUN, true);
                pLevel.setBlock(pPos, newState, Block.UPDATE_CLIENTS);
            }
        }
        else if(!powered && hasRun){
            BlockState newState = pState.setValue(HAS_RUN, false);
            pLevel.setBlock(pPos, newState, Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pLevel.isClientSide())
            return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());

        boolean isDayMode = pState.getValue(DAY_MODE);
        if(pPlayer.getItemInHand(pHand).isEmpty() && pPlayer.isShiftKeyDown()){
            BlockState newState = pState.setValue(DAY_MODE, !isDayMode);
            pLevel.setBlock(pPos, newState, Block.UPDATE_ALL);
            PacketDistributor.sendToPlayer((ServerPlayer) pPlayer, new PlaySoundPacket(pPos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS.getName(), 0.5f, 0.6f));
            pPlayer.displayClientMessage(Component.literal(newState.getValue(DAY_MODE) ? "'Day Mode' on" : "'Day Mode' off"), true);
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SkyShifterBlockEntity(blockPos, blockState);
    }

    @ParametersAreNonnullByDefault
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) return null;
        return createTickerHelper(pBlockEntityType, ModBlockEntity.SKY_SHIFTER_BE.get(), IBlockEntityTicker.getTickerHelper());
    }
}
