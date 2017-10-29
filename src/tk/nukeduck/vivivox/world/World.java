package tk.nukeduck.vivivox.world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.util.*;

import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.*;

import tk.nukeduck.vivivox.Fonts;
import tk.nukeduck.vivivox.Main;
import tk.nukeduck.vivivox.block.*;
import tk.nukeduck.vivivox.helper.*;
import tk.nukeduck.vivivox.world.generator.*;

public class World
{
	public int minX = 0, minY = 0, minZ = 0, maxX = 5, maxY = 5, maxZ = 5;
	
	public static int worldSize = 150;
	public static int worldHeight = 128;
	
	public Chunk[][][] chunks = new Chunk[(int) Math.ceil(worldSize / 16) + 1][(int) Math.ceil(worldHeight / 16) + 1][(int) Math.ceil(worldSize / 16) + 1];
	public int[][][] chunksRender = new int[(int) Math.ceil(worldSize / 16) + 1][(int) Math.ceil(worldHeight / 16) + 1][(int) Math.ceil(worldSize / 16) + 1];
	public int[][][] chunksRenderWater = new int[(int) Math.ceil(worldSize / 16) + 1][(int) Math.ceil(worldHeight / 16) + 1][(int) Math.ceil(worldSize / 16) + 1];
	
	/** Current entities in the world right now */
	//public ArrayList<Entity> entities = new ArrayList<Entity>();
	
	// Different seeds used during world generation
	// And yes, these are just random numbers on the end there.
	protected int terrainSeed = Main.seed;
	protected int underwaterGroundSeed = Main.seed + 9274;
	protected int groundSeed = Main.seed + 7362;
	protected int biomeSeed = Main.seed + 9526;
	protected int treeSeed = Main.seed + 5458;
	
	private static final String[] days = {
		"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
	};
	
	private static final String[] tips = {
		"Jumping helps you get in the air.", 
		"You can sprint by holding shift.", 
		"Vivivox comes from the Latin \"vive\" and English \"voxel\", so it literally means voxel life.", 
		"It took a while to write that tree generator.", 
		"Don't tell anybody, but you can glitch through ceilings.", 
		"Most of the time, the tree generator works 100% of the time.", 
		"This is just a random tip that doesn't tell you anything.", 
		"Graphical errors galore!", 
		"Wow, this is taking a long time.", 
		"Please stay on the line, your call is very important to us.", 
		"Hello, World!", 
		"I think it might be broken. It's taking too long to do that...", 
		"Congratulations! You're the 9999th player! Collect your iPad " + (new Random().nextInt(20) + 3) + " now!", 
		"!aciremA ot htaeD", 
		"Yvan eht Nioj!", 
		"It's 'Image-err'.", 
		"Bananas are a great source of potassium!", 
		"Happy " + days[Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1] + "!", 
		"Do you have the time?", 
		"This is taking too long.", 
		"I'm getting impatient.", 
		"Are we there yet?", 
		"So, how's the family?", 
		"*sigh*", 
		"This never used to take this long.", 
		"0 to the power of 0 = ?", 
		"if(!Attempt.getInstance().succeed()) {Motivation.tryAgain();}", 
		"Patch notes: 1 problem fixed, 100 added.", 
		"Indie developing isn't as fun as you might think.", 
		"[Insert popular culture reference here]", 
		"JAVA4EVA", 
		"I'll make it faster, I promise.", 
		"Wait for it...", 
		"Sorry. I really am.", 
		"Caves, son!", 
		"Generation is hard...", 
		"Goats! Goats! Goats!", 
		"Long live the King!", 
		"Step 1: Never tell your parents.", 
		"Step 2: NEVER TELL YOUR PARENTS!", 
		"I wonder if OpenGL will do what I say this time."
	};
	
	private static long time = System.currentTimeMillis();
	private static long lastTime = time;
	
	public static Image vignetteOverlay;
	public static Image itemBar;
	
