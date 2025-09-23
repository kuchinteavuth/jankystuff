package com.inteavuthkuch.jankystuff.block.dirt;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.config.JankyStuffCommonConfig;
import com.inteavuthkuch.jankystuff.tag.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

public class CorruptedDirtBlock extends Block {

    public CorruptedDirtBlock() {
        super(Blocks.DIRT.properties());
    }

    @ParametersAreNonnullByDefault
    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTootipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTootipComponents, pTooltipFlag);
        pTootipComponents.add(Component.translatable("block.jankystuff.corrupted_dirt.description").withStyle(ChatFormatting.GRAY));
    }

    @ParametersAreNonnullByDefault
    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        pLevel.scheduleTick(pPos, pState.getBlock(), JankyStuffCommonConfig.CORRUPTED_DIRT_SPAWN_DELAY.get());
    }

    @ParametersAreNonnullByDefault
    @Override
    protected void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if(!pLevel.isAreaLoaded(pPos, 3)
                || !pLevel.getFluidState(pPos.above()).isEmpty()
                || pLevel.getLevelData().getDifficulty() == Difficulty.PEACEFUL
                || !pLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)
                || pLevel.getMaxLocalRawBrightness(pPos.above()) > 6
        ) {
            pLevel.scheduleTick(pPos, pState.getBlock(), JankyStuffCommonConfig.CORRUPTED_DIRT_SPAWN_DELAY.get());
            return;
        }

        int totalEntity = pLevel.getEntitiesOfClass(LivingEntity.class,
                new AABB(pPos.getX() - 3,
                        pPos.getY(),
                        pPos.getZ() - 3,
                        pPos.getX() + 3,
                        pPos.getY() + 1,
                        pPos.getZ() + 3))
                .size();

        if(totalEntity > JankyStuffCommonConfig.CORRUPTED_DIRT_MAX_ENTITY.get()) {
            pLevel.scheduleTick(pPos, pState.getBlock(), JankyStuffCommonConfig.CORRUPTED_DIRT_SPAWN_DELAY.get());
            return;
        }

        mobToSpawn(pLevel, pPos, pRandom).ifPresent(entity -> {
            entity.setPos(pPos.getX(), pPos.getY() + 1.2d, pPos.getZ());
            if(!pLevel.noCollision(entity) || !pLevel.isUnobstructed(entity)) return;
            pLevel.addFreshEntity(entity);
        });

        // Schedule spawn again after X amount of ticks
        pLevel.scheduleTick(pPos, pState.getBlock(), JankyStuffCommonConfig.CORRUPTED_DIRT_SPAWN_DELAY.get());
    }

    private Optional<Entity> mobToSpawn(ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        WeightedRandomList<MobSpawnSettings.SpawnerData> hostileEntities = getHostileEntities(pLevel, pPos);
        if(hostileEntities.unwrap().isEmpty()) return Optional.empty();

        Optional<MobSpawnSettings.SpawnerData> spawnerData = hostileEntities.getRandom(pRandom);
        if(spawnerData.isPresent()){
            boolean canBeSpawn = SpawnPlacements.checkSpawnRules(spawnerData.get().type, pLevel, MobSpawnType.NATURAL, pPos, pLevel.getRandom());
            if(canBeSpawn){
                Entity entity = spawnerData.get().type.create(pLevel);
                if(entity instanceof Mob mob){
                    //noinspection deprecation,OverrideOnly
                    mob.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(pPos), MobSpawnType.NATURAL, null);
                    int weakestMob = pRandom.nextInt(100);
                    // Once a while will spawn "The weakest one"
                    if (weakestMob == 69) {
                        settingUpWeakestMob(mob, pLevel);
                        // This is the most useless log to get mob id but just want to know if it spawned
                        JankyStuff.LOGGER.info("Spawning 'The Weakest One': {}", mob.getId());
                    } else {
                        mob.setSilent(true);
                        mob.targetSelector.removeAllGoals(x -> true);
                        mob.goalSelector.removeAllGoals(x -> true);
                        mob.goalSelector.addGoal(1, new RandomStrollGoal((PathfinderMob) mob, 1));
                        mob.goalSelector.addGoal(2, new RandomLookAroundGoal(mob));
                    }
                    return Optional.of(entity);
                }
            }
        }
        return Optional.empty();
    }

    private void settingUpWeakestMob(Mob mob, ServerLevel pLevel) {
        try {
            Registry<Enchantment> registry = pLevel.registryAccess().registryOrThrow(Registries.ENCHANTMENT);

            ItemStack helmet = new ItemStack(Items.NETHERITE_HELMET);
            ItemStack chestPlate = new ItemStack(Items.NETHERITE_CHESTPLATE);
            ItemStack leggings = new ItemStack(Items.NETHERITE_LEGGINGS);
            ItemStack boot = new ItemStack(Items.NETHERITE_BOOTS);
            ItemStack sword = new ItemStack(Items.NETHERITE_SWORD);

            registry.getHolder(Enchantments.SHARPNESS)
                    .ifPresent(sharpnessEnchantment
                            -> sword.enchant(sharpnessEnchantment, 5));

            registry.getHolder(Enchantments.PROTECTION)
                    .ifPresent(enchantment -> {
                        helmet.enchant(enchantment, 5);
                        chestPlate.enchant(enchantment, 5);
                        leggings.enchant(enchantment, 5);
                        boot.enchant(enchantment, 5);
                    });

            mob.equipItemIfPossible(sword);
            mob.setDropChance(EquipmentSlot.MAINHAND, 0.0f);

            mob.setItemSlot(EquipmentSlot.HEAD, helmet);
            mob.setDropChance(EquipmentSlot.HEAD, 0.0f);

            mob.setItemSlot(EquipmentSlot.CHEST, chestPlate);
            mob.setDropChance(EquipmentSlot.CHEST, 0.0f);

            mob.setItemSlot(EquipmentSlot.LEGS, leggings);
            mob.setDropChance(EquipmentSlot.LEGS, 0.0f);

            mob.setItemSlot(EquipmentSlot.FEET, boot);
            mob.setDropChance(EquipmentSlot.FEET, 0.0f);

            mob.setCustomName(Component.literal("The weakest one").withStyle(ChatFormatting.DARK_RED));
            mob.setCustomNameVisible(true);

        }
        catch (IllegalStateException ex) {
            // Error message already written from registryOrThrow
            // If any error then mob will not equip any armor or sword
            JankyStuff.LOGGER.error("Error during setting up mob: {}", ex.getMessage());
        }
        finally {
            // no matter what error or not op mob will always get 100HP
            mob.setHealth(100);
        }
    }

    private static WeightedRandomList<MobSpawnSettings.SpawnerData> getHostileEntities(ServerLevel level, BlockPos pos) {
        return level.getChunkSource().getGenerator().getMobsAt(level.getBiome(pos), level.structureManager(), MobCategory.MONSTER, pos);
    }
}
