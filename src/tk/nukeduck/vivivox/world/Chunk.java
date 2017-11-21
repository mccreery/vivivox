package tk.nukeduck.vivivox.world;

import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import tk.nukeduck.vivivox.VivivoxMain;
import tk.nukeduck.vivivox.block.Block;

public class Chunk {
	public static final int chunkSize = 32;
	
	public byte[][][] blocks = new byte[chunkSize][chunkSize][chunkSize];
	public byte[][][] lightLevels = new byte[chunkSize][chunkSize][chunkSize];
	public byte[][] biomes = new byte[chunkSize][chunkSize];
	
	public Chunk(int relativeX, int relativeY, int relativeZ) {
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.relativeZ = relativeZ;
	}
	
	public int getBlockAt(int x, int y, int z) {
		return blocks[x][y][z];
	}
	
	public byte getBiomeAt(int x, int z) {
		return biomes[x][z];
	}
	
	public byte getLightLevelAt(int x, int y, int z) {
		return lightLevels[x][y][z];
	}
	
	public void setBlockAt(int x, int y, int z, Block block) {
		this.blocks[x][y][z] = block.getId();
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
		
		for(int x1 = 0; x1 < blocks.length; x1++) {
			for(int y1 = 0; y1 < blocks.length; y1++) {
				for(int z1 = 0; z1 < blocks.length; z1++) {
					if(getRelativeBlock(x1, y1, z1, world) != null && getRelativeBlock(x1, y1, z1, world) != Block.air && !(getRelativeBlock(x1 + 1, y1, z1, world) != null && getRelativeBlock(x1 + 1, y1, z1, world) != Block.air && getRelativeBlock(x1 + 1, y1, z1, world).isOpaque() && getRelativeBlock(x1 - 1, y1, z1, world) != null && getRelativeBlock(x1 - 1, y1, z1, world) != Block.air && getRelativeBlock(x1 - 1, y1, z1, world).isOpaque() && getRelativeBlock(x1, y1, z1 + 1, world) != null && getRelativeBlock(x1, y1, z1 + 1, world) != Block.air && getRelativeBlock(x1, y1, z1 + 1, world).isOpaque() && getRelativeBlock(x1, y1, z1 - 1, world) != null && getRelativeBlock(x1, y1, z1 - 1, world) != Block.air && getRelativeBlock(x1, y1, z1 - 1, world).isOpaque() && getRelativeBlock(x1, y1 + 1, z1, world) != null && getRelativeBlock(x1, y1 + 1, z1, world) != Block.air && getRelativeBlock(x1, y1 + 1, z1, world).isOpaque() && getRelativeBlock(x1, y1 - 1, z1, world) != null && getRelativeBlock(x1, y1 - 1, z1, world) != Block.air && getRelativeBlock(x1, y1 - 1, z1, world).isOpaque())) {
						if(getRelativeBlock(x1, y1, z1, world) != Block.water) {
							amountOfVertices += Block.getShowingSides(world, relativeX + x1, relativeY + y1, relativeZ + z1) * 6;
						} else {
							if(world.getBlock(relativeX + x1, relativeY + y1 + 1, relativeZ + z1) != Block.water) amountOfWaterVertices += 6;
						}
					}
				}
			}
		}
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(amountOfVertices * vertexSize + amountOfVertices * colorSize + amountOfVertices * textureSize);
		FloatBuffer waterVertexData = BufferUtils.createFloatBuffer(amountOfWaterVertices * vertexSize + amountOfWaterVertices * colorSize + amountOfWaterVertices * textureSize);
		
		try {
			for(int x1 = 0; x1 < blocks.length; x1++) {
				for(int y1 = 0; y1 < blocks.length; y1++) {
					for(int z1 = 0; z1 < blocks.length; z1++) {
						if(getRelativeBlock(x1, y1, z1, world) != null && getRelativeBlock(x1, y1, z1, world) != Block.air && !(getRelativeBlock(x1 + 1, y1, z1, world) != null && getRelativeBlock(x1 + 1, y1, z1, world) != Block.air && getRelativeBlock(x1 + 1, y1, z1, world).isOpaque() && getRelativeBlock(x1 - 1, y1, z1, world) != null && getRelativeBlock(x1 - 1, y1, z1, world) != Block.air && getRelativeBlock(x1 - 1, y1, z1, world).isOpaque() && getRelativeBlock(x1, y1, z1 + 1, world) != null && getRelativeBlock(x1, y1, z1 + 1, world) != Block.air && getRelativeBlock(x1, y1, z1 + 1, world).isOpaque() && getRelativeBlock(x1, y1, z1 - 1, world) != null && getRelativeBlock(x1, y1, z1 - 1, world) != Block.air && getRelativeBlock(x1, y1, z1 - 1, world).isOpaque() && getRelativeBlock(x1, y1 + 1, z1, world) != null && getRelativeBlock(x1, y1 + 1, z1, world) != Block.air && getRelativeBlock(x1, y1 + 1, z1, world).isOpaque() && getRelativeBlock(x1, y1 - 1, z1, world) != null && getRelativeBlock(x1, y1 - 1, z1, world) != Block.air && getRelativeBlock(x1, y1 - 1, z1, world).isOpaque())) {
							if(getRelativeBlock(x1, y1, z1, world) != Block.water) {
								getRelativeBlock(x1, y1, z1, world).renderToVBO(world, relativeX + x1, relativeY + y1, relativeZ + z1, smoothLighting, vertexData);
							} else {
								getRelativeBlock(x1, y1, z1, world).renderToVBO(world, relativeX + x1, relativeY + y1, relativeZ + z1, smoothLighting, waterVertexData);
							}
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		vertexData.flip();
		
		world.chunksRender[x][y][z] = world.chunksRenderWater[x][y][z] = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, world.chunksRender[x][y][z]);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		// No need to bind default buffer because we're about to rebind anyway
		
		waterVertexData.flip();
		
		world.chunksRenderWater[x][y][z] = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, world.chunksRenderWater[x][y][z]);
		glBufferData(GL_ARRAY_BUFFER, waterVertexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	public static int renderMax = VivivoxMain.renderDistance + 4;
	
	public int relativeX;
	public int relativeY;
	public int relativeZ;
	
	public Block getRelativeBlock(int x, int y, int z, World world) {
		return world.getBlock(relativeX + x, relativeY + y, relativeZ + z);
	}
	
	public byte getRelativeBiome(int x, int z, World world) {
		return world.getBiome(relativeX + x, relativeZ + z);
	}

	public void updateBlocks() {
		for(int x = 0; x < chunkSize; x++) {
			for(int y = 0; y < chunkSize; y++) {
				for(int z = 0; z < chunkSize; z++) {
					
				}
			}
		}
	}
}
