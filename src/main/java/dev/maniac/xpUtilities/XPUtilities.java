package dev.maniac.xpUtilities;

import dev.maniac.xpUtilities.core.XPBlockEntityTypes;
import dev.maniac.xpUtilities.core.XPBlocks;
import dev.maniac.xpUtilities.core.XPFluids;
import dev.maniac.xpUtilities.core.XPItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

public class XPUtilities implements ModInitializer {
	public static final String MOD_ID = "xp_utilities";
	public static final int XP_PER_BUCKET = 324;
	public static final double XP_PER_DROPLET = (double) XP_PER_BUCKET / (double) FluidConstants.BUCKET;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		XPBlocks.init();
		XPFluids.init();
		XPItems.init();
		XPBlockEntityTypes.init();

		System.out.println("Registering XP Utilities!");
	}
}
