package dev.maniac.xpUtilities.core;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static dev.maniac.xpUtilities.XPUtilities.MOD_ID;
import static dev.maniac.xpUtilities.core.XPFluids.STILL_LIQUID_XP;

public class XPBlocks {
    public static Block LIQUID_XP = Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "liquid_xp"), new FluidBlock(STILL_LIQUID_XP, FabricBlockSettings.copy(Blocks.WATER)){});

    public static void init() {
    }
}
