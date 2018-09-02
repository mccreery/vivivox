package tk.nukeduck.vivivox.world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.util.*;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.*;

import tk.nukeduck.vivivox.Fonts;
import tk.nukeduck.vivivox.VivivoxMain;
import tk.nukeduck.vivivox.block.*;
import tk.nukeduck.vivivox.entity.Entity;
import tk.nukeduck.vivivox.helper.*;
import tk.nukeduck.vivivox.world.generator.*;

public class World
{
	public int minX = 0, minY = 0, minZ = 0, maxX = 5, maxY = 5, maxZ = 5;

	public int worldSize = 256; // In blocks
	public int worldHeight = 100; // In blocks

	public Chunk[][][] chunks = new Chunk[(int) Math.ceil(worldSize / Chunk.chunkSize) + 1][(int) Math.ceil(worldHeight / Chunk.chunkSize) + 1][(int) Math.ceil(worldSize / Chunk.chunkSize) + 1];
	public int[][][] chunksRender = new int[(int) Math.ceil(worldSize / Chunk.chunkSize) + 1][(int) Math.ceil(worldHeight / Chunk.chunkSize) + 1][(int) Math.ceil(worldSize / Chunk.chunkSize) + 1];
	public int[][][] chunksRenderWater = new int[(int) Math.ceil(worldSize / Chunk.chunkSize) + 1][(int) Math.ceil(worldHeight / Chunk.chunkSize) + 1][(int) Math.ceil(worldSize / Chunk.chunkSize) + 1];

	/** Current entities in the world right now */
	public ArrayList<Entity> entities = new ArrayList<Entity>();

	// Different seeds used during world generation
	// And yes, these are just random numbers on the end there.
	protected int terrainSeed = VivivoxMain.seed;
	protected int underwaterGroundSeed = VivivoxMain.seed + 9274;
	protected int groundSeed = VivivoxMain.seed + 7362;
	protected int biomeSeed = VivivoxMain.seed + 9526;
	protected int treeSeed = VivivoxMain.seed + 5458;

	private static final String[] days = {
		"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
	};

	private static final String[] tips = {
		"Jumping helps you get in the air.",
		"You can sprint by holding shift.",
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
		"I wonder if OpenGL will do what I say this time.",
		"Gettin' stuff done.",
		"Boy oh boy do I love working with OpenGL."
	};

	private static long time = System.currentTimeMillis();
	private static long lastTime = time;

	public static Image vignetteOverlay;
	public static Image itemBar;

	public int calculateVariationFrom(int i, int x, int z) {
		int variation = (bV[getBiome(x, z)] +

				bV[getBiome(x - 1, z)] +
				bV[getBiome(x + 1, z)] +
				bV[getBiome(x, z - 1)] +
				bV[getBiome(x, z + 1)] +

				bV[getBiome(x - 1, z - 1)] +
				bV[getBiome(x - 1, z + 1)] +
				bV[getBiome(x + 1, z + 1)] +
				bV[getBiome(x + 1, z - 1)] +

				// Top
				bV[getBiome(x - 1, z + 2)] +
				bV[getBiome(x, z + 2)] +
				bV[getBiome(x + 1, z + 2)] +

				// Right
				bV[getBiome(x + 2, z + 1)] +
				bV[getBiome(x + 2, z)] +
				bV[getBiome(x + 2, z - 1)] +

				// Bottom
				bV[getBiome(x + 1, z - 2)] +
				bV[getBiome(x, z - 2)] +
				bV[getBiome(x - 1, z - 2)] +

				// Left
				bV[getBiome(x - 2, z - 1)] +
				bV[getBiome(x - 2, z)] +
				bV[getBiome(x - 2, z + 1)]);
		double var = variation / 210.0;

		int height = (biomeHeights[getBiome(x, z)] +

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
				biomeHeights[getBiome(x - 1, z - 2)] +

				// Left
				biomeHeights[getBiome(x - 2, z - 1)] +
				biomeHeights[getBiome(x - 2, z)] +
				biomeHeights[getBiome(x - 2, z + 1)]);
		double hei = height / 21.0;

		return (int) ((i * var) + hei);
	}

	static int[] biomeHeights = {45, 0};
	static int[] bV = {5, 10};

	public static Image button, cursor;

