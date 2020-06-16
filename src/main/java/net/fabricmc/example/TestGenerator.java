package net.fabricmc.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public class TestGenerator extends ChunkGenerator {
	protected final boolean customBool;
	protected final int sizeX;
	protected final int sizeZ;
	protected final int curveSize;
	protected final int oceanHeight;
	public static final Codec<TestGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance
			.group(BiomeSource.field_24713.fieldOf("biome_source").forGetter((generator) -> generator.biomeSource),
					Codec.BOOL.fieldOf("custom_bool").forGetter((generator) -> generator.customBool),
					Codec.INT.fieldOf("size_x").forGetter((generator) -> generator.sizeX),
					Codec.INT.fieldOf("size_z").forGetter((generator) -> generator.sizeZ),
					Codec.INT.fieldOf("curve_size").forGetter((generator) -> generator.curveSize))
			.apply(instance, instance.stable(TestGenerator::new)));

//	public static final TestGenerator INSTANCE = new TestGenerator();
	public TestGenerator(BiomeSource biomeSource, boolean customBool, int sizeX, int sizeZ, int curveSize) {
		super(new FixedBiomeSource(Biomes.THE_VOID), new StructuresConfig(false));
		this.customBool = customBool;
		this.sizeX = sizeX;
		this.sizeZ = sizeZ;
		this.curveSize = curveSize;
		this.oceanHeight = 64;
		System.out.println(sizeX + " " + sizeZ);
	}

	@Override
	public void buildSurface(ChunkRegion region, Chunk chunk) {
		ChunkRandom chunkRandom = new ChunkRandom();
		//other stuff...
		this.genBedrock(chunk, chunkRandom);
	}

	private void genBedrock(Chunk chunk, ChunkRandom chunkRandom) {
		//lets work with some blocks
		BlockPos.Mutable current = new BlockPos.Mutable();
		//current chunk position.
		int xPos = chunk.getPos().getStartX();
		int zPos = chunk.getPos().getStartZ();
		//loop through all x-z blocks in this chunk.
		for (final BlockPos pos : BlockPos.iterate(xPos, 0, zPos, xPos + 15, 0, zPos + 15)) {
			//we are currently at height 0.
			double height = 0;
			if (Math.abs(pos.getX()) <= (this.sizeX / 2) && Math.abs(pos.getZ()) <= (this.sizeZ / 2)) {
				height = 1;//if we have no height yet, lets just assume we are at 1, because its there now.
				chunk.setBlockState(current.set(pos.getX(), 0, pos.getZ()), Blocks.BEDROCK.getDefaultState(), false);
			}
			if (Math.abs(pos.getX()) <= this.sizeX / 2 + curveSize / 4
					&& Math.abs(pos.getZ()) <= this.sizeZ / 2 + curveSize / 4) {
				int xDist = Math.abs(pos.getX()) - (sizeX / 2 - curveSize / 2);
				int zDist = Math.abs(pos.getZ()) - (sizeZ / 2 - curveSize / 2);
				if (xDist >= 0)
					height += xDist * xDist;
				if (zDist >= 0)
					height += zDist * zDist;
				height = (int) Math.sqrt(height);
				if (height > this.curveSize / 2) {
					height = (this.curveSize * 3 / 4 - height) * (oceanHeight * 12 / (5 * curveSize));
					for (int i = 0; i <= height; i++) {
						chunk.setBlockState(current.set(pos.getX(), i, pos.getZ()), Blocks.BEDROCK.getDefaultState(),
								false);
					}
				} else {
					height *= (oceanHeight * 6 / (5 * curveSize));
					for (int i = 0; i <= height; i++) {
						chunk.setBlockState(current.set(pos.getX(), i, pos.getZ()), Blocks.BEDROCK.getDefaultState(),
								false);
					}
				}
			}
			for (int spot = 3; spot > 0 && height > 0; spot--) {
				if (spot <= chunkRandom.nextInt(5)) {
					chunk.setBlockState(current.set(pos.getX(), spot + height, pos.getZ()),
							Blocks.BEDROCK.getDefaultState(), false);
				}
			}
		}
	}

	@Override
	public BlockView getColumnSample(int x, int z) {
		return new VerticalBlockSample(new BlockState[0]);
	}

	@Override
	public int getHeight(int x, int z, Type heightmapType) {
		return 0;
	}

	@Override
	protected Codec<? extends ChunkGenerator> method_28506() {
		return TestGenerator.CODEC;
	}

	@Override
	public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk) {
		// TODO Auto-generated method stub

	}

	@Override
	public ChunkGenerator withSeed(long arg0) {
		return this;
	}

}
