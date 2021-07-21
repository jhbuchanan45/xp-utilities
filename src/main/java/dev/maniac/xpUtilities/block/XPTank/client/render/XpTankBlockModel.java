package dev.maniac.xpUtilities.block.XPTank.client.render;

import com.mojang.datafixers.util.Pair;
import dev.maniac.xpUtilities.block.XPTank.XpTankItem;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.maniac.xpUtilities.core.XPFluids.STILL_LIQUID_XP;

@SuppressWarnings("deprecation")
public class XpTankBlockModel implements UnbakedModel, BakedModel, FabricBakedModel {
    private static final Identifier BASE_BLOCK_MODEL = new Identifier("minecraft:block/block");
    private final SpriteIdentifier[] SPRITE_IDS;
    private Sprite[] SPRITES;
    ModelTransformation transformation;
    private Mesh xpTankMesh;
    private RenderMaterial translucentMaterial;

    public XpTankBlockModel(Identifier xpTankWalls, Identifier xpTankTop) {
        SPRITES = new Sprite[2];
        SPRITE_IDS = new SpriteIdentifier[2];
        SPRITE_IDS[0] = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, xpTankWalls);
        SPRITE_IDS[1] = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, xpTankTop);
    }

    public XpTankBlockModel(Identifier xpTankWalls) {
        SPRITES = new Sprite[1];
        SPRITE_IDS = new SpriteIdentifier[1];
        SPRITE_IDS[0] = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, xpTankWalls);
    }

    public XpTankBlockModel(ModelTransformation transformation, SpriteIdentifier tankSpriteId, Sprite tankSprite, RenderMaterial translucentMaterial,
                            Mesh tankMesh) {
        SPRITES = new Sprite[1];
        SPRITE_IDS = new SpriteIdentifier[1];
        this.transformation = transformation;
        this.SPRITE_IDS[0] = tankSpriteId;
        this.SPRITES[0] = tankSprite;
        this.translucentMaterial = translucentMaterial;
        this.xpTankMesh = tankMesh;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Arrays.asList(BASE_BLOCK_MODEL); // This model does not depend on other models.
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Arrays.asList(SPRITE_IDS);
    }

    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        // get sprites
        for(int i = 0; i < SPRITE_IDS.length; ++i) {
            SPRITES[i] = textureGetter.apply(SPRITE_IDS[i]);
        }
        transformation = ((JsonUnbakedModel) loader.getOrLoadModel(BASE_BLOCK_MODEL)).getTransformations();

        // build mesh with renderer api
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        RenderMaterial cutoutMaterial = renderer.materialFinder().blendMode(0, BlendMode.CUTOUT_MIPPED).find();
        translucentMaterial = renderer.materialFinder().blendMode(0, BlendMode.SOLID).emissive(0, true).find();

        MeshBuilder builder = RendererAccess.INSTANCE.getRenderer().meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        for (Direction direction : Direction.values()) {
            int spriteIndex = (SPRITE_IDS.length == 2 && direction == Direction.UP) ? 1 : 0;

            emitter.material(cutoutMaterial);
            // add new face
            emitter.square(direction, 0, 0, 1, 1, 0.0f);
            emitter.cullFace(direction);
            // Set sprite for face *after* .square()
            // no uv, so use whole texture with BAKE_LOCK_UV
            emitter.spriteBake(0, SPRITES[spriteIndex], MutableQuadView.BAKE_LOCK_UV);
            // enable texture usage
            emitter.spriteColor(0, -1, -1, -1, -1);
            // add quad to mesh
            emitter.emit();
        }
        xpTankMesh = builder.build();
        return this;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
        return null;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return true;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getSprite() {
        return SPRITES[0];
    }

    @Override
    public ModelTransformation getTransformation() {
        return transformation;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false; // False to trigger FabricBakedModel rendering
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {
        // We just render the mesh
        renderContext.meshConsumer().accept(xpTankMesh);
    }

    @Override
    public void emitItemQuads(ItemStack itemStack, Supplier<Random> supplier, RenderContext renderContext) {
        renderContext.meshConsumer().accept(xpTankMesh);

        if (itemStack.getItem() instanceof XpTankItem) {
            NbtCompound nbt = itemStack.getNbt();
            if (nbt != null) {
                NbtCompound entityNbt = nbt.getCompound("BlockEntityTag");
                if (entityNbt != null) {
                    long amount = entityNbt.getLong("amt");
                    long capacity = entityNbt.getLong("cap");
                    if (!(amount == 0)) {
                        float fillFraction = (float) amount / capacity;
                        drawFluid(renderContext.getEmitter(), fillFraction, FluidVariant.of(STILL_LIQUID_XP));
                    }
                }
            }
        }
    }

    private void drawFluid(QuadEmitter emitter, float fillFraction, FluidVariant fluid) {
        Sprite stillSprite = FluidVariantRendering.getSprite(fluid);
        int color = FluidVariantRendering.getColor(fluid) | 0xff << 24; // TODO - workaround for bug in getColor(fluid), remove when fixed
        for (Direction direction : Direction.values()) {
            float topSpace, depth, bottomSpace;
            if (FluidVariantRendering.fillsFromTop(fluid)) {
                bottomSpace = direction.getAxis().isHorizontal() ? 1 - fillFraction + 0.01f : 0;
                depth = direction == Direction.DOWN ? fillFraction : 0;
                topSpace = 0;
            } else {
                bottomSpace = 0;
                topSpace = direction.getAxis().isHorizontal() ? 1 - fillFraction + 0.01f : 0;
                depth = direction == Direction.UP ? 1 - fillFraction : 0;
            }
            emitter.material(translucentMaterial);
            emitter.square(direction, 0, bottomSpace, 1, 1 - topSpace, depth + 0.01f);
            emitter.spriteBake(0, stillSprite, MutableQuadView.BAKE_LOCK_UV);
            emitter.spriteColor(0, color, color, color, color);
            emitter.emit();
        }
    }
}
