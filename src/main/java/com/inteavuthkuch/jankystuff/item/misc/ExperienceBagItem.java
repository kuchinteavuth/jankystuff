package com.inteavuthkuch.jankystuff.item.misc;

import com.inteavuthkuch.jankystuff.util.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ExperienceBagItem extends Item {
    public static final Properties PROPERTIES = new Properties()
            .stacksTo(1);
    public static final String NBT_ID = "StoredXP";
    public ExperienceBagItem() {
        super(PROPERTIES);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        CustomData data = pStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if(!data.isEmpty() && data.contains(NBT_ID)) {
            int storedXP = data.copyTag().getInt(NBT_ID);
            return storedXP > 0;
        }
        return false;
    }

    @ParametersAreNonnullByDefault
    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        pTooltipComponents.add(Component.translatable("item.jankystuff.experience_bag.description_1").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable("item.jankystuff.experience_bag.description_2").withStyle(ChatFormatting.GRAY));

        CustomData data = pStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if(!data.contains(NBT_ID))
            return;

        CompoundTag tag = data.copyTag();
        int storedXp = tag.getInt(NBT_ID);
        if(storedXp > 0) {
            pTooltipComponents.add(Component.empty()); // Make empty line in tooltip before showing stored level
            int level = PlayerUtil.getLevelFromXP(storedXp);
            pTooltipComponents.add(Component.translatable("item.jankystuff.experience_bag.stored_xp", level).withStyle(ChatFormatting.BLUE));
        }

        if(FMLEnvironment.dist == Dist.CLIENT){
            if(Minecraft.getInstance().player != null && Minecraft.getInstance().player.isCreative()){
                if (pTooltipFlag.isAdvanced()) {
                    if(!tag.isEmpty()){
                        // Show entire tag
                        pTooltipComponents.add(Component.literal("NBT: " + tag).withStyle(ChatFormatting.DARK_GRAY));
                    }
                }
            }
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(InteractionHand.MAIN_HAND); // Only if item in main-hand not allow on off-hand
        if(pUsedHand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.pass(stack);

        if(!pLevel.isClientSide()){
            CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            CompoundTag dataTag = data.copyTag();
            int storedXP = dataTag.getInt(NBT_ID);

            int currentXP = PlayerUtil.getPlayerTotalXP(pPlayer);
            int xpToNext = pPlayer.getXpNeededForNextLevel();

            if(pPlayer.isShiftKeyDown()){
                // 🔁 Retrieve 1 level if possible
                if (storedXP >= xpToNext) {
                    CustomData.update(DataComponents.CUSTOM_DATA, stack, nbt -> {
                        nbt.putInt(NBT_ID, storedXP - xpToNext);
                        pPlayer.giveExperiencePoints(xpToNext);
                        pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 0.8F);
                    });
                } else if (storedXP > 0) {
                    // Give remaining XP
                    CustomData.update(DataComponents.CUSTOM_DATA, stack, nbt -> {
                        nbt.putInt(NBT_ID, 0);
                        pPlayer.giveExperiencePoints(storedXP);
                        pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 0.8F);
                    });
                }
            }
            else{
                // 📥 Store 1 level if possible, else store remaining XP
                if (currentXP >= xpToNext) {
                    CustomData.update(DataComponents.CUSTOM_DATA, stack, nbt -> {
                        nbt.putInt(NBT_ID, storedXP + xpToNext);
                        pPlayer.giveExperiencePoints(-xpToNext);
                        pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                    });
                } else if (currentXP > 0) {
                    CustomData.update(DataComponents.CUSTOM_DATA, stack, nbt -> {
                        nbt.putInt(NBT_ID, storedXP + currentXP);
                        pPlayer.giveExperiencePoints(-currentXP);
                        pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                    });
                }
            }
        }
        // No need to send player feedback because Item has appendHoverText about level stored
        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
    }
}
