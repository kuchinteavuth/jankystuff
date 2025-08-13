package com.inteavuthkuch.jankystuff.item.tool;

import com.inteavuthkuch.jankystuff.block.MinerLightBlock;
import com.inteavuthkuch.jankystuff.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MinerPickaxe extends PickaxeItem {

    public MinerPickaxe(Tier tier) {
        super(tier, new Properties()
                .attributes(PickaxeItem.createAttributes(tier, 1.0F, -2.8F))
                .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
                .rarity(Rarity.UNCOMMON)
                .fireResistant()
                .setNoRepair()
        );
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction face = context.getClickedFace();
        BlockPos placePos = clickedPos.relative(face);

        // Only place on server side
        if (!level.isClientSide) {
            BlockState stateAtPos = level.getBlockState(placePos);

            // Only place if the target position is air or water
            if (stateAtPos.isAir() || stateAtPos.is(Blocks.WATER)) {
                // Create the block state with correct facing
                BlockState lightState = ModBlocks.MINER_LIGHT.get()
                        .defaultBlockState()
                        .setValue(MinerLightBlock.FACING, face);

                // Prevent stacking on another MinerLightBlock
                if (lightState.canSurvive(level, placePos)) {
                    level.setBlock(placePos, lightState, 3);

                    // Play placement sound manually
                    SoundType soundType = lightState.getSoundType(level, placePos, null);
                    level.playSound(
                            null, // player (null = global)
                            placePos,
                            soundType.getPlaceSound(),
                            SoundSource.BLOCKS,
                            (soundType.getVolume() + 1.0F) / 2.0F,
                            soundType.getPitch() * 0.8F
                    );


                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }
}
