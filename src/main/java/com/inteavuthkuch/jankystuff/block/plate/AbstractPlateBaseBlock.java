package com.inteavuthkuch.jankystuff.block.plate;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractPlateBaseBlock extends Block {

    public static final GameProfile DEFAULT_PROFILE = new GameProfile(UUID.fromString("10064d02-d194-4854-8a3b-e46a0555c5b9"), "MobDamagePlate");
    public static final VoxelShape PLATE_SHAPE = Block.box(0.0,0.0,0.0,16.0,1.0,16.0);

    protected FakePlayer fakePlayer;
    protected final boolean isPlayerDamage;
    protected final GameProfile profile;

    public AbstractPlateBaseBlock() {
        this(false, DEFAULT_PROFILE);
    }

    public AbstractPlateBaseBlock(boolean isPlayerDamage, GameProfile profile) {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .strength(1.5F, 6.0F)
                .isValidSpawn(AbstractPlateBaseBlock::always)
        );

        this.isPlayerDamage = isPlayerDamage;
        this.profile = profile;
    }

    protected static boolean always(BlockState pState, BlockGetter pGetter, BlockPos pPos, EntityType<?> pEntity) {
        return true;
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AbstractPlateBaseBlock.PLATE_SHAPE;
    }

    @Override
    protected void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if(pLevel.isClientSide() || !(pEntity instanceof LivingEntity entity)) return;
        if(pEntity instanceof Player) return; // Ignore player

        if(pLevel instanceof ServerLevel level) {
            if(isPlayerDamage) {
                if(fakePlayer != null){
                    fakePlayer.setServerLevel(level);
                    fakePlayer.setPos(pPos.getX() + 0.5d, pPos.getY() + 0.5d, pPos.getZ() + 0.5d);
                    fakePlayer.attack(entity);
                    fakePlayer.setDeltaMovement(entity.getDeltaMovement().multiply(0, 1,0));
                    entity.setLastHurtByPlayer(fakePlayer);
                }
                else {
                    fakePlayer = new FakePlayer(level, this.profile);
                    Objects.requireNonNull(fakePlayer.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(100);
                    Objects.requireNonNull(fakePlayer.getAttribute(Attributes.ATTACK_SPEED)).setBaseValue(100);
                }
            }
            else {
                pEntity.hurt(pLevel.damageSources().cactus(), 15);
            }

        }

        super.entityInside(pState, pLevel, pPos, pEntity);
    }
}
