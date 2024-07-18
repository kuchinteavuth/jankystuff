package com.inteavuthkuch.jankystuff.menu;

import com.inteavuthkuch.jankystuff.JankyStuff;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuType {
    public static final DeferredRegister<MenuType<?>> MENUS;
    public static final DeferredHolder<MenuType<?>, MenuType<WoodenCrateMenu>> WOODEN_CRATE;
    public static final DeferredHolder<MenuType<?>, MenuType<MetalCrateMenu>> METAL_CRATE;
    public static final DeferredHolder<MenuType<?>, MenuType<PortableCrateMenu>> PORTABLE_CRATE;
    public static final DeferredHolder<MenuType<?>, MenuType<BasicQuarryMenu>> BASIC_QUARRY;
    public static final DeferredHolder<MenuType<?>, MenuType<BlockBreakerMenu>> BLOCK_BREAKER;

    static {
        MENUS = DeferredRegister.create(Registries.MENU, JankyStuff.MODID);
        WOODEN_CRATE = MENUS.register("wooden_crate", () -> new MenuType<>(WoodenCrateMenu::new, FeatureFlags.REGISTRY.allFlags()));
        METAL_CRATE = MENUS.register("metal_crate", () -> new MenuType<>(MetalCrateMenu::new, FeatureFlags.REGISTRY.allFlags()));
        PORTABLE_CRATE = MENUS.register("portable_crate", () -> new MenuType<>(PortableCrateMenu::new, FeatureFlags.REGISTRY.allFlags()));
        BASIC_QUARRY = MENUS.register("basic_quarry", () -> new MenuType<>(BasicQuarryMenu::new, FeatureFlags.REGISTRY.allFlags()));
        BLOCK_BREAKER = MENUS.register("block_breaker", () -> new MenuType<>(BlockBreakerMenu::new, FeatureFlags.REGISTRY.allFlags()));
    }
}
