package com.inteavuthkuch.jankystuff.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    private static final String KEY_CATEGORY_MAIN = "key.jankystuff.category.main";
    private static final String KEY_TOGGLE_MAGNET = "key.jankystuff.toggle_magnet";
    private static final String KEY_OPEN_PERSONAL_CRATE = "key.jankystuff.open_personal_crate";

    public static final KeyMapping TOGGLE_MAGNET;
    public static final KeyMapping OPEN_PERSONAL_CRATE;

    static {
        TOGGLE_MAGNET = new KeyMapping(KEY_TOGGLE_MAGNET, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Y, KEY_CATEGORY_MAIN);
        OPEN_PERSONAL_CRATE = new KeyMapping(KEY_OPEN_PERSONAL_CRATE, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, KEY_CATEGORY_MAIN);
    }
}
