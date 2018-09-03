package tk.nukeduck.vivivox.block;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Image;

import tk.nukeduck.vivivox.helper.Vec3i;
import tk.nukeduck.vivivox.world.BlockState;
import tk.nukeduck.vivivox.world.World;

public class Block {
	protected byte id = 0;
	protected boolean isOpaque = true;

	protected boolean isAir = false;

	protected Vector3f min = new Vector3f(0.0F, 0.0F, 0.0F);
	protected Vector3f max = new Vector3f(1.0F, 1.0F, 1.0F);

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

	public Block setBlockBounds(float x, float y, float z, float x2, float y2, float z2) {
		this.min = new Vector3f(x, y, z);
		this.max = new Vector3f(x2, y2, z2);
		return this;
	}

	public Block setIsAir() {
		this.isAir = true;
		return this;
	}

	public Block setTextureOffset(int x, int y) {
		setDifferentSides(x, y, x, y, x, y, x, y, x, y, x, y);
		return this;
	}

	public void blockTick(World world, int x, int y, int z) {
		// Do whatever the block has to do here
	}

	public static final Block air = new Block(0, "Air").setTransparent().setIsAir();
	public static final Block dirt = new Block(1, "Dirt").setTextureOffset(1, 2);
	public static final Block grass = new Block(2, "Grass").setDifferentSides(0, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2);
	public static final Block stone = new Block(3, "Rock").setTextureOffset(3, 2);

