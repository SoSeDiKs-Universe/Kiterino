package me.sosedik.kiterino.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NullMarked;

@NullMarked
// Kiterino - Melt packed ice in Nether
public class PackedIceBlock extends Block {

    public PackedIceBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (world.kiterinoConfig.meltPackedIceInNether && world.dimensionType().ultraWarm()) {
            IceBlock.meltBlock(state, world, pos);
        }

    }

}
