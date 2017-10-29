package tk.nukeduck.vivivox.block;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Image;

import tk.nukeduck.vivivox.world.World;

public class Block {
	protected byte id = 0;
	protected boolean isOpaque = true;
	protected boolean hasDifferentSides = false;
	
	protected boolean isAir = false;
	
	protected Vector3f min = new Vector3f(0.0F, 0.0F, 0.0F);
	protected Vector3f max = new Vector3f(1.0F, 1.0F, 1.0F);
	
	protected String name;
	
	public static Block[] blocks = new Block[256];
	public static int size = 0;
	
	public static Image blockMap;
	
	public static void blockIconRegister() {
		try {
			blockMap = new Image("src/textures/blockMap.png");
			blockMap.setFilter(Image.FILTER_NEAREST);
		} catch(Exception e) {
			System.err.println("Block icons could not be registered. This will not go well.");
		}
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
		this.textureX = x;
		this.textureY = y;
		this.textureCoordX = (float) (x) * s;
		this.textureCoordY = (float) (y) * s;
		
		return this;
	}
	
	public void blockTick(World world, int x, int y, int z) {
		// Do whatever the block has to do here
	}
	
	public static final Block air = new Block(0, "Air").setTransparent().setIsAir();
	public static final Block dirt = new Block(1, "Dirt").setTextureOffset(1, 0);
	public static final Block grass = new Block(2, "Grass").setDifferentSides(0, 0, 1, 0, 2, 0, 2, 0, 2, 0, 2, 0);
	public static final Block stone = new Block(3, "Rock").setTextureOffset(3, 0);
	
