package tk.nukeduck.vivivox.block;

import java.nio.FloatBuffer;
import tk.nukeduck.vivivox.world.World;

public class BlockWater extends Block {
	public BlockWater(int id) {
		super(id);
		this.setTransparent();
	}
	
	public BlockWater setHeight(int height) {
		this.setBlockBounds(0, 0, 0, 1, (float) height * 0.125F, 1);
		return this;
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
	
	@Override
	public void renderToVBO(World world, int x, int y, int z, boolean smoothLighting, FloatBuffer vertexData) {
		float minX = min.x + x;
		float maxX = max.x + x;
		// float minY = min.y + y; Not needed because bottom side is never rendered
		float maxY = max.y + y;
		float minZ = min.z + z;
		float maxZ = max.z + z;
		
		//
		float thisBlock = world.getLightLevel(x, y + 1, z);
		
		float xPlus = world.getLightLevel(x + 1, y + 1, z);
		float xMinus = world.getLightLevel(x - 1, y + 1, z);
		float zPlus = world.getLightLevel(x, y + 1, z + 1);
		float zMinus = world.getLightLevel(x, y + 1, z - 1);
		
		float xPluszPlus = world.getLightLevel(x + 1, y + 1, z + 1);
		float xMinuszMinus = world.getLightLevel(x - 1, y + 1, z - 1);
		float xPluszMinus = world.getLightLevel(x + 1, y + 1, z - 1);
		float xMinuszPlus = world.getLightLevel(x - 1, y + 1, z + 1);
		
		float[] colors = {(thisBlock + zMinus + xMinuszMinus + xMinus) / 4 * 0.1F + 0.4F, (thisBlock + xMinus + zPlus + xMinuszPlus) / 4 * 0.1F + 0.4F, (thisBlock + zPlus + xPluszPlus + xPlus) / 4 * 0.1F + 0.4F, (thisBlock + xPlus + zMinus + xPluszMinus) / 4 * 0.1F + 0.4F};
		float[][] colorsTop = new float[][] {{colors[0], colors[0], colors[0]}, {colors[1], colors[1], colors[1]}, {colors[2], colors[2], colors[2]}, {colors[3], colors[3], colors[3]}};
		float[][] colorsTopOccluded = new float[][] {{colors[0] - 0.3F, colors[0] - 0.3F, colors[0] - 0.3F}, {colors[1] - 0.3F, colors[1] - 0.3F, colors[1] - 0.3F}, {colors[2] - 0.3F, colors[2] - 0.3F, colors[2] - 0.3F}, {colors[3] - 0.3F, colors[3] - 0.3F, colors[3] - 0.3F}};
		//
		
		//float[] color = new float[] {
		//	(world.getLightLevel(x, y, z) * 0.06F) + 0.4F, (world.getLightLevel(x, y, z) * 0.06F) + 0.4F, (world.getLightLevel(x, y, z) * 0.06F) + 0.4F
		//};
		
		//float[] colorOccluded = new float[] {
		//		color[0] - 0.2F, color[1] - 0.2F, color[2] - 0.2F
		//};
		
		if(!isAir) {
			//if(this.hasDifferentSides) {
			
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
				if(world.getBlock(x, y + 1, z) != Block.water) {
					vertexData.put(new float[] {minX, maxY, minZ}); // 1
					vertexData.put(new float[] {textureCoordX, textureCoordY + s}); // 1
					if(shouldOccludeTop(world, x, y, z, 1)) {
						vertexData.put(colorsTopOccluded[0]);
					} else {
						vertexData.put(colorsTop[0]);
					}
					
					vertexData.put(new float[] {minX, maxY, maxZ}); // 2
					vertexData.put(new float[] {textureCoordX, textureCoordY}); // 2
					if(shouldOccludeTop(world, x, y, z, 2)) {
						vertexData.put(colorsTopOccluded[1]);
					} else {
						vertexData.put(colorsTop[1]);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoordX + s, textureCoordY}); // 3
					if(shouldOccludeTop(world, x, y, z, 3)) {
						vertexData.put(colorsTopOccluded[2]);
					} else {
						vertexData.put(colorsTop[2]);
					}
					
					vertexData.put(new float[] {minX, maxY, minZ}); // 1
					vertexData.put(new float[] {textureCoordX, textureCoordY + s}); // 1
					if(shouldOccludeTop(world, x, y, z, 1)) {
						vertexData.put(colorsTopOccluded[0]);
					} else {
						vertexData.put(colorsTop[0]);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoordX + s, textureCoordY}); // 3
					if(shouldOccludeTop(world, x, y, z, 3)) {
						vertexData.put(colorsTopOccluded[2]);
					} else {
						vertexData.put(colorsTop[2]);
					}
					
					vertexData.put(new float[] {maxX, maxY, minZ}); // 4
					vertexData.put(new float[] {textureCoordX + s, textureCoordY + s}); // 4
					if(shouldOccludeTop(world, x, y, z, 4)) {
						vertexData.put(colorsTopOccluded[3]);
					} else {
						vertexData.put(colorsTop[3]);
					}
				}
			/*} else {
			/* 1, 2, 3, 1, 3, 4
			 *
			 * 2_____3
			 * |    /|
			 * |   / |
			 * |  /  |
			 * | /   |
			 * |/    |
			 * 1-----4
			 *//*
			
				// Top
				if(world.getBlock(x, y + 1, z) != Block.water) {
					vertexData.put(new float[] {minX, maxY, minZ}); // 1
					vertexData.put(new float[] {textureCoordX, textureCoordY + s}); // 1
					if(shouldOccludeTop(world, x, y, z, 1)) {
						vertexData.put(colorOccluded);
					} else {
						vertexData.put(color);
					}
					
					vertexData.put(new float[] {minX, maxY, maxZ}); // 2
					vertexData.put(new float[] {textureCoordX, textureCoordY}); // 2
					if(shouldOccludeTop(world, x, y, z, 2)) {
						vertexData.put(colorOccluded);
					} else {
						vertexData.put(color);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoordX + s, textureCoordY}); // 3
					if(shouldOccludeTop(world, x, y, z, 3)) {
						vertexData.put(colorOccluded);
					} else {
						vertexData.put(color);
					}
					
					vertexData.put(new float[] {minX, maxY, minZ}); // 1
					vertexData.put(new float[] {textureCoordX, textureCoordY + s}); // 1
					if(shouldOccludeTop(world, x, y, z, 1)) {
						vertexData.put(colorOccluded);
					} else {
						vertexData.put(color);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoordX + s, textureCoordY}); // 3
					if(shouldOccludeTop(world, x, y, z, 3)) {
						vertexData.put(colorOccluded);
					} else {
						vertexData.put(color);
					}
					
					vertexData.put(new float[] {maxX, maxY, minZ}); // 4
					vertexData.put(new float[] {textureCoordX + s, textureCoordY + s}); // 4
					if(shouldOccludeTop(world, x, y, z, 4)) {
						vertexData.put(colorOccluded);
					} else {
						vertexData.put(color);
					}
				}
			}*/
		}
	}
}
