package com.inteavuthkuch.jankystuff.block;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.blockaccelerator.AdvanceBlockAccelerator;
import com.inteavuthkuch.jankystuff.block.blockaccelerator.BasicBlockAccelerator;
import com.inteavuthkuch.jankystuff.block.blockaccelerator.EliteBlockAccelerator;
import com.inteavuthkuch.jankystuff.block.blockaccelerator.UltimateBlockAccelerator;
import com.inteavuthkuch.jankystuff.block.crate.MetalCrateBlock;
import com.inteavuthkuch.jankystuff.block.crate.WoodenCrateBlock;
import com.inteavuthkuch.jankystuff.block.dirt.CorruptedDirtBlock;
import com.inteavuthkuch.jankystuff.block.plate.AdvanceDamagePlateBlock;
import com.inteavuthkuch.jankystuff.block.plate.MobDamagePlateBlock;
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
    public static final DeferredBlock<Block> CORRUPTED_DIRT;
    public static final DeferredBlock<Block> BLOCK_BREAKER;
    public static final DeferredBlock<Block> PASSTHROUGH_GLASS;
    public static final DeferredBlock<Block> MOB_DAMAGE_PLATE;
    public static final DeferredBlock<Block> ADVANCE_DAMAGE_PLATE;
    public static final DeferredBlock<Block> MINER_LIGHT;
    public static final DeferredBlock<Block> BASIC_BLOCK_ACCELERATOR;
    public static final DeferredBlock<Block> ADVANCE_BLOCK_ACCELERATOR;
    public static final DeferredBlock<Block> ELITE_BLOCK_ACCELERATOR;
    public static final DeferredBlock<Block> ULTIMATE_BLOCK_ACCELERATOR;

    static {
        BLOCKS = DeferredRegister.createBlocks(JankyStuff.MOD_ID);
        TICK_ACCELERATOR = registerBlockWithItem("tick_accelerator", TickAccelerator::new, TickAccelerator.ITEM_PROPERTY);
        WOODEN_CRATE = registerBlockWithItem("wooden_crate", WoodenCrateBlock::new);
        METAL_CRATE = registerBlockWithItem("metal_crate", MetalCrateBlock::new);
        LAMP = registerBlockWithItem("lamp", LampBlock::new);
        BASIC_QUARRY = registerBlockWithItem("basic_quarry", BasicQuarryBlock::create, new Item.Properties().stacksTo(1));
        CORRUPTED_DIRT = registerBlockWithItem("corrupted_dirt", CorruptedDirtBlock::new);
        BLOCK_BREAKER = registerBlockWithItem("block_breaker", BlockBreakerBlock::new);
        PASSTHROUGH_GLASS = registerBlockWithItem("passthrough_tinted_glass", PassthroughGlassBlock::new);
        MOB_DAMAGE_PLATE = registerBlockWithItem("mob_damage_plate", MobDamagePlateBlock::new);
        ADVANCE_DAMAGE_PLATE = registerBlockWithItem("advance_damage_plate", AdvanceDamagePlateBlock::new);

        MINER_LIGHT = BLOCKS.register("miner_light", MinerLightBlock::new); // Only Block without Item
        BASIC_BLOCK_ACCELERATOR =  registerBlockWithItem("basic_block_accelerator", BasicBlockAccelerator::new);
        ADVANCE_BLOCK_ACCELERATOR =  registerBlockWithItem("advance_block_accelerator", AdvanceBlockAccelerator::new);
        ELITE_BLOCK_ACCELERATOR =  registerBlockWithItem("elite_block_accelerator", EliteBlockAccelerator::new);
        ULTIMATE_BLOCK_ACCELERATOR =  registerBlockWithItem("ultimate_block_accelerator", UltimateBlockAccelerator::new);
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
