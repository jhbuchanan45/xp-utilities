package dev.maniac.xpUtilities.pipes.network;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class PipeNetworkType {
    private static Map<Identifier, PipeNetworkType> networkTypes = new HashMap<>();

    private final Identifier identifier;
    private final BiFunction<Integer, PipeNetworkData, PipeNetwork> newNetwork;
    private final Supplier<PipeNetworkNode> newNode;

    private PipeNetworkType(Identifier identifier, BiFunction<Integer, PipeNetworkData, PipeNetwork>newNetwork, Supplier<PipeNetworkNode> newNode) {
        this.identifier = identifier;
        this.newNetwork = newNetwork;
        this.newNode = newNode;
    }

    public static PipeNetworkType register(Identifier identifier, BiFunction<Integer, PipeNetworkData, PipeNetwork>newNetwork, Supplier<PipeNetworkNode> newNode) {
        PipeNetworkType type = new PipeNetworkType(identifier, newNetwork, newNode);
        PipeNetworkType prevType = networkTypes.put(identifier, type);
        if (prevType != null) {
            throw new IllegalArgumentException("Cannot register another PipeNetworkType with the same identifier");
        }
        return type;
    }

    public static Map<Identifier, PipeNetworkType> getNetworkTypes() {
        return networkTypes;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public BiFunction<Integer, PipeNetworkData, PipeNetwork> getNewNetwork() {
        return newNetwork;
    }

    public Supplier<PipeNetworkNode> getNewNode() {
        return newNode;
    }
}
