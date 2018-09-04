package tk.nukeduck.vivivox.block;

import java.nio.FloatBuffer;

import org.newdawn.slick.Image;

import tk.nukeduck.vivivox.helper.Vec3i;
import tk.nukeduck.vivivox.world.BlockState;
import tk.nukeduck.vivivox.world.World;

public class Block {
	protected byte id = 0;
	protected boolean isOpaque = true;

	protected boolean isAir = false;

	protected String name;

	private static final Block[] blocks = new Block[256];
	public static int size = 0;

	public static Image blockMap;

	public static Block getBlock(int id) {
		if(id < 0 || id >= blocks.length) return air;

		Block block = blocks[id];
		return block != null ? block : air;
	}

	public static void blockIconRegister() {
		//for(int i = 0; i < waters.length; i++) waters[i].setId((byte) (5 + i));

		try {
			blockMap = new Image("textures/blockMap.png");
			blockMap.setFilter(Image.FILTER_NEAREST);
		} catch(Exception e) {
			System.err.println("Block icons could not be registered. This will not go well.");
		}
	}

	public void update(World world, int x, int y, int z) {
		//
	}

	public Block setIsAir() {
		this.isAir = true;
		return this;
	}

	public void blockTick(World world, int x, int y, int z) {
		// Do whatever the block has to do here
	}

	public static final Block air = new Block(0, "Air").setTransparent().setIsAir();
	public static final Block dirt = new Block(1, "Dirt").setTextureOffset(1, 2);
	public static final Block grass = new Block(2, "Grass").setTextureOffset(2, 2, 0, 2, 1, 2);
	public static final Block stone = new Block(3, "Rock").setTextureOffset(3, 2);

	public static final Block water = new BlockWater(4).setName("Water").setTransparent().setTextureOffset(4, 0);

	/*public static final Block[] waters = {
		((BlockWater) water).setHeight(1),
		((BlockWater) water).setHeight(2),
		((BlockWater) water).setHeight(3),
		((BlockWater) water).setHeight(4),
		((BlockWater) water).setHeight(5),
		((BlockWater) water).setHeight(6),
		((BlockWater) water).setHeight(7),
		((BlockWater) water).setHeight(8)
	};*/

	public static final Block wood = new Block(5, "Wooden Planks").setTextureOffset(5, 0);
	public static final Block cinderBlock = new Block(6, "Cinder Block").setTextureOffset(6, 0);

	public static final Block lamp = new BlockLight(7, 10).setName("Lamp").setTextureOffset(6, 0);

	public static final Block sand = new Block(8, "Sand").setTextureOffset(7, 4);
	public static final Block log = new Block(9, "Oak Log").setTextureOffset(8, 0);

	public static final Block leaves = new Block(10, "Leaves").setTextureOffset(9, 0);

	public Block(int id) {
		this.setId((byte)id);
		blocks[id] = this;
		size++;
	}

	public Block(int id, String name) {
		this(id);
		this.setName(name);
	}

