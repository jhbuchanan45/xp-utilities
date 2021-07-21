package dev.maniac.xpUtilities.core;

import dev.maniac.xpUtilities.block.XPExtractor.XpExtractorBlockEntity;
import dev.maniac.xpUtilities.block.XPShower.XpShowerBlockEntity;
import dev.maniac.xpUtilities.block.XPTank.XpTankBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static dev.maniac.xpUtilities.XPUtilities.MOD_ID;
import static dev.maniac.xpUtilities.core.XPBlocks.*;

public class XPBlockEntityTypes {
    public static final BlockEntityType<XpExtractorBlockEntity> XP_EXTRACTOR_BLOCK_TYPE = registerXPTankStorageBlockEntity("xp_extractor", XP_EXTRACTOR_BLOCK::createBlockEntity, XP_EXTRACTOR_BLOCK);
    public static final BlockEntityType<XpTankBlockEntity> XP_TANK_BLOCK_TYPE = registerXPTankStorageBlockEntity("xp_tank", XP_TANK_BLOCK::createBlockEntity, XP_TANK_BLOCK);
    public static final BlockEntityType<XpShowerBlockEntity> XP_SHOWER_BLOCK_TYPE = registerBlockEntity("xp_shower", XP_SHOWER_BLOCK::createBlockEntity, XP_SHOWER_BLOCK);

    private static <B extends BlockEntity> BlockEntityType<B> registerBlockEntity(String name, FabricBlockEntityTypeBuilder.Factory<B> factory, Block... block) {
        Identifier id = new Identifier(MOD_ID, name + "_block_entity_type");

        BlockEntityType<B> type = FabricBlockEntityTypeBuilder.<B>create(factory, block).build();

        return Registry.register(Registry.BLOCK_ENTITY_TYPE, id, type);
    }

    private static <E extends BlockEntity> BlockEntityType<E> registerXPTankStorageBlockEntity(String name, FabricBlockEntityTypeBuilder.Factory<E> factory, Block... block) {
        Identifier id = new Identifier(MOD_ID, name + "_block_entity_type");

        BlockEntityType<E> type = FabricBlockEntityTypeBuilder.create(factory, block).build();

        FluidStorage.SIDED.registerSelf(type);
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, id, type);
    }

    public static void init() {
    }
}
