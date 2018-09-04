package tk.nukeduck.vivivox.world;

import java.util.ArrayList;
import java.util.List;

import tk.nukeduck.vivivox.VivivoxMain;
import tk.nukeduck.vivivox.block.Block;
import tk.nukeduck.vivivox.block.BlockView;
import tk.nukeduck.vivivox.helper.Vec3i;

public class Chunk extends BlockView {
	public static final int chunkSize = 32;
	public static final Vec3i chunkSizeVec = new Vec3i(chunkSize, chunkSize, chunkSize);

	public Block[][][] blocks = new Block[chunkSize][chunkSize][chunkSize];
	public byte[][][] lightLevels = new byte[chunkSize][chunkSize][chunkSize];
	public byte[][] biomes = new byte[chunkSize][chunkSize];

	private final World world;
	private final Vec3i position;

	public Chunk(World world, Vec3i position) {
		super(position, position.add(chunkSizeVec));
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
			updateVBOs();
		}

		@Override
		public void setLightLevel(byte light) {
			lightLevels[localPosition.x][localPosition.y][localPosition.z] = light;
			updateVBOs();
		}

		@Override
		public void setBiome(byte id) {
			biomes[localPosition.x][localPosition.z] = id;
		}

		private void updateVBOs() {
			if(!VivivoxMain.hasStartedGame) return;

			Chunk.this.updateVBO(false);
			List<BlockState> invalid = new ArrayList<>();

			if(localPosition.x == 0) invalid.add(xneg());
			else if(localPosition.x == blocks.length - 1) invalid.add(xpos());
			if(localPosition.y == 0) invalid.add(down());
			else if(localPosition.y == blocks.length - 1) invalid.add(up());
			if(localPosition.z == 0) invalid.add(zneg());
			else if(localPosition.z == blocks.length - 1) invalid.add(zpos());

			for(BlockState state : invalid) {
				world.getChunk(state.getPosition()).updateVBO(false);
			}
		}

		public BlockState move(Vec3i offset) {
			return getState(position.add(offset));
		}
	}

	public BlockState getState(Vec3i position) {
		if(position.in(min, max)) {
			return new State(position);
		} else {
			return world.getState(position);
		}
	}

	public void setBiomeAt(int x, int z, byte id) {
		this.biomes[x][z] = id;
	}

	public void setLightLevelAt(int x, int y, int z, byte light) {
		this.lightLevels[x][y][z] = light;
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