	public World() {
		try {
			vignetteOverlay = new Image("textures/vignette.png");
			itemBar = new Image("textures/itembar.png");
			itemBar.setFilter(Image.FILTER_NEAREST);
			button = new Image("textures/button.png");
			button.setFilter(Image.FILTER_NEAREST);
			cursor = new Image("textures/cursor.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}

		currentTip = tips[VivivoxMain.random.nextInt(tips.length)];

		int chunksAdded = 0;
		int maxChunksAdded = chunks.length;

		int relativeX = 0, relativeY = 0, relativeZ = 0;

		int chunkLength = chunks.length;
		int chunkHeight = chunks[0].length;

		for(int x = 0; x < chunkLength; x++) {
			for(int y = 0; y < chunkHeight; y++) {
				for(int z = 0; z < chunkLength; z++) {
					chunks[x][y][z] = new Chunk(relativeX, relativeY, relativeZ);
					relativeZ += Chunk.chunkSize;
				}
				relativeY += Chunk.chunkSize;
				relativeZ = 0;
			}
			relativeX += Chunk.chunkSize;
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
					if(y < height - 3) {
						setBlock(x, y, z, Block.stone);
					} else {
						setBlock(x, y, z, Block.dirt);
					}
				}

				setBlock(x, height - 1, z, Block.grass);

				int n = perlinNoise(((double)x + groundSeed) / 25, ((double)z + groundSeed) / 25, 1) / (5 / 3);
				if(n > 80) {
					setBlock(x, height - 1, z, Block.stone);
				}

				double xScaled = (double) x / 15D, zScaled = (double) z / 15D;
				for(int p = 0; p < 3; p++) {
					for(int y = 0; y < height; y++) {
						if(NoiseGenerator.GradientCoherentNoise3D(xScaled, y / 15D, zScaled, VivivoxMain.seed + (p * 10000000), NoiseGenerator.NoiseQuality.QUALITY_FAST) > 0.6F + ((float) p) * 0.1F) {
							setBlock(x, y, z, Block.air);
						}
					}
				}

				Block b = getBlock(x, height - 1, z);
				if(VivivoxMain.random.nextInt(30) == 0 && b != null && b.isOpaque()) {
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
				Block floorBlock = perlinNoise((double) x + underwaterGroundSeed / 5, (double) z + underwaterGroundSeed / 5, 1) > 60 ? Block.dirt : Block.sand;

				int height = calculateVariationFrom(perlinNoise(((double)x + terrainSeed) / 50, ((double)z + terrainSeed) / 50, 1) / (5 / 3), x, z);

				if(height < 55) setBlock(x, height - 1, z, floorBlock);

				for(int y = height / (5 / 3); y < 55; y++) {
					setBlock(x, y, z, Block.water);
				}

				if(VivivoxMain.random.nextInt(300) == 0 && getBlock(x, height - 1, z) == Block.grass && perlinNoise(((double)x + treeSeed) / 5, ((double)z + treeSeed) / 5, 1) < 60) {
					trees[VivivoxMain.random.nextInt(trees.length)].generate(this, x, height, z);
				}
			}
			columnsSet2++;
			updateWorldGenText("Filling oceans: " + (int)(((float)columnsSet2 / (float)maxColumnsSet2) * 100) + "%");
		}

		for(int x = 0; x < worldSize; x++) {
			for(int z = 0; z < worldSize; z++) {
				Block under = getBlock(x, 54, z), over = getBlock(x, 55, z);
				if(under != null && under.isOpaque() && under != Block.water && (over == null || !over.isOpaque())) {
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

		/*for(int x = 0; x < 100; x += 5) {
			for(int z = 0; z < 100; z += 5) {
				spawnEntity(new EntityKillyThingy(new Vector3f(x, 100.0F, z), new Vector3f()));
			}
		}*/
	}

	public void update() {
		for(int x = minX; x < maxX; x++) {
			for(int y = minY; y < maxY; y++) {
				for(int z = minZ; z < maxZ; z++) {
					this.chunks[x][y][z].updateBlocks();
				}
			}
		}
	}

	public void spawnEntity(Entity entity) {
		this.entities.add(entity);
	}

	// Types of tree
	public static final GeneratorTree[] trees = {
		new GeneratorTree(5, 5, 8, 3, 5, 2, 0.5F, 0.5F), // Small Oak
		new GeneratorTree(10, 10, 16, 6, 10, 2, 0.5F, 0.5F), // Big Oak
		new GeneratorTree(10, 10, 15, 4, 7, 1, 1.0F, 0.05F), // Weird Trees
		//new GeneratorTree(new Random().nextInt(15) + 5, new Random().nextInt(10) + 2, new Random().nextInt(20) + 1, new Random().nextInt(5) + 1, new Random().nextInt(10) + 1, new Random().nextInt(10) + 1, new Random().nextFloat(), new Random().nextFloat()) // Pot Luck
	};

	private static String currentTip;

	private static long currentTime, screenUpdateTime = time;

	public static void updateWorldGenText(String text) {
		currentTime = System.currentTimeMillis();

		// Calls after 500 seconds
		if(currentTime - time > 500000) {
			currentTip = "I don't have time for this. Tell the boss I quit.";
		}

		// Calls every 5 seconds
		if(currentTime - lastTime > 5000) {
			lastTime = currentTime;

			if(VivivoxMain.random.nextInt(10) == 0 && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 25 && Calendar.getInstance().get(Calendar.MONTH) == 11) currentTip = "Merry Christmas!";
			else currentTip = tips[VivivoxMain.random.nextInt(tips.length)];
		}

		// Calls every half second
		if(currentTime - screenUpdateTime > 500) {
			screenUpdateTime = currentTime;

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			VivivoxMain.logo.draw(Display.getWidth() / 2 - VivivoxMain.logo.getWidth() / 2, Display.getHeight() / 2 - VivivoxMain.logo.getHeight() / 2);

			FontTools.renderText((Display.getWidth() / 2) - (Fonts.hudFont.getWidth(text) / 2), (Display.getHeight() / 2) + 100, text, Color.white, Fonts.hudFont);

			FontTools.renderText((Display.getWidth() / 2) - (Fonts.hudFont.getWidth(currentTip) / 2), Fonts.hudFont.getHeight(), currentTip, Color.white, Fonts.hudFont);

			World.vignetteOverlay.draw(0, 0, Display.getWidth(), Display.getHeight(), 0, 0, 256, 256, new Color(1.0f, 1.0f, 1.0f, 0.5f));

			Display.update();

			if(Display.isCloseRequested()) {
				Display.destroy();
				System.exit(0);
			}
		}
	}

	public void setBlock(int x, int y, int z, Block block) {
		try {
			chunks[(int) Math.floor(x / Chunk.chunkSize)][(int) Math.floor(y / Chunk.chunkSize)][(int) Math.floor(z / Chunk.chunkSize)].setBlockAt(x % Chunk.chunkSize, y % Chunk.chunkSize, z % Chunk.chunkSize, block);
			if(VivivoxMain.hasStartedGame) {
				chunks[(int) Math.floor(x / Chunk.chunkSize)][(int) Math.floor(y / Chunk.chunkSize)][(int) Math.floor(z / Chunk.chunkSize)].updateVBO(false, this, (int) (x / Chunk.chunkSize), (int) (y / Chunk.chunkSize), (int) (z / Chunk.chunkSize));

				int xScaled = x / Chunk.chunkSize, yScaled = y / Chunk.chunkSize, zScaled = z / Chunk.chunkSize;

				int xRel = x % Chunk.chunkSize;
				if(xRel == 0 && xScaled > 1) chunks[(int) (xScaled) - 1][(int) (yScaled)][(int) (zScaled)].updateVBO(false, this, (int) (xScaled) - 1, (int) (yScaled), (int) (zScaled));
				else if(xRel == 15 && xScaled + 1 < chunks.length) chunks[(int) (xScaled) + 1][(int) (yScaled)][(int) (zScaled)].updateVBO(false, this, (int) (xScaled) + 1, (int) (yScaled), (int) (zScaled));

				int yRel = y % Chunk.chunkSize;
				if(yRel == 0 && yScaled > 1) chunks[(int) (xScaled)][(int) (yScaled) - 1][(int) (zScaled)].updateVBO(false, this, (int) (xScaled), (int) (yScaled) - 1, (int) (zScaled));
				if(yRel == 15 && yScaled + 1 < chunks.length) chunks[(int) (xScaled)][(int) (yScaled) + 1][(int) (zScaled)].updateVBO(false, this, (int) (xScaled), (int) (yScaled) + 1, (int) (zScaled));

				int zRel = z % Chunk.chunkSize;
				if(zRel == 0 && zScaled > 1) chunks[(int) (xScaled)][(int) (yScaled)][(int) (zScaled) - 1].updateVBO(false, this, (int) (xScaled), (int) (yScaled), (int) (zScaled) - 1);
				if(zRel == 15 && zScaled + 1 < chunks.length) chunks[(int) (xScaled)][(int) (yScaled)][(int) (zScaled) + 1].updateVBO(false, this, (int) (xScaled), (int) (yScaled), (int) (zScaled) + 1);
			}
		} catch(ArrayIndexOutOfBoundsException e) {}
	}

	public void setLightLevel(int x, int y, int z, byte light) {
		int xScaled = x / Chunk.chunkSize, yScaled = y / Chunk.chunkSize, zScaled = z / Chunk.chunkSize;
		chunks[x / Chunk.chunkSize][y / Chunk.chunkSize][z / Chunk.chunkSize].setLightLevelAt(x % Chunk.chunkSize, y % Chunk.chunkSize, z % Chunk.chunkSize, light);

		if(VivivoxMain.hasStartedGame) {
			chunks[xScaled][yScaled][zScaled].updateVBO(false, this, xScaled, yScaled, zScaled);

			int xRel = x % Chunk.chunkSize;
			if(xRel == 0 && xScaled > 1) chunks[xScaled - 1][yScaled][zScaled].updateVBO(false, this, xScaled - 1, yScaled, zScaled);
			else if(xRel == 15 && xScaled + 1 < chunks.length) chunks[xScaled + 1][yScaled][zScaled].updateVBO(false, this, xScaled + 1, yScaled, zScaled);

			int yRel = y % Chunk.chunkSize;
			if(yRel == 0 && yScaled > 1) chunks[xScaled][yScaled - 1][zScaled].updateVBO(false, this, xScaled, yScaled - 1, zScaled);
			if(yRel == 15 && yScaled + 1 < chunks.length) chunks[xScaled][yScaled + 1][zScaled].updateVBO(false, this, xScaled, yScaled + 1, zScaled);

			int zRel = z % Chunk.chunkSize;
			if(zRel == 0 && zScaled > 1) chunks[xScaled][yScaled][zScaled - 1].updateVBO(false, this, xScaled, yScaled, zScaled - 1);
			if(zRel == 15 && zScaled + 1 < chunks.length) chunks[xScaled][yScaled][zScaled + 1].updateVBO(false, this, xScaled, yScaled, zScaled + 1);
		}
	}

	public void setBiome(int x, int z, byte id) {
		chunks[x / Chunk.chunkSize][0][z / Chunk.chunkSize].setBiomeAt(x % Chunk.chunkSize, z % Chunk.chunkSize, id);
	}

	public Block getBlock(int x, int y, int z) {
		try {
			Block b = Block.blocks[chunks[x / Chunk.chunkSize][y / Chunk.chunkSize][z / Chunk.chunkSize].getBlockAt(x % Chunk.chunkSize, y % Chunk.chunkSize, z % Chunk.chunkSize)];
			return b != null ? b : Block.air;
		} catch(Exception e) {
			return Block.air;
		}
	}

	public byte getLightLevel(int x, int y, int z) {
		try {
			return chunks[x / Chunk.chunkSize][y / Chunk.chunkSize][z / Chunk.chunkSize].getLightLevelAt(x % Chunk.chunkSize, y % Chunk.chunkSize, z % Chunk.chunkSize);
		} catch(Exception e) {
			return 0;
		}
	}

	public byte getBiome(int x, int z) {
		try {
			return chunks[x / Chunk.chunkSize][0][z / Chunk.chunkSize].getBiomeAt(x % Chunk.chunkSize, z % Chunk.chunkSize);
		} catch(Exception e) {
			return 0;
		}
	}

	public Frustum viewFrustum = new Frustum();

	public void render(Vector3f position, float rotation, float rotationY) {
		viewFrustum.calculateFrustum();

		glPushMatrix(); {
			glEnableClientState(GL_VERTEX_ARRAY);
			glEnableClientState(GL_COLOR_ARRAY);
			glEnableClientState(GL_TEXTURE_COORD_ARRAY);

			glTranslatef(position.x, position.y, position.z);
			glRotatef(rotation, 0, 1, 0);
			glRotatef(rotationY, 0, 0, 1);

			Block.blockMap.bind();

			GL20.glUseProgram(VivivoxMain.shaderProgram);

			for(int worldX = -1; worldX < 2; worldX++) {
				for(int worldZ = -1; worldZ < 2; worldZ++) {
					glPushMatrix(); {
						glTranslatef(worldSize * worldX, 0.0F, worldSize * worldZ);

						for(int x = minX; x < maxX; x++) {
							for(int y = minY; y < maxY; y++) {
								for(int z = minZ; z < maxZ; z++) {
									if(viewFrustum.cubeInFrustum((x * Chunk.chunkSize) - (worldX * worldSize) + position.x, (y * Chunk.chunkSize) + position.y, (z * Chunk.chunkSize) - (worldZ * worldSize) + position.z, Chunk.chunkSize)) {
										glBindBuffer(GL_ARRAY_BUFFER, chunksRender[x][y][z]);

										glVertexPointer(Chunk.vertexSize, GL_FLOAT, 32, 0);
										glTexCoordPointer(Chunk.textureSize, GL_FLOAT, 32, 12);
										glColorPointer(Chunk.colorSize, GL_FLOAT, 32, 20);

										glDrawArrays(GL_TRIANGLES, 0, chunks[x][y][z].amountOfVertices);
									}
								}
							}
						}
					}
					glPopMatrix();
				}
			}

            /*GL20.glUseProgram(VivivoxMain.underwaterShaderProgram);
            glEnable(GL_BLEND);

            GL20.glUniform1f(GL20.glGetUniformLocation(VivivoxMain.underwaterShaderProgram, "waveOffset"), waveOffset);

			for(int worldX = -1; worldX < 2; worldX++) {
				for(int worldZ = -1; worldZ < 2; worldZ++) {
					glPushMatrix(); {
						glTranslatef(worldSize * worldX, 0.0F, worldSize * worldZ);

						for(int x = minX; x < maxX; x++) {
							for(int y = minY; y < maxY; y++) {
								for(int z = minZ; z < maxZ; z++) {
									if(viewFrustum.cubeInFrustum((x * 16) + (worldX * worldSize), (y * 16) + position.y, (z * 16) + (worldZ * worldSize), 16)) {
										glBindBuffer(GL_ARRAY_BUFFER, chunksRenderWater[x][y][z]);

							            glVertexPointer(Chunk.vertexSize, GL_FLOAT, 32, 0);
							            glTexCoordPointer(Chunk.textureSize, GL_FLOAT, 32, 12);
							            glColorPointer(Chunk.colorSize, GL_FLOAT, 32, 20);

							            //
							            glDrawArrays(GL_TRIANGLES, 0, chunks[x][y][z].amountOfWaterVertices);

							            //glBindBuffer(GL_ARRAY_BUFFER, 0);
							            //
									}
								}
							}
						}
					}
					glPopMatrix();
				}
			}*/

			glBindBuffer(GL_ARRAY_BUFFER, 0);
			GL20.glUseProgram(0);

            glDisableClientState(GL_TEXTURE_COORD_ARRAY);
            glDisableClientState(GL_COLOR_ARRAY);
            glDisableClientState(GL_VERTEX_ARRAY);

			for(int worldX = -1; worldX < 2; worldX++) {
				for(int worldZ = -1; worldZ < 2; worldZ++) {
					glDisable(GL_TEXTURE_2D);
					for(Entity e : entities) {
						if((e.getPosition().x + (worldX * worldSize)) / (float) Chunk.chunkSize >= minX && (e.getPosition().x + (worldX * worldSize)) / (float) Chunk.chunkSize < maxX
							&& e.getPosition().y / (float) Chunk.chunkSize >= minY && e.getPosition().y / (float) Chunk.chunkSize < maxY
							&& (e.getPosition().z + (worldZ * worldSize)) / (float) Chunk.chunkSize >= minZ && (e.getPosition().z + (worldZ * worldSize)) / (float) Chunk.chunkSize < maxZ
							&& viewFrustum.cubeInFrustum(
									e.getPosition().x + position.x + (worldX * worldSize),
									e.getPosition().y + position.y,
									e.getPosition().z + position.z + (worldZ * worldSize), 1)) {
							glPushMatrix(); {
								GL11.glTranslatef(e.getPosition().x, e.getPosition().y, e.getPosition().z);
								e.render(this);
							}
							glPopMatrix();
						}
					}
					glEnable(GL_TEXTURE_2D);
				}
			}
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
