package tk.nukeduck.vivivox.item;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import tk.nukeduck.vivivox.helper.GeneralTools;

public class Item {
	protected byte id = 0;
	
	protected String name;
	
	public int renderValue = 0;
	
	public static Item[] items = new Item[256];
	public static int size = 0;
	
	public static Item sword, head, debug, gun;
	
	public static Image itemMap;
	
	public static Color[] subTextureIndices = new Color[32];
	
	public static void itemIconRegister() {
		try {
			itemMap = new Image("src/textures/itemMap.png");
			itemMap.setFilter(Image.FILTER_NEAREST);
		} catch(Exception e) {
			System.err.println("Item icons could not be registered. This will not go well.");
		}
		
		byte[] data = itemMap.getTexture().getTextureData();
		
		// Do texture coordinate registry here too because why not
		for(int x = 0; x < itemMap.getWidth(); x += 8) {
			subTextureIndices[x / 8] = getPixelAt(x, 128, itemMap.getWidth(), data);
		}
		
		sword = new Item(0).setTextureOffsets(0, 0, 1, 0, 0, 0);
		sword.updateVBO();
		
		head = new Item(1).setTextureOffsets(2, 0, 3, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 5, 0);
		head.updateVBO();
		
		debug = new Item(2).setTextureOffsets(6, 0, 7, 0, 6, 0, 7, 0, 6, 0, 7, 0, 6, 0, 7, 0, 6, 0, 7, 0, 6, 0, 7, 0, 6, 0, 7, 0, 6, 0, 7, 0);
		debug.updateVBO();
		
		gun = new Item(3).setTextureOffsets(8, 0, 9, 0, 10, 0, 9, 0, 8, 0);
		gun.updateVBO();
	}
	
	public Color[][] pixels;
	
	public static Color getPixelAt(int x, int y, int width, byte[] data) {
		//int height = (data.length / 4) / width;
		
		/*int r = data[4 * ((y + 1) * width + (width - x - 1))] < 0 ? data[4 * ((y + 1) * width + (width - x - 1))] + 256 : data[4 * ((y + 1) * width + (width - x - 1))];
		int g = data[4 * ((y + 1) * width + (width - x - 1)) + 1] < 0 ? data[4 * ((y + 1) * width + (width - x - 1)) + 1] + 256 : data[4 * ((y + 1) * width + (width - x - 1)) + 1];
		int b = data[4 * ((y + 1) * width + (width - x - 1)) + 2] < 0 ? data[4 * ((y + 1) * width + (width - x - 1)) + 2] + 256 : data[4 * ((y + 1) * width + (width - x - 1)) + 2];
		int a = data[4 * ((y + 1) * width + (width - x - 1)) + 3] < 0 ? data[4 * ((y + 1) * width + (width - x - 1)) + 3] + 256 : data[4 * ((y + 1) * width + (width - x - 1)) + 3];*/
		
		int r, g, b, a = 0;
		
		int pixelPos = ((y * width) + x) * 4;
		
		r = data[pixelPos] < 0 ? data[pixelPos] + 256 : data[pixelPos];
		g = data[pixelPos + 1] < 0 ? data[pixelPos + 1] + 256 : data[pixelPos + 1];
		b = data[pixelPos + 2] < 0 ? data[pixelPos + 2] + 256 : data[pixelPos + 2];
		a = data[pixelPos + 3] < 0 ? data[pixelPos + 3] + 256 : data[pixelPos + 3];
		
		return new Color(r, g, b, a);
	}
	
	public Item setTextureOffsets(int... offsets) {
		textureCoords = offsets.clone();
		
		pixels = new Color[itemMap.getWidth()][itemMap.getHeight()];
		
		byte[] data = itemMap.getTexture().getTextureData();
		
		for(int i = 0; i < data.length; i++) {
			if (data[i] < 0) data[i] += 256;
		}
		
		for(int x = 0; x < itemMap.getWidth(); x++) {
			for(int y = 0; y < itemMap.getHeight(); y++) {
				pixels[x][y] = getPixelAt(x, y, itemMap.getWidth(), data);
			}
		}
		
		return this;
	}
	
	public int[] textureCoords;
	
	public Item(int id) {
		this.setId((byte)id);
		items[id] = this;
		size++;
	}
	
