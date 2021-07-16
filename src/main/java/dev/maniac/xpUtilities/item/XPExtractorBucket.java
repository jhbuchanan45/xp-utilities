package dev.maniac.xpUtilities.item;

import dev.maniac.xpUtilities.core.XPItems;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import static dev.maniac.xpUtilities.XPUtilities.XP_PER_BUCKET;
import static dev.maniac.xpUtilities.core.XPFluids.STILL_LIQUID_XP;

@SuppressWarnings("deprecation")
public class XPExtractorBucket extends BucketItem {
    public XPExtractorBucket(Fluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (!playerEntity.isSneaking()) {
            // if liquid is not liquid xp, stop
            BlockHitResult blockHitResult = raycast(world, playerEntity, RaycastContext.FluidHandling.SOURCE_ONLY);
            if (blockHitResult.getType() == HitResult.Type.BLOCK)  {
                BlockState blockState = world.getBlockState(blockHitResult.getBlockPos());
                if (!blockState.getFluidState().getFluid().equals(STILL_LIQUID_XP)) {
                  return TypedActionResult.fail(playerEntity.getStackInHand(hand));
                }
            }

            return super.use(world, playerEntity, hand);
        } else {
            if (playerEntity.totalExperience >= XP_PER_BUCKET || playerEntity.experienceLevel >= 11) {
                // if player has bucket of xp
                playerEntity.addExperience(-XP_PER_BUCKET);
                ItemStack newBucket = ItemUsage.exchangeStack(playerEntity.getStackInHand(hand), playerEntity, XPItems.LIQUID_XP_EXTRACTOR_BUCKET_FILLED.getDefaultStack());
                playerEntity.playSound(SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.0F, 1.0F);
                return TypedActionResult.success(newBucket);
            }
            return TypedActionResult.fail(playerEntity.getStackInHand(hand));
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockState state = context.getWorld().getBlockState(context.getBlockPos());
        Storage<FluidVariant> storage = FluidStorage.SIDED.find(context.getWorld(), context.getBlockPos(), context.getSide());

        if (storage == null) {
            return ActionResult.PASS;
        }

        try (Transaction transaction = Transaction.openOuter()) {
            // storage can't be null
            long extracted = storage.extract(FluidVariant.of(STILL_LIQUID_XP), FluidConstants.BUCKET, transaction);

            if (extracted == FluidConstants.BUCKET) {
                transaction.commit();
                PlayerEntity player = context.getPlayer();
                player.getStackInHand(context.getHand()).decrement(1);
                player.getInventory().insertStack(XPItems.LIQUID_XP_EXTRACTOR_BUCKET_FILLED.getDefaultStack());
                context.getPlayer().playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 0.3F);
                return ActionResult.success(context.getWorld().isClient());
            }
        }

        return ActionResult.FAIL;
    }
}
