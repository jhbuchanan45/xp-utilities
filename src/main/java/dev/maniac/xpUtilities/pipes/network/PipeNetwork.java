package dev.maniac.xpUtilities.pipes.network;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

import java.util.Map;

public class PipeNetwork extends PersistentState {
    public static final String NAME = "xp_utilities_pipe_networks";

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        for (Map.Entry<PipeNetworkType, PipeNetworkManager> entry : managers.entrySet()) {
            nbt.put(entry.getKey().getIdentifier().toString(), entry.getValue().toTag(new NbtCompound()));
        }
        return nbt;
    }
}
