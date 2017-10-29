package tk.nukeduck.vivivox.item;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import tk.nukeduck.vivivox.Main;

public class Item {
	protected byte id = 0;
	
	protected String name;
	
	public static Item[] items = new Item[256];
	public static int size = 0;
	
	public static Item sword;
	
	public static Image itemMap;
	
	public static void ItemIconRegister() {
		try {
			itemMap = new Image("src/textures/itemMap.png");
			itemMap.setFilter(Image.FILTER_NEAREST);
		} catch(Exception e) {
			System.err.println("Item icons could not be registered. This will not go well.");
		}
		sword = new Item(0).setTextureOffsets(0, 0, 1, 0, 0, 0);
	}
	
	public Color[][] pixels;
	
	public static Color getPixelAt(int x, int y, int width, byte[] data) {
		int height = (data.length / 4) / width;
		
		int r = data[4 * ((height - y - 1) * width + (width - x - 1))] < 0 ? data[4 * ((height - y - 1) * width + (width - x - 1))] + 256 : data[4 * ((height - y - 1) * width + (width - x - 1))];
		int g = data[4 * ((height - y - 1) * width + (width - x - 1)) + 1] < 0 ? data[4 * ((height - y - 1) * width + (width - x - 1)) + 1] + 256 : data[4 * ((height - y - 1) * width + (width - x - 1)) + 1];
		int b = data[4 * ((height - y - 1) * width + (width - x - 1)) + 2] < 0 ? data[4 * ((height - y - 1) * width + (width - x - 1)) + 2] + 256 : data[4 * ((height - y - 1) * width + (width - x - 1)) + 2];
		int a = data[4 * ((height - y - 1) * width + (width - x - 1)) + 3] < 0 ? data[4 * ((height - y - 1) * width + (width - x - 1)) + 3] + 256 : data[4 * ((height - y - 1) * width + (width - x - 1)) + 3];
		
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
		glPushMatrix(); {
			glTranslatef(Main.world.worldSize / 2F, 150.0F, Main.world.worldSize / 2F);
			itemMap.bind();
			glBegin(GL_QUADS); {
				for(int i = 0; i < textureCoords.length; i += 2) {
					for(int x = textureCoords[i] * 16; x < (textureCoords[i] + 1) * 16; x++) {
						for(int y = textureCoords[i + 1] * 16; y < (textureCoords[i + 1] + 1) * 16; y++) {
							glColor4f(pixels[x][y].r, pixels[x][y].g, pixels[x][y].b, pixels[x][y].a);
							glVertex3f(x * 0.1F, y * 0.1F, 0.0F);
							glVertex3f(x * 0.1F, (y + 1) * 0.1F, 0.0F);
							glVertex3f((x + 1) * 0.1F, (y + 1) * 0.1F, 0.0F);
							glVertex3f((x + 1) * 0.1F, y * 0.1F, 0.0F);
						}
					}
				}
			}
			glEnd();
			}
		glPopMatrix();
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
