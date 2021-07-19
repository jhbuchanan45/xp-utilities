package dev.maniac.xpUtilities.block.XPTank.client.render;

import dev.maniac.xpUtilities.block.XPTank.XpTankBlockEntity;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

public class XpTankRenderer<T extends XpTankBlockEntity> implements BlockEntityRenderer<T> {
    public XpTankRenderer(BlockEntityRendererFactory.Context context) {
    }

    private static final float TANK_W = 0.02f;
    public static final int FULL_LIGHT = 0x00F0_00F0;

    @Override
    public void render(T tank, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!tank.getResource().isBlank() && tank.getAmount() > 0) {
            float fill = (float) tank.getAmount() / tank.getCapacity();
            FluidVariant fluid = tank.getResource();

            VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
            Sprite sprite = FluidVariantRendering.getSprite(fluid);
            int color = FluidVariantRendering.getColor(fluid);
            float r = ((color >> 16) & 255) / 256f;
            float g = ((color >> 8) & 255) / 256f;
            float b = (color & 255) / 256f;

            // Make sure fill is within [TANK_W, 1 - TANK_W]
            fill = Math.min(fill, 1 - TANK_W);
            fill = Math.max(fill, TANK_W);
            // Top and bottom positions of the fluid inside the tank
            float topHeight = fill;
            float bottomHeight = TANK_W;
            // Render gas from top to bottom
            if (FluidVariantRendering.fillsFromTop(fluid)) {
                topHeight = 1 - TANK_W;
                bottomHeight = 1 - fill;
            }

            Renderer renderer = RendererAccess.INSTANCE.getRenderer();
            for (Direction direction : Direction.values()) {
                QuadEmitter emitter = renderer.meshBuilder().getEmitter();

                if (direction.getAxis().isVertical()) {
                    emitter.square(direction, TANK_W, TANK_W, 1 - TANK_W, 1 - TANK_W, direction == Direction.UP ? 1 - topHeight : bottomHeight);
                } else {
                    emitter.square(direction, TANK_W, bottomHeight, 1 - TANK_W, topHeight, TANK_W);
                }

                emitter.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV);
                emitter.spriteColor(0, -1, -1, -1, -1);
                vc.quad(matrices.peek(), emitter.toBakedQuad(0, sprite, false), r, g, b, FULL_LIGHT, OverlayTexture.DEFAULT_UV);
            }
        }
    }
}
