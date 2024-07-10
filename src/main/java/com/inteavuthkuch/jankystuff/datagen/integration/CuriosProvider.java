package com.inteavuthkuch.jankystuff.datagen.integration;

import com.inteavuthkuch.jankystuff.JankyStuff;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class CuriosProvider extends CuriosDataProvider {

    public CuriosProvider(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(JankyStuff.MODID, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {

        this.createSlot("ring")
                .size(4)
                .order(500)
                .renderToggle(false)
                .addCosmetic(false)
                .useNativeGui(true);

        this.createSlot("hands")
                .size(2)
                .useNativeGui(true)
                .addCosmetic(false)
                .replace(false)
                .renderToggle(false);

        this.createEntities("janky")
                .addPlayer()
                .addSlots("ring", "hands");
    }
}
