package com.inteavuthkuch.jankystuff.util;

import com.inteavuthkuch.jankystuff.JankyStuff;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ResourceLocationUtil {

    public static @NotNull ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(JankyStuff.MODID, path);
    }

    public static @NotNull ResourceLocation gui(String guiName) {
        return path("textures/gui/" + guiName);
    }
}