	public void render() {
		/*glEnable(GL_BLEND);
		glBegin(GL_QUADS); {
			for(int i = 0; i < textureCoords.length; i += 2) {
				for(int x = 0; x < 16; x++) {
					for(int y = 0; y < 16; y++) {
						int texX = textureCoords[i] * 16 + x;
						int texY = textureCoords[i + 1] * 16 + y;
						glColor4f(pixels[texX][texY].r, pixels[texX][texY].g, pixels[texX][texY].b, pixels[texX][texY].a);
						//System.out.println(pixels[x][y].r + ", " + pixels[x][y].g + ", " + pixels[x][y].b + ", " + pixels[x][y].a);
						
						float iScaled = (float) i * 0.05F;
						
						// Front
						//0, 0, 0
						//0, 1, 0
						//1, 1, 0
						//1, 0, 0
						
						glVertex3f(x * 0.1F, y * 0.1F, iScaled);
						glVertex3f(x * 0.1F, (y + 1) * 0.1F, iScaled);
						glVertex3f((x + 1) * 0.1F, (y + 1) * 0.1F, iScaled);
						glVertex3f((x + 1) * 0.1F, y * 0.1F, iScaled);
						
						// Right
						//1, 0, 0
						//1, 1, 0
						//1, 1, 1
						//1, 0, 1
						
						glVertex3f((x + 1) * 0.1F, y * 0.1F, iScaled);
						glVertex3f((x + 1) * 0.1F, (y + 1) * 0.1F, iScaled);
						glVertex3f((x + 1) * 0.1F, (y + 1) * 0.1F, iScaled + 0.1F);
						glVertex3f((x + 1) * 0.1F, y * 0.1F, iScaled + 0.1F);
						
						// Back
						//1, 0, 1
						//1, 1, 1
						//0, 1, 1
						//0, 0, 1
						
						glVertex3f((x + 1) * 0.1F, y * 0.1F, iScaled + 0.1F);
						glVertex3f((x + 1) * 0.1F, (y + 1) * 0.1F, iScaled + 0.1F);
						glVertex3f(x * 0.1F, (y + 1) * 0.1F, iScaled + 0.1F);
						glVertex3f(x * 0.1F, y * 0.1F, iScaled + 0.1F);
						
						// Left
						//0, 0, 1
						//0, 1, 1
						//0, 1, 0
						//0, 0, 0
						
						glVertex3f(x * 0.1F, y * 0.1F, iScaled + 0.1F);
						glVertex3f(x * 0.1F, (y + 1) * 0.1F, iScaled + 0.1F);
						glVertex3f(x * 0.1F, (y + 1) * 0.1F, iScaled);
						glVertex3f(x * 0.1F, y * 0.1F, iScaled);
						
						// Top
						//0, 1, 0
						//0, 1, 1
						//1, 1, 1
						//1, 1, 0
						
						glVertex3f(x * 0.1F, (y + 1) * 0.1F, iScaled);
						glVertex3f(x * 0.1F, (y + 1) * 0.1F, iScaled + 0.1F);
						glVertex3f((x + 1) * 0.1F, (y + 1) * 0.1F, iScaled + 0.1F);
						glVertex3f((x + 1) * 0.1F, (y + 1) * 0.1F, iScaled);
						
						// Bottom
						//0, 0, 0
						//0, 0, 1
						//1, 0, 1
						//1, 0, 0
						
						glVertex3f(x * 0.1F, y * 0.1F, iScaled);
						glVertex3f(x * 0.1F, y * 0.1F, iScaled + 0.1F);
						glVertex3f((x + 1) * 0.1F, y * 0.1F, iScaled + 0.1F);
						glVertex3f((x + 1) * 0.1F, y * 0.1F, iScaled);
					}
				}
			}
		}
		glEnd();*/
		
		itemMap.bind();
		
		glTranslatef(0.0F, 1.0F, 0.0F);
		
		glBindBuffer(GL_ARRAY_BUFFER, renderValue);
        glVertexPointer(3, GL_FLOAT, 32, 0);
        
        glColorPointer(3, GL_FLOAT, 32, 12);
        
        glTexCoordPointer(2, GL_FLOAT, 32, 24);
        
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        
        glDrawArrays(GL_QUADS, 0, amountOfVertices);
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
	}
	
	int amountOfVertices = 0;
	
