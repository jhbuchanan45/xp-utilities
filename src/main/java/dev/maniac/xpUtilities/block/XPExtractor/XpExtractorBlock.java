package dev.maniac.xpUtilities.block.XPExtractor;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
public class XpExtractorBlock extends Block implements BlockEntityProvider {

    public XpExtractorBlock(Settings settings) {
        super(settings);
    }

    private ItemStack getStack(BlockEntity entity) {
        XpExtractorBlockEntity extractorEntity = (XpExtractorBlockEntity) entity;
        ItemStack stack = new ItemStack(asItem());
        if (!extractorEntity.isEmpty()) {
            NbtCompound tag = new NbtCompound();
            tag.put("BlockEntityTag", extractorEntity.toClientTag(new NbtCompound()));
            stack.setNbt(tag);
        }
        return stack;
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        LootContext lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
        return Arrays.asList(getStack(lootContext.get(LootContextParameters.BLOCK_ENTITY)));
    }

    @Nullable
    @Override
    public XpExtractorBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new XpExtractorBlockEntity(pos, state, FluidConstants.BUCKET * 5);
    }
}