	public static final Block water = new BlockWater(4).setName("Water").setTransparent().setTextureOffset(4, 0).setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.75f, 1.0f);
	
	public static final Block wood = new Block(5, "Wooden Planks").setTextureOffset(5, 0);
	public static final Block cinderBlock = new Block(6, "Cinder Block").setTextureOffset(6, 0);
	
	public static final Block lamp = new BlockLight(7, 10).setName("Lamp").setTextureOffset(6, 0).setBlockBounds(0.25F, 0F, 0.25F, 0.75F, 0.5F, 0.75F).setTransparent();
	
	public static final Block sand = new Block(8, "Sand").setTextureOffset(7, 0);
	public static final Block log = new Block(9, "Oak Log").setTextureOffset(8, 0);
	
	public static final Block leaves = new Block(10, "Leaves").setTextureOffset(9, 0);
	
	public int textureX, textureY;
	protected float textureCoordX, textureCoordY;
	
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
	
	public float[] textureCoords = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	
	public static final float s = 0.0625f;
	
	public Block setDifferentSides(int topX, int topY, int bottomX, int bottomY, int leftX, int leftY, int rightX, int rightY, int frontX, int frontY, int backX, int backY) {
		this.hasDifferentSides = true;
		
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
	
	public static boolean shouldOccludeTop(World world, int x, int y, int z, int corner) {
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
				return world.getBlock(x - 1, y + 1, z).isOpaque() || world.getBlock(x, y + 1, z - 1).isOpaque() || world.getBlock(x - 1, y + 1, z - 1).isOpaque();
			}
			case 2: {
				return world.getBlock(x - 1, y + 1, z).isOpaque() || world.getBlock(x, y + 1, z + 1).isOpaque() || world.getBlock(x - 1, y + 1, z + 1).isOpaque();
			}
			case 3: {
				return world.getBlock(x + 1, y + 1, z).isOpaque() || world.getBlock(x, y + 1, z + 1).isOpaque() || world.getBlock(x + 1, y + 1, z + 1).isOpaque();
			}
			case 4: {
				return world.getBlock(x + 1, y + 1, z).isOpaque() || world.getBlock(x, y + 1, z - 1).isOpaque() || world.getBlock(x + 1, y + 1, z - 1).isOpaque();
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
	
	public void renderToVBO(World world, int x, int y, int z, boolean smoothLighting, FloatBuffer vertexData) {
		float minX = x + min.x;
		float maxX = x + max.x;
		float minY = y + min.y;
		float maxY = y + max.y;
		float minZ = z + min.z;
		float maxZ = z + max.z;
		
		float[] colorTop = new float[] {(world.getLightLevel(x, y, z) * 0.06F) + 0.4F, (world.getLightLevel(x, y, z) * 0.06F) + 0.4F, (world.getLightLevel(x, y, z) * 0.06F) + 0.4F};
		float[] colorTopOccluded = new float[] {colorTop[0] - 0.3F, colorTop[1] - 0.3F, colorTop[2] - 0.3F};
		
		float[] colorBottom = new float[] {(world.getLightLevel(x, y - 1, z) * 0.06F) + 0.4F, (world.getLightLevel(x, y - 1, z) * 0.06F) + 0.4F, (world.getLightLevel(x, y - 1, z) * 0.06F) + 0.4F};
		float[] colorBottomOccluded = new float[] {colorBottom[0] - 0.3F, colorBottom[1] - 0.3F, colorBottom[2] - 0.3F};
		
		float[] colorFront = new float[] {(world.getLightLevel(x, y, z - 1) * 0.06F) + 0.4F, (world.getLightLevel(x, y, z - 1) * 0.06F) + 0.4F, (world.getLightLevel(x, y, z - 1) * 0.06F) + 0.4F};
		float[] colorFrontOccluded = new float[] {colorFront[0] - 0.3F, colorFront[1] - 0.3F, colorFront[2] - 0.3F};
		
		float[] colorBack = new float[] {(world.getLightLevel(x, y, z + 1) * 0.06F) + 0.4F, (world.getLightLevel(x, y, z + 1) * 0.06F) + 0.4F, (world.getLightLevel(x, y, z + 1) * 0.06F) + 0.4F};
		float[] colorBackOccluded = new float[] {colorBack[0] - 0.3F, colorBack[1] - 0.3F, colorBack[2] - 0.3F};
		
		float[] colorRight = new float[] {(world.getLightLevel(x + 1, y, z) * 0.06F) + 0.4F, (world.getLightLevel(x + 1, y, z) * 0.06F) + 0.4F, (world.getLightLevel(x + 1, y, z) * 0.06F) + 0.4F};
		float[] colorRightOccluded = new float[] {colorRight[0] - 0.3F, colorRight[1] - 0.3F, colorRight[2] - 0.3F};
		
		float[] colorLeft = new float[] {(world.getLightLevel(x - 1, y, z) * 0.06F) + 0.4F, (world.getLightLevel(x - 1, y, z) * 0.06F) + 0.4F, (world.getLightLevel(x - 1, y, z) * 0.06F) + 0.4F};
		float[] colorLeftOccluded = new float[] {colorLeft[0] - 0.3F, colorLeft[1] - 0.3F, colorLeft[2] - 0.3F};
		
		if(!isAir) {
			if(this.hasDifferentSides) {
			
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
			
				// Top
				if(world.getBlock(x, y + 1, z) == null || !world.getBlock(x, y + 1, z).isOpaque() || world.getBlock(x, y + 1, z) == Block.air) {
					vertexData.put(new float[] {minX, maxY, minZ}); // 1
					vertexData.put(new float[] {textureCoords[0] + o, textureCoords[1]+ s - o}); // 1
					if(shouldOccludeTop(world, x, y, z, 1)) {
						vertexData.put(colorTopOccluded);
					} else {
						vertexData.put(colorTop);
					}
					
					vertexData.put(new float[] {minX, maxY, maxZ}); // 2
					vertexData.put(new float[] {textureCoords[0] + o, textureCoords[1] + o}); // 2
					if(shouldOccludeTop(world, x, y, z, 2)) {
						vertexData.put(colorTopOccluded);
					} else {
						vertexData.put(colorTop);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoords[0]+ s - o, textureCoords[1] + o}); // 3
					if(shouldOccludeTop(world, x, y, z, 3)) {
						vertexData.put(colorTopOccluded);
					} else {
						vertexData.put(colorTop);
					}
					
					vertexData.put(new float[] {minX, maxY, minZ}); // 1
					vertexData.put(new float[] {textureCoords[0] + o, textureCoords[1]+ s - o}); // 1
					if(shouldOccludeTop(world, x, y, z, 1)) {
						vertexData.put(colorTopOccluded);
					} else {
						vertexData.put(colorTop);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoords[0]+ s - o, textureCoords[1] + o}); // 3
					if(shouldOccludeTop(world, x, y, z, 3)) {
						vertexData.put(colorTopOccluded);
					} else {
						vertexData.put(colorTop);
					}
					
					vertexData.put(new float[] {maxX, maxY, minZ}); // 4
					vertexData.put(new float[] {textureCoords[0]+ s - o, textureCoords[1]+ s - o}); // 4
					if(shouldOccludeTop(world, x, y, z, 4)) {
						vertexData.put(colorTopOccluded);
					} else {
						vertexData.put(colorTop);
					}
				}
				
				// Bottom
				if(world.getBlock(x, y - 1, z) == null || !world.getBlock(x, y - 1, z).isOpaque() || world.getBlock(x, y - 1, z) == Block.air) {
					vertexData.put(new float[] {minX, minY, minZ}); // 1
					vertexData.put(new float[] {textureCoords[2] + o, textureCoords[3]+ s - o}); // 1
					if(shouldOccludeBottom(world, x, y, z, 1)) {
						vertexData.put(colorBottomOccluded);
					} else {
						vertexData.put(colorBottom);
					}
					
					vertexData.put(new float[] {maxX, minY, minZ}); // 2
					vertexData.put(new float[] {textureCoords[2] + o, textureCoords[3] + o}); // 2
					if(shouldOccludeBottom(world, x, y, z, 2)) {
						vertexData.put(colorBottomOccluded);
					} else {
						vertexData.put(colorBottom);
					}
					
					vertexData.put(new float[] {maxX, minY, maxZ}); // 3
					vertexData.put(new float[] {textureCoords[2]+ s - o, textureCoords[3] + o}); // 3
					if(shouldOccludeBottom(world, x, y, z, 3)) {
						vertexData.put(colorBottomOccluded);
					} else {
						vertexData.put(colorBottom);
					}
					
					vertexData.put(new float[] {minX, minY, minZ}); // 1
					vertexData.put(new float[] {textureCoords[2] + o, textureCoords[3]+ s - o}); // 1
					if(shouldOccludeBottom(world, x, y, z, 1)) {
						vertexData.put(colorBottomOccluded);
					} else {
						vertexData.put(colorBottom);
					}
					
					vertexData.put(new float[] {maxX, minY, maxZ}); // 3
					vertexData.put(new float[] {textureCoords[2]+ s - o, textureCoords[3] + o}); // 3
					if(shouldOccludeBottom(world, x, y, z, 3)) {
						vertexData.put(colorBottomOccluded);
					} else {
						vertexData.put(colorBottom);
					}
					
					vertexData.put(new float[] {minX, minY, maxZ}); // 4
					vertexData.put(new float[] {textureCoords[2]+ s - o, textureCoords[3]+ s - o}); // 4
					if(shouldOccludeBottom(world, x, y, z, 4)) {
						vertexData.put(colorBottomOccluded);
					} else {
						vertexData.put(colorBottom);
					}
				}
				
				// Front
				if(world.getBlock(x, y, z - 1) == null || !world.getBlock(x, y, z - 1).isOpaque() || world.getBlock(x, y, z - 1) == Block.air) {
					vertexData.put(new float[] {minX, minY, minZ}); // 1
					vertexData.put(new float[] {textureCoords[8] + o, textureCoords[9] + s - o}); // 1
					if(shouldOccludeFront(world, x, y, z, 1)) {
						vertexData.put(colorFrontOccluded);
					} else {
						vertexData.put(colorFront);
					}
					
					vertexData.put(new float[] {minX, maxY, minZ}); // 2
					vertexData.put(new float[] {textureCoords[8] + o, textureCoords[9] + o}); // 2
					if(shouldOccludeFront(world, x, y, z, 2)) {
						vertexData.put(colorFrontOccluded);
					} else {
						vertexData.put(colorFront);
					}
					
					vertexData.put(new float[] {maxX, maxY, minZ}); // 3
					vertexData.put(new float[] {textureCoords[8] + s - o, textureCoords[9] + o}); // 3
					if(shouldOccludeFront(world, x, y, z, 3)) {
						vertexData.put(colorFrontOccluded);
					} else {
						vertexData.put(colorFront);
					}
					
					vertexData.put(new float[] {minX, minY, minZ}); // 1
					vertexData.put(new float[] {textureCoords[8] + o, textureCoords[9] + s - o}); // 1
					if(shouldOccludeFront(world, x, y, z, 1)) {
						vertexData.put(colorFrontOccluded);
					} else {
						vertexData.put(colorFront);
					}
					
					vertexData.put(new float[] {maxX, maxY, minZ}); // 3
					vertexData.put(new float[] {textureCoords[8] + s - o, textureCoords[9] + o}); // 3
					if(shouldOccludeFront(world, x, y, z, 3)) {
						vertexData.put(colorFrontOccluded);
					} else {
						vertexData.put(colorFront);
					}
					
					vertexData.put(new float[] {maxX, minY, minZ}); // 4
					vertexData.put(new float[] {textureCoords[8] + s - o, textureCoords[9] + s - o}); // 4
					if(shouldOccludeFront(world, x, y, z, 4)) {
						vertexData.put(colorFrontOccluded);
					} else {
						vertexData.put(colorFront);
					}
				}
				
				// Back
				if(world.getBlock(x, y, z + 1) == null || !world.getBlock(x, y, z + 1).isOpaque() || world.getBlock(x, y, z + 1) == Block.air) {
					vertexData.put(new float[] {maxX, minY, maxZ}); // 1
					vertexData.put(new float[] {textureCoords[10] + o, textureCoords[11] + s - o}); // 1
					if(shouldOccludeBack(world, x, y, z, 1)) {
						vertexData.put(colorBackOccluded);
					} else {
						vertexData.put(colorBack);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 2
					vertexData.put(new float[] {textureCoords[10] + o, textureCoords[11] + o}); // 2
					if(shouldOccludeBack(world, x, y, z, 2)) {
						vertexData.put(colorBackOccluded);
					} else {
						vertexData.put(colorBack);
					}
					
					vertexData.put(new float[] {minX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoords[10] + s - o, textureCoords[11] + o}); // 3
					if(shouldOccludeBack(world, x, y, z, 3)) {
						vertexData.put(colorBackOccluded);
					} else {
						vertexData.put(colorBack);
					}
					
					vertexData.put(new float[] {maxX, minY, maxZ}); // 1
					vertexData.put(new float[] {textureCoords[10] + o, textureCoords[11] + s - o}); // 1
					if(shouldOccludeBack(world, x, y, z, 1)) {
						vertexData.put(colorBackOccluded);
					} else {
						vertexData.put(colorBack);
					}
					
					vertexData.put(new float[] {minX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoords[10] + s - o, textureCoords[11] + o}); // 3
					if(shouldOccludeBack(world, x, y, z, 3)) {
						vertexData.put(colorBackOccluded);
					} else {
						vertexData.put(colorBack);
					}
					
					vertexData.put(new float[] {minX, minY, maxZ}); // 4
					vertexData.put(new float[] {textureCoords[10] + s - o, textureCoords[11] + s - o}); // 4
					if(shouldOccludeBack(world, x, y, z, 4)) {
						vertexData.put(colorBackOccluded);
					} else {
						vertexData.put(colorBack);
					}
				}
				
				// Right
				if(world.getBlock(x + 1, y, z) == null || !world.getBlock(x + 1, y, z).isOpaque() || world.getBlock(x + 1, y, z) == Block.air) {
					vertexData.put(new float[] {maxX, minY, minZ}); // 1
					vertexData.put(new float[] {textureCoords[6] + o, textureCoords[7] + s - o}); // 1
					if(shouldOccludeRight(world, x, y, z, 1)) {
						vertexData.put(colorRightOccluded);
					} else {
						vertexData.put(colorRight);
					}
					
					vertexData.put(new float[] {maxX, maxY, minZ}); // 2
					vertexData.put(new float[] {textureCoords[6] + o, textureCoords[7] + o}); // 2
					if(shouldOccludeRight(world, x, y, z, 2)) {
						vertexData.put(colorRightOccluded);
					} else {
						vertexData.put(colorRight);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoords[6] + s - o, textureCoords[7] + o}); // 3
					if(shouldOccludeRight(world, x, y, z, 3)) {
						vertexData.put(colorRightOccluded);
					} else {
						vertexData.put(colorRight);
					}
					
					vertexData.put(new float[] {maxX, minY, minZ}); // 1
					vertexData.put(new float[] {textureCoords[6] + o, textureCoords[7] + s - o}); // 1
					if(shouldOccludeRight(world, x, y, z, 1)) {
						vertexData.put(colorRightOccluded);
					} else {
						vertexData.put(colorRight);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoords[6] + s - o, textureCoords[7] + o}); // 3
					if(shouldOccludeRight(world, x, y, z, 3)) {
						vertexData.put(colorRightOccluded);
					} else {
						vertexData.put(colorRight);
					}
					
					vertexData.put(new float[] {maxX, minY, maxZ}); // 4
					vertexData.put(new float[] {textureCoords[6] + s - o, textureCoords[7] + s - o}); // 4
					if(shouldOccludeRight(world, x, y, z, 4)) {
						vertexData.put(colorRightOccluded);
					} else {
						vertexData.put(colorRight);
					}
				}
				
				// Left
				if(world.getBlock(x - 1, y, z) == null || !world.getBlock(x - 1, y, z).isOpaque() || world.getBlock(x - 1, y, z) == Block.air) {
					vertexData.put(new float[] {minX, minY, maxZ}); // 1
					vertexData.put(new float[] {textureCoords[4] + o, textureCoords[5] + s - o}); // 1
					if(shouldOccludeLeft(world, x, y, z, 1)) {
						vertexData.put(colorLeftOccluded);
					} else {
						vertexData.put(colorLeft);
					}
					
					vertexData.put(new float[] {minX, maxY, maxZ}); // 2
					vertexData.put(new float[] {textureCoords[4] + o, textureCoords[5] + o}); // 2
					if(shouldOccludeLeft(world, x, y, z, 2)) {
						vertexData.put(colorLeftOccluded);
					} else {
						vertexData.put(colorLeft);
					}
					
					vertexData.put(new float[] {minX, maxY, minZ}); // 3
					vertexData.put(new float[] {textureCoords[4] + s - o, textureCoords[5] + o}); // 3
					if(shouldOccludeLeft(world, x, y, z, 3)) {
						vertexData.put(colorLeftOccluded);
					} else {
						vertexData.put(colorLeft);
					}
					
					vertexData.put(new float[] {minX, minY, maxZ}); // 1
					vertexData.put(new float[] {textureCoords[4] + o, textureCoords[5] + s - o}); // 1
					if(shouldOccludeLeft(world, x, y, z, 1)) {
						vertexData.put(colorLeftOccluded);
					} else {
						vertexData.put(colorLeft);
					}
					
					vertexData.put(new float[] {minX, maxY, minZ}); // 3
					vertexData.put(new float[] {textureCoords[4] + s - o, textureCoords[5] + o}); // 3
					if(shouldOccludeLeft(world, x, y, z, 3)) {
						vertexData.put(colorLeftOccluded);
					} else {
						vertexData.put(colorLeft);
					}
					
					vertexData.put(new float[] {minX, minY, minZ}); // 4
					vertexData.put(new float[] {textureCoords[4] + s - o, textureCoords[5] + s - o}); // 4
					if(shouldOccludeLeft(world, x, y, z, 4)) {
						vertexData.put(colorLeftOccluded);
					} else {
						vertexData.put(colorLeft);
					}
				}
			} else {
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
				if(world.getBlock(x, y + 1, z) == null || !world.getBlock(x, y + 1, z).isOpaque() || world.getBlock(x, y + 1, z) == Block.air) {
					vertexData.put(new float[] {minX, maxY, minZ}); // 1
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + s - o}); // 1
					if(shouldOccludeTop(world, x, y, z, 1)) {
						vertexData.put(colorTopOccluded);
					} else {
						vertexData.put(colorTop);
					}
					
					vertexData.put(new float[] {minX, maxY, maxZ}); // 2
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + o}); // 2
					if(shouldOccludeTop(world, x, y, z, 2)) {
						vertexData.put(colorTopOccluded);
					} else {
						vertexData.put(colorTop);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + o}); // 3
					if(shouldOccludeTop(world, x, y, z, 3)) {
						vertexData.put(colorTopOccluded);
					} else {
						vertexData.put(colorTop);
					}
					
					vertexData.put(new float[] {minX, maxY, minZ}); // 1
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + s - o}); // 1
					if(shouldOccludeTop(world, x, y, z, 1)) {
						vertexData.put(colorTopOccluded);
					} else {
						vertexData.put(colorTop);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + o}); // 3
					if(shouldOccludeTop(world, x, y, z, 3)) {
						vertexData.put(colorTopOccluded);
					} else {
						vertexData.put(colorTop);
					}
					
					vertexData.put(new float[] {maxX, maxY, minZ}); // 4
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + s - o}); // 4
					if(shouldOccludeTop(world, x, y, z, 4)) {
						vertexData.put(colorTopOccluded);
					} else {
						vertexData.put(colorTop);
					}
				}
				
				// Bottom
				if(world.getBlock(x, y - 1, z) == null || !world.getBlock(x, y - 1, z).isOpaque() || world.getBlock(x, y - 1, z) == Block.air) {
					vertexData.put(new float[] {minX, minY, minZ}); // 1
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + s - o}); // 1
					if(shouldOccludeBottom(world, x, y, z, 1)) {
						vertexData.put(colorBottomOccluded);
					} else {
						vertexData.put(colorBottom);
					}
					
					vertexData.put(new float[] {maxX, minY, minZ}); // 2
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + o}); // 2
					if(shouldOccludeBottom(world, x, y, z, 2)) {
						vertexData.put(colorBottomOccluded);
					} else {
						vertexData.put(colorBottom);
					}
					
					vertexData.put(new float[] {maxX, minY, maxZ}); // 3
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + o}); // 3
					if(shouldOccludeBottom(world, x, y, z, 3)) {
						vertexData.put(colorBottomOccluded);
					} else {
						vertexData.put(colorBottom);
					}
					
					vertexData.put(new float[] {minX, minY, minZ}); // 1
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + s - o}); // 1
					if(shouldOccludeBottom(world, x, y, z, 1)) {
						vertexData.put(colorBottomOccluded);
					} else {
						vertexData.put(colorBottom);
					}
					
					vertexData.put(new float[] {maxX, minY, maxZ}); // 3
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + o}); // 3
					if(shouldOccludeBottom(world, x, y, z, 3)) {
						vertexData.put(colorBottomOccluded);
					} else {
						vertexData.put(colorBottom);
					}
					
					vertexData.put(new float[] {minX, minY, maxZ}); // 4
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + s - o}); // 4
					if(shouldOccludeBottom(world, x, y, z, 4)) {
						vertexData.put(colorBottomOccluded);
					} else {
						vertexData.put(colorBottom);
					}
				}
				
				// Front
				if(world.getBlock(x, y, z - 1) == null || !world.getBlock(x, y, z - 1).isOpaque() || world.getBlock(x, y, z - 1) == Block.air) {
					vertexData.put(new float[] {minX, minY, minZ}); // 1
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + s - o}); // 1
					if(shouldOccludeFront(world, x, y, z, 1)) {
						vertexData.put(colorFrontOccluded);
					} else {
						vertexData.put(colorFront);
					}
					
					vertexData.put(new float[] {minX, maxY, minZ}); // 2
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + o}); // 2
					if(shouldOccludeFront(world, x, y, z, 2)) {
						vertexData.put(colorFrontOccluded);
					} else {
						vertexData.put(colorFront);
					}
					
					vertexData.put(new float[] {maxX, maxY, minZ}); // 3
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + o}); // 3
					if(shouldOccludeFront(world, x, y, z, 3)) {
						vertexData.put(colorFrontOccluded);
					} else {
						vertexData.put(colorFront);
					}
					
					vertexData.put(new float[] {minX, minY, minZ}); // 1
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + s - o}); // 1
					if(shouldOccludeFront(world, x, y, z, 1)) {
						vertexData.put(colorFrontOccluded);
					} else {
						vertexData.put(colorFront);
					}
					
					vertexData.put(new float[] {maxX, maxY, minZ}); // 3
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + o}); // 3
					if(shouldOccludeFront(world, x, y, z, 3)) {
						vertexData.put(colorFrontOccluded);
					} else {
						vertexData.put(colorFront);
					}
					
					vertexData.put(new float[] {maxX, minY, minZ}); // 4
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + s - o}); // 4
					if(shouldOccludeFront(world, x, y, z, 4)) {
						vertexData.put(colorFrontOccluded);
					} else {
						vertexData.put(colorFront);
					}
				}
				
				// Back
				if(world.getBlock(x, y, z + 1) == null || !world.getBlock(x, y, z + 1).isOpaque() || world.getBlock(x, y, z + 1) == Block.air) {
					vertexData.put(new float[] {maxX, minY, maxZ}); // 1
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + s - o}); // 1
					if(shouldOccludeBack(world, x, y, z, 1)) {
						vertexData.put(colorBackOccluded);
					} else {
						vertexData.put(colorBack);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 2
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + o}); // 2
					if(shouldOccludeBack(world, x, y, z, 2)) {
						vertexData.put(colorBackOccluded);
					} else {
						vertexData.put(colorBack);
					}
					
					vertexData.put(new float[] {minX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + o}); // 3
					if(shouldOccludeBack(world, x, y, z, 3)) {
						vertexData.put(colorBackOccluded);
					} else {
						vertexData.put(colorBack);
					}
					
					vertexData.put(new float[] {maxX, minY, maxZ}); // 1
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + s - o}); // 1
					if(shouldOccludeBack(world, x, y, z, 1)) {
						vertexData.put(colorBackOccluded);
					} else {
						vertexData.put(colorBack);
					}
					
					vertexData.put(new float[] {minX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + o}); // 3
					if(shouldOccludeBack(world, x, y, z, 3)) {
						vertexData.put(colorBackOccluded);
					} else {
						vertexData.put(colorBack);
					}
					
					vertexData.put(new float[] {minX, minY, maxZ}); // 4
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + s - o}); // 4
					if(shouldOccludeBack(world, x, y, z, 4)) {
						vertexData.put(colorBackOccluded);
					} else {
						vertexData.put(colorBack);
					}
				}
				
				// Right
				if(world.getBlock(x + 1, y, z) == null || !world.getBlock(x + 1, y, z).isOpaque() || world.getBlock(x + 1, y, z) == Block.air) {
					vertexData.put(new float[] {maxX, minY, minZ}); // 1
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + s - o}); // 1
					if(shouldOccludeRight(world, x, y, z, 1)) {
						vertexData.put(colorRightOccluded);
					} else {
						vertexData.put(colorRight);
					}
					
					vertexData.put(new float[] {maxX, maxY, minZ}); // 2
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + o}); // 2
					if(shouldOccludeRight(world, x, y, z, 2)) {
						vertexData.put(colorRightOccluded);
					} else {
						vertexData.put(colorRight);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + o}); // 3
					if(shouldOccludeRight(world, x, y, z, 3)) {
						vertexData.put(colorRightOccluded);
					} else {
						vertexData.put(colorRight);
					}
					
					vertexData.put(new float[] {maxX, minY, minZ}); // 1
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + s - o}); // 1
					if(shouldOccludeRight(world, x, y, z, 1)) {
						vertexData.put(colorRightOccluded);
					} else {
						vertexData.put(colorRight);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + o}); // 3
					if(shouldOccludeRight(world, x, y, z, 3)) {
						vertexData.put(colorRightOccluded);
					} else {
						vertexData.put(colorRight);
					}
					
					vertexData.put(new float[] {maxX, minY, maxZ}); // 4
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + s - o}); // 4
					if(shouldOccludeRight(world, x, y, z, 4)) {
						vertexData.put(colorRightOccluded);
					} else {
						vertexData.put(colorRight);
					}
				}
				
				// Left
				if(world.getBlock(x - 1, y, z) == null || !world.getBlock(x - 1, y, z).isOpaque() || world.getBlock(x - 1, y, z) == Block.air) {
					vertexData.put(new float[] {minX, minY, maxZ}); // 1
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + s - o}); // 1
					if(shouldOccludeLeft(world, x, y, z, 1)) {
						vertexData.put(colorLeftOccluded);
					} else {
						vertexData.put(colorLeft);
					}
					
					vertexData.put(new float[] {minX, maxY, maxZ}); // 2
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + o}); // 2
					if(shouldOccludeLeft(world, x, y, z, 2)) {
						vertexData.put(colorLeftOccluded);
					} else {
						vertexData.put(colorLeft);
					}
					
					vertexData.put(new float[] {minX, maxY, minZ}); // 3
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + o}); // 3
					if(shouldOccludeLeft(world, x, y, z, 3)) {
						vertexData.put(colorLeftOccluded);
					} else {
						vertexData.put(colorLeft);
					}
					
					vertexData.put(new float[] {minX, minY, maxZ}); // 1
					vertexData.put(new float[] {textureCoordX + o, textureCoordY + s - o}); // 1
					if(shouldOccludeLeft(world, x, y, z, 1)) {
						vertexData.put(colorLeftOccluded);
					} else {
						vertexData.put(colorLeft);
					}
					
					vertexData.put(new float[] {minX, maxY, minZ}); // 3
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + o}); // 3
					if(shouldOccludeLeft(world, x, y, z, 3)) {
						vertexData.put(colorLeftOccluded);
					} else {
						vertexData.put(colorLeft);
					}
					
					vertexData.put(new float[] {minX, minY, minZ}); // 4
					vertexData.put(new float[] {textureCoordX + s - o, textureCoordY + s - o}); // 4
					if(shouldOccludeLeft(world, x, y, z, 4)) {
						vertexData.put(colorLeftOccluded);
					} else {
						vertexData.put(colorLeft);
					}
				}
			}
		}
	}
	
	public static int countTruths(boolean... vars) {
	    int count = 0;
	    for (boolean var : vars) {
	        count += (var ? 1 : 0);
	    }
	    return count;
	}
	
	public static int getShowingSides(World world, int x, int y, int z) {
		return countTruths(
			world.getBlock(x, y + 1, z) == null || !world.getBlock(x, y + 1, z).isOpaque() || world.getBlock(x, y + 1, z) == Block.air,
			world.getBlock(x, y - 1, z) == null || !world.getBlock(x, y - 1, z).isOpaque() || world.getBlock(x, y - 1, z) == Block.air,
			world.getBlock(x, y, z - 1) == null || !world.getBlock(x, y, z - 1).isOpaque() || world.getBlock(x, y, z - 1) == Block.air,
			world.getBlock(x, y, z + 1) == null || !world.getBlock(x, y, z + 1).isOpaque() || world.getBlock(x, y, z + 1) == Block.air,
			world.getBlock(x + 1, y, z) == null || !world.getBlock(x + 1, y, z).isOpaque() || world.getBlock(x + 1, y, z) == Block.air,
			world.getBlock(x - 1, y, z) == null || !world.getBlock(x - 1, y, z).isOpaque() || world.getBlock(x - 1, y, z) == Block.air
		);
	}
}
