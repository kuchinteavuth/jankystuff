package com.inteavuthkuch.jankystuff.block;

import com.inteavuthkuch.jankystuff.tag.ModTags;
import com.inteavuthkuch.jankystuff.util.ComponentUtil;
import com.inteavuthkuch.jankystuff.util.AmethystRandomSource;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

public class TickAccelerator extends Block {

    public TickAccelerator() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BROWN)
                .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                .strength(0.5F, 3600000.0F)
                .sound(SoundType.METAL));
    }

    public static final Item.Properties ITEM_PROPERTY = new Item.Properties();

    @ParametersAreNonnullByDefault
    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> tooltips, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, tooltips, pTooltipFlag);
        tooltips.add(ComponentUtil.translateBlock("tick_accelerator.description").withStyle(ChatFormatting.GRAY));
    }

    @ParametersAreNonnullByDefault
    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if(pLevel.isClientSide()) return;
        pLevel.scheduleTick(pPos, pState.getBlock(), 20);
    }

    @ParametersAreNonnullByDefault
    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState cropState = level.getBlockState(pos.above(2));
        BlockPos cropPos = pos.above(2);
        if(cropState.is(ModTags.Blocks.ALLOW_ACCELERATION)){
            boolean applyGeneralRandomTick = true; // if true will apply just random tick to any block above

            if(cropState.getBlock() instanceof CropBlock crop){
                if (!crop.isMaxAge(cropState)) {
                    Property<?> cropProperty = crop.getStateDefinition().getProperty("age");
                    if(cropProperty instanceof IntegerProperty ageProperty){
                        int maxAge = Collections.max(ageProperty.getPossibleValues());
                        boolean isMaxAge = crop.isMaxAge(cropState);
                        boolean isTorchFlowerBlock = cropState.getBlock() == Blocks.TORCHFLOWER;

                        // I don't want to apply random tick if crop is fully growth
                        if(!isMaxAge || !isTorchFlowerBlock){
                            BlockState newCropState = cropState.setValue(ageProperty, maxAge);
                            if(cropState.getBlock() == Blocks.TORCHFLOWER_CROP){
                                level.setBlock(cropPos, Blocks.TORCHFLOWER.defaultBlockState(), Block.UPDATE_ALL);
                                showParticles(level, cropPos);
                            }
                            else{
                                level.setBlock(cropPos, newCropState, Block.UPDATE_ALL);
                                showParticles(level, cropPos);
                            }
                        }
                    }
                }
                applyGeneralRandomTick = false;
            }
            else if(cropState.getBlock() instanceof StemBlock stem){
                Property<?> cropProperty = stem.getStateDefinition().getProperty("age");
                if(cropProperty instanceof IntegerProperty ageProperty){
                    int maxAge = Collections.max(ageProperty.getPossibleValues());
                    BlockState newCropState = cropState.setValue(ageProperty, maxAge);
                    if(cropState != newCropState){
                        level.setBlock(cropPos, newCropState, Block.UPDATE_ALL);
                        showParticles(level, cropPos);
                    }
                    // Keep applied random tick to stem block so they keep making example Pumpkin or Melon
                    cropState.randomTick(level, cropPos, random);
                }
                applyGeneralRandomTick = false;
            }
            else if(cropState.getBlock() instanceof AmethystBlock) {
                RandomSource r1 = AmethystRandomSource.createFixSource(0);
                RandomSource r2 = AmethystRandomSource.createFixSource(0);

                cropState.randomTick(level, cropPos, r1);
                cropState.randomTick(level, cropPos, r2);

                showParticles(level, cropPos);
                applyGeneralRandomTick = false;
            }
            else if(cropState.getBlock() instanceof SaplingBlock saplingBlock) {
                showParticles(level, cropPos);
                saplingBlock.advanceTree(level, cropPos, cropState, random);
                applyGeneralRandomTick = false;
            }

            if(applyGeneralRandomTick){
                cropState.randomTick(level, cropPos, random);
                showParticles(level, cropPos);
            }
        }
        // re-schedule tick - check again every second
        level.scheduleTick(pos, state.getBlock(), 20);
    }

    private void showParticles(ServerLevel level, BlockPos pos) {
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                pos.getX() + 0.5,
                pos.getY() + 0.75,
                pos.getZ() + 0.5,
                5,
                0.2,
                0.2,
                0.2,
                0.1);
    }
}
