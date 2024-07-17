package com.inteavuthkuch.jankystuff.block.dirt;

import com.inteavuthkuch.jankystuff.config.JankyStuffCommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;

public class CorruptedDirtBlock extends Block {

    public CorruptedDirtBlock() {
        super(
                Blocks.DIRT.properties()
                        .randomTicks()
        );
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTootipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTootipComponents, pTooltipFlag);
        pTootipComponents.add(Component.translatable("block.jankystuff.corrupted_dirt").withStyle(ChatFormatting.GRAY));
        pTootipComponents.add(Component.translatable("block.jankystuff.corrupted_dirt.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
        pLevel.scheduleTick(pPos, pState.getBlock(), 40);
    }

    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        pLevel.scheduleTick(pPos, pState.getBlock(), 20);
    }

    @Override
    protected void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if(!pLevel.isAreaLoaded(pPos, 3)) return;
        if(!pLevel.getFluidState(pPos.above()).isEmpty()) return;
        if(pLevel.getLevelData().getDifficulty() == Difficulty.PEACEFUL) return;
        if(!pLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) return;
        if(pLevel.getMaxLocalRawBrightness(pPos.above()) > 6) return;

        int totalEntity = pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(pPos.getX() - 3, pPos.getY(), pPos.getZ() - 3, pPos.getX() + 3, pPos.getY() + 1, pPos.getZ() + 3)).size();

        if(totalEntity > JankyStuffCommonConfig.CORRUPTED_DIRT_MAX_ENTITY.get()) return;

        pLevel.scheduleTick(pPos, pState.getBlock(), pLevel.random.nextInt(JankyStuffCommonConfig.CORRUPTED_DIRT_SPAWN_DELAY.get()));

        trySpawnMob(pLevel, pPos, pRandom).ifPresent(entity -> {
            entity.setPos(pPos.getX(), pPos.getY() + 1.2d, pPos.getZ());
            if(!pLevel.noCollision(entity) || !pLevel.isUnobstructed(entity)) return;
            pLevel.addFreshEntity(entity);
        });
    }

    private Optional<Entity> trySpawnMob(ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        WeightedRandomList<MobSpawnSettings.SpawnerData> hostileEntities = getHostileEntities(pLevel, pPos);
        if(hostileEntities.unwrap().isEmpty()) return Optional.empty();

        Optional<MobSpawnSettings.SpawnerData> spawnerData = hostileEntities.getRandom(pRandom);
        if(spawnerData.isPresent()){
            boolean canBeSpawn = SpawnPlacements.checkSpawnRules(spawnerData.get().type, pLevel, MobSpawnType.NATURAL, pPos, pLevel.getRandom());
            if(canBeSpawn){
                Entity entity = spawnerData.get().type.create(pLevel);
                if(entity instanceof Mob mob){
                    mob.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(pPos), MobSpawnType.NATURAL, null);
                    return Optional.of(entity);
                }
            }
        }
        return Optional.empty();
    }

    private static WeightedRandomList<MobSpawnSettings.SpawnerData> getHostileEntities(ServerLevel level, BlockPos pos) {
        return level.getChunkSource().getGenerator().getMobsAt(level.getBiome(pos), level.structureManager(), MobCategory.MONSTER, pos);
    }
}
