package tk.nukeduck.vivivox.helper;

import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;

import tk.nukeduck.vivivox.VivivoxMain;

public class Render
{
	private static Graphics g;
	
	public static void initGraphics(){
		g = new Graphics(Display.getWidth(), Display.getHeight());
	}
	
    public static void renderStatusBar(int x, int y, int width, int height, int value, int maxValue, Color color)
    {
    	g.setColor(new Color(color.r - 0.5F, color.g - 0.5F, color.b - 0.5F, color.a - 0.5F));
    	g.drawRect(x + 4, y + 4, width, height);
    	g.fillRect(x + 4, y + 4, Math.round(value * width / maxValue), height);
    	
    	g.setColor(color);
    	g.drawRect(x, y, width, height);
    	g.fillRect(x, y, Math.round(value * width / maxValue), height);
    }
    
    public static void renderFilledStatusBar(int x, int y, int width, int height, int value, int maxValue, Color color, Color backColor)
    {
    	g.setColor(new Color(backColor.r - 0.5F, backColor.g - 0.5F, backColor.b - 0.5F, backColor.a - 0.5F));
    	g.fillRect(x + 4, y + 4, width, height);
    	
    	g.setColor(new Color(color.r - 0.5F, color.g - 0.5F, color.b - 0.5F, color.a - 0.5F));
    	g.fillRect(x + 4, y + 4, Math.round(value * width / maxValue), height);
    	
    	
    	g.setColor(backColor);
    	g.fillRect(x, y, width, height);
    	
    	g.setColor(color);
    	g.fillRect(x, y, Math.round(value * width / maxValue), height);
    }
    
    public static void drawRectangle(int x, int y, int width, int height, Color color) {
    	g.setColor(color);
    	fillRect(x, y, width, height, color);
    }
    
    public static void drawBorderedRectangle(int x, int y, int width, int height, Color color, Color borderColor) {
    	g.setColor(color);
    	g.fillRect(x, y, width, height);
    	g.setColor(borderColor);
    	g.drawRect(x, y, width, height);
    }
    
    public static void drawString(int x, int y, String text, Color color, Font font) {
    	GL11.glColor4f(color.r, color.g, color.b, color.a);
    	font.drawString(x, y, text);
    }
    
    public static void fillRect(int x, int y, int width, int height, Color color) {
    	//GL11.glDisable(GL11.GL_TEXTURE_2D);
    	//GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    	GL11.glEnable(GL11.GL_COLOR);
    	GL11.glEnable(GL_COLOR_MATERIAL);
    	GL11.glColor3f(color.r, color.g, color.b);
    	VivivoxMain.logo.draw(0, 0);
    	GL11.glBegin(GL11.GL_QUADS); {
    		GL11.glVertex2f(x, y);
    		GL11.glTexCoord2f(0.0F, 0.0F);
    		GL11.glVertex2f(x + width, y);
    		GL11.glTexCoord2f(1.0F, 0.0F);
    		GL11.glVertex2f(x + width, y + height);
    		GL11.glTexCoord2f(1.0F, 1.0F);
    		GL11.glVertex2f(x, y + height);
    		GL11.glTexCoord2f(0.0F, 1.0F);
    	}
    	GL11.glEnd();
    }
}
