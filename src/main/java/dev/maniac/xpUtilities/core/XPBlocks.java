package dev.maniac.xpUtilities.core;

import dev.maniac.xpUtilities.block.XPExtractor.XpExtractorBlock;
import dev.maniac.xpUtilities.block.XPExtractor.XpExtractorBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static dev.maniac.xpUtilities.XPUtilities.MOD_ID;
import static dev.maniac.xpUtilities.core.XPFluids.STILL_LIQUID_XP;

@SuppressWarnings("deprecation")
public class XPBlocks {
    public static final Block LIQUID_XP = Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "liquid_xp"), new FluidBlock(STILL_LIQUID_XP, FabricBlockSettings.copy(Blocks.WATER)){});
    public static final XpExtractorBlock XP_EXTRACTOR_BLOCK = registerBlock("xp_extractor", new XpExtractorBlock(FabricBlockSettings.of(Material.METAL)));


    private static <T extends Storage<FluidVariant>, B extends  Block> B registerBlock(String name, B block) {
        Identifier id = new Identifier(MOD_ID, name);

        return Registry.register(Registry.BLOCK, id, block);
    }

    public static void init() {
    }
}
