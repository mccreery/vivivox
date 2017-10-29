package tk.nukeduck.vivivox.block;

import java.nio.FloatBuffer;
import tk.nukeduck.vivivox.world.World;

public class BlockWater extends Block {
	public BlockWater(int id) {
		super(id);
		this.setTransparent();
	}
	
	public static int animationFrame = 0;
	
	@Override
	public void renderToVBO(World world, int x, int y, int z, boolean smoothLighting, FloatBuffer vertexData) {
		float minX = min.x + x;
		float maxX = max.x + x;
		// float minY = min.y + y; Not needed because bottom side is never rendered
		float maxY = max.y + y;
		float minZ = min.z + z;
		float maxZ = max.z + z;
		
		float[] color = new float[] {
			(world.getLightLevel(x, y, z) * 0.06F) + 0.4F, (world.getLightLevel(x, y, z) * 0.06F) + 0.4F, (world.getLightLevel(x, y, z) * 0.06F) + 0.4F
		};
		
		float[] colorOccluded = new float[] {
				color[0] - 0.2F, color[1] - 0.2F, color[2] - 0.2F
		};
		
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
			
				// Top
				if(world.getBlock(x, y + 1, z) != Block.water) {
					vertexData.put(new float[] {minX, maxY, minZ}); // 1
					vertexData.put(new float[] {textureCoords[0], textureCoords[1] + s}); // 1
					if(shouldOccludeTop(world, x, y, z, 1)) {
						vertexData.put(colorOccluded);
					} else {
						vertexData.put(color);
					}
					
					vertexData.put(new float[] {minX, maxY, maxZ}); // 2
					vertexData.put(new float[] {textureCoords[0], textureCoords[1]}); // 2
					if(shouldOccludeTop(world, x, y, z, 2)) {
						vertexData.put(colorOccluded);
					} else {
						vertexData.put(color);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoords[0] + s, textureCoords[1]}); // 3
					if(shouldOccludeTop(world, x, y, z, 3)) {
						vertexData.put(colorOccluded);
					} else {
						vertexData.put(color);
					}
					
					vertexData.put(new float[] {minX, maxY, minZ}); // 1
					vertexData.put(new float[] {textureCoords[0], textureCoords[1] + s}); // 1
					if(shouldOccludeTop(world, x, y, z, 1)) {
						vertexData.put(colorOccluded);
					} else {
						vertexData.put(color);
					}
					
					vertexData.put(new float[] {maxX, maxY, maxZ}); // 3
					vertexData.put(new float[] {textureCoords[0] + s, textureCoords[1]}); // 3
					if(shouldOccludeTop(world, x, y, z, 3)) {
						vertexData.put(colorOccluded);
					} else {
						vertexData.put(color);
					}
					
					vertexData.put(new float[] {maxX, maxY, minZ}); // 4
					vertexData.put(new float[] {textureCoords[0] + s, textureCoords[1] + s}); // 4
					if(shouldOccludeTop(world, x, y, z, 4)) {
						vertexData.put(colorOccluded);
					} else {
						vertexData.put(color);
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
			}
		}
	}
}
