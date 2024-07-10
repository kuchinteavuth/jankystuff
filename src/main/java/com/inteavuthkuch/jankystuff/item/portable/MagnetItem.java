package com.inteavuthkuch.jankystuff.item.portable;

import com.inteavuthkuch.jankystuff.config.JankyStuffCommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.List;

public class MagnetItem extends Item {
    public MagnetItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public static void toggleMagnet(Player pPlayer, ItemStack itemStack, boolean sendClientMessage) {
        if(itemStack.getItem() instanceof MagnetItem){
            CustomData component = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            CompoundTag tag = component.copyTag();

            pPlayer.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.5f, tag.getBoolean("enabled") ? 0.8f: 1.2f);

            tag.putBoolean("enabled", !tag.getBoolean("enabled"));
            component = CustomData.of(tag);
            itemStack.set(DataComponents.CUSTOM_DATA, component);

            if(sendClientMessage){
                String magnetStateString = tag.getBoolean("enabled") ? "state.jankystuff.on" : "state.jankystuff.off";
                pPlayer.displayClientMessage(Component.translatable("item.jankystuff.magnet.state", Component.translatable(magnetStateString)), true);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(Component.translatable("item.jankystuff.magnet.description", JankyStuffCommonConfig.MAGNET_RANGE.get()).withStyle(ChatFormatting.GRAY));

        CompoundTag tag = pStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        String magnetStateString = tag.getBoolean("enabled") ? "state.jankystuff.on" : "state.jankystuff.off";
        pTooltipComponents.add(Component.translatable("item.jankystuff.magnet.state", Component.translatable(magnetStateString)));

        pTooltipComponents.add(Component.empty());

        pTooltipComponents.add(Component.literal("Current texture has not yet finalized"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);

        MagnetItem.toggleMagnet(pPlayer, itemStack, true);

        return new InteractionResultHolder<>(InteractionResult.CONSUME, itemStack);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        CustomData component = pStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return component.copyTag().getBoolean("enabled");
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        CustomData component = pStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = component.copyTag();

        if(pEntity instanceof Player && tag.getBoolean("enabled")) {
            int range = JankyStuffCommonConfig.MAGNET_RANGE.get();

            // Find entities around configured radius
            List<ItemEntity> itemEntities = pLevel.getEntitiesOfClass(ItemEntity.class, pEntity.getBoundingBox().inflate(range));
            for(ItemEntity itemEntity : itemEntities) {

                Entity owner = itemEntity.getOwner();
                if(owner != null && owner.getUUID().equals(pEntity.getUUID()) && itemEntity.hasPickUpDelay())
                    continue;

                if(!pLevel.isClientSide()){
                    itemEntity.setNoPickUpDelay();
                    itemEntity.setPos(pEntity.getX(), pEntity.getY(), pEntity.getZ());
                }
            }

            // Handle Experience Pick-up
            List<ExperienceOrb> orbs = pLevel.getEntitiesOfClass(ExperienceOrb.class, pEntity.getBoundingBox().inflate(range));
            for(ExperienceOrb orb : orbs) {
                if(!pLevel.isClientSide()) {
                    orb.setPos(pEntity.getX(), pEntity.getY(), pEntity.getZ());
                }
            }
        }
    }
}
