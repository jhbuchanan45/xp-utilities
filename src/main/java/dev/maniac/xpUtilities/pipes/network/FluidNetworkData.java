package dev.maniac.xpUtilities.pipes.network;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluidNetworkData extends PipeNetworkData {
    FluidVariant fluid;

    public FluidNetworkData(FluidVariant fluid) {
        this.fluid = fluid;
    }

    @Override
    public FluidNetworkData clone() {
        return new FluidNetworkData(fluid);
    }

    @Override
    public void fromTag(NbtCompound tag) {
        if (tag == null || !tag.contains("fluid")) {
            fluid = FluidVariant.blank();
        }

        if (tag.get("fluid") instanceof NbtString) {
            fluid = FluidVariant.of(Registry.FLUID.get(new Identifier(tag.getString("fluid"))));
        } else {
            NbtCompound compound = tag.getCompound("fluid");
            if (compound.contains("fk")) {
                fluid = FluidVariant.fromNbt(compound.getCompound("fk"));
            } else {
                Fluid fluidNormal;
                if (tag.contains("ObjName") && tag.getString("Registry").equals("f")) {
                    fluidNormal = Registry.FLUID.get(new Identifier(tag.getString("ObjName")));
                } else {
                    fluidNormal = Fluids.EMPTY;
                }
                fluid = FluidVariant.of(fluidNormal);
            }
        }
    }

    @Override
    public NbtCompound toTag(NbtCompound tag) {
        NbtCompound savedTag = new NbtCompound();
        savedTag.put("fk", fluid.toNbt());
        tag.put("fluid", savedTag);
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FluidNetworkData fluidData) {
            return fluidData.fluid == fluid;
        } else {
            return false;
        }
    }
}
