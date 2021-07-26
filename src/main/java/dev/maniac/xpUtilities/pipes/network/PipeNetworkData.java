package dev.maniac.xpUtilities.pipes.network;

import net.minecraft.nbt.NbtCompound;

public abstract class PipeNetworkData {
    public abstract PipeNetworkData clone();

    public abstract void fromTag(NbtCompound tag);

    public abstract  NbtCompound toTag(NbtCompound tag);
}
