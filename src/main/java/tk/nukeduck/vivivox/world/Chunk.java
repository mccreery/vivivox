package tk.nukeduck.vivivox.world;

import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import tk.nukeduck.vivivox.VivivoxMain;
import tk.nukeduck.vivivox.block.Block;
import tk.nukeduck.vivivox.helper.Vec3i;

public class Chunk {
	public static final int chunkSize = 32;
	public static final Vec3i chunkSizeVec = new Vec3i(chunkSize, chunkSize, chunkSize);

	public Block[][][] blocks = new Block[chunkSize][chunkSize][chunkSize];
	public byte[][][] lightLevels = new byte[chunkSize][chunkSize][chunkSize];
	public byte[][] biomes = new byte[chunkSize][chunkSize];

	private final World world;
	private final Vec3i position;

	public Chunk(World world, Vec3i position) {
		this.world = world;
		this.position = position;
	}

	private class State extends BlockState {
		private final Vec3i position;
		private final Vec3i localPosition;

		State(Vec3i position) {
			this.position = position;
			localPosition = position.sub(Chunk.this.position);
		}

		@Override
		public Vec3i getPosition() {
			return position;
		}

		public boolean isValid() {
			return localPosition.in(Vec3i.ZERO, chunkSizeVec);
		}

		public Block getBlock() {
			Block block = blocks[localPosition.x][localPosition.y][localPosition.z];
			return block != null ? block : Block.air;
		}

		public byte getLightLevel() {
			return lightLevels[localPosition.x][localPosition.y][localPosition.z];
		}

		public byte getBiome() {
			return lightLevels[localPosition.x][localPosition.y][localPosition.z];
		}

		@Override
		public void setBlock(Block block) {
			blocks[localPosition.x][localPosition.y][localPosition.z] = block;
		}

		public BlockState move(Vec3i offset) {
			Vec3i translated = position.add(offset);
			State state = new State(translated);

			if(state.isValid()) {
				return state;
			} else {
				return world.getState(translated);
			}
		}
	}

	public BlockState getState(Vec3i position) {
		State state = new State(position);

		if(state.isValid()) {
			return state;
		} else {
			return BlockState.AIR;
		}
	}

	public void setBiomeAt(int x, int z, byte id) {
		this.biomes[x][z] = id;
	}

	public void setLightLevelAt(int x, int y, int z, byte light) {
		this.lightLevels[x][y][z] = light;
	}

	public int amountOfVertices;
	public int amountOfWaterVertices;
	public static final int vertexSize = 3;
	public static final int colorSize = 3;
	public static final int textureSize = 2;

	public void updateVBO(boolean smoothLighting, World world, int x, int y, int z) {
		amountOfVertices = 0;
		amountOfWaterVertices = 0;

		for(int i = 0; i < blocks.length; i++) {
			for(int j = 0; j < blocks.length; j++) {
				for(int k = 0; k < blocks.length; k++) {
					BlockState state = getState(new Vec3i(position.x + i, position.y + j, position.z + k));
					if(state.getBlock().isAir()) continue;

					int sides = 6 - state.getAdjacentBlocks();
					if(sides > 0) {
						if(state.getBlock() != Block.water) {
							amountOfVertices += sides * 6;
						} else if(state.up().getBlock() != Block.water) {
							amountOfWaterVertices += 6;
						}
					}
				}
			}
		}

		final int totalVertexSize = vertexSize + colorSize + textureSize;
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(amountOfVertices * totalVertexSize);
		FloatBuffer waterVertexData = BufferUtils.createFloatBuffer(amountOfWaterVertices * totalVertexSize);

		for(int i = 0; i < blocks.length; i++) {
			for(int j = 0; j < blocks.length; j++) {
				for(int k = 0; k < blocks.length; k++) {
					BlockState state = getState(new Vec3i(position.x + i, position.y + j, position.z + k));
					Block block = state.getBlock();
					if(block.isAir()) continue;

					//int sides = 6 - state.getAdjacentBlocks();
					//if(sides > 0) {
						if(block != Block.water) {
							block.renderToVBO(world, state, smoothLighting, vertexData);
						} else if(state.up().getBlock() != Block.water) {
							block.renderToVBO(world, state, smoothLighting, waterVertexData);
						}
					//}
				}
			}
		}

		vertexData.flip();
		world.chunksRender[x][y][z] = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, world.chunksRender[x][y][z]);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

		waterVertexData.flip();
		world.chunksRenderWater[x][y][z] = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, world.chunksRenderWater[x][y][z]);
		glBufferData(GL_ARRAY_BUFFER, waterVertexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public static int renderMax = VivivoxMain.renderDistance + 4;

	public void updateBlocks() {
		for(int x = 0; x < chunkSize; x++) {
			for(int y = 0; y < chunkSize; y++) {
				for(int z = 0; z < chunkSize; z++) {

				}
			}
		}
	}
}
