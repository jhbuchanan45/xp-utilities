package dev.maniac.xpUtilities.block.XPShower;

import dev.maniac.xpUtilities.block.XPExtractor.XpExtractorBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.block.*;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static dev.maniac.xpUtilities.core.XPBlockEntityTypes.XP_SHOWER_BLOCK_TYPE;

public class XpShowerBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    public static final BooleanProperty DRAINING = BooleanProperty.of("draining");
    public static final DirectionProperty FACING;
    protected static final VoxelShape EAST_SHAPE;
    protected static final VoxelShape WEST_SHAPE;
    protected static final VoxelShape SOUTH_SHAPE;
    protected static final VoxelShape NORTH_SHAPE;

    public XpShowerBlock(Settings settings) {
        super(settings.nonOpaque());
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(DRAINING, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return world.setBlockState(pos, state.with(DRAINING, !state.get(DRAINING))) ? ActionResult.SUCCESS : ActionResult.FAIL;
    }

    // from BlockWithEntity
    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

    // actual ticker getter?? (also server only?)
    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends XpShowerBlockEntity> expectedType) {
        return world.isClient ? null : checkType(givenType, expectedType, XpShowerBlockEntity::tick);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, XP_SHOWER_BLOCK_TYPE);
    }

    @Nullable
    @Override
    public XpShowerBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new XpShowerBlockEntity(XP_SHOWER_BLOCK_TYPE, pos, state);
    }

    private ItemStack getStack(BlockEntity entity) {
        ItemStack stack = new ItemStack(asItem());
        return stack;
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        LootContext lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
        return Arrays.asList(getStack(lootContext.get(LootContextParameters.BLOCK_ENTITY)));
    }

    private boolean canPlaceOn(BlockView world, BlockPos pos, Direction side) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isSideSolidFullSquare(world, pos, side);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = state.get(FACING);
        return this.canPlaceOn(world, pos.offset(direction.getOpposite()), direction);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState2;
        if (!ctx.canReplaceExisting()) {
            blockState2 = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(ctx.getSide().getOpposite()));
            if (blockState2.isOf(this) && blockState2.get(FACING) == ctx.getSide()) {
                return null;
            }
        }

        blockState2 = this.getDefaultState();
        WorldView worldView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        Direction[] var6 = ctx.getPlacementDirections();
        int var7 = var6.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Direction direction = var6[var8];
            if (direction.getAxis().isHorizontal()) {
                blockState2 = (BlockState)blockState2.with(FACING, direction.getOpposite());
                if (blockState2.canPlaceAt(worldView, blockPos)) {
                    return (BlockState)blockState2;
                }
            }
        }

        return null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, DRAINING);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch((Direction)state.get(FACING)) {
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
            case EAST:
            default:
                return EAST_SHAPE;
        }
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
        EAST_SHAPE = Block.createCuboidShape(0.0D, 11.0D, 6.0D, 9.0D, 15.0D, 10.0D);
        WEST_SHAPE = Block.createCuboidShape(7.0D, 11.0D, 6.0D, 16.0D, 15.0D, 10.0D);
        SOUTH_SHAPE = Block.createCuboidShape(6.0D, 11.0D, 0.0D, 10.0D, 15.0D, 9.0D);
        NORTH_SHAPE = Block.createCuboidShape(6.0D, 11.0D, 7.0D, 10.0D, 15.0D, 16.0D);
    }
}
