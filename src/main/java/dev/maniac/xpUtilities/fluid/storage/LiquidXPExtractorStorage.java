package dev.maniac.xpUtilities.fluid.storage;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;

import static dev.maniac.xpUtilities.core.XPFluids.STILL_LIQUID_XP;
import static net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants.BUCKET;

@SuppressWarnings("deprecation")
public class LiquidXPExtractorStorage extends SingleFluidStorage {
    private static int markDirtyCount = 0;

    public LiquidXPExtractorStorage() {
    }

    @Override
    protected boolean canInsert(FluidVariant fluidVariant) {
        return fluidVariant.isOf(STILL_LIQUID_XP);
    }

    @Override
    protected boolean canExtract(FluidVariant fluidVariant) {
        return canInsert(fluidVariant);
    }

    @Override
    protected long getCapacity(FluidVariant fluidVariant) {
        return BUCKET * 5;
    }

    @Override
    protected void markDirty() {
        markDirtyCount++;
    }
}
