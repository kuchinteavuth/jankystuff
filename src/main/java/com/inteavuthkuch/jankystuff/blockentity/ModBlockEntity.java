package com.inteavuthkuch.jankystuff.blockentity;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.blockentity.blockaccelerator.AdvanceBlockAcceleratorBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.blockaccelerator.BasicBlockAcceleratorBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.blockaccelerator.EliteBlockAcceleratorBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.blockaccelerator.UltimateBlockAcceleratorBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.crate.MetalCrateBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.crate.WoodenCrateBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.fluidtank.AdvancedFluidTankBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.fluidtank.BasicFluidTankBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.fluidtank.EliteFluidTankBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.fluidtank.UltimateFluidTankBlockEntity;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Type;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class ModBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WoodenCrateBlockEntity>> WOODEN_CRATE_BE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MetalCrateBlockEntity>> METAL_CRATE_BE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BasicQuarryBlockEntity>> BASIC_QUARRY_BE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockBreakerBlockEntity>> BLOCK_BREAKER_BE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BasicBlockAcceleratorBlockEntity>> BASIC_BLOCK_ACCELERATOR_BE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvanceBlockAcceleratorBlockEntity>> ADVANCED_BLOCK_ACCELERATOR_BE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EliteBlockAcceleratorBlockEntity>> ELITE_BLOCK_ACCELERATOR_BE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<UltimateBlockAcceleratorBlockEntity>> ULTIMATE_BLOCK_ACCELERATOR_BE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BasicFluidTankBlockEntity>> BASIC_FLUID_TANK_BE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedFluidTankBlockEntity>> ADVANCED_FLUID_TANK_BE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EliteFluidTankBlockEntity>> ELITE_FLUID_TANK_BE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<UltimateFluidTankBlockEntity>> ULTIMATE_FLUID_TANK_BE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WaterSourceBlockEntity>> WATER_SOURCE_BE;

    static {
        BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, JankyStuff.MOD_ID);

        WOODEN_CRATE_BE = createBlockEntity("wooden_crate", WoodenCrateBlockEntity::new, ModBlocks.WOODEN_CRATE);
        METAL_CRATE_BE = createBlockEntity("metal_crate", MetalCrateBlockEntity::new, ModBlocks.METAL_CRATE);
        BASIC_QUARRY_BE = createBlockEntity("basic_quarry", BasicQuarryBlockEntity::new, ModBlocks.BASIC_QUARRY);
        BLOCK_BREAKER_BE = createBlockEntity("block_breaker", BlockBreakerBlockEntity::new, ModBlocks.BLOCK_BREAKER);
        BASIC_BLOCK_ACCELERATOR_BE = createBlockEntity("basic_block_accelerator", BasicBlockAcceleratorBlockEntity::new, ModBlocks.BASIC_BLOCK_ACCELERATOR);
        ADVANCED_BLOCK_ACCELERATOR_BE = createBlockEntity("advanced_block_accelerator", AdvanceBlockAcceleratorBlockEntity::new, ModBlocks.ADVANCED_BLOCK_ACCELERATOR);
        ELITE_BLOCK_ACCELERATOR_BE = createBlockEntity("elite_block_accelerator", EliteBlockAcceleratorBlockEntity::new, ModBlocks.ELITE_BLOCK_ACCELERATOR);
        ULTIMATE_BLOCK_ACCELERATOR_BE = createBlockEntity("ultimate_block_accelerator", UltimateBlockAcceleratorBlockEntity::new, ModBlocks.ULTIMATE_BLOCK_ACCELERATOR);
        BASIC_FLUID_TANK_BE = createBlockEntity("basic_fluid_tank", BasicFluidTankBlockEntity::new, ModBlocks.BASIC_FLUID_TANK);
        ADVANCED_FLUID_TANK_BE = createBlockEntity("advanced_fluid_tank", AdvancedFluidTankBlockEntity::new, ModBlocks.ADVANCED_FLUID_TANK);
        ELITE_FLUID_TANK_BE = createBlockEntity("elite_fluid_tank", EliteFluidTankBlockEntity::new, ModBlocks.ELITE_FLUID_TANK);
        ULTIMATE_FLUID_TANK_BE = createBlockEntity("ultimate_fluid_tank", UltimateFluidTankBlockEntity::new, ModBlocks.ULTIMATE_FLUID_TANK);
        WATER_SOURCE_BE = createBlockEntity("water_source_be", WaterSourceBlockEntity::new, ModBlocks.WATER_SOURCE);
    }

    @NotNull
    private static <T extends BlockEntity>DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> createBlockEntity(String name,
                                                                                                                   BlockEntityType.BlockEntitySupplier<T> supplier,
                                                                                                                   DeferredBlock<Block> block) {
        Type<?> type = DSL.emptyPartType();
        return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, block.get()).build(type));
    }
}
