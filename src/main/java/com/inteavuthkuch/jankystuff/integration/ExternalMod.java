package com.inteavuthkuch.jankystuff.integration;

import com.inteavuthkuch.jankystuff.common.UpgradeType;
import net.neoforged.fml.ModList;

public enum ExternalMod {
    FARMER_DELIGHT("farmersdelight"),
    AE2("ae2"),
    CURIOS("curios");


    private final String modId;
    ExternalMod(String modId) {
        this.modId = modId;
    }

    public boolean isLoaded() {

        return ModList.get().isLoaded(modId);
    }
    public String modId() {
        return modId;
    }
}