	public static final Block water = new BlockWater(4).setName("Water").setTransparent().setTextureOffset(4, 0).setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.75f, 1.0f);

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

	public static final Block lamp = new BlockLight(7, 10).setName("Lamp").setTextureOffset(6, 0).setBlockBounds(0.25F, 0F, 0.25F, 0.75F, 0.5F, 0.75F).setTransparent();

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

	public float[] textureCoords = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

	public static final float s = 0.0625f;

	public Block setDifferentSides(int topX, int topY, int bottomX, int bottomY, int leftX, int leftY, int rightX, int rightY, int frontX, int frontY, int backX, int backY) {
		textureCoords[0] = (float) (topX) * s;
		textureCoords[1] = (float) (topY) * s;
		textureCoords[2] = (float) (bottomX) * s;
		textureCoords[3] = (float) (bottomY) * s;
		textureCoords[4] = (float) (leftX) * s;
		textureCoords[5] = (float) (leftY) * s;
		textureCoords[6] = (float) (rightX) * s;
		textureCoords[7] = (float) (rightY) * s;
		textureCoords[8] = (float) (frontX) * s;
		textureCoords[9] = (float) (frontY) * s;
		textureCoords[10] = (float) (backX) * s;
		textureCoords[11] = (float) (backY) * s;
		return this;
	}

	public static final float o = 0.0001f;

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
		try {
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
		} catch(Exception e) {
			return false;
		}

		return false;
	}

	public static boolean shouldOccludeBottom(World world, int x, int y, int z, int corner) {
		/*
		 * 2_____3
		 * |    /|
		 * |   / |
		 * |  /  |
		 * | /   |
		 * |/    |
		 * 1-----4
		 */
		try {
		switch(corner) {
			case 1: {
				return world.getBlock(x - 1, y - 1, z).isOpaque() || world.getBlock(x, y - 1, z - 1).isOpaque() || world.getBlock(x - 1, y - 1, z - 1).isOpaque();
			}
			case 2: {
				return world.getBlock(x + 1, y - 1, z).isOpaque() || world.getBlock(x, y - 1, z - 1).isOpaque() || world.getBlock(x + 1, y - 1, z - 1).isOpaque();
			}
			case 3: {
				return world.getBlock(x + 1, y - 1, z).isOpaque() || world.getBlock(x, y - 1, z + 1).isOpaque() || world.getBlock(x + 1, y - 1, z + 1).isOpaque();
			}
			case 4: {
				return world.getBlock(x - 1, y - 1, z).isOpaque() || world.getBlock(x, y - 1, z + 1).isOpaque() || world.getBlock(x - 1, y - 1, z + 1).isOpaque();
			}
		}
		} catch(Exception e) {
			return false;
		}

		return false;
	}

	public static boolean shouldOccludeLeft(World world, int x, int y, int z, int corner) {
		/*
		 * 2_____3
		 * |    /|
		 * |   / |
		 * |  /  |
		 * | /   |
		 * |/    |
		 * 1-----4
		 */
		try {
		switch(corner) {
			case 1: {
				return world.getBlock(x - 1, y - 1, z).isOpaque() || world.getBlock(x - 1, y, z + 1).isOpaque() || world.getBlock(x - 1, y - 1, z + 1).isOpaque();
			}
			case 2: {
				return world.getBlock(x - 1, y, z + 1).isOpaque() || world.getBlock(x - 1, y + 1, z).isOpaque() || world.getBlock(x - 1, y + 1, z + 1).isOpaque();
			}
			case 3: {
				return world.getBlock(x - 1, y + 1, z).isOpaque() || world.getBlock(x - 1, y + 1, z - 1).isOpaque() || world.getBlock(x - 1, y, z - 1).isOpaque();
			}
			case 4: {
				return world.getBlock(x - 1, y - 1, z - 1).isOpaque() || world.getBlock(x - 1, y - 1, z).isOpaque() || world.getBlock(x - 1, y, z - 1).isOpaque();
			}
		}
		} catch(Exception e) {
			return false;
		}

		return false;
	}

	public static boolean shouldOccludeRight(World world, int x, int y, int z, int corner) {
		/*
		 * 2_____3
		 * |    /|
		 * |   / |
		 * |  /  |
		 * | /   |
		 * |/    |
		 * 1-----4
		 */
		try {
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
		}

		return false;
	}

	public static boolean shouldOccludeFront(World world, int x, int y, int z, int corner) {
		/*
		 * 2_____3
		 * |    /|
		 * |   / |
		 * |  /  |
		 * | /   |
		 * |/    |
		 * 1-----4
		 */
		try {
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
		}

		return false;
	}

	public static boolean shouldOccludeBack(World world, int x, int y, int z, int corner) {
		/*
		 * 2_____3
		 * |    /|
		 * |   / |
		 * |  /  |
		 * | /   |
		 * |/    |
		 * 1-----4
		 */
		try {
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
		}

		return false;
	}

	public void renderToVBO(World world, BlockState state, boolean smoothLighting, FloatBuffer vertexData) {
		if(isAir) return;

		int x = state.getPosition().x;
		int y = state.getPosition().y;
		int z = state.getPosition().z;

		float minX = x + min.x;
		float maxX = x + max.x;
		float minY = y + min.y;
		float maxY = y + max.y;
		float minZ = z + min.z;
		float maxZ = z + max.z;

		float[] colorBack = {(world.getLightLevel(x, y, z + 1) * 0.06F) + 0.4F, (world.getLightLevel(x, y, z + 1) * 0.06F) + 0.4F, (world.getLightLevel(x, y, z + 1) * 0.06F) + 0.4F};
		float[] colorBackOccluded = {colorBack[0] - 0.3F, colorBack[1] - 0.3F, colorBack[2] - 0.3F};

		float[] colorRight = {(world.getLightLevel(x + 1, y, z) * 0.06F) + 0.4F, (world.getLightLevel(x + 1, y, z) * 0.06F) + 0.4F, (world.getLightLevel(x + 1, y, z) * 0.06F) + 0.4F};
		float[] colorRightOccluded = {colorRight[0] - 0.3F, colorRight[1] - 0.3F, colorRight[2] - 0.3F};

		float[] colorLeft = {(world.getLightLevel(x - 1, y, z) * 0.06F) + 0.4F, (world.getLightLevel(x - 1, y, z) * 0.06F) + 0.4F, (world.getLightLevel(x - 1, y, z) * 0.06F) + 0.4F};
		float[] colorLeftOccluded = {colorLeft[0] - 0.3F, colorLeft[1] - 0.3F, colorLeft[2] - 0.3F};

		float[] l = new float[27];

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				for(int k = 0; k < 3; k++) {
					l[j * 9 + i * 3 + k] = world.getLightLevel(x + (i - 1), y + (j - 1), z + (k - 1));
				}
			}
		}
		// z, x, y

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
		//world.getBlock(x, y + 1, z) == null || !world.getBlock(x, y + 1, z).isOpaque() || world.getBlock(x, y + 1, z) == Block.air

		//int pos = vertexData.position();

		// Top
		if(!world.getBlock(x, y + 1, z).isOpaque()) {
			float[] colors = {
				(l[18] + l[19] + l[21] + l[22]) / 4 * 0.1F + (shouldOccludeTop(state, 1) ? 0.1F : 0.4F),
				(l[19] + l[20] + l[22] + l[23]) / 4 * 0.1F + (shouldOccludeTop(state, 2) ? 0.1F : 0.4F),
				(l[22] + l[23] + l[25] + l[26]) / 4 * 0.1F + (shouldOccludeTop(state, 3) ? 0.1F : 0.4F),
				(l[21] + l[22] + l[24] + l[25]) / 4 * 0.1F + (shouldOccludeTop(state, 4) ? 0.1F : 0.4F)
			};

			vertexData.put(new float[] {
				minX, maxY, minZ, // 1
				textureCoords[0] + s - 0, textureCoords[1] + s - 0,
				colors[0], colors[0], colors[0],
				minX, maxY, maxZ, // 2
				textureCoords[0] + s - 0, textureCoords[1] + 0,
				colors[1], colors[1], colors[1],
				maxX, maxY, maxZ, // 3
				textureCoords[0] + 0, textureCoords[1] + 0,
				colors[2], colors[2], colors[2],
				minX, maxY, minZ, // 1
				textureCoords[0] + s - 0, textureCoords[1] + s - 0,
				colors[0], colors[0], colors[0],
				maxX, maxY, maxZ, // 3
				textureCoords[0] + 0, textureCoords[1] + 0,
				colors[2], colors[2], colors[2],
				maxX, maxY, minZ, // 4
				textureCoords[0] + o, textureCoords[1] + s - 0,
				colors[3], colors[3], colors[3]
			});
		}

		// Bottom
		if(!world.getBlock(x, y - 1, z).isOpaque()) {
			float[] colors = {
				(l[0] + l[1] + l[3] + l[4]) / 4 * 0.1F + (shouldOccludeBottom(world, x, y, z, 1) ? 0.1F : 0.4F),
				(l[3] + l[4] + l[6] + l[7]) / 4 * 0.1F + (shouldOccludeBottom(world, x, y, z, 2) ? 0.1F : 0.4F),
				(l[4] + l[5] + l[7] + l[8]) / 4 * 0.1F + (shouldOccludeBottom(world, x, y, z, 3) ? 0.1F : 0.4F),
				(l[1] + l[2] + l[4] + l[5]) / 4 * 0.1F + (shouldOccludeBottom(world, x, y, z, 4) ? 0.1F : 0.4F)
			};

			vertexData.put(new float[] {
				minX, minY, minZ, // 1
				textureCoords[0] + s - 0, textureCoords[1] + s - 0,
				colors[0], colors[0], colors[0],
				maxX, minY, minZ, // 2
				textureCoords[0] + s - 0, textureCoords[1] + 0,
				colors[1], colors[1], colors[1],
				maxX, minY, maxZ, // 3
				textureCoords[0] + 0, textureCoords[1] + 0,
				colors[2], colors[2], colors[2],
				minX, minY, minZ, // 1
				textureCoords[0] + s - 0, textureCoords[1] + s - 0,
				colors[0], colors[0], colors[0],
				maxX, minY, maxZ, // 3
				textureCoords[0] + 0, textureCoords[1] + 0,
				colors[2], colors[2], colors[2],
				minX, minY, maxZ, // 4
				textureCoords[0] + o, textureCoords[1] + s - 0,
				colors[3], colors[3], colors[3]
			});
		}

		// Front
		if(!world.getBlock(x, y, z - 1).isOpaque()) {
			float[] colors = {
				(l[0] + l[9] + l[3] + l[12]) / 4 * 0.1F + (shouldOccludeFront(world, x, y, z, 1) ? 0.1F : 0.4F),
				(l[9] + l[18] + l[12] + l[21]) / 4 * 0.1F + (shouldOccludeFront(world, x, y, z, 2) ? 0.1F : 0.4F),
				(l[12] + l[21] + l[15] + l[24]) / 4 * 0.1F + (shouldOccludeFront(world, x, y, z, 3) ? 0.1F : 0.4F),
				(l[3] + l[12] + l[6] + l[15]) / 4 * 0.1F + (shouldOccludeFront(world, x, y, z, 4) ? 0.1F : 0.4F)
			};

			vertexData.put(new float[] {
				minX, minY, minZ, // 1
				textureCoords[8] + s - 0, textureCoords[9] + s - 0,
				colors[0], colors[0], colors[0],
				minX, maxY, minZ, // 2
				textureCoords[8] + s - 0, textureCoords[9] + 0,
				colors[1], colors[1], colors[1],
				maxX, maxY, minZ, // 3
				textureCoords[8] + 0, textureCoords[9] + 0,
				colors[2], colors[2], colors[2],
				minX, minY, minZ, // 1
				textureCoords[8] + s - 0, textureCoords[9] + s - 0,
				colors[0], colors[0], colors[0],
				maxX, maxY, minZ, // 3
				textureCoords[8] + 0, textureCoords[9] + 0,
				colors[2], colors[2], colors[2],
				maxX, minY, minZ, // 4
				textureCoords[8] + o, textureCoords[9] + s - 0,
				colors[3], colors[3], colors[3]
			});
		}

		// Back
		if(!world.getBlock(x, y, z + 1).isOpaque()) {
			vertexData.put(new float[] {maxX, minY, maxZ}); // 1
			vertexData.put(new float[] {textureCoords[10] + s - o, textureCoords[11] + s - o}); // 1
			if(shouldOccludeBack(world, x, y, z, 1)) {
				vertexData.put(colorBackOccluded);
			} else {
				vertexData.put(colorBack);
			}

			vertexData.put(new float[] {maxX, maxY, maxZ}); // 2
			vertexData.put(new float[] {textureCoords[10] + s - o, textureCoords[11] + o}); // 2
			if(shouldOccludeBack(world, x, y, z, 2)) {
				vertexData.put(colorBackOccluded);
			} else {
				vertexData.put(colorBack);
			}

			vertexData.put(new float[] {minX, maxY, maxZ}); // 3
			vertexData.put(new float[] {textureCoords[10] + o, textureCoords[11] + o}); // 3
			if(shouldOccludeBack(world, x, y, z, 3)) {
				vertexData.put(colorBackOccluded);
			} else {
				vertexData.put(colorBack);
			}

			vertexData.put(new float[] {maxX, minY, maxZ}); // 1
			vertexData.put(new float[] {textureCoords[10] + s - o, textureCoords[11] + s - o}); // 1
			if(shouldOccludeBack(world, x, y, z, 1)) {
				vertexData.put(colorBackOccluded);
			} else {
				vertexData.put(colorBack);
			}

			vertexData.put(new float[] {minX, maxY, maxZ}); // 3
			vertexData.put(new float[] {textureCoords[10] + o, textureCoords[11] + o}); // 3
			if(shouldOccludeBack(world, x, y, z, 3)) {
				vertexData.put(colorBackOccluded);
			} else {
				vertexData.put(colorBack);
			}

			vertexData.put(new float[] {minX, minY, maxZ}); // 4
			vertexData.put(new float[] {textureCoords[10] + o, textureCoords[11] + s - o}); // 4
			if(shouldOccludeBack(world, x, y, z, 4)) {
				vertexData.put(colorBackOccluded);
			} else {
				vertexData.put(colorBack);
			}
		}

		// Right
		if(!world.getBlock(x + 1, y, z).isOpaque()) {
			vertexData.put(new float[] {maxX, minY, minZ}); // 1
			vertexData.put(new float[] {textureCoords[6] + s - o, textureCoords[7] + s - o}); // 1
			if(shouldOccludeRight(world, x, y, z, 1)) {
				vertexData.put(colorRightOccluded);
			} else {
				vertexData.put(colorRight);
			}

			vertexData.put(new float[] {maxX, maxY, minZ}); // 2
			vertexData.put(new float[] {textureCoords[6] + s - o, textureCoords[7] + o}); // 2
			if(shouldOccludeRight(world, x, y, z, 2)) {
				vertexData.put(colorRightOccluded);
			} else {
				vertexData.put(colorRight);
			}

			vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
			vertexData.put(new float[] {textureCoords[6] + o, textureCoords[7] + o}); // 3
			if(shouldOccludeRight(world, x, y, z, 3)) {
				vertexData.put(colorRightOccluded);
			} else {
				vertexData.put(colorRight);
			}

			vertexData.put(new float[] {maxX, minY, minZ}); // 1
			vertexData.put(new float[] {textureCoords[6] + s - o, textureCoords[7] + s - o}); // 1
			if(shouldOccludeRight(world, x, y, z, 1)) {
				vertexData.put(colorRightOccluded);
			} else {
				vertexData.put(colorRight);
			}

			vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
			vertexData.put(new float[] {textureCoords[6] + o, textureCoords[7] + o}); // 3
			if(shouldOccludeRight(world, x, y, z, 3)) {
				vertexData.put(colorRightOccluded);
			} else {
				vertexData.put(colorRight);
			}

			vertexData.put(new float[] {maxX, minY, maxZ}); // 4
			vertexData.put(new float[] {textureCoords[6] + o, textureCoords[7] + s - o}); // 4
			if(shouldOccludeRight(world, x, y, z, 4)) {
				vertexData.put(colorRightOccluded);
			} else {
				vertexData.put(colorRight);
			}
		}

		// Left
		if(!world.getBlock(x - 1, y, z).isOpaque()) {
			vertexData.put(new float[] {minX, minY, maxZ}); // 1
			vertexData.put(new float[] {textureCoords[4] + s - o, textureCoords[5] + s - o}); // 1
			if(shouldOccludeLeft(world, x, y, z, 1)) {
				vertexData.put(colorLeftOccluded);
			} else {
				vertexData.put(colorLeft);
			}

			vertexData.put(new float[] {minX, maxY, maxZ}); // 2
			vertexData.put(new float[] {textureCoords[4] + s - o, textureCoords[5] + o}); // 2
			if(shouldOccludeLeft(world, x, y, z, 2)) {
				vertexData.put(colorLeftOccluded);
			} else {
				vertexData.put(colorLeft);
			}

			vertexData.put(new float[] {minX, maxY, minZ}); // 3
			vertexData.put(new float[] {textureCoords[4] + o, textureCoords[5] + o}); // 3
			if(shouldOccludeLeft(world, x, y, z, 3)) {
				vertexData.put(colorLeftOccluded);
			} else {
				vertexData.put(colorLeft);
			}

			vertexData.put(new float[] {minX, minY, maxZ}); // 1
			vertexData.put(new float[] {textureCoords[4] + s - o, textureCoords[5] + s - o}); // 1
			if(shouldOccludeLeft(world, x, y, z, 1)) {
				vertexData.put(colorLeftOccluded);
			} else {
				vertexData.put(colorLeft);
			}

			vertexData.put(new float[] {minX, maxY, minZ}); // 3
			vertexData.put(new float[] {textureCoords[4] + o, textureCoords[5] + o}); // 3
			if(shouldOccludeLeft(world, x, y, z, 3)) {
				vertexData.put(colorLeftOccluded);
			} else {
				vertexData.put(colorLeft);
			}

			vertexData.put(new float[] {minX, minY, minZ}); // 4
			vertexData.put(new float[] {textureCoords[4] + o, textureCoords[5] + s - o}); // 4
			if(shouldOccludeLeft(world, x, y, z, 4)) {
				vertexData.put(colorLeftOccluded);
			} else {
				vertexData.put(colorLeft);
			}
		}
	}
}
