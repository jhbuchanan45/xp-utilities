package dev.maniac.xpUtilities;

import dev.maniac.xpUtilities.block.XPTank.client.render.XpTankBlockModel;
import dev.maniac.xpUtilities.block.XPTank.client.render.XpTankModelProvider;
import dev.maniac.xpUtilities.block.XPTank.client.render.XpTankRenderer;
import dev.maniac.xpUtilities.core.XPBlockEntityTypes;
import dev.maniac.xpUtilities.core.XPFluids;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;

import java.util.function.Function;

import static dev.maniac.xpUtilities.XPUtilities.MOD_ID;

public class XPUtilitiesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        setupFluidRendering(XPFluids.STILL_LIQUID_XP, XPFluids.FLOWING_LIQUID_XP, new Identifier("minecraft", "lava"), 0x32f932);
        registerModelProviders();
        XpTankModelProvider.register(new Identifier(MOD_ID, "block/" + "xp_extractor"), new XpTankBlockModel(new Identifier("minecraft", "block/glass"), new Identifier(MOD_ID, "block/iron_grate")));
        XpTankModelProvider.register(new Identifier(MOD_ID, "item/" + "xp_extractor"), new XpTankBlockModel(new Identifier("minecraft", "block/glass"), new Identifier(MOD_ID, "block/iron_grate")));
        XpTankModelProvider.register(new Identifier(MOD_ID, "item/" + "xp_tank"), new XpTankBlockModel(new Identifier("minecraft", "block/glass")));
        XpTankModelProvider.register(new Identifier(MOD_ID, "block/" + "xp_tank"), new XpTankBlockModel(new Identifier("minecraft", "block/glass")));
        BlockEntityRendererRegistry.INSTANCE.register(XPBlockEntityTypes.XP_EXTRACTOR_BLOCK_TYPE, XpTankRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(XPBlockEntityTypes.XP_TANK_BLOCK_TYPE, XpTankRenderer::new);
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), XPFluids.STILL_LIQUID_XP, XPFluids.FLOWING_LIQUID_XP);
    }

    public static void registerModelProviders() {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new XpTankModelProvider());
    }

    public static void setupFluidRendering(final Fluid still, final Fluid flowing, final Identifier textureFluidId, final int color) {
        final Identifier stillSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_still");
        final Identifier flowingSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_flow");

        // If they're not already present, add the sprites to the block atlas
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
            registry.register(stillSpriteId);
            registry.register(flowingSpriteId);
        });

        final Identifier fluidId = Registry.FLUID.getId(still);
        final Identifier listenerId = new Identifier(fluidId.getNamespace(), fluidId.getPath() + "_reload_listener");

        final Sprite[] fluidSprites = { null, null };

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return listenerId;
            }

            /**
             * Get the sprites from the block atlas when resources are reloaded
             */
            @Override
            public void reload(ResourceManager resourceManager) {
                final Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
                fluidSprites[0] = atlas.apply(stillSpriteId);
                fluidSprites[1] = atlas.apply(flowingSpriteId);
            }
        });

        // The FluidRenderer gets the sprites and color from a FluidRenderHandler during rendering
        final FluidRenderHandler renderHandler = new FluidRenderHandler()
        {
            @Override
            public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state) {
                return fluidSprites;
            }

            @Override
            public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state) {
                return color;
            }
        };

        FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler);
        FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler);
    }
}
