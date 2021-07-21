package dev.maniac.xpUtilities.block.XPShower;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static dev.maniac.xpUtilities.XPUtilities.XP_PER_BUCKET;
import static dev.maniac.xpUtilities.XPUtilities.XP_PER_DROPLET;
import static dev.maniac.xpUtilities.block.XPShower.XpShowerBlock.DRAINING;
import static dev.maniac.xpUtilities.block.XPShower.XpShowerBlock.FACING;
import static dev.maniac.xpUtilities.core.XPFluids.STILL_LIQUID_XP;

public class XpShowerBlockEntity extends BlockEntity {
    public static int MAX_LIQUID_XP_PER_TICK = (int) (FluidConstants.BUCKET / 4);
    private int ticksToNextDrop = 0;
    private boolean onFromPower = false;

    public XpShowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @SuppressWarnings("deprecation")
    public static void tick(World world, BlockPos pos, BlockState state, XpShowerBlockEntity be) {
        if (world.isReceivingRedstonePower(pos)) {
            // power turns on
            world.setBlockState(pos, state.with(DRAINING, true));
            be.onFromPower = true;
        } else if (be.onFromPower) {
            world.setBlockState(pos, state.with(DRAINING, false));
            be.onFromPower = false;
        }
        if (world instanceof ServerWorld) {
            if (state.get(DRAINING)) {
                // pull from storage in 'DIRECTION'
                Storage<FluidVariant> tank = FluidStorage.SIDED.find(world, pos.offset(state.get(FACING).getOpposite()), state.get(FACING).getOpposite());


                if (tank != null && be.ticksToNextDrop == 0) {
                    try (Transaction transaction = Transaction.openOuter()) {
                        long maxExtract = tank.extract(FluidVariant.of(STILL_LIQUID_XP), MAX_LIQUID_XP_PER_TICK, transaction);

                        transaction.commit();
                        ExperienceOrbEntity.spawn((ServerWorld) world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), (int) (maxExtract * XP_PER_DROPLET));
                        be.ticksToNextDrop = 5;
                    }
                }
                // spawn XP orb of correct size
                // yay
                be.ticksToNextDrop--;
            }
        }
    }
}