	public float[] averageColor(float[]... colors) {
		if(colors[0].length == 4) {
			float r, g, b, a;
			r = g = b = a = 0.0F;
			
			for(float[] c : colors) {
				r += c[0];
				g += c[1];
				b += c[2];
				a += c[3];
			}
			
			return new float[] {r, g, b, a};
		} else {
			float r, g, b;
			r = g = b = 0.0F;
			
			for(float[] c : colors) {
				r += c[0];
				g += c[1];
				b += c[2];
			}
			
			return new float[] {r, g, b};
		}
	}
	
	public void updateVBO() {
		amountOfVertices = 0;
		
		for(int i = 0; i < textureCoords.length; i += 2) {
			for(int x = 0; x < 16; x++) {
				for(int y = 0; y < 16; y++) {
					int texX = textureCoords[i] * 16 + x;
					int texY = textureCoords[i + 1] * 16 + y;
					int texXFront = i > 1 ? textureCoords[i - 2] * 16 + x : 0;
					int texYFront = i > 1 ? textureCoords[i - 1] * 16 + y : 0;
					int texXBack = i < textureCoords.length - 2 ? textureCoords[i + 2] * 16 + x : 0;
					int texYBack = i < textureCoords.length - 2 ? textureCoords[i + 3] * 16 + y : 0;
					
					if(pixels[texX][texY].a > 0.0F) {
						if(i == 0 || pixels[texXFront][texYFront].a < 1.0F) {amountOfVertices += 4;} // Front
						if(texX % 16 == 15 || pixels[texX + 1][texY].a < 1.0F) {amountOfVertices += 4;} // Right
						if(i == textureCoords.length - 1 || pixels[texXBack][texYBack].a < 1.0F) {amountOfVertices += 4;} // Back
						if(texX % 16 == 0 || pixels[texX - 1][texY].a < 1.0F) {amountOfVertices += 4;} // Left
						if(texY % 16 == 15 || pixels[texX][texY + 1].a < 1.0F) {amountOfVertices += 4;} // Top
						if(texY % 16 == 0 || pixels[texX][texY - 1].a < 1.0F) {amountOfVertices += 4;} // Bottom
					}
				}
			}
		}
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(amountOfVertices * 8); // Vertex = 3, colour = 3, texture = 2
		
		float voxelSize = 1F / 16F;
		
		for(int i = 0; i < textureCoords.length; i += 2) {
			for(int x = 0; x < 16; x++) {
				for(int y = 0; y < 16; y++) {
					int texX = textureCoords[i] * 16 + x;
					int texY = textureCoords[i + 1] * 16 + y;
					int texXFront = i > 1 ? textureCoords[i - 2] * 16 + x : 0;
					int texYFront = i > 1 ? textureCoords[i - 1] * 16 + y : 0;
					int texXBack = i < textureCoords.length - 2 ? textureCoords[i + 2] * 16 + x : 0;
					int texYBack = i < textureCoords.length - 2 ? textureCoords[i + 3] * 16 + y : 0;
					
					float iScaled = (float) i * voxelSize / 2;
					
					float[] color = new float[] {pixels[texX][texY].r, pixels[texX][texY].g, pixels[texX][texY].b/*, pixels[texX][texY].a*/};
					
//					float[][][] colors = new float[][][] {};
//					for(int xx = texX - 1; xx < texX + 1; xx++) {
//						for(int yy = texY - 1; yy < texY + 1; yy++) {
//							colors[xx][yy] = new float[] {pixels[xx][yy].r, pixels[xx][yy].g, pixels[xx][yy].b};
//						}
//					}
					
					//float[] colorDark = new float[] {color[0] - 0.1F, color[1] - 0.1F, color[2] - 0.1F};
					
					int textureIndex = GeneralTools.indexOf(subTextureIndices, getPixelAt(texX, texY + 16, 256, itemMap.getTexture().getTextureData()));
					float subTexX = (textureIndex * 8F) / 256F;
					float subTexY = 129F / 256F;
					
					float subTexSize = 8F / 256F;
					
					if(pixels[texX][texY].a > 0.0F) {
						if(i == 0 || pixels[texXFront][texYFront].a < 1.0F) {
							vertexData.put(new float[] {(x + 1) * voxelSize, y * -voxelSize, iScaled});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX, subTexY});
							
							vertexData.put(new float[] {(x + 1) * voxelSize, (y + 1) * -voxelSize, iScaled});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX, subTexY + subTexSize});
							
							vertexData.put(new float[] {x * voxelSize, (y + 1) * -voxelSize, iScaled});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX + subTexSize, subTexY + subTexSize});
							
