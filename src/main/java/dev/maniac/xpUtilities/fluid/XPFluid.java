package dev.maniac.xpUtilities.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.world.WorldView;

import static dev.maniac.xpUtilities.core.XPBlocks.LIQUID_XP;
import static dev.maniac.xpUtilities.core.XPFluids.FLOWING_LIQUID_XP;
import static dev.maniac.xpUtilities.core.XPFluids.STILL_LIQUID_XP;
import static dev.maniac.xpUtilities.core.XPItems.LIQUID_XP_BUCKET_FILLED;

public abstract class XPFluid extends BaseFluid {
    @Override
    public Fluid getStill() {
        return STILL_LIQUID_XP;
    }

    @Override
    public Fluid getFlowing() {
        return FLOWING_LIQUID_XP;
    }

    @Override
    public Item getBucketItem() {
        return LIQUID_XP_BUCKET_FILLED;
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        // method_15741 converts the LEVEL_1_8 of the fluid state to the LEVEL_15 the fluid block uses
        return LIQUID_XP.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    public static class Flowing extends XPFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }

        @Override
        protected int getLevelDecreasePerBlock(WorldView worldView) {
            return 3;
        }

        @Override
        protected int getFlowSpeed(WorldView worldView) {
            return 1;
        }

        @Override
        public int getTickRate(WorldView worldView) {
            return 2;
        }
    }

    public static class Still extends XPFluid {
        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}
