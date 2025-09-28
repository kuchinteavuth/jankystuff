package com.inteavuthkuch.jankystuff.common;

import com.inteavuthkuch.jankystuff.util.ResourceLocationUtil;

public enum UserInterface {
    CONTAINER_11_BY_8("container_11x8.png", 212, 256,256, 256),
    BASIC_QUARRY("basic_quarry_gui.png", 203, 166,256, 256),
    ADVANCED_QUARRY("advanced_quarry.png", 176, 166,256, 256),
    BLOCK_BREAKER("block_breaker.png", 203, 166,256, 256),
    BASIC_ITEM_FILTER("basic_item_filter.png", 176, 166, 256, 256),
    CONTAINER_13_BY_9("container_13x9.png", 242,256,256,256),
    FARM_SIMULATION("farm_simulation.png", 176, 166, 256, 256),
    FARM_SIMULATION_PROGRESS("sprites/farm_simulation_progress.png", 34, 16, 34, 16)
    ;

    private final Texture texture;

    UserInterface(String guiName, int imgW, int imgH, int textureW, int textureH){
        this.texture = Texture.create(ResourceLocationUtil.gui(guiName), imgW, imgH,textureW, textureH);
    }

    public Texture getTexture() {
        return this.texture;
    }
}
