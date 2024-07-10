package com.inteavuthkuch.jankystuff.block;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.crate.MetalCrateBlock;
import com.inteavuthkuch.jankystuff.block.crate.WoodenCrateBlock;
import com.inteavuthkuch.jankystuff.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS;
    public static final DeferredBlock<Block> TICK_ACCELERATOR;
    public static final DeferredBlock<Block> WOODEN_CRATE;
    public static final DeferredBlock<Block> METAL_CRATE;
    public static final DeferredBlock<Block> LAMP;
    public static final DeferredBlock<Block> BASIC_QUARRY;

    static {
        BLOCKS = DeferredRegister.createBlocks(JankyStuff.MODID);
        TICK_ACCELERATOR = registerBlockWithItem("tick_accelerator", TickAccelerator::new);
        WOODEN_CRATE = registerBlockWithItem("wooden_crate", WoodenCrateBlock::new);
        METAL_CRATE = registerBlockWithItem("metal_crate", MetalCrateBlock::new);
        LAMP = registerBlockWithItem("lamp", LampBlock::new);
        BASIC_QUARRY = registerBlockWithItem("basic_quarry", BasicQuarryBlock::create, new Item.Properties().stacksTo(1));
    }

    private static @NotNull DeferredBlock<Block> registerBlockWithItem(String name, Supplier<? extends Block> supplier) {
        DeferredBlock<Block> block = BLOCKS.register(name, supplier);
        ModItems.ITEMS.registerSimpleBlockItem(name, block);
        return block;
    }

    private static @NotNull DeferredBlock<Block> registerBlockWithItem(String name, Supplier<? extends Block> supplier, Item.Properties itemProps) {
        DeferredBlock<Block> block = BLOCKS.register(name, supplier);
        ModItems.ITEMS.registerSimpleBlockItem(name, block, itemProps);
        return block;
    }
}
