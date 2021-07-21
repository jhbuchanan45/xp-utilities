package dev.maniac.xpUtilities.core;

import dev.maniac.xpUtilities.block.XPTank.XpTankItem;
import dev.maniac.xpUtilities.item.XPExtractorBucket;
import dev.maniac.xpUtilities.item.XPExtractorBucketFilled;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static dev.maniac.xpUtilities.XPUtilities.MOD_ID;
import static dev.maniac.xpUtilities.core.XPBlocks.*;
import static dev.maniac.xpUtilities.core.XPFluids.STILL_LIQUID_XP;

public class XPItems {
    public static final Item LIQUID_XP_EXTRACTOR_BUCKET = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "liquid_xp_extractor_bucket"), new XPExtractorBucket(Fluids.EMPTY, new Item.Settings().group(ItemGroup.MISC).maxCount(16)));
    public static final Item LIQUID_XP_EXTRACTOR_BUCKET_FILLED = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "liquid_xp_extractor_bucket_filled"), new XPExtractorBucketFilled(STILL_LIQUID_XP, new Item.Settings().group(ItemGroup.MISC).recipeRemainder(LIQUID_XP_EXTRACTOR_BUCKET).maxCount(1)));
    public static final Item LIQUID_XP_BUCKET_FILLED = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "liquid_xp_bucket"), new BucketItem(STILL_LIQUID_XP, new Item.Settings().group(ItemGroup.MISC).recipeRemainder(Items.BUCKET).maxCount(1)));
    public static final Item XP_EXTRACTOR_ITEM = registerItem("xp_extractor", new XpTankItem(XP_EXTRACTOR_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
    public static final Item XP_SHOWER_ITEM = registerItem("xp_shower", new BlockItem(XP_SHOWER_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
    public static final Item XP_TANK_ITEM = registerItem("xp_tank", new XpTankItem(XP_TANK_BLOCK, new Item.Settings().group(ItemGroup.MISC)));

    private static <I extends Item> I registerItem(String name, I item) {
        Identifier id = new Identifier(MOD_ID, name);

        return Registry.register(Registry.ITEM, id, item);
    }

    public static void init() {
    }
}
