package com.inteavuthkuch.jankystuff.datagen;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.datagen.integration.CuriosProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = JankyStuff.MODID)
public class JankyDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Wired up all providers
        generator.addProvider(event.includeClient(), new JankyItemModelProvider(output, fileHelper));
        generator.addProvider(event.includeClient(), new JankyBlockStateProvider(output, fileHelper));

        JankyBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new JankyBlockTagProvider(output, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new JankyItemTagProvider(output, lookupProvider, blockTagProvider.contentsGetter()));
        generator.addProvider(event.includeServer(), new CuriosProvider(output, fileHelper, lookupProvider));
        generator.addProvider(event.includeServer(), new JankyGlobalLootModifierProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), JankyBlockLootTableProvider.create(output, lookupProvider));
        generator.addProvider(event.includeServer(), new JankyRecipeProvider(output, lookupProvider));
    }
}
