package dev.maniac.xpUtilities.item;

import dev.maniac.xpUtilities.core.XPItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import static dev.maniac.xpUtilities.XPUtilities.XP_PER_BUCKET;

public class XPExtractorBucketFilled extends BucketItem {
    public XPExtractorBucketFilled(Fluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (!playerEntity.isSneaking()) {
            return super.use(world, playerEntity, hand);
        } else {
            playerEntity.addExperience(+XP_PER_BUCKET);
            ItemStack newBucket = ItemUsage.exchangeStack(playerEntity.getStackInHand(hand), playerEntity, XPItems.LIQUID_XP_EXTRACTOR_BUCKET.getDefaultStack());
            playerEntity.playSound(SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.0F, 1.0F);
            return TypedActionResult.success(newBucket);
        }
    }
}