	public int calculateVariationFrom(int i, int x, int z) {
		float variation = biomeVariation[getBiome(x, z)] + 
				biomeVariation[getBiome(x - 1, z)] + 
				biomeVariation[getBiome(x + 1, z)] + 
				biomeVariation[getBiome(x, z - 1)] + 
				biomeVariation[getBiome(x, z + 1)] + 
				
				biomeVariation[getBiome(x - 1, z - 1)] +
				biomeVariation[getBiome(x - 1, z + 1)] +
				biomeVariation[getBiome(x + 1, z + 1)] +
				biomeVariation[getBiome(x + 1, z - 1)] +
				
				// Top
				biomeVariation[getBiome(x - 1, z + 2)] +
				biomeVariation[getBiome(x, z + 2)] +
				biomeVariation[getBiome(x + 1, z + 2)] +
				
				// Right
				biomeVariation[getBiome(x + 2, z + 1)] +
				biomeVariation[getBiome(x + 2, z + 0)] +
				biomeVariation[getBiome(x + 2, z - 1)] +
				
				// Bottom
				biomeVariation[getBiome(x + 1, z - 2)] +
				biomeVariation[getBiome(x, z - 2)] +
				biomeVariation[getBiome(x - 1, z -2)] +
				
				// Left
				biomeVariation[getBiome(x - 2, z - 1)] +
				biomeVariation[getBiome(x - 2, z)] +
				biomeVariation[getBiome(x -2, z + 1)];
		variation /= 21.0F;
		
		float height = biomeHeights[getBiome(x, z)] + 
				biomeHeights[getBiome(x - 1, z)] + 
				biomeHeights[getBiome(x + 1, z)] + 
				biomeHeights[getBiome(x, z - 1)] + 
				biomeHeights[getBiome(x, z + 1)] + 
				
				biomeHeights[getBiome(x - 1, z - 1)] +
				biomeHeights[getBiome(x - 1, z + 1)] +
				biomeHeights[getBiome(x + 1, z + 1)] +
				biomeHeights[getBiome(x + 1, z - 1)] +
				
				// Top
				biomeHeights[getBiome(x - 1, z + 2)] +
				biomeHeights[getBiome(x, z + 2)] +
				biomeHeights[getBiome(x + 1, z + 2)] +
				
				// Right
				biomeHeights[getBiome(x + 2, z + 1)] +
				biomeHeights[getBiome(x + 2, z + 0)] +
				biomeHeights[getBiome(x + 2, z - 1)] +
				
				// Bottom
				biomeHeights[getBiome(x + 1, z - 2)] +
				biomeHeights[getBiome(x, z - 2)] +
				biomeHeights[getBiome(x - 1, z -2)] +
				
				// Left
				biomeHeights[getBiome(x - 2, z - 1)] +
				biomeHeights[getBiome(x - 2, z)] +
				biomeHeights[getBiome(x -2, z + 1)];
		height /= 21.0F;
		
		return (int) (((float) i * variation) + height);
	}
	
	static int[] biomeHeights = {45, 0};
	static float[] biomeVariation = {0.4F, 1.0F};
	
