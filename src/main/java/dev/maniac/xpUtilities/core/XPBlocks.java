package dev.maniac.xpUtilities.core;

import dev.maniac.xpUtilities.block.XPExtractor.XpExtractorBlock;
import dev.maniac.xpUtilities.block.XPShower.XpShowerBlock;
import dev.maniac.xpUtilities.block.XPTank.XpTankBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static dev.maniac.xpUtilities.XPUtilities.MOD_ID;
import static dev.maniac.xpUtilities.core.XPFluids.STILL_LIQUID_XP;
import static net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags.PICKAXES;

@SuppressWarnings("deprecation")
public class XPBlocks {
    public static final Block LIQUID_XP = Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "liquid_xp"), new FluidBlock(STILL_LIQUID_XP, FabricBlockSettings.copy(Blocks.WATER).emissiveLighting(((state, world, pos) -> true))){});
    public static final XpExtractorBlock XP_EXTRACTOR_BLOCK = registerBlock("xp_extractor", new XpExtractorBlock(FabricBlockSettings.of(Material.METAL).hardness(3.0F).breakByTool(PICKAXES)));
    public static final XpShowerBlock XP_SHOWER_BLOCK = registerBlock("xp_shower", new XpShowerBlock(FabricBlockSettings.of(Material.METAL).hardness(3.0F).breakByTool(PICKAXES)));
    public static final XpTankBlock XP_TANK_BLOCK = registerBlock("xp_tank", new XpTankBlock(FabricBlockSettings.of(Material.METAL).hardness(4.0F).breakByTool(PICKAXES)));

    private static <B extends  Block> B registerBlock(String name, B block) {
        Identifier id = new Identifier(MOD_ID, name);

        return Registry.register(Registry.BLOCK, id, block);
    }

    public static void init() {
    }
}
