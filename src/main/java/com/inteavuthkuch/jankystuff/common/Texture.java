package com.inteavuthkuch.jankystuff.common;

import net.minecraft.resources.ResourceLocation;

public record Texture(ResourceLocation location, int imageWidth, int imageHeight, int textureWidth, int textureHeight) {
    public static Texture create(ResourceLocation location, int imageWidth, int imageHeight, int textureWidth, int textureHeight) {
        return new Texture(location, imageWidth, imageHeight, textureWidth, textureHeight);
    }
}
