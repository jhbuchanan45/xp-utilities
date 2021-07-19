package dev.maniac.xpUtilities.block.XPTank;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleViewIterator;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;

import static dev.maniac.xpUtilities.core.XPFluids.STILL_LIQUID_XP;

@SuppressWarnings("deprecation")
public class XpTankBlockEntity extends BlockEntity implements Storage<FluidVariant>, StorageView<FluidVariant>, BlockEntityClientSerializable {
    final FluidVariant fluid = FluidVariant.of(STILL_LIQUID_XP);
    final long capacity;
    private final XpTankParticipant participant = new XpTankParticipant();
    long amount;
    private long version = 0;

    public XpTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, long capacity) {
        super(type, pos, state);
        this.capacity = capacity;
    }

    @Override
    public long insert(FluidVariant insertedFluid, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(insertedFluid, maxAmount);

        if ((insertedFluid.equals(fluid))) {
            long insertedAmount = Math.min(maxAmount, getCapacity() - amount);

            if (insertedAmount > 0) {
                participant.updateSnapshots(transaction);

                amount += insertedAmount;
            }

            return insertedAmount;
        }

        return 0;
    }

    @Override
    public long extract(FluidVariant insertedFluid, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(insertedFluid, maxAmount);

        if ((insertedFluid.equals(fluid))) {
            long extracted = Math.min(maxAmount, amount);

            if (extracted > 0) {
                participant.updateSnapshots(transaction);
                amount -= extracted;
            }

            return extracted;
        }

        return 0;
    }

    public void onChanged() {
        version++;
        markDirty();
        if (!world.isClient)
            sync();
    }

    @Override
    public boolean isResourceBlank() {
        return false;
    }

    @Override
    public FluidVariant getResource() {
        return fluid;
    }

    public boolean isEmpty() {
        return amount == 0;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        amount = tag.getLong("amt");
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        tag.putLong("amt", amount);
        tag.putLong("cap", capacity);
        return tag;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        toClientTag(tag);
        return super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        fromClientTag(tag);
        super.readNbt(tag);
    }

    @Override
    public boolean supportsInsertion() {
        return true;
    }

    @Override
    public boolean supportsExtraction() {
        return true;
    }

    @Override
    public Iterator<StorageView<FluidVariant>> iterator(TransactionContext transaction) {
        return SingleViewIterator.create(this, transaction);
    }

    /**
     * Special BlockEntity that does NOT update comparators when markDirty is
     * called, making it VERY FAST. This might cause issues with blocks that change
     * BlockState, don't use block states with this class!
     * https://github.com/AztechMC/Modern-Industrialization/blob/0e5a27a31d9481826c94b57294c7cb970128e995/src/main/java/aztech/modern_industrialization/api/FastBlockEntity.java#L36
     */
    @Override
    public void markDirty() {
        if (this.world != null) {
            this.world.markDirty(this.pos);
        }
    }

    private class XpTankParticipant extends SnapshotParticipant<ResourceAmount<FluidVariant>> {
        @Override
        protected ResourceAmount<FluidVariant> createSnapshot() {
            return new ResourceAmount<>(fluid, amount);
        }

        @Override
        protected void readSnapshot(ResourceAmount<FluidVariant> snapshot) {
            amount = snapshot.amount();
        }

        @Override
        protected void onFinalCommit() {
            onChanged();
        }
    }
}
