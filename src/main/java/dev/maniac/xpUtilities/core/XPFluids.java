package dev.maniac.xpUtilities.core;

import dev.maniac.xpUtilities.fluid.LiquidXPFluid;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static dev.maniac.xpUtilities.XPUtilities.MOD_ID;

public class XPFluids {
    public static final FlowableFluid STILL_LIQUID_XP = Registry.register(Registry.FLUID, new Identifier(MOD_ID, "liquid_xp"), new LiquidXPFluid.Still());
    public static final FlowableFluid FLOWING_LIQUID_XP = Registry.register(Registry.FLUID, new Identifier(MOD_ID, "liquid_xp_flowing"), new LiquidXPFluid.Flowing());

    public static void init() {
    }
}
