package dev.maniac.xpUtilities.core;

import dev.maniac.xpUtilities.block.XPExtractor.XpExtractorBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static dev.maniac.xpUtilities.XPUtilities.MOD_ID;
import static dev.maniac.xpUtilities.core.XPBlocks.XP_EXTRACTOR_BLOCK;

public class XPBlockEntityTypes {
    public static final BlockEntityType<XpExtractorBlockEntity> XP_EXTRACTOR_BLOCK_ENTITY_TYPE = registerFluidStorageBlockEntity("xp_extractor", XP_EXTRACTOR_BLOCK::createBlockEntity, XP_EXTRACTOR_BLOCK);

    private static <B extends BlockEntity> BlockEntityType<B> registerBlockEntity(String name, FabricBlockEntityTypeBuilder.Factory<B> factory, Block... block) {
        Identifier id = new Identifier(MOD_ID, name + "_block_entity_type");

        BlockEntityType<B> type = FabricBlockEntityTypeBuilder.<B>create(factory, block).build();

        return Registry.register(Registry.BLOCK_ENTITY_TYPE, id, type);
    }

    private static <B extends BlockEntity> BlockEntityType<B> registerFluidStorageBlockEntity(String name, FabricBlockEntityTypeBuilder.Factory<B> factory, Block... block) {
        Identifier id = new Identifier(MOD_ID, name + "_block_entity_type");

        BlockEntityType<B> type = FabricBlockEntityTypeBuilder.<B>create(factory, block).build();

        FluidStorage.SIDED.registerSelf(type);
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, id, type);
    }
}