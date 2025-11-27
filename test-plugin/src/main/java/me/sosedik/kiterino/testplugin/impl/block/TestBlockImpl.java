package me.sosedik.kiterino.testplugin.impl.block;

import me.sosedik.kiterino.world.block.KiterinoBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

@NullMarked
public class TestBlockImpl extends Block implements KiterinoBlock {

    private @Nullable BlockState bukkitState;

    public TestBlockImpl(Properties properties) {
//        super(properties); // If not using the passed properties, the id must be retained
        super(
            Properties.ofFullCopy(Blocks.ACACIA_PLANKS)
                .setId(requireNonNull(properties.getId()))
        );
    }

    @Override
    public @Nullable BlockState serializeBlockToClient(Object currentState) {
        // Sadly, Bukkit's block state is not available on constructor call,
        // so construct & cache it upon the first request
        if (this.bukkitState == null) this.bukkitState = Material.ACACIA_PLANKS.createBlockData().createBlockState();
        return this.bukkitState;
    }

}
