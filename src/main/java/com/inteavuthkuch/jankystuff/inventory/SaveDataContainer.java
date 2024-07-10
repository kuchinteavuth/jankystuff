package com.inteavuthkuch.jankystuff.inventory;

import com.inteavuthkuch.jankystuff.common.ContainerType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

/**
 * Use for portable crate for saving item data into item stack
 */
public final class SaveDataContainer extends SimpleContainer {

    private final ItemStack parent;

    public SaveDataContainer(int size, ItemStack parent) {
        super(size);
        this.parent = parent;

        if(parent.has(DataComponents.CONTAINER)){
            ItemContainerContents component = parent.get(DataComponents.CONTAINER);
            if(component != null){
                for(int i=0;i<component.getSlots();i++){
                    setItem(i, component.getStackInSlot(i));
                }
            }
        }
    }

    public SaveDataContainer(ContainerType containerType, ItemStack parent) {
        this(containerType.getSize(), parent);
    }

    @Override
    public void stopOpen(Player pPlayer) {
        ItemContainerContents updatedComponent = ItemContainerContents.fromItems(getItems());
        parent.set(DataComponents.CONTAINER, updatedComponent);
        super.stopOpen(pPlayer);
    }
}
