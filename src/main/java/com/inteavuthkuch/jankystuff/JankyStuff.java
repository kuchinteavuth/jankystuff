package com.inteavuthkuch.jankystuff;

import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.blockentity.ModBlockEntity;
import com.inteavuthkuch.jankystuff.capability.CapabilityHandler;
import com.inteavuthkuch.jankystuff.config.JankyStuffCommonConfig;
import com.inteavuthkuch.jankystuff.integration.CuriosIntegration;
import com.inteavuthkuch.jankystuff.integration.ExternalMod;
import com.inteavuthkuch.jankystuff.item.ModItems;
import com.inteavuthkuch.jankystuff.common.loot.ModLootModifier;
import com.inteavuthkuch.jankystuff.menu.ModMenuType;
import com.inteavuthkuch.jankystuff.tab.ModTabs;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/**
 * Well I said now it become janky as the mod keep expanding lol
 * It's my first time trying to make a mod with NeoForge
 */
@Mod(JankyStuff.MODID)
public class JankyStuff
{
    public static final String MODID = "jankystuff";
    public static final Logger LOGGER = LogUtils.getLogger();
    public JankyStuff(IEventBus eventBus, ModContainer modContainer)
    {
        ModItems.ITEMS.register(eventBus);
        ModBlocks.BLOCKS.register(eventBus);
        ModTabs.CREATIVE_MODE_TABS.register(eventBus);
        ModLootModifier.GLOBAL_LOOT_MODIFIER.register(eventBus);
        ModBlockEntity.BLOCK_ENTITIES.register(eventBus);
        ModMenuType.MENUS.register(eventBus);

        CapabilityHandler.register(eventBus);

        if(ExternalMod.CURIOS.isLoaded()){
            // Prevent from required Curios if not installed on your client
            CuriosIntegration.register(eventBus);
        }

        // Register Configs Files
        modContainer.registerConfig(ModConfig.Type.COMMON, JankyStuffCommonConfig.SPEC, "jankystuff-common.toml");
    }
}
