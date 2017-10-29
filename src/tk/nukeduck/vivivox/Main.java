package tk.nukeduck.vivivox;

import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import tk.nukeduck.vivivox.block.Block;
import tk.nukeduck.vivivox.player.Player;
import tk.nukeduck.vivivox.player.PlayerMovement;
import tk.nukeduck.vivivox.world.World;

public class Main {
	public static int renderDistance = 128;
	
	public static String textSeed;
	public static int seed;
	
	public static boolean hasStartedGame = false;
	
	public static void main(String[] args) {
		textSeed = args[0];
		seed = convertToNumber(textSeed);
		
		initDisplay();
		initOpenGL();
		
		Fonts.initFonts();
		
		world = new World();
		player = new Player(world);
		
		gameLoop();
	}
	
	public static int convertToNumber(String text) {
		String x = "";
		for(char i : text.toCharArray()) {
			x += (int) i;
		}
		return Integer.parseInt(x.substring(0, 8));
	}
	
	public static void initDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(1280, 720));
			Display.setTitle("Vivivox");
			//Display.setVSyncEnabled(true);
			Display.setInitialBackground(133F / 255F, 169F / 255F, 204F / 255F);
			Display.create();
			Mouse.setGrabbed(true);
		} catch(LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static Image logo;
	
	public static void initOpenGL() {
		try {
			logo = new Image("src/textures/logo.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		Block.blockIconRegister();
		
		glEnable(GL_TEXTURE_2D);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(60.0F, (float) Display.getWidth() / (float) Display.getHeight(), 0.1F, 64.0F);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		//glEnable(GL_COLOR_MATERIAL);
		
		//glDepthFunc(GL_LEQUAL);
		//glColorMaterial(GL_FRONT, GL_DIFFUSE);
	}
	
    public static void make2D() {
        //Remove the Z axis
        /*glDisable(GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
        glEnable(GL_BLEND);*/
    }
    
    public static void make3D() {
        //Restore the Z axis
        /*glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);*/
    }
	
	public static final Random random = new Random();
	
	public static final int maxFps = 60;
	
	public static long lastTime, lastTime2;
	
	public static World world;
	public static Player player;
	
	public static void gameLoop() {
		lastTime = System.currentTimeMillis();
		lastTime2 = System.currentTimeMillis();
		
		//make3D();
		
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
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glPushMatrix();
		{
			glRotatef(player.rotationPitch, 1, 0, 0);
			glRotatef(player.rotationYaw, 0, 1, 0);
			glTranslatef(-player.position.x, -(player.position.y + 1.75F), -player.position.z);
			//renderOverlay();
			/*glBegin(GL_QUADS);
			glColor3f(1.0f, 1.0f, 1.0f);
			glVertex3f(0.0F, 0.0F, 0.0F);
			glVertex3f(1.0F, 0.0F, 0.0F);
			glVertex3f(1.0F, 0.0F, 1.0F);
			glVertex3f(0.0F, 0.0F, 1.0F);
			glEnd();*/
			world.render(new Vector3f(0.0F, 0.0F, 0.0F), 0.0F, 0.0F);
		}
		glPopMatrix();
		
		System.out.println("Pos: " + player.position.x + ", " + player.position.y + ", " + player.position.z + " / " + player.rotationPitch + ", " + player.rotationYaw);
		
		//updateFps();
		Display.update();
	}
	
	public static void renderOverlay() {
		make2D();
		logo.draw(0, 0, 100, 100);
		Fonts.hudFont.getFont().drawString(0, 0, "Rotation: " + player.rotationPitch + ", " + player.rotationYaw);
		Fonts.hudFont.getFont().drawString(0, 20, "Position: " + player.position.x + ", " + player.position.y + ", " + player.position.z);
		make3D();
	}
	
	public static float playerSpeed = 0.1F;
	public static float speedMultiplier = 1.0F;
	
	private static void inputTick() {
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
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			yVel = 0.1F;
		}
		yVel -= 0.01F;
		player.position.y += yVel;
		
		player.rotationYaw += (float) Mouse.getDX() / 8;
		player.rotationPitch -= (float) Mouse.getDY() / 8;

		if (player.rotationPitch > 90) {
			player.rotationPitch = 90;
		} else if (player.rotationPitch < -90) {
			player.rotationPitch = -90;
		}
	}
	
	public void MoveY(Player player, World world) {
		float positionMovedTo = player.position.y + yVel;
		if(world.getBlock((int) player.position.x, (int) positionMovedTo, (int) player.position.z).isOpaque()) {
			
		}
	}
	
	public static float yVel;
	public static final float terminalVel = -0.5F;
	
	public static float playerWidth = 0.1F;

	public static int lastFps;
	
	public static void updateFps() {
		
	}
}