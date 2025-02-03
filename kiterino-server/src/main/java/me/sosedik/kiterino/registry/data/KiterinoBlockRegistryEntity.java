package me.sosedik.kiterino.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import me.sosedik.kiterino.util.KiterinoDataInjectorRuntimeException;
import me.sosedik.kiterino.world.block.KiterinoBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.block.BlockType;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

@NullMarked
// Kiterino - Data-driven blocks
public class KiterinoBlockRegistryEntity implements BlockRegistryEntity {

	protected @Nullable KiterinoBlock nmsBlock;

	protected final Conversions conversions;
	public ResourceLocation blockKey;

	public KiterinoBlockRegistryEntity(
			Conversions conversions,
			@Nullable Block internal
	) {
		this.conversions = conversions;
	}

	@Override
	public @Nullable KiterinoBlock nmsBlock() {
		return this.nmsBlock;
	}

	@Override
	public Object constructBlockProperties() {
		return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, this.blockKey));
	}

	public static final class KiterinoBuilder extends KiterinoBlockRegistryEntity implements Builder,
			PaperRegistryBuilder<Block, BlockType> {

		public KiterinoBuilder(Conversions conversions, @Nullable Block internal) {
			super(conversions, internal);
		}

		@Override
		public Block build() {
			Block block = (Block) this.nmsBlock;
			assert block != null;
			if (this.nmsBlock.getBlockDataClasses() != null) {
				CraftBlockData.register(block.getClass(), state -> {
					try {
						return (CraftBlockData) this.nmsBlock.getBlockDataClasses().second().getDeclaredConstructor(BlockState.class).newInstance(state);
					} catch (NoSuchMethodException e) {
						throw new KiterinoDataInjectorRuntimeException("Invalid block data constructor", e);
					} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
						throw new KiterinoDataInjectorRuntimeException("Something went wrong constructing block data", e);
					}
				});
			}
			return block;
		}

		@Override
		public Builder nmsBlock(KiterinoBlock nmsBlock) {
			if (!(nmsBlock instanceof Block)) throw new IllegalArgumentException("NMS block must extend Block");
			this.nmsBlock = nmsBlock;
			return this;
		}

	}

}
