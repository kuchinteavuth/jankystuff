package com.inteavuthkuch.jankystuff.block;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.blockaccelerator.AdvancedBlockAccelerator;
import com.inteavuthkuch.jankystuff.block.blockaccelerator.BasicBlockAccelerator;
import com.inteavuthkuch.jankystuff.block.blockaccelerator.EliteBlockAccelerator;
import com.inteavuthkuch.jankystuff.block.blockaccelerator.UltimateBlockAccelerator;
import com.inteavuthkuch.jankystuff.block.crate.MetalCrateBlock;
import com.inteavuthkuch.jankystuff.block.crate.WoodenCrateBlock;
import com.inteavuthkuch.jankystuff.block.custom.FarmSimulationBlock;
import com.inteavuthkuch.jankystuff.block.custom.JankyAmethystBlock;
import com.inteavuthkuch.jankystuff.block.dirt.CorruptedDirtBlock;
import com.inteavuthkuch.jankystuff.block.fluidtank.AdvancedFluidTank;
import com.inteavuthkuch.jankystuff.block.fluidtank.BasicFluidTank;
import com.inteavuthkuch.jankystuff.block.fluidtank.EliteFluidTank;
import com.inteavuthkuch.jankystuff.block.fluidtank.UltimateFluidTank;
import com.inteavuthkuch.jankystuff.block.plate.AdvanceDamagePlateBlock;
import com.inteavuthkuch.jankystuff.block.plate.MobDamagePlateBlock;
import com.inteavuthkuch.jankystuff.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiFunction;
import java.util.function.Function;
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
    public static final DeferredBlock<Block> ADVANCED_BLOCK_ACCELERATOR;
    public static final DeferredBlock<Block> ELITE_BLOCK_ACCELERATOR;
    public static final DeferredBlock<Block> ULTIMATE_BLOCK_ACCELERATOR;
    public static final DeferredBlock<Block> BASIC_FLUID_TANK;
    public static final DeferredBlock<Block> ADVANCED_FLUID_TANK;
    public static final DeferredBlock<Block> ELITE_FLUID_TANK;
    public static final DeferredBlock<Block> ULTIMATE_FLUID_TANK;
    public static final DeferredBlock<Block> WATER_SOURCE;
    public static final DeferredBlock<Block> SKY_SHIFTER;
    public static final DeferredBlock<Block> ADVANCED_QUARRY;
    public static final DeferredBlock<Block> SMALL_CERAMETRON_BUD;
    public static final DeferredBlock<Block> MEDIUM_CERAMETRON_BUD;
    public static final DeferredBlock<Block> LARGE_CERAMETRON_BUD;
    public static final DeferredBlock<Block> CERAMETRON_CLUSTER;
    public static final DeferredBlock<Block> BUDDING_CERAMETRON;
    public static final DeferredBlock<Block> CERAMETRON_CLUSTER_BLOCK;
    public static final DeferredBlock<Block> FARM_SIMULATION;


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
        ADVANCED_BLOCK_ACCELERATOR =  registerBlockWithItem("advanced_block_accelerator", AdvancedBlockAccelerator::new);
        ELITE_BLOCK_ACCELERATOR =  registerBlockWithItem("elite_block_accelerator", EliteBlockAccelerator::new);
        ULTIMATE_BLOCK_ACCELERATOR =  registerBlockWithItem("ultimate_block_accelerator", UltimateBlockAccelerator::new);
        BASIC_FLUID_TANK = registerBlockWithItem("basic_fluid_tank", BasicFluidTank::new);
        ADVANCED_FLUID_TANK = registerBlockWithItem("advanced_fluid_tank", AdvancedFluidTank::new);
        ELITE_FLUID_TANK = registerBlockWithItem("elite_fluid_tank", EliteFluidTank::new);
        ULTIMATE_FLUID_TANK = registerBlockWithItem("ultimate_fluid_tank", UltimateFluidTank::new);
        WATER_SOURCE = registerBlockWithItem("water_source", WaterSourceBlock::new);
        SKY_SHIFTER = registerBlockWithItem("skyshifter", Skyshifter::new);
        ADVANCED_QUARRY = registerBlockWithItem("advanced_quarry", AdvancedQuarryBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(BASIC_QUARRY.get()));
        SMALL_CERAMETRON_BUD = BLOCKS.register("small_cerametron_bud", () -> new AmethystClusterBlock(3.0F, 4.0F, BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD).noLootTable().noOcclusion()));
        MEDIUM_CERAMETRON_BUD = BLOCKS.register("medium_cerametron_bud", () -> new AmethystClusterBlock(4.0F, 3.0F, BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD).noLootTable().noOcclusion()));
        LARGE_CERAMETRON_BUD = BLOCKS.register("large_cerametron_bud", () -> new AmethystClusterBlock(5.0F, 3.0F, BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_AMETHYST_BUD).noLootTable().noOcclusion()));
        CERAMETRON_CLUSTER = registerBlockWithItem("cerametron_cluster", () -> new AmethystClusterBlock(7.0F, 3.0F, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).noOcclusion()));
        BUDDING_CERAMETRON = registerBlockWithItem("budding_cerametron", () -> new JankyAmethystBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BUDDING_AMETHYST).randomTicks(),
                SMALL_CERAMETRON_BUD.get(), MEDIUM_CERAMETRON_BUD.get(), LARGE_CERAMETRON_BUD.get(), CERAMETRON_CLUSTER.get()));
        CERAMETRON_CLUSTER_BLOCK = registerBlockWithItem("cerametron_cluster_block", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK));
        FARM_SIMULATION = registerBlockWithItem("farm_simulation", FarmSimulationBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(BASIC_QUARRY.get()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
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

    @ParametersAreNonnullByDefault
    @NotNull
    private static DeferredBlock<Block> registerBlockWithItem(String name,
                                                              Function<BlockBehaviour.Properties, ? extends Block> blockFactory,
                                                              Supplier<BlockBehaviour.Properties> propertiesSupplier) {

        DeferredBlock<Block> block = BLOCKS.register(name, () -> blockFactory.apply(propertiesSupplier.get()));
        ModItems.ITEMS.registerSimpleBlockItem(name, block, new Item.Properties());
        return block;
    }
}
