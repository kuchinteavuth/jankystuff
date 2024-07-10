package com.inteavuthkuch.jankystuff.tab;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, JankyStuff.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> JANKY_TAB = CREATIVE_MODE_TABS.register("jankystuff_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("tab.jankystuff.main"))
        .withTabsBefore(CreativeModeTabs.COMBAT)
        .icon(() -> ModItems.RING_OF_THE_SKY.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            ModItems.ITEMS.getEntries().forEach(entry -> {
                output.accept(entry.get());
            });
        }).build());

}
