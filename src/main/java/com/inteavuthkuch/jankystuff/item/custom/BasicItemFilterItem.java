package com.inteavuthkuch.jankystuff.item.custom;

import com.inteavuthkuch.jankystuff.component.ModComponentTypes;
import com.inteavuthkuch.jankystuff.component.custom.FilterTypeComponent;
import com.inteavuthkuch.jankystuff.inventory.SaveDataContainer;
import com.inteavuthkuch.jankystuff.menu.BasicItemFilterMenu;
import com.inteavuthkuch.jankystuff.network.packet.PlayerPlaySoundPacket;
import com.inteavuthkuch.jankystuff.util.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicItemFilterItem extends Item implements IFilterItem {


    public BasicItemFilterItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        FilterTypeComponent filterType = pStack.getOrDefault(ModComponentTypes.FILTER_TYPE, FilterTypeComponent.BLACKLIST);
        MutableComponent status = Component.translatable("text.jankystuff.filter_type").append(CommonComponents.space());
        String filterTypeKey = filterType.blackList() ? "button.jankystuff.blacklist" : "button.jankystuff.whitelist";
        status.append(Component.translatable(filterTypeKey).withStyle(ChatFormatting.BOLD));
        pTooltipComponents.add(status);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemInHand = pPlayer.getItemInHand(pUsedHand);
        if(pLevel instanceof ServerLevel && itemInHand.getItem() instanceof IFilterItem) {
            if(pPlayer.isShiftKeyDown()){
                FilterTypeComponent filterType = itemInHand.getOrDefault(ModComponentTypes.FILTER_TYPE, FilterTypeComponent.BLACKLIST);
                itemInHand.set(ModComponentTypes.FILTER_TYPE, filterType.blackList() ? FilterTypeComponent.WHITELIST : FilterTypeComponent.BLACKLIST);
                PacketDistributor.sendToPlayer((ServerPlayer) pPlayer, new PlayerPlaySoundPacket(
                        SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f
                ));
            }
            else{
                SaveDataContainer container = new SaveDataContainer(27, itemInHand);
                MenuProvider menu = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("item.jankystuff.basic_item_filter");
                    }

                    @Override
                    public @NotNull AbstractContainerMenu createMenu(int pContainerId, Inventory playerInventory, Player player) {
                        return new BasicItemFilterMenu(pContainerId, playerInventory, container);
                    }
                };

                PlayerUtil.tryOpenMenu(pPlayer, menu);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
