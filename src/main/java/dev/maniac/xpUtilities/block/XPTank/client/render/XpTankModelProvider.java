package dev.maniac.xpUtilities.block.XPTank.client.render;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class XpTankModelProvider implements ModelResourceProvider {
    private static final Map<Identifier, UnbakedModel> modelMap = new HashMap<>();

    public static void register(Identifier id, UnbakedModel model) {
        if (modelMap.put(id, model) != null) {
            throw new RuntimeException("Duplicate registration of model " + id);
        }
    }

    @Override
    public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) {
        return modelMap.get(identifier);
    }
}
