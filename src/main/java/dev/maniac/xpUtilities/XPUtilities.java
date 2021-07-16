package dev.maniac.xpUtilities;

import dev.maniac.xpUtilities.core.XPBlocks;
import dev.maniac.xpUtilities.core.XPFluids;
import dev.maniac.xpUtilities.core.XPItems;
import net.fabricmc.api.ModInitializer;

public class XPUtilities implements ModInitializer {
	public static final String MOD_ID = "xp_utilities";
	public static final int XP_PER_BUCKET = 162;
	public static final long XP_PER_DROPLET = XP_PER_BUCKET / 81000L;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		XPBlocks.init();
		XPFluids.init();
		XPItems.init();

		System.out.println("Registering XP Utilities!");
	}
}
