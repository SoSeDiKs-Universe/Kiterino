package me.sosedik.kiterino.testplugin.impl;

import me.sosedik.kiterino.world.block.KiterinoBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

@NullMarked
public class BadassBlock extends Block implements KiterinoBlock {

	private @Nullable BlockState bukkitState;

	public BadassBlock(Properties properties) {
		super(Properties.ofFullCopy(Blocks.ACACIA_PLANKS).setId(requireNonNull(properties.getId())));
	}

	@Override
	public @Nullable BlockState serializeBlockToClient(Object currentState) {
		if (this.bukkitState == null) {
			this.bukkitState = Material.ACACIA_PLANKS.createBlockData().createBlockState();
		}
		return this.bukkitState;
	}

}