	public World() {
		try {
			vignetteOverlay = new Image("src/textures/vignette.png");
			itemBar = new Image("src/textures/itembar.png");
			itemBar.setFilter(Image.FILTER_NEAREST);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		currentTip = tips[Main.random.nextInt(tips.length)];
		
		int chunksAdded = 0;
		int maxChunksAdded = chunks.length;
		
		int relativeX = 0, relativeY = 0, relativeZ = 0;
		
		int chunkLength = chunks.length;
		int chunkHeight = chunks[0].length;
		
		for(int x = 0; x < chunkLength; x++) {
			for(int y = 0; y < chunkHeight; y++) {
				for(int z = 0; z < chunkLength; z++) {
					chunks[x][y][z] = new Chunk(relativeX, relativeY, relativeZ);
					relativeZ += 16;
				}
				relativeY += 16;
				relativeZ = 0;
			}
			relativeX += 16;
			relativeY = 0;
			relativeZ = 0;
			
			chunksAdded++;
			
			updateWorldGenText("Cleaning slate: " + (int)(((float)chunksAdded / (float)maxChunksAdded) * 100) + "%");
		}
		
		int columnsSet = 0;
		int maxColumnsSet = worldSize;
		
		for(int x7 = 0; x7 < worldSize; x7++) {
			for(int z7 = 0; z7 < worldSize; z7++) {
				int n2 = perlinNoise(((double)x7 + biomeSeed) / 25, ((double)z7 + biomeSeed) / 25, 1) / (5 / 3);
				setBiome(x7, z7, n2 > 60 ? (byte) 0 : (byte) 1);
			}
		}
		
		for(int x = 0; x < worldSize; x++) {
			for(int z = 0; z < worldSize; z++) {
				int height = calculateVariationFrom(perlinNoise(((double)x + terrainSeed) / 50, ((double)z + terrainSeed) / 50, 1) / (5 / 3), x, z);
				
				for(int y = 0; y < height - 1; y++) {
					setBlock(x, y, z, Block.dirt);
					if(y < height - 3) {
						setBlock(x, y, z, Block.stone);
					}
				}
				
				setBlock(x, height - 1, z, Block.grass);
				
				int n = perlinNoise(((double)x + groundSeed) / 25, ((double)z + groundSeed) / 25, 1) / (5 / 3);
				if(n > 80) {
					setBlock(x, height - 1, z, Block.stone);
				}
				
				for(int y = 0; y < height; y++) {
					if(NoiseGenerator.GradientCoherentNoise3D(x / 10D, y / 10D, z / 10D, Main.seed, NoiseGenerator.NoiseQuality.QUALITY_FAST) > 0.5) {
						setBlock(x, y, z, Block.air);
					}
				}
				
				if(Main.random.nextInt(30) == 0 && getBlock(x, height - 1, z) != null && getBlock(x, height - 1, z).isOpaque()) {
					setBlock(x, height, z, Block.lamp);
				}
			}
			columnsSet++;
			updateWorldGenText("Scaping land: " + (int)(((float)columnsSet / (float)maxColumnsSet) * 100) + "%");
		}
		
		int columnsSet2 = 0;
		int maxColumnsSet2 = worldSize;
		
		for(int x = 0; x < worldSize; x++) {
			for(int z = 0; z < worldSize; z++) {
				Block floorBlock = perlinNoise(((double)x + underwaterGroundSeed) / 5, ((double)z + underwaterGroundSeed) / 5, 1) > 60 ? Block.dirt : Block.sand;
				
				int height = calculateVariationFrom(perlinNoise(((double)x + terrainSeed) / 50, ((double)z + terrainSeed) / 50, 1) / (5 / 3), x, z);
				
				if(height < 55) setBlock(x, height - 1, z, floorBlock);
				
				for(int y = height / (5 / 3); y < 55; y++) {
					setBlock(x, y, z, Block.water);
				}
				
				if(Main.random.nextInt(300) == 0 && getBlock(x, height - 1, z) == Block.grass && perlinNoise(((double)x + treeSeed) / 5, ((double)z + treeSeed) / 5, 1) < 60) {
					trees[Main.random.nextInt(trees.length)].generate(x, height, z, this);
				}
			}
			columnsSet2++;
			updateWorldGenText("Filling oceans: " + (int)(((float)columnsSet2 / (float)maxColumnsSet2) * 100) + "%");
		}
		
		for(int x = 0; x < worldSize; x++) {
			for(int z = 0; z < worldSize; z++) {
				if(getBlock(x, 54, z).isOpaque() && getBlock(x, 54, z) != Block.water && (getBlock(x, 55, z) == null || !getBlock(x, 55, z).isOpaque())) {
					setBlock(x, 54, z, Block.sand);
				}
			}
		}
		
		updateLight();
		
		int chunksDrawn = 0;
		int maxChunksDrawn = chunks.length * chunks[0].length;
		
		for(int x = 0; x < chunks.length; x++) {
			for(int y = 0; y < chunks[0].length; y++) {
				for(int z = 0; z < chunks[0][0].length; z++) {
					chunks[x][y][z].updateVBO(false, this, x, y, z);
				}
				chunksDrawn++;
				updateWorldGenText("Painting Canvas: " + (int)(((float)chunksDrawn / (float)maxChunksDrawn) * 100) + "%");
			}
		}
		System.out.println("What a nan");
	}
	
	/*public void spawnEntity(Entity entity) {
		this.entities.add(entity);
	}*/
	
	// Types of tree
	public static final GeneratorTree[] trees = {
		new GeneratorTree(5, 5, 8, 3, 5, 2, 0.5F, 0.5F), // Small Oak
		new GeneratorTree(10, 10, 16, 6, 10, 2, 0.5F, 0.5F), // Big Oak
		new GeneratorTree(10, 10, 15, 4, 7, 1, 1.0F, 0.05F), // Weird Trees
		//new GeneratorTree(new Random().nextInt(15) + 5, new Random().nextInt(10) + 2, new Random().nextInt(20) + 1, new Random().nextInt(5) + 1, new Random().nextInt(10) + 1, new Random().nextInt(10) + 1, new Random().nextFloat(), new Random().nextFloat()) // Pot Luck
	};
	
	private static String currentTip;
	
	public static void updateWorldGenText(String text) {
		/*Main.make2D();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		GLU.gluOrtho2D(0, Display.getWidth(), Display.getHeight(), 0);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_COLOR_MATERIAL);
		glDisable(GL11.GL_LIGHTING);
		
		glPushMatrix();
		glLoadIdentity();
		
		glDisable(GL_DEPTH_TEST);
		
		Main.logo.draw(Display.getWidth() / 2 - Main.logo.getWidth() / 2, Display.getHeight() / 2 - Main.logo.getHeight() / 2);
		
		FontTools.renderText((Display.getWidth() / 2) - (Fonts.hudFont.getWidth(text) / 2), (Display.getHeight() / 2) + 100, text, Color.white, Fonts.hudFont);
		
		FontTools.renderText((Display.getWidth() / 2) - (Fonts.hudFont.getWidth(currentTip) / 2), Fonts.hudFont.getHeight(), currentTip, Color.white, Fonts.hudFont);
		
		if(System.currentTimeMillis() > time + 1000000) {
			currentTip = "I don't have time for this. Tell the boss I quit.";
		} else if(System.currentTimeMillis() > lastTime + 5000) {
			lastTime = System.currentTimeMillis();
			
			if(Main.random.nextInt(10) == 0 && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 25 && Calendar.getInstance().get(Calendar.MONTH) == 11) currentTip = "Merry Christmas!";
			else currentTip = tips[Main.random.nextInt(tips.length)];
		}
		
		World.vignetteOverlay.draw(0, 0, Display.getWidth(), Display.getHeight(), 0, 0, 256, 256, new Color(1.0f, 1.0f, 1.0f, 0.5f));
		
		Display.update();
		
		if(Display.isCloseRequested()) {
			Display.destroy();
			System.exit(0);
		}
		Main.make3D();*/
	}
	
	public void setBlock(int x, int y, int z, Block block) {
		try {
		chunks[(int) Math.floor(x / 16)][(int) Math.floor(y / 16)][(int) Math.floor(z / 16)].setBlockAt(x % 16, y % 16, z % 16, block);
		if(Main.hasStartedGame) {
			chunks[(int) Math.floor(x / 16)][(int) Math.floor(y / 16)][(int) Math.floor(z / 16)].updateVBO(false, this, (int) (x / 16), (int) (y / 16), (int) (z / 16));
			
			if(x % 16 == 0 && (int) Math.floor(x / 16) - 1 > 0) chunks[(int) Math.floor(x / 16) - 1][(int) Math.floor(y / 16)][(int) Math.floor(z / 16)].updateVBO(false, this, (int) (x / 16) - 1, (int) (y / 16), (int) (z / 16));
			if(x % 16 == 15 && (int) Math.floor(x / 16) + 1 < chunks.length) chunks[(int) Math.floor(x / 16) + 1][(int) Math.floor(y / 16)][(int) Math.floor(z / 16)].updateVBO(false, this, (int) (x / 16) + 1, (int) (y / 16), (int) (z / 16));
			
			if(y % 16 == 0 && (int) Math.floor(y / 16) - 1 > 0) chunks[(int) Math.floor(x / 16)][(int) Math.floor(y / 16) - 1][(int) Math.floor(z / 16)].updateVBO(false, this, (int) (x / 16), (int) (y / 16) - 1, (int) (z / 16));
			if(y % 16 == 15 && (int) Math.floor(y / 16) + 1 < chunks.length) chunks[(int) Math.floor(x / 16)][(int) Math.floor(y / 16) + 1][(int) Math.floor(z / 16)].updateVBO(false, this, (int) (x / 16), (int) (y / 16) + 1, (int) (z / 16));
			
			if(z % 16 == 0 && (int) Math.floor(z / 16) - 1 > 0) chunks[(int) Math.floor(x / 16)][(int) Math.floor(y / 16)][(int) Math.floor(z / 16) - 1].updateVBO(false, this, (int) (x / 16), (int) (y / 16), (int) (z / 16) - 1);
			if(z % 16 == 15 && (int) Math.floor(z / 16) + 1 < chunks.length) chunks[(int) Math.floor(x / 16)][(int) Math.floor(y / 16)][(int) Math.floor(z / 16) + 1].updateVBO(false, this, (int) (x / 16), (int) (y / 16), (int) (z / 16) + 1);
		}
		} catch(ArrayIndexOutOfBoundsException e) {
			
		}
	}
	
	public void setLightLevel(int x, int y, int z, byte light) {
		chunks[(int) Math.floor(x / 16)][(int) Math.floor(y / 16)][(int) Math.floor(z / 16)].setLightLevelAt(x % 16, y % 16, z % 16, light);
		if(Main.hasStartedGame) {
			chunks[(int) Math.floor(x / 16)][(int) Math.floor(y / 16)][(int) Math.floor(z / 16)].updateVBO(false, this, (int) (x / 16), (int) (y / 16), (int) (z / 16));
			
			if(x % 16 == 0 && (int) Math.floor(x / 16) - 1 > 0) chunks[(int) Math.floor(x / 16) - 1][(int) Math.floor(y / 16)][(int) Math.floor(z / 16)].updateVBO(false, this, (int) (x / 16) - 1, (int) (y / 16), (int) (z / 16));
			if(x % 16 == 15 && (int) Math.floor(x / 16) + 1 < chunks.length) chunks[(int) Math.floor(x / 16) + 1][(int) Math.floor(y / 16)][(int) Math.floor(z / 16)].updateVBO(false, this, (int) (x / 16) + 1, (int) (y / 16), (int) (z / 16));
			
			if(y % 16 == 0 && (int) Math.floor(y / 16) - 1 > 0) chunks[(int) Math.floor(x / 16)][(int) Math.floor(y / 16) - 1][(int) Math.floor(z / 16)].updateVBO(false, this, (int) (x / 16), (int) (y / 16) - 1, (int) (z / 16));
			if(y % 16 == 15 && (int) Math.floor(y / 16) + 1 < chunks.length) chunks[(int) Math.floor(x / 16)][(int) Math.floor(y / 16) + 1][(int) Math.floor(z / 16)].updateVBO(false, this, (int) (x / 16), (int) (y / 16) + 1, (int) (z / 16));
			
			if(z % 16 == 0 && (int) Math.floor(z / 16) - 1 > 0) chunks[(int) Math.floor(x / 16)][(int) Math.floor(y / 16)][(int) Math.floor(z / 16) - 1].updateVBO(false, this, (int) (x / 16), (int) (y / 16), (int) (z / 16) - 1);
			if(z % 16 == 15 && (int) Math.floor(z / 16) + 1 < chunks.length) chunks[(int) Math.floor(x / 16)][(int) Math.floor(y / 16)][(int) Math.floor(z / 16) + 1].updateVBO(false, this, (int) (x / 16), (int) (y / 16), (int) (z / 16) + 1);
		}
	}
	
	public void setBiome(int x, int z, byte id) {
		chunks[(int) Math.floor(x / 16)][0][(int) Math.floor(z / 16)].setBiomeAt(x % 16, z % 16, id);
	}
	
	public Block getBlock(int x, int y, int z) {
		try {
			return Block.blocks[chunks[(int)x / 16][(int)y / 16][(int)z / 16].getBlockAt(x % 16, y % 16, z % 16)];
		} catch(Exception e) {
			return null;
		}
	}
	
	public byte getLightLevel(int x, int y, int z) {
		try {
			return chunks[(int)x / 16][(int)y / 16][(int)z / 16].getLightLevelAt(x % 16, y % 16, z % 16);
		} catch(Exception e) {
			return 0;
		}
	}
	
	public byte getBiome(int x, int z) {
		try {
			return chunks[(int)x / 16][0][(int)z / 16].getBiomeAt(x % 16, z % 16);
		} catch(Exception e) {
			return 0;
		}
	}
	
	public Frustum viewFrustum = new Frustum();
	
	public void render(Vector3f position, float rotation, float rotationY) {
		viewFrustum.calculateFrustum();
		glPushMatrix(); {
			glTranslatef(position.x, position.y, position.z);
			glRotatef(rotation, 0, 1, 0);
			glRotatef(rotationY, 0, 0, 1);
			
			Block.blockMap.bind();
			
			for(int x = minX; x < maxX; x++) {
				for(int y = minY; y < maxY; y++) {
					for(int z = minZ; z < maxZ; z++) {
						if(viewFrustum.cubeInFrustum(x * 16, y * 16, z * 16, 16)) {
							glBindBuffer(GL_ARRAY_BUFFER, chunksRender[x][y][z]);
				            glVertexPointer(Chunk.vertexSize, GL_FLOAT, 32, 0);
				            
				            glTexCoordPointer(Chunk.textureSize, GL_FLOAT, 32, 12);
				            
				            glColorPointer(Chunk.colorSize, GL_FLOAT, 32, 20);
				            
				            glEnableClientState(GL_VERTEX_ARRAY);
				            glEnableClientState(GL_COLOR_ARRAY);
				            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
				            
				            glDrawArrays(GL_TRIANGLES, 0, chunks[x][y][z].amountOfVertices);
				            
				            glBindBuffer(GL_ARRAY_BUFFER, 0);
				            
				            glDisableClientState(GL_TEXTURE_COORD_ARRAY);
				            glDisableClientState(GL_COLOR_ARRAY);
				            glDisableClientState(GL_VERTEX_ARRAY);
						}
					}
				}
			}
			
            //GL20.glUseProgram(Main.underwaterShaderProgram);
            
            //GL20.glUniform1f(GL20.glGetUniformLocation(Main.underwaterShaderProgram, "waveOffset"), waveOffset);
            
			for(int x = minX; x < maxX; x++) {
				for(int y = minY; y < maxY; y++) {
					for(int z = minZ; z < maxZ; z++) {
						if(viewFrustum.cubeInFrustum(x * 16, y * 16, z * 16, 16)) {
							glBindBuffer(GL_ARRAY_BUFFER, chunksRenderWater[x][y][z]);
				            glVertexPointer(Chunk.vertexSize, GL_FLOAT, 32, 0);
				            
				            glTexCoordPointer(Chunk.textureSize, GL_FLOAT, 32, 12);
				            
				            glColorPointer(Chunk.colorSize, GL_FLOAT, 32, 20);
				            
				            glEnableClientState(GL_VERTEX_ARRAY);
				            glEnableClientState(GL_COLOR_ARRAY);
				            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
				            
				            glDrawArrays(GL_TRIANGLES, 0, chunks[x][y][z].amountOfWaterVertices);
				            
				            glBindBuffer(GL_ARRAY_BUFFER, 0);
				            
				            glDisableClientState(GL_TEXTURE_COORD_ARRAY);
				            glDisableClientState(GL_COLOR_ARRAY);
				            glDisableClientState(GL_VERTEX_ARRAY);
						}
					}
				}
			}
			
			//GL20.glUseProgram(Main.shaderProgram);
		}
		glPopMatrix();
	}
	
	public void updateLight() {
		int blocksProcessed = 0;
		int maxBlocksProcessed = worldSize;
		
		for(int x = 1; x < worldSize; x++) {
			for(int y = 1; y < worldSize; y++) {
				for(int z = 1; z < worldSize; z++) {
					if(getBlock(x, y, z) instanceof BlockLight) {
						((BlockLight) getBlock(x, y, z)).processLight(x, y, z, this);
					}
				}
			}
			blocksProcessed++;
			updateWorldGenText("Lighting the world: " + (int)(((float)blocksProcessed / (float)maxBlocksProcessed) * 100) + "%");
		}
	}
	
	public static byte max(byte... vals) {
		byte max = Byte.MIN_VALUE;
		
		for (byte n : vals) {
			if (n > max) max = n;
		}
		return max;
	}
	
	public float waveOffset = 0;
	
	public static int perlinNoise(double x, double y, int nbOctave) {
		int result = 0;
		
		int sx = (int)(x * 256);
		int sy = (int)(y * 256);
		
		int octave = nbOctave;
		
		while(octave != 0) {
			int bX = sx & 0xFF;
			int bY = sy & 0xFF;
			
			int sxp = sx >> 8;
			int syp = sy >> 8;
			
			// Compute noise for each corner of current cell
			int yCorner00 = syp * 1376312589;
			int yCorner01 = yCorner00 + 1376312589;
			
			int xyCorner00 = sxp + yCorner00;
			int xyCorner10 = xyCorner00 + 1;
			int xyCorner01 = sxp + yCorner01;
			int xyCorner11 = xyCorner01 + 1;
			
			int xyBase00 = (xyCorner00 << 13) ^ xyCorner00;
			int xyBase10 = (xyCorner10 << 13) ^ xyCorner10;
			int xyBase01 = (xyCorner01 << 13) ^ xyCorner01;
			int xyBase11 = (xyCorner11 << 13) ^ xyCorner11;
			
			int alt1 = xyBase00 * (xyBase00 * xyBase00 * 15731 + 789221) + 1376312589;
			int alt2 = xyBase10 * (xyBase10 * xyBase10 * 15731 + 789221) + 1376312589;
			int alt3 = xyBase01 * (xyBase01 * xyBase01 * 15731 + 789221) + 1376312589;
			int alt4 = xyBase11 * (xyBase11 * xyBase11 * 15731 + 789221) + 1376312589;
			
			// True gradient noise
			int grad1X = (alt1 & 0xFF) - 128;
			int grad1Y = ((alt1 >> 8) & 0xFF) - 128;
			
			int grad2X = (alt2 & 0xFF) - 128;
			int grad2Y = ((alt2 >> 8) & 0xFF) - 128;
			
			int grad3X = (alt3 & 0xFF) - 128;
			int grad3Y = ((alt3 >> 8) & 0xFF) - 128;
			
			int grad4X = (alt4 & 0xFF) - 128;
			int grad4Y = ((alt4 >> 8) & 0xFF) - 128;
			
			int sX1 = bX >> 1;
			int sY1 = bY >> 1;
			int sX2 = 128 - sX1;
			int sY2 = sY1;
			int sX3 = sX1;
			int sY3 = 128 - sY1;
			int sX4 = 128 - sX1;
			int sY4 = 128 - sY1;
			
			alt1 = (grad1X * sX1 + grad1Y * sY1) + 16384 + ((alt1 & 0xFF0000) >> 9); // To avoid seams to be 0 we use an offset
			alt2 = (grad2X * sX2 + grad2Y * sY2) + 16384 + ((alt2 & 0xFF0000) >> 9);
			alt3 = (grad3X * sX3 + grad3Y * sY3) + 16384 + ((alt3 & 0xFF0000) >> 9);
			alt4 = (grad4X * sX4 + grad4Y * sY4) + 16384 + ((alt4 & 0xFF0000) >> 9);
			
			// Value noise
			alt1 &= 0xFFFF;
			alt2 &= 0xFFFF;
			alt3 &= 0xFFFF;
			alt4 &= 0xFFFF;
			
			//Bilinear interpolation
			int f24 = (bX * bY) >> 8;
			int f23 = bX - f24;
			int f14 = bY - f24;
			int f13 = 256 - f14 - f23 - f24;
			
			int val = (alt1 * f13 + alt2 * f23 + alt3 * f14 + alt4 * f24);
			
			// Accumulate in result
			result += val << octave;
			
			octave--;
			
			sx <<= 1;
			sy <<= 1;
		}
		
		return (result >>> (17 + nbOctave));
	}
}