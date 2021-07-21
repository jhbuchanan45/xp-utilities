package dev.maniac.xpUtilities.block.XPExtractor;

import dev.maniac.xpUtilities.block.XPTank.XpTankBlock;
import dev.maniac.xpUtilities.core.XPBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static dev.maniac.xpUtilities.XPUtilities.XP_PER_BUCKET;
import static dev.maniac.xpUtilities.core.XPFluids.STILL_LIQUID_XP;

public class XpExtractorBlock extends XpTankBlock {
    public XpExtractorBlock(Settings settings) {
        super(settings.nonOpaque().allowsSpawning((s, w, p, t) -> false));
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isReceivingRedstonePower(pos)) { // disable with redstone signal
            if (entity instanceof PlayerEntity playerEntity) {
                if (playerEntity.totalExperience >= XP_PER_BUCKET / 9 || playerEntity.experienceLevel >= 3 && !(playerEntity.isSneaking() || world.isReceivingRedstonePower(pos))) { // disable if sneaking
                    try (Transaction transaction = Transaction.openOuter()) {
                        // storage can't be null
                        long inserted = world.getBlockEntity(pos, XPBlockEntityTypes.XP_EXTRACTOR_BLOCK_TYPE).orElseThrow().insert(FluidVariant.of(STILL_LIQUID_XP), FluidConstants.BUCKET / 9, transaction);

                        if (inserted == FluidConstants.BUCKET / 9) {
                            transaction.commit();

                            playerEntity.addExperience(-(XP_PER_BUCKET / 9));
                            playerEntity.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.1F);
                        }
                    }
                }
            }
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public @Nullable XpExtractorBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new XpExtractorBlockEntity(pos, state, FluidConstants.BUCKET * 5);
    }
}
