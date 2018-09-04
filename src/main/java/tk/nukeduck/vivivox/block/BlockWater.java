package tk.nukeduck.vivivox.block;

public class BlockWater extends Block {
	public BlockWater(int id) {
		super(id);
		this.setTransparent();
	}

	public static int animationFrame = 0;

	/*@Override
	public void update(World world, int x, int y, int z) {
		int height = positionOf(Block.waters, this) + 1;

		if(world.getBlock(x,  y - 1, z).equals(Block.air)) {
			world.setBlock(x, y - 1, z, this);
			world.setBlock(x, y, z, Block.air);
		} else if(world.getBlock(x, y - 1, z) instanceof BlockWater) {
			int heightUnder = positionOf(Block.waters, world.getBlock(x, y - 1, z)) + 1;
			if(heightUnder < 8) {
				heightUnder += height;
				if(heightUnder > 8) heightUnder = 8;
			}
		}
	}*/

	public int positionOf(Block[] blocks, Block block) {
		for(int i = 0; i < blocks.length; i++) {
			if(blocks[i].equals(block)) return i;
		}
		return 0;
	}
/*
	@Override
	public void renderToVBO(World world, BlockState state, boolean smoothLighting, FloatBuffer vertexData) {
		int x = state.getPosition().x;
		int y = state.getPosition().y;
		int z = state.getPosition().z;

		float minX = min.x + x;
		float maxX = max.x + x;
		// float minY = min.y + y; Not needed because bottom side is never rendered
		float maxY = max.y + y;
		float minZ = min.z + z;
		float maxZ = max.z + z;

		 1, 2, 3, 1, 3, 4
			*
			* 2_____3
			* |    /|
			* |   / |
			* |  /  |
			* | /   |
			* |/    |
			* 1-----4


		float[] l = new float[27];

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				for(int k = 0; k < 3; k++) {
					l[j * 9 + i * 3 + k] = world.getLightLevel(x + (i - 1), y + (j - 1), z + (k - 1));
				}
			}
		}

		if(!world.getBlock(x, y + 1, z).isOpaque()) {
			float[] colors = {
				(l[18] + l[19] + l[21] + l[22]) / 4 * 0.1F + (shouldOccludeTop(state, 1) ? 0.1F : 0.4F),
				(l[19] + l[20] + l[22] + l[23]) / 4 * 0.1F + (shouldOccludeTop(state, 2) ? 0.1F : 0.4F),
				(l[22] + l[23] + l[25] + l[26]) / 4 * 0.1F + (shouldOccludeTop(state, 3) ? 0.1F : 0.4F),
				(l[21] + l[22] + l[24] + l[25]) / 4 * 0.1F + (shouldOccludeTop(state, 4) ? 0.1F : 0.4F)
			};

			vertexData.put(new float[] {
				minX, maxY, minZ, // 1
				textureCoords[0] + TEX_UNIT - 0, textureCoords[1] + TEX_UNIT - 0,
				colors[0], colors[0], colors[0],
				minX, maxY, maxZ, // 2
				textureCoords[0] + TEX_UNIT - 0, textureCoords[1] + 0,
				colors[1], colors[1], colors[1],
				maxX, maxY, maxZ, // 3
				textureCoords[0] + 0, textureCoords[1] + 0,
				colors[2], colors[2], colors[2],
				minX, maxY, minZ, // 1
				textureCoords[0] + TEX_UNIT - 0, textureCoords[1] + TEX_UNIT - 0,
				colors[0], colors[0], colors[0],
				maxX, maxY, maxZ, // 3
				textureCoords[0] + 0, textureCoords[1] + 0,
				colors[2], colors[2], colors[2],
				maxX, maxY, minZ, // 4
				textureCoords[0], textureCoords[1] + TEX_UNIT - 0,
				colors[3], colors[3], colors[3]
			});
		}
}*/
}
