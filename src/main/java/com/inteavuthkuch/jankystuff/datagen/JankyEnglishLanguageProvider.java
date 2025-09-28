package com.inteavuthkuch.jankystuff.datagen;

import com.inteavuthkuch.jankystuff.JankyStuff;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class JankyEnglishLanguageProvider extends LanguageProvider {
    public JankyEnglishLanguageProvider(PackOutput output) {
        super(output, JankyStuff.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {

    }
}
