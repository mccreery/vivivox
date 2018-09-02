package tk.nukeduck.vivivox;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import tk.nukeduck.vivivox.block.Block;
import tk.nukeduck.vivivox.entity.Entity;
import tk.nukeduck.vivivox.gui.Gui;
import tk.nukeduck.vivivox.gui.GuiElementButton;
import tk.nukeduck.vivivox.helper.Render;
import tk.nukeduck.vivivox.helper.ShaderProgram;
import tk.nukeduck.vivivox.item.Item;
import tk.nukeduck.vivivox.player.Player;
import tk.nukeduck.vivivox.player.PlayerMovement;
import tk.nukeduck.vivivox.world.Chunk;
import tk.nukeduck.vivivox.world.World;

public class VivivoxMain {
	public static int renderDistance = 512;

	public static String textSeed;
	public static int seed;

	public static boolean hasStartedGame = false;

	public static void main(String[] args) {
		textSeed = args.length > 0 ? args[0] : null;
		seed = textSeed != null ? convertToNumber(textSeed) : random.nextInt(10000000);

		initDisplay();
		Logger.getLogger("Vivivox").log(Level.INFO, "Display initialisation complete.");
		initOpenGL();
		Logger.getLogger("Vivivox").log(Level.INFO, "OpenGL initialisation complete.");

		Fonts.initFonts();
		Logger.getLogger("Vivivox").log(Level.INFO, "Fonts initialisation complete.");

		VivivoxMain.make2D();
		Logger.getLogger("Vivivox").log(Level.INFO, "Beginning world generation...");
		world = new World();
		Logger.getLogger("Vivivox").log(Level.INFO, "World generation complete.");
		player = new Player(world);
		VivivoxMain.make3D();

		hasStartedGame = true;
		gameLoop();
	}

	public static int convertToNumber(String text) {
		String x = "";
		for(char i : text.toCharArray()) {
			x += (int) i;
		}
		return Integer.parseInt((x.length() > 8 ? x.substring(0, 8) : x));
	}