	public Block setName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return this.name;
	}

	public Block setId(byte id) {
		this.id = id;
		return this;
	}

	public byte getId() {
		return this.id;
	}

	public Block setTransparent() {
		this.isOpaque = false;
		return this;
	}

	public boolean isOpaque() {
		return this.isOpaque;
	}

	public static boolean isOpaque(final Block block) {
		return block != null && block.isOpaque();
	}

	public boolean isAir() {
		return isAir;
	}

	public static boolean isAir(final Block block) {
		return block != null && block.isAir();
	}

	public float[] textureCoords = new float[12];
	public static final float TEX_UNIT = 0.0625f;

	public Block setTextureOffset(int u, int v, int... coords) {
		int i = 0;

		for(; i + 1 < coords.length; i += 2) {
			textureCoords[i] = coords[i] * TEX_UNIT;
			textureCoords[i + 1] = coords[i + 1] * TEX_UNIT;
		}
		for(; i < textureCoords.length; i += 2) {
			textureCoords[i] = u * TEX_UNIT;
			textureCoords[i + 1] = v * TEX_UNIT;
		}
		return this;
	}

	public static boolean shouldOccludeTop(BlockState state, int corner) {
		/*
		 * 2_____3
		 * |    /|
		 * |   / |
		 * |  /  |
		 * | /   |
		 * |/    |
		 * 1-----4
		 */
		switch(corner) {
			case 1: {
				return state.move(new Vec3i(-1, 1, 0)).getBlock().isOpaque() ||
					state.move(new Vec3i(0, 1, -1)).getBlock().isOpaque() ||
					state.move(new Vec3i(-1, 1, -1)).getBlock().isOpaque();
			}
			case 2: {
				return state.move(new Vec3i(-1, 1, 0)).getBlock().isOpaque() ||
					state.move(new Vec3i(0, 1, 1)).getBlock().isOpaque() ||
					state.move(new Vec3i(-1, 1, 1)).getBlock().isOpaque();
			}
			case 3: {
				return state.move(new Vec3i(1, 1, 0)).getBlock().isOpaque() ||
					state.move(new Vec3i(0, 1, 1)).getBlock().isOpaque() ||
					state.move(new Vec3i(1, 1, 1)).getBlock().isOpaque();
			}
			case 4: {
				return state.move(new Vec3i(1, 1, 0)).getBlock().isOpaque() ||
					state.move(new Vec3i(0, 1, -1)).getBlock().isOpaque() ||
					state.move(new Vec3i(1, 1, -1)).getBlock().isOpaque();
			}
		}
		return false;
	}

	public static boolean shouldOccludeBottom(BlockState state, int corner) {
		/*
		 * 2_____3
		 * |    /|
		 * |   / |
		 * |  /  |
		 * | /   |
		 * |/    |
		 * 1-----4
		 */
		switch(corner) {
			case 1: {
				return state.move(new Vec3i(-1, -1, 0)).getBlock().isOpaque() ||
					state.move(new Vec3i(0, -1, -1)).getBlock().isOpaque() ||
					state.move(new Vec3i(-1, -1, -1)).getBlock().isOpaque();
			}
			case 2: {
				return state.move(new Vec3i(1, -1, 0)).getBlock().isOpaque() ||
					state.move(new Vec3i(0, -1, -1)).getBlock().isOpaque() ||
					state.move(new Vec3i(1, -1, -1)).getBlock().isOpaque();
			}
			case 3: {
				return state.move(new Vec3i(1, -1, 0)).getBlock().isOpaque() ||
					state.move(new Vec3i(0, -1, 1)).getBlock().isOpaque() ||
					state.move(new Vec3i(1, -1, 1)).getBlock().isOpaque();
			}
			case 4: {
				return state.move(new Vec3i(-1, -1, 0)).getBlock().isOpaque() ||
					state.move(new Vec3i(0, -1, 1)).getBlock().isOpaque() ||
					state.move(new Vec3i(-1, -1, 1)).getBlock().isOpaque();
			}
		}
		return false;
	}

	public static boolean shouldOccludeLeft(BlockState state, int corner) {
		/*
		 * 2_____3
		 * |    /|
		 * |   / |
		 * |  /  |
		 * | /   |
		 * |/    |
		 * 1-----4
		 */
		switch(corner) {
			case 1: {
				return state.move(new Vec3i(-1, -1, 0)).getBlock().isOpaque() ||
					state.move(new Vec3i(-1, 0, 1)).getBlock().isOpaque() ||
					state.move(new Vec3i(-1, -1, 1)).getBlock().isOpaque();
			}
			case 2: {
				return state.move(new Vec3i(-1, 0, 1)).getBlock().isOpaque() ||
					state.move(new Vec3i(-1, 1, 0)).getBlock().isOpaque() ||
					state.move(new Vec3i(-1, 1, 1)).getBlock().isOpaque();
			}
			case 3: {
				return state.move(new Vec3i(-1, 1, 0)).getBlock().isOpaque() ||
					state.move(new Vec3i(-1, 1, -1)).getBlock().isOpaque() ||
					state.move(new Vec3i(-1, 0, -1)).getBlock().isOpaque();
			}
			case 4: {
				return state.move(new Vec3i(-1, -1, -1)).getBlock().isOpaque() ||
					state.move(new Vec3i(-1, -1, 0)).getBlock().isOpaque() ||
					state.move(new Vec3i(-1, 0, -1)).getBlock().isOpaque();
			}
		}
		return false;
	}

	public static boolean shouldOccludeRight(BlockState state, int corner) {
		/*
		 * 2_____3
		 * |    /|
		 * |   / |
		 * |  /  |
		 * | /   |
		 * |/    |
		 * 1-----4
		 */
		/*try {
		switch(corner) {
			case 1: {
				return world.getBlock(x + 1, y - 1, z - 1).isOpaque() || world.getBlock(x + 1, y - 1, z).isOpaque() || world.getBlock(x + 1, y, z - 1).isOpaque();
			}
			case 2: {
				return world.getBlock(x + 1, y + 1, z).isOpaque() || world.getBlock(x + 1, y + 1, z - 1).isOpaque() || world.getBlock(x + 1, y, z - 1).isOpaque();
			}
			case 3: {
				return world.getBlock(x + 1, y, z + 1).isOpaque() || world.getBlock(x + 1, y + 1, z).isOpaque() || world.getBlock(x + 1, y + 1, z + 1).isOpaque();
			}
			case 4: {
				return world.getBlock(x + 1, y - 1, z).isOpaque() || world.getBlock(x + 1, y, z + 1).isOpaque() || world.getBlock(x + 1, y - 1, z + 1).isOpaque();
			}
		}
		} catch(Exception e) {
			return false;
		}*/

		return false;
	}

	public static boolean shouldOccludeFront(BlockState state, int corner) {
		/*
		 * 2_____3
		 * |    /|
		 * |   / |
		 * |  /  |
		 * | /   |
		 * |/    |
		 * 1-----4
		 */
		/*try {
		switch(corner) {
			case 1: {
				return world.getBlock(x - 1, y - 1, z - 1).isOpaque() || world.getBlock(x, y - 1, z - 1).isOpaque() || world.getBlock(x - 1, y, z - 1).isOpaque();
			}
			case 2: {
				return world.getBlock(x, y + 1, z - 1).isOpaque() || world.getBlock(x - 1, y + 1, z - 1).isOpaque() || world.getBlock(x - 1, y, z - 1).isOpaque();
			}
			case 3: {
				return world.getBlock(x + 1, y, z - 1).isOpaque() || world.getBlock(x + 1, y + 1, z - 1).isOpaque() || world.getBlock(x, y + 1, z - 1).isOpaque();
			}
			case 4: {
				return world.getBlock(x, y - 1, z - 1).isOpaque() || world.getBlock(x + 1, y - 1, z - 1).isOpaque() || world.getBlock(x + 1, y, z - 1).isOpaque();
			}
		}
		} catch(Exception e) {
			return false;
		}*/

		return false;
	}

	public static boolean shouldOccludeBack(BlockState state, int corner) {
		/*
		 * 2_____3
		 * |    /|
		 * |   / |
		 * |  /  |
		 * | /   |
		 * |/    |
		 * 1-----4
		 */
		/*try {
		switch(corner) {
			case 4: {
				return world.getBlock(x - 1, y - 1, z + 1).isOpaque() || world.getBlock(x, y - 1, z + 1).isOpaque() || world.getBlock(x - 1, y, z + 1).isOpaque();
			}
			case 3: {
				return world.getBlock(x, y + 1, z + 1).isOpaque() || world.getBlock(x - 1, y + 1, z + 1).isOpaque() || world.getBlock(x - 1, y, z + 1).isOpaque();
			}
			case 2: {
				return world.getBlock(x + 1, y, z + 1).isOpaque() || world.getBlock(x + 1, y + 1, z + 1).isOpaque() || world.getBlock(x, y + 1, z + 1).isOpaque();
			}
			case 1: {
				return world.getBlock(x, y - 1, z + 1).isOpaque() || world.getBlock(x + 1, y - 1, z + 1).isOpaque() || world.getBlock(x + 1, y, z + 1).isOpaque();
			}
		}
		} catch(Exception e) {
			return false;
		}*/

		return false;
	}

	public void renderToVBO(BlockView world, BlockState state, boolean smoothLighting, FloatBuffer vertexData) {
		if(isAir) return;

		int x = state.getPosition().x;
		int y = state.getPosition().y;
		int z = state.getPosition().z;

		float minX = x;
		float maxX = x + 1;
		float minY = y;
		float maxY = y + 1;
		float minZ = z;
		float maxZ = z + 1;

		BlockView relative = world.offset(state.getPosition());
		BlockWindow lightWindow = new BlockWindow(relative, new Vec3i(-1, -1, -1), new Vec3i(1, 1, 1));

		float[][][] light = new float[2][2][2];
		for(int k = 0; k < 2; k++) {
			for(int j = 0; j < 2; j++) {
				for(int i = 0; i < 2; i++) {
					light[i][j][k] = lightWindow.move(new Vec3i(i, j, k)).getAverageLight();
				}
			}
		}

		/* 1, 2, 3, 1, 3, 4
		 *
		 * 2_____3
		 * |    /|
		 * |   / |
		 * |  /  |
		 * | /   |
		 * |/    |
		 * 1-----4
		 */

		// Top
		if(!state.up().getBlock().isOpaque()) {
			float[] colors = {light[0][1][0], light[0][1][1], light[1][1][1], light[1][1][0]};
			for(int i = 0; i < colors.length; i++) {
				colors[i] *= 0.1f;
				if(shouldOccludeTop(state, i + 1)) {
					colors[i] += 0.1f;
				} else {
					colors[i] += 0.4f;
				}
			}

			vertexData.put(new float[] {
				minX, maxY, minZ, // 1
				textureCoords[0] + TEX_UNIT, textureCoords[1] + TEX_UNIT,
				colors[0], colors[0], colors[0],
				minX, maxY, maxZ, // 2
				textureCoords[0] + TEX_UNIT, textureCoords[1],
				colors[1], colors[1], colors[1],
				maxX, maxY, maxZ, // 3
				textureCoords[0], textureCoords[1],
				colors[2], colors[2], colors[2],
				minX, maxY, minZ, // 1
				textureCoords[0] + TEX_UNIT, textureCoords[1] + TEX_UNIT,
				colors[0], colors[0], colors[0],
				maxX, maxY, maxZ, // 3
				textureCoords[0], textureCoords[1],
				colors[2], colors[2], colors[2],
				maxX, maxY, minZ, // 4
				textureCoords[0], textureCoords[1] + TEX_UNIT,
				colors[3], colors[3], colors[3]
			});
		}

		// Bottom
		if(!state.down().getBlock().isOpaque()) {
			float[] colors = {light[0][0][0], light[1][0][0], light[1][0][1], light[0][0][1]};
			for(int i = 0; i < colors.length; i++) {
				colors[i] *= 0.1f;
				if(shouldOccludeBottom(state, i + 1)) {
					colors[i] += 0.1f;
				} else {
					colors[i] += 0.4f;
				}
			}

			vertexData.put(new float[] {
				minX, minY, minZ, // 1
				textureCoords[2] + TEX_UNIT, textureCoords[3] + TEX_UNIT,
				colors[0], colors[0], colors[0],
				maxX, minY, minZ, // 2
				textureCoords[2] + TEX_UNIT, textureCoords[3],
				colors[1], colors[1], colors[1],
				maxX, minY, maxZ, // 3
				textureCoords[2], textureCoords[1],
				colors[2], colors[2], colors[2],
				minX, minY, minZ, // 1
				textureCoords[2] + TEX_UNIT, textureCoords[3] + TEX_UNIT,
				colors[0], colors[0], colors[0],
				maxX, minY, maxZ, // 3
				textureCoords[2], textureCoords[1],
				colors[2], colors[2], colors[2],
				minX, minY, maxZ, // 4
				textureCoords[2], textureCoords[3] + TEX_UNIT,
				colors[3], colors[3], colors[3]
			});
		}

		// Front
		if(!state.zneg().getBlock().isOpaque()) {
			float[] colors = {light[0][0][0], light[0][1][0], light[1][1][0], light[1][0][0]};
			for(int i = 0; i < colors.length; i++) {
				colors[i] *= 0.1f;
				if(shouldOccludeFront(state, i + 1)) {
					colors[i] += 0.1f;
				} else {
					colors[i] += 0.4f;
				}
			}

			vertexData.put(new float[] {
				minX, minY, minZ, // 1
				textureCoords[8] + TEX_UNIT, textureCoords[9] + TEX_UNIT,
				colors[0], colors[0], colors[0],
				minX, maxY, minZ, // 2
				textureCoords[8] + TEX_UNIT, textureCoords[9],
				colors[1], colors[1], colors[1],
				maxX, maxY, minZ, // 3
				textureCoords[8], textureCoords[9],
				colors[2], colors[2], colors[2],
				minX, minY, minZ, // 1
				textureCoords[8] + TEX_UNIT, textureCoords[9] + TEX_UNIT,
				colors[0], colors[0], colors[0],
				maxX, maxY, minZ, // 3
				textureCoords[8], textureCoords[9],
				colors[2], colors[2], colors[2],
				maxX, minY, minZ, // 4
				textureCoords[8], textureCoords[9] + TEX_UNIT,
				colors[3], colors[3], colors[3]
			});
		}

		// Back
		if(!state.zpos().getBlock().isOpaque()) {
			float[] colors = {light[1][0][1], light[1][1][1], light[0][1][1], light[0][0][1]};
			for(int i = 0; i < colors.length; i++) {
				colors[i] *= 0.1f;
				if(shouldOccludeBack(state, i + 1)) {
					colors[i] += 0.1f;
				} else {
					colors[i] += 0.4f;
				}
			}

			vertexData.put(new float[] {
				maxX, minY, maxZ, // 1
				textureCoords[10] + TEX_UNIT, textureCoords[11] + TEX_UNIT,
				colors[0], colors[0], colors[0],
				maxX, maxY, maxZ, // 2
				textureCoords[10] + TEX_UNIT, textureCoords[11],
				colors[1], colors[1], colors[1],
				minX, maxY, maxZ, // 3
				textureCoords[10], textureCoords[11],
				colors[2], colors[2], colors[2],
				maxX, minY, maxZ, // 1
				textureCoords[10] + TEX_UNIT, textureCoords[11] + TEX_UNIT,
				colors[0], colors[0], colors[0],
				minX, maxY, maxZ, // 3
				textureCoords[10], textureCoords[11],
				colors[2], colors[2], colors[2],
				minX, minY, maxZ, // 4
				textureCoords[10], textureCoords[11] + TEX_UNIT,
				colors[3], colors[3], colors[3]
			});
		}

		// Right
		if(!state.xpos().getBlock().isOpaque()) {
			float[] colors = {light[1][0][0], light[1][1][0], light[1][1][1], light[1][0][1]};
			for(int i = 0; i < colors.length; i++) {
				colors[i] *= 0.1f;
				if(shouldOccludeRight(state, i + 1)) {
					colors[i] += 0.1f;
				} else {
					colors[i] += 0.4f;
				}
			}

			vertexData.put(new float[] {
				maxX, minY, minZ, // 1
				textureCoords[6] + TEX_UNIT, textureCoords[7] + TEX_UNIT,
				colors[0], colors[0], colors[0],
				maxX, maxY, minZ, // 2
				textureCoords[6] + TEX_UNIT, textureCoords[7],
				colors[1], colors[1], colors[1],
				maxX, maxY, maxZ, // 3
				textureCoords[6], textureCoords[7],
				colors[2], colors[2], colors[2],
				maxX, minY, minZ, // 1
				textureCoords[6] + TEX_UNIT, textureCoords[7] + TEX_UNIT,
				colors[0], colors[0], colors[0],
				maxX, maxY, maxZ, // 3
				textureCoords[6], textureCoords[7],
				colors[2], colors[2], colors[2],
				maxX, minY, maxZ, // 4
				textureCoords[6], textureCoords[7] + TEX_UNIT,
				colors[3], colors[3], colors[3]
			});
		}

		// Left
		if(!state.xneg().getBlock().isOpaque()) {
			float[] colors = {light[0][0][1], light[0][1][1], light[0][1][0], light[0][0][0]};
			for(int i = 0; i < colors.length; i++) {
				colors[i] *= 0.1f;
				if(shouldOccludeLeft(state, i + 1)) {
					colors[i] += 0.1f;
				} else {
					colors[i] += 0.4f;
				}
			}

			vertexData.put(new float[] {
				minX, minY, maxZ, // 1
				textureCoords[4] + TEX_UNIT, textureCoords[5] + TEX_UNIT,
				colors[0], colors[0], colors[0],
				minX, maxY, maxZ, // 2
				textureCoords[4] + TEX_UNIT, textureCoords[5],
				colors[1], colors[1], colors[1],
				minX, maxY, minZ, // 3
				textureCoords[4], textureCoords[5],
				colors[2], colors[2], colors[2],
				minX, minY, maxZ, // 1
				textureCoords[4] + TEX_UNIT, textureCoords[5] + TEX_UNIT,
				colors[0], colors[0], colors[0],
				minX, maxY, minZ, // 3
				textureCoords[4], textureCoords[5],
				colors[2], colors[2], colors[2],
				minX, minY, minZ, // 4
				textureCoords[4], textureCoords[5] + TEX_UNIT,
				colors[3], colors[3], colors[3]
			});
		}
	}
}
