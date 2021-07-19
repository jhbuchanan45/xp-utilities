package dev.maniac.xpUtilities.block.XPExtractor;

import dev.maniac.xpUtilities.block.XPTank.XpTankBlockEntity;
import dev.maniac.xpUtilities.core.XPBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("deprecation")
public class XpExtractorBlockEntity extends XpTankBlockEntity {

    public XpExtractorBlockEntity(BlockPos pos, BlockState state, long capacity) {
        super(XPBlockEntityTypes.XP_EXTRACTOR_BLOCK_ENTITY_TYPE, pos, state, capacity);
    }

}
