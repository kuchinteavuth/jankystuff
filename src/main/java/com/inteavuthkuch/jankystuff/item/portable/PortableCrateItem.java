package com.inteavuthkuch.jankystuff.item.portable;

import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.inventory.SaveDataContainer;
import com.inteavuthkuch.jankystuff.item.ModItems;
import com.inteavuthkuch.jankystuff.menu.PortableCrateMenu;
import com.inteavuthkuch.jankystuff.util.ComponentUtil;
import com.inteavuthkuch.jankystuff.util.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PortableCrateItem extends Item {

    public PortableCrateItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        handleGUI(pLevel, pPlayer, pUsedHand);
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        pTooltipComponents.add(ComponentUtil.translateItem(ModItems.PORTABLE_CRATE, "description1").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(ComponentUtil.translateItem(ModItems.PORTABLE_CRATE, "description2").withStyle(ChatFormatting.DARK_RED));

    }

    private void handleGUI(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemInHand = pPlayer.getItemInHand(pHand);
        if(!pLevel.isClientSide() && pPlayer instanceof ServerPlayer && itemInHand.getItem() instanceof PortableCrateItem){

            SaveDataContainer container = new SaveDataContainer(ContainerType.PORTABLE.getSize(), itemInHand);

            MenuProvider menu = new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return ComponentUtil.translateItem(ContainerType.PORTABLE.getId());
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
                    return new PortableCrateMenu(pContainerId, pPlayerInventory, container);
                }
            };

            PlayerUtil.tryOpenMenu(pPlayer, menu);
        }
    }
}
