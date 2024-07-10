package com.inteavuthkuch.jankystuff.blockentity;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.ModBlocks;
import com.inteavuthkuch.jankystuff.blockentity.crate.MetalCrateBlockEntity;
import com.inteavuthkuch.jankystuff.blockentity.crate.WoodenCrateBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WoodenCrateBlockEntity>> WOODEN_CRATE_BE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MetalCrateBlockEntity>> METAL_CRATE_BE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BasicQuarryBlockEntity>> BASIC_QUARRY_BE;

    static {
        BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, JankyStuff.MODID);
        WOODEN_CRATE_BE = BLOCK_ENTITIES.register("wooden_crate",
                () -> BlockEntityType.Builder.of(WoodenCrateBlockEntity::new, ModBlocks.WOODEN_CRATE.get()).build(null));
        METAL_CRATE_BE = BLOCK_ENTITIES.register("metal_crate",
                () -> BlockEntityType.Builder.of(MetalCrateBlockEntity::new, ModBlocks.METAL_CRATE.get()).build(null));
        BASIC_QUARRY_BE = BLOCK_ENTITIES.register("basic_quarry",
                () -> BlockEntityType.Builder.of(BasicQuarryBlockEntity::new, ModBlocks.BASIC_QUARRY.get()).build(null));
    }
}
