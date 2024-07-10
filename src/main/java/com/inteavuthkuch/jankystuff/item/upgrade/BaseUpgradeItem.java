package com.inteavuthkuch.jankystuff.item.upgrade;

import com.inteavuthkuch.jankystuff.blockentity.IUpgradableBlockEntity;
import com.inteavuthkuch.jankystuff.common.UpgradeType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseUpgradeItem extends Item {
    public BaseUpgradeItem(@NotNull Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(pContext.getLevel().isClientSide()) return InteractionResult.SUCCESS;

        BlockEntity blockEntity = pContext.getLevel().getBlockEntity(pContext.getClickedPos());
        if(blockEntity != null){
            if(blockEntity instanceof IUpgradableBlockEntity be){
                ItemStack itemStack = pContext.getItemInHand();
                BaseUpgradeItem item = (BaseUpgradeItem) itemStack.getItem();
                List<Integer> indexes = be.upgradeSlotsIndex();
                for(int index : indexes) {
                    if(be.canAccept(index, item.getUpgradeType())){
                        if(be.insertUpgrade(index, itemStack)){
                            return InteractionResult.CONSUME;
                        }
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    public BaseUpgradeItem() {
        this(new Properties());
    }

    public double energyUsage(){
        return 0;
    }

    public double speedUsage(){
        return 0;
    }

    public abstract UpgradeType getUpgradeType();
}