							vertexData.put(new float[] {x * voxelSize, y * -voxelSize, iScaled});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX + subTexSize, subTexY});
						} // Front
						
						if(texX % 16 == 15 || pixels[texX + 1][texY].a < 1.0F) {
							vertexData.put(new float[] {(x + 1) * voxelSize, y * -voxelSize, iScaled + voxelSize});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX, subTexY});
							
							vertexData.put(new float[] {(x + 1) * voxelSize, (y + 1) * -voxelSize, iScaled + voxelSize});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX, subTexY + subTexSize});
							
							vertexData.put(new float[] {(x + 1) * voxelSize, (y + 1) * -voxelSize, iScaled});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX + subTexSize, subTexY + subTexSize});
							
							vertexData.put(new float[] {(x + 1) * voxelSize, y * -voxelSize, iScaled});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX + subTexSize, subTexY});
						} // Right
						
						if(i == textureCoords.length - 1 || pixels[texXBack][texYBack].a < 1.0F) {
							vertexData.put(new float[] {x * voxelSize, y * -voxelSize, iScaled + voxelSize});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX, subTexY});
							
							vertexData.put(new float[] {x * voxelSize, (y + 1) * -voxelSize, iScaled + voxelSize});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX, subTexY + subTexSize});
							
							vertexData.put(new float[] {(x + 1) * voxelSize, (y + 1) * -voxelSize, iScaled + voxelSize});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX + subTexSize, subTexY + subTexSize});
							
							vertexData.put(new float[] {(x + 1) * voxelSize, y * -voxelSize, iScaled + voxelSize});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX + subTexSize, subTexY});
						} // Back
						
						if(texX % 16 == 0 || pixels[texX - 1][texY].a < 1.0F) {
							vertexData.put(new float[] {x * voxelSize, y * -voxelSize, iScaled});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX, subTexY});
							
							vertexData.put(new float[] {x * voxelSize, (y + 1) * -voxelSize, iScaled});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX, subTexY + subTexSize});
							
							vertexData.put(new float[] {x * voxelSize, (y + 1) * -voxelSize, iScaled + voxelSize});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX + subTexSize, subTexY + subTexSize});
							
							vertexData.put(new float[] {x * voxelSize, y * -voxelSize, iScaled + voxelSize});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX + subTexSize, subTexY});
						} // Left
						
						if(texY % 16 == 15 || pixels[texX][texY + 1].a < 1.0F) {
							vertexData.put(new float[] {(x + 1) * voxelSize, (y + 1) * -voxelSize, iScaled});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX, subTexY});
							
							vertexData.put(new float[] {(x + 1) * voxelSize, (y + 1) * -voxelSize, iScaled + voxelSize});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX, subTexY + subTexSize});
							
							vertexData.put(new float[] {x * voxelSize, (y + 1) * -voxelSize, iScaled + voxelSize});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX + subTexSize, subTexY + subTexSize});
							
							vertexData.put(new float[] {x * voxelSize, (y + 1) * -voxelSize, iScaled});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX + subTexSize, subTexY});
						} // Top
						
						if(texY % 16 == 0 || pixels[texX][texY - 1].a < 1.0F) {
							vertexData.put(new float[] {x * voxelSize, y * -voxelSize, iScaled});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX, subTexY});
							
							vertexData.put(new float[] {x * voxelSize, y * -voxelSize, iScaled + voxelSize});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX, subTexY + subTexSize});
							
							vertexData.put(new float[] {(x + 1) * voxelSize, y * -voxelSize, iScaled + voxelSize});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX + subTexSize, subTexY + subTexSize});
							
							vertexData.put(new float[] {(x + 1) * voxelSize, y * -voxelSize, iScaled});
							vertexData.put(color);
							vertexData.put(new float[] {subTexX + subTexSize, subTexY});
						} // Bottom
					}
				}
			}
		}
		
		vertexData.flip();
		
		renderValue = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, renderValue);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	public Item(int id, String name) {
		this(id);
		this.setName(name);
	}
	
	public Item setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getName() {
		return this.name;
	}

	public Item setId(byte id) {
		this.id = id;
		return this;
	}
	
	public byte getId() {
		return this.id;
	}
	
	public static final float s = 0.0625f;
	
	public static final float o = 0.0001f;
	
	public static int countTruths(boolean... vars) {
	    int count = 0;
	    for (boolean var : vars) {
	        count += (var ? 1 : 0);
	    }
	    return count;
	}
}
