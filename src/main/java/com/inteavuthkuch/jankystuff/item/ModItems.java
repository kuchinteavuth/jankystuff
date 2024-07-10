package com.inteavuthkuch.jankystuff.item;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.item.portable.MagnetItem;
import com.inteavuthkuch.jankystuff.item.portable.PortableCrateItem;
import com.inteavuthkuch.jankystuff.item.ring.*;
import com.inteavuthkuch.jankystuff.item.misc.FuelItem;
import com.inteavuthkuch.jankystuff.item.smithing.InfinitySmithingTemplateItem;
import com.inteavuthkuch.jankystuff.item.smithing.ReinforceSmithingTemplateItem;
import com.inteavuthkuch.jankystuff.item.tool.InfinityPickaxe;
import com.inteavuthkuch.jankystuff.item.tool.ModToolTiers;
import com.inteavuthkuch.jankystuff.item.tool.ReinforcedPickaxe;
import com.inteavuthkuch.jankystuff.item.tool.ReinforcedSword;
import com.inteavuthkuch.jankystuff.item.upgrade.SpeedUpgradeItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS;
    public static final DeferredItem<Item> ROASTED_APPLE;
    public static final DeferredItem<Item> DRIED_FLESH;
    public static final DeferredItem<Item> COAL_PIECE;
    public static final DeferredItem<Item> CHARCOAL_PIECE;
    public static final DeferredItem<Item> REINFORCED_COMPOUND;
    public static final DeferredItem<Item> REINFORCED_SMITHING_TEMPLATE;
    public static final DeferredItem<Item> INFINITY_SMITHING_TEMPLATE;
    public static final DeferredItem<Item> REINFORCED_IRON_PICKAXE;
    public static final DeferredItem<Item> REINFORCED_DIAMOND_PICKAXE;
    public static final DeferredItem<Item> REINFORCED_IRON_SWORD;
    public static final DeferredItem<Item> REINFORCED_DIAMOND_SWORD;
    public static final DeferredItem<Item> REINFORCED_NETHERITE_SWORD;
    public static final DeferredItem<Item> INFINITY_PICKAXE;
    public static final DeferredItem<Item> RING_OF_THE_SKY;
    public static final DeferredItem<Item> RING_OF_TRUE_SIGHT;
    public static final DeferredItem<Item> RING_OF_FIRE;
    public static final DeferredItem<Item> RING_OF_SATURATION;
    public static final DeferredItem<Item> RING_OF_REGENERATION;
    public static final DeferredItem<Item> RING_OF_WATER;
    public static final DeferredItem<Item> PORTABLE_CRATE;
    public static final DeferredItem<Item> MAGNET;
    public static final DeferredItem<Item> SPEED_UPGRADE;


    static {
        ITEMS = DeferredRegister.createItems(JankyStuff.MODID);
        ROASTED_APPLE = registerSimpleFoodItem("roasted_apple", Foods.BAKED_POTATO);
        DRIED_FLESH = ITEMS.registerSimpleItem("dried_flesh");
        COAL_PIECE = registerSimpleFuelItem("coal_piece", 200);
        CHARCOAL_PIECE = registerSimpleFuelItem("charcoal_piece", 200);
        REINFORCED_COMPOUND = ITEMS.registerSimpleItem("reinforced_compound");
        REINFORCED_SMITHING_TEMPLATE = ITEMS.register("reinforced_smithing_template", ReinforceSmithingTemplateItem::new);
        INFINITY_SMITHING_TEMPLATE = ITEMS.register("infinity_smithing_template", InfinitySmithingTemplateItem::new);
        REINFORCED_IRON_PICKAXE = ITEMS.register("reinforced_iron_pickaxe", () -> new ReinforcedPickaxe(ModToolTiers.REINFORCED_IRON, 1.0f, -2.0f));
        REINFORCED_DIAMOND_PICKAXE = ITEMS.register("reinforced_diamond_pickaxe", () -> new ReinforcedPickaxe(ModToolTiers.REINFORCED_DIAMOND, 1.0f, -2.0f));
        REINFORCED_IRON_SWORD = ITEMS.register("reinforced_iron_sword", () -> new ReinforcedSword(ModToolTiers.REINFORCED_IRON, 6, -2.4F));
        REINFORCED_DIAMOND_SWORD = ITEMS.register("reinforced_diamond_sword", () -> new ReinforcedSword(ModToolTiers.REINFORCED_DIAMOND, 8, -1.4F));
        REINFORCED_NETHERITE_SWORD = ITEMS.register("reinforced_netherite_sword", () -> new ReinforcedSword(ModToolTiers.REINFORCED_NETHERITE, 10, -1.4F));
        INFINITY_PICKAXE  = ITEMS.register("infinity_pickaxe", () -> new InfinityPickaxe(2.0f, 2.0f));
        RING_OF_THE_SKY = ITEMS.register("ring_of_the_sky", RingOfTheSkyItem::new);
        RING_OF_TRUE_SIGHT = ITEMS.register("ring_of_true_sight", RingOfTrueSightItem::new);
        RING_OF_FIRE = ITEMS.register("ring_of_fire", RingOfFireItem::new);
        RING_OF_SATURATION = ITEMS.register("ring_of_saturation", RingOfSaturationItem::new);
        RING_OF_REGENERATION = ITEMS.register("ring_of_regeneration", RingOfRegenerationItem::new);
        RING_OF_WATER = ITEMS.register("ring_of_water", RingOfWaterItem::new);
        PORTABLE_CRATE = ITEMS.register("portable_crate", PortableCrateItem::new);
        MAGNET = ITEMS.register("magnet", MagnetItem::new);
        SPEED_UPGRADE = ITEMS.register("speed_upgrade", SpeedUpgradeItem::new);
    }

    protected static DeferredItem<Item> registerSimpleFuelItem(String name, int burnTime) {
        return ITEMS.register(name, () -> new FuelItem(burnTime));
    }
    public static DeferredItem<Item> registerSimpleFoodItem(String name, FoodProperties properties) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().food(properties)));
    }
}
