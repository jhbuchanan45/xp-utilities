package dev.maniac.xpUtilities.block.XPTank;

import dev.maniac.xpUtilities.block.XPExtractor.XpExtractorBlockEntity;
import dev.maniac.xpUtilities.core.XPBlockEntityTypes;
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
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
public class XpTankBlock extends Block implements BlockEntityProvider {
    public XpTankBlock(Settings settings) {
        super(settings.nonOpaque());
    }

    protected ItemStack getStack(BlockEntity entity) {
        XpTankBlockEntity tankEntity = (XpTankBlockEntity) entity;
        ItemStack stack = new ItemStack(asItem());
        if (!tankEntity.isEmpty()) {
            NbtCompound tag = new NbtCompound();
            tag.put("BlockEntityTag", tankEntity.toClientTag(new NbtCompound()));
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
    public XpTankBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new XpTankBlockEntity(XPBlockEntityTypes.XP_TANK_BLOCK_TYPE, pos, state, FluidConstants.BUCKET * 20);
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return false;
    }
}
