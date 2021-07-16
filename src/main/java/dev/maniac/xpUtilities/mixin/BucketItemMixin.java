package dev.maniac.xpUtilities.mixin;

import dev.maniac.xpUtilities.core.XPItems;
import dev.maniac.xpUtilities.item.XPExtractorBucket;
import dev.maniac.xpUtilities.item.XPExtractorBucketFilled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class BucketItemMixin {
    @Inject(at = @At("HEAD"), method = "getEmptiedStack", cancellable = true)
    private static void getEmptiedStack(ItemStack stack, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.getItem() instanceof XPExtractorBucketFilled) {
            cir.setReturnValue(!player.getAbilities().creativeMode ? new ItemStack(XPItems.LIQUID_XP_EXTRACTOR_BUCKET) : stack);
        }
    }

    @ModifyVariable(method = "use", ordinal = 1, at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/FluidDrainable;tryDrainFluid(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack setCorrectFilledBucket(ItemStack itemStack2) {
        if ((BucketItem) (Object) this instanceof XPExtractorBucket) {
            return XPItems.LIQUID_XP_EXTRACTOR_BUCKET_FILLED.getDefaultStack();
        } else {
            return itemStack2;
        }
    }
}
