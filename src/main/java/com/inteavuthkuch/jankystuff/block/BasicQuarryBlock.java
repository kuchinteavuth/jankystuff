package com.inteavuthkuch.jankystuff.block;

import com.inteavuthkuch.jankystuff.blockentity.BasicQuarryBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.util.ComponentUtil;
import com.inteavuthkuch.jankystuff.util.PlayerUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BasicQuarryBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty ON = BooleanProperty.create("on");
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public static final MapCodec<BasicQuarryBlock> CODEC = simpleCodec(BasicQuarryBlock::new);

    public static BasicQuarryBlock create() {
        return new BasicQuarryBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_BROWN)
                        .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                        .strength(3.5F)
                        .sound(SoundType.METAL)
        );
    }

    public BasicQuarryBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ON, Boolean.FALSE)
                .setValue(ENABLED, Boolean.TRUE)
        );
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTootipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTootipComponents, pTooltipFlag);
        pTootipComponents.add(ComponentUtil.translateBlock(ModBlocks.BASIC_QUARRY, "description").withStyle(ChatFormatting.GRAY));
        pTootipComponents.add(Component.empty());
        pTootipComponents.add(Component.literal("WIP: Currently only collect specific useful ores"));
        pTootipComponents.add(Component.literal("Recipe will be add in later version"));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()).setValue(ENABLED, Boolean.TRUE);
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, ON, ENABLED);
    }

    @Override
    protected void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        boolean hasNeighborSignal = pLevel.hasNeighborSignal(pPos);
        if(hasNeighborSignal){
            pLevel.setBlock(pPos, pState.setValue(ENABLED, Boolean.FALSE), 2);
        }else{
            pLevel.setBlock(pPos, pState.setValue(ENABLED, Boolean.TRUE), 2);
        }
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        Containers.dropContentsOnDestroy(pState, pNewState, pLevel, pPos);
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            PlayerUtil.tryOpenMenu(pPlayer, pLevel, pPos);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return BasicQuarryBlock.CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BasicQuarryBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) return null;

        return createTickerHelper(pBlockEntityType, ModBlockEntity.BASIC_QUARRY_BE.get(), IBlockEntityTicker.getTickerHelper());
    }
}