	public static void initDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(1280, 720));
			//Display.setFullscreen(true);

			Display.setTitle("Vivivox");
			//Display.setVSyncEnabled(true);
			Display.setInitialBackground(133F / 255F, 169F / 255F, 204F / 255F);
			Display.create();
			Mouse.setGrabbed(true);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static Image logo, crosshair;

	public static void initOpenGL() {
		try {
			logo = new Image("textures/logo.png");
			crosshair = new Image("textures/crosshair.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		Block.blockIconRegister();
		Item.itemIconRegister();

		glEnable(GL_TEXTURE_2D);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(60.0F, (float) Display.getWidth() / (float) Display.getHeight(), 0.1F, renderDistance);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);

		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}

    public static void make2D() {
        //Remove the Z axis
        glDisable(GL_DEPTH_TEST);
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
        glEnable(GL_BLEND);
    }

    public static void make3D() {
        //Restore the Z axis
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glEnable(GL_DEPTH_TEST);
    }

	public static final Random random = new Random();

	public static final int maxFps = 500;

	public static long lastTime, lastTime2;

	public static World world;
	public static Player player;

	public static ShaderProgram normal;
	public static ShaderProgram water;

	public static Gui gui = new Gui();

	public static void gameLoop() {
		lastTime = System.currentTimeMillis();
		lastTime2 = System.currentTimeMillis();
		secondTime = System.currentTimeMillis();

		normal = new ShaderProgram();
		normal.init("shader.vert", "shader.frag");
		shaderProgram = normal.getProgramId();

		water = new ShaderProgram();
		water.init("underwaterShader.vert", "underwaterShader.frag");
		underwaterShaderProgram = water.getProgramId();

		Render.initGraphics();

		gui.addElement(new GuiElementButton(100, 10, 128, 64));
		gui.addElement(new GuiElementButton(100, 90, 128, 64, "Custom text"));

		currentGui = gui; //new GuiInventory();

		while(!Display.isCloseRequested()) {
			if(System.currentTimeMillis() - lastTime > 10) {
				inputTick();
				lastTime = System.currentTimeMillis();
			}

			if(System.currentTimeMillis() - lastTime2 > (1000 / maxFps)) {
				renderTick();
				lastTime2 = System.currentTimeMillis();
			}
		}
		Display.destroy();
	}

	private static void renderTick() {
		renderDistance = 128;

		while(world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z) != null && world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z).isOpaque()) {
			player.position.y = (int) player.position.y + 1;
		}

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glPushMatrix(); {
			//GL20.glUseProgram(VivivoxMain.shaderProgram);

			if((Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_D)) && isOnGround) {
				glTranslatef((float) Math.sin(animationHelp * 4) / 32, (float) Math.sin(animationHelp * 8) / 16, 0.0F);
			}

			//world.render(new Vector3f(0.0F, 0.0F, 0.0F), 0.0F, 0.0F);
			//GL20.glUseProgram(VivivoxMain.shaderProgram);

			glRotatef(player.rotationPitch, 1, 0, 0);
			glRotatef(player.rotationYaw, 0, 1, 0);
			glTranslatef(-player.position.x, -(player.position.y + 1.75F), -player.position.z);

			glPushMatrix(); {
				world.render(new Vector3f(0.0F, 0.0F, 0.0F), 0.0F, 0.0F);
			}
			glPopMatrix();

			/*// SIDES
			glPushMatrix(); {
				world.render(new Vector3f(World.worldSize, 0.0F, 0.0F), 0.0F, 0.0F);
			}
			glPopMatrix();

			glPushMatrix(); {
				world.render(new Vector3f(-World.worldSize, 0.0F, 0.0F), 0.0F, 0.0F);
			}
			glPopMatrix();

			glPushMatrix(); {
				world.render(new Vector3f(0.0F, 0.0F, World.worldSize), 0.0F, 0.0F);
			}
			glPopMatrix();

			glPushMatrix(); {
				world.render(new Vector3f(0.0F, 0.0F, -World.worldSize), 0.0F, 0.0F);
			}
			glPopMatrix();
			//

			// CORNERS
			glPushMatrix(); {
				world.render(new Vector3f(World.worldSize, 0.0F, World.worldSize), 0.0F, 0.0F);
			}
			glPopMatrix();

			glPushMatrix(); {
				world.render(new Vector3f(-World.worldSize, 0.0F, -World.worldSize), 0.0F, 0.0F);
			}
			glPopMatrix();

			glPushMatrix(); {
				world.render(new Vector3f(-World.worldSize, 0.0F, World.worldSize), 0.0F, 0.0F);
			}
			glPopMatrix();

			glPushMatrix(); {
				world.render(new Vector3f(World.worldSize, 0.0F, -World.worldSize), 0.0F, 0.0F);
			}
			glPopMatrix();*/
		}
		glPopMatrix();

		renderOverlay();

		updateFps();
		Display.update();
	}

	public static Gui currentGui = null;

	public static void renderOverlay() {
		make2D();

		crosshair.draw((Display.getWidth() / 2) - (crosshair.getWidth() / 2), (Display.getHeight() / 2) - (crosshair.getHeight() / 2));
		logo.draw(Display.getWidth() - 105, 5, 100, 100);
		//World.cursor.draw(Mouse.getX(), Mouse.getY());
		Fonts.hudFont.getFont().drawString(10, 10, "FPS: " + displayFps);
		Fonts.hudFont.getFont().drawString(10, 15 + Fonts.hudFont.getHeight(), "X: " + player.position.x);
		Fonts.hudFont.getFont().drawString(10, 20 + (Fonts.hudFont.getHeight() * 2), "Y: " + player.position.y);
		Fonts.hudFont.getFont().drawString(10, 25 + (Fonts.hudFont.getHeight() * 3), "Z: " + player.position.z);
		Fonts.hudFont.getFont().drawString(Display.getWidth() / 2 - (Fonts.hudFont.getWidth("Vivivox 0.1.0") / 2), Display.getHeight() - 5 - Fonts.hudFont.getHeight(), "Vivivox 0.1.0");
		Fonts.hudFont.getFont().drawString(5, 200, "Light: " + world.getLightLevel((int) player.position.x, (int) player.position.y, (int) player.position.z));

		if(currentGui != null) currentGui.drawGUI();

		make3D();
	}

	public static float playerSpeed = 0.055F;
	public static float speedMultiplier = 1.0F;

	public static float mouseDampening = 0.6F;
	public static float mouseSpeed = 0.125F;

	public static float jumpHeight = 0.1F, gravity = 0.004F;

	public static float animationHelp;

	public static int updateCounter = 0;

	private static void inputTick() {
		updateCounter += 1;
		if(updateCounter == 10) {
			updateCounter = 0;
			world.update();
		}

		world.waveOffset += 0.02F;
		animationHelp += 0.02F;

		world.minX = (int) ((player.position.x - renderDistance) / Chunk.chunkSize);
		world.minY = (int) ((player.position.y - renderDistance) / Chunk.chunkSize);
		world.minZ = (int) ((player.position.z - renderDistance) / Chunk.chunkSize);

		if(world.minX < 0) world.minX = 0;
		if(world.minY < 0) world.minY = 0;
		if(world.minZ < 0) world.minZ = 0;

		world.maxX = (int) ((player.position.x + renderDistance) / Chunk.chunkSize);
		world.maxY = (int) ((player.position.y + renderDistance) / Chunk.chunkSize);
		world.maxZ = (int) ((player.position.z + renderDistance) / Chunk.chunkSize);

		if(world.maxX > world.chunks.length) world.maxX = world.chunks.length;
		if(world.maxY > world.chunks[0].length) world.maxY = world.chunks[0].length;
		if(world.maxZ > world.chunks[0][0].length) world.maxZ = world.chunks[0][0].length;

		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			PlayerMovement.walkForwards(playerSpeed * speedMultiplier, player);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			PlayerMovement.walkBackwards(playerSpeed * speedMultiplier, player);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			PlayerMovement.strafeLeft(playerSpeed * speedMultiplier, player);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			PlayerMovement.strafeRight(playerSpeed * speedMultiplier, player);
		}

		player.position.x = (player.position.x + world.worldSize) % world.worldSize;
		player.position.z = (player.position.z + world.worldSize) % world.worldSize;

		if(((world.getBlock((int) player.position.x, (int) player.position.y - 1, (int) player.position.z) != null && world.getBlock((int) player.position.x, (int) player.position.y - 1, (int) player.position.z).isOpaque()) ||
				(world.getBlock((int) (player.position.x - (2 * playerWidth)), (int) player.position.y - 1, (int) player.position.z) != null && world.getBlock((int) (player.position.x - (2 * playerWidth)), (int) player.position.y - 1, (int) player.position.z).isOpaque()) ||
				(world.getBlock((int) (player.position.x + (2 * playerWidth)), (int) player.position.y - 1, (int) player.position.z) != null && world.getBlock((int) (player.position.x + (2 * playerWidth)), (int) player.position.y - 1, (int) player.position.z).isOpaque()) ||
				(world.getBlock((int) player.position.x, (int) player.position.y - 1, (int) (player.position.z - (2 * playerWidth))) != null && world.getBlock((int) player.position.x, (int) player.position.y - 1, (int) (player.position.z - (2 * playerWidth))).isOpaque()) ||
				(world.getBlock((int) player.position.x, (int) player.position.y - 1, (int) (player.position.z + (2 * playerWidth))) != null && world.getBlock((int) player.position.x, (int) player.position.y - 1, (int) (player.position.z + (2 * playerWidth))).isOpaque())) && player.position.y % 1.0F == 0.0F) {
			isOnGround = true;
		} else {
			isOnGround = false;
		}

		if(isOnGround && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			yVel = jumpHeight;
			isOnGround = false;
		}

		if(!isOnGround) yVel -= gravity;
		else yVel = 0.0F;
		player.position.y += /*Keyboard.isKeyDown(Keyboard.KEY_SPACE) ? 0.1F : Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? -0.1F : 0.0F*/yVel;

		mouseAccelerationX += (float) Mouse.getDX() * mouseSpeed;
		mouseAccelerationY -= (float) Mouse.getDY() * mouseSpeed;

		mouseAccelerationX *= mouseDampening;
		mouseAccelerationY *= mouseDampening;

		player.rotationYaw += mouseAccelerationX;
		player.rotationPitch += mouseAccelerationY;

		if (player.rotationPitch > 90) {
			player.rotationPitch = 90;
		} else if (player.rotationPitch < -90) {
			player.rotationPitch = -90;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_M)) {
			jumpHeight += 0.001F;
			System.out.println("Jump Height: " + jumpHeight);
		} else if(Keyboard.isKeyDown(Keyboard.KEY_N)) {
			jumpHeight -= 0.001F;
			System.out.println("Jump Height: " + jumpHeight);
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_B)) {
			gravity += 0.001F;
			System.out.println("Gravity: " + gravity);
		} else if(Keyboard.isKeyDown(Keyboard.KEY_V)) {
			gravity -= 0.001F;
			System.out.println("Gravity: " + gravity);
		}

		for(Entity e : world.entities) {
			e.tick(world);
		}
	}

	public static float mouseAccelerationX = 0.0F, mouseAccelerationY = 0.0F;

	public static boolean isOnGround;
	public static float yVel;
	public static final float terminalVel = -0.5F;

	public static float playerWidth = 0.1F;

	public static int lastFps;

	public static int underwaterShaderProgram;

	public static int shaderProgram;

	public static long secondTime;
	public static int fpsSinceLast;
	public static int displayFps;

	public static void updateFps() {
		if(System.currentTimeMillis() >= secondTime + 1000) {
			secondTime = System.currentTimeMillis();
			displayFps = fpsSinceLast;
			fpsSinceLast = 0;
		} else {
			fpsSinceLast++;
		}
	}
}
