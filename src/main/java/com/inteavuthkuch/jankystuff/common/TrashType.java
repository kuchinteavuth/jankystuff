package com.inteavuthkuch.jankystuff.common;

public enum TrashType {
    ITEM("item_trash",true, false, false),
    FLUID("fluid_trash",false, true, false),
    ENERGY("energy_trash",false, false, true),
    VOID("void_trash",true, true, true),

    ;

    private final String id;
    private final boolean itemCapability;
    private final boolean fluidCapability;
    private final boolean energyCapability;

    TrashType(String id, boolean itemCapability, boolean fluidCapability, boolean energyCapability) {
        this.id = id;
        this.itemCapability = itemCapability;
        this.fluidCapability = fluidCapability;
        this.energyCapability = energyCapability;
    }

    public boolean hasItemCapability() {
        return itemCapability;
    }

    public boolean hasFluidCapability() {
        return fluidCapability;
    }

    public boolean hasEnergyCapability() {
        return energyCapability;
    }

    public String getId() {
        return id;
    }
}
